package maint.mapper;

import maint.dto.planDaiya.dailyBusDaiyas.DailyBusDaiyasDetailDto;
import maint.dto.planDaiya.dailyBusDaiyas.DailyDaiyaOperationHistoryDto;
import maint.dto.planDaiya.dailyBusDaiyas.SearchDailyBusDaiyasDto;
import maint.dto.operation.holidayCalendars.DailyBusDaiyaDto;
import maint.entity.DailyBusDaiyasEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日毎便ダイヤMAPPER
 *
 * @author jeon
 */

@Mapper
public interface DailyBusDaiyasMapper {

  /**
   * 日毎便ダイヤ一覧取得
   *
   * @param dto 日毎便ダイヤ検索DTO
   * @return 日毎便ダイヤ情報検索結果リスト
   */
  List<DailyBusDaiyasDetailDto> getDailyBusDaiyasBySearch(SearchDailyBusDaiyasDto dto);

  /**
   * 日毎便ダイヤ操作履歴を登録する
   *
   * @param dto 登録DTO
   * @return 影響件数
   */
  int insertDailyDaiyaOperationHistory(DailyDaiyaOperationHistoryDto dto);

  /**
   * 日毎便ダイヤ一覧件数取得
   *
   * @param dto 日毎便ダイヤ検索DTO
   * @return 全件件数
   */
  Long countDailyBusDaiyas(SearchDailyBusDaiyasDto dto);

  /**
   * 日毎便ダイヤ情報取得
   *
   * @param id 日毎便ダイヤ Entity
   * @return 日毎便ダイヤ情報DTO
   */
  DailyBusDaiyasDetailDto getDailyBusDaiyasByPk(DailyBusDaiyasEntity id);

  /**
   * 日毎便ダイヤを更新する
   *
   * @param dailyBusDaiyasEntity 日毎便ダイヤEntity
   * @return 更新件数
   */
  int updateDailyBusDaiya(DailyBusDaiyasEntity dailyBusDaiyasEntity);

  /**
   * 登録停留所一覧取得する
   *
   * @param dailyBusDaiyasEntity 日毎便ダイヤ Entity
   * @return 日毎便ダイヤ情報結果リスト
   */
  List<DailyBusDaiyasDetailDto> getlineRouteBusStopsList(DailyBusDaiyasEntity dailyBusDaiyasEntity);

  /**
   * 日毎便ダイヤ操作履歴を取得する
   *
   * @param dailyBusDaiyaId 日毎便ダイヤID
   * @return 操作履歴リスト
   */
  List<DailyDaiyaOperationHistoryDto> getDailyDaiyaOperationHistories(
      @Param("dailyBusDaiyaId") Long dailyBusDaiyaId
  );

  /**
   * 日毎便ダイヤの存在確認（同一条件で存在するか）
   *
   * @param id 条件エンティティ
   * @return 件数（0: 不存在, 1以上: 存在）
   */
  Long existsDailyBusDaiyasByPk(DailyBusDaiyasEntity id);

  /**
   * 便IDから日毎便ダイヤを取得
   *
   * @param busServiceId 便ID
   * @param startDate 開始日
   * @param endDate 終了日
   * @return 日毎便ダイヤリスト
   */
  List<DailyBusDaiyaDto> getDailyBusDaiyasByBusServiceId(@Param("busServiceId") Long busServiceId,
      @Param("startDate") String startDate, @Param("endDate") String endDate);

  /**
   * 便IDから対象日の日毎便ダイヤを取得
   *
   * @param busServiceId 便ID
   * @param targetDates 対象日リスト
   * @return 日毎便ダイヤリスト
   */
  List<DailyBusDaiyaDto> getDailyBusDaiyasByServiceIdAndDateList(
      @Param("busServiceId") Long busServiceId, @Param("targetDates") List<String> targetDates);

  /**
   * 便IDから対象日の日毎便ダイヤを取得
   *
   * @param busServiceId 便ID
   * @param targetDates 対象日リスト
   * @return 日毎便ダイヤリスト
   */
  List<Long> getDailyBusDaiyasByServiceIdAndDateListNotIn(
      @Param("busServiceId") Long busServiceId, @Param("targetDates") List<String> targetDates);

  /**
   * 日毎便ダイヤ登録
   *
   * @param dailyBusDaiyasEntity 日毎便ダイヤEntity
   * @return 登録数
   */
  int registerDailyBusDaiya(DailyBusDaiyasEntity dailyBusDaiyasEntity);

  /**
   * 日毎便ダイヤ更新
   *
   * @param dailyBusDaiyasEntity 日毎便ダイヤEntity
   * @return 更新数
   */
  int updateDailyBusDaiyaForRebuild(DailyBusDaiyasEntity dailyBusDaiyasEntity);

  /**
   * 日毎便ダイヤ削除
   *
   * @param dailyBusDaiyasEntity 日毎便ダイヤEntity
   * @return 削除数
   */
  int deleteDailyBusDaiya(DailyBusDaiyasEntity dailyBusDaiyasEntity);

}
