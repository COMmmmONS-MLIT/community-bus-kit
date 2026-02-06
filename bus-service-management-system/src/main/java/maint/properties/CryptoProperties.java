package maint.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 暗号化 Properties
 * 
 * @author sangsoo.lee
 * 
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "crypto")
public class CryptoProperties {

  /** Algorithm */
  private String algorithm;

}
