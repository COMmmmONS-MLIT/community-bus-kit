package maint.service.record;

import maint.dto.record.operatingReport.OperatingReportDaiyasRegisterDownLoadDto;
import maint.dto.record.operatingReport.OperatingReportListsSelectDto;
import maint.form.operatingReport.OperatingReportListsExcelForm;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 運行回数出力SERVICE
 *
 * @author JEKAL.H
 */

@Service
public class OperatingReportListsService extends BaseService {

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 全路線取得（SELECT用）
   */
  public List<OperatingReportListsSelectDto> getOperatingReportListsSelectOption() {

    return linesMapper.getOperatingReportListsSelectOption(getUserSession().getBusinessCorpId());
  }

  /**
   * 一覧表出ダウンロード用一覧取得
   *
   * @param form 一覧表出FORM
   * @return 一覧表出ダウンロード用DTO
   */
  public List<OperatingReportDaiyasRegisterDownLoadDto> getOperatingReportDaiyasRegisterForDownload(
      OperatingReportListsExcelForm form) {
    // セッションの事業者IDを設定
    try {
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    } catch (Exception ignored) {
      // セッション未取得時はそのまま実行（外部バッチ等）
    }
    return linesMapper.getOperatingReportDaiyasRegisterForDownload(form);
  }
}

