package maint.service.plan;

import maint.constants.CommConstant;
import maint.doc.FareReportListExcel;
import maint.dto.plan.fareReport.FareReportDownLoadDto;
import maint.form.fareReport.FareReportListsExcelForm;
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
import java.util.List;

/**
 * 運賃表出力SERVICE
 *
 * @author JEKAL.H
 *
 */
@Service
public class FareReportListRegisterService extends BaseService {

  /**
   * データ行開始位置
   */
  private final static int START_DATA_ROW_NO = 2;

  /**
   * ResourceLoader
   */
  @Autowired
  private ResourceLoader resourceLoader;

  /**
   * テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE = "fareReportList";

  /**
   * EXCELファイル名
   */
  private final String EXCEL_FILE_NAME = "運賃表出力";

  /**
   * 運賃表出力SERVICE
   */
  @Autowired
  FareReportListsService fareReportListsService;

  /**
   * 運賃表出力EXCELダウンロード
   *
   * @param form 運賃表出力フォーム
   * @return Excel
   * @throws Exception
   */
  public ResponseEntity<?> getFareReportInfo(FareReportListsExcelForm form) throws Exception {

    // テンプレートを読み込み 作業用のBookを作成
    Resource resource = resourceLoader.getResource(
        CommConstant.TEMP_EXCEL_FOLDER_PATH + this.TEMP_EXCEL_FILE + CommConstant.EXCEL_FILE_EXTENSION);

    // try-with-resourceを利用しているのでclose処理不要。
    try (XSSFWorkbook workBook = new XSSFWorkbook(resource.getInputStream())) {
      FareReportListExcel workExcel = new FareReportListExcel(workBook);
      workExcel.setSheetName(EXCEL_FILE_NAME);

      //新規ダウンロード
      List<FareReportDownLoadDto> FareReportDownLoadList =
          fareReportListsService.getFareReportForDownload(form);
      // 各列へ入力
      int currentRow = START_DATA_ROW_NO - 1;
      for (FareReportDownLoadDto dto : FareReportDownLoadList) {
        currentRow = workExcel.setFareReportInfo(currentRow, dto, form);
      }
      // ダウンロードするExcelを書き込み
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      workExcel.getSXSSFWorkbook().write(os);
      return excelDownloadProc(os.toByteArray());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw e;
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
    String fileName = EXCEL_FILE_NAME + "_" + currentDateTime + CommConstant.EXCEL_FILE_EXTENSION;
    headers.set("Content-Disposition",
        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
  }
}
