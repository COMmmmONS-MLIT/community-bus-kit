/** 削除ボタン押下イベント */
$('.delBtn').on('click',function(e){
    //権限ロールID設定
    $("#permRoleId").val(e.target.value);
    //権限ロール名設定
    $("#permissionRoleName").val($(this).closest('td').find('.permRoleName').val());
    clickDeletePermission();
});

/** 削除処理 */
function clickDeletePermission(){
    // ダイアログメッセージの設定
    var message = "「" + $('#permissionRoleName').val() + "」権限を削除します。<BR><BR>宜しいでしょうか。"
    messageList.set('delPermissionRoleInfo', message);
    // ダイアログを呼び出す
    openCustomReturnValueDialog('権限削除', 'delPermissionRoleInfo').then(function(result) {
        if (result) {
            $('#delForm').submit();
        }
        $("#permRoleId").val("");
            return false;
        });
}
