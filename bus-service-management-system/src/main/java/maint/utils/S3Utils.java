package maint.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * S3に対する操作を定義
 *
 * @author masaya.tanaka
 */
public class S3Utils {

  /** Asia Pacific TOKYO */
  private static final String DEFAULT_REGION = "ap-northeast-1";

  /**
   * Amazon S3オブジェクト
   */
  private AmazonS3 s3;
  /**
   * 使用するbucket名
   */
  private String s3BucketName;

  /**
   * コンストラクタ
   *
   * @param credentials
   * @param s3BucketName
   */
  public S3Utils(AWSCredentials credentials, String s3BucketName) {
    super();

    this.s3 = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(DEFAULT_REGION)
        .build();
    this.s3BucketName = s3BucketName;
  }

  /**
   * コンストラクタ
   *
   * @param awsAccessKey
   * @param awsSecretKey
   * @param s3BucketName
   */
  public S3Utils(String awsAccessKey, String awsSecretKey, String s3BucketName) {
    this(new BasicAWSCredentials(awsAccessKey, awsSecretKey), s3BucketName);
  }

  /**
   * コンストラクタ
   *
   * @param s3BucketName
   * @see DefaultAWSCredentialsProviderChain
   */
  public S3Utils(String s3BucketName) {
    this(s3BucketName, DEFAULT_REGION);
  }

  /**
   * コンストラクタ
   *
   * @param s3BucketName
   * @param region
   */
  public S3Utils(String s3BucketName, String region) {
    this.s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
    this.s3BucketName = s3BucketName;
  }

  /**
   * @return
   */
  public AmazonS3 getAmazonS3() {
    return this.s3;
  }

  /**
   * @return
   */
  public String getBucketName() {
    return this.s3BucketName;
  }

  /**
   * key一覧を取得する
   *
   * @return
   */
  public List<String> list() {
    return list(new ListObjectsRequest().withBucketName(this.s3BucketName));
  }

  /**
   * key一覧を取得する(prefix指定)
   *
   * @param prefix
   * @return
   */
  public List<String> list(String prefix) {
    return list(new ListObjectsRequest().withBucketName(this.s3BucketName).withPrefix(prefix));
  }

  /**
   * key一覧を取得する
   *
   * @param listObjectsRequest
   * @return
   */
  public List<String> list(ListObjectsRequest listObjectsRequest) {

    List<String> keyList = new ArrayList<>();

    ObjectListing objectListing;
    do {
      objectListing = this.s3.listObjects(listObjectsRequest);
      for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        keyList.add(objectSummary.getKey());
      }
      listObjectsRequest.setMarker(objectListing.getNextMarker());
    } while (objectListing.isTruncated());

    return keyList;
  }

  /**
   * ファイルの内容を文字列で取得
   *
   * @param key
   * @return
   * @throws IOException
   */
  public String getText(String key) throws IOException {

    String text = null;

    try (S3Object object = this.s3.getObject(new GetObjectRequest(this.s3BucketName, key))) {
      text = this.getTextInputStream(object.getObjectContent());
    }

    return text;
  }

  /**
   * 指定したkeyのファイルをダウンロードし、そのFileオブジェクトを返す
   *
   * @param key
   * @param downloadFileName
   * @return
   * @throws IOException
   * @throws AmazonServiceException
   * @throws AmazonClientException
   * @throws InterruptedException
   */
  public File get(String key, String downloadFileName)
      throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {

    File downloadFile = new File(downloadFileName);
    File parentDir = new File(downloadFile.getParent());
    if (!parentDir.exists()) {
      parentDir.mkdirs();
    }

    try (S3Object object = this.s3.getObject(new GetObjectRequest(this.s3BucketName, key));
        OutputStream output = new FileOutputStream(downloadFileName)) {

      int c;
      while ((c = object.getObjectContent().read()) != -1) {
        output.write(c);
      }
    }

    return downloadFile;
  }


  /**
   * 指定したkeyのS3Objectを返す
   *
   * @param bucketName
   * @param key
   * @return S3Object
   */
  public S3Object getObject(String bucketName, String key) {
    return this.s3.getObject(bucketName, key);
  }

  /**
   * 指定したファイルをputする
   *
   * @param key  S3上の保存先キー
   * @param file アップロード対象の InputStream
   * @return
   */
  public PutObjectResult put(String key, File file) {
    return this.s3.putObject(new PutObjectRequest(this.s3BucketName, key, file));
  }


  /**
   * 指定したファイルを一般公開でPUTする。
   *
   * @param key S3上の保存先キー
   * @param input  アップロード対象の InputStream
   * @param metadata アップロード対象のメタ情報（Content-Type や Content-Length など）
   * @return
   */
  public PutObjectResult putCannedAclPublicRead(String key, InputStream input,
      ObjectMetadata metadata) {
    PutObjectRequest putReq = new PutObjectRequest(this.s3BucketName, key, input, metadata);
    putReq.setCannedAcl(CannedAccessControlList.PublicRead);
    return this.s3.putObject(putReq);
  }


  /**
   * 指定したストリームをputする
   *
   * @param key S3上の保存先キー
   * @param input アップロード対象の InputStream
   * @param metadata <p>
   *                 e.g. s3Manager.put("bucketName", "outputpath/yyymm/file.csv", inputStream,
   *                 metadata)
   *                 </p>
   * @return
   */
  public PutObjectResult put(String key, InputStream input, ObjectMetadata metadata) {
    return this.s3.putObject(s3BucketName, key, input, metadata);
  }

  /**
   * 指定したファイルをdeleteする(複数)
   *
   * @param keyList
   * @return
   */
  public DeleteObjectsResult delete(List<String> keyList) {

    if (keyList == null || keyList.size() == 0) {
      return null;
    }

    List<KeyVersion> keys = new ArrayList<>();
    for (String key : keyList) {
      KeyVersion kv = new KeyVersion(key);
      keys.add(kv);
    }

    DeleteObjectsRequest request = new DeleteObjectsRequest(this.s3BucketName);
    request.setKeys(keys);

    return this.s3.deleteObjects(request);
  }

  /**
   * 指定したファイルをdeleteする
   *
   * @param key
   * @return
   */
  public DeleteObjectsResult delete(String key) {
    String[] keyArray = {key};

    return this.delete(Arrays.asList(keyArray));
  }

  /**
   * 指定したファイルを削除する<br> DeleteObjectsRequestを使用しない実装のdelete
   *
   * @param key
   */
  public void deleteObject(String key) {
    this.s3.deleteObject(this.s3BucketName, key);
  }

  /**
   * @param input
   * @return
   * @throws IOException
   */
  private String getTextInputStream(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    String text = "";
    String line = "";

    while (true) {
      line = reader.readLine();
      if (line == null) {
        break;
      }
      text = text + line;
    }
    return text;
  }
}
