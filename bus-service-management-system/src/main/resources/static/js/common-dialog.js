var messageList = new Map();

/** 選択ポップアップ生成 */
function openCustomReturnValueDialog(title, msgCd) {
	var defer = $.Deferred();
	$('#info-modal').modal('show');
	$('#alertTitle').text(title);
	$('#alertMessage').html(messageList.get(msgCd));
	$('#modalCancel').show();
	$('#modalCancel').on('click', function(e) {
		defer.resolve(false);
		$('#info-modal').modal('hide');
	});
	$('#modalConfirm').on('click', function(e) {
		$('.double-click').prop('disabled', true);
		defer.resolve(true);
	});
	return defer.promise();
}