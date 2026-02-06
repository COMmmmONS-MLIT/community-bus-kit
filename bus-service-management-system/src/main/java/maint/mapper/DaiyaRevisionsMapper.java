package maint.mapper;

import maint.dto.planDaiya.daiyaRevisions.DaiyaRevisionsDetailDto;
import maint.dto.planDaiya.daiyaRevisions.SearchDaiyaRevisionsDto;
import maint.entity.DaiyaRevisionsEntity;
import maint.dto.selectBox.DaiyaRevisionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 便ダイヤ改正MAPPER
 *
 * @author jeon
 */
@Mapper
public interface DaiyaRevisionsMapper {

  /**
   * 便ダイヤ改正一覧取得
   *
   * @param dto 便ダイヤ改正検索DTO
   * @return 便ダイヤ改正情報検索結果リスト
   */
  List<DaiyaRevisionsDetailDto> getDaiyaRevisionsBySearch(SearchDaiyaRevisionsDto dto);

  /**
   * 便ダイヤ改正一覧件数取得
   *
   * @param dto 便ダイヤ改正検索DTO
   * @return 全件件数
   */
  Long countDaiyaRevisions(SearchDaiyaRevisionsDto dto);

  /**
   * 路線に紐づく便ダイヤ改正数
   *
   * @param rosenId 路線ID
   * @return 便ダイヤ改正数
   */
  Long countDaiyaRevisionForLine(Long rosenId);

  /**
   * 系統管理便ダイヤ改正利用期間チェック
   *
   * @param rosenId 路線ID
   * @param lineRouteId 系統ID
   * @param serviceEndDate 運行終了日
   * @return 便ダイヤ改正数
   */
  Long countDaiyaRevisionForLineRoute(Long rosenId, Long lineRouteId, String serviceEndDate);

  /**
   * 便ダイヤ改正情報取得
   *
   * @param id 便ダイヤ改正ID
   * @return 便ダイヤ改正情報DTO
   */
  DaiyaRevisionsDetailDto getDaiyaRevisionsByPk(Long id);

  /**
   * 便ダイヤ改正登録
   *
   * @param daiyaRevisionsEntity  便ダイヤ改正 Entity
   * @return insert件数
   */
  int registerDaiyaRevision(DaiyaRevisionsEntity daiyaRevisionsEntity);

  /**
   * 便ダイヤ改正変更
   *
   * @param daiyaRevisionsEntity  便ダイヤ改正 Entity
   * @return 更新件数
   */
  int updateDaiyaRevisions(DaiyaRevisionsEntity daiyaRevisionsEntity);

  /**
   * 路線の利用期間内か
   *
   * @param daiyaRevisionsEntity  便ダイヤ改正 Entity
   * @return 件数
   */
  int countRosenAvailableOnServiceStartDate(DaiyaRevisionsEntity daiyaRevisionsEntity);

  /**
   * 一括論理削除(日毎便ダイヤ、便ダイヤ明細、便、便ダイヤ改正)を実施します。
   *
   * @param condition rosenId, serviceStartDate
   */
  void deleteCascadeByRosenAndStartDate(DaiyaRevisionsEntity condition);

  /**
   * 便ダイヤ改正の更新（既存行の更新）
   *
   * @param entity rosenId, serviceStartDate を含む更新条件
   */
  void updateDaiyaRevisionByCondition(DaiyaRevisionsEntity entity);

  /**
   * 便ダイヤ改正日取得
   * @param businessCorpId 事業者ID
   * @return 便ダイヤ改正日セット用DTO
   */
  List<DaiyaRevisionDto> getServiceStartDates(@Param("businessCorpId") Long businessCorpId);

  /**
   * 有効な便ダイヤ改正数を取得
   * 改正が基準日以降で有効かをカウントします（削除=0かつ開始日 > baseDate などの条件を想定）。
   *
   * @param businessCorpId  事業者ID
   * @param daiyaRevisionId ダイヤ改正ID
   * @param baseDate        基準日（この日より後に開始しているかを判定）
   * @return 件数（0:該当なし/1以上:該当あり）
   */
  int countRevisionActiveAfterDateByBusinessCorpId(@Param("businessCorpId") String businessCorpId,
      @Param("daiyaRevisionId") String daiyaRevisionId, @Param("baseDate") LocalDate baseDate);

}
