package maint.mapper;

import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.entity.BusStopsLinkEntity;
import maint.entity.LineBusStopsReportListsExcelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 停留所MAPPER(停留所・標柱管理画面から)
 *
 * @author k.takeuchi
 */

@Mapper
public interface BusStopLinkMapper {

  /**
   * 停留所情報取得
   *
   * @param Id PK
   * @return 停留所情報
   */
  BusStopLinkDetailDto getBusStopByPk(Long Id);

  /**
   * 登録
   *
   * @param busStopsLinkEntity 停留所ENTITY
   * @return 登録数
   */
  int registerBusStop(BusStopsLinkEntity busStopsLinkEntity);

  /**
   * 変更
   *
   * @param busStopsLinkEntity 停留所ENTITY
   * @return 変更数
   */
  int updateBusStop(BusStopsLinkEntity busStopsLinkEntity);

  /**
   * 利用期間による停留所数取得（系統利用期間チェック用）
   *
   * @param busStopId 停留所ID
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @return 停留所数
   */

  Long countBusStopsByRouteDate(@Param("busStopId") Long busStopId,
      @Param("serviceStartDate") String serviceStartDate,
      @Param("serviceEndDate") String serviceEndDate);

  /**
   * 指定された停留所コードが、指定された有効期間内に既に存在するかをチェックする。
   *
   * @param businessCorpId 事業者ID
   * @param busStopId      停留所ID
   * @param stopCode       停留所コード
   * @param excelStart     利用開始年月日
   * @param excelEnd       利用終了年月日
   * @return 重複件数
   */
  int countDuplicateStopCode(Long businessCorpId, Long busStopId, String stopCode,
      String excelStart, String excelEnd);

  /**
   * 系統停留所IDに対応する「運行順序（停留所の並び順）」を取得します。
   *
   * @param lineRouteBusStopId 系統停留所ID
   * @return 運行順序（1以上想定／未該当時は0等の実装依存）
   */
  int getBusStopSeqSequence(@Param("lineRouteBusStopId") String lineRouteBusStopId);

  /**
   * 停留所情報を登録する。
   *
   * @param lineBusStop 登録対象の停留所情報
   * @return
   */
  int insertLineBusStop(LineBusStopsReportListsExcelEntity lineBusStop);

}
