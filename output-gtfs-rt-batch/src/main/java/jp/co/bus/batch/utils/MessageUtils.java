package jp.co.bus.batch.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.bus.batch.enums.SendMailTypeEnums;
import jp.co.bus.batch.properties.BatchProperties;

/**
 * Message Utility
 * 
 * @author g.ko
 *
 */
public class MessageUtils {

  /** log */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** バッチ設定 */
  private static final BatchProperties propertice = new BatchProperties();

  /**
   * 管理者へエラーメール送信
   * 
   * @param errMsg
   * @param exception
   */
  public static void sendErrorMailToAdmin(String errMsg, Exception exception) {
    try {
      // Set Contents
      StringBuffer contents = new StringBuffer();
      contents.append("発生時間 : " + CommonUtils.getSystemDateCurrentTimestamp() + "\n");
      contents.append("エラー : " + errMsg + "\n\n\n");
      String stackTrace = getStringByExceptionStackTrace(exception);
      if (Objects.nonNull(stackTrace))
        contents.append("Java Log :" + stackTrace);

      if (propertice.getPropertiesByKey("send.mail.type").equals(SendMailTypeEnums.SES.getType())) {
        SmtpMailUtils.getInstance().send(propertice.getPropertiesByKey("admin.mail.subject"),
            contents.toString(), propertice.getPropertiesByKey("admin.mail.address"));
      } else {
        SmtpMailUtils.getInstance().send(propertice.getPropertiesByKey("admin.mail.subject"),
            contents.toString(), propertice.getPropertiesByKey("admin.mail.address"));
      }

    } catch (Exception e) {
      LOGGER.error("Send mail exception : ", e);
    }
  }

  /**
   * Change to string by stack trace of exception
   * 
   * @param e Exception
   * @return stackTrace
   */
  private static String getStringByExceptionStackTrace(Exception e) {
    if (e == null)
      return null;
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }
}
