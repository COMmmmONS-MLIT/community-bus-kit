package jp.co.bus.batch.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.transit.realtime.GtfsRealtime;
import jp.co.bus.batch.properties.S3Properties;
import jp.co.bus.batch.utils.S3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * GTFS-RT形式のフィードデータを S3 にアップロードするサービスクラス。
 *
 * @author jeon
 */
public class S3UploadService {

  /** アップロード処理のログ出力に使用するロガー */
  private static final Logger LOGGER = LoggerFactory.getLogger(S3UploadService.class);

  /** S3 接続処理を行うユーティリティクラス */
  private final S3Utils s3Utils;

  /**
   * コンストラクタ。 S3Properties の設定値を読み込み、S3Utils を初期化する。
   */
  public S3UploadService() {
    this.s3Utils = new S3Utils(new S3Properties());
  }

  /**
   * GTFS-RT の FeedMessage を S3 にアップロードするメソッド。
   *
   * @param feed   アップロード対象のFeedMessage
   * @param prefix S3 での保存先キー
   */
  public void uploadFeed(GtfsRealtime.FeedMessage feed, String prefix) {
    try {
      // Feed → byte[]
      byte[] bytes = feed.toByteArray();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

      // メタデータ設定
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(bytes.length);
      metadata.setContentType("application/x-protobuf");

      // インスタンスメソッドで S3 に PUT
      PutObjectResult putResult = s3Utils.put(prefix, inputStream, metadata);

      LOGGER.info("S3アップロード成功: {}", putResult);

    } catch (Exception e) {
      LOGGER.error("S3アップロード中にエラー発生", e);
    }
  }
}
