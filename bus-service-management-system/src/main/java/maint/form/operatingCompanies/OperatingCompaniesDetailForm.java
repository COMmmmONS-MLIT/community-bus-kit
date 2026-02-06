package maint.form.operatingCompanies;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.operatingCompanies.OperatingCompaniesDetailDto;
import maint.enums.DispMode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;
/**
 * 運行会社詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OperatingCompaniesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 4672122680137766127L;

  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 事業者 */
  private String businessCorpName;
  /** 運行会社名 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 100)
  private String operatingName;
  /** 電話番号 */
  @Pattern(regexp = ExRegUtils.phoneNumber, message = "{validation.common.error.tel}")
  @Size(max = 15)
  private String phoneNumber;
  /** 連絡先メール */
  @Email
  @Size(max = 256)
  private String mailAddress;
  /** URL */
  @Size(max = 256)
  private String url;
  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;
  /** 画面モード */
  private DispMode dispMode;

  /**
   * コンストラクタ
   *
   * @param dto           運行会社情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(OperatingCompaniesDetailDto dto, Long page, DispMode updateDispMode) {
    this.operatingCompanyId = dto.getOperatingCompanyId();
    this.businessCorpName = dto.getBusinessCorpName();
    this.operatingName = dto.getOperatingName();
    this.phoneNumber = dto.getPhoneNumber();
    this.mailAddress = dto.getMailAddress();
    this.url = dto.getUrl();
    this.availableFlag = dto.getAvailableFlag();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
