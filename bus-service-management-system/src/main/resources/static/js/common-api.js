const API_CHANGE_PASSWORD_URL = '/api/password-change/';
const CHANGE_AGENCY = '/api/agency-change/';
const GET_BUS_STOP_LIST = '/api/get-bus-stop-list-by-service-date/';
const GET_LINE_BUS_STOP_LIST = '/api/get-line-bus-stop-list-by-service-date-and-rosen-id/';

function changeUserPassword(json) {
    connectAjax(API_CHANGE_PASSWORD_URL, json);
}

/**
 * 事業者更新
 *
 */
function changeAgency() {
    const selectedValue = document.getElementById('selectAgency').value;
    if (selectedValue === "") return;
    const json = {"businessCorpId" : selectedValue};
    // 事業者切り替え用のAPIを呼び出す
    connectAjax(CHANGE_AGENCY, json);
}

function setAjaxResultData(url, data) {
	if (API_CHANGE_PASSWORD_URL === url) {
        setChangeUserPasswordResult(data);
    } else if (CHANGE_AGENCY === url){
        // API結果による画面制御
        if (data.textStatus === "success") {
            // すぐ遷移させると、セッション情報が反映されないため、少し待ってから画面遷移を行う
            setTimeout(function() {
                window.location.href = document.getElementById('topUrlByMenu').value;
            }, 250);
        } else {
            alert("事業者変更に失敗しました。システム管理者にお問い合わせください。")
        }
    } else if (GET_BUS_STOP_LIST === url) {
        updateBusStopSelectOptions(data)
    } else if (GET_LINE_BUS_STOP_LIST === url) {
        updateLineBusStops(data);
    }
}

/** パスワード変更API実行結果の設定 */
function setChangeUserPasswordResult(data) {
    if (data === null) {
        showAjaxResultMessageInModal(false, 'パスワードの変更に失敗しました。');
        return;
    }

    if(!data.result) {
        showAjaxResultMessageInModal(data.result, data.message);
        return false;
    }

    showAjaxResultMessageInModal(data.result, data.message);
    $('#currentPass').val("");
    $('#newPass').val("");
    $('#confPass').val("");
    // 「×」ボタン表示
    $('.close').show();
    $('#modalCancel').show();
    $('#modalConfirm').addClass('successPass');
    // フラグをtrue（変更済）に設定
    $('#passChangeFlag').val('1');

}
