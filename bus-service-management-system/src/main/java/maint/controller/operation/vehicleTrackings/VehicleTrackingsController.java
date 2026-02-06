package maint.controller.operation.vehicleTrackings;

import maint.controller.BaseController;
import maint.dto.operation.vehicleTrackings.VehicleTrackingLocationDto;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.vehicleTrackings.VehicleTrackingsForm;
import maint.message.CommonMessage;
import maint.service.planDaiya.VehiclesService;
import maint.service.operation.VehicleTrackingsService;
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
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 車両動態CONTROLLER
 *
 * @author g.ko
 */

@Controller
@Validated
@RequestMapping("/operation/vehicleTrackings")
@SessionAttributes("vehicleTrackingsForm")
public class VehicleTrackingsController extends BaseController {

  /** 車両動態画面パス */
  private static final String INDEX_PAGE = "operation/vehicleTrackings/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.VEHICLE_MOVEMENT;

  /** 車両動態SERVICE */
  @Autowired
  VehicleTrackingsService vehicleTrackingsService;

  /** 車両SERVICE */
  @Autowired
  VehiclesService vehiclesService;

  /**
   * 車両動態FORM設定
   *
   * @return 車両動態FORM
   */
  @ModelAttribute("vehicleTrackingsForm")
  public VehicleTrackingsForm setVehicleTrackingsForm() {
    return new VehicleTrackingsForm();
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
   * 車両動態画面
   *
   * @param model Model
   * @param form 車両動態FORM
   * @param initFlag 検索フォーム初期化フラグ
   * @return 車両動態画面
   */
  @GetMapping("/")
  public String index(Model model, VehicleTrackingsForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new VehicleTrackingsForm();
    }
    commonProcess(model, form);
    return INDEX_PAGE;
  }

  /**
   * 車両動態検索
   *
   * @param model Model
   * @param form 車両動態FORM
   * @param bindingResult BindingResult
   * @return 車両動態画面
   */
  @PostMapping("/")
  public String search(Model model,
      @Validated @ModelAttribute("vehicleTrackingsForm") VehicleTrackingsForm form,
      BindingResult bindingResult) {
    hasAccessPermission(NOW_MENU, false, false);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      commonProcess(model, form);
      return INDEX_PAGE;
    }

    try {
      // 車両動態情報取得
      VehicleTrackingLocationDto dto =
          vehicleTrackingsService.getVehicleTracking(form.getVehicleId());

      // データが存在しない場合
      if (ExObjectUtils.isBlank(dto)) {
        setErrorMsg(CommonMessage.ERROR_GET_CORRECT_DATA);
        commonProcess(model, form);
        return INDEX_PAGE;
      }

      // 車両動態情報設定
      model.addAttribute("vehicleTrackingInfo", dto);

    } catch (Exception e) {
      LOGGER.error("【車両動態管理】車両動態情報取得失敗", e);
      setErrorMsg(CommonMessage.ERROR_GET_DATA);
      commonProcess(model, form);
      return INDEX_PAGE;
    }

    commonProcess(model, form);

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 車両動態FORM
   */
  private void commonProcess(Model model, VehicleTrackingsForm form) {
    // 車両情報取得（SELECT用）
    model.addAttribute("vehicleList", vehiclesService.getVehicleOptions());
    model.addAttribute("vehicleTrackingsForm", form);
  }
}
