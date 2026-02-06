package maint.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * 画像属性
 *
 * @author kang
 */
@Component
@ConfigurationProperties(prefix = "image")
@Data
public class ImageProperties implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = -5596980276151101704L;

  /** 画像ベースURL */
  private static String baseUrl;
  /** 画像バケット名 */
  private String bucketName;
  /** 画像パス */
  private String basePath;
  /** S3Certification Configure Name */
  private String s3ConfigureName;
  /** S3リージョン */
  private String s3Region;

  /**
   * BaseUrl Static化(Set)
   *
   * @param baseUrl BaseUrl
   */
  public void setBaseUrl(String baseUrl) {
    ImageProperties.baseUrl = baseUrl;
  }

  /**
   * Url全部取得
   *
   * @return FullBaseUrl
   */
  public String getFullBasUrl() {
    return baseUrl + basePath;
  }

}
