package maint.controller.baseInfo.busStopSignposts;

import maint.controller.BaseController;
import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.dto.baseInfo.busStopSignposts.SignpostsLinkDetailDto;
import maint.enums.DispMode;
import maint.enums.IndoorDivision;
import maint.enums.Menu;
import maint.form.busStopSignposts.BusStopSignpostsDetailForm;
import maint.message.BusStopsMessage;
import maint.message.CommonMessage;
import maint.properties.GoogleMapsProperties;
import maint.service.baseInfo.BusStopSignpostsService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static maint.utils.date.ExDateUtils.getCurrentDate;

/**
 * 停留所・標柱詳細CONTROLLER
 *
 * @author k.takeuchi
 */
@Controller
@RequestMapping("/baseInfo/busStopSignposts/detail")
@SessionAttributes("busStopSignpostsDetailForm")
public class BusStopSignpostsDetailController extends BaseController {

  /** 停留所・標柱詳細画面パス */
  private static final String DETAIL_PAGE = "baseInfo/busStopSignposts/detail";
  /** 停留所・標柱確認画面パス */
  private static final String CONFIRM_PAGE = "baseInfo/busStopSignposts/confirm";
  /** 停留所・標柱完了画面パス */
  private static final String COMPLETE_PAGE = "baseInfo/busStopSignposts/complete";
  /** 停留所・標柱完了画面URL */
  private static final String COMPLETE_URL = "baseInfo/busStopSignposts/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.STOP_MARKER;

  /** 停留所・標柱SERVICE */
  @Autowired
  BusStopSignpostsService busStopSignpostsService;

  /** googleマップキー */
  private final GoogleMapsProperties googleMapsProperties;

  /**
   * GoogleMapsPropertiesを注入してSignpostsDetailController初期化します。
   *
   * @param googleMapsProperties application.yml に定義された Google Maps 関連の設定を格納するクラス
   */
  @Autowired
  public BusStopSignpostsDetailController(GoogleMapsProperties googleMapsProperties) {
    this.googleMapsProperties = googleMapsProperties;
  }

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("busStopSignpostsDetailForm")
  public BusStopSignpostsDetailForm setBusStopSignpostsDetailForm() {
    return new BusStopSignpostsDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("googleMapsApiKey", googleMapsProperties.getKey());
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 停留所・標柱詳細(新規登録)
   *
   * @param model Model
   * @param form 検索FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String newBusStop(Model model,
      @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 新規登録フォーム初期化
    form = new BusStopSignpostsDetailForm(form.getPage());

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(1);
    form.setSignpostsSize(0);
    form.setPressDownAddSignpostsButtonNo(-1);

    return DETAIL_PAGE;
  }

  /**
   * 停留所・標柱詳細(変更)
   *
   * @param model Model
   * @param form 検索FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String loadBusStop(Model model,
      @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 停留所情報取得
    BusStopLinkDetailDto busStopLinkDetailDto = busStopSignpostsService.getBusStopByPk(form.getBusStopId());

    // 標柱情報一覧取得
    List<SignpostsLinkDetailDto> signpostsLinkDetailDtoList = busStopSignpostsService.getSignpostsLinkDetailDtoList(form.getBusStopId());
    for(int i=0; i<signpostsLinkDetailDtoList.size(); i++) {
      signpostsLinkDetailDtoList.get(i).setPageListNo(String.format("%02d", i + 1));
      signpostsLinkDetailDtoList.get(i).setModeFlag(1);
      if (signpostsLinkDetailDtoList.get(i).getArrivalPointRadius() != null) {
        signpostsLinkDetailDtoList.get(i).setArrivalPointRadiusStr(String.valueOf(signpostsLinkDetailDtoList.get(i).getArrivalPointRadius()));
      }
    }
    busStopLinkDetailDto.setSignpostsLinkDetailDtoList(signpostsLinkDetailDtoList);

    // 停留所情報設定
    form.setInfo(busStopLinkDetailDto, form.getPage(), DispMode.UPDATE_MODE);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(1);
    form.setSignpostsSize(0);
    form.setPressDownAddSignpostsButtonNo(-1);

    return DETAIL_PAGE;
  }

  /**
   * 停留所・標柱詳細画面（戻る）
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(form.getBusStopLinkDetailDtoList().size());
    form.setSignpostsSize(form.getBusStopLinkDetailDtoList().size() - 1);
    form.setPressDownAddSignpostsButtonNo(-1);

    return DETAIL_PAGE;
  }

  /**
   * 停留所・標柱確認画面(参照権限)
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 確認画面
   */
  @RequestMapping("/content/")
  public String content(Model model,
      @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    // 停留所情報取得
    BusStopLinkDetailDto busStopLinkDetailDto = busStopSignpostsService.getBusStopByPk(form.getBusStopId());

    // 標柱情報一覧取得
    List<SignpostsLinkDetailDto> signpostsLinkDetailDtoList = busStopSignpostsService.getSignpostsLinkDetailDtoList(form.getBusStopId());
    for(int i=0; i<signpostsLinkDetailDtoList.size(); i++) {
      signpostsLinkDetailDtoList.get(i).setPageListNo(String.format("%02d", i + 1));
      signpostsLinkDetailDtoList.get(i).setModeFlag(2);
    }
    busStopLinkDetailDto.setSignpostsLinkDetailDtoList(signpostsLinkDetailDtoList);

    // 停留所情報設定
    form.setInfo(busStopLinkDetailDto, form.getPage(), DispMode.CONTENT_MODE);

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(form.getBusStopLinkDetailDtoList().size());
    form.setSignpostsSize(form.getBusStopLinkDetailDtoList().size() - 1);
    form.setPressDownAddSignpostsButtonNo(-1);

    return CONFIRM_PAGE;
  }

  /**
   * 停留所・標柱確認画面
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData((BusStopSignpostsDetailForm) model.asMap().get("busStopSignpostsDetailForm"));

    // 期間Toが未入力の場合、9999/12/31で登録
    List<BusStopLinkDetailDto> busStopLinkDetailDto = form.getBusStopLinkDetailDtoList();
    for (int i=0; i<busStopLinkDetailDto.size(); i++) {
      if (ExObjectUtils.isBlank(busStopLinkDetailDto.get(i).getServiceEndDate())) {
        busStopLinkDetailDto.get(i).setServiceEndDate("9999-12-31");
      }
    }

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(form.getBusStopLinkDetailDtoList().size());
    form.setSignpostsSize(form.getBusStopLinkDetailDtoList().size() - 1);
    form.setPressDownAddSignpostsButtonNo(-1);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    return CONFIRM_PAGE;
  }

  /**
   * 停留所・標柱登録、変更
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "busStopSignpostsDetailForm";
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      commonProcess(model, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    // 登録、変更処理
    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      try {
        // 登録処理
        busStopSignpostsService.insert(form);
      } catch (Exception e) {
        LOGGER.error("【停留所・標柱管理】登録失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_REGISTER);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
      try {
        // 更新処理
        busStopSignpostsService.update(form);
      } catch (Exception e) {
        LOGGER.error("【停留所・標柱管理】更新失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_UPDATE);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    }

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 停留所・標柱完了画面
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE)
        ? CommonMessage.SUCCESS_REGISTER
        : CommonMessage.SUCCESS_UPDATE);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 停留所追加
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/addBusStop/")
  public String addbusstop(Model model, @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 停留所追加処理
    form.addBusStop();

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setBusStopSize(form.getBusStopLinkDetailDtoList().size());
    form.setSignpostsSize(form.getBusStopLinkDetailDtoList().size() - 1);
    form.setPressDownAddSignpostsButtonNo(-1);

    return DETAIL_PAGE;
  }

  /**
   * 標柱追加
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/addSignposts/")
  public String addsignposts(Model model, @ModelAttribute("busStopSignpostsDetailForm") BusStopSignpostsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 標柱追加処理
    form.addSignposts(form.getPressDownAddSignpostsButtonNo() - 1);

    // 共通処理
    commonProcess(model, form);

    // 個別初期処理
    form.setSignpostsSize(form.getPressDownAddSignpostsButtonNo() - 1);
    form.setPressDownAddSignpostsButtonNo(-1);

    return DETAIL_PAGE;
  }

  /**
   * 共通Validation処理
   *
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(BusStopSignpostsDetailForm form, BindingResult bindingResult) {

    // 停留所のリストの取得～チェック
    List<BusStopLinkDetailDto> busStopLinkDetailDto = form.getBusStopLinkDetailDtoList();
    for (int i=0; i<busStopLinkDetailDto.size(); i++) {
      String valueName;

      // 緯度経度の未入力チェック　(停留所が「屋内」の場合のみ）
      valueName = "busStopLinkDetailDtoList[" + i + "].latitudeLongitude";
      if (IndoorDivision.INDOOR.getValue().equals(busStopLinkDetailDto.get(i).getIndoorBusTerminalFlag())) {
        if (busStopLinkDetailDto.get(i).getLatitudeLongitude().isBlank()) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), valueName,
              busStopLinkDetailDto.get(i).getLatitudeLongitude(), false, null, null,
              getMessage(BusStopsMessage.ERROR_LATITUDELONGITUDE_REQUIRED.getMessage())));
        }
      }

      // 利用開始日チェック（利用開始日変更可否フラグがtrueかつ利用開始日が過去日の場合、エラー）
      valueName = "busStopLinkDetailDtoList[" + i + "].serviceStartDate";
      if (ExObjectUtils.isNotBlank(busStopLinkDetailDto.get(i).getServiceStartDate())) {
        if (busStopLinkDetailDto.get(i).isCanServiceStartDateChangeFlag() && (
            ExDateUtils.compareLocalDate(busStopLinkDetailDto.get(i).getServiceStartDate(), getCurrentDate()) < 0)) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), valueName,
              busStopLinkDetailDto.get(i).getServiceStartDate(), false, null, null,
              getMessage(BusStopsMessage.ERROR_PAST_START_DATE.getMessage())));
        }
      }

      // 利用終了日チェック（利用開始日変更可否フラグがfalseかつ利用終了日が過去日の場合、エラー）
      valueName = "busStopLinkDetailDtoList[" + i + "].serviceEndDate";
      if (ExObjectUtils.isNotBlank(busStopLinkDetailDto.get(i).getServiceEndDate())) {
        if (!busStopLinkDetailDto.get(i).isCanServiceStartDateChangeFlag() && (
            ExDateUtils.compareLocalDate(busStopLinkDetailDto.get(i).getServiceEndDate(), getCurrentDate()) < 0)) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), valueName,
              busStopLinkDetailDto.get(i).getServiceEndDate(), false, null, null,
              getMessage(BusStopsMessage.ERROR_PAST_END_DATE.getMessage())));
        }
      }

      // 利用終了日チェック（開始日より過去日の場合、エラー）
      if (ExObjectUtils.isNotBlankAll(busStopLinkDetailDto.get(i).getServiceStartDate(), busStopLinkDetailDto.get(i).getServiceEndDate())) {
        if (ExDateUtils.compareLocalDate(busStopLinkDetailDto.get(i).getServiceEndDate(), busStopLinkDetailDto.get(i).getServiceStartDate()) < 0) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), valueName,
              busStopLinkDetailDto.get(i).getServiceEndDate(), false, null, null,
              getMessage(BusStopsMessage.ERROR_END_DATE_BEFORE_START_DATE.getMessage())));
        }
      }

      // 利用期間チェック（系統との相関チェック）
      if (form.getDispMode().equals(DispMode.UPDATE_MODE) && ExObjectUtils.isNotBlankAll(
          busStopLinkDetailDto.get(i).getServiceStartDate(), busStopLinkDetailDto.get(i).getServiceEndDate())) {
        if (busStopSignpostsService.checkLineServicePeriod(form.getBusStopId(), busStopLinkDetailDto.get(i).getServiceStartDate(),
            busStopLinkDetailDto.get(i).getServiceEndDate())) {
          this.setErrorMsg(BusStopsMessage.ERROR_DATE_ROUTE.getMessage());
        }
      }
    }

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 詳細FORM
   */
  private void commonProcess(Model model, BusStopSignpostsDetailForm form) {
    // 停留所・標柱情報設定
    model.addAttribute("busStopSignpostsDetailForm", form);
  }
}
