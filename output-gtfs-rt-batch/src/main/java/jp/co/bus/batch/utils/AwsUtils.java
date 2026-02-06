package jp.co.bus.batch.utils;

import java.lang.invoke.MethodHandles;
import javax.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.bus.batch.properties.BatchProperties;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 * AWS UTIL
 *
 * @author ryuichi.kashiwai
 *
 */
public class AwsUtils {

  /** Batch properties */
  private static final BatchProperties properties = new BatchProperties();

  /** logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static AwsUtils getInstance() {
    return new AwsUtils();
  }

  /**
   * AWSによるメール送信(テキスト文)
   *
   * @param mailAddress メール
   * @param subject 件名
   * @param contents 本文
   * @throws Exception
   */
  public void sendMailTextType(String subject, String contents,String mailAddress)
      throws Exception {

    InternetAddress address =
        new InternetAddress(properties.getPropertiesByKey("from.mail.address"),
            properties.getPropertiesByKey("from.mail.name"), "ISO-2022-JP");

    // HTML本文なしのメール
    sendMail(new SendEmailRequest().withDestination(new Destination().withToAddresses(mailAddress))
        .withMessage(new Message()
            .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(contents)))
            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
        .withSource(address.toString()));

    LOGGER.info("mailType:SES " + mailAddress + " : " + subject);
  }

  /**
   * AWSによるメール送信(HTML文/テキスト文)
   *
   * @param mailAddress メール
   * @param subject 件名
   * @param contents 本文
   * @param htmlContents HTML本文
   * @throws Exception
   */
  public void sendMailHtmlType(String mailAddress, String subject, String contents,
      String htmlContents) throws Exception {

    InternetAddress address =
        new InternetAddress(properties.getPropertiesByKey("from.mail.address"),
            properties.getPropertiesByKey("from.mail.name"), "ISO-2022-JP");

    // HTML本文ありのメール
    sendMail(new SendEmailRequest().withDestination(new Destination().withToAddresses(mailAddress))
        .withMessage(new Message()
            .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlContents))
                .withText(new Content().withCharset("UTF-8").withData(contents)))
            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
        .withSource(address.toString()));

    LOGGER.info("mailType:SES " + mailAddress + " : " + subject);
  }

  /**
   * 送信
   *
   * @param request 送信Request
   */
  private void sendMail(SendEmailRequest request) {

    AWSCredentialsProvider provider =
        new ProfileCredentialsProvider(properties.getPropertiesByKey("ses.profile.name"));
    try {
      AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
          .withCredentials(provider).withRegion(Regions.AP_NORTHEAST_1).build();

      client.sendEmail(request);
    } catch (Exception e) {
      LOGGER.error("【AWS SEND MAIL ERROR】", e);
      throw e;
    }
  }
}
