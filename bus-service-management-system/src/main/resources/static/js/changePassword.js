/** 画面ロード時処理 */
$(function(){
    // パスワード変更フラグがfalse（変更前）の場合、パスワード変更モーダルを強制表示する
    if($('#passChangeFlag').val() === '0') {
      openChangePasswordModal();
    }
});

/** MODAL_メッセージ 初期化*/
function initMsgBoxInModal() {
    var $successMsgBoxInModal = $('#modalSuccessMsgBox');
    var $errorMsgBoxInModal = $('#modalErrorMsgBox');
    $successMsgBoxInModal.empty();
    $successMsgBoxInModal.hide();
    $errorMsgBoxInModal.empty();
    $errorMsgBoxInModal.hide();
}

/**ヘッダのパスワード変更アイコンクリック時、パスワード変更モーダルを開く */
function openChangePasswordModal() {
     //メッセージボックス初期化
    initMsgBoxInModal();
     //入力内容初期化
    $('#currentPass').val("");
    $('#newPass').val("");
    $('#confPass').val("");
    $('#modalConfirm').removeClass('successPass').text('変更');
    $('#modalCancel').text('閉じる');
    //モーダル表示
    $('#password-change-modal').modal('show');

    //モーダル右上の「×」ボタン、「No]ボタン表示制御
    if($('#passChangeFlag').val() === '0') {
        $('.close').hide();
        $('#modalCancel').hide();
    } else {
        $('.close').show();
        $('#modalCancel').show();
    }
}

/** パスワード変更モーダル内の「YES」ボタン押下時処理 */
function onChangePass() {
    var currentPass = $('#currentPass').val();
    var newPass = $('#newPass').val();
    var confPass = $('#confPass').val();
    $('#modalConfirm').blur();

    //メッセージボックス初期化
    initMsgBoxInModal();

    // 入力判定
    if(currentPass === '' || newPass === '' || confPass === '') {
      showAjaxResultMessageInModal(null, "未入力項目があります。");
      return false;
    }

    // 新パスワードと再確認パスワードが同じか判定
    if (newPass !== confPass) {
        showAjaxResultMessageInModal(null, "新パスワードと再確認パスワードが一致しません。");
        return false;
    }

    // 新パスワードと再確認パスワードが同じか判定
    if (currentPass === newPass) {
        showAjaxResultMessageInModal(null, "現パスワードと新パスワードが同じです。");
        return false;
    }

    // 送信データ作成
    var json = {
        "currentPass" : currentPass,
        "newPass" : newPass
    };

    // パスワード変更API呼出
    changeUserPassword(json);
};

/**パスワード変更モーダル内の「NO」ボタン押下時処理 */
function onNoChangePass() {
    $('#password-change-modal').modal('hide');
}
