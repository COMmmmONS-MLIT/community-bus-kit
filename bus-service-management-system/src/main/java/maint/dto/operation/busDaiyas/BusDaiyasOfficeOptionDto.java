package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ登録ダウンロード用DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasOfficeOptionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111115L;

  /** 営業所ID */
  private String officeId;
  /** 営業所名 */
  private String officeName;

}
