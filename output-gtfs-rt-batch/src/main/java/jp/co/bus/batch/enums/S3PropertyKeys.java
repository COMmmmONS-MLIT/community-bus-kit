package jp.co.bus.batch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * S3 Property Keys
 * 
 * @author s-fujioka
 */
@Getter
@AllArgsConstructor
public enum S3PropertyKeys {

  /** base.url */
  BASE_URL("base.url"),
  /** bucket.name */
  BUCKET_NAME("bucket.name"),
  /** configure.name */
  CONFIGURE_NAME("configure.name"),
  /** region */
  REGION("region");

  /** key */
  private String key;

}
