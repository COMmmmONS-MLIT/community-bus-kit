package maint.service.plan;

import maint.dto.plan.fareReport.FareReportDownLoadDto;
import maint.dto.plan.fareReport.FareReportListsSelectDto;
import maint.form.fareReport.FareReportListsExcelForm;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 運賃表出力SERVICE
 *
 * @author JEKAL.H
 */

@Service
public class FareReportListsService extends BaseService {

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;


  /**
   * 運賃改定日取得（SELECT用）
   */
  public List<FareReportListsSelectDto> getFareReportListsSelectOption() {
    return linesMapper.getFareReportListsSelectOption(getUserSession().getBusinessCorpId());
  }

  /**
   * 運賃表出力ダウンロード用一覧取得
   *
   * @param form 運賃表出力FORM
   * @return 運賃表出力ダウンロード用DTO
   */
  public List<FareReportDownLoadDto> getFareReportForDownload(
      FareReportListsExcelForm form) {
    // セッションの事業者IDを設定
    try {
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    } catch (Exception ignored) {
      // セッション未取得時はそのまま実行（外部バッチ等）
    }
    return linesMapper.getFareReportForDownload(form);
  }
}

