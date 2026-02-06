package maint.form.vehicleTrackings;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.BaseForm;

/**
 * 車両動態FORM
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleTrackingsForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 2354236426L;

  /** 車両ID */
  @NotNull
  private Long vehicleId;

}
