package maint.dto.baseInfo.offices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 営業所情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OfficesDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 5944907093598497085L;

  /** 営業所ID */
  private Long officeId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 運行会社名 */
  private String operatingName;
  /** 営業所名 */
  private String name;
  /** 営業所URL */
  private String officeUrl;
  /** 営業所電話番号 */
  private String phoneNumber;
  /** 利用区分 */
  private Integer availableFlag;

}
