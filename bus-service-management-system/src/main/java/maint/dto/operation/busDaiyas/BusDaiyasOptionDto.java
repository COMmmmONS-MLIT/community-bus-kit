package maint.dto.operation.busDaiyas;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 便ダイヤ改正の選択肢DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasOptionDto {

  /** 便ダイヤID */
  private Long daiyaRevisionId;
  /** 便ダイヤ開始日 */
  private String serviceStartDate;
  /** 便ダイヤ終了日 */
  private String serviceEndDate;
  /** 便ダイヤ改正理由 */
  private String daiyaRevisionName;

  /**
   * 便ダイヤ改正Selectの表示用ラベルを取得する
   * @return 表示用ラベル
   */
  public String getText() {
    return String.format("%s～%s（%s）", serviceStartDate, serviceEndDate, daiyaRevisionName);
  }
}
