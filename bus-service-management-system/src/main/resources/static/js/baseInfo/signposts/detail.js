let map;        // Googleマップのインスタンス
let geocoder;   // 住所から座標を取得するためのジオコーダー
let marker;     // 地図上に表示するマーカー
// 停留所セレクトボックス
const $busStopSelect = $('#busStopId');
// 停留所の緯度・経度を表示するためのラベル要素
const $labelSpan = $('#selectedBusStopLatLng');
// 停留所名 input
const $signpostNameInput = $('input[name="signpostName"]');
// 停留所名かな input
const $signpostKanaNameInput = $('input[name="signpostKanaName"]');
// 停留所名英文 input
const $signpostEnNameInput = $('input[name="signpostEnName"]');
// 緯度,経度 input
const $latitudeLongitudeInput = $('input[name="latitudeLongitude"]');

function initMap() {
    // 地図を初期化（東京を中心にズーム16）
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 35.6895, lng: 139.6917 }, // 東京の緯度経度
        zoom: 16,
    });

    // Geocoderオブジェクトの作成（住所検索用）
    geocoder = new google.maps.Geocoder();

    // 「検索」ボタンがクリックされたときの処理
    document.getElementById("searchBtn").addEventListener("click", function () {
        const address = document.getElementById("addressInput").value;
        if (!address.trim()) {
            alert("住所を入力してください"); // 空欄チェック
            return;
        }

        // 住所から座標を取得
        geocoder.geocode({ address: address }, function (results, status) {
            if (status === "OK") {
                const location = results[0].geometry.location;
                const lat = location.lat().toFixed(12); // 緯度（小数点12桁）
                const lng = location.lng().toFixed(12); // 経度（小数点12桁）
                setMarker(location, `: ${lat},${lng}`); // マーカーを設定
                document.getElementById("latitudeLongitude").value = `${lat},${lng}`; // 座標を入力欄に表示
            } else {
                alert("現在地を取得できませんでした: " + status); // エラー表示
            }
        });
    });

    // 地図をクリックしたときの処理
    map.addListener("click", function (event) {
        try {
            const lat = parseFloat(event.latLng.lat().toFixed(12)); // 緯度
            const lng = parseFloat(event.latLng.lng().toFixed(12)); // 経度
            const location = { lat: lat, lng: lng };
            setMarker(location, `: ${lat},${lng}`); // マーカーを設定
            document.getElementById("latitudeLongitude").value = `${lat},${lng}`; // 座標を入力欄に表示
        } catch (error) {
            alert("位置情報の取得に失敗しました: " + error.message); // エラー処理
        }
    });

    // ページ読み込み時に座標が入力されていればマーカーを表示
    const latitudeLongitudeValue = document.getElementById("latitudeLongitude").value.trim();
    if (latitudeLongitudeValue) {
        const parts = latitudeLongitudeValue.split(",");
        if (parts.length === 2) {
            const lat = parseFloat(parts[0]);
            const lng = parseFloat(parts[1]);
            if (!isNaN(lat) && !isNaN(lng)) {
                const location = { lat: lat, lng: lng };
                setMarker(location, `${lat},${lng}`); // マーカーを設定
            }
        }
    }

    // 座標入力欄からフォーカスが外れたときの処理
    document.getElementById("latitudeLongitude").addEventListener("blur", function () {
        const value = this.value.trim();
        if (!value) return;

        const parts = value.split(",");
        if (parts.length !== 2) return;

        const lat = parseFloat(parts[0]);
        const lng = parseFloat(parts[1]);

        if (isNaN(lat) || isNaN(lng)) return;

        const location = { lat: lat, lng: lng };
        setMarker(location, `${lat},${lng}`); // マーカーを設定
    });
}

// マーカーを地図に表示する関数
function setMarker(location, label) {
    if (marker) marker.setMap(null); // 既存のマーカーを削除
    marker = new google.maps.Marker({
        map: map,
        position: location,
        title: label,
    });
    map.setCenter(location); // 地図の中心をマーカー位置に移動
}

// ページ読み込み時に地図を初期化
window.onload = initMap;

// 画面ロード時に表示
setSelectedBusStopLatLngFromSelect(false);

// セレクトボックスで選択されている停留所の緯度・経度をラベルに反映する
function setSelectedBusStopLatLngFromSelect(isBusStopSelectChanged) {

    if ($busStopSelect.length === 0 || $labelSpan.length === 0) {
        return;
    }
    const $opt = $busStopSelect.find('option:selected');
    if ($opt.length === 0) {
        $labelSpan.text('');
        return;
    }
    const latlng = $opt.data('latlng');
    if (latlng && $.trim(latlng) !== '') {
        $("#preBtn").show();
        // 取得した緯度,経度を input(latitudeLongitude) に設定
        $latitudeLongitudeInput.val(latlng);
        $labelSpan.text('※停留所の緯度・経度：' + latlng);
    } else {
        $("#preBtn").hide();
        $latitudeLongitudeInput.val('');
        $labelSpan.text(isBusStopSelectChanged
            ? '※停留所の緯度・経度：-'
            : '');
    }

    if (isBusStopSelectChanged) {
        // 取得した各種停留所名を設定
        const nameText = $.trim($opt.text());
        if (!isPlaceholder(nameText)) {
            $signpostNameInput.val(nameText);
        } else {
            $signpostNameInput.val('');
        }
        $signpostKanaNameInput.val($.trim($opt.data('busStopKanaName')));
        $signpostEnNameInput.val($.trim($opt.data('busStopEnName')));
    }
}

function isPlaceholder(text) {
    const t = (text || '').trim();
    return t === '' || /^-+$/.test(t);
}


// 停留所セレクトボックスを変更した時にラベルを更新
$busStopSelect.on('change', function () {
    setSelectedBusStopLatLngFromSelect(true);
});

/** OpenStreetMap START */

let mapWin = null;

function openMapWindow() {
    // 緯度、経度が取得できない場合終了
    const spanText = $labelSpan.text();
    if (!(spanText && spanText.trim() !== '' && spanText.split('：')[1] !== '-')) { return; }
    // 緯度、経度の情報を切り出す
    const latlngValue = spanText.split('：')[1];

    // 既存のウィンドウが開いている場合は閉じる
    if (mapWin && !mapWin.closed) {
        mapWin.close();
    }

    // 新しいウィンドウを開く
    mapWin = window.open("", "_blank", "width=1400,height=900");

    mapWin.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>OpenStreetMap</title>
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                #map { width: 100%; height: 100vh; }
                body { margin: 0; }
            </style>
        </head>
        <body>
            <div id="map"></div>

            <script>
                const latlng = "${latlngValue}";
                const [lat, lng] = latlng.split(',').map(Number);

                const map = L.map('map').setView([lat, lng], 15);

                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);

                L.marker([lat, lng]).addTo(map);
            </script>
        </body>
        </html>
    `);

    mapWin.document.close();
}

/** OpenStreetMap END */
