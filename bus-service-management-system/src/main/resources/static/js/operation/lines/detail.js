$(document).ready(function () {

    // 利用期間変更処理
    refreshBusStopList();

    // selectBox更新
    $('#serviceStartDate').on('change', () => refreshBusStopList());
    $('#serviceEndDate').on('change', () => refreshBusStopList());

    // 「追加・削除」ボタン制御
    const $tableBody = $('.busStopTableBody');

    $tableBody.on('click', 'input[type="button"]', function () {
        const $row = $(this).closest('tr');
        const action = $(this).val();

        if (action === '追加') {
            handleAddRow($row);
        } else if (action === '削除') {
            handleDeleteRow($row);
        }
    });

    // ラジオボタン値設定
    $tableBody.on('change', 'input[type=radio][name$=".rideAlightDivision"]', function () {
        const $radio = $(this);
        const selectedValue = $radio.val();

        const $row = $radio.closest('tr');

        // hidden input 更新
        $row.find('input[type=hidden][name$=".rideAlightDivision"]').val(selectedValue);
    });
});

/** 行を追加する処理 */
function handleAddRow($targetRow) {
    destroySelect2(); // select2 を一時的に解除

    const $tbody = $targetRow.closest('tbody');
    const $newRow = $targetRow.clone(true); // イベント付きで行を複製
    $newRow.find('input[type="hidden"][name$=".rosenBusStopId"]').remove();

    // select 要素を取得
    const $select = $newRow.find('select.selectBox');
    const nameInfo = getBaseName($select.attr('name'));

    // 行を直後に追加
    $targetRow.after($newRow);

    $tbody.find('tr').each(function (index) {
        const $row = $(this);

        // 順番 <span> と hidden を更新
        const $sequenceTd = $row.find('td:first');
        $sequenceTd.find('span').text(index + 1);

        const $hiddenInputSequenceNo = $sequenceTd.find('input[type=hidden][name$=".sequenceNo"]');
        if (nameInfo && $hiddenInputSequenceNo.length > 0) {
            $hiddenInputSequenceNo.attr('name', `${nameInfo.base}[${index}].sequenceNo`);
            $hiddenInputSequenceNo.val(index + 1);
        }

        // selectBox name 属性
        const $selectBox = $row.find('select.selectBox');
        if (nameInfo) {
            $selectBox.attr('name', `${nameInfo.base}[${index}].${nameInfo.property}`);
        }

        // busStopName hidden 再設定
        const $busStopNameInput = $row.find('input[type=hidden][name$=".busStopName"]');
        if (nameInfo && $busStopNameInput.length > 0) {
            $busStopNameInput.attr('name', `${nameInfo.base}[${index}].busStopName`);
            if ($row.is($newRow)) {
                // 値初期化
                $busStopNameInput.val('');
            }
        }

        // busStopId hidden 再設定
        const $hiddenBusStopId = $row.find('input[type=hidden][name$=".busStopId"]');
        if (nameInfo && $hiddenBusStopId.length > 0) {
            $hiddenBusStopId.attr('name', `${nameInfo.base}[${index}].busStopId`);
            if ($row.is($newRow)) {
                $hiddenBusStopId.val('');
            }
        }

        // rosenBusStopId hidden 再設定
        const $hiddenRosenId = $row.find('input[type=hidden][name$=".rosenBusStopId"]');
        if (nameInfo && $hiddenRosenId.length > 0) {
            $hiddenRosenId.attr('name', `${nameInfo.base}[${index}].rosenBusStopId`);
        }

        // rideAlightDivision hidden 再設定
        const $hiddenRideAlightDivision = $row.find('input[type=hidden][name$=".rideAlightDivision"]');
        if (nameInfo && $hiddenRideAlightDivision.length > 0) {
            $hiddenRideAlightDivision.attr('name', `${nameInfo.base}[${index}].rideAlightDivision`);
            if ($row.is($newRow)) {
                $hiddenRideAlightDivision.val('2');
            }
        }

        // ラジオボタンの name 属性を再設定
        $row.find('input[type=radio]').each(function () {
            $(this).attr('name', `${nameInfo.base}[${index}].rideAlightDivision`);
        });

        // 新しい行の disabled を解除
        if ($row.is($newRow)) {
            $row.find(':input:disabled').prop('disabled', false);
            $row.find('input[type="button"][value="削除"]').css('width', '60px').show();
            $selectBox.val("0");
        }
    });

    // selectBox再設定
    refreshBusStopList();

    // ラジオボタンレイアウト調整
    syncRadioWithHidden();

    // select2 を再初期化
    initSelect2();
}

/** 行を削除する処理 */
function handleDeleteRow($targetRow) {
    const $tbody = $targetRow.closest('tbody');
    const rowCount = $tbody.find('tr').length;

    // 行が1つしかない場合は削除しない
    if (rowCount <= 1) {
        return;
    }

    $targetRow.remove(); // 対象の行を削除

    // 残りの行の順番と name 属性を再設定
    $tbody.find('tr').each(function (index) {
        const $row = $(this);

        const $select = $row.find('select.selectBox');
        const nameInfo = getBaseName($select.attr('name'));

        // 順番 <span> と hidden を更新
        const $sequenceTd = $row.find('td:first');
        $sequenceTd.find('span').text(index + 1);

        const $hiddenInputSequenceNo = $sequenceTd.find('input[type=hidden][name$=".sequenceNo"]');
        if (nameInfo && $hiddenInputSequenceNo.length > 0) {
            $hiddenInputSequenceNo.attr('name', `${nameInfo.base}[${index}].sequenceNo`);
            $hiddenInputSequenceNo.val(index + 1);
        }

        // select の name 属性を再設定
        if (nameInfo) {
            $select.attr('name', `${nameInfo.base}[${index}].${nameInfo.property}`);
        }

        // busStopNameの hidden name を再設定
        const $hiddenInputBusStopName = $row.find('input[type=hidden][name$=".busStopName"]');
        if (nameInfo && $hiddenInputBusStopName.length > 0) {
            $hiddenInputBusStopName.attr('name', `${nameInfo.base}[${index}].busStopName`);
        }

        // busStopNameの hidden name を再設定
        const $hiddenBusStopId = $row.find('input[type=hidden][name$=".busStopId"]');
        if (nameInfo && $hiddenBusStopId.length > 0) {
            $hiddenBusStopId.attr('name', `${nameInfo.base}[${index}].busStopId`);
        }

        // rosenBusStopId hidden name を再設定
        const $hiddenRosenId = $row.find('input[type=hidden][name$=".rosenBusStopId"]');
        if (nameInfo && $hiddenRosenId.length > 0) {
            $hiddenRosenId.attr('name', `${nameInfo.base}[${index}].rosenBusStopId`);
        }

        // rideAlightDivision hidden name を再設定
        const $hiddenRideAlightDivision = $row.find('input[type=hidden][name$=".rideAlightDivision"]');
        if (nameInfo && $hiddenRideAlightDivision.length > 0) {
            $hiddenRideAlightDivision.attr('name', `${nameInfo.base}[${index}].rideAlightDivision`);
        }

        // ラジオボタンの name 属性を再設定
        $row.find('input[type=radio]').each(function () {
            $(this).attr('name', `${nameInfo.base}[${index}].rideAlightDivision`);
        });
    });
}

/** selectのname属性からベース名を取得する関数 */
function getBaseName(nameAttr) {
    const match = nameAttr.match(/^([^\[]+)\[\d+\]\.(.+)$/);
    if (match) {
        return {
            base: match[1],     // "lineBusStopsList"
            property: match[2]  // "busStopId" or "rideAlightDivision"
        };
    }
    return null;
}

/** AjaxCall後selectBox更新 */
function refreshBusStopList() {

    const startDate = $('#serviceStartDate').val();
    const endDate = $('#serviceEndDate').val();
    const corpId = $('#businessCorpId').val();

    // 両方の値が空の場合は処理を中止
    if (!startDate && !endDate) {
        return;
    }

    const json = {
        "serviceStartDate": startDate,
        "serviceEndDate": endDate,
        "businessCorpId": corpId
    };

    connectAjax(GET_BUS_STOP_LIST, json);
}

/** selectBox更新 */
function updateBusStopSelectOptions(data) {
    // データが null または undefined の場合は処理を中止
    if (!data) {
        return;
    }

    destroySelect2(); // select2 を一度解除

    $("select.select2:not([disabled])").each(function () {
        const $select = $(this);
        const currentValue = $select.val(); // 現在選択されている値を取得
        let matchFound = false;

        // 既存のオプションをすべて削除
        $select.empty();

        // デフォルトの選択肢（value = 0）を追加
        $select.append($('<option>', {
            value: 0,
            text: '---------------'
        }));

        // サーバーから受け取ったデータでオプションを追加
        data.forEach(item => {
            const isSelected = String(item.busStopId) === String(currentValue);
            if (isSelected) matchFound = true;

            const $option = $('<option>', {
                value: item.busStopId,
                text: item.busStopName,
                selected: isSelected,
                'data-latlon': item.latitudeLongitude
            });

            $select.append($option);

        });

        // 一致する値が存在しない場合は、デフォルトの選択状態（value = 0）に設定
        if (!matchFound) {
            $select.val("0");
        }
    });

    // 既存停留所にdata-latlon設定
    $("select.select2[disabled]").each(function () {
        const $select = $(this);

        // 既存 option の value と一致するデータを探して data-latlon を更新
        $select.find("option").each(function () {
            const $option = $(this);
            const val = $option.val();

            // value に対応する item を検索
            const item = data.find(d => String(d.busStopId) === String(val));
            if (item) {
                $option.attr("data-latlon", item.latitudeLongitude);
            }
        });
    });

    initSelect2(); // select2 を再初期化
}

/** 停留所名・シーケンス設定 */
$('form').on('submit', function(event) {
    // 各行をループ
    $('.busStopTableBody tr').each(function(index) {
        const $row = $(this);

        // select から選択されたテキストを取得
        const selectedText = $row.find('select.selectBox option:selected').text();

        // busStopNameをhiddenInputに設定
        const $busStopNameInput = $row.find('input[type=hidden][name$=".busStopName"]');
        if ($busStopNameInput.length) {
            $busStopNameInput.val(selectedText);
        }

        // sequenceNo を span から取得して hidden input に設定
        const sequenceText = $row.find('td:first span').text().trim();
        const $sequenceInput = $row.find('input[type=hidden][name$=".sequenceNo"]');
        if ($sequenceInput.length) {
            $sequenceInput.val(sequenceText);
        }
    });
});

/** ラジオボタンレイアウト調整 */
function syncRadioWithHidden() {
    $('.busStopTableBody tr').each(function () {
        const $row = $(this);

        const hiddenValue = $row.find('input[type=hidden][name$=".rideAlightDivision"]').val();

        // ラジオボタンチェック解除
        $row.find('input[type=radio][name$=".rideAlightDivision"]').prop('checked', false);

        // ラジオボタン再度チェック
        $row.find(`input[type=radio][value="${hiddenValue}"]`).prop('checked', true);
    });
}

/** OpenStreetMap START */

let map;

/** OpenStreetMap(Leaflet) 初期化 */
function initMap() {
    $('#map').show();

    const results = [];

    $('tr').each(function () {
        const $tr = $(this);

        const $selectedOption = $tr.find('select[name*="busStopId"] option:selected');

        // 順番
        const sequenceNo = +($tr.find('input[name*="sequenceNo"]').attr('name')?.match(/\[(\d+)]/)[1]) + 1;
        if (!sequenceNo) return;
        // 緯度経度
        const latlon = $selectedOption.data('latlon');
        //停留所名
        const busStopName = $selectedOption.text();

        results.push({
            sequenceNo,
            latlon,
            busStopName
        });
    });

    const validResults = results.filter(item => item.latlon);

    if (map) {
        map.remove();
    }

    // 地図生成（初期は東京中心）
    map = L.map($('#map').get(0)).setView([35.6895, 139.6917], 15);

    // OSM タイルレイヤ
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    if (validResults.length === 0) return;

    const latlngs = [];

    validResults.forEach(item => {
        const [lat, lng] = item.latlon.split(',').map(parseFloat);
        latlngs.push([lat, lng]);

        // マーカー作成とsequenceNo表示
        const marker = L.marker([lat, lng]).addTo(map);

        marker.bindTooltip(`順番: ${item.sequenceNo}<br>停留所名: ${item.busStopName}`, {
            permanent: true,
            direction: 'top',
            offset: [0, -10]
        });
    });

    // 中心点とズーム調整
    if (latlngs.length === 1) {
        // マーカー1つならその位置を中心に
        map.setView(latlngs[0], 15);
    } else {
        // 複数ならすべてが見えるように調整
        const bounds = L.latLngBounds(latlngs);
        map.fitBounds(bounds, { padding: [50, 50] });
    }
}

/** OpenStreetMap END */
