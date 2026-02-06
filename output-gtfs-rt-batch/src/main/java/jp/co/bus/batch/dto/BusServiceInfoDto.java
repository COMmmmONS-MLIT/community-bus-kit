package jp.co.bus.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 便情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusServiceInfoDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -508947238005432L;

  /** 事業者ID */
  private Long businessCorpId;
}
