package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ変更画面の系統selectBox情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasLineRouteOptionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111112L;

  /** 系統ID */
  private String lineRouteId;
  /** 系統名 */
  private String lineRouteName;

}
