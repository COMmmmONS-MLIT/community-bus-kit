package maint.controller.operation.importBusServices;

import java.util.*;

import jakarta.servlet.http.HttpSession;
import maint.controller.BaseController;
import maint.doc.ImportBusServicesExcelUpload;
import maint.enums.DispMode;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.importBusServices.ImportBusServicesForm;

import maint.service.operation.ImportBusServicesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 便情報インポートCONTROLLER
 *
 * @author JEKAL.H
 */

@Controller
@Validated
@RequestMapping("/operation/importBusServices")
@SessionAttributes("importBusServicesForm")
public class ImportBusServicesController extends BaseController {

  /** 便情報インポート画面パス */
  private static final String INDEX_PAGE = "operation/importBusServices/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.IMPORT_TRIP;

  /** 便情報インポートSERVICE */
  @Autowired
  ImportBusServicesService importBusServicesService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 便情報インポート画面（初期表示）
   *
   * @param model    Model
   * @param form     便情報インポートFORM
   * @param initFlag フォーム初期化フラグ
   * @return 便情報インポート画面
   */
  @GetMapping("/")
  public String index(Model model, ImportBusServicesForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new ImportBusServicesForm();
    }
    model.addAttribute("importBusServicesForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form  便情報インポートFORM
   * @return ダウンロードレスポンス
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("importBusServicesForm")
      ImportBusServicesForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);

    execCommonProc(model, NOW_MENU);
    form.setDispMode(DispMode.UPDATE_MODE);

    try {
      return importBusServicesService.getImportBusServicesTemplate();
    } catch (Exception e) {
      model.addAttribute("importBusServicesForm", form);
      setExcelDownloadErrorMessage("便情報インポート");
      return INDEX_PAGE;
    }
  }

  /**
   * EXCELアップロード処理
   *
   * @param file           MultipartFile
   * @param uploadFileName ファイル名
   */
  @Transactional
  @PostMapping(value = "/uploadExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> register(
      @RequestParam("excel_file") MultipartFile file,
      @RequestParam("uploadFileName") String uploadFileName, HttpSession session) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    Map<String, Object> body = new HashMap<>();
    List<String> msgList = new ArrayList<>();

    // ファイルチェック（ファイル名と拡張子）
    if (!importBusServicesService.uploadFileValidationCheck(file)) {
      msgList.add("ファイルを確認してください（必須/拡張子: .xlsx）。");
      session.setAttribute("errorMessages", msgList);
      body.put("success", false);
      body.put("errors", msgList);
      body.put("redirect", true);
      return ResponseEntity.badRequest().body(body);
    }

    // Excel読み込み
    try (ImportBusServicesExcelUpload excel = new ImportBusServicesExcelUpload(file, uploadFileName)) {

      // データが0件ならNG
      if (ExObjectUtils.isBlank(excel) || excel.getDataCount() <= 0) {
        msgList.add("ファイルに登録内容が存在しません。ファイルをもう一度確認してください。");
        session.setAttribute("errorMessages", msgList);
        body.put("success", false);
        body.put("errors", msgList);
        body.put("redirect", true);
        return ResponseEntity.badRequest().body(body);
      }

      // 便情報インポート
      importBusServicesService.importBusServices(excel, msgList, file, uploadFileName);

    } catch (Exception e) {
      LOGGER.error("【便ダイヤ登録表Excelアップロード(AJAX)】エラー", e);
      msgList.add("システムエラーが発生しました。時間をおいて再度お試しください。");
      session.setAttribute("errorMessages", msgList);
      body.put("success", false);
      body.put("errors", msgList);
      body.put("redirect", true);
      return ResponseEntity.internalServerError().body(body);
    }

    boolean ok = msgList.isEmpty();
    body.put("success", ok);
    if (!ok) {
      session.setAttribute("errorMessages", msgList);
      body.put("success", false);
      body.put("errors", msgList);
      body.put("redirect", true);
      return ResponseEntity.badRequest().body(body);

    }
    String successMsg = "アップロードが成功しました。";
    session.setAttribute("successMessages", java.util.List.of(successMsg));

    Map<String, Object> okBody = new HashMap<>();
    okBody.put("success", true);
    okBody.put("message", successMsg);
    okBody.put("redirect", true);
    return ResponseEntity.ok(okBody);
  }
}
