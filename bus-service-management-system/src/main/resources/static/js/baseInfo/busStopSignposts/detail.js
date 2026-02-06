let bssize = 1;  // 停留所の数
let bsmap;       // Googleマップのインスタンス(停留所)
let bsgeocoder;  // 住所から座標を取得するためのジオコーダー(停留所)
let bsmarker;    // 地図上に表示するマーカー(停留所)
let spsize = 1;  // 標柱の数
let spmap;       // Googleマップのインスタンス(標柱)
let spgeocoder;  // 住所から座標を取得するためのジオコーダー(標柱)
let spmarker;    // 地図上に表示するマーカー(標柱)

function setBtn(btnNo) {
    // 有効になっている「標柱の追加」ボタンの設定
    document.getElementById('pressDownAddSignpostsButtonNo').value = btnNo;
}

function initMap() {
    // 停留所の数の取得
    bssize = document.getElementById("busStopSize").value;
    bsmap = Array(bssize);
    bsgeocoder = Array(bssize);
    bsmarker = Array(bssize);
    spsize = Array(bssize);
    spmap = Array(bssize);
    spgeocoder = Array(bssize);
    spmarker = Array(bssize);

    // 停留所のレコード数だけループ
    for (let i=0; i<bssize; i++) {
        bsmap[i] = undefined;
        bsgeocoder[i] = undefined;
        bsmarker[i] = undefined;

        let busstopId;
        if (i < 9) {
            busstopId = "0" + (i+1);
        } else {
            busstopId = "" + (i+1);
        }

        spsize[i] = document.getElementById("signpostsSize" + busstopId).value;
        spmap[i] = Array(spsize[i]);
        spgeocoder[i] = Array(spsize[i]);
        spmarker[i] = Array(spsize[i]);

        // *** 停留所のイベント処理 *** //
        // 地図を初期化（東京を中心にズーム16）
        bsmap[i] = new google.maps.Map(document.getElementById("bsmap" + busstopId), {
            center: { lat: 35.6895, lng: 139.6917 }, // 東京
            zoom: 16,
        });

        // ジオコーダーを作成
        bsgeocoder[i] = new google.maps.Geocoder();

        // 「検索」ボタンがクリックされたときの処理
        document.getElementById("busStopSearchBtn" + busstopId).addEventListener("click", function () {
            const address = document.getElementById("busStopAddressInput" + busstopId).value;
            if (!address.trim()) {
                alert("住所を入力してください"); // 空欄チェック
                return;
            }

            // 住所から座標を取得
            bsgeocoder[i].geocode({ address: address }, function (results, status) {
                if (status === "OK") {
                    const location = results[0].geometry.location;
                    const lat = location.lat().toFixed(12); // 緯度（小数点12桁）
                    const lng = location.lng().toFixed(12); // 経度（小数点12桁）
                    setBusStopMarker(location, `: ${lat},${lng}`, i); // マーカーを設定
                    document.getElementById("latitudeLongitude" + busstopId).value = `${lat},${lng}`; // 座標を入力欄に表示
                } else {
                    alert("現在地を取得できませんでした: " + status); // エラー表示
                }
            });
        });

        // 地図をクリックしたときの処理
        bsmap[i].addListener("click", function (event) {
            try {
                const lat = parseFloat(event.latLng.lat().toFixed(12)); // 緯度
                const lng = parseFloat(event.latLng.lng().toFixed(12)); // 経度
                const location = { lat: lat, lng: lng };
                setBusStopMarker(location, `: ${lat},${lng}`, i); // マーカーを設定
                document.getElementById("latitudeLongitude" + busstopId).value = `${lat},${lng}`; // 座標を入力欄に表示
            } catch (error) {
                alert("位置情報の取得に失敗しました: " + error.message); // エラー処理
            }
        });

        // ページ読み込み時に座標が入力されていればマーカーを表示
        const latitudeLongitudeValue = document.getElementById("latitudeLongitude" + busstopId).value.trim();
        if (latitudeLongitudeValue) {
            const parts = latitudeLongitudeValue.split(",");
            if (parts.length === 2) {
                const lat = parseFloat(parts[0]);
                const lng = parseFloat(parts[1]);
                if (!isNaN(lat) && !isNaN(lng)) {
                    const location = { lat: lat, lng: lng };
                    setBusStopMarker(location, `${lat},${lng}`, i); // マーカーを設定
                }
            }
        }

        // 座標入力欄からフォーカスが外れたときの処理
        document.getElementById("latitudeLongitude" + busstopId).addEventListener("blur", function () {
            const value = this.value.trim();
            if (!value) return;

            const parts = value.split(",");
            if (parts.length !== 2) return;

            const lat = parseFloat(parts[0]);
            const lng = parseFloat(parts[1]);

            if (isNaN(lat) || isNaN(lng)) return;

            const location = { lat: lat, lng: lng };
            setBusStopMarker(location, `${lat},${lng}`, i); // マーカーを設定
        });

        // *** 標柱のイベント処理 *** //
        // 標柱のレコード数だけループ
        for (let j=0; j<spsize[i]; j++) {
            spmap[i][j] = undefined;
            spgeocoder[i][j] = undefined;
            spmarker[i][j] = undefined;

            let signpostsId;
            if (j < 9) {
                signpostsId = "0" + (j+1);
            } else {
                signpostsId = "" + (j+1);
            }

            // 地図を初期化（東京を中心にズーム16）
            spmap[i][j] = new google.maps.Map(document.getElementById("spmap" + busstopId + signpostsId), {
                center: { lat: 35.6895, lng: 139.6917 }, // 東京
                zoom: 16,
            });

            // ジオコーダーを作成
            spgeocoder[i][j] = new google.maps.Geocoder();

            // 「検索」ボタンがクリックされたときの処理
            document.getElementById("signpostSearchBtn" + busstopId + signpostsId).addEventListener("click", function () {
                const address = document.getElementById("SignpostAddressInput" + busstopId + signpostsId).value;
                if (!address.trim()) {
                    alert("住所を入力してください"); // 空欄チェック
                    return;
                }

                // 住所から座標を取得
                spgeocoder[i][j].geocode({ address: address }, function (results, status) {
                    if (status === "OK") {
                        const location = results[0].geometry.location;
                        const lat = location.lat().toFixed(12); // 緯度（小数点12桁）
                        const lng = location.lng().toFixed(12); // 経度（小数点12桁）
                        setSignpostsMarker(location, `: ${lat},${lng}`, i, j); // マーカーを設定
                        document.getElementById("latitudeLongitude" + busstopId + signpostsId).value = `${lat},${lng}`; // 座標を入力欄に表示
                    } else {
                        alert("現在地を取得できませんでした: " + status); // エラー表示
                    }
                });
            });

            // 地図をクリックしたときの処理
            spmap[i][j].addListener("click", function (event) {
                try {
                    const lat = parseFloat(event.latLng.lat().toFixed(12)); // 緯度
                    const lng = parseFloat(event.latLng.lng().toFixed(12)); // 経度
                    const location = { lat: lat, lng: lng };
                    setSignpostsMarker(location, `: ${lat},${lng}`, i, j); // マーカーを設定
                    document.getElementById("latitudeLongitude" + busstopId + signpostsId).value = `${lat},${lng}`; // 座標を入力欄に表示
                } catch (error) {
                    alert("位置情報の取得に失敗しました: " + error.message); // エラー処理
                }
            });

            // ページ読み込み時に座標が入力されていればマーカーを表示
            const latitudeLongitudeValue = document.getElementById("latitudeLongitude" + busstopId + signpostsId).value.trim();
            if (latitudeLongitudeValue) {
                const parts = latitudeLongitudeValue.split(",");
                if (parts.length === 2) {
                    const lat = parseFloat(parts[0]);
                    const lng = parseFloat(parts[1]);
                    if (!isNaN(lat) && !isNaN(lng)) {
                        const location = { lat: lat, lng: lng };
                        setSignpostsMarker(location, `${lat},${lng}`, i, j); // マーカーを設定
                    }
                }
            }

            // 座標入力欄からフォーカスが外れたときの処理
            document.getElementById("latitudeLongitude" + busstopId + signpostsId).addEventListener("blur", function () {
                const value = this.value.trim();
                if (!value) return;

                const parts = value.split(",");
                if (parts.length !== 2) return;

                const lat = parseFloat(parts[0]);
                const lng = parseFloat(parts[1]);

                if (isNaN(lat) || isNaN(lng)) return;

                const location = { lat: lat, lng: lng };
                setSignpostsMarker(location, `${lat},${lng}`, i, j); // マーカーを設定
            });
        }
    }
}

// マーカーを地図に表示する関数(停留所用)
function setBusStopMarker(location, label, num) {
    // 停留所のレコード数だけループ
    let marker = bsmarker[num];
    if (marker) marker.setMap(null); // 既存のマーカーを削除
    marker = new google.maps.Marker({
        map: bsmap[num],
        position: location,
        title: label,
    });
    bsmarker[num] = marker;
    bsmap[num].setCenter(location); // 地図の中心をマーカー位置に移動
}

// マーカーを地図に表示する関数(標柱用)
function setSignpostsMarker(location, label, bsnum, spnum) {
    // 停留所のレコード数だけループ
    let marker = spmarker[bsnum][spnum];
    if (marker) marker.setMap(null); // 既存のマーカーを削除
    marker = new google.maps.Marker({
        map: spmap[bsnum][spnum],
        position: location,
        title: label,
    });
    spmarker[bsnum][spnum] = marker;
    spmap[bsnum][spnum].setCenter(location); // 地図の中心をマーカー位置に移動
}

// ページ読み込み時に地図を初期化
window.onload = initMap;
