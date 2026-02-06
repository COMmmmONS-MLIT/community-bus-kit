package maint.controller.operation.lineChargesInfoReport;


import maint.controller.BaseController;

import maint.enums.DispMode;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.lineChargesInfoReport.LineChargesInfoReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LineChargesInfoReportListsService;
import maint.utils.ExObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.util.*;

/**
 * 運賃情報インポートCONTROLLER
 *
 * @author JEON
 */

@Controller
@Validated
@RequestMapping("/operation/lineChargesInfoReport")
@SessionAttributes("lineChargesInfoReportListsExcelForm")
public class LineChargesInfoReportListsController extends BaseController {

  /**
   * 運賃情報インポート画面パス
   */
  private static final String INDEX_PAGE = "operation/lineChargesInfoReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.IMPORT_FARE;

  /**
   * 運賃情報インポートSERVICE
   */
  @Autowired
  LineChargesInfoReportListsService lineChargesInfoReportListsService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("LineChargesInfoReportListsExcelForm")
  LineChargesInfoReportListsExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 運賃情報インポート画面（初期表示）
   *
   * @param model    Model
   * @param form     運賃情報インポートFORM
   * @param initFlag 運賃情報インポートフォーム初期化フラグ
   * @return 運賃情報インポート画面
   */
  @GetMapping("/")
  public String index(Model model, LineChargesInfoReportListsExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new LineChargesInfoReportListsExcelForm();
    }
    model.addAttribute("lineChargesInfoReportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * Multipartファイルアップロードを処理するためのリゾルバを定義する。
   *
   * @return MultipartResolverのインスタンス
   */
  @Bean
  public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form  運賃情報インポートFORM
   * @return ダウンロードレスポンス
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("lineChargesInfoReportListsExcelForm")
      LineChargesInfoReportListsExcelForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);

    execCommonProc(model, NOW_MENU);
    form.setDispMode(DispMode.UPDATE_MODE);

    try {
      return lineChargesInfoReportListsService.getLineChargesInfoReportTemplate();
    } catch (Exception e) {
      model.addAttribute("lineChargesInfoReportListsExcelForm", form);
      setExcelDownloadErrorMessage("運賃情報インポート");
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
  public Object uploadExcel(Model model,
      @Validated @ModelAttribute("lineChargesInfoReportListsExcelForm")
      LineChargesInfoReportListsExcelForm form, BindingResult bindingResult,
      @RequestParam("excel_file") MultipartFile file,
      @RequestParam("uploadFileName") String uploadFileName) throws IOException {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);
    execCommonProc(model, this.NOW_MENU);

    // ① 画面入力のバリデーションエラー → 画面戻し
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }

    // ② ファイルチェック（null / 空）
    if (file == null || file.isEmpty()) {
      setErrorMsg(getMessage("message.error.file.check.required"));
      return INDEX_PAGE;
    }

    // ③ 拡張子チェック
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null
        || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
      setErrorMsg(getMessage("message.error.file.no.content.found"));
      return INDEX_PAGE;
    }


    // ④ サービスで Excel 内容のチェック＋登録まで実施
    List<String> msgList =
        lineChargesInfoReportListsService.processUploadExcel(file, uploadFileName);

    // ⑤ Service からの業務エラー
    if (!msgList.isEmpty()) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("success", false);
      errorBody.put("errors", msgList);
      errorBody.put("redirect", false);
      return ResponseEntity.internalServerError().body(errorBody);
    }

    // ⑥ 正常終了
    Map<String, Object> okBody = new HashMap<>();
    okBody.put("success", true);
    okBody.put("message", "アップロードが成功しました。");
    okBody.put("redirect", true);
    return ResponseEntity.ok(okBody);
  }
}
