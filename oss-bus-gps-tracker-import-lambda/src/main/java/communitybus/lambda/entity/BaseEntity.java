package communitybus.lambda.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Base Entity
 *
 * @author masaru.miyamoto
 *
 */
@Data
public class BaseEntity implements Serializable {

  /** Serial version UID */
  private static final long serialVersionUID = 8812429264740883146L;

  /** 作成者ID */
  protected String createdUser;

  /** 作成日時 */
  protected String createdAt;

  /** 更新者ID */
  protected String updatedUser;

  /** 更新日時 */
  protected String updatedAt;

}
