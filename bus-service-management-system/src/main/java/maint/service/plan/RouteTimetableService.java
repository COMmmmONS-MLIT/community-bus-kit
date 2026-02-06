package maint.service.plan;

import maint.dto.selectBox.DaiyaRevisionDto;
import maint.dto.plan.RouteTimetableListDownloadDto;
import maint.form.report.RouteTimetableExcelForm;
import maint.mapper.DaiyaRevisionsMapper;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 路線時刻表図出力SERVICE
 *
 * @author 林竜生
 */

@Service
public class RouteTimetableService extends BaseService {

  /** 便ダイヤ改正MAPPER */
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
   * 路線時刻表ダウンロード用一覧取得
   *
   * @param form 路線時刻表出力FORM
   * @return 路線時刻表ダウンロード用DTO
   */
  public List<RouteTimetableListDownloadDto> getRouteTimetableListsForDownload(
      RouteTimetableExcelForm form) {
    // セッションの事業者IDを設定
    try {
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    } catch (Exception ignored) {
      // セッション未取得時はそのまま実行（外部バッチ等）
    }
    return linesMapper.getRouteTimetableListsForDownload(form.getBusinessCorpId(),
        form.getRosenId(), form.getDaiyaRevisionId());
  }
}
