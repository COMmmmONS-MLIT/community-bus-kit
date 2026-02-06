package communitybus.lambda.utils;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import communitybus.lambda.properties.AppProperties;

/**
 * AWS mailUtils(SES mail)
 * 
 * @author syuuichi.tsubakihara
 *
 */
public class AwsMailUtils {

  /** Logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * インスタンス生成
   *
   * @return メール発送UTIL
   */
  public static AwsMailUtils getInstance() {
    return new AwsMailUtils();
  }

  /**
   * AWSによるメール送信(テキスト文)
   *
   * @param mailAddresses メール
   * @param subject 件名
   * @param contents 本文
   * @throws Exception
   */
  public static void sendMailTextType(List<String> mailAddresses, String subject, String contents)
      throws Exception {

    InternetAddress address = new InternetAddress(AppProperties.AWS_FROM_EMAIL,
        AppProperties.AWS_FROM_NAME, "ISO-2022-JP");

    // HTML本文なしのメール
    sendMail(new SendEmailRequest()
        .withDestination(new Destination().withToAddresses(mailAddresses))
        .withMessage(new Message()
            .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(contents)))
            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
        .withSource(address.toString()));

    LOGGER.info("mailType:smtp {} : {}", mailAddresses, subject);
  }

  /**
   * 送信
   *
   * @param request 送信Request
   */
  private static void sendMail(SendEmailRequest request) {

    try {
      AmazonSimpleEmailService client = getAmazonSimpleEmailService();
      client.sendEmail(request);
    } catch (Exception e) {
      LOGGER.error("【AWS SEND MAIL ERROR】", e);
      throw e;
    }
  }

  /**
   * AmazonSimpleEmailService取得
   * 
   * @return AmazonSimpleEmailService
   */
  private static AmazonSimpleEmailService getAmazonSimpleEmailService() {
    
    Regions sesRegion = Regions.valueOf(AppProperties.SES_REGION.toUpperCase());
    
    if (!StringUtils.isBlank(AppProperties.ACTIVE_ENV)
        && "LOCAL".equals(AppProperties.ACTIVE_ENV)) {
      return AmazonSimpleEmailServiceClientBuilder.standard()
          .withCredentials(new ProfileCredentialsProvider(AppProperties.AWS_SES_PROFILE))
          .withRegion(sesRegion).build();
    }

    return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(sesRegion)
        .build();
  }

}
