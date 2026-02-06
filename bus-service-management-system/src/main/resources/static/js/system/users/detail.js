$(document).ready(function() {
	// 初期画面をロードする場合
	updateOperatingCompanies(true);

	// 変更する場合
	$("#agencySelect").on("change", function() {
		updateOperatingCompanies(false);
	});
});

/** 事業者に紐づく運行会社で更新 */
function updateOperatingCompanies(isInitial) {
	var agencyId = $("#agencySelect").val();
	var $select = $("#operatingCompanySelect");
	var html = '<option value="">------------</option>';

	// 事業者が選択されていない場合は、基本オプションのみを表示
	if (!agencyId) {
		$select.html(html).val("");
		return;
	}

	// 選択された事業者に該当する運行会社をフィルタリング
	allOperatingCompanies
	.filter(c => c.businessCorpId == agencyId)
	.forEach(c => {
		html += `<option value="${c.operatingCompanyId}">${c.operatingName}</option>`;
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

/** 再発行モーダル内の「YES」ボタン押下時処理 */
$("#passwordReissueModalConfirm").on('click', function() {
    $('#passwordReissueForm').submit();
});
