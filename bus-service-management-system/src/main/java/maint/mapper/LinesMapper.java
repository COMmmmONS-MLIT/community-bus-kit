package maint.mapper;

import maint.dto.document.DistanceReportDto;
import maint.dto.document.StopMapListDownloadDto;
import maint.dto.opeInfo.StaffReportListDownloadDto;
import maint.dto.plan.RouteTimetableListDownloadDto;
import maint.dto.plan.fareReport.FareReportDownLoadDto;
import maint.dto.plan.fareReport.FareReportListsSelectDto;
import maint.dto.operation.displayDaiyas.DisplayDaiyasDto;
import maint.dto.operation.lineRoutes.LineRouteBusStopsDto;
import maint.dto.record.operationRecords.OperationRecordsDto;
import maint.dto.selectBox.*;
import maint.dto.integrate.gtfsReport.GtfsDaiyaRevisionDto;
import maint.dto.export.lists.ReportDownLoadDto;
import maint.dto.record.operatingReport.OperatingReportDaiyasRegisterDownLoadDto;
import maint.dto.record.operatingReport.OperatingReportListsSelectDto;
import maint.entity.LinesEntity;
import maint.form.displayDaiyas.DisplayDaiyasSearchForm;
import maint.form.distanceReport.DistanceReportForm;
import maint.form.fareReport.FareReportListsExcelForm;
import maint.form.lines.LinesDetailForm;
import maint.dto.operation.lines.LinesSelectDto;
import maint.dto.operation.lines.SearchLinesDto;
import maint.form.operatingReport.OperatingReportListsExcelForm;
import maint.form.report.ReportListsExcelForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 路線Mapper
 *
 * @author y.kim
 */
@Mapper
public interface LinesMapper {

  /**
   * 全路線取得（SELECT用）
   * @param businessCorpId 事業者ID
   * @return 路線リスト
   */
  List<LinesSelectDto> getAllLinesForSelectOption(@Param("businessCorpId") Long businessCorpId);

  /**
   * 路線ロール件数取得(Page用)
   *
   * @param dto 路線検索DTO
   * @return 路線ロール件数
   */
  Long countLines(SearchLinesDto dto);

  /**
   * 路線ロールリスト取得(Page用)
   *
   * @param dto 路線検索DTO
   * @return 路線ロールリスト
   */
  List<SearchLinesDto> getLines(SearchLinesDto dto);

  /**
   * 路線登録
   *
   * @param entity 路線 Entity
   * @return 登録件数
   */
  Long insert(LinesEntity entity);

  /**
   * 路線更新
   *
   * @param entity 路線 Entity
   * @return 更新件数
   */
  int update(LinesEntity entity);

  /**
   * 路線取得
   *
   * @param rosenId PKID
   * @return 路線
   */
  LinesDetailForm getLineByPkId(Long rosenId);


  /**
   * 路線リスト取得 (SELECT用)
   *
   * @param businessCorpId 事業者ID
   * @return 路線リスト
   */
  List<SearchLinesDto> getSelectableLines(Long businessCorpId);

  /**
   * 路線と利用期間から路線停留所リスト取得
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param rosenId 路線ID
   * @return 路線停留所リスト
   */
  List<LineRouteBusStopsDto> getLineBusStopListByServiceDateAndRosenId(String serviceStartDate,
      String serviceEndDate, Long rosenId);

  /**
   * キロ程表EXCEL出力用 停留所情報取得
   *
   * @param form キロ程表出力FORM
   * @return キロ程表EXCEL出力用 停留所情報
   */
  List<DistanceReportDto> getDistanceReportForBusStopList(DistanceReportForm form);

  /**
   * 利用期間のチェック
   *
   * @param rosenId 路線ID
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @return 路線数
   */
  Long countLineByDateAndLine(Long rosenId, String serviceStartDate, String serviceEndDate);

  /**
   * 路線取得（SELECT用）
   *
   * @return 路線リスト
   */
  List<LinesSelectDto> getLinesForSelectOption(@Param("businessCorpId") Long businessCorpId);

  /**
   * 利用期間による路線数取得
   *
   * @param serviceStartDate 利用開始日
   * @param rosenId 路線ID
   * @param businessCorpId 事業者ID
   * @return 路線数
   */
  Long countLinesByIdDate(@Param("serviceStartDate") String serviceStartDate,
      @Param("rosenId") Long rosenId, @Param("businessCorpId") Long businessCorpId);

  /**
   * 路線の利用終了日取得
   *
   * @return 路線の利用終了日
   */
  String getServiceEndDateById(@Param("rosenId") Long rosenId);

  /**
   * 棒ダイヤ情報取得
   *
   * @param form 棒ダイヤ検索FORM
   * @return 棒ダイヤ取得
   */
  List<DisplayDaiyasDto> getDisplayDaiyas(DisplayDaiyasSearchForm form);

  /**
   * 運行実績取得
   *
   * @param extractionStartDate 抽出開始日
   * @param extractionEndDate 抽出終了日
   * @param rosenId 路線ID
   * @param businessCorpId 事業者ID
   * @return 運行実績リスト
   */
  List<OperationRecordsDto> getOperationRecords(String extractionStartDate,
      String extractionEndDate, Long rosenId, Long businessCorpId);

  /**
   * 指定されたGTFSレポートフォームから、ダイヤ改正情報の一覧を取得します。
   *
   * @param businessCorpId 事業者ID
   * @param targetDate 対象日
   * @return ダイヤ改正情報を保持するDaiyaRevisionDtoのリスト
   */
  List<GtfsDaiyaRevisionDto> getDaiyaRevisionList(@Param("businessCorpId") Long businessCorpId,
      @Param("targetDate") String targetDate);

  /**
   * セレクトボックス用の路線情報取得
   *
   * @return 路線選択肢を表すGtfsReportListsSelectDtoのリスト
   */
  List<LineInfoForReportDto> getLineInfosForSelectOption(
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 一覧表出力ダウンロード用一覧取得
   * @param form 一覧表出力FORM
   * @return 一覧表出力ダウンロード用DTO
   */
  List<ReportDownLoadDto> getReportForDownload(ReportListsExcelForm form);

  /**
   * 路線時刻表出力ダウンロード用一覧取得
   * @param businessCorpId 事業者ID
   * @param rosenId 路線ID
   * @param daiyaRevisionId 便ダイヤ改正ID
   * @return 路線時刻表ダウンロード用DTO
   */
  List<RouteTimetableListDownloadDto> getRouteTimetableListsForDownload(
      @Param("businessCorpId") Long businessCorpId, @Param("rosenId") Long rosenId,
      @Param("daiyaRevisionId") Long daiyaRevisionId);

  /**
   * スターフダウンロード用一覧取得
   * @param businessCorpId 事業者ID
   * @param rosenId 路線ID
   * @param daiyaRevisionId 便ダイヤ改正ID
   * @return スターフダウンロード用DTO
   */
  List<StaffReportListDownloadDto> getStaffReportListsForDownload(
      @Param("businessCorpId") Long businessCorpId, @Param("rosenId") Long rosenId,
      @Param("daiyaRevisionId") Long daiyaRevisionId);

  /**
   * 停留所付近図ダウンロード用一覧取得
   * @param businessCorpId 事業者ID
   * @param rosenId 路線ID
   * @return 停留所付近図ダウンロード用DTO
   */
  List<StopMapListDownloadDto> getStopMapListsForDownload(
      @Param("businessCorpId") Long businessCorpId, @Param("rosenId") Long rosenId,
      @Param("processingDate") LocalDate processingDate);

  /**
   * 運賃改定日一覧取得（SELECT用）
   * @param businessCorpId 事業者ID
   * @return 運賃改定DTO（SELECT用）
   */
  List<FareReportListsSelectDto> getFareReportListsSelectOption(
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 運賃表出力ダウンロード用一覧取得
   * @param form 運賃表出力FORM
   * @return 運賃表出力ダウンロード用DTO
   */
  List<FareReportDownLoadDto> getFareReportForDownload(FareReportListsExcelForm form);

  /**
   * 運行回数出力初期情報取得
   * @param businessCorpId 事業者ID
   * @return 利用開始年月、路線ID、事業者IDリスト
   */
  List<OperatingReportListsSelectDto> getOperatingReportListsSelectOption(
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 運行回数出力ダウンロード用一覧取得
   * @param form 一覧表出FORM
   * @return 一覧表出ダウンロード用DTO
   */
  List<OperatingReportDaiyasRegisterDownLoadDto> getOperatingReportDaiyasRegisterForDownload(
      OperatingReportListsExcelForm form);

}
