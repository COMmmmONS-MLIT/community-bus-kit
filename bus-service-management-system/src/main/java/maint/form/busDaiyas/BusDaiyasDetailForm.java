package maint.form.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.busDaiyas.BusDaiyasDetailDto;
import maint.dto.operation.busDaiyas.BusDaiyasRegistedDto;
import maint.enums.DispMode;
import maint.form.BaseForm;

import java.util.List;

/**
 * 便ダイヤ詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -832346452393980836L;

  /** 系統ID */
  private Long lineRouteId;
  /** 路線ID */
  private Long rosenId;
  /** 路線名 */
  private String rosenName;
  /** 系統名称 */
  private String lineRouteName;
  /** 往路区分 */
  private Integer lineDetailServiceDivision;
  /** 便ダイヤ開始日 */
  private String serviceStartDate;
  /** 便ダイヤ終了日 */
  private String serviceEndDate;
  /** ダイヤ改正理由 */
  private String daiyaRevisionName;
  /** 表示区分 */
  private Integer displayDivisionFlag;
  /** 事業者ID */
  private Long businessCorpId;
  /** 便ダイヤID */
  private Long daiyaRevisionId;
  /** 系統停留所ID */
  private Integer lineRouteBusStopId;
  /** 標柱名 */
  private String signpostName;
  /** 便ダイヤ改正ID（運行期間Select） */
  private Long revisionSelect;

  /** 便ダイヤ 表示登録用リスト */
  private List<BusDaiyasRegistedDto> busDaiyasRegistedList;

  /**
   * コンストラクタ
   *
   * @param dto            便ダイヤ情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(BusDaiyasDetailDto dto, Long page, DispMode updateDispMode) {
    this.daiyaRevisionName = dto.getDaiyaRevisionName();
    this.serviceStartDate = dto.getServiceStartDate();
    this.serviceEndDate = dto.getServiceEndDate();
    this.lineDetailServiceDivision = dto.getLineDetailServiceDivision();
    this.lineRouteId = dto.getLineRouteId();
    this.rosenId = dto.getRosenId();
    this.rosenName = dto.getRosenName();
    this.lineRouteName = dto.getLineRouteName();
    this.page = page;
    this.dispMode = updateDispMode;
  }

}
