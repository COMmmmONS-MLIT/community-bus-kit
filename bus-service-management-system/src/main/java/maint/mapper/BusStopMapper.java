package maint.mapper;

import java.util.List;
import maint.dto.baseInfo.busStops.BusStopDetailDto;
import maint.dto.baseInfo.busStops.BusStopDto;
import maint.dto.baseInfo.busStops.BusStopSelectDto;
import maint.dto.baseInfo.busStops.SearchBusStopsDto;
import maint.entity.BusStopsEntity;
import maint.entity.LineBusStopsReportListsExcelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 停留所MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface BusStopMapper {

  /**
   * 停留所数取得
   *
   * @param dto 停留所検索DTO
   * @return 停留所数
   */
  Long countBusStopsBySearch(SearchBusStopsDto dto);

  /**
   * 停留所一覧取得
   *
   * @param dto 停留所検索DTO
   * @return 停留所一覧
   */
  List<BusStopDto> getBusStopsBySearch(SearchBusStopsDto dto);

  /**
   * 停留所情報取得
   *
   * @param Id PK
   * @return 停留所情報
   */
  BusStopDetailDto getBusStopByPk(Long Id);

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
   * 停留所取得（SELECT用）
   *
   * @return 停留所リスト
   */
  List<BusStopSelectDto> getBusStopsForSelectOption(
      @Param("operatingCompanyId") Long operatingCompanyId);

  /**
   * 利用期間から停留所リスト取得
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param businessCorpId 事業者ID
   * @return 停留所リスト
   */
  List<BusStopsEntity> getBusStopListByServiceDate(String serviceStartDate,
      String serviceEndDate, Long businessCorpId);

  /**
   * 指定された停留所コードが、指定された有効期間内に既に存在するかをチェックする。
   *
   * @param businessCorpId 事業者ID
   * @param stopCode       停留所コード
   * @param excelStart     利用開始年月日
   * @param excelEnd       利用終了年月日
   * @return 重複件数
   */
  int countDuplicateStopCode(Long businessCorpId, String stopCode, String excelStart,
      String excelEnd);

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

