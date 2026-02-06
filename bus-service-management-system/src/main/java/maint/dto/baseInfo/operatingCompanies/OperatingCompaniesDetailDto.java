package maint.dto.baseInfo.operatingCompanies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 運行会社情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OperatingCompaniesDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -1582356852203907207L;

  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 事業者ID */
  private String businessCorpName;
  /** 運行会社名 */
  private String operatingName;
  /** 電話番号 */
  private String phoneNumber;
  /** 連絡先メール */
  private String mailAddress;
  /** URL */
  private String url;
  /** 利用区分 */
  private Integer availableFlag;
}
