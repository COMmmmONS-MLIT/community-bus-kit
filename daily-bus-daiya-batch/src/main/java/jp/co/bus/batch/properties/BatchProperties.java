package jp.co.bus.batch.properties;

import java.util.ResourceBundle;

/**
 * Batch Properties
 * 
 * @author hr.kim
 *
 */
public class BatchProperties {

  /** File Name */
  private static final String FILE_NAME = "batch";
  /** Configuration */
  private ResourceBundle resources;

  /** constructor */
  public BatchProperties() {
    this.resources = ResourceBundle.getBundle(FILE_NAME);
  }

  public String getPropertiesByKey(String key) {
    return this.resources.getString(key);
  }

}
