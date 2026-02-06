package maint.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * FileProperties
 *
 * @author kang
 */
@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {

  /** cryptoKey */
  private String cryptoKey;

  /** algorithm */
  private String algorithm;

  /** Root Directory */
  private static String rootDir;

  /** Temp Directory */
  private static String tempDir;

  /** 表面ファイル名 */
  private String surfaceFileName;

  /** 裏面ファイル名 */
  private String reverseFileName;

  /** 裏面（2枚目）ファイル名 */
  private String reverseOtherFileName;

  /**
   * Root Dir Static化(Get)
   *
   * @return rootDir
   */
  public static String getRootDir() {
    return rootDir;
  }

  /**
   * Temp Dir Static化(Get)
   *
   * @return tempDir
   */
  public static String getTempDir() {
    return tempDir;
  }

  /**
   * Temp Dir Static化(Set)
   *
   * @param rootDir rootDir
   */
  public void setRootDir(String rootDir) {
    FileProperties.rootDir = rootDir;
  }

  /**
   * Temp Dir Static化(Set)
   *
   * @param tempDir tempDir
   */
  public void setTempDir(String tempDir) {
    FileProperties.tempDir = tempDir;
  }
}
