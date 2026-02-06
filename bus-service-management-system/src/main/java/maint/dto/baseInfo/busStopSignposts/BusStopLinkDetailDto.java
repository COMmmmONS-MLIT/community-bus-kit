package maint.dto.baseInfo.busStopSignposts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.enums.IndoorDivision;
import maint.enums.UsageDivision;
import maint.enums.WheelchairAccessibleDivision;
import maint.utils.ExRegUtils;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 停留所詳細情報DTO
 *
 * @author k.takeuchi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusStopLinkDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 6454184122600L;

  /** 利用開始日変更可否フラグ（true:可, false:不可） */
  private boolean canServiceStartDateChangeFlag;

  /** 画面上の表示番号 */
  private String busStopListNo;

  /** 停留所ID */
  private Long busStopId;

  /** DBアクセスモード (0:Insert, 1:Update, 2:Select) */
  private Integer modeFlag;

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

  /** 緯度/経度（世界測地） */
  @Pattern(
      regexp = ExRegUtils.latitudeLongitudeWithBlankReg,
      message = "{validation.common.error.latitudeLongitude.format}"
  )
  private String latitudeLongitude;

  /** 停留所URL */
  @IllegalCharacters
  @Size(max = 256)
  private String busStopUrl;

  /** 利用区分（0：利用不可、1：利用中） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /** 屋内バスターミナルフラグ */
  @NotNull
  @ValidEnumValue(enumClass = IndoorDivision.class)
  private Integer indoorBusTerminalFlag;

  /** 保持標柱情報リスト */
  @Valid
  private List<SignpostsLinkDetailDto> signpostsLinkDetailDtoList;

  /** 標柱リストのサイズ */
  private Integer signpostsSize;

  /**
   * 標柱の初期設定
   */
  public void newSignposts() {
    this.signpostsLinkDetailDtoList = new ArrayList<SignpostsLinkDetailDto>();
  }

  /**
   * 新規標柱の追加
   *
   * @param signpostsListNo   画面表示上の番号
   */
  public void addSignposts(String signpostsListNo) {
    if (this.signpostsLinkDetailDtoList == null) {
      this.newSignposts();
    }
    SignpostsLinkDetailDto signpostsLinkDetailDto = new SignpostsLinkDetailDto();
    signpostsLinkDetailDto.setPageListNo(signpostsListNo);
    signpostsLinkDetailDto.setAvailableFlag(UsageDivision.ACTIVE.getValue());
    signpostsLinkDetailDto.setWheelchairAccessibleType(WheelchairAccessibleDivision.NO_INFO.getValue());
    signpostsLinkDetailDto.setModeFlag(0);
    this.signpostsLinkDetailDtoList.add(signpostsLinkDetailDto);
  }

  /**
   * 標柱リストサイズの設定
   */
  public void setSignpostsSize() {
    this.setSignpostsSize(this.getSignpostsLinkDetailDtoList().size());
  }
}
