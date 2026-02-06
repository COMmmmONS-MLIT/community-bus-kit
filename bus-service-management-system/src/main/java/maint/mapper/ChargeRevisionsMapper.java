package maint.mapper;

import java.util.List;
import maint.dto.planDaiya.chargeRevisions.ChargeRevisionDto;
import maint.dto.planDaiya.chargeRevisions.SearchChargeRevisionDto;
import maint.dto.integrate.gtfsReport.GtfsFareInfoDto;
import maint.entity.ChargeRevisionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 運賃改定MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface ChargeRevisionsMapper {

  /**
   * 運賃改定数取得
   *
   * @param dto 運賃改定検索DTO
   * @return 運賃改定数
   */
  Long countChargeRevisionsBySearch(SearchChargeRevisionDto dto);

  /**
   * 運賃改定一覧取得
   *
   * @param dto 運賃改定検索DTO
   * @return 運賃改定一覧
   */
  List<ChargeRevisionDto> getChargeRevisionsBySearch(SearchChargeRevisionDto dto);

  /**
   * 運賃改定詳細情報取得
   *
   * @param rosenId 路線ID
   * @param serviceStartDate 運賃適用開始日
   * @param businessCorpId 事業者ID
   * @return 運賃改定DTOリスト
   */
  List<ChargeRevisionDto> getChargeRevisionsByRosenId(Long rosenId, String serviceStartDate,
      Long businessCorpId);

  /**
   * 路線に紐づく運賃改正数
   *
   * @param rosenId 路線ID
   * @return 運賃改正数
   */
  Long countChargeRevisionsForLine(Long rosenId);

  /**
   * 路線IDリストと対象日から運賃情報を取得
   *
   * @param rosenIdList 路線IDリスト
   * @param targetDate 対象日
   * @return 運賃情報出力DTOリスト
   */
  List<GtfsFareInfoDto> getFareInfoList(@Param("rosenIdList") List<Long> rosenIdList,
      @Param("targetDate") String targetDate);

  /**
   * 登録
   *
   * @param chargeRevisionEntity 運賃改定ENTITY
   * @return 登録件数
   */
  int registerChargeRevision(ChargeRevisionEntity chargeRevisionEntity);

  /**
   * 運賃改定終了日更新
   *
   * @param chargeRevisionEntity 運賃改定ENTITY
   * @return 更新件数
   */
  int updateServiceEndDate(ChargeRevisionEntity chargeRevisionEntity);

  /**
   * 削除
   *
   * @param chargeRevisionEntity 運賃改定ENTITY
   * @return 削除件数
   */
  int deleteChargeRevision(ChargeRevisionEntity chargeRevisionEntity);

  /**
   * 運賃改定IDと事業者IDに基づいて有効な運賃改定が存在するかをチェックする
   *
   * @param chargeRevisionId 運賃改定ID（Excelから取得）
   * @param businessCorpId 事業者ID（Excelから取得）
   * @return 存在すれば true、存在しなければ false
   */
  boolean existsValidRevisionWithLine(@Param("chargeRevisionId") String chargeRevisionId,
      @Param("businessCorpId") String businessCorpId);

}
