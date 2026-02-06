package maint.form.busStops;

import static maint.utils.date.ExDateUtils.getCurrentDate;

import jakarta.validation.constraints.*;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.busStops.BusStopDetailDto;
import maint.enums.DispMode;
import maint.enums.IndoorDivision;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 停留所詳細FORM
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusStopsDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 266994562369053L;

  /** PKID */
  private Long busStopId;

  /** 画面モード */
  private DispMode dispMode;

  /** 停留所コード */
  @NotBlank
  @IllegalCharacters
  @Size(max = 10)
  private String busStopCode;

  /** 利用期間_開始 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;

  /** 利用期間_終了 */
  @DateFormat("yyyy-MM-dd")
  private String serviceEndDate;

  /** 停留所名称 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 100)
  private String busStopName;

  /** 停留所名称 かな */
  @NotBlank
  @IllegalCharacters
  @Size(max = 100)
  private String busStopKanaName;

  /** 停留所名称 英語 */
  @IllegalCharacters
  @Size(max = 200)
  private String busStopEnName;

  /** 緯度,経度（世界測地） */
  private String latitudeLongitude;

  /** 停留所URL */
  @Size(max = 256)
  private String busStopUrl;

  /** 屋内バスターミナルフラグ */
  @NotNull
  @ValidEnumValue(enumClass = IndoorDivision.class)
  private Integer indoorBusTerminalFlag;

  /** 利用区分（0：利用不可、1：利用中） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /** 利用開始日変更可否フラグ（true:可, false:不可） */
  private boolean canServiceStartDateChangeFlag = true;

  // 緯度,経度フォーマットチェック
  @AssertTrue(message = "{validation.common.error.latitudeLongitude.format}")
  public boolean isValidateLatitudeLongitude() {
    if (ExObjectUtils.isBlank(this.latitudeLongitude)) {
      return true;
    }
    String regexp = "^-?(90|[1-8]?[0-9])(\\.[0-9]{1,12})?,-?(180|1[0-7][0-9]|[1-9]?[0-9])(\\.[0-9]{1,12})?$";
    return this.latitudeLongitude.matches(regexp);
  }

  // 緯度,経度必須チェック
  @AssertTrue(message = "{validation.busStop.error.latitudeLongitude.required}")
  public boolean isValidateLatitudeLongitudeRequired() {
    if (ExObjectUtils.isBlank(this.latitudeLongitude)) {
      return IndoorDivision.OUTDOOR.getValue().equals(this.indoorBusTerminalFlag);
    }
    return true;
  }

  /**
   * コンストラクタ（新規登録時）
   *
   * @param page ページ番号
   */
  public BusStopsDetailForm(Long page) {
    this.page = page;
    this.dispMode = DispMode.INSERT_MODE;
    this.serviceStartDate = getCurrentDate();
    this.serviceEndDate = "9999-12-31";
    this.availableFlag = UsageDivision.ACTIVE.getValue();
    this.indoorBusTerminalFlag = IndoorDivision.OUTDOOR.getValue();
  }

  /**
   * コンストラクタ
   *
   * @param dto            停留所情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(BusStopDetailDto dto, Long page, DispMode updateDispMode) {
    this.page = page;
    this.dispMode = updateDispMode;
    this.busStopId = dto.getBusStopId();
    this.busStopCode = dto.getBusStopCode();
    this.serviceStartDate = dto.getServiceStartDate();
    this.serviceEndDate = dto.getServiceEndDate();
    this.busStopName = dto.getBusStopName();
    this.busStopKanaName = dto.getBusStopKanaName();
    this.busStopEnName = dto.getBusStopEnName();
    this.latitudeLongitude = dto.getLatitudeLongitude();
    this.busStopUrl = dto.getBusStopUrl();
    this.availableFlag = dto.getAvailableFlag();
    this.indoorBusTerminalFlag = dto.getIndoorBusTerminalFlag();
    this.canServiceStartDateChangeFlag =
        this.dispMode.equals(DispMode.INSERT_MODE) || (this.dispMode.equals(DispMode.UPDATE_MODE)
            && ExDateUtils.compareLocalDate(getCurrentDate(), dto.getServiceStartDate()) <= 0);
  }

}
