package maint.controller.sales.importIncomes;

import java.util.List;
import maint.controller.BaseController;
import maint.dto.sales.importIncomes.BusServiceOptionsDto;
import maint.dto.sales.importIncomes.ImportIncomesDto;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.importIncomes.ImportIncomesForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.sales.ImportIncomesService;
import maint.utils.ExObjectUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 収入情報インポートCONTROLLER
 *
 * @author g.ko
 */

@Controller
@Validated
@RequestMapping("/sales/importIncomes")
@SessionAttributes("importIncomesSearchForm")
public class ImportIncomesController extends BaseController {

  /** 収入情報インポート画面パス */
  private static final String INDEX_PAGE = "sales/importIncomes/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.IMPORT_REVENUE;

  /** 収入情報インポートSERVICE */
  @Autowired
  ImportIncomesService importIncomesService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("importIncomesSearchForm")
  public ImportIncomesForm setImportIncomesForm() {
    return new ImportIncomesForm();
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
   * 収入情報インポート画面（初期表示）
   *
   * @param model Model
   * @param form 収入情報インポートFORM
   * @param initFlag フォーム初期化フラグ
   * @return 収入情報インポート画面
   */
  @GetMapping("/")
  public String index(Model model,
      @ModelAttribute("importIncomesSearchForm") ImportIncomesForm form,
      @RequestParam(name = "init", required = false) Integer initFlag,
      @RequestParam(name = "insert", required = false) Integer insertFlag,
      @RequestParam(name = "update", required = false) Integer updateFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new ImportIncomesForm();
    }

    if (ExObjectUtils.isNotBlank(insertFlag) && FlagValueEnums.TRUE.getValue().equals(insertFlag)) {
      this.setSuccessMsg(CommonMessage.SUCCESS_REGISTER);
    } else if (ExObjectUtils.isNotBlank(updateFlag) && FlagValueEnums.TRUE.getValue()
        .equals(updateFlag)) {
      this.setSuccessMsg(CommonMessage.SUCCESS_UPDATE);
    }

    commonProcess(model, form);

    return INDEX_PAGE;
  }

  /**
   * 収入情報インポート画面(検索)
   *
   * @param model Model
   * @param form 収入情報インポートFORM
   * @param bindingResult BindingResult
   * @return 収入情報インポート画面
   */
  @PostMapping("/")
  public String search(Model model,
      @Validated @ModelAttribute("importIncomesSearchForm") ImportIncomesForm form,
      BindingResult bindingResult) {
    hasAccessPermission(NOW_MENU, false, false);
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      commonProcess(model, form);
      return INDEX_PAGE;
    }

    // 収入情報インポート情報取得
    ImportIncomesDto dto = importIncomesService.getImportIncomes(form);

    // 収入情報インポート情報設定
    if (ExObjectUtils.isNotBlank(dto)) {
      form.setInfo(form, dto);
    } else {
      form.setDailyBusDaiyaId(null);
      form.setIncomeInfoId(null);
      form.setPassengersCount(null);
      form.setCashIncome(null);
      form.setIcAmount(null);
      form.setCouponTicket(null);
      this.setErrorMsg(CommonMessage.ERROR_GET_CORRECT_DATA);
    }

    commonProcess(model, form);

    return INDEX_PAGE;
  }

  /**
   * 収入情報インポート登録、変更
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   * @return INDEX画面
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("importIncomesSearchForm") ImportIncomesForm form,
      BindingResult bindingResult, UriComponentsBuilder builder,
      RedirectAttributes redirectAttributes) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 登録Validationチェック
    validationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、処理を実行しない。
    if (!this.errorMsgList.isEmpty()) {
      commonProcess(model, form);
      return INDEX_PAGE;
    }

    // 登録、変更処理
    if (ExObjectUtils.isBlank(form.getIncomeInfoId())) {
      try {
        // 登録処理
        Long incomeInfoId = importIncomesService.insert(form);
        form.setIncomeInfoId(incomeInfoId);
        commonProcess(model, form);
        model.addAttribute("importIncomesSearchForm", form);
        return "redirect:/sales/importIncomes/?insert=1";
      } catch (Exception e) {
        LOGGER.error("【収入情報インポート】登録失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_REGISTER);
        commonProcess(model, form);
        return INDEX_PAGE;
      }
    } else {
      try {
        // 更新処理
        importIncomesService.update(form);
        commonProcess(model, form);
        return "redirect:/sales/importIncomes/?update=1";
      } catch (Exception e) {
        LOGGER.error("【収入情報インポート】更新失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_UPDATE);
        commonProcess(model, form);
        return INDEX_PAGE;
      }
    }
  }

  /**
   * Validation処理
   *
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   */
  private void validationProcess(ImportIncomesForm form, BindingResult bindingResult) {

    // 登録Validationチェック
    importIncomesService.registerValidate(form, bindingResult);

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 収入情報インポートFORM
   */
  private void commonProcess(Model model, ImportIncomesForm form) {
    model.addAttribute("importIncomesSearchForm", form);
    // 路線取得（SELECT用）
    model.addAttribute("lineList", linesService.getBusStopsForSelectOption());
  }

  /**
   * 便オプション取得（SELECT用）
   *
   * @param rosenId 路線ID
   * @param serviceDate 運行実績
   * @return 便情報
   */
  @GetMapping("/getBusServiceOption")
  @ResponseBody
  public List<BusServiceOptionsDto> getBusServices(@RequestParam(required = false) Long rosenId,
      @RequestParam(required = false) String serviceDate) {
    if (ExObjectUtils.isBlank(rosenId) && ExObjectUtils.isBlank(serviceDate)) {
      return List.of();
    }
    return importIncomesService.getBusServiceOptions(rosenId, serviceDate);
  }
}
