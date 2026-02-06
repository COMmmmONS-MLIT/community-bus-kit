package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.form.busDaiyas.BusDaiyasDetailForm;

/**
 * 便ダイヤ登録画面生成情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasDetailSearchParamDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111118L;

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
  /** 表示区分 */
  private Integer displayDivisionFlag;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 便ダイヤID(前画面から引継ぎ) */
  private Long daiyaRevisionId;
  /** 便ダイヤID(画面選択) */
  private Long revisionSelect;
  /** 系統停留所ID */
  private Integer lineRouteBusStopId;
  /** 標柱名 */
  private String signpostName;

  /**
   * コンストラクタ
   *
   * @param dto                便ダイヤ情報DTO
   * @param businessCorpId     事業者ID
   * @param operatingCompanyId 運行会社ID
   */
  public BusDaiyasDetailSearchParamDto(BusDaiyasDetailForm dto, Long businessCorpId,
      Long operatingCompanyId) {
    this.lineDetailServiceDivision = dto.getLineDetailServiceDivision();
    this.lineRouteId = dto.getLineRouteId();
    this.rosenId = dto.getRosenId();
    this.rosenName = dto.getRosenName();
    this.lineRouteName = dto.getLineRouteName();
    this.operatingCompanyId = operatingCompanyId;
    this.daiyaRevisionId = dto.getDaiyaRevisionId();
    this.revisionSelect = dto.getRevisionSelect();
    this.businessCorpId = businessCorpId;
  }
}
