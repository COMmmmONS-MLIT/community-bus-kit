package maint.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * AWS Properties
 *
 * @author administrator
 */
@Component
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {

  /** SESプロファイル */
  private String sesProfile;
  /** 送信者名 */
  private String fromName;
  /** システムメールの送信元アドレス */
  private String fromEmail;
  /** SESリージョン指定 */
  private String sesRegion;
  /** システム管理者メール */
  private String systemAdminEmail;
  /** 管理画面URL */
  private String maintUrl;
  /** 送信者のメールアドレス */
  private String adminEmail;

}
