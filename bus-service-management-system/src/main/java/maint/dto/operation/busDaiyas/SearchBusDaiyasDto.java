// Java
package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.busDaiyas.BusDaiyasDetailForm;
import maint.form.busDaiyas.BusDaiyasSearchForm;

/**
 * 便ダイヤ検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchBusDaiyasDto extends BaseDto {

  private static final long serialVersionUID = 6082984423495392597L;

  /** 便ダイヤID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 便ダイヤ開始日 */
  private String serviceStartDate;
  /** 便ダイヤ終了日 */
  private String serviceEndDate;
  /** 路線ID  */
  private Long rosenId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 系統ID */
  private Long lineRouteId;
  /** 往復区分 */
  private Integer lineDetailServiceDivision;

  /**
   * コンストラクタ
   *
   * @param form       便ダイヤ検索FORM（条件入力元）
   * @param startIndex 行の開始インデックス（0始まり、ページング用）
   */
  public SearchBusDaiyasDto(BusDaiyasSearchForm form, Long startIndex) {
    // ページング開始位置を設定
    this.startIndex = startIndex;

    if (form == null) {
      return;
    }

    this.daiyaRevisionName = form.getSearchDaiyaRevisionName();
    this.serviceStartDate = form.getSearchServiceStartDate();
    this.serviceEndDate = form.getSearchServiceEndDate();
    this.lineDetailServiceDivision = form.getSearchLineDetailServiceDivisionFlg();
    this.rosenId = form.getSearchRosenId();
  }

  /**
   * コンストラクタ
   *
   * @param form       便ダイヤ詳細FORM（条件入力元）
   * @param startIndex 行の開始インデックス（0始まり、ページング用）
   */
  public SearchBusDaiyasDto(BusDaiyasDetailForm form, long startIndex) {
    // ページング開始位置を設定
    this.startIndex = startIndex;

    if (form == null) {
      return;
    }

    this.daiyaRevisionName = form.getDaiyaRevisionName();
    this.lineRouteId = form.getLineRouteId();
    this.rosenId = form.getRosenId();
    this.rosenName = form.getRosenName();
    this.daiyaRevisionId = form.getDaiyaRevisionId();
    this.serviceEndDate = form.getServiceEndDate();
    this.serviceStartDate = form.getServiceStartDate();
    this.rosenId = form.getRosenId();
    this.lineDetailServiceDivision = form.getLineDetailServiceDivision();
    this.businessCorpId = form.getBusinessCorpId();
    this.lineRouteId = form.getLineRouteId();
  }
}
