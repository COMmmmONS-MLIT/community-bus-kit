$(document).ready(function() {
    // 初期画面をロードする場合
    updateOperatingCompanies(true);

    // 変更する場合
    $("#rosenSelect").on("change", function() {
        updateOperatingCompanies(false);
    });
});

/** 路線に紐づく利用開始日で更新 */
function updateOperatingCompanies(isInitial) {
    var rosenId = $("#rosenSelect").val();
    var $select = $("#serviceStartDate");
    var html = '<option value="">------------</option>';

    // 路線が選択されていない場合は、基本オプションのみを表示
    if (!rosenId) {
        $select.html(html).val("");
        return;
    }

    // 選択された路線に該当する利用開始日をフィルタリング
    allDaiyaDateList
        .filter(c => c.rosenId == rosenId)
        .forEach(c => {
            html += `<option value="${c.daiyaRevisionId}">${c.serviceStartDate}</option>`;
        });

    // Optionを作成
    $select.html(html);

    if (isInitial) {
        // 初期画面をロードする場合、フォームの値を設定
        var sel = $select.data("selected");
        if (sel) $select.val(sel);
    } else {
        // 変更する場合、値を初期化
        $select.val("");
    }
}
