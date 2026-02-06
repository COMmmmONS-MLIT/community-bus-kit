package maint.controller.record.operationRecords;

import java.time.LocalDateTime;
import java.util.List;

import maint.constants.CommConstant;
import maint.controller.BaseController;
import maint.dto.record.operationRecords.OperationRecordsDto;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.operationRecords.OperationRecordsForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.record.OperationRecordsService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 運行実績CONTROLLER
 *
 * @author g.ko
 */

@Controller
@Validated
@RequestMapping("/record/operationRecords")
@SessionAttributes("operationRecordsForm")
public class OperationRecordsController extends BaseController {

  /** 停留所画面パス */
  private static final String INDEX_PAGE = "record/operationRecords/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.OPERATION_RECORD;

  /** 運行実績SERVICE */
  @Autowired
  OperationRecordsService operationRecordsService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("operationRecordsForm")
  public OperationRecordsForm setOperationRecordsForm() {
    return new OperationRecordsForm();
  }

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
   * 運行実績画面
   *
   * @param model Model
   * @param form 運行実績FORM
   * @param initFlag 検索フォーム初期化フラグ
   * @return 運行実績画面
   */
  @GetMapping("/")
  public String index(Model model, OperationRecordsForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new OperationRecordsForm();
    }

    commonProcess(model, form);
    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 運行実績FORM
   */
  private void commonProcess(Model model, OperationRecordsForm form) {
    // 路線取得（SELECT用）
    model.addAttribute("lineList", linesService.getBusStopsForSelectOption());
    model.addAttribute("operationRecordsForm", form);
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form 運行回数出力FORM
   * @param bindingResult BindingResult
   */
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("operationRecordsForm") OperationRecordsForm form,
      BindingResult bindingResult) {

    // 入力チェック
    if (bindingResult.hasErrors()) {
      commonProcess(model, form);
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }

    try {
      // 運行実績情報取得
      List<OperationRecordsDto> rows =
          operationRecordsService.getOperationRecords(form.getExtractionStartDate(),
              form.getExtractionEndDate(), form.getRosenId());

      // 運行実績情報が0件の場合
      if (ExObjectUtils.isBlank(rows)) {
        commonProcess(model, form);
        setErrorMsg(CommonMessage.ERROR_GET_CORRECT_DATA);
        return INDEX_PAGE;
      }

      // Excel生成
      byte[] bytes = operationRecordsService.buildExcelFromTemplate(rows);

      String datetime = LocalDateTime.now().format(ExDateFormat.uuuuMMddHHmmss);
      String baseName = "運行実績_" + datetime + CommConstant.EXCEL_FILE_EXTENSION;
      String asciiFallback = "operation-records.xlsx";

      String encoded = java.net.URLEncoder.encode(baseName, java.nio.charset.StandardCharsets.UTF_8)
          .replace("+", "%20");

      return org.springframework.http.ResponseEntity.ok()
          .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + asciiFallback + "\"; filename*=UTF-8''" + encoded)
          .contentType(org.springframework.http.MediaType.parseMediaType(
              "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(bytes);

    } catch (Exception e) {
      LOGGER.error("【運行実績管理】Excelダウンロードエラー", e);
      commonProcess(model, form);
      setExcelDownloadErrorMessage("運行実績");
      return INDEX_PAGE;
    }
  }
}
