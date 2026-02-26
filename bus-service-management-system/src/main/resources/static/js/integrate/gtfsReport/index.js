function errorObjClear() {
    // 表示になっているエラーを消去する
    $('.panel-message').each(function() {
        $(this).hide();
    });
    $('#targetDateError').hide();
    $('#targetDate').removeClass('error');
}
