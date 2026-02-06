package maint.controller.operation.lineCharges;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import maint.controller.BaseController;
import maint.dto.operation.lineCharges.LineChargeDetailDto;
import maint.enums.DispMode;
import maint.enums.LineChargeDivision;
import maint.enums.Menu;
import maint.form.lineCharges.LineChargeDetailForm;
import maint.message.CommonMessage;
import maint.service.operation.LineChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 運賃詳細CONTROLLER
 *
 * @author g.ko
 */
@Controller
@RequestMapping("/operation/lineCharges/detail")
@SessionAttributes("lineChargeDetailForm")
public class LineChargesDetailController extends BaseController {

  /** 運賃詳細画面パス */
  private static final String DETAIL_PAGE = "operation/lineCharges/detail";

  /** 運賃確認画面パス */
  private static final String CONFIRM_PAGE = "operation/lineCharges/confirm";

  /** 運賃完了画面パス */
  private static final String COMPLETE_PAGE = "operation/lineCharges/complete";

  /** 運賃完了URL */
  private static final String COMPLETE_URL = "operation/lineCharges/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.FARE;

  /** 運賃SERVICE */
  @Autowired
  LineChargesService lineChargesService;

  @Autowired
  SmartValidator validator;


  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("lineChargeDetailForm")
  public LineChargeDetailForm setLineChargeDetailForm() {
    return new LineChargeDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 運賃詳細
   *
   * @param model Model
   * @param form 検索FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String loadLineCharge(Model model,
      @ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 共通処理
    commonProcess(model, form);

    // 画面モード設定
    form.setDispMode(form.getInsertFlag() == 1 ? DispMode.INSERT_MODE : DispMode.UPDATE_MODE);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    model.addAttribute("lineChargeDetailForm", form);
    return DETAIL_PAGE;
  }

  /**
   * 運賃確認画面(参照権限)
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 確認画面
   */
  @RequestMapping("/content/")
  public String content(Model model,
      @ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 画面モード設定
    form.setDispMode(DispMode.CONTENT_MODE);

    // 共通処理
    commonProcess(model, form);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    return CONFIRM_PAGE;
  }

  /**
   * 詳細画面共通処理
   *
   * @param model Model
   * @param form 詳細FORM
   */
  private void commonProcess(Model model, LineChargeDetailForm form) {
    List<LineChargeDetailDto> dtoList =
        lineChargesService.getLineChargeDetailsByRevisionId(form.getChargeRevisionId());

    form.setInfo(dtoList, form.getPage());

    putTriangleModelFromDb(model, form);
  }

  /**
   * 運賃確認画面
   *
   * @param model         Model
   * @param form          運賃詳細FORM
   * @param bindingResult BindingResult
   * @return 運賃確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form,
      BindingResult bindingResult) {

    validateByDivision(form, bindingResult);
    boolean isError = commonValidationProcess(bindingResult);

    if (isError) {
      putTriangleModelFromDb(model, form);
      return DETAIL_PAGE;
    }
    // 成功時だけ：非選択区分を消す
    clearNonSelectedDivision(form);

    // 三角運賃選択時は三角運賃表を渡す
    if (LineChargeDivision.TRIANGULAR_FARE.getValue().equals(form.getLineChargeDivision())) {
      putTriangleModelFromDb(model, form);
    }

    return CONFIRM_PAGE;
  }

  /**
   * 運賃登録・変更
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult  バリデーション結果
   * @param builder URL構築用オブジェクト
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form,
      BindingResult bindingResult,
      UriComponentsBuilder builder) {

    hasAccessPermission(NOW_MENU, true, false);
    form.isInvalidSession();

    validateByDivision(form, bindingResult);
    boolean isError = commonValidationProcess(bindingResult);

    if (isError) {
      putTriangleModelFromDb(model, form);
      return DETAIL_PAGE;
    }

    try {
      if (DispMode.INSERT_MODE.equals(form.getDispMode()) || DispMode.UPDATE_MODE.equals(
          form.getDispMode())) {
        lineChargesService.registerByForm(form);
      }

      return "redirect:" + builder.path(COMPLETE_URL).build().toUri();

    } catch (Exception e) {
      LOGGER.error("【運賃管理】登録/更新失敗 mode={}", form.getDispMode(), e);
      model.addAttribute("errorMessages",
          DispMode.INSERT_MODE.equals(form.getDispMode())
              ? getMessage(CommonMessage.ERROR_REGISTER.getMessage())
              : getMessage(CommonMessage.ERROR_UPDATE.getMessage())
      );

      putTriangleModelFromDb(model, form);
      return DETAIL_PAGE;
    }
  }

  /**
   * 運賃完了画面
   *
   * @param form  運賃詳細FORM
   * @return 運賃完了画面
   */
  @GetMapping("/complete/")
  public String complete(@ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 運賃詳細画面（戻る）
   *
   * @param model Model
   * @param form  運賃詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("lineChargeDetailForm") LineChargeDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);
    form.isInvalidSession();

    putTriangleModelFromDb(model, form);
    return DETAIL_PAGE;
  }

  /**
   * Validation検証
   *
   * @param form 運賃詳細FORM
   * @param bindingResult BindingResult
   */
  private void validateByDivision(LineChargeDetailForm form, BindingResult bindingResult) {
    Integer div = form.getLineChargeDivision();

    // 共通は常に検証
    validator.validate(form, bindingResult, LineChargeDetailForm.CommonGroup.class);

    // 運賃区分がnullの場合は終了
    if (div == null) return;

    if (LineChargeDivision.FLAT_FARE.getValue().equals(div)) {
      validator.validate(form, bindingResult, LineChargeDetailForm.UniformGroup.class);
    } else if (LineChargeDivision.TRIANGULAR_FARE.getValue().equals(div)) {
      validator.validate(form, bindingResult, LineChargeDetailForm.TriangleGroup.class);
    }
  }

  /**
   * 共通Validation処理
   *
   * @param bindingResult BindingResult
   * @return エラーフラグ（true:エラーあり, false:エラーなし）
   */
  private boolean commonValidationProcess(BindingResult bindingResult) {
    boolean isError = bindingResult.hasErrors();
    if (isError) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
    return isError;
  }

  /**
   * 三角運賃表示処理
   *
   * @param model Model
   * @param form 運賃詳細FORM
   */
  private void putTriangleModelFromDb(Model model, LineChargeDetailForm form) {
    List<LineChargeDetailDto> dtoList =
        lineChargesService.getLineChargeDetailsByRevisionId(form.getChargeRevisionId());

    Map<String, Object> tri = lineChargesService.calcTriangleTable(dtoList);

    model.addAttribute("rowStopIds", tri.get("rowStopIds"));
    model.addAttribute("colStopIds", tri.get("colStopIds"));
    model.addAttribute("busStopNames", tri.get("busStopNames"));
    model.addAttribute("rowStops", tri.get("rowStops"));
    model.addAttribute("colStops", tri.get("colStops"));
  }

  /**
   * 確認画面遷移時FORM初期化処理
   *
   * @param form 運賃詳細FORM
   */
  private void clearNonSelectedDivision(LineChargeDetailForm form) {
    Integer div = form.getLineChargeDivision();
    if (div == null) return;

    if (LineChargeDivision.FLAT_FARE.getValue().equals(div)) {
      // 均一選択 → 三角の「値」だけ消す（Listは消さない）
      if (form.getTriangle() != null) {
        for (var t : form.getTriangle()) {
          t.setLineCharge(null);
          t.setIcPrice(null);
        }
      }
    } else if (LineChargeDivision.TRIANGULAR_FARE.getValue().equals(div)) {
      form.setUniformCharge("");
      form.setUniformIcPrice("");
      form.setUniformTransferType(null);
    }
  }
}
