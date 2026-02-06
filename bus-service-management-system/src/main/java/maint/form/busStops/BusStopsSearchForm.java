package maint.form.busStops;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 停留所検索FORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusStopsSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 84365619799330L;

  /** 停留所名（検索用） */
  @IllegalCharacters
  @Size(max = 100)
  private String busStopName;

  /** 停留所コード（検索用） */
  @IllegalCharacters
  @Size(max = 10)
  private String busStopCode;

  /** 利用期間_開始（検索用） */
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;

  /** 利用期間_終了（検索用） */
  @DateFormat("yyyy-MM-dd")
  private String serviceEndDate;

  /** 利用可否（検索用） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer searchUsageDivision;

  /** 事業者名 */
  private String businessCorpName;

  /**
   * コンストラクタ
   */
  public BusStopsSearchForm() {
    this.searchUsageDivision = UsageDivision.ACTIVE.getValue();
  }

}
