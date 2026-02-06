var vehicleMap;

$(function () {
    if ($("#vehicleMap").length) {
        buildMap();
    }
});

/** 地図を生成 */
function buildMap() {
    var $el = $("#vehicleMap");
    if ($el.length === 0) {
        return;
    }

    var ll = parseLatLng($el.data("latlng"));

    if (!ll) {
        return;
    }

    var created = $el.data("created") || "";

    if (vehicleMap) {
        vehicleMap.remove();
    }

    vehicleMap = L.map($el.get(0)).setView([ll.lat, ll.lng], 17);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 20,
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(vehicleMap);

    var marker = L.marker([ll.lat, ll.lng]).addTo(vehicleMap);

    var popupText = "現在位置";
    if (created) {
        popupText += "<br/>取得時刻: " + created;
    }

    marker.bindPopup(popupText);
}

/** 緯度。経度文字列を{lat, lng}に変換 */
function parseLatLng(latlngStr) {
    if (!latlngStr || typeof latlngStr !== "string") {
        return null;
    }
    var parts = $.map(latlngStr.split(","), function (s) {
        return $.trim(s);
    });
    if (parts.length !== 2) {
        return null;
    }

    var lat = parseFloat(parts[0]);
    var lng = parseFloat(parts[1]);
    if (!isFinite(lat) || !isFinite(lng)) {
        return null;
    }

    return {lat: lat, lng: lng};
}
