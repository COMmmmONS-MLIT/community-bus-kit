package maint.dto.selectBox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ改正日セット用DTO
 *
 * @author 林竜生
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class DaiyaRevisionDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -2765022517547082381L;

  /** 路線ID */
  private Long rosenId;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;
}
