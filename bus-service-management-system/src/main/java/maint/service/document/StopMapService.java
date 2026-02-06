package maint.service.document;

import maint.dto.document.StopMapListDownloadDto;
import maint.form.report.StopMapExcelForm;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 停留所付近図出力SERVICE
 *
 * @author 林竜生
 */

@Service
public class StopMapService extends BaseService {

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 停留所付近図ダウンロード用一覧取得
   *
   * @param form 停留所付近図出力FORM
   * @return 停留所付近図ダウンロード用DTO
   */
  public List<StopMapListDownloadDto> getStopMapListsForDownload(StopMapExcelForm form) {
    // セッションの事業者IDを設定
    try {
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    } catch (Exception ignored) {
      // セッション未取得時はそのまま実行（外部バッチ等）
    }
    return linesMapper.getStopMapListsForDownload(form.getBusinessCorpId(), form.getRosenId(),
        form.getProcessingDate());
  }
}
