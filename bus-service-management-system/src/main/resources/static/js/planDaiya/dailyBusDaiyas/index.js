$(function () {
    // 路線
    const $rosen = $('#rosenSelect');
    // 運行期間（開始）
    const $serviceStartDate = $('#serviceStartDate');
    // 運行期間（終了）
    const $serviceEndDate = $('#serviceEndDate');
    // 運休フラグ
    const $salesCancelFlg = $('input[name="searchSalesCancelFlg"]');
    // 始発出発時刻（開始）
    const $busStopDepartureTimeStart = $("#busStopDepartureTimeStart");
    // 始発出発時刻（終了）
    const $busStopDepartureTimeEnd = $("#busStopDepartureTimeEnd");

    const $date = $('#serviceDateInput');
    // 便名セレクト
    const $bus = $('#busServiceSelect');

    $rosen.select2(
        {placeholder: '------------', allowClear: true});
    $bus.select2({placeholder: '------------', allowClear: true});

    function clearBusOptions() {
        $bus.empty().append(new Option('------------', ''));
    }

    function enableBus() {
        $bus.prop('disabled', false).trigger('change.select2');
    }

    function loadBusServices(salesCancelFlg, serviceStartDate,
                             serviceEndDate, rosenId, busStopDepartureTimeStart,
                             busStopDepartureTimeEnd, preselectValue) {
        const hasRosen = !!rosenId;

        if (!hasRosen) {
            clearBusOptions();
            enableBus();
            return;
        }

        const formData = {
            searchRosenId : rosenId,
            searchServiceStartDate : serviceStartDate,
            searchServiceEndDate : serviceEndDate,
            searchBusStopDepartureTimeStart : busStopDepartureTimeStart,
            searchBusStopDepartureTimeEnd : busStopDepartureTimeEnd,
            searchSalesCancelFlg : salesCancelFlg,
        }

        $.ajax({
            url: '/planDaiya/dailyBusDaiyas/getBusServiceOption',
            type: 'GET',
            data: formData,
        }).done(function (list) {
            clearBusOptions();
            if (Array.isArray(list) && list.length > 0) {
                for (opt of list) {
                    let optBusServiceText = opt.busServiceName;
                    if (!optBusServiceText) {
                        optBusServiceText = opt.busServiceTripId;
                    }
                    $bus.append(
                        new Option(optBusServiceText, opt.busServiceId, false, false));
                }

                if (preselectValue) {
                    if (list.some(
                        o => String(o.busServiceId) === String(preselectValue))) {
                        $bus.val(preselectValue).trigger('change');
                    } else {
                        $bus.val(null).trigger('change');
                    }
                } else {
                    $bus.val(null).trigger('change');
                }
            } else {
                $bus.val(null).trigger('change');
            }
        }).fail(function () {
            clearBusOptions();
        });
    }

    $rosen.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });

    $serviceStartDate.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });

    $serviceEndDate.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });

    $salesCancelFlg.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });

    $busStopDepartureTimeStart.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });

    $busStopDepartureTimeEnd.on('change', function () {
        loadBusServices($salesCancelFlg.filter(':checked').val()
            , $serviceStartDate.val()
            , $serviceEndDate.val()
            , $rosen.val()
            , $busStopDepartureTimeStart.val()
            , $busStopDepartureTimeEnd.val()
            , null);
    });


    const initialRosenId = $rosen.val();
    const initialServiceStartDate = $serviceStartDate.val();
    const initialServiceEndDate = $serviceEndDate.val();
    const initialSalesCancelFlg = $salesCancelFlg.filter(':checked').val();
    const initialBusStopDepartureTimeStart = $busStopDepartureTimeStart.val();
    const initialBusStopDepartureTimeEnd = $busStopDepartureTimeEnd.val();
    const initialBusId = $bus.data('initial-bus');

    if (initialRosenId) {
        loadBusServices(initialSalesCancelFlg
            , initialServiceStartDate
            , initialServiceEndDate
            , initialRosenId
            , initialBusStopDepartureTimeStart
            , initialBusStopDepartureTimeEnd
            , initialBusId);
    } else {
        clearBusOptions();
        enableBus();
    }
});
