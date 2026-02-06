package maint.entity;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import maint.session.UserSession;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base Entity
 *
 * @author administrator
 */
@Data
public class BaseEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = -8022252665166783986L;

  /** Function */
  protected String function;

  /** UserSession */
  protected UserSession session;

  /**
   * コンストラクタ
   */
  public BaseEntity() {
    this.session =
        (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

  }

  /**
   * コンストラクタ
   */
  public BaseEntity(Long userId) {
    this.session = new UserSession();
    session.setUserId(userId);
  }

  /**
   * コンストラクタ
   *
   * @param function FUNCTION
   */
  public BaseEntity(String function) {
    this();
    this.function = function;
  }
}
