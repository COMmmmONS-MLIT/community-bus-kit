package maint.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * GoogleMapsAPI Properties
 *
 * @author jeon
 */
@Component
@ConfigurationProperties(prefix = "google.maps.api")
public class GoogleMapsProperties {

  /** Googleマップキー */
  private String key = "NOT_SET";

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
