package maint.form.signposts;

import jakarta.validation.constraints.*;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.signposts.SignpostDetailDto;
import maint.enums.DispMode;
import maint.enums.UsageDivision;
import maint.enums.WheelchairAccessibleDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 標柱詳細FORM
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignpostsDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = -5629435777783L;

  /** PKID */
  private Long signpostId;

  /** 停留所ID */
  @NotNull
  private Long busStopId;

  /** 標柱枝番 */
  @IllegalCharacters
  @NotBlank
  @Size(max = 100)
  private String signpostBranchNumber;

  /** 標柱番号 */
  @IllegalCharacters
  @Size(max = 100)
  private String signpostBranchCode;

  /** 標柱名称 */
  @IllegalCharacters
  @NotBlank
  @Size(max = 100)
  private String signpostName;

  /** 標柱名称 かな */
  @IllegalCharacters
  @NotBlank
  @Size(max = 100)
  private String signpostKanaName;

  /** 標柱名称 英語 */
  @IllegalCharacters
  @Size(max = 200)
  private String signpostEnName;

  /** 標柱副名称 */
  @Pattern(regexp = "^$|^.{1,100}$", message = "{validation.signpost.subname.pattern}")
  private String signpostSubName;

  /** 標柱住所 */
  @IllegalCharacters
  @Size(max = 100)
  private String address;

  /** 標柱地先名 */
  @IllegalCharacters
  @Size(max = 100)
  private String placeName;

  /** 標柱付加情報 */
  @IllegalCharacters
  @Size(max = 100)
  private String signpostDesc;

  /** 緯度/経度（世界測地） */
  @Pattern(
      regexp = "^-?(90|[1-8]?[0-9])(\\.[0-9]{1,12})?,-?(180|1[0-7][0-9]|[1-9]?[0-9])(\\.[0-9]{1,12})?$",
      message = "{validation.common.error.latitudeLongitude.format}"
  )
  private String latitudeLongitude;

  /** 標柱URL */
  @IllegalCharacters
  @Size(max = 256)
  private String signpostUrl;

  /** のりば情報 */
  @IllegalCharacters
  @Size(max = 20)
  private String platformCode;

  /** 車いす利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = WheelchairAccessibleDivision.class)
  private Integer wheelchairAccessibleType;

  /** 到着判定緯度、経度 */
  @Pattern(
      regexp = "^$|^-?(90|[1-8]?[0-9])(\\.[0-9]{1,12})?,-?(180|1[0-7][0-9]|[1-9]?[0-9])(\\.[0-9]{1,12})?$",
      message = "{validation.common.error.latitudeLongitude.format}"
  )
  private String arrivalPoint;

  /** 到着判定半径 */
  @Pattern(regexp = "^$|\\d+", message = "{validation.onlyNumber.invalid}")
  @Size(max = 9)
  private String arrivalPointRadius;

  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /**
   * コンストラクタ（新規登録時）
   *
   * @param page ページ番号
   */
  public SignpostsDetailForm(Long page) {
    this.page = page;
    this.dispMode = DispMode.INSERT_MODE;
    this.availableFlag = UsageDivision.ACTIVE.getValue();
    this.wheelchairAccessibleType = WheelchairAccessibleDivision.NO_INFO.getValue();
  }

  /**
   * コンストラクタ
   *
   * @param dto ユーザ情報DTO
   * @param page ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(SignpostDetailDto dto, Long page, DispMode updateDispMode) {
    this.signpostId =  dto.getSignpostId();
    this.busStopId =  dto.getBusStopId();
    this.signpostBranchNumber = dto.getSignpostBranchNumber();
    this.signpostBranchCode = dto.getSignpostCode();
    this.signpostName = dto.getSignpostName();
    this.signpostKanaName = dto.getSignpostKanaName();
    this.signpostEnName = dto.getSignpostEnName();
    this.address = dto.getAddress();
    this.placeName = dto.getPlaceName();
    this.signpostDesc = dto.getSignpostDesc();
    this.signpostSubName = dto.getSignpostSubName();
    this.latitudeLongitude = dto.getLatitudeLongitude();
    this.signpostUrl = dto.getSignpostUrl();
    this.platformCode = dto.getPlatformCode();
    this.availableFlag = dto.getAvailableFlag();
    this.wheelchairAccessibleType = dto.getWheelchairAccessibleType();
    this.arrivalPoint = dto.getArrivalPoint();
    this.arrivalPointRadius = dto.getArrivalPointRadius() == null ? "" : dto.getArrivalPointRadius().toString();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
