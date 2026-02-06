package maint.mapper;

import maint.dto.operation.lineCharges.LineChargeDetailDto;
import maint.entity.LineChargeDetailsEntity;
import maint.entity.LineChargeDetailsInsertEntity;
import maint.session.UserSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 運賃明細MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface LineChargeDetailsMapper {

  /**
   * 運賃詳細情報取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @return 運賃詳細情報DTO
   */
  List<LineChargeDetailDto> getLineChargeDetailsByRevisionId(@Param("chargeRevisionId") Long chargeRevisionId);


  /**
   * (路線停留所from,路線停留所to)の運賃詳細IDの取得
   *
   *  @param lineChargeId 運賃ID（ヘッダのID）
   *  @param fromId 出発停留所ID（From）
   *  @param toId 到着停留所ID（To）
   *  @return 運賃詳細ID（存在しない場合はnull）
   */
  Long findDetailId(Long lineChargeId, Long fromId, Long toId);

  /**
   * 登録
   *
   * @param entity 運賃詳細Entity
   * @return 登録数
   */
  int registerLineChargeDetail(LineChargeDetailsEntity entity);

  /**
   * 更新
   *
   * @param entity 運賃詳細Entity
   * @return 更新数
   */
  int updateLineChargeDetail(@Param("entity") LineChargeDetailsEntity entity,
      @Param("session") UserSession session);

  /**
   * 運賃明細情報を登録する
   * @param dto 運賃明細DTO
   * @return 登録件数（通常は1）
   */
  int insertLineChargeDetail(LineChargeDetailsInsertEntity dto);

  /**
   * 削除
   *
   * @param session ユーザーセッション
   * @param lineChargeId 運賃ID
   * @return 削除件数
   */
  int deleteChargeDetails(@Param("session") UserSession session,
      @Param("lineChargeId") Long lineChargeId);
}
