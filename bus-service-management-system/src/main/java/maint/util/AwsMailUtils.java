package maint.util;

import java.lang.invoke.MethodHandles;
import javax.mail.internet.InternetAddress;

import maint.properties.AwsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 * AWS mailUtils(SES mail)
 *
 * @author sangsoo.lee
 */
@Component
public class AwsMailUtils {

  /** Aws Properties */
  private static AwsProperties awsProperties;

  /** Aws Properties 生成者 */
  @Autowired
  public AwsMailUtils(AwsProperties awsProperties) {
    AwsMailUtils.awsProperties = awsProperties;
  }

  /** Logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


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
        new InternetAddress(awsProperties.getFromEmail(), awsProperties.getFromName(),
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
              .withRegion(awsProperties.getSesRegion()).build();
      client.sendEmail(request);
    } catch (Exception e) {
      LOGGER.error("【AWS SEND MAIL ERROR】", e);
      throw e;
    }
  }
}
