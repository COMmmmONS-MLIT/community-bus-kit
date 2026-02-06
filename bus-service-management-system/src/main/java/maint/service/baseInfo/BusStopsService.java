package maint.service.baseInfo;

import maint.dto.ListPager;
import maint.dto.baseInfo.busStops.SearchBusStopsDto;
import maint.entity.BusStopsEntity;
import maint.exception.CommonDbException;
import maint.form.busStops.BusStopsDetailForm;
import maint.form.busStops.BusStopsSearchForm;
import java.util.List;
import maint.dto.baseInfo.busStops.BusStopSelectDto;
import maint.mapper.BusStopMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    SearchBusStopsDto dto = new SearchBusStopsDto(form, pager.getStartIndex(), getUserSession());
    pager.setTotalListSize(busStopMapper.countBusStopsBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(busStopMapper.getBusStopsBySearch(dto));
    }
  }

  /**
   * 停留所登録
   *
   * @param form 詳細FORM
   * @return 停留所ID
   */
  @Transactional
  public Long insert(BusStopsDetailForm form) {
    // 停留所登録
    BusStopsEntity busStopsEntity = new BusStopsEntity(form);

    // 登録失敗時、エラーとする
    if (busStopMapper.registerBusStop(busStopsEntity) != 1) {
      throw new CommonDbException("【停留所管理】登録エラー");
    }

    return busStopsEntity.getBusStopId();
  }

  /**
   * 停留所変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(BusStopsDetailForm form) {
    // 停留所変更
    BusStopsEntity busStopsEntity = new BusStopsEntity(form);

    if (busStopMapper.updateBusStop(busStopsEntity) != 1) {
      throw new CommonDbException("【停留所管理】変更エラー");
    }
  }

  /**
   * 標柱取得（SELECT用）
   *
   * @return 標柱DTOリスト
   */
  public List<BusStopSelectDto> getBusStopsForSelectOption() {
    return busStopMapper.getBusStopsForSelectOption(getUserSession().getBusinessCorpId());
  }

}
