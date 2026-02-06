/** 権限情報リスト生成 */
function makePermissionList() {
	$('.top-permission').each(function(topIndex, top) {
		$(top).find('.detail-permission').each(function(subIndex, sub) {
			$(sub).find('input').each(function(index, perm) {
				if ($(perm).hasClass('menu-id')) {
					$(perm).attr('name', 'topMenuList[' + topIndex + '].subMenuList[' + subIndex + '].menuId');
					return true;
				}
				if ($(perm).hasClass('menu-name')) {
					$(perm).attr('name', 'topMenuList[' + topIndex + '].subMenuList[' + subIndex + '].menuName');
					return true;
				}
				if ($(perm).hasClass('menu-code')) {
					$(perm).attr('name', 'topMenuList[' + topIndex + '].subMenuList[' + subIndex + '].menuCode');
					return true;
				}
				if ($(perm).hasClass('read-perm')) {
					$(sub).append(getHiddenInput(topIndex, subIndex, 'readPermFlg', $(perm).prop('checked') ? 1 : 0));
					return true;
				}
				if ($(perm).hasClass('write-perm')) {
					$(sub).append(getHiddenInput(topIndex, subIndex, 'writePermFlg', $(perm).prop('checked') ? 1 : 0));
					return true;
				}
				if ($(perm).hasClass('download-perm')) {
					$(sub).append(getHiddenInput(topIndex, subIndex, 'downloadPermFlg', $(perm).prop('checked') ? 1 : 0));
					return true;
				}

			});
		});
	});
	return false;
}

/** 権限情報 INPUT OBJECT 生成 */
function getHiddenInput(topIndex, subIndex, name, value) {
	hidden = $('<input type="hidden"></input>');
	$(hidden).attr('name', 'topMenuList[' + topIndex + '].subMenuList[' + subIndex + '].' + name);
	$(hidden).val(value);
	return hidden
}
