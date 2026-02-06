package maint.service.document;

import maint.constants.CommConstant;
import maint.doc.DistanceReportExcel;
import maint.dto.document.DistanceReportDto;
import maint.form.distanceReport.DistanceReportForm;
import maint.mapper.LinesMapper;
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

@Service
public class DistanceReportService extends BaseService {

  /** 路線 MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /**
   * ResourceLoader
   */
  @Autowired
  private ResourceLoader resourceLoader;

  /** キロ程表出力EXCELファイル名 */
  private final String EXCEL_FILE_NAME = "キロ程表出力";

  /**
   * 一覧表出力EXCELダウンロード
   *
   * @param form 一覧表出力フォーム
   * @return Excel
   * @throws Exception
   */
  public ResponseEntity<?> downLoadExcel(DistanceReportForm form) throws Exception {

    String TEMPLATE_PATH = "classpath:excel/distanceReport.xlsx";

    Resource resource = resourceLoader.getResource(TEMPLATE_PATH);

    try (XSSFWorkbook workbook = new XSSFWorkbook(resource.getInputStream())) {

      // 出力データ取得
      List<DistanceReportDto> reportList = linesMapper.getDistanceReportForBusStopList(form);

      DistanceReportExcel excelWriter = new DistanceReportExcel(workbook);
      excelWriter.setSheetName(EXCEL_FILE_NAME);

      // excel作成
      excelWriter.writeDistanceReport(reportList);

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      excelWriter.getSXSSFWorkbook().write(os);

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
  private ResponseEntity<?> excelDownloadProc(byte[] documentContent) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    // xlsx形式のContent-Typeを設定
    headers.setContentType(MediaType.parseMediaType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    // ファイル名設定
    String currentDateTime = ExDateUtils.convertDateTimeFormat(ExDateUtils.getCurrentDateTime(),
        ExDateFormat.uuuuMMddHHmmss);
    String fileName = EXCEL_FILE_NAME + "_" + currentDateTime + CommConstant.EXCEL_FILE_EXTENSION;
    headers.set("Content-Disposition",
        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
  }

}
