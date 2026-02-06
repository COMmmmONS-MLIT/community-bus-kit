package maint.service;

import maint.properties.AwsProperties;
import maint.utils.BaseMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import maint.enums.MailDivisionEnums;
import maint.util.AwsMailUtils;

/**
 * メール送信SERVICE
 * 
 * @author sangsoo.lee
 * 
 */
@Service
public class SendMailService extends BaseService {

  /** AwsProperties */
  @Autowired
  private AwsProperties awsProperties;

  /** ベースメールUTILS */
  @Autowired
  private BaseMailUtils baseMailUtils;

  /**
   * 管理者へメール送信
   *
   * @param mailDiv メール区分Enums
   * @param dto メール本文DTO
   * @param mailAddress メール
   * @throws Exception
   */
  public void sendMailToUser(MailDivisionEnums mailDiv, Object dto, String mailAddress)
      throws Exception {
    try {
       AwsMailUtils.sendMailTextType(mailAddress, mailDiv.getSubject(),
          baseMailUtils.createContents(mailDiv.getFtlFileName(), dto));
    } catch (Exception e) {
      if (MailDivisionEnums.USERS_SEND_TWO_FACTOR.equals(mailDiv)) {
        sendErrorToSystemAdmin(mailDiv.getSubject(), mailAddress);
      }
      throw e;
    }
  }

  /**
   * システム管理者へエラーメール送信
   * 
   * @param errMsgName エラーメッセージ名
   * @param errAddr エラー対象メール
   */
  public void sendErrorToSystemAdmin(String errMsgName, String errAddr) {
    try {
      String subject = getErrorMailSubject(errMsgName);
      String content = getErrorMailContents(errMsgName, errAddr);
      AwsMailUtils.sendMailTextType(awsProperties.getSystemAdminEmail(), subject, content);
    } catch (Exception e) {
      LOGGER.error("Send mail exception : ", e);
    }
  }

  /**
   * エラーメールタイトル作成
   * 
   * @param errMsgName エラーメッセージ名
   * @return エラーメールタイトル
   */
  private String getErrorMailSubject(String errMsgName) {
    return String.format("%s送信に失敗しました。", errMsgName);
  }

  /**
   * エラーメール本文作成
   * 
   * @param errMsgName エラーメッセージ名
   * @param errAddr エラー対象メール
   * @return エラーメール本文
   */
  private String getErrorMailContents(String errMsgName, String errAddr) {
    // エラーメール本文
    String contents = """
        下記管理者への「%s」送信に失敗しました。
        ログインID：%s
        """.formatted(errMsgName, errAddr);

    return contents;
  }
}
