/** 乗降区分CONSTANTS */
const RIDE_ALIGHT_CONSTANTS = {
    COLORS: {
        RIDE: '#87CEEB',        // 乗車
        ALIGHT: '#FFC0CB',      // 降車
        RIDE_ALIGHT: '#FFFFE0'  // 乗降車
    },
    TEXTS: {
        RIDE: '乗車',
        ALIGHT: '降車',
        RIDE_ALIGHT: '乗降車'
    }
};

$(document).ready(function () {

    // 路線停留所テーブルの表示を初期化
    refreshLineBusStopList();

    // ページロード時にラジオボタンのdisabledを設定
    initializeDisabledByOriginalValue();

    // 路線選択・利用期間の変更時に、路線停留所リストを再取得
    $('#rosenId').on('change', () => refreshLineBusStopList());
    $('#serviceStartDate').on('change', () => refreshLineBusStopList());
    $('#serviceEndDate').on('change', () => refreshLineBusStopList());

    // 運行区分（上り／下り）変更時の処理
    $('input[name="lineDetailServiceDivision"]').on('change', function () {
        if ($('.busStopTableBody tr').length > 0) {
            refreshRideAlightDivision(); // 停留所テーブルの乗降区分表示更新
        }
        if ($('.registerBusStopTableBody tr').length > 0) {
            refreshRegisterRideAlightDivision(); // 登録済停留所のラジオボタン更新
        }
    });

    // 「追加」ボタンが押されたときのイベント
    $(document).on('click', '.busStopTableBody input[type="button"][value="追加"]', function () {
        const $row = $(this).closest('tr'); // ボタンが押された行を取得
        const sequenceNo = $('.registerBusStopTableBody tr').length + 1; // 登録順を決定
        const busStopName = $row.find('td').eq(1).text().trim(); // 停留所名
        const rideValue = parseInt($row.find('td').eq(2).data('ride')); // 元の乗降区分
        const rosenBusStopId = $row.find('input[type="hidden"][name="rosenBusStopId"]').val();
        const signpostId = $row.find('input[type="hidden"][name="signpostId"]').val();// 標柱ID

        // 行を登録済テーブルに追加
        addRegisterRow(sequenceNo, busStopName, rideValue, rosenBusStopId, signpostId);
    });

    // 「削除」ボタンが押されたときのイベント
    $(document).on('click', '.registerBusStopTableBody input[type="button"]', function () {
        if ($(this).val() === '削除') {
            const $targetRow = $(this).closest('tr');
            deleteRegisterRow($targetRow);
        }
    });

    // ラジオボタンが変更されたとき、hiddenフィールドに選択値を反映
    $(document).on('change', '.registerBusStopTableBody input[type="radio"]', function () {
        const $radio = $(this);
        const value = $radio.val();
        const $row = $radio.closest('tr');
        $row.find('input[type="hidden"][name$=".rideAlightDivision"]').val(value);
    });

});

/** 行追加 */
function addRegisterRow(sequenceNo, busStopName, originalRideValue, rosenBusStopId, signpostId) {
    const $tbody = $('.registerBusStopTableBody');
    const index = sequenceNo - 1;
    const nameBase = `lineRouteBusStopList[${index}]`; // フォームのname属性のベース

    const isDown = $('input[name="lineDetailServiceDivision"]:checked').val() === "0"; // 下り判定
    const labelTexts = isDown ? ['降車', '乗車', '乗降車'] : ['乗車', '降車', '乗降車']; // 表示順変更

    const textToValue = {
        '乗車': 0,
        '降車': 1,
        '乗降車': 2
    };

    const rideValue = (isDown && (originalRideValue === 0 || originalRideValue === 1))
        ? 1 - originalRideValue
        : originalRideValue;

    // 追加時は常に「乗降区分」をデフォルト選択にする
    const allowedValue = rideValue;

    // ラジオボタンHTML作成
    const radioHtml = labelTexts.map(text => {
        const value = textToValue[text];
        const isChecked = (value === allowedValue) ? 'checked' : '';
        const disabled = getDisabledAttr(value, rideValue);
        return `
            <label style="margin-right: 10px;">
                <input type="radio" name="${nameBase}.rideAlightDivision" value="${value}" ${isChecked} ${disabled} />
                ${text}
            </label>`;
    }).join('');

    // 行のHTML構築
    const newRowHtml = `
        <tr>
            <td><span>${sequenceNo}</span>
                <input type="hidden" name="${nameBase}.sequenceNo" value="${sequenceNo}" />
            </td>
            <td><span>${busStopName}</span>
                <input type="hidden" name="${nameBase}.busStopName" value="${busStopName}" />
                <input type="hidden" name="${nameBase}.rosenBusStopId" value="${rosenBusStopId}" />
                <input type="hidden" name="${nameBase}.signpostId" value="${signpostId}" />
            </td>
            <td>
                <input type="text" maxlength="50" name="${nameBase}.stopHeadsign" style="width: 100%" />
            </td>
            <td>
                <input type="text" maxlength="100" name="${nameBase}.stopHeadsignEn" style="width: 100%" />
            </td>
            <td>
                ${radioHtml}
                <input type="hidden" name="${nameBase}.rideAlightDivision" value="${allowedValue}" />
                <input type="hidden" name="${nameBase}.originalRideAlightDivision" value="${rideValue}" />
            </td>
            <td class="input-center">
                <input type="text" maxlength="7" style="width: 100px;" name="${nameBase}.shapeDistTraveled" />
            </td>
            <td>
                <input type="button" style="width: 100px;" class="delete-row" value="削除" />
            </td>
        </tr>
    `;

    $tbody.append(newRowHtml); // 行追加
}

/** 行削除 */
function deleteRegisterRow($targetRow) {
    const $tbody = $('.registerBusStopTableBody');
    $targetRow.remove(); // 行を削除

    // 再度運行区分取得（ラジオボタン）
    const serviceDivision = $('input[name="lineDetailServiceDivision"]:checked').val();
    const isDown = serviceDivision === "0";
    const labelTexts = isDown ? ['降車', '乗車', '乗降車'] : ['乗車', '降車', '乗降車'];
    const textToValue = {'乗車': 0, '降車': 1, '乗降車': 2};


    // 各行の再構成（名前と値の整合性維持）
    $tbody.find('tr').each(function (index) {
        const $row = $(this);
        const nameBase = `lineRouteBusStopList[${index}]`;

        // 番号・名称再設定
        $row.find('td').eq(0).find('span').text(index + 1);
        $row.find('input[name$=".sequenceNo"]').attr('name', `${nameBase}.sequenceNo`).val(index + 1);

        const busStopName = $row.find('td').eq(1).find('span').text().trim();
        $row.find('input[name$=".busStopName"]').attr('name', `${nameBase}.busStopName`).val(busStopName);
        $row.find('input[name$=".rosenBusStopId"]').attr('name', `${nameBase}.rosenBusStopId`);
        $row.find('input[name$=".signpostId"]').attr('name', `${nameBase}.signpostId`);

        // 停留所行先の名前を更新
        $row.find('td').eq(2).find('input[type="text"]').attr('name', `${nameBase}.stopHeadsign`);

        // 停留所行先 英語 の名前を更新
        $row.find('td').eq(3).find('input[type="text"]').attr('name', `${nameBase}.stopHeadsignEn`);

        // PKID
        const lineRouteBusStopId = parseInt($row.find('input[type="hidden"][name$=".lineRouteBusStopId"]').val());

        // 現在値を取得
        const currentValue = parseInt($row.find('input[type="hidden"][name$=".rideAlightDivision"]').val());
        const originalValue = parseInt($row.find('input[type="hidden"][name$=".originalRideAlightDivision"]').val());

        // ラジオボタン再生成
        const radioHtml = labelTexts.map(text => {
            const value = textToValue[text];
            const isChecked = value === currentValue ? 'checked' : '';
            const disabled = getDisabledAttr(value, originalValue) === 'disabled' || lineRouteBusStopId ? 'disabled' : '';
            return `
                <label style="margin-right: 10px;">
                    <input type="radio" name="${nameBase}.rideAlightDivision" value="${value}" ${isChecked} ${disabled}/>
                    ${text}
                </label>`;
        }).join('');

        // テーブルの該当セルを更新
        $row.find('td').eq(4).html(`
            ${radioHtml}
            <input type="hidden" name="${nameBase}.rideAlightDivision" value="${currentValue}">
            <input type="hidden" name="${nameBase}.originalRideAlightDivision" value="${originalValue}">
        `);

        // 距離入力欄の名前を更新
        $row.find('td').eq(5).find('input[type="text"]').attr('name', `${nameBase}.shapeDistTraveled`);
    });
}

/** AjaxCall後路線停留所更新 */
function refreshLineBusStopList() {

    const startDate = $('#serviceStartDate').val();
    const endDate = $('#serviceEndDate').val();
    const rosenId = $('#rosenId').val();

    // 路線の値が空の場合は処理を中止
    if (!rosenId) {
        return;
    }

    const json = {
        "serviceStartDate": startDate,
        "serviceEndDate": endDate,
        "rosenId": rosenId
    };

    connectAjax(GET_LINE_BUS_STOP_LIST, json);
}

/** 路線停留所登録一覧更新 */
function updateLineBusStops(data) {
    // データが null または undefined の場合は処理を中止
    if (!data) return;

    const tableBody = document.querySelector(".busStopTableBody");

    // 既存の行を削除
    tableBody.innerHTML = '';

    // チェックされたラジオボタンの値取得
    const checkedRadio = document.querySelector('input[name="lineDetailServiceDivision"]:checked');
    const serviceDivision = checkedRadio ? checkedRadio.value : "1";

    // 乗降区分
    let rideAlightDivisionMap = {
        0: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE, color: RIDE_ALIGHT_CONSTANTS.COLORS.RIDE},
        1: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.ALIGHT, color: RIDE_ALIGHT_CONSTANTS.COLORS.ALIGHT},
        2: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE_ALIGHT, color: RIDE_ALIGHT_CONSTANTS.COLORS.RIDE_ALIGHT}
    };

    // 下り運行 (value=0) の場合swap
    if (serviceDivision === "0") {
        rideAlightDivisionMap = {
            0: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.ALIGHT, color: RIDE_ALIGHT_CONSTANTS.COLORS.ALIGHT},
            1: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE, color: RIDE_ALIGHT_CONSTANTS.COLORS.RIDE},
            2: {text: RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE_ALIGHT, color: RIDE_ALIGHT_CONSTANTS.COLORS.RIDE_ALIGHT}
        };
    }

    // 各データで行を生成
    data.forEach(item => {
        const row = document.createElement("tr");

        // hidden input 追加
        const hiddenInputRosenBusStopId = document.createElement("input");
        hiddenInputRosenBusStopId.type = "hidden";
        hiddenInputRosenBusStopId.name = "rosenBusStopId";
        hiddenInputRosenBusStopId.value = item.rosenBusStopId;
        row.appendChild(hiddenInputRosenBusStopId);

        const hiddenInputSignpostId = document.createElement("input");
        hiddenInputSignpostId.type = "hidden";
        hiddenInputSignpostId.name = "signpostId";
        hiddenInputSignpostId.value = item.signpostId;
        row.appendChild(hiddenInputSignpostId);

        // 登録順 (sequenceNo)
        const seqCell = document.createElement("td");
        seqCell.style.textAlign = "center";
        seqCell.innerHTML = `<span>${item.sequenceNo}</span>`;
        row.appendChild(seqCell);

        // 停留所名 (busStopName)
        const nameCell = document.createElement("td");
        nameCell.innerHTML = `<span>${item.busStopName}</span>`;
        row.appendChild(nameCell);

        // 乗降区分 (rideAlightDivision)
        const divisionCell = document.createElement("td");
        divisionCell.classList.add("input-center");
        divisionCell.setAttribute('data-ride', item.rideAlightDivision);
        const divisionInfo = rideAlightDivisionMap[item.rideAlightDivision] || {text: "", color: ""};
        divisionCell.innerHTML = `<span>${divisionInfo.text}</span>`;
        divisionCell.style.backgroundColor = divisionInfo.color;
        row.appendChild(divisionCell);

        // 追加ボタン
        const isDisabled = document.getElementById('dispLineBusStopsFlag').value === "true";
        const addButtonCell = document.createElement("td");
        addButtonCell.classList.add("input-center");
        addButtonCell.innerHTML = `<input type="button" class="btn-gray" style="width: 150px;" value="追加" ${isDisabled ? "disabled" : ""}/>`;
        row.appendChild(addButtonCell);

        // 行をtbodyに追加
        tableBody.appendChild(row);
    });
}

/** 路線停留所乗降区分レイアウト調整 */
function refreshRideAlightDivision() {
    const $rows = $('.busStopTableBody tr');
    if ($rows.length === 0) return;

    const serviceDivision = $('input[name="lineDetailServiceDivision"]:checked').val();
    const isReverse = serviceDivision === "0"; // 下り判定

    $rows.each(function () {
        const $divisionCell = $(this).find('td').eq(2); // 乗降区分セル
        const rideValue = parseInt($divisionCell.data('ride'));
        if (isNaN(rideValue)) return;

        if (rideValue === 2) {
            // 乗降車は変更しない
            $divisionCell.html(`<span>${RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE_ALIGHT}</span>`);
            $divisionCell.css('background-color', RIDE_ALIGHT_CONSTANTS.COLORS.RIDE_ALIGHT);
            return;
        }

        if (isReverse) {
            // 下り運行：乗車／降車ラベル反転
            if (rideValue === 0) {
                $divisionCell.html(`<span>${RIDE_ALIGHT_CONSTANTS.TEXTS.ALIGHT}</span>`);
                $divisionCell.css('background-color', RIDE_ALIGHT_CONSTANTS.COLORS.ALIGHT);
            } else if (rideValue === 1) {
                $divisionCell.html(`<span>${RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE}</span>`);
                $divisionCell.css('background-color', RIDE_ALIGHT_CONSTANTS.COLORS.RIDE);
            }
        } else {
            // 上り運行：元のまま
            if (rideValue === 0) {
                $divisionCell.html(`<span>${RIDE_ALIGHT_CONSTANTS.TEXTS.RIDE}</span>`);
                $divisionCell.css('background-color', RIDE_ALIGHT_CONSTANTS.COLORS.RIDE);
            } else if (rideValue === 1) {
                $divisionCell.html(`<span>${RIDE_ALIGHT_CONSTANTS.TEXTS.ALIGHT}</span>`);
                $divisionCell.css('background-color', RIDE_ALIGHT_CONSTANTS.COLORS.ALIGHT);
            }
        }
    });
}

/** 登録停留所乗降区分ラジオボタン調整 */
function refreshRegisterRideAlightDivision() {
    const $tbody = $('.registerBusStopTableBody');  // 停留所リストのtbody要素
    const serviceDivision = $('input[name="lineDetailServiceDivision"]:checked').val();  // 上り／下りの選択値
    const isDown = serviceDivision === "0";  // 下りの場合 true

    // 表示するラベルを運行区分に応じて切り替える
    const labelTexts = isDown ? ['降車', '乗車', '乗降車'] : ['乗車', '降車', '乗降車'];

    const textToValue = {
        '乗車': 0,
        '降車': 1,
        '乗降車': 2
    };


    // 各行に対して処理
    $tbody.find('tr').each(function (index) {
        const $row = $(this);
        const nameBase = `lineRouteBusStopList[${index}]`;  // input要素のname属性のベース

        let originalValue = parseInt($row.find('input[type="hidden"][name$=".originalRideAlightDivision"]').val());

        // 乗車・降車の値反転
        if (originalValue === 0) {
            originalValue = 1;
        } else if (originalValue === 1) {
            originalValue = 0;
        }

        $row.find('input[type="hidden"][name$=".originalRideAlightDivision"]').val(originalValue);

        // 現在値を取得
        let currentValue = parseInt($row.find('input[type="hidden"][name$=".rideAlightDivision"]').val());

        // 乗車・降車の値反転
        if (currentValue === 0) {
            currentValue = 1;
        } else if (currentValue === 1) {
            currentValue = 0;
        }

        // PKID
        const lineRouteBusStopId = parseInt($row.find('input[type="hidden"][name$=".lineRouteBusStopId"]').val());

        // チェック値変更
        if (originalValue !== 2 && currentValue !== originalValue) {
            currentValue = originalValue;
        }

        // ラジオボタンHTMLの再生成
        const radioHtml = labelTexts.map(text => {
            const value = textToValue[text];
            const isChecked = value === currentValue ? 'checked' : '';
            const disabled = getDisabledAttr(value, originalValue) === 'disabled' || lineRouteBusStopId ? 'disabled' : '';
            return `
                <label style="margin-right: 10px;">
                    <input type="radio" name="${nameBase}.rideAlightDivision" value="${value}" ${isChecked} ${disabled}/>
                    ${text}
                </label>`;
        }).join('');

        // td要素を更新
        $row.find('td').eq(4).html(`
            ${radioHtml}
            <input type="hidden" name="${nameBase}.rideAlightDivision" value="${currentValue}">
            <input type="hidden" name="${nameBase}.originalRideAlightDivision" value="${originalValue}">
        `);
        $row.find('td').eq(5).find('input[type="text"]').attr('name', `${nameBase}.shapeDistTraveled`);

    });
}

/** ページロード時にhidden値をもとにラジオボタンのdisabledを設定する */
function initializeDisabledByOriginalValue() {
    const $tbody = $('.registerBusStopTableBody');


    // 各行に対して処理
    $tbody.find('tr').each(function (index) {
        const $row = $(this);
        const nameBase = `lineRouteBusStopList[${index}]`;

        // PKID
        const lineRouteBusStopId = parseInt($row.find('input[type="hidden"][name$=".lineRouteBusStopId"]').val());

        // originalRideAlightDivisionの値を取得
        const originalValue = parseInt($row.find('input[type="hidden"][name$=".originalRideAlightDivision"]').val());

        // rideAlightDivisionの現在値を取得
        const currentValue = parseInt($row.find('input[type="hidden"][name$=".rideAlightDivision"]').val());

        // ラジオボタンのdisabledとcheckedを再設定
        $row.find('td').eq(4).find('input[type="radio"]').each(function () {
            const $radio = $(this);
            const value = parseInt($radio.val());

            // disabled属性の設定
            if (getDisabledAttr(value, originalValue) === 'disabled' || lineRouteBusStopId) {
                $radio.prop('disabled', true);
            } else {
                $radio.prop('disabled', false);
            }

            // checked属性の設定
            $radio.prop('checked', value === currentValue);
        });
    });
}

/** ラジオボタンのdisabled属性を返す */
function getDisabledAttr(value, originalValue) {
    if (originalValue === 0 && value !== 0) return 'disabled';
    if (originalValue === 1 && value !== 1) return 'disabled';
    return '';
}
