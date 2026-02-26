package maint.service.baseInfo;

import maint.dto.ListPager;
import maint.dto.baseInfo.busStops.SearchBusStopsDto;
import maint.form.busStops.BusStopsSearchForm;
import maint.mapper.BusStopMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 停留所SERVICE
 *
 * @author g.ko
 */
@Service
public class BusStopsService extends BaseService {

  /** 停留所MAPPER */
  @Autowired
  BusStopMapper busStopMapper;

  /**
   * 停留所一覧取得
   *
   * @param form 検索FORM
   * @param pager ListPager
   */
  public void getBusStops(BusStopsSearchForm form, ListPager pager) {
    // 検索条件の設定
    SearchBusStopsDto dto = new SearchBusStopsDto(form, pager.getStartIndex(), getUserSession());

    // データ取得・ページ設定
    pager.setTotalListSize(busStopMapper.countBusStopsBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(busStopMapper.getBusStopsBySearch(dto));
    }
  }

}
