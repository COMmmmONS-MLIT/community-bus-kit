package maint.dto.baseInfo.agencies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 事業者DTO（SELECT用）
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class AgencySelectDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 9589367821882L;

  /** 事業者ID */
  private Long businessCorpId;

  /** 事業者名 */
  private String name;

}
