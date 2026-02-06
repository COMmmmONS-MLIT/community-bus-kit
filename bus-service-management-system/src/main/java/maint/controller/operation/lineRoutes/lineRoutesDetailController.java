package maint.controller.operation.lineRoutes;

import maint.controller.BaseController;
import maint.dto.operation.lineRoutes.LineRouteBusStopsDto;
import maint.dto.operation.lines.SearchLinesDto;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.lineRoutes.LineRoutesDetailForm;
import maint.message.CommonMessage;
import maint.service.CommonService;
import maint.service.operation.LineRoutesService;
import maint.service.operation.LinesService;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系統管理詳細 Controller
 *
 * @author y.kim
 */

@Controller
@RequestMapping(value = "/operation/lineRoutes/detail")
@SessionAttributes("lineRoutesDetailForm")
public class lineRoutesDetailController extends BaseController {

  /** 系統詳細画面PATH */
  private static final String DETAIL_PAGE = "operation/lineRoutes/detail";
  /** 系統確認画面PATH */
  private static final String CONFIRM_PAGE = "operation/lineRoutes/confirm";
  /** 系統完了画面PATH */
  private static final String COMPLETE_PAGE = "operation/lineRoutes/complete";
  /** 系統完了URL */
  private static final String COMPLETE_URL = "operation/lineRoutes/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.SYSTEM_LINE;

  /** 系統SERVICE */
  @Autowired
  LineRoutesService lineRoutesService;

  /** 共通SERVICE */
  @Autowired
  CommonService commonService;

  /** 路線SERVICE */
  @Autowired
  private LinesService linesService;

  // 登録停留所errorMSGリスト
  List<String> lineRouteBusStopErrorMsgList = new ArrayList<>();

  /**
   * 系統詳細FORM設定
   *
   * @return 系統詳細FORM
   */
  @ModelAttribute("lineRoutesDetailForm")
  public LineRoutesDetailForm setLineRoutesDetailForm() {
    return new LineRoutesDetailForm();
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
   * 系統詳細画面(新規登録)
   *
   * @param model Model
   * @param form  系統詳細FORM
   * @return 系統詳細画面
   */
  @GetMapping("/")
  public String newLineRoutes(Model model,
      @ModelAttribute("displayForm") LineRoutesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);
    form.setServiceStartDate(ExDateUtils.getCurrentDate());
    form.setServiceEndDate("9999-12-31");
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 系統詳細画面（変更）
   *
   * @param model Model
   * @param form  系統詳細FORM
   * @return 系統詳細画面
   */
  @PostMapping("/")
  public String loadLineRoutes(Model model,
      @ModelAttribute("displayForm") LineRoutesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    LineRoutesDetailForm loadedForm = lineRoutesService.getLineRouteByPkId(form.getLineRouteId());
    form.setAllPropertiesFrom(loadedForm);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 系統詳細画面(戻る)
   *
   * @param model Model
   * @param form  系統詳細FORM
   * @return 系統詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("lineRoutesDetailForm") LineRoutesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();
    isServiceStartDateChangeAllowed(model, form);
    model.addAttribute("selectableLines", getSelectableLines());
    model.addAttribute("dispLineBusStopsFlag", getDispLineBusStopsFlag(form));
    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 系統確認画面(参照系統)
   *
   * @param model Model
   * @param form  系統詳細FORM
   * @return 系統確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") LineRoutesDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    LineRoutesDetailForm loadedForm = lineRoutesService.getLineRouteByPkId(form.getLineRouteId());
    form.setAllPropertiesFrom(loadedForm);
    form.setDispMode(DispMode.CONTENT_MODE);

    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 系統確認画面
   *
   * @param model         Model
   * @param form          系統詳細FORM
   * @param bindingResult BindingResult
   * @return 系統確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") LineRoutesDetailForm form,
      BindingResult bindingResult) {

    hasAccessPermission(NOW_MENU, true, false);

    LineRoutesDetailForm orgForm = (LineRoutesDetailForm) model.asMap().get("lineRoutesDetailForm");

    form.setOriginFormData(orgForm);

    form.isInvalidSession();
    // 期間Toが未入力の場合、9999/12/31で登録
    if (ExObjectUtils.isBlank(form.getServiceEndDate())) {
      form.setServiceEndDate("9999-12-31");
    }

    commonValidationProcess(model, form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      isServiceStartDateChangeAllowed(model, form);
      model.addAttribute("selectableLines", getSelectableLines());
      return DETAIL_PAGE;
    }

    commonInitProcess(model, form);
    model.addAttribute("dispLineBusStopsFlag", true);

    return CONFIRM_PAGE;
  }

  /**
   * 系統登録、変更
   *
   * @param model              Model
   * @param form               系統詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 系統完了URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("lineRoutesDetailForm") LineRoutesDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);

    LineRoutesDetailForm orgForm = (LineRoutesDetailForm) model.asMap().get("lineRoutesDetailForm");

    form.setOriginFormData(orgForm);

    form.isInvalidSession();

    commonValidationProcess(model, form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
        lineRoutesService.insert(form);
      } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
        lineRoutesService.update(form);
        return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
      }
    } catch (Exception e) {
      model.addAttribute("errorMessage", form.getDispMode().equals(DispMode.INSERT_MODE) ?
          getMessage(CommonMessage.ERROR_REGISTER.getMessage()) :
          getMessage(CommonMessage.ERROR_UPDATE.getMessage()));
      form.setSessionKey("");
      return COMPLETE_PAGE;
    }

    model.addAttribute("lineRoutesDetailForm", form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 系統完了画面
   *
   * @param model Model
   * @param form  系統詳細FORM
   * @return 系統完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("lineRoutesDetailForm") LineRoutesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  系統詳細FORM
   */
  private void commonInitProcess(Model model, LineRoutesDetailForm form) {

    isServiceStartDateChangeAllowed(model, form);
    // 路線一覧 (SELECT用)
    model.addAttribute("selectableLines", getSelectableLines());
    model.addAttribute("dispLineBusStopsFlag", getDispLineBusStopsFlag(form));
    model.addAttribute("lineRoutesDetailForm", form);
  }

  /**
   * 路線リスト取得 (SELECT用)
   *
   * @return 路線リスト
   */
  private List<SearchLinesDto> getSelectableLines() {
    UserSession userSession = getUserSession();
    Long businessCorpId = userSession.getBusinessCorpId();
    // 路線一覧 (SELECT用)
    return lineRoutesService.getSelectableLines(businessCorpId);
  }

  /**
   * 共通Validation処理
   *
   * @param model         model
   * @param form          系統詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(Model model, LineRoutesDetailForm form, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    // エラーメッセージ初期化
    this.lineRouteBusStopErrorMsgList = new ArrayList<>();

    // 系統番号入力チェック
    if (ExObjectUtils.isEmpty(form.getLineRouteShortName()) && ExObjectUtils.isEmpty(
        form.getLineRouteNumber())) {
      this.setErrorMsg("validation.lineRoutes.error.requiredSystemNumberOrName");
    }

    // 停留所入力チェック
    if (ExObjectUtils.isEmpty(form.getLineRouteBusStopList())) {
      this.lineRouteBusStopErrorMsgList.add(getMessage("validation.lineRoutes.error.emptyBusStop"));
    }

    // 停留所整合性チェック
    if (ExObjectUtils.isNotEmpty(form.getLineRouteBusStopList())) {
      List<LineRouteBusStopsDto> referenceBusStops =
          commonService.getLineBusStopListByServiceDateAndRosenId(form.getServiceStartDate(),
              form.getServiceEndDate(), form.getRosenId());

      // 路線停留所一覧と、登録された停留所 チェック
      Set<Long> referenceSignpostIds =
          referenceBusStops.stream().map(LineRouteBusStopsDto::getSignpostId)
              .filter(Objects::nonNull).collect(Collectors.toSet());

      List<Long> inputSignpostIds =
          form.getLineRouteBusStopList().stream().map(LineRouteBusStopsDto::getSignpostId)
              .filter(Objects::nonNull).toList();

      List<Long> unmatchedSignpostIds =
          inputSignpostIds.stream().filter(id -> !referenceSignpostIds.contains(id)).toList();

      if (ExObjectUtils.isNotEmpty(unmatchedSignpostIds)) {
        this.lineRouteBusStopErrorMsgList.add(getMessage("validation.lineRoutes.error.checkBusStopExistence"));
      }

      // 重複された signpostId チェック
      Map<Long, Integer> idCountMap = new HashMap<>();
      List<Long> duplicateSignpostIds = new ArrayList<>();

      for (Long id : inputSignpostIds) {
        if (id == null)
          continue;

        int count = idCountMap.getOrDefault(id, 0) + 1;
        idCountMap.put(id, count);

        if (count == 2) {
          duplicateSignpostIds.add(id);
        }
      }

      if (ExObjectUtils.isNotEmpty(duplicateSignpostIds)) {
        this.lineRouteBusStopErrorMsgList.add(getMessage("validation.lineRoutes.error.duplicateBusStop"));
      }
    }

    // 路線情報利用期間チェック
    if (ExObjectUtils.isNotBlankAll(form.getServiceStartDate(), form.getServiceEndDate(),
        form.getRosenId()) && linesService.checkLineByDateAndLine(form.getServiceStartDate(),
        form.getServiceEndDate(), form.getRosenId())) {
      this.setErrorMsg("validation.lineRoutes.error.checkLineRoute");
    }

    // 便ダイヤ利用期間チェック
    if (ExObjectUtils.isNotBlank(form.getServiceEndDate())) {
      if (DispMode.UPDATE_MODE.equals(form.getDispMode()) && lineRoutesService.validateDaiyaRevision(
          form.getRosenId(), form.getLineRouteId(), form.getServiceEndDate())) {
        this.setErrorMsg("validation.lineRoutes.error.checkServiceSchedule");
      }
    }

    if (ExObjectUtils.isNotEmpty(lineRouteBusStopErrorMsgList)) {
      if (ExObjectUtils.isEmpty(this.errorMsgList)) {
        this.setErrorMsg(CommonMessage.ERROR_INPUT);
      }
      model.addAttribute("lineRouteBusStopErrorMsgList", lineRouteBusStopErrorMsgList);
    }
  }

  /**
   * 路線停留所登録一覧 表示フラグ
   *
   *
   */
  private boolean getDispLineBusStopsFlag(LineRoutesDetailForm form) {
    if (ExObjectUtils.isBlank(form.getServiceStartDate()))
      return false;
    DispMode dispMode = form.getDispMode();

    if (DispMode.CONTENT_MODE.equals(dispMode)) {
      return true;
    }

    if (DispMode.UPDATE_MODE.equals(dispMode)) {
      Integer compareResult =
          ExDateUtils.compareStringDate(form.getServiceStartDate(), ExDateUtils.getCurrentDate());
      if (ExObjectUtils.isNotBlank(compareResult) && compareResult < 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * 利用期間開始の変更可否設定
   *
   * @param model Model
   * @param form  系統詳細FORM
   */
  private void isServiceStartDateChangeAllowed(Model model, LineRoutesDetailForm form) {
    if (DispMode.UPDATE_MODE.equals(form.getDispMode()) && ExObjectUtils.isNotBlank(
        form.getServiceStartDate())) {
      boolean isStartDateNotPast = ExDateUtils.compareStringDate(ExDateUtils.getCurrentDate(),
          form.getServiceStartDate()) <= 0;
      model.addAttribute("isServiceStartDateChangeAllowed", isStartDateNotPast);
    }
  }
}
