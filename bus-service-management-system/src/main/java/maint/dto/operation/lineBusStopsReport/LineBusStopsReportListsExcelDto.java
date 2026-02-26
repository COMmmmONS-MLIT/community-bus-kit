package maint.dto.operation.lineBusStopsReport;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.utils.ExRegUtils;

/**
 * 停留所情報インポート EXCEL Dto すべて文字列で保持し、バリデーションはアノテーションで実施する。
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineBusStopsReportListsExcelDto extends BaseDto {

  private static final long serialVersionUID = -5326657281948471768L;

  /** バリデーショングループ */
  public interface DefaultGroup {}
  public interface StopGroup {}
  public interface LatLon {}
  public interface SignpostGroup {}
  public interface AfterPattenGroup {}

  @GroupSequence({DefaultGroup.class, AfterPattenGroup.class})
  public interface DefaultGroupSequence {}

  @GroupSequence({StopGroup.class, AfterPattenGroup.class})
  public interface StopGroupSequence {}

  @GroupSequence({SignpostGroup.class, AfterPattenGroup.class})
  public interface SignpostGroupSequence {}

  public interface NotDefaultGroup {}

  /** 事業者ID（1～11桁の数値） */
  @NotBlank(message = "{validation.linebusstops.report.businesscorpid.notblank}", groups = DefaultGroup.class)
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linebusstops.report.businesscorpid.pattern}", groups = DefaultGroup.class)
  private String businessCorpId;

  /** 停留所コード（1～10文字） */
  @NotBlank(message = "{validation.linebusstops.report.busstopcode.notblank}", groups = DefaultGroup.class)
  @Size(max = 10, message = "{validation.linebusstops.report.busstopcode.size}", groups = DefaultGroup.class)
  private String busStopCode;

  /** 停留所・標柱区分（1桁、0：標柱、1：停留所） */
  @NotBlank(message = "{validation.linebusstops.report.busstoptype.notblank}", groups = DefaultGroup.class)
  @Pattern(regexp = ExRegUtils.zeroToOneOptionalReg, message = "{validation.linebusstops.report.busstoptype.pattern}", groups = DefaultGroup.class)
  private String busStopType;

  /** 停留所名称（1～100文字） ※停留所のみ */
  @NotBlank(message = "{validation.linebusstops.report.busstopname.notblank}", groups = StopGroup.class)
  @Size(min = 1, max = 100, message = "{validation.linebusstops.report.busstopname.size}", groups = StopGroup.class)
  private String busStopName;

  /** 停留所名称 かな（1～100文字） ※停留所のみ */
  @NotBlank(message = "{validation.linebusstops.report.busstopkananame.notblank}", groups = StopGroup.class)
  @Size(min = 1, max = 100, message = "{validation.linebusstops.report.busstopkananame.size}", groups = StopGroup.class)
  private String busStopKanaName;

  /** 停留所名称 英語（最大200文字） ※停留所のみ */
  @Size(max = 200, message = "{validation.linebusstops.report.busstopenname.size}", groups = StopGroup.class)
  private String busStopEnName;

  /** 停留所URL（最大100文字） ※停留所のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.busstopurl.size}", groups = StopGroup.class)
  private String busStopUrl;

  /** 屋内バスターミナルフラグ（0:屋外、1:屋内） ※停留所のみ */
  @NotBlank(message = "{validation.linebusstops.report.indoorbusterminalflag.notblank}", groups = StopGroup.class)
  @Pattern(regexp = ExRegUtils.zeroToOneOptionalReg, message = "{validation.linebusstops.report.indoorbusterminalflag.pattern}", groups = StopGroup.class)
  private String indoorBusTerminalFlag;

  /** 緯度経度（カンマ区切り、(-90~90, -180~180)） */
  @NotBlank(message = "{validation.linebusstops.report.latitudelongitude.notblank}", groups = DefaultGroup.class)
  @Pattern(regexp = ExRegUtils.latitudeLongitude, message = "{validation.linebusstops.report.latitudelongitude.pattern}", groups = DefaultGroup.class)
  private String latitudeLongitude;

  /** 標柱枝番（1～100文字） ※標柱のみ */
  @NotBlank(message = "{validation.linebusstops.report.signpostbranchnumber.notblank}", groups = SignpostGroup.class)
  @Size(min = 1, max = 100, message = "{validation.linebusstops.report.signpostbranchnumber.size}", groups = SignpostGroup.class)
  private String signpostBranchNumber;

  /** 標柱番号（最大100文字） ※標柱のみ（任意） */
  @Size(max = 100, message = "{validation.linebusstops.report.signpostcode.size}", groups = SignpostGroup.class)
  private String signpostCode;

  /** 標柱名称（1～100文字） ※標柱のみ */
  @NotBlank(message = "{validation.linebusstops.report.signpostname.notblank}", groups = SignpostGroup.class)
  @Size(min = 1, max = 100, message = "{validation.linebusstops.report.signpostname.size}", groups = SignpostGroup.class)
  private String signpostName;

  /** 標柱名称 かな（最大100文字） ※標柱のみ */
  @NotBlank(message = "{validation.linebusstops.report.signpostkananame.notblank}", groups = SignpostGroup.class)
  @Size(min = 1, max = 100, message = "{validation.linebusstops.report.signpostkananame.size}", groups = SignpostGroup.class)
  private String signpostKanaName;

  /** 標柱名称 英語（1～200文字） ※標柱のみ */
  @Size(max = 200, message = "{validation.linebusstops.report.signpostenname.size}", groups = SignpostGroup.class)
  private String signpostEnName;

  /** 標柱副名称（最大100文字） ※標柱のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.signpostsubname.size}", groups = SignpostGroup.class)
  private String signpostSubName;

  /** 標柱住所（最大100文字） ※標柱のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.address.size}", groups = SignpostGroup.class)
  private String address;

  /** 標柱地先名（最大100文字） ※標柱のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.placename.size}", groups = SignpostGroup.class)
  private String placeName;

  /** 標柱付加情報（最大100文字） ※標柱のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.signpostdesc.size}", groups = SignpostGroup.class)
  private String signpostDesc;

  /** 標柱URL（最大100文字） ※標柱のみ */
  @Size(max = 100, message = "{validation.linebusstops.report.signposturl.size}", groups = SignpostGroup.class)
  private String signpostUrl;

  /** のりば情報 platform_code（最大20文字） ※標柱のみ */
  @Size(max = 20, message = "{validation.linebusstops.report.platformcode.size}", groups = SignpostGroup.class)
  private String platformCode;

  /** 車いす利用区分（1桁、0:情報なし、1:1台可、2:不可） ※標柱のみ */
  @Pattern(regexp = ExRegUtils.zeroToTwoOptionalReg, message = "{validation.linebusstops.report.wheelchairaccessibletype.pattern}", groups = SignpostGroup.class)
  private String wheelchairAccessibleType;

  /** 到着判定緯度・経度（カンマ区切り、(-90~90, -180~180)）*/
  @Pattern(
      regexp = ExRegUtils.latitudeLongitudeWithBlankReg,
      message = "{validation.linebusstops.report.arrivalplace.pattern}",
      groups = SignpostGroup.class
  )
  private String arrivalPoint;

  /** 到着判定半径(正の整数) */
  @Pattern(
      regexp = ExRegUtils.optionalNumeric1to11Reg,
      message = "{validation.linebusstops.report.arrivalradius.pattern}",
      groups = SignpostGroup.class)
  private String arrivalPointRadius;

}

