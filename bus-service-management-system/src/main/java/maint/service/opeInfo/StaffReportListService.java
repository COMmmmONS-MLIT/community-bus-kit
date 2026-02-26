package maint.service.opeInfo;

import maint.constants.CommConstant;
import maint.doc.StaffReportListExcel;
import maint.dto.opeInfo.StaffReportListDownloadDto;
import maint.form.report.StaffReportExcelForm;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * スターフリストEXCEL出力SERVICE
 *
 * @author 林竜生
 *
 */
@Service
public class StaffReportListService extends BaseService {

  /**
   * データ行開始位置
   */
  private final static int START_DATA_ROW_NO = 7;

  /**
   * データ列開始位置
   */
  private final static int START_DATA_COL_NO = 3;

  /**
   * ResourceLoader
   */
  @Autowired
  private ResourceLoader resourceLoader;

  /**
   * テンプレートEXCELファイル
   */
  private final String TEMP_EXCEL_FILE = "staffReportList";

  /**
   * EXCELファイル名
   */
  private final String EXCEL_FILE_NAME = "スターフ出力";

  /**
   * スターフ出力SERVICE
   */
  @Autowired
  StaffReportService staffReportService;

  /**
   * EXCELファイル名
   *
   * @throws Exception
   */
  public ResponseEntity<?> getStaffReportInfo(StaffReportExcelForm form) throws Exception {

    // ResourceLoaderを使ってテンプレートファイルを読み込む
    Resource templateResource = resourceLoader.getResource(
        "classpath:/excel/" + TEMP_EXCEL_FILE + CommConstant.EXCEL_FILE_EXTENSION);
    XSSFWorkbook workBook = new XSSFWorkbook(templateResource.getInputStream());

    StaffReportListExcel workExcel = new StaffReportListExcel(workBook);

    // 新規ダウンロード
    List<StaffReportListDownloadDto> staffReportDL =
        staffReportService.getStaffReportListsForDownload(form);

    if (staffReportDL.isEmpty()) {
      // データが0件の場合、テンプレートをそのままダウンロード
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      workBook.write(os);
      return excelDownloadProc(os.toByteArray());
    }

    Map<Long, List<StaffReportListDownloadDto>> busServiceGroupData = new LinkedHashMap<>();

    // データを便IDでグループ化しつつ、順序を保持
    for (StaffReportListDownloadDto dto : staffReportDL) {
      // グループ化
      Long busServiceId = dto.getBusServiceId();
      busServiceGroupData.computeIfAbsent(busServiceId, k -> new ArrayList<>()).add(dto);

      // 出発時刻の文字列の変更　(HHMM　→　HH:MM)
      dto.setBusStopDepartureTime(
          ExDateUtils.convertTimeFormat(dto.getBusStopDepartureTime(), ExDateFormat.HHmm_COLON));
    }

    // シート毎に一意の組み合わせを追跡するためのセット
    Set<String> uniqueSheetCombinations = new HashSet<>();

    // 各便ごとに処理
    for (Map.Entry<Long, List<StaffReportListDownloadDto>> entry : busServiceGroupData.entrySet()) {
      List<StaffReportListDownloadDto> groupData = entry.getValue();

      if (!groupData.isEmpty()) {
        StaffReportListDownloadDto firstDto = groupData.get(0);
        String combinationKey = String.valueOf(firstDto.getBusServiceId());

        // このシート組み合わせが初めての場合、シートを作成
        if (uniqueSheetCombinations.add(combinationKey)) {
          // テンプレートからシートを作成し、静的情報を設定
          workExcel.createSheetFromTemplate(firstDto.getBusServiceId(), firstDto.getBusServiceName(),
              firstDto.getBusServiceTripId());
          workExcel.setStaffReportStaticPlot(firstDto);
        }

        // シートの切り替え
        workExcel.selectSheetFor(firstDto);

        // 列(便)・行(停留所) のマッピング準備
        // 系統と便の組み合わせをキーとするレコード
        record TripKey(Long lineRouteId, Long busServiceId) {}
        // 系統と便の組み合わせに対応する列インデックスのマップ
        Map<TripKey, Integer> colIndexByTrip = new LinkedHashMap<>();
        // 系統名を格納するマップ
        Map<TripKey, String> lineRouteNameByTrip = new LinkedHashMap<>();
        // 便名を格納するマップ
        Map<TripKey, String> busServiceNameByTrip = new LinkedHashMap<>();
        // 停留所IDに対応する行インデックスのマップ
        Map<Long, Integer>    rowIndexByStop = new LinkedHashMap<>();
        // 停留所IDに対応する停留所名のマップ
        Map<Long, String>     stopNameById   = new LinkedHashMap<>();
        // セルの値を格納する二次元マップ（行→列→値のリスト）
        Map<Integer, Map<Integer, List<String>>> cellValues = new LinkedHashMap<>();


        int baseRow = START_DATA_ROW_NO - 1;   // データ開始の直前行
        int baseCol = START_DATA_COL_NO - 1;   // データ開始の直前列
        int nextCol = baseCol;
        int nextRow = baseRow;

        // データ（出発時刻）に対する行列を確定
        for (StaffReportListDownloadDto d : groupData) {
          // 系統と便の組み合わせキーを作成
          TripKey k = new TripKey(d.getLineRouteId(), d.getBusServiceId());
          // 新しい系統/便の組み合わせの場合、新しい列を割り当て
          if (!colIndexByTrip.containsKey(k)) {
            nextCol++;
            colIndexByTrip.put(k, nextCol);

            // ヘッダー文字列を保存
            lineRouteNameByTrip.put(k, d.getLineRouteName());
            busServiceNameByTrip.put(k, d.getBusServiceName());
          }
          // 新しい停留所の場合、新しい行を割り当て
          if (!rowIndexByStop.containsKey(d.getBusStopId())) {
            nextRow++;
            rowIndexByStop.put(d.getBusStopId(), nextRow);
            stopNameById.put(d.getBusStopId(), d.getBusStopName());
          }
        }

        // ヘッダー（系統／便）と停留所名（A-B結合）を書き出し
        for (var e2 : colIndexByTrip.entrySet()) {
          TripKey k = e2.getKey();
          int c = e2.getValue() - 1;
          // ヘッダー情報を書き出し
          workExcel.writeHeader(baseRow, c, lineRouteNameByTrip.get(k),
              busServiceNameByTrip.get(k));
        }

        // 停留所名を行に書き出し
        for (var e2 : rowIndexByStop.entrySet()) {
          Long stopId = e2.getKey();
          int r = e2.getValue();
          workExcel.writeStopNameRow(r, stopNameById.get(stopId));
        }

        // シート別DTOのデータからセル値を収集
        for (StaffReportListDownloadDto dto : groupData) {
          TripKey k = new TripKey(dto.getLineRouteId(), dto.getBusServiceId());
          Integer r = rowIndexByStop.get(dto.getBusStopId());
          Integer c = colIndexByTrip.get(k) - 1;
          if (r == null || c == null)
            continue;

          // 出発時刻がない場合はスキップ
          String t = dto.getBusStopDepartureTime();
          if (t == null || t.isBlank())
            continue;

          // 行・列ごとに時刻を収集
          cellValues.computeIfAbsent(r, _r -> new LinkedHashMap<>())
              .computeIfAbsent(c, _c -> new ArrayList<>()).add(t);
        }

        // まとめてセルに書き出し（非訪問停留所は空白のまま）
        for (var rowEntry : cellValues.entrySet()) {
          int r = rowEntry.getKey();
          for (var colEntry : rowEntry.getValue().entrySet()) {
            int c = colEntry.getKey();
            String value = String.join("\n", colEntry.getValue());
            workExcel.setCellDataBorder(r, c, value);
          }
        }

        // 全ての停留所（行）と便（列）の組み合わせについて、
        // まだ値が設定されていないセルに対してnullを渡して罫線のみを設定
        for (Integer r : rowIndexByStop.values()) {
          for (Integer c : colIndexByTrip.values()) {
            // colIndexByTripは1始まりで格納されているため、実際の列インデックスは-1する
            int actualCol = c - 1;
            // その行と列の組み合わせにデータが存在しない場合、nullで罫線のみ設定
            if (!cellValues.containsKey(r) || !cellValues.get(r).containsKey(actualCol)) {
              workExcel.setCellDataBorder(r, actualCol, null);
            }
          }
        }
      }
    }

    workExcel.removeTemplateSheet();

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    workExcel.buildStreamingWorkbook().write(os);

    return excelDownloadProc(os.toByteArray());
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
