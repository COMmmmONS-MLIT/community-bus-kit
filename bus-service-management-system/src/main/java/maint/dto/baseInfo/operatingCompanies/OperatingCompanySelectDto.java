package maint.dto.baseInfo.operatingCompanies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行会社DTO（SELECT用）
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class OperatingCompanySelectDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 53800218622L;

  /** 運行会社ID */
  private Long operatingCompanyId;

  /** 運行会社名 */
  private String operatingName;

  /** 事業者ID */
  private Long businessCorpId;

}
