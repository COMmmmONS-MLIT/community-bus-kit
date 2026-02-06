package maint.service.opeInfo;

import maint.dto.selectBox.DaiyaRevisionDto;
import maint.dto.opeInfo.StaffReportListDownloadDto;
import maint.form.report.StaffReportExcelForm;
import maint.mapper.DaiyaRevisionsMapper;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * スターフ出力SERVICE
 *
 * @author 林竜生
 */

@Service
public class StaffReportService extends BaseService {

  /** スターフ出力MAPPER */
  @Autowired
  DaiyaRevisionsMapper daiyaRevisionMapper;

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 便ダイヤ改正日取得（SELECT用）
   */
  public List<DaiyaRevisionDto> getServiceStartDateList() {
    return daiyaRevisionMapper.getServiceStartDates(getUserSession().getBusinessCorpId());
  }

  /**
   * スターフダウンロード用一覧取得
   *
   * @param form スターフ出力FORM
   * @return スターフダウンロード用DTO
   */
  public List<StaffReportListDownloadDto> getStaffReportListsForDownload(
      StaffReportExcelForm form) {
    // セッションの事業者IDを設定
    try {
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    } catch (Exception ignored) {
      // セッション未取得時はそのまま実行（外部バッチ等）
    }
    return linesMapper.getStaffReportListsForDownload(form.getBusinessCorpId(),
        form.getRosenId(), form.getDaiyaRevisionId());
  }
}
