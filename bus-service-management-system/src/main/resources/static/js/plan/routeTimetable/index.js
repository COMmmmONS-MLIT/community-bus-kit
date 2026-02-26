function errorObjClear() {
    // 表示になっているエラーを消去する
    $('.panel-message').each(function() {
        $(this).hide();
    });
    $('#rosenIdError').hide();
    $('#rosenSelect').select2('destroy');
    $('#rosenSelect').removeClass('error');
    $('#rosenSelect').select2();
    $('#daiyaRevisionIdError').hide();
    $('#serviceStartDate').select2('destroy');
    $('#serviceStartDate').removeClass('error');
    $('#serviceStartDate').select2();
}
