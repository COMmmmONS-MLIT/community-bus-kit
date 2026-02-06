package jp.co.bus.batch.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import jp.co.bus.batch.enums.S3PropertyKeys;
import jp.co.bus.batch.properties.S3Properties;
import lombok.Getter;

import java.io.InputStream;

/**
 * Amazon S3 へのアップロード処理を提供するユーティリティクラス。
 *
 * @author jeon
 */
@Getter
public class S3Utils {

  /** シングルトンインスタンス */
  private static S3Utils INSTANCE;

  /** S3接続設定を保持するプロパティ */
  private S3Properties s3Properties = new S3Properties();

  /** Amazon S3 クライアント */
  private AmazonS3 s3 = s3Properties.getS3();

  /**
   * コンストラクタ。
   *
   * @param s3Properties S3 接続設定を保持するプロパティ
   */
  public S3Utils(S3Properties s3Properties) {
    this.s3Properties = s3Properties;
    this.s3 = s3Properties.getS3();
    INSTANCE = this;
  }

  /**
   * シングルトンインスタンスを取得する。
   *
   * @return S3Utils インスタンス
   */
  public static S3Utils getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new S3Utils(new S3Properties());
    }
    return INSTANCE;
  }

  /**
   * 設定ファイルからバケット名を取得する。
   *
   * @return バケット名
   */
  public String getBucketName() {
    return this.s3Properties.getPropertiesByKey(S3PropertyKeys.BUCKET_NAME);
  }

  /**
   * S3オブジェクトキーを生成する。
   *
   * @param parts キーを構成するパス要素
   * @return "/" で連結したキー文字列
   */
  public String createKey(String... parts) {
    return String.join("/", parts);
  }

  /**
   * S3へデータをアップロードする（static用）。
   *
   * @param key      アップロード先のS3キー
   * @param input    入力ストリーム
   * @param metadata メタデータ
   * @return アップロード結果
   */
  public static PutObjectResult put(String key, InputStream input, ObjectMetadata metadata) {
    return getInstance().putInternal(key, input, metadata);
  }

  /**
   * S3へデータをアップロードする内部処理。
   *
   * @param key      アップロード先キー
   * @param input    データストリーム
   * @param metadata メタデータ
   * @return アップロード結果
   */
  private PutObjectResult putInternal(String key, InputStream input, ObjectMetadata metadata) {
    PutObjectRequest putReq =
        new PutObjectRequest(getBucketName(), key, input, metadata);
    return this.s3.putObject(putReq);
  }

}

