package maint.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * メール属性
 *
 * @author administrator
 */
@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {

  /** System管理者メール */
  private String systemMailSubject;
  /** Systemメール件名 */
  private String systemAdminEmail;

}
