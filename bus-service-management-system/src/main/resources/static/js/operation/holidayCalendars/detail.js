var getSystemCalendar = '/api/operation/holidayCalendar/get-calendar';
var srcCalendarEl = document.getElementById('first-calendar');
var destCalendarEl = document.getElementById('second-calendar');

/**
 * 画面読み込み時処理
 */
$(document).ready(function() {
    const date = new Date();
    var currentMonthFirstDay = date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, "0") + '-01';
    var nextMonthFirstDay = date.getFullYear() + '-' + (date.getMonth() + 2).toString().padStart(2, "0") + '-01';
    var srcEvent = [];
    var destEvent = [];
    setEvent(srcEvent, $lastMonth);
    setEvent(srcEvent, $currentMonth);
    setEvent(srcEvent, $nextMonth);
    setEvent(destEvent, $currentMonth);
    setEvent(destEvent, $nextMonth);
    setEvent(destEvent, $twoMonthsLater);
    renderSrcCalendar(srcEvent, currentMonthFirstDay);
    renderDestCalendar(destEvent, nextMonthFirstDay);
    $(window).resize();
});

/**
 * イベントにオブジェクトを設定
 *
 * @param {Object} event イベント
 * @param {Array} $months 月リスト
 */
function setEvent(event, $months) {
  for (let i = 0; i < $months.length; i++) {
    var $month = $months[i];
    event.push({
      start: $month.holidayDate,
      display: 'background',
      rendering: 'background'
    });
  }
}

/**
 * 祝日の背景色の設定
 *
 * @param {string} calendarSelector カレンダー要素のセレクタ
 * @param {Array<Object>} events 対象日付を含むイベント配列
 */
function setHolidayBackground(calendarSelector, events) {
  if (!events || !events.length) return;
  events.forEach(function(ev) {
    var dateStr =
        (ev && typeof ev.start === 'string' && ev.start) ||
        (ev && ev.start instanceof Date && ev.start.toISOString().slice(0, 10)) ||
        (ev && ev.startStr) || '';
    if (!dateStr) return;
    $(calendarSelector).find('[data-date="' + dateStr + '"]').addClass('fc-sun');
  });
}

/**
 * 左カレンダー描画
 *
 * @param {Object} srcEvent イベント
 * @param {String} defaultDate 基準日
 */
function renderSrcCalendar(srcEvent, defaultDate) {
    let srcEventObject = getCommonEventObject();
    srcEventObject.header = { left: 'prev', center: 'title', right: '' };
    srcEventObject.events = srcEvent;
    srcEventObject.defaultDate = defaultDate;
    var srcCalendar = new FullCalendar.Calendar(srcCalendarEl, srcEventObject);
    srcCalendar.render();
    setTimeout(function(){ setHolidayBackground('#first-calendar', srcEvent); }, 0);
}

/**
 * 右カレンダー描画
 *
 * @param {object} destEvent イベント
 * @param {String} defaultDate 基準日
 */
function renderDestCalendar(destEvent, defaultDate) {
    let destEventObject = getCommonEventObject();
    destEventObject.header = { left: '', center: 'title', right: 'next' };
    destEventObject.events = destEvent;
    destEventObject.defaultDate = defaultDate;
    var destCalendar = new FullCalendar.Calendar(destCalendarEl, destEventObject);
    destCalendar.render();
    setTimeout(function(){ setHolidayBackground('#second-calendar', destEvent); }, 0);
}

/**
 * 左カレンダーの年と、月の取得
 *
 * @return { srcYear, srcMonth }
 */
function getSrcYearAndMonth() {
    var firstFirstDate = $("#first-calendar .fc-body td:not(.fc-other-month).fc-day-top:first").data("date").split("-");
    var srcYear = firstFirstDate[0];
    var srcMonth = firstFirstDate[1];
    return { srcYear, srcMonth };
}

/**
 * 共通描画
 *
 * @param {Object} srcEvent 左カレンダーイベント
 * @param {Object} destEvent 右カレンダーイベント
 * @param {String} srcDefaultDate 左カレンダー基準日
 * @param {String} destDefaultDate 右カレンダー基準日
 */
function commonRender(srcEvent, destEvent, srcDefaultDate, destDefaultDate) {
    srcCalendarEl.innerHTML = '';
    destCalendarEl.innerHTML = '';
    renderSrcCalendar(srcEvent, srcDefaultDate);
    renderDestCalendar(destEvent, destDefaultDate);
}

/**
 * 共通のイベントオブジェクト取得
 *
 * @return {Object} 共通イベントオブジェクト
 */
function getCommonEventObject() {
  var commonEventObject = {
    plugins: ['interaction', 'dayGrid'],
    timezone: 'Asia/Tokyo',
    selectable: true,
    locale: 'ja',
    eventOrder: 'id',
    eventBackgroundColor: 'transparent',
    eventBorderColor: 'transparent'
  };
  return commonEventObject;
}

/**
 * 前月ボタン押下時に対象データを取得、設定
 */
$(document).on('click', '.fc-left', function() {
    $('#messageBox').hide();
    $('.fc-button').prop('disabled', true);
    var { srcYear, srcMonth } = getSrcYearAndMonth();
    srcMonth = Number(srcMonth - 1);
    if (srcMonth === 0) {
        srcMonth = 12;
        srcYear = Number(srcYear) - 1;
    } else {
        srcMonth = srcMonth.toString().padStart(2, "0");
    }
    var req = { srcYear: srcYear, srcMonth: srcMonth };
    //システムカレンダー取得処理の呼び出し
    callSytemCalendarProcess(req, getSystemCalendar).then(
        function(res) {
            setEventDefaultDate(res, srcYear, srcMonth);
        },
        function(error) {
            return errorHandle(error);
        });;
});

/**
 * 次月ボタン押下時に対象データを取得、設定
 */
$(document).on('click', '.fc-right', function() {
    $('#messageBox').hide();
    $('.fc-button').prop('disabled', true);
    var secondFirstDate = $("#second-calendar .fc-body td:not(.fc-other-month).fc-day-top:first").data("date").split("-");
    let srcYear = Number(secondFirstDate[0]);
    let srcMonth = Number(secondFirstDate[1]) + 1;
    if ((srcMonth - 1) === 0) {
        srcMonth = 12;
        srcYear = srcYear - 1;
    } else {
        srcMonth = srcMonth - 1;
        srcMonth = srcMonth.toString().padStart(2, "0");
    }
    var req = { srcYear: srcYear, srcMonth: srcMonth }
    //システムカレンダー取得処理の呼び出し
    callSytemCalendarProcess(req, getSystemCalendar).then(
        function(res) {
            setEventDefaultDate(res, srcYear, srcMonth);
        },
        function(error) {
            return errorHandle(error);
        });
});

/**
 * システムカレンダー取得処理の呼び出し
 *
 * @param {Object} req リクエストパラメータ
 * @param {String} url 呼び出す処理のパス
 * @return システムカレンダー登録処理結果
 */
function callSytemCalendarProcess(req, url) {
    return $.ajax({
        type: 'post',
        url: url,
        data: JSON.stringify(req),
        contentType: 'application/json'
    })
}

/**
 * イベントとデフォルト日の設定
 *
 * @param {Object} res レスポンス
 * @param {String} srcYear 左カレンダー年
 * @param {String} srcMonth 左カレンダー月
 */
function setEventDefaultDate(res, srcYear, srcMonth) {
    var { srcEvent, destEvent } = getCalendar(res);
    let destYear = Number(srcYear);
    let srcDefaultDate = srcYear + "-" + srcMonth + "-01";
    let destMonth = Number(srcMonth) + 1;
    if (destMonth === 13) {
        destMonth = 1;
        destYear += 1;
    }
    destMonth = destMonth.toString().padStart(2, "0");
    let destDefaultDate = destYear + "-" + destMonth + "-01";
    commonRender(srcEvent, destEvent, srcDefaultDate, destDefaultDate);
}

/**
 * 月ごとのカレンダーの取得
 *
 * @param {object} res レスポンス
 * @return イベントオブジェクト
 */
function getCalendar(res) {
    var srcEvent = [];
    var destEvent = [];
    setEvent(srcEvent, res.lastMonth);
    setEvent(srcEvent, res.currentMonth);
    setEvent(srcEvent, res.nextMonth);
    setEvent(destEvent, res.currentMonth);
    setEvent(destEvent, res.nextMonth);
    setEvent(destEvent, res.twoMonthsLater);
    return { srcEvent, destEvent };
}

(function enforceHolidayBackgroundOnly() {
  var css = `
#first-calendar .fc-daygrid-day.fc-sun .fc-daygrid-day-frame,
#second-calendar .fc-daygrid-day.fc-sun .fc-daygrid-day-frame {
  background-color: rgba(192, 0, 0, .12) !important;
}

#first-calendar td.fc-day.fc-sun,
#second-calendar td.fc-day.fc-sun {
  background-color: rgba(192, 0, 0, .12) !important;
}

#first-calendar .fc-daygrid-day .fc-daygrid-day-number,
#second-calendar .fc-daygrid-day .fc-daygrid-day-number,
#first-calendar td.fc-day .fc-day-number,
#second-calendar td.fc-day .fc-day-number,
#first-calendar .fc-daygrid-day a.fc-daygrid-day-number,
#second-calendar .fc-daygrid-day a.fc-daygrid-day-number,
#first-calendar td.fc-day a.fc-day-number,
#second-calendar td.fc-day a.fc-day-number {
  color: #000 !important;
}

#first-calendar [data-date].fc-sun,
#second-calendar [data-date].fc-sun,
#first-calendar td.fc-day.fc-sun,
#second-calendar td.fc-day.fc-sun {
  color: inherit !important;
}
`;
  var style = document.getElementById('fc-holiday-enforce') || document.createElement('style');
  style.id = 'fc-holiday-enforce';
  style.textContent = css;
  document.head.appendChild(style);
})();

/** CSVファイル設定 */
$(function () {
  var $file = $('#csvInput');
  var $name = $('#csvFileName');

  $file.on('change', function () {
    if (this.files && this.files.length) {
      var names = $.map(this.files, function (f) { return f.name; }).join(', ');
      $name.val(names);
    } else {
      $name.val('');
    }
  });
});
$(function(){
  $('#csvInput').on('change', function(){
    var names = this.files && this.files.length ? $.map(this.files, f => f.name).join(', ') : '';
    $('#csvFileName').val(names);
  });
});
