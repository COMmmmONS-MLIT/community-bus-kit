package maint.form.dailyBusDaiyas;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;

/**
 * 日毎便ダイヤ検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyasSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -7622555655717056036L;

  /** ダイヤ改正ID */
  private Long searchDaiyaRevisionId;
  /** 路線ID */
  private Long searchRosenId;
  /** ダイヤ改正名称 */
  private String searchDaiyaRevisionName;
  /** 日毎便ダイヤ開始日 */
  private String searchServiceStartDate;
  /** 日毎便ダイヤ終了日 */
  private String searchServiceEndDate;
  /** 到着時刻時刻 */
  @IllegalCharacters
  @Size(max = 4)
  private String searchBusStopArrivalTime;
  /** 始発出発時刻 */
  @IllegalCharacters
  @Size(max = 4)
  private String searchBusStopDepartureTime;
  /** 運休フラグ */
  private Integer searchSalesCancelFlg;
  /** 便ID */
  private Long searchBusServiceId;

}
