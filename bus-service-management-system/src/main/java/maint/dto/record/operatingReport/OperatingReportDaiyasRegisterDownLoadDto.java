package maint.dto.record.operatingReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行回数出力ダウンロード用DTO
 *
 * @author JEKAL.HUN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OperatingReportDaiyasRegisterDownLoadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 2572143723305543357L;

  /** 系統コード */
  private String lineRouteCode;
  /** 系統名称 */
  private String lineRouteName;
  /** 運行回数(運休) */
  private String salesCancelFlg1Count;
  /** 運行回数(運行) */
  private String salesCancelFlg0Count;
}
