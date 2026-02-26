package maint.service.document;

import maint.constants.CommConstant;
import maint.doc.StopMapListExcel;
import maint.dto.document.StopMapListDownloadDto;
import maint.form.report.StopMapExcelForm;
import maint.service.BaseService;
import maint.utils.date.ExDateFormat;
import maint.utils.date.ExDateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * 停留所付近図EXCEL出力SERVICE
 *
 * @author 林竜生
 *
 */
@Service
public class StopMapListService extends BaseService {

  /**
   * ResourceLoader
   */
  @Autowired
  private ResourceLoader resourceLoader;

  /**
   * テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE = "stopMapList";

  /**
   * EXCELファイル名
   */
  private final String EXCEL_FILE_NAME = "停留所付近図出力";

  /**
   * 停留所付近図出力SERVICE
   */
  @Autowired
  StopMapService stopMapService;

  /**
   * 停留所付近図EXCELダウンロード
   *
   * @param form 停留所付近図出力フォーム
   * @return Excel
   * @throws Exception
   */
  public ResponseEntity<?> getStopMapInfo(
      StopMapExcelForm form) throws Exception {

    // 対象日付を画面の入力フォーマットから変換してFormに入れる
    String processingDateStr = form.getProcessingDateStr();
    LocalDate processingDate = LocalDate.parse(processingDateStr, ExDateFormat.uuuuMMdd_HYPEN);
    form.setProcessingDate(processingDate);

    // テンプレートを読み込み 作業用のBookを作成
    Resource resource = resourceLoader.getResource(
        CommConstant.TEMP_EXCEL_FOLDER_PATH + this.TEMP_EXCEL_FILE + CommConstant.EXCEL_FILE_EXTENSION);

    try (XSSFWorkbook workBook = new XSSFWorkbook(resource.getInputStream())) {
      StopMapListExcel workExcel = new StopMapListExcel(workBook);

      List<StopMapListDownloadDto> stopMapDL = stopMapService.getStopMapListsForDownload(form);

      if (stopMapDL.isEmpty()) {
        // データが0件の場合、テンプレートをそのままダウンロード
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workBook.write(os);
        return excelDownloadProc(os.toByteArray());
      } else {

        // 復路標柱がnullのレコードは同一往路標柱に復路標柱が紐づかない場合(count==1)のみ抽出対象とする
        List<StopMapListDownloadDto> filteredStopMapDL =
            stopMapDL.stream()
                .filter(
                    d -> d.getInSignpostId() != null ||
                        stopMapDL.stream().filter(
                            e -> e.getOutSignpostId().equals(d.getOutSignpostId())
                        ).count() == 1
                ).toList();

        for (StopMapListDownloadDto dto : filteredStopMapDL) {
          workExcel.createSheetFromTemplate(dto.getBusStopName(), dto.getOutSignpostId(),
              dto.getInSignpostId());
          workExcel.setStopMapProtInfo(dto);
        }

        workExcel.removeTemplateSheet();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workExcel.buildStreamingWorkbook().write(os);
        return excelDownloadProc(os.toByteArray());
      }
    }
  }

  /**
   * Excel取得
   *
   * @param documentContent Excelデータ
   * @return Excel
   * @throws Exception
   */
  private ResponseEntity<?> excelDownloadProc(byte[] documentContent)
      throws Exception {
    HttpHeaders headers = new HttpHeaders();
    // xlsx形式のContent-Typeを設定
    headers.setContentType(MediaType.parseMediaType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    // ファイル名設定（便ダイヤ_運行開始日）
    String currentDateTime = ExDateUtils.convertDateTimeFormat(ExDateUtils.getCurrentDateTime(),
        ExDateFormat.uuuuMMddHHmmss);
    String fileName =
        EXCEL_FILE_NAME + "_" + currentDateTime + CommConstant.EXCEL_FILE_EXTENSION;
    headers.set("Content-Disposition",
        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
  }
}
