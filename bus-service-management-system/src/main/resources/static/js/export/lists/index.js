function errorObjClear() {
    // 表示になっているエラーを消去する
    $('.panel-message').each(function() {
        $(this).hide();
    });
    $('#reportTypeError').hide();
    $('#reportType').select2('destroy');
    $('#reportType').removeClass('error');
    $('#reportType').select2();
    $('#rosenIdError').hide();
    $('#rosenSelect').select2('destroy');
    $('#rosenSelect').removeClass('error');
    $('#rosenSelect').select2();
    $('#serviceStartDateError').hide();
    $('#serviceStartDate').select2('destroy');
    $('#serviceStartDate').removeClass('error');
    $('#serviceStartDate').select2();
}
