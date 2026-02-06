let map;        // Googleマップのインスタンス
let geocoder;   // 住所から座標を取得するためのジオコーダー
let marker;     // 地図上に表示するマーカー

function initMap() {
    // 地図を初期化（東京を中心にズーム16）
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 35.6895, lng: 139.6917 }, // 東京
        zoom: 16,
    });

    // ジオコーダーを作成
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
