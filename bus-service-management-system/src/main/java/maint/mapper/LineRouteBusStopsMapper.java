package maint.mapper;

import maint.dto.operation.importBusServices.FindLineRouteStopSequenceDto;
import maint.dto.integrate.gtfsReport.GtfsStopsInfoDto;
import maint.dto.integrate.gtfsReport.GtfsStopsOtherInfoDto;
import maint.entity.LineRouteBusStopsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系統停留所Mapper
 *
 * @author y.kim
 */
@Mapper
public interface LineRouteBusStopsMapper {

  /**
   * 系統に紐づく有効な系統停留所数を取得します
   *
   * @param lineRouteId 系統ID
   * @return 有効な系統停留所レコード数
   */
  int countActiveStopsByLineRoute(@Param("lineRouteId") String lineRouteId);

  /**
   * 路線IDリストから停留所情報を取得
   *
   * @param rosenIdList 路線IDリスト
   * @return 停留所情報出力DTOリスト
   */
  List<GtfsStopsInfoDto> getStopsInfoList(@Param("rosenIdList") List<Long> rosenIdList);

  /**
   * 路線IDリストから停留所の補足情報を取得
   *
   * @param rosenIdList 路線IDリスト
   * @return 標柱情報出力DTOリスト
   */
  List<GtfsStopsOtherInfoDto> getStopsOtherInfoList(@Param("rosenIdList") List<Long> rosenIdList);

  /**
   * 改正ID＋事業者IDを条件に、系統停留所ID・系統ID・運行順序などの一覧を取得します。 Excel検証時の整合性チェック（並び順・存在確認）に利用します。
   *
   * @param daiyaRevisionId ダイヤ改正ID
   * @param businessCorpId  事業者ID
   * @return 系統停留所・順序の情報リスト（0件可）
   */
  List<FindLineRouteStopSequenceDto> findLineRouteStopSequence(
      @Param("daiyaRevisionId") String daiyaRevisionId,
      @Param("businessCorpId") String businessCorpId);

  /**
   * 系統停留所登録
   *
   * @param lineRouteBusStopsEntity 系統停留所 Entity
   * @return 登録件数
   */
  int insert(List<LineRouteBusStopsEntity> lineRouteBusStopsEntity);
}
