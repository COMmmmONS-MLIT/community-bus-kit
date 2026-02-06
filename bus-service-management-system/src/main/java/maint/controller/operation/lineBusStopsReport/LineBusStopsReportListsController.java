package maint.controller.operation.lineBusStopsReport;

import maint.controller.BaseController;
import maint.enums.DispMode;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.lineBusStopsReport.LineBusStopsReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LineBusStopsReportListsService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 停留所情報インポートCONTROLLER
 *
 * @author JEON
 */

@Controller
@Validated
@RequestMapping("/operation/lineBusStopsReport")
@SessionAttributes("lineBusStopsReportListsExcelForm")
public class LineBusStopsReportListsController extends BaseController {

  /**
   * 停留所情報インポート画面パス
   */
  private static final String INDEX_PAGE = "operation/lineBusStopsReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.IMPORT_STOP;

  /**
   * 停留所情報インポートSERVICE
   */
  @Autowired
  LineBusStopsReportListsService lineBusStopsReportListsService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("LineBusStopsReportListsExcelForm") LineBusStopsReportListsExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 停留所情報インポート画面（初期表示）
   *
   * @param model    Model
   * @param form     停留所情報インポートFORM
   * @param initFlag 停留所情報インポートフォーム初期化フラグ
   * @return 停留所情報インポート画面
   */
  @GetMapping("/")
  public String index(Model model, LineBusStopsReportListsExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new LineBusStopsReportListsExcelForm();
    }
    model.addAttribute("lineBusStopsReportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form  停留所情報インポートFORM
   * @return ダウンロードレスポンス
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("lineBusStopsReportListsExcelForm") LineBusStopsReportListsExcelForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);

    execCommonProc(model, NOW_MENU);
    form.setDispMode(DispMode.UPDATE_MODE);

    try {
      return lineBusStopsReportListsService.getLineBusStopReportTemplate();
    } catch (Exception e) {
      model.addAttribute("lineBusStopsReportListsExcelForm", form);
      setExcelDownloadErrorMessage("停留所インポート");
      return INDEX_PAGE;
    }
  }

  /**
   * 停留所・標柱情報のExcelファイルをアップロードし、データを取り込み・登録する。
   *
   * @param model          Spring MVCのModelオブジェクト
   * @param form           画面から送信されたフォームデータ（バリデーション対象）
   * @param bindingResult  バリデーション結果
   * @param file           アップロードされたExcelファイル
   * @param uploadFileName 表示用のファイル名（画面に返す目的など）
   * @return 処理結果（画面遷移やJSONレスポンスなど）
   * @throws IOException ファイル読み込みに失敗した場合に発生
   */
  @Transactional
  @PostMapping("/uploadExcel")
  public Object uploadExcel(Model model, @Validated @ModelAttribute("lineBusStopsReportListsExcelForm") LineBusStopsReportListsExcelForm form,
      BindingResult bindingResult, @RequestParam("excel_file") MultipartFile file, @RequestParam("uploadFileName") String uploadFileName) throws IOException {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    execCommonProc(model, this.NOW_MENU);

    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }

    if (file == null || file.isEmpty()) {
      setErrorMsg(getMessage("message.error.file.heck.required"));
      return INDEX_PAGE;
    }

    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
      setErrorMsg(getMessage("message.error.file.no.content.found"));
      return INDEX_PAGE;
    }

    // ファイルアップロード処理
    List<String> msgList = lineBusStopsReportListsService.processUploadExcel(file, uploadFileName);

    if (!msgList.isEmpty()) {
      Map<String, Object> okBody = new HashMap<>();
      okBody.put("success", false);
      okBody.put("errors", msgList);
      okBody.put("redirect", false); // エラー時はリダイレクト不要
      return ResponseEntity.internalServerError().body(okBody);
    }

    Map<String, Object> okBody = new HashMap<>();
    okBody.put("success", true);
    okBody.put("message", "アップロードが成功しました。");
    okBody.put("redirect", true);
    return ResponseEntity.ok(okBody);
  }

}
