// JavaScript
(function () {
    $(function () {
        var $btn = $('#display_busDaiya');
        if (!$btn.length) return;
        // "表示区分"ラジオボタンの取得,
        var $radios = $('input[name="displayDivisionFlag"]');
        var $revisionSelect = $('#revisionSelect');
        if (!$radios.length) return;
        var $dispChangeBtn = $('#disp-change-button');


        var MSG_DOWNLOAD_DIV = '表示区分を選択してください。';
        var MSG_REVISION_REQUIRED = '運行期間を選択してください。';

        function ensureDownloadDivErr() {
            var $el = $('#downloadDivisionFlagClientError');
            if ($el.length) return $el;
            var $container = $radios.first().closest('.radio');
            if (!$container.length) $container = $radios.first().parent();
            $el = $('<p>', {id: 'downloadDivisionFlagClientError', class: 'text-danger', css: {display: 'none'}});
            $container.append($el);
            return $el;
        }

        function ensureRevisionErr() {
            var $el = $('#daiyaRevisionIdClientError');
            if ($el.length) return $el;
            var $container = $revisionSelect.closest('.col-md-10');
            if (!$container.length) $container = $revisionSelect.parent();
            $el = $('<p>', {id: 'daiyaRevisionIdClientError', class: 'text-danger', css: {display: 'none'}});
            $container.append($el);
            return $el;
        }

        function clearRevisionErr() {
            $('#daiyaRevisionIdClientError').text('').hide();
        }

        function clearDownloadDivErr() {
            $('#downloadDivisionFlagClientError').text('').hide();
        }

        function showDownloadDivErr(msg) {
            ensureDownloadDivErr().text(msg).show();
        }

        function showRevisionErr(msg) {
            ensureRevisionErr().text(msg).show();
        }

        function getDownloadDivisionValue() {
            var $checked = $radios.filter(':checked');
            return $checked.length ? $checked.val() : undefined; // '1' | '0' | undefined
        }

        function isRevisionSelected() {
            var v = ($revisionSelect.val() || '').toString().trim();
            return v !== '';
        }

        // 運行期間
        $revisionSelect.on('change', function () {
            if (isRevisionSelected()) {
                clearRevisionErr();
            }
        });

        function isDownloadDivisionSelected() {
            return $radios.is(':checked');
        }

        $radios.on('change', function () {
            clearDownloadDivErr();
            var val = getDownloadDivisionValue();
            if (val === '1') {
                if (isRevisionSelected()) {
                    clearRevisionErr();
                } else {
                    showRevisionErr(MSG_REVISION_REQUIRED);
                }
            } else if (val === '0') {
                clearRevisionErr();
            }
        });

        // 運行期間
        $revisionSelect.on('change', function () {
            if (isRevisionSelected()) {
                clearRevisionErr();
            } else {
                // 過去ダイヤ
                if (getDownloadDivisionValue() === '0') showRevisionErr(MSG_REVISION_REQUIRED);
            }
        });

        function dispValidation(e) {
            var hasError = false;

            // 1) ダウンロード区分
            if (!isDownloadDivisionSelected()) {
                showDownloadDivErr(MSG_DOWNLOAD_DIV);
                hasError = true;
            }

            // 2) 運行期間
            var rawVal = getDownloadDivisionValue();
            var val = (typeof rawVal === 'undefined' || rawVal === null) ? undefined : String(rawVal);
            var requireRevision = (val === '1') || (typeof val === 'undefined');
            if (requireRevision && !isRevisionSelected()) {
                showRevisionErr(MSG_REVISION_REQUIRED);
                hasError = true;
            }

            if (hasError) {
                if (!isDownloadDivisionSelected()) {
                    try {
                        $radios.first().focus();
                    } catch (err) {
                    }
                } else if (requireRevision && !isRevisionSelected()) {
                    try {
                        $revisionSelect.focus();
                    } catch (err) {
                    }
                }
                e.preventDefault();
                e.stopImmediatePropagation();
                return false;
            }
            clearDownloadDivErr();
            clearRevisionErr();

            $(this).prop('disabled', true);
            const formAction = $(this).attr('formaction');
            if (formAction !== undefined) {
                $(this).closest('form').attr('action', formAction).submit();
                return;
            }
            $(this).closest('form').submit();

        }
        $btn.on('click', function (e) {
            var hasError = false;

            // 1) 表示区分
            if (!isDownloadDivisionSelected()) {
                showDownloadDivErr(MSG_DOWNLOAD_DIV);
                hasError = true;
            }

            // 2) 運行期間
            var rawVal = getDownloadDivisionValue();
            var val = (typeof rawVal === 'undefined' || rawVal === null) ? undefined : String(rawVal);
            var requireRevision = (val === '1') || (typeof val === 'undefined');
            if (requireRevision && !isRevisionSelected()) {
                showRevisionErr(MSG_REVISION_REQUIRED);
                hasError = true;
            }

            if (hasError) {
                if (!isDownloadDivisionSelected()) {
                    try {
                        $radios.first().focus();
                    } catch (err) {
                    }
                } else if (requireRevision && !isRevisionSelected()) {
                    try {
                        $revisionSelect.focus();
                    } catch (err) {
                    }
                }
                e.preventDefault();
                e.stopImmediatePropagation();
                return false;
            }

            clearDownloadDivErr();
            clearRevisionErr();

        });

        // 付加情報表示切り替えボタンの実装
        if (!$dispChangeBtn.length) return;
        $dispChangeBtn.on('click', function () {
            if ($('.busDaiyaDetailOpt').hasClass("busDaiyaDetailOptDispNone")) {
                $('.busDaiyaDetailOpt').removeClass("busDaiyaDetailOptDispNone");
                $dispChangeBtn.text("付加情報非表示");
            } else {
                $('.busDaiyaDetailOpt').addClass("busDaiyaDetailOptDispNone");
                $dispChangeBtn.text("付加情報表示");
            }
        });
        // 便追加ボタン押下時の処理
        const $addBusDaiya = $('#add-bus-daiya');
        if (!$addBusDaiya.length) return;
        $addBusDaiya.on('click', function () {
            // 何列目か取得
            var colNum = $('.busDaiyaDetailCol').length;
            // 列のclone
            var $clone = $("#bus-daiya-area .busDaiyaDetailCol:last-child").clone(true);
            // select2のspanタグ削除
            $clone.find("span").each(function () {
                if (!($(this).hasClass('busDaiyaDetailSkipBusStop')
                    || $(this).hasClass('busDaiyaDispColNumber')
                    || $(this).hasClass('form-item-name'))) {
                    $(this).remove();
                }
            });
            // 入力系タグのvalue, 選択値のクリア
            $clone.find('input').val("");
            $clone.find('select').prop("selectedIndex", 0);
            // 追加行入力系のname属性の変更
            $clone.find('input').each(function () {
                const name = $(this).attr('name');
                const preIndex = name.indexOf("[");
                const prefix = name.slice(0, preIndex + 1);
                const suffIndex = name.indexOf("]");
                const suffix = name.slice(suffIndex, name.length);
                $(this).attr('name', prefix + colNum + suffix);
            });
            $clone.find('select').each(function () {
                const prefix = $(this).attr('name').split('[')[0];
                const suffix = $(this).attr('name').split(']')[1];
                $(this).attr('name', prefix + '[' + colNum + ']' + suffix);
            });
            $clone.find('select').prop("selectedIndex", 0);
            $clone.find('select[name$="lineRouteId"]').trigger('change');

            // No.の書き換え
            $clone.find('.busDaiyaDispColNumber').text(colNum + 1);
            $clone.find('input[name$="busDaiyaDispColNumber"').val(colNum + 1);
            $clone.find('input[name$="skipBusStopFlag"').val(1);
            // select2の初期化
            $clone.find(".select2").select2();
            $clone.appendTo("#bus-daiya-area");
            initSelect2(); // select2 を再初期化
        });

        // 系統プルダウン変更時の処理
        var $lineRouteSelect = $('select[name$="lineRouteId"]');
        if (!$lineRouteSelect.length) return;
        $lineRouteSelect.on('change', function () {
            // 便ダイヤ明細部分のHTML(スキップと入力)
            let lineRouteBusStopSkipHtml = `
                <div class="busDaiyaInputDivArrDep">
                    <div class="div-flex busDaiyaInputDivArr">
                        <span class="busDaiyaDetailSkipBusStop">↓</span>
                        <input type="hidden" name="busDaiyasRegistedList[daiya_index].detailList[dtl_index].skipBusStopFlag" value="1">
                    </div>
                </div>`;
            let lineRouteBusStopHtml = `
                <div class="busDaiyaInputDivArrDep">
                    <input type="hidden" name="busDaiyasRegistedList[daiya_index].detailList[dtl_index].lineRouteBusStopId" value="line_route_id">
                    <div class="div-flex busDaiyaInputDivArr">
                        <label class="busDaiyaInputLabelArrDep">
                            <span class="form-item-name">到着</span>
                        </label>
                        <input type="text" minlength="1" maxlength="4" class="form-control busDaiyaInputArrDep" name="busDaiyasRegistedList[daiya_index].detailList[dtl_index].busStopArrivalTime" value="">
                    </div>
                    <div class="div-flex busDaiyaInputDivDep">
                        <label class="busDaiyaInputLabelArrDep">
                            <span class="form-item-name">出発</span>
                        </label>
                        <input type="text" minlength="1" maxlength="4" class="form-control busDaiyaInputArrDep" name="busDaiyasRegistedList[daiya_index].detailList[dtl_index].busStopDepartureTime" value="">
                    </div>
                </div>`;
            let col_idx = $(this).attr('name').split('[')[1].split(']')[0];

            let selectedId = ($(this).val());
            let $headDom = $(this).closest('.busDaiyaDetailCol');
            let $detailArea = $headDom.find('div:last-child');
            $detailArea.find('div').remove();


            for (let i = 0; i < rosenBusStops.length; i++) {
                let htmlstr = lineRouteBusStopSkipHtml;
                for (let j = 0; j < lineRouteBusStops.length; j++) {
                    if (rosenBusStops[i].rosenBusStopId == lineRouteBusStops[j].rosenBusStopId
                        && lineRouteBusStops[j].lineRouteId == selectedId) {
                        htmlstr = lineRouteBusStopHtml;
                        let lineRouteVal = lineRouteBusStops[j].lineRouteBusStopId;
                        htmlstr = htmlstr.replace('line_route_id', lineRouteVal);
                    }
                }
                $detailArea.append(htmlstr.replaceAll('daiya_index', col_idx).replaceAll('dtl_index', i.toString()));
            }
        });
    });


    'use strict';

    // ---- CSRF ----
    function getCsrfToken() {
        return (document.querySelector('meta[name="_csrf"]') || {}).content
            || (document.querySelector('input[name="_csrf"]') || {}).value
            || '';
    }

    function getCsrfHeader() {
        return (document.querySelector('meta[name="_csrf_header"]') || {}).content || 'X-CSRF-TOKEN';
    }

    // ---- Error UI ----
    function showErrors(boxId, listId, messages) {
        var box = document.getElementById(boxId);
        var list = document.getElementById(listId);
        var arr = Array.isArray(messages) ? messages : [String(messages || 'エラーが発生しました。')];

        if (list) {
            list.innerHTML = '';
            arr.forEach(function (m) {
                var li = document.createElement('li');
                li.textContent = m;
                list.appendChild(li);
            });
        }
        if (box) box.style.display = '';
        try {
            window.scrollTo({top: 0, behavior: 'smooth'});
        } catch (e) { /* noop */
        }
    }

    function showErrorRaw(boxId, listId, rawText, extra) {
        // 生HTMLは表示しない（サニタイズせず非表示）。簡易メッセージのみ出力。
        var status = extra && extra.status ? extra.status : undefined;
        var msg = status ? ('処理に失敗しました。（HTTP ' + status + '）') : '処理に失敗しました。';
        showErrors(boxId, listId, [msg]);
    }

    function clearErrors(boxId, listId) {
        var box = document.getElementById(boxId);
        var list = document.getElementById(listId);
        if (list) list.innerHTML = '';
        if (box) box.style.display = 'none';
    }

    // ---- Init ----
    function init() {
        // 事前にやることがあれば書く
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();
