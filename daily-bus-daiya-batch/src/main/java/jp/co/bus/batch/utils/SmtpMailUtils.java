package jp.co.bus.batch.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.bus.batch.properties.BatchProperties;

/**
 * メール発送UTIL
 *
 * @author jp.lee
 *
 */
public class SmtpMailUtils {

  /** Batch properties */
  private static final BatchProperties properties = new BatchProperties();

  /** logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  public static SmtpMailUtils getInstance() {
    return new SmtpMailUtils();
  }

  /**
   * メール送信
   * 
   * @param subject
   * @param contents
   * @param toMailAddress
   * @throws Exception
   */
  public void send(String subject, String contents, String toMailAddress) throws Exception {
    send(subject, contents, toMailAddress, null, null, null, null);
  }

  /**
   * メール送信
   * 
   * @param subject タイトル
   * @param contents メール内容
   * @param toMailAddress 宛先
   * @throws Exception
   */
  public void send(String subject, String contents, String toMailAddress, String ccMailAddress,
      String bccMailAddress, String attachMentFileName, String attachmentFileBase64)
      throws Exception {

    Properties props = new Properties();
    props.put("mail.smtp.host", properties.getPropertiesByKey("smtp.mail.host"));
    props.put("mail.smtp.port", properties.getPropertiesByKey("smtp.mail.port"));
    props.put("mail.smtp.auth", properties.getPropertiesByKey("smtp.mail.auth"));
    props.put("mail.smtp.starttls.enable", properties.getPropertiesByKey("smtp.mail.starttls"));
    props.put("mail.smtp.connectiontimeout",
        properties.getPropertiesByKey("smtp.mail.connectiontimeout"));
    props.put("mail.smtp.timeout", properties.getPropertiesByKey("smtp.mail.timeout"));
    props.put("mail.debug", properties.getPropertiesByKey("smtp.mail.debug"));

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(
            properties.getPropertiesByKey("smtp.mail.user"),
            properties.getPropertiesByKey("smtp.mail.pass"));
      }
    });

    try {
      MimeMessage message = new MimeMessage(session);

      // Set From:
      message.setFrom(new InternetAddress(properties.getPropertiesByKey("from.mail.address"),
          properties.getPropertiesByKey("from.mail.name")));
      // Set To:
      message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailAddress));
      if (StringUtils.isNoneBlank(ccMailAddress)) {
        message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailAddress));
      }
      if (StringUtils.isNoneBlank(bccMailAddress)) {
        message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccMailAddress));
      }
      message.setSubject(subject, properties.getPropertiesByKey("smtp.mail.char-set"));

      Multipart alternativePart =
          new MimeMultipart(StringUtils.isNotBlank(attachmentFileBase64) ? "mixed" : "alternative");
      // html mail
      MimeBodyPart textBodyPart = new MimeBodyPart();
      textBodyPart.setText(contents);
      textBodyPart.setHeader("Content-Type", "text/plain; charset=UTF-8");
      alternativePart.addBodyPart(textBodyPart);

      if (StringUtils.isNotBlank(attachmentFileBase64)) {
        // attachment
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.setText(attachmentFileBase64);
        attachment.setHeader("Content-Type",
            MimeUtility.encodeWord("application/zip; name=\"" + attachMentFileName + "\""));
        alternativePart.addBodyPart(attachment);
      }

      // set alternative
      message.setContent(alternativePart);
      Transport.send(message);
      LOGGER.info("mailType:smtp " + toMailAddress + " : " + subject);

    } catch (Exception e) {
      LOGGER.error("【SEND MAIL ERROR】", e);
      throw e;
    }
  }

  /**
   * Send mail not exception
   * 
   * @param subject
   * @param contents
   * @param toMailAddress
   */
  public void sendNotException(String subject, String contents, String toMailAddress) {
    try {
      send(subject, contents, toMailAddress);
    } catch (Exception e) {
      LOGGER.error("【SEND MAIL ERROR】", e);
    }
  }

  /**
   * Change to string by stack trace of exception
   * 
   * @param e Exception
   * @return stackTrace
   */
  public static String getStringByExceptionStackTrace(Exception e) {
    if (e == null)
      return null;
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }
}
