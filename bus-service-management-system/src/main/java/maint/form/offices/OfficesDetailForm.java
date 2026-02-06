package maint.form.offices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.offices.OfficesDetailDto;
import maint.enums.DispMode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 営業所管理詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OfficesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 4672122680137766127L;

  /** 営業所ID */
  private Long officeId;
  /** 運行会社ID */
  @NotNull
  private Long operatingCompanyId;
  /** 運行会社名 */
  private String operatingName;
  /** 営業所名 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 100)
  private String name;
  /** 営業所URL */
  @Size(max = 256)
  private String officeUrl;
  /** 営業所電話番号 */
  @Size(max = 15)
  @Pattern(regexp = ExRegUtils.phoneNumber, message = "{validation.common.error.tel}")
  private String phoneNumber;
  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;
  /** 画面モード */
  private DispMode dispMode;

  /**
   * コンストラクタ
   *
   * @param dto            営業所情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(OfficesDetailDto dto, Long page, DispMode updateDispMode) {
    this.officeId = dto.getOfficeId();
    this.operatingCompanyId = dto.getOperatingCompanyId();
    this.operatingName = dto.getOperatingName();
    this.name = dto.getName();
    this.availableFlag = dto.getAvailableFlag();
    this.officeUrl = dto.getOfficeUrl();
    this.phoneNumber = dto.getPhoneNumber();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
