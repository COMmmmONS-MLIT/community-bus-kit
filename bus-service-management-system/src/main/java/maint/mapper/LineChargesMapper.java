package maint.mapper;

import java.util.List;
import maint.dto.operation.lineCharges.LineChargeIdDivisionDto;
import maint.entity.LineChargesInfoReportListsExcelEntity;
import maint.session.UserSession;
import maint.dto.operation.lineCharges.LineChargeDto;
import maint.dto.operation.lineCharges.SearchLineChargesDto;
import maint.entity.LineChargesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 運賃MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface LineChargesMapper {

  /**
   * 運賃改定IDから運賃取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @return 運賃ID・区分DTOリスト
   */
  List<LineChargeIdDivisionDto> getLineChargesByRevisionId(
      @Param("chargeRevisionId") Long chargeRevisionId);

  /**
   * 削除
   *
   * @param session ユーザーセッション
   * @param lineChargeId 運賃ID
   * @return 削除件数
   */
  int deleteLineCharge(@Param("session") UserSession session,
      @Param("lineChargeId") Long lineChargeId);

  /**
   * 運賃数取得
   *
   * @param dto 運賃検索DTO
   * @return 運賃件数
   */
  Long countLineChargesBySearch(SearchLineChargesDto dto);

  /**
   * 運賃一覧取得
   *
   * @param dto 運賃検索DTO
   * @return 運賃一覧
   */
  List<LineChargeDto> getLineChargesBySearch(SearchLineChargesDto dto);

  /**
   * 登録
   *
   * @param entity 運賃Entity
   * @return 登録数
   */
  int registerLineCharge(LineChargesEntity entity);

  /**
   * 変更
   *
   * @param entity 運賃Entity
   * @return 変更数
   */
  int updateLineCharge(LineChargesEntity entity);

  /**
   * 運賃情報を登録する
   * @param dto 運賃情報DTO
   * @return 登録された運賃ID（自動採番）
   */
  int insertLineCharge(LineChargesInfoReportListsExcelEntity dto);

}
