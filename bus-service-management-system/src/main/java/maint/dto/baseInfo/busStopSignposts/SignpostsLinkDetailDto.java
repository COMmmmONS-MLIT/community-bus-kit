package maint.dto.baseInfo.busStopSignposts;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.enums.UsageDivision;
import maint.enums.WheelchairAccessibleDivision;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 標柱詳細情報DTO
 *
 * @author k.takeuchi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignpostsLinkDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 4573888826002L;

  /** 利用開始日変更可否フラグ（true:可, false:不可） */
  private boolean canServiceStartDateChangeFlag;

  /** 標柱ID */
  private Long signpostId;

  /** 停留所ID */
  private Long busStopId;

  /** 画面上の表示番号 */
  private String pageListNo;

  /** DBアクセスモード (0:Insert, 1:Update, 2:Select) */
  private Integer modeFlag;

  /** 標柱枝番 */
  @IllegalCharacters
  @NotBlank
  @Size(max = 100)
  private String signpostBranchNumber;

  /** 標柱番号 */
  @IllegalCharacters
  @Size(max = 100)
  private String signpostCode;

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
  @IllegalCharacters
  @Size(max = 100)
  private String signpostSubName;

  /** 標柱住所 */
  @IllegalCharacters
  @Size(max = 100)
  private String address;

  /** 標柱地先名 */
  @IllegalCharacters
  @Size(max = 100)
  private String placeName;

  /** 緯度/経度（世界測地） */
  @NotBlank
  @Pattern(
      regexp = ExRegUtils.latitudeLongitudeWithBlankReg,
      message = "{validation.common.error.latitudeLongitude.format}"
  )
  private String latitudeLongitude;

  /** 標柱付加情報 */
  @IllegalCharacters
  @Size(max = 100)
  private String signpostDesc;

  /** 標柱URL */
  @IllegalCharacters
  @Size(max = 256)
  private String signpostUrl;

  /** 停留所・標柱区分（0：標柱、1:停留所） */
  private Integer busStopType;

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
      regexp = ExRegUtils.latitudeLongitudeWithBlankReg,
      message = "{validation.common.error.latitudeLongitude.format}"
  )
  private String arrivalPoint;

  /** 到着判定半径(DBアクセス用) */
  private Integer arrivalPointRadius;

  /** 到着判定半径(画面表示用) */
  @Pattern(regexp = "^$|\\d+", message = "{validation.onlyNumber.invalid}")
  @Size(max = 5)
  private String arrivalPointRadiusStr;

  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

}
