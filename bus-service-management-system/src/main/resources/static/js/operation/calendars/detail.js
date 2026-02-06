/** nameのインデクス設定（共通） */
function replaceIndex($el, listKey, newIndex) {
    var name = $el.attr('name');
    if (!name) return;
    var re = new RegExp(listKey.replace('[', '\\[').replace(']', '\\]') + '\\[\\d+\\]');
    $el.attr('name', name.replace(re, listKey + '[' + newIndex + ']'));
}

/** nameのインデクス設定(運行除外日) */
function setIndexCancel($el, newIndex) {
    replaceIndex($el, 'calendarCancelDateList', newIndex);
}

/** Indexを再作成(運行除外日) */
function renumberRowsCancel($table) {
    var $tbody = $table.find('tbody').first();

    var $ids     = $tbody.find('input[name^="calendarCancelDateList["][name$=".calendarCancelDateId"]');
    var $dates   = $tbody.find('input[name^="calendarCancelDateList["][name$=".operationCancelDate"]');
    var $keeps   = $tbody.find('input[name^="calendarCancelDateList["][name$=".keep"]');
    var $deletes = $tbody.find('input[name^="calendarCancelDateList["][name$=".delete"]');

    var n = Math.max($ids.length, $dates.length, $keeps.length, $deletes.length);
    for (var i = 0; i < n; i++) {
        if ($ids[i])     setIndexCancel($($ids[i]), i);
        if ($dates[i])   setIndexCancel($($dates[i]), i);
        if ($keeps[i])   setIndexCancel($($keeps[i]), i);
        if ($deletes[i]) setIndexCancel($($deletes[i]), i);
    }
}

/** ボタン表示制御(運行除外日) */
function refreshRowButtonsCancel($table) {
    var $rows = $table.find('tbody tr:visible');
    if ($rows.length === 0) return;
    $table.find('tbody tr td:last-child button').hide();
    var $last = $rows.last();
    var $ops = $last.children('td').last();
    $ops.find('button.btn-success').show(); // 追加
    if ($rows.length > 1) $ops.find('button.btn-danger').show(); // 削除
}

/** 行テンプレート(運行除外日) */
function buildRowHtmlCancel(startIndex) {
    var html = '<tr role="row" class="odd">';
    for (var k = 0; k < 7; k++) {
        var idx = startIndex + k;
        html += ''
            + '<td>'
            + '  <input type="hidden" name="calendarCancelDateList[' + idx + '].calendarCancelDateId" value="">'
            + '  <input type="hidden" name="calendarCancelDateList[' + idx + '].keep" value="1">'
            + '  <span></span>'
            + '</td>'
            + '<td>'
            + '  <input type="text" name="calendarCancelDateList[' + idx + '].operationCancelDate" maxlength="6" placeholder="yymmdd" style="width: 80px;" />'
            + '</td>'
            + '<td>'
            + '  <input type="checkbox" name="calendarCancelDateList[' + idx + '].delete" />'
            + '</td>';
    }
    html += ''
        + '<td style="white-space:nowrap;">'
        + '  <button type="button" class="btn btn-danger btn-sm" style="display:inline-block;" onclick="deleteExclusionRow(this)">削除</button>'
        + '  <button type="button" class="btn btn-success btn-sm" style="display:inline-block; margin-left:8px;" onclick="addExclusionRow(this)">追加</button>'
        + '</td>'
        + '</tr>';
    return html;
}

/** 行追加(運行除外日) */
window.addExclusionRow = function (btn) {
    var $table = $(btn).closest('table');
    var $tbody = $table.find('tbody').first();

    var currentCount = $tbody.find('input[name^="calendarCancelDateList["][name$=".operationCancelDate"]').length;
    var startIndex = currentCount;

    $tbody.append(buildRowHtmlCancel(startIndex));

    // 追加後は全フィールドの index を整える
    renumberRowsCancel($table);
    refreshRowButtonsCancel($table);
};

/** 行削除（運行除外日） */
window.deleteExclusionRow = function (btn) {
    var $table = $(btn).closest('table');
    var $tr = $(btn).closest('tr');

    $tr.find('input[name^="calendarCancelDateList["][name$=".keep"]').val('0');

    $tr.hide();

    refreshRowButtonsCancel($table);
};

/** name のインデックス置換（運行指定日） */
function setIndexDesignated($el, newIndex) {
    replaceIndex($el, 'calendarDesignatedDateList', newIndex);
}

/** Index を再作成（運行指定日） */
function renumberRowsDesignated($table) {
    var $tbody   = $table.find('tbody').first();
    var $ids     = $tbody.find('input[name^="calendarDesignatedDateList["][name$=".calendarDesignatedDateId"]');
    var $dates   = $tbody.find('input[name^="calendarDesignatedDateList["][name$=".operationDesignatedDate"]');
    var $keeps   = $tbody.find('input[name^="calendarDesignatedDateList["][name$=".keep"]');
    var $deletes = $tbody.find('input[name^="calendarDesignatedDateList["][name$=".delete"]');

    var n = Math.max($ids.length, $dates.length, $keeps.length, $deletes.length);
    for (var i = 0; i < n; i++) {
        if ($ids[i])     setIndexDesignated($($ids[i]), i);
        if ($dates[i])   setIndexDesignated($($dates[i]), i);
        if ($keeps[i])   setIndexDesignated($($keeps[i]), i);
        if ($deletes[i]) setIndexDesignated($($deletes[i]), i);
    }
}

/** 行末ボタン表示制御（運行指定日） */
function refreshRowButtonsDesignated($table) {
    var $rows = $table.find('tbody tr:visible');
    if ($rows.length === 0) return;

    $table.find('tbody tr td:last-child button').hide();

    var $last = $rows.last();
    var $ops  = $last.children('td').last();
    $ops.find('button.btn-success').show();
    if ($rows.length > 1) $ops.find('button.btn-danger').show();
}

/** 行テンプレート（運行指定日） */
function buildDesignatedRowHtml(startIndex) {
    var html = '<tr role="row" class="odd">';
    for (var k = 0; k < 7; k++) {
        var idx = startIndex + k;
        html += ''
            + '<td>'
            + '  <input type="hidden" name="calendarDesignatedDateList[' + idx + '].calendarDesignatedDateId" value="">'
            + '  <input type="hidden" name="calendarDesignatedDateList[' + idx + '].keep" value="1">'
            + '  <span></span>'
            + '</td>'
            + '<td>'
            + '  <input type="text" name="calendarDesignatedDateList[' + idx + '].operationDesignatedDate" maxlength="6" placeholder="yymmdd" style="width: 80px;" />'
            + '</td>'
            + '<td>'
            + '  <input type="checkbox" name="calendarDesignatedDateList[' + idx + '].delete" />'
            + '</td>';
    }
    html += ''
        + '<td style="white-space:nowrap;">'
        + '  <button type="button" class="btn btn-danger btn-sm" style="display:inline-block;" onclick="deleteDesignatedRow(this)">削除</button>'
        + '  <button type="button" class="btn btn-success btn-sm" style="display:inline-block; margin-left:8px;" onclick="addDesignatedRow(this)">追加</button>'
        + '</td>'
        + '</tr>';
    return html;
}

/** 行追加（運行指定日） */
window.addDesignatedRow = function (btn) {
    var $table = $(btn).closest('table');
    var $tbody = $table.find('tbody').first();

    var currentCount = $tbody.find('input[name^="calendarDesignatedDateList["][name$=".operationDesignatedDate"]').length;
    var startIndex = currentCount;

    $tbody.append(buildDesignatedRowHtml(startIndex));

    renumberRowsDesignated($table);
    refreshRowButtonsDesignated($table);
};

/** 行削除（運行指定日） */
window.deleteDesignatedRow = function (btn) {
    var $table = $(btn).closest('table');
    var $tr    = $(btn).closest('tr');

    $tr.find('input[name^="calendarDesignatedDateList["][name$=".keep"]').val('0');
    $tr.hide();

    refreshRowButtonsDesignated($table);
};

/** 画面ロード時 */
$(function () {
    $('table.table-exclusions').each(function () {
        var $table = $(this);
        var hasCancelInputs = $table.find('tbody input[name^="calendarCancelDateList["]').length > 0;
        if (hasCancelInputs) {
            renumberRowsCancel($table);
            refreshRowButtonsCancel($table);
        }
    });

    $('table.table-exclusions').each(function () {
        var $table = $(this);
        var hasDesignatedInputs = $table.find('tbody input[name^="calendarDesignatedDateList["]').length > 0;
        if (hasDesignatedInputs) {
            renumberRowsDesignated($table);
            refreshRowButtonsDesignated($table);
        }
    });
});

/** Disableに変更 */
function setSectionDisabled($root, disabled) {
    $root.find('input, select, textarea, button').prop('disabled', disabled);
    if (!disabled) {
        $root.find('[data-locked="true"]').prop('disabled', true);
    }
    $root.css('opacity', disabled ? 0.6 : 1.0);
}

/** 「必須」表示・非表示 */
function setRequiredBadge($section, visible) {
    $section.find('.require-txt').toggle(!!visible);
}

/** 曜日指定可否区分による動き */
function applyWeekDesignatableFlag(flagVal) {
    var enabled = String(flagVal) === '1';

    // 曜日祝日指定区分
    var $secWeekDesignation = $('#section-weekDesignationType');
    setSectionDisabled($secWeekDesignation, !enabled);
    setRequiredBadge($secWeekDesignation, enabled);
    // 曜日区分
    var $secWeekday = $('#section-weekday');
    setSectionDisabled($secWeekday, !enabled);
    setRequiredBadge($secWeekday, enabled);
    // 運行除外日設定
    setSectionDisabled($('#section-cancel'), !enabled);
}

$(function () {
    // 画面ロード時
    var initVal = $('input[name="weekDesignatableFlag"]:checked').val();
    applyWeekDesignatableFlag(initVal);

    // 変更時
    $(document).on('change', 'input[name="weekDesignatableFlag"]', function () {
        applyWeekDesignatableFlag($(this).val());
    });
});
