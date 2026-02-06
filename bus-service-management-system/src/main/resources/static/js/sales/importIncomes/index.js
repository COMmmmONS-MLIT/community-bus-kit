$(function () {
  const $rosen = $('#rosenSelect');
  const $date = $('#serviceDateInput');
  const $bus = $('#busServiceSelect');

  $rosen.select2(
      {width: '300px', placeholder: '------------', allowClear: true});
  $bus.select2({width: '300px', placeholder: '------------', allowClear: true});

  const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
  const csrfToken = $('meta[name="_csrf"]').attr('content');

  function clearBusOptions() {
    $bus.empty().append(new Option('------------', ''));
  }

  function enableBus() {
    $bus.prop('disabled', false).trigger('change.select2');
  }

  function loadBusServices(rosenId, serviceDate, preselectValue) {
    const hasRosen = !!rosenId;
    const hasDate = !!serviceDate;

    if (!hasRosen && !hasDate) {
      clearBusOptions();
      enableBus();
      return;
    }

    $.ajax({
      url: '/sales/importIncomes/getBusServiceOption',
      type: 'GET',
      data: {rosenId: rosenId || '', serviceDate: serviceDate || ''},
      // ...생략...
    }).done(function (list) {
      clearBusOptions();
      if (Array.isArray(list) && list.length > 0) {
        list.forEach(function (opt) {
          $bus.append(
              new Option(opt.busServiceName, opt.busServiceId, false, false));
        });

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
    loadBusServices($rosen.val(), $date.val(), null);
  });

  $date.on('change', function () {
    loadBusServices($rosen.val(), $date.val(), null);
  });

  const initialRosenId = $rosen.val();
  const initialDate = $date.val();
  const initialBusId = $bus.data('initial-bus');

  if (initialRosenId || initialDate) {
    loadBusServices(initialRosenId, initialDate, initialBusId);
  } else {
    clearBusOptions();
    enableBus();
  }
});
