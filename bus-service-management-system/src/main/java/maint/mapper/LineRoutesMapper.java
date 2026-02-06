package maint.mapper;

import maint.dto.operation.lineRoutes.SearchLineRoutesDto;
import maint.dto.integrate.gtfsReport.GtfsLineRouteDto;
import maint.entity.LineRoutesEntity;
import maint.form.lineRoutes.LineRoutesDetailForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系統Mapper
 *
 * @author y.kim
 */
@Mapper
public interface LineRoutesMapper {

  /**
   * 系統ロール件数取得(Page用)
   *
   * @param dto 系統検索DTO
   * @return 系統ロール件数
   */
  Long countLineRoutes(SearchLineRoutesDto dto);

  /**
   * 系統ロールリスト取得(Page用)
   *
   * @param dto 系統検索DTO
   * @return 系統ロールリスト
   */
  List<SearchLineRoutesDto> getLineRoutes(SearchLineRoutesDto dto);

  /**
   * 系統登録
   *
   * @param entity 系統 Entity
   * @return 登録件数
   */
  Long insert(LineRoutesEntity entity);

  /**
   * 系統更新
   *
   * @param entity 系統 Entity
   * @return 更新件数
   */
  int update(LineRoutesEntity entity);

  /**
   * 系統取得
   *
   * @param lineRouteId PKID
   * @return 系統
   */
  LineRoutesDetailForm getLineRouteByPkId(Long lineRouteId);

  /**
   * 路線に紐づく系統数
   *
   * @param rosenId 路線ID
   * @return 系統数
   */
  Long countLineRoutesForLine(Long rosenId);

  /**
   * 利用期間内の停留所チェック
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate   利用終了日
   * @param rosenId          路線ID
   * @return 停留所数
   */
  Long countLineRouteByDateAndLine(String serviceStartDate, String serviceEndDate, Long rosenId);

  /**
   * 路線IDリストから経路情報を取得
   *
   * @param rosenIdList 路線IDリスト
   * @return 経路情報出力DTOリスト
   */
  List<GtfsLineRouteDto> getLineRouteList(@Param("rosenIdList") List<Long> rosenIdList);
}
