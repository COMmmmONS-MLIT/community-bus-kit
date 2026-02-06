package maint.service.export;

import maint.dto.selectBox.LineInfoForReportDto;
import maint.dto.export.lists.ReportDownLoadDto;
import maint.form.report.ReportListsExcelForm;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 一覧表出力SERVICE
 *
 * @author JEKAL.H
 */

@Service
public class ReportListsService extends BaseService {

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 路線取得（便ダイヤ改正のリスト用）
   */
  public List<LineInfoForReportDto> getAllLinesForSelectOption(ReportListsExcelForm form) {
      // セッションの事業者IDを設定
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());

      return linesMapper.getLineInfosForSelectOption(getUserSession().getBusinessCorpId());
  }

    /**
     * 一覧表出力ダウンロード用一覧取得
     * @param form 一覧表出力FORM
     * @return 一覧表出力ダウンロード用DTO
     */
    public List<ReportDownLoadDto> getReportForDownload(ReportListsExcelForm form) {
        // セッションの事業者IDを設定
        try {
            form.setBusinessCorpId(getUserSession().getBusinessCorpId());
        } catch (Exception ignored) {
            // セッション未取得時はそのまま実行（外部バッチ等）
        }
        return linesMapper.getReportForDownload(form);
    }
}

