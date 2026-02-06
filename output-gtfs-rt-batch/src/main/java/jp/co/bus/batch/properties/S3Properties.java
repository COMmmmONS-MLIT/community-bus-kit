package jp.co.bus.batch.properties;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jp.co.bus.batch.enums.S3PropertyKeys;
import lombok.NoArgsConstructor;

import java.util.ResourceBundle;

/**
 * S3 Properties
 * 
 * @author s-fujioka
 */
@NoArgsConstructor
public class S3Properties {

  /** デフォルトのプロパティファイル名(拡張子なし) */
  private static final String DEFAULT_FILE_NAME = "s3";

  /** Configuration */
  private ResourceBundle resources = ResourceBundle.getBundle(DEFAULT_FILE_NAME);

  /**
   * 与えられたプロパティファイル名のリソースバンドルを設定
   * 
   * @param fileName プロパティファイル名(拡張子なし)
   */
  public S3Properties(String fileName) {
    this.resources = ResourceBundle.getBundle(fileName);
  }

  /**
   * プロパティ値を取得
   * 
   * @param key
   * 
   * @return プロパティ値
   */
  public String getPropertiesByKey(S3PropertyKeys key) {
    return this.resources.getString(key.getKey());
  }

  /**
   * S3オブジェクトを取得
   * 
   * @return S3オブジェクト
   */
  public AmazonS3 getS3() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(
            new ProfileCredentialsProvider(getPropertiesByKey(S3PropertyKeys.CONFIGURE_NAME))
                .getCredentials()))
        .withRegion(getPropertiesByKey(S3PropertyKeys.REGION)).build();
  }

}
