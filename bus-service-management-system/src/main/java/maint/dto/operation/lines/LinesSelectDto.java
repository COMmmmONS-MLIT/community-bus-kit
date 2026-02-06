package maint.dto.operation.lines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線DTO（SELECT用）
 *
 * @author jeon
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class LinesSelectDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -4932910309536103092L;

  /** 路線ID */
  private Long rosenId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 路線名 */
  private String rosenName;

}
