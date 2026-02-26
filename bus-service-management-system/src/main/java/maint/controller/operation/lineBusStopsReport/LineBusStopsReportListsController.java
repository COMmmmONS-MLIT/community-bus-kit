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
   */
  @PostMapping(value = "/uploadExcel", produces = "application/json")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> uploadExcel(
      Model model,
      @Validated @ModelAttribute("lineBusStopsReportListsExcelForm") LineBusStopsReportListsExcelForm form,
      BindingResult bindingResult,
      @RequestParam("excel_file") MultipartFile file,
      @RequestParam("uploadFileName") String uploadFileName) {

    hasAccessPermission(NOW_MENU, true, false);
    execCommonProc(model, NOW_MENU);

    if (bindingResult.hasErrors()) {
      return badRequest(errorBody(List.of(getMessage(CommonMessage.ERROR_INPUT.getMessage()))));
    }

    if (ExObjectUtils.isBlank(file)) {
      return badRequest(errorBody(List.of(getMessage("message.error.file.heck.required"))));
    }

    if (!isExcelFile(file)) {
      return badRequest(errorBody(List.of(getMessage("message.error.file.no.content.found"))));
    }

    try {
      // ファイルアップロード処理
      List<String> msgList = lineBusStopsReportListsService.processUploadExcel(file, uploadFileName);
      if (msgList != null && !msgList.isEmpty()) {
        return internalServerError(errorBody(msgList));
      }
      return ok(successBody());
    } catch (Exception e) {
      LOGGER.error("【停留所情報インポート】アップロード処理失敗", e);
      return internalServerError(
          errorBody(List.of(getMessage(CommonMessage.ERROR_REGISTER.getMessage()))));
    }
  }

  /**
   * JSONエラーレスポンス用のBody
   *
   * @param errors エラーメッセージ一覧
   * @return 共通エラーレスポンスBody
   */
  private Map<String, Object> errorBody(List<String> errors) {
    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("errors", (errors == null) ? List.of() : errors);
    body.put("redirect", false);
    return body;
  }

  /**
   * JSON成功レスポンス用のBody
   *
   * @return 共通成功レスポンスBody
   */
  private Map<String, Object> successBody() {
    Map<String, Object> body = new HashMap<>();
    body.put("success", true);
    body.put("message", "アップロードが成功しました。");
    body.put("redirect", true);
    return body;
  }

  /**
   * アップロードファイル形式判定（.xlsx/.xlsかどうか）
   *
   * @param file アップロードファイル
   * @return true：Excel形式、false：それ以外
   */
  private boolean isExcelFile(MultipartFile file) {
    if (file == null) return false;
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null) return false;
    String name = originalFilename.toLowerCase(Locale.ROOT);
    return name.endsWith(".xlsx") || name.endsWith(".xls");
  }

  /**
   * HTTP 200 OK のResponseEntity
   *
   * @param body JSONレスポンスBody
   * @return 200 OKレスポンス
   */
  private ResponseEntity<Map<String, Object>> ok(Map<String, Object> body) {
    return ResponseEntity.ok(body);
  }

  /**
   * エラーのResponseEntity（400 Bad Request エラー）
   *
   * @param body JSONレスポンスBody
   * @return エラーレスポンス
   */
  private ResponseEntity<Map<String, Object>> badRequest(Map<String, Object> body) {
    return ResponseEntity.badRequest().body(body);
  }

  /**
   * エラーのResponseEntity（500 サーバ側エラー）
   *
   * @param body JSONレスポンスBody
   * @return エラーレスポンス
   */
  private ResponseEntity<Map<String, Object>> internalServerError(Map<String, Object> body) {
    return ResponseEntity.status(500).body(body);
  }

}
