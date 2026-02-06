package maint.form.vehicles;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 車両検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VehiclesSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -2868048012172853674L;

  /** 車両ID */
  private Long searchVehicleId;
  /** 運行会社ID */
  private Long searchOperatingCompanyId;
  /** 車両名 */
  @IllegalCharacters
  @Size(max = 100)
  private String searchVehicleName;
  /** GPSトラッカーシリアル */
  private String searchGpsSerial;
  /** 利用区分（検索用） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer searchAvailableFlag;
  /** ユーザーID */
  private Long userId;

}
