package maint.mapper;

import java.util.List;

import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.dto.operation.importBusServices.ImportBusServicesInsertDto;
import maint.dto.integrate.gtfsReport.GtfsBusServiceTripDto;
import maint.dto.integrate.gtfsReport.GtfsCalendarDayTypeDto;
import maint.dto.sales.importIncomes.BusServiceOptionsDto;
import maint.entity.BusServiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 便MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface BusServicesMapper {

  /**
   * 便データ取得
   *
   * @param businessCorpId 事業者ID
   * @return 便データリスト
   */
  List<BusServiceInfoDto> getBusServicesByBusinessCorpId(
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 便取得（SELECT用）
   *
   * @param rosenId 路線ID
   * @param serviceDate 運行日
   * @return 便リスト
   */
  List<BusServiceOptionsDto> getBusServiceOptions(@Param("rosenId") Long rosenId,
      @Param("serviceDate") String serviceDate, @Param("businessCorpId") Long businessCorpId);

  /**
   * 指定されたGTFSレポートフォームから、曜日別の運行種別情報を取得します。
   * この情報は、calendar.txtの生成に使用されます。
   *
   * @param daiyaRevisionIdList 便ダイヤ改定IDリスト
   * @return カレンダー情報出力DTOリスト
   */
  List<GtfsCalendarDayTypeDto> getCalendarDayTypeList(
      @Param("daiyaRevisionIdList") List<Long> daiyaRevisionIdList);

  /**
   * 便ダイヤ改定IDリストからバス運行情報を取得
   *
   * @param daiyaRevisionIdList 便ダイヤ改定IDリスト
   * @return 便・通過時刻・帰属情報出力Dtoリスト
   */
  List<GtfsBusServiceTripDto> getBusServiceTripList(
      @Param("daiyaRevisionIdList") List<Long> daiyaRevisionIdList);

  /**
   * 便データ取得
   *
   * @param businessCorpId 事業者ID
   * @param busServiceIdList 便IDリスト
   * @return 便データリスト
   */
  List<BusServiceInfoDto> getBusServicesByBusServiceId(@Param("businessCorpId") Long businessCorpId,
      @Param("busServiceIdList") List<Long> busServiceIdList);

  /**
   * 便データ取得
   *
   * @param businessCorpId 事業者ID
   * @param calendarId 運行カレンダーID
   * @return 便データリスト
   */
  List<BusServiceInfoDto> getBusServicesByCalendarId(@Param("businessCorpId") Long businessCorpId,
      @Param("calendarId") Long calendarId);

  /**
   * 便ダイヤ改正IDから便番号数取得
   *
   * @param daiyaRevisionId 改正ID
   * @param busServiceTripId 便番号
   * @param busServiceId 便ID
   * @return 件数
   */
  int countBusServiceTripIdByRevisionId(@Param("daiyaRevisionId") String daiyaRevisionId,
      @Param("busServiceTripId") String busServiceTripId,
      @Param("busServiceId") String busServiceId);

  /**
   * 便データ登録
   *
   * @param entity 便データDTO
   * @return 登録件数
   */
  int insertBusService(BusServiceEntity entity);

  /**
   * 便の更新
   *
   * @param entity 便 Entity
   * @return 更新件数
   */
  int updateBusService(BusServiceEntity entity);

  /**
   * 便データを登録します。
   *
   * @param dto 登録対象の便DTO
   * @return 登録件数
   */
  int insertBusServiceForImport(ImportBusServicesInsertDto dto);

}
