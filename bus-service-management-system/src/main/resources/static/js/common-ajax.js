var $successMsgBox = $('.task-success');
var $errorMsgBox = $('.task-danger');

const api = {
	API_GET_CORPORATION_LIST: {url: '/api/get-corporation/', success: successGetCorporations, fail: failureGetCorporations, dataType: 'json'},
	API_GET_LINE_LIST_BY_CORPORATION: {url: '/api/get-lines-by-corporation/', success: successGetLinesByCorporation, fail: failureGetLinesByCorporation, dataType: 'json'},
	API_GET_BUS_LIST_BY_LINE_CORPORATION: {url: '/api/get-buses-by-line-corporation/', success: successGetBusesByLineCorporation, fail: failureGetBusesByLineCorporation, dataType: 'json'},
  API_GET_BUS_LIST_BY_LINE_CORPORATION_REPORT: {url: '/api/get-buses-by-line-corporation-report/', success: successGetBusesByLineCorporation, fail: failureGetBusesByLineCorporation, dataType: 'json'},
	API_DOWNLOAD_DAILY_REPORTS_CSV: {url: '/order/daily-reports/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '日報CSV', ext: '.csv'}},
  API_GET_ORDER_SECTION_CONFIRM: {url: '/order/manager/change/section/confirm/api/', success: successOrderSectionConfirm, fail: failureCommonApi},
	API_DOWNLOAD_ROUTES_CSV: {url: '/service/bus-routes/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '系統CSV', ext: '.csv'}},
	API_DOWNLOAD_PERMISSION_CSV: {url: '/system/permissions/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '権限CSV', ext: '.csv'}},
  API_PERFORMANCE_CSV_DOWNLOAD: {url: '/service/performance/csv/download/', success: successDownload, fail: failureCommonApi, param: {ext: '.csv'}},
	API_DOWNLOAD_SERVICE_CORP_CSV: {url: '/master/service-corps/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '運行会社CSV', ext: '.csv'}},
	API_DOWNLOAD_VEHICLES_CSV: {url: '/master/vehicles/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '車両CSV', ext: '.csv'}},
	API_DOWNLOAD_BUS_STOP_CSV: {url: '/master/bus-stops/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '停留所CSV', ext: '.csv'}},
	API_DOWNLOAD_BUS_STOP_GROUP_CSV: {url: '/master/bus-stop-grps/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '停留所グループCSV', ext: '.csv'}},
	API_DOWNLOAD_AGENCY_CSV: {url: '/master/agencies/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '代理店CSV', ext: '.csv'}},
	API_DOWNLOAD_LINES_CSV: {url: '/service/bus-lines/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '路線CSV', ext: '.csv'}},
	API_DOWNLOAD_MEMBERS_CSV: {url: '/site/members/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '会員CSV', ext: '.csv'}},
	API_DOWNLOAD_CITIZEN_MEMBERS_CSV: {url: '/site/citizen-members/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '市民会員CSV', ext: '.csv'}},
	API_DOWNLOAD_USERS_CSV: {url: '/system/users/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: 'ユーザCSV', ext: '.csv'}},
	API_DOWNLOAD_AGENCY_USERS_CSV: {url: '/system/agency-users/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: "代理店ユーザCSV", ext: '.csv'}},
	API_DOWNLOAD_CREW_TERMINALS_CSV: {url: '/system/crew-terminals/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: "乗務員端末CSV", ext: '.csv'}},
  API_CHANGE_ORDER_COMMENT: {url: '/api/service/order-manager/change/order/comment', success: successChangeMessage, fail: failChangeMessage},
  API_GET_LINES: {url: '/api/order/specialist/lines', type: 'GET', success: () => {return (data) => data}, fail: () => {return () => failureCommon('システムエラーが発生しました。')}},
  API_CHANGE_ORDER_DETAIL: { url: '/api/service/order-manager/change/order/detail/', success: successChangeMessage, fail: failOrderChange},
  API_GET_ORDER_CHANGE_HISTORIES: {url: '/api/service/order-manager/get-order-change-histories/', success: successGetOrderChangeHistory, fail: failGetOrderChangeHistory},
}

class CommonAjax {
	constructor(api, data) {
		this.type = api.type??"POST";
		this.url = api.url;
		this.data = data;
		this.dataType = api?.dataType;
		this.successFunction = api.success;
		this.failureFunction = api.fail;
		this.param = api.param;
	}

	callApi() {
		return $.ajax({
			type: this.type,
			url: this.url,
			data: this.data,
			dataType: this.dataType,
			async: false,
			timeout: 100000,
		}).then(this.successFunction(this.param),
						this.failureFunction()
		);
	}
}

/** 共通エラー */
function failureCommon(data) {
  const messages = data.responseJSON;
  if (messages instanceof Array) {
    const errors = [...messages].map(message => `<div class="task-text">${message}</div>`);
    $('.task-danger').append(errors).closest('#messageBox').show();
    return;
  }

  const message = data.responseText;

  /** undefinedの場合は文字列を取得したと判断してdataの中身を直接出力する */
  if(message == undefined){
    $('.task-danger').closest('#messageBox').show();
    const redMessageStyle = getRedMessageStyle(data);
    $('.task-danger').append(`<div class="task-text"${redMessageStyle}>${data}</div>`).show();
    return;
  }

  /** エラーメッセージ設定 */
  $('.task-danger').closest('#messageBox').show();
  const redMessageStyle = getRedMessageStyle(message);
  $('.task-danger').append(`<div class="task-text"${redMessageStyle}>${message}</div>`).show();
}

/** 対象のエラーメッセージが表示されるとき、文字色を赤く表示する */
function getRedMessageStyle(message) {
  const redMessages = ['料金が異なるため、変更できません。'];
  const redMessageStyle = redMessages.includes(message)
    ? ` style="color: red;"`
    : ``;
  return redMessageStyle;
}

/** 会社一覧取得(成功) */
function successGetCorporations() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`会社が存在しません。`);
      return;
    }
    data.forEach(e => {
        appendList += `<option class="searchCorporationId" value="${e.serviceCorporationId}">${e.serviceCorporationName}</option>`;
    });
    $('#searchCorporationId').append(appendList);
  }
}

/** 会社一覧取得(失敗) */
function failureGetCorporations() {
  return () => failureCommon('会社の取得に失敗しました。');
}

/** 路線一覧取得(成功) */
function successGetLinesByCorporation() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`路線が存在しません。`);
      return;
    }
    data.forEach(e => {
      appendList += `<option class="searchLineId" value="${e.lineId}">${e.lineCode}:${e.lineName}</option>`;
    });
    $('#searchLineId').append(appendList);
  }
}

/** 路線一覧取得(失敗) */
function failureGetLinesByCorporation() {
  return () => failureCommon('路線の取得に失敗しました。');
}

/** 便号車一覧取得(成功) */
function successGetBusesByLineCorporation() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`号車が存在しません。`);
      return;
    }
    data.forEach(e => {
      appendList += `<option class="searchBusId" value="${e.busId}">${e.busServiceName} 便（${e.busName} 号車）</option>`;
    });
    $('#searchBusId').append(appendList);
  }
}

/** 便号車一覧取得(失敗) */
function failureGetBusesByLineCorporation() {
  return () => failureCommon('便号車の取得に失敗しました。');
}

/** ダウンロード(成功) */
function successDownload(param) {
	return (data) => {
		// 日本語文字化け回避
		let bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
		let blob = new Blob([bom, data], {
			type: 'text/csv'
		});
		let url = (window.URL || window.webkitURL).createObjectURL(blob);
		let link = document.createElement('a');
		// ファイル名
		link.download = param.fileName + '_' + $.now() + param.ext;
		link.href = url;
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	}
}

/** API失敗 共通処理 */
function failureCommonApi() {
  return (data) => failureCommon(data);
}

/** API失敗 共通処理 */
function failureConfirmCommonApi() {
  $('#confirmBtn').prop('disabled', true);
  return (data) => failureCommon(data.responseText);
}

/** Ajax通信START */
function connectAjax(url, jsonData, $targetObj) {

  $.ajax({
    type : "POST",
    url : url,
    data : jsonData,
    dataType : 'json',
    timeout : 100000,
    success : function(data) {
      setAjaxResultData(url, data, $targetObj);
    },
    error : function(e) {
      setAjaxResultData(url, null, $targetObj);
    }
  });
}

/** Ajax通信START */
function connectAjaxWithRequstBody(url, request, $targetObj) {

  $.ajax({
    type: 'POST',
    url: url,
    data: JSON.stringify(request),
    contentType: 'application/json',
    timeout : 100000,
    success : function(data, textStatus, jqXHR) {
      setAjaxResultData(url, data, $targetObj, jqXHR.status);
    },
    error : function(res) {
      setAjaxResultData(url, res, $targetObj);
    }
  });
}

/** Ajax通信結果メッセージ表示 */
function showAjaxResultMessage(result, message) {
    $successMsgBox.empty();
    $errorMsgBox.empty();
    if(result){
        $successMsgBox.closest('#messageBox').show();
        $errorMsgBox.closest('#messageBox').hide();
        $successMsgBox.append('<div class="task-text">' + message + '</dev>');
    } else {
        $successMsgBox.closest('#messageBox').hide();
        $errorMsgBox.closest('#messageBox').show();
        $errorMsgBox.append('<div class="task-text">' + message + '</dev>');
    }
    return;
}

/** Ajax通信エラー */
function errorAjaxConnect(message) {
  $errorMsgBox.empty();
  $errorMsgBox.closest('.panel-message').show();
  $errorMsgBox.append('<div class="task-text">' + message + '</dev>');
  return;
}
/** MODAL_Ajax通信結果メッセージ表示 */
function showAjaxResultMessageInModal(result, message) {
  $msgBox = result ? $('#modalSuccessMsgBox') : $('#modalErrorMsgBox');
  $msgBox.empty();
  $msgBox.show();
  $msgBox.append('<li>' + message + '</li>');
  return;
}
