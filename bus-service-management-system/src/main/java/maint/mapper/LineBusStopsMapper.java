package maint.mapper;

import maint.dto.operation.lineCharges.LineBusStopDto;
import maint.entity.LineBusStopsEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 路線停留所Mapper
 *
 * @author y.kim
 */
@Mapper
public interface LineBusStopsMapper {

  /**
   * 有効な路線停留所取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @param businessCorpId   事業者ID
   * @return 路線停留所リスト
   */
  List<Map<String, Object>> selectValidStopsForRevision(String chargeRevisionId,
      String businessCorpId);

  /**
   * 三角運賃の最終行チェック用：乗車停留所に対応する降車候補停留所を取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @param rosenId 路線ID
   * @param fromStopId 乗車停留所ID
   * @return 降車候補停留所のリスト
   */
  List<Map<String, Object>> getExpectedToStops(
      @Param("chargeRevisionId") String chargeRevisionId,
      @Param("rosenId") String rosenId,
      @Param("fromStopId") String fromStopId
  );

  /**
   * 路線停留所登録
   *
   * @param lineBusStopsEntity 路線停留所 Entity
   * @return 登録件数
   */
  int insert(List<LineBusStopsEntity> lineBusStopsEntity);

  /**
   * 路線停留所更新
   *
   * @param lineBusStopsEntity 路線停留所 Entity
   * @return 更新件数
   */
  int update(List<LineBusStopsEntity> lineBusStopsEntity);
}
