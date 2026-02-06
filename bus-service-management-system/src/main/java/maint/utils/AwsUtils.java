package maint.utils;

import java.lang.invoke.MethodHandles;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import jakarta.annotation.PostConstruct;
import maint.properties.AwsProperties;

/**
 * AWS UTIL
 *
 * @author administrator
 */
@Component
public class AwsUtils {

  /** awsProperties */
  static AwsProperties awsProperties;
  /** wiredAwsProperties */
  @Autowired
  private AwsProperties wiredAwsProperties;
  /** logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * AWSUTILの初期設定
   */
  @PostConstruct
  private void init() {
    AwsUtils.awsProperties = wiredAwsProperties;
  }

  /**
   * AWSによるメール送信(テキスト文)
   *
   * @param mailAddress メール
   * @param subject     件名
   * @param contents    本文
   * @throws Exception
   */
  public static void sendMailTextType(String mailAddress, String subject, String contents)
      throws Exception {

    InternetAddress address =
        new InternetAddress(awsProperties.getAdminEmail(), awsProperties.getFromName(),
            "ISO-2022-JP");

    // HTML本文なしのメール
    sendMail(new SendEmailRequest().withDestination(new Destination().withToAddresses(mailAddress))
        .withMessage(new Message().withBody(
                new Body().withText(new Content().withCharset("UTF-8").withData(contents)))
            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
        .withSource(address.toString()));

    LOGGER.info("mailType:smtp " + mailAddress + " : " + subject);
  }

  /**
   * 送信
   *
   * @param request 送信Request
   */
  private static void sendMail(SendEmailRequest request) {

    AWSCredentialsProvider provider = new ProfileCredentialsProvider(awsProperties.getSesProfile());
    try {
      AmazonSimpleEmailService client =
          AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(provider)
              .withRegion(Regions.AP_NORTHEAST_1).build();

      client.sendEmail(request);
    } catch (Exception e) {
      LOGGER.error("【AWS SEND MAIL ERROR】", e);
      throw e;
    }
  }
}
