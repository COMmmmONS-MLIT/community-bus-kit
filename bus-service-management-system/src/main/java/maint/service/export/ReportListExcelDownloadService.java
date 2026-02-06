package maint.service.export;

import maint.constants.CommConstant;
import maint.doc.ReportListExcel;
import maint.dto.export.lists.ReportDownLoadDto;
import maint.form.report.ReportListsExcelForm;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
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
import java.util.*;
import java.nio.charset.StandardCharsets;
import maint.enums.ReportType;

/**
 * 一覧表EXCEL出力SERVICE
 *
 * @author JEKAL.H
 *
 */
@Service
public class ReportListExcelDownloadService extends BaseService {

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
   * 停留所一覧テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE_BUS_STOP_LIST = "busStopList";

  /**
   * 標柱一覧テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE_SIGNPOSTS_LIST = "signpostsList";

  /**
   * 標柱停留所一覧テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE_SIGNPOSTS_BUS_STOP_LIST = "signpostsBusStopList";

  /**
   * 系統一覧テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE_LINE_ROUTES_LIST = "lineRoutesList";

  /**
   * 便ダイヤ一覧テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE_DAIYA_LIST = "daiyaList";

  /**
   * 停留所一覧EXCELファイル名
   */
  private final String EXCEL_FILE_NAME_BUS_STOP_LIST = "停留所一覧";

  /**
   * 標柱一覧EXCELファイル名
   */
  private final String EXCEL_FILE_NAME_SIGNPOSTS_LIST = "標柱一覧";

  /**
   * 標柱停留所一覧EXCELファイル名
   */
  private final String EXCEL_FILE_NAME_SIGNPOSTS_BUS_STOP_LIST = "標柱停留所一覧";

  /**
   * 系統一覧EXCELファイル名
   */
  private final String EXCEL_FILE_NAME_LINE_ROUTES_LIST = "系統一覧";

  /**
   * 便ダイヤ一覧EXCELファイル名
   */
  private final String EXCEL_FILE_NAME_DAIYA_LIST = "便ダイヤ一覧";

  /**
   * 一覧表出力EXCELファイル名
   */
  private String tempExcelFile;

  /**
   * 一覧表出力テンプレートEXCELファイル
   */
  private String excelFileName;

  /**
   * 一覧表出力SERVICE
   */
  @Autowired
  ReportListsService reportListsService;

  /**
   * 一覧表出力EXCELダウンロード
   *
   * @param form 一覧表出力フォーム
   * @return Excel
   * @throws Exception
   */
  public ResponseEntity<?> getReportRegisterInfo(ReportListsExcelForm form) throws Exception {
    //出力対象プルダウン
    if ((ReportType.BUS_STOP_LIST.getValue()).equals(form.getReportType())) {
      this.tempExcelFile = TEMP_EXCEL_FILE_BUS_STOP_LIST;
      this.excelFileName = EXCEL_FILE_NAME_BUS_STOP_LIST;
    } else if ((ReportType.SIGN_POST_LIST.getValue()).equals(form.getReportType())) {
      this.tempExcelFile = TEMP_EXCEL_FILE_SIGNPOSTS_LIST;
      this.excelFileName = EXCEL_FILE_NAME_SIGNPOSTS_LIST;
    } else if ((ReportType.SIGN_POST_STOP_LIST.getValue()).equals(form.getReportType())) {
      this.tempExcelFile = TEMP_EXCEL_FILE_SIGNPOSTS_BUS_STOP_LIST;
      this.excelFileName = EXCEL_FILE_NAME_SIGNPOSTS_BUS_STOP_LIST;
    } else if ((ReportType.SYSTEM_LIST.getValue()).equals(form.getReportType())) {
      this.tempExcelFile = TEMP_EXCEL_FILE_LINE_ROUTES_LIST;
      this.excelFileName = EXCEL_FILE_NAME_LINE_ROUTES_LIST;
    } else {
      this.tempExcelFile = TEMP_EXCEL_FILE_DAIYA_LIST;
      this.excelFileName = EXCEL_FILE_NAME_DAIYA_LIST;
    }

    // テンプレートを読み込み 作業用のBookを作成
    Resource resource = resourceLoader.getResource(
        CommConstant.TEMP_EXCEL_FOLDER_PATH + this.tempExcelFile + CommConstant.EXCEL_FILE_EXTENSION);

    // try-with-resourceを利用しているのでclose処理不要。
    try (XSSFWorkbook workBook = new XSSFWorkbook(resource.getInputStream())) {
      ReportListExcel workExcel = new ReportListExcel(workBook);
      workExcel.setSheetName(excelFileName);

      // 新規ダウンロード
      List<ReportDownLoadDto> ReportDownLoadList =
          reportListsService.getReportForDownload(form);

      // 停留所一覧ダウンロードの場合、停留場IDで重複を消す
      if ((ReportType.BUS_STOP_LIST.getValue()).equals(form.getReportType())) {
        Set<Object> seen = new HashSet<>();
        List<ReportDownLoadDto> deduped = new ArrayList<>();
        for (ReportDownLoadDto dto : ReportDownLoadList) {
          Object key = dto.getBusStopId();
          if (ExObjectUtils.isBlank(key) || seen.add(key)) {
            deduped.add(dto);
          }
        }
        ReportDownLoadList = deduped;
      }

      // 標柱一覧ダウンロードの場合、標柱IDで重複を消す
      if ((ReportType.SIGN_POST_LIST.getValue()).equals(form.getReportType())) {
        Set<Object> seen = new HashSet<>();
        List<ReportDownLoadDto> deduped = new ArrayList<>();
        for (ReportDownLoadDto dto : ReportDownLoadList) {
          Object key = dto.getSignpostId();
          if (ExObjectUtils.isBlank(key) || seen.add(key)) {
            deduped.add(dto);
          }
        }
        ReportDownLoadList = deduped;
      }

      // 標柱停留所一覧ダウンロードの場合、停留所ID・標柱IDで重複を消す
      if ((ReportType.SIGN_POST_STOP_LIST.getValue()).equals(form.getReportType())) {
        Set<String> seen = new HashSet<>();
        List<ReportDownLoadDto> deduped = new ArrayList<>();
        for (ReportDownLoadDto dto : ReportDownLoadList) {
          Long busStopId = dto.getBusStopId();
          Long signpostId = dto.getSignpostId();
          if (ExObjectUtils.isBlank(busStopId) || ExObjectUtils.isBlank(signpostId)) {
            deduped.add(dto);
            continue;
          }
          String key = busStopId + "|" + signpostId;
          if (seen.add(key)) {
            deduped.add(dto);
          }
        }
        ReportDownLoadList = deduped;
      }

      // 系統一覧ダウンロードの場合、系統ID・系統停留所IDで重複を消す
      if ((ReportType.SYSTEM_LIST.getValue()).equals(form.getReportType())) {
        Set<String> seen = new HashSet<>();
        List<ReportDownLoadDto> deduped = new ArrayList<>();
        for (ReportDownLoadDto dto : ReportDownLoadList) {
          Long lineRouteId = dto.getLineRouteId();
          Integer lineRouteBusStopId = dto.getLineRouteBusStopId();
          if (ExObjectUtils.isBlank(lineRouteId) || ExObjectUtils.isBlank(lineRouteBusStopId)) {
            deduped.add(dto);
            continue;
          }
          String key = lineRouteId + "|" + lineRouteBusStopId;
          if (seen.add(key)) {
            deduped.add(dto);
          }
        }
        ReportDownLoadList = deduped;
      }

      // 各列へ入力
      int currentRow = START_DATA_ROW_NO - 1;
      for (ReportDownLoadDto dto : ReportDownLoadList) {
        currentRow = workExcel.setReportRegisterInfo(currentRow, dto, form);
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
  private ResponseEntity<?> excelDownloadProc(byte[] documentContent) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    // xlsx形式のContent-Typeを設定
    headers.setContentType(MediaType.parseMediaType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    // ファイル名設定（便ダイヤ_運行開始日）
    String currentDateTime = ExDateUtils.convertDateTimeFormat(ExDateUtils.getCurrentDateTime(),
        ExDateFormat.uuuuMMddHHmmss);
    String fileName = this.excelFileName + "_" + currentDateTime + CommConstant.EXCEL_FILE_EXTENSION;
    headers.set("Content-Disposition",
        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
  }
}
