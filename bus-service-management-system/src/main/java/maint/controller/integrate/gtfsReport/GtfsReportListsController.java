package maint.controller.integrate.gtfsReport;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.ReportType;
import maint.form.gtfsReport.GtfsReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.integrate.GtfsReportListsService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * GTFS-ScheduleCONTROLLER
 *
 * @author JEON
 */

@Controller
@Validated
@RequestMapping("/integrate/gtfsReport")
@SessionAttributes("busDaiyasSearchForm")
public class GtfsReportListsController extends BaseController {

  /**
   * GTFS-Schedule画面パス
   */
  private static final String INDEX_PAGE = "integrate/gtfsReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.GTFS;

  /**
   * 路線SERVICE
   */
  @Autowired
  LinesService linesService;
  /**
   * GTFS-ScheduleSERVICE
   */
  @Autowired
  GtfsReportListsService gtfsReportListsService;

  /**
   * 生成ファイルリスト
   */
  List<String> gtfsFiles =
      List.of("fare_rules.txt", "fare_attributes.txt", "calendar_dates.txt", "calendar.txt", "trips.txt", "stop_times.txt", "routes.txt", "stops.txt",
          "attributions.txt", "feed_info.txt", "agency.txt", "translations.txt");

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("GtfsReportListsExcelForm") GtfsReportListsExcelForm form) {
    model.addAttribute("reportType", ReportType.values());
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * GTFS-Schedule画面（初期表示）
   *
   * @param model    Model
   * @param form     GTFS-ScheduleFORM
   * @param initFlag GTFS-Scheduleフォーム初期化フラグ
   * @return GTFS-Schedule画面
   */
  @GetMapping("/")
  public String index(Model model, GtfsReportListsExcelForm form, @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new GtfsReportListsExcelForm();
    }
    model.addAttribute("gtfsReportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * Textダウンロード処理
   *
   * @param model         Model
   * @param form          GTFS-ScheduleFORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model, @Validated @ModelAttribute("gtfsReportListsExcelForm") GtfsReportListsExcelForm form, BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);
    execCommonProc(model, this.NOW_MENU);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      // 出力対象ごとエラーメッセージ表示
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }

    try {

      for (String fileName : gtfsFiles) {
        Path filePath = Paths.get("output/gtfs", fileName);
        Files.deleteIfExists(filePath);
      }

      Path zipPath = gtfsReportListsService.exportGtfsFeedZip(form, gtfsFiles);

      byte[] zipBytes = Files.readAllBytes(zipPath);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      // 出力ファイル名の生成
      String dlFileName = gtfsReportListsService.getDownloadFileName(form.getTargetDate());
      headers.setContentDispositionFormData("attachment", URLEncoder.encode(dlFileName, StandardCharsets.UTF_8));
      return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.error("【GTFS-ScheduleTEXTダウンロード】TEXTダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("GTFS-Schedule");
      return INDEX_PAGE;
    }
  }

}
