/**
 * 二重クリック防止処理
 * ボタンを押したら即座に非活性化し、必要に応じて form の action を差し替えて送信する
 */
$('.double-click').on('click', function() {
	$(this).prop('disabled', true);
	const formAction = $(this).attr('formaction');
	if (formAction !== undefined) {
		$(this).closest('form').attr('action', formAction).submit();
		return;
	}
	$(this).closest('form').submit();
});

/**
 * ブラウザの戻る操作でページが復元された場合
 * （Back/Forward Cache から復元された時）、無効化されたボタンを再度有効化する
 */
window.addEventListener('pageshow', function(event) {
	if (event.persisted) {
		$('.double-click').prop('disabled', false);
	}
});

/**
 * set header CSRF token for AJAX call
 *
 * @returns
 */
$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	// Select2の設定
	if ($('.select2').length) {
		$(".select2").select2({
		    language: "ja",
		    dropdownAutoWidth: true,
		    width: '100%',
			templateResult: resultState
		});
		// 必須エラー等がある場合、背景色を変更する
		$('.select2.error').parent().find(".select2-selection").css("background-color", "pink");
	}

	function resultState(data, container) {
		if (data.element) {
			$(container).addClass($(data.element).attr("class"));
		}
		return data.text;
	}

});

/** type="number"のmaxlength制限 */
$(document).on('input', 'input[type="number"]', function(){
	if($(this).attr('maxlength') !== undefined && this.value.length > this.maxLength) {
		this.value = this.value.slice(0, this.maxLength);
	}
});

/** 注文詳細画面に戻る */
$('#back-order-detail-button').on('click', function() {
  $('#order-detail-form').submit();
});

/** Select2にフォーカスが当たっているときに、標準ではEnterで開くが、↓で開くようにする  */
$(document).on('keydown', '.select2', function(e) {
  if (e.originalEvent && e.which == 40) {
    e.preventDefault();
    $(this).siblings('select').select2('open');
  }
});

/** select2を初期化する処理 */
function initSelect2() {
	$("select.select2").each(function () {
		$(this).select2({
			language: "ja",
			dropdownAutoWidth: true,
			templateResult: (data, container) => {
				if (data.element) {
					$(container).addClass($(data.element).attr("class"));
				}
				return data.text;
			},
		});
	});
}

/** select2を解除する処理 */
function destroySelect2() {
	$("select.select2").each(function () {
		$(this).select2("destroy");
	});
}

/** 入力項目の注釈ボタン */
function toggleHelp(id, btn) {
	var el = document.getElementById(id);
	if (!el) return;

	var willOpen = (el.style.display === 'none' || el.style.display === '');
	el.style.display = willOpen ? 'block' : 'none';

	if (btn) {
		if (!btn.dataset.originalText) {
			btn.dataset.originalText = btn.textContent;
		}
		var openText = btn.dataset.openText || 'v';
		btn.textContent = willOpen ? openText : btn.dataset.originalText;
		btn.setAttribute('aria-expanded', String(willOpen));
	}
}

/** 注釈ボタンの表示テキスト設定 */
(function () {
    function takeFirstN(str, n) {
        return Array.from((str || "").trim()).slice(0, n).join("");
    }

    function getHelpIdFromOnclick(btn) {
        const oc = btn.getAttribute("onclick") || "";
        const m = oc.match(/toggleHelp\(['"]([^'"]+)['"]/);
        return m ? m[1] : null;
    }

    function buildLabelFromHelp(helpEl, max = 10) {
        const raw = (helpEl?.textContent || "").replace(/\s+/g, " ").trim();
        const head = takeFirstN(raw, max);
        const dots = raw.length > max ? "..." : "";
        return `${head}${dots} ＞`;
    }

    function initOne(btn) {
        if (btn.dataset.helpLabelInitialized === "1") return;

        const id = btn.dataset.helpTarget || getHelpIdFromOnclick(btn);
        if (!id) return;
        const helpEl = document.getElementById(id);
        if (!helpEl) return;

        const label = buildLabelFromHelp(helpEl, 10);
        btn.dataset.originalText = label;
        btn.textContent = label;
        btn.setAttribute("aria-controls", id);
        btn.setAttribute("aria-expanded", "false");
        btn.dataset.helpLabelInitialized = "1";
    }

    function initAll(scope) {
        (scope || document).querySelectorAll("button.help-toggle").forEach(initOne);
    }

    window.initHelpButtons = initAll;

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", () => initAll(document));
    } else {
        initAll(document);
    }

    const mo = new MutationObserver((mutations) => {
        mutations.forEach((m) => {
            m.addedNodes.forEach((node) => {
                if (node.nodeType !== 1) return;
                if (node.matches && node.matches("button.help-toggle")) {
                    initOne(node);
                } else if (node.querySelectorAll) {
                    initAll(node);
                }
            });
        });
    });
    mo.observe(document.body, { childList: true, subtree: true });
})();
