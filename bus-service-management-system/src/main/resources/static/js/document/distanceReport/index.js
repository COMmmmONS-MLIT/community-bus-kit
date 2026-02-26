function errorObjClear() {
    // 表示になっているエラーを消去する
    $('.panel-message').each(function() {
        $(this).hide();
    });
    $('#rosenIdError').hide();
    $('#rosenId').select2('destroy');
    $('#rosenId').removeClass('error');
    $('#rosenId').select2();
    $('#serviceDateError').hide();
    $('#serviceDate').removeClass('error');
}
