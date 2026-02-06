package maint.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.dto.UserPermissionDto;
import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.dto.system.users.UserTwoFactorMailContentsDto;
import maint.entity.TwoFactorAuthHistoriesEntity;
import maint.entity.UsersEntity;
import maint.enums.CodeMasterEnums;
import maint.enums.MailDivisionEnums;
import maint.exception.AdminAuthException;
import maint.exception.CommonDbException;
import maint.mapper.AgenciesMapper;
import maint.mapper.PermissionRoleDetailsMapper;
import maint.mapper.TwoFactorAuthHistoriesMapper;
import maint.mapper.UsersMapper;
import maint.message.AdminAuthErrorMessage;
import maint.session.UserSession;
import maint.util.date.ExDateUtils;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ログインSERVICE
 *
 * @author administrator
 */
@Service
public class AuthService extends BaseService {

  /** 管理者MAPPER */
  @Autowired
  private UsersMapper usersMapper;

  /** 事業者MAPPER */
  @Autowired
  private AgenciesMapper agenciesMapper;

  /** パスワードUTILS */
  @Autowired
  private PasswordUtils passwordUtils;

  /** 権限詳細MAPPER */
  @Autowired
  private PermissionRoleDetailsMapper permissionRoleDetailsMapper;

  /** 管理者2要素認証履歴MAPPER */
  @Autowired
  private TwoFactorAuthHistoriesMapper twoFactorAuthHistoriesMapper;

  /** メール送信サービス */
  @Autowired
  private SendMailService sendMailService;

  /**
   * 管理者情報取得
   *
   * @param email メール
   * @return 管理者情報
   */
  public UsersEntity getUserInfo(String email, String loginPass) {
    // 管理者Entitiy取得
    UsersEntity usersEntity = usersMapper.getUserByLoginInfo(email);

    // 該当アカウントがない場合
    if (ExObjectUtils.isBlank(usersEntity)) {
      throw new AdminAuthException("Not Exist Account", AdminAuthErrorMessage.NOT_EXIST);
    }

    // パスワードチェック
    if (!passwordUtils.isMatchPassword(loginPass, usersEntity.getPassword())) {
      //パスワード間違いの場合、該当アカウントの認証失敗回数を更新し、処理を中断する
      usersMapper.updateAuthFailureCount(usersEntity);

      throw new AdminAuthException("Login Error", AdminAuthErrorMessage.LOGIN_FAIL);
    }
    // アカウントロックチェック
    if (usersMapper.isAccountLocked(usersEntity.getUserId())) {
      throw new AdminAuthException("Login Error", AdminAuthErrorMessage.LOGIN_FAIL);
    }

    //ログイン成功の場合、ユーザーマスタの更新を行う
    usersMapper.updateUserInfoOnLoginSuccess(usersEntity);


    return usersEntity;
  }

  /**
   * ユーザ権限リスト取得
   *
   * @param permRoleId 権限ロールID
   * @return ユーザ権限リスト
   */
  public List<UserPermissionDto> getPermissionList(Long permRoleId) {
    return permissionRoleDetailsMapper.getPermissionListByPk(permRoleId);
  }

  /**
   * 全事業者取得
   *
   * @return 事業者リスト
   */
  public List<AgencySelectDto> getAllAgency() {
    return agenciesMapper.getAllAgenciesForSelectOption();
  }

  /**
   * 事業者名取得
   *
   * @param businessCorpId 事業者ID
   * @return 事業者名
   */
  public String getBusinessCorpNameByBusinessCorpId(Long businessCorpId) {
    return agenciesMapper.getBusinessCorpNameByBusinessCorpId(businessCorpId);
  }

  /**
   * 2要素認証メール送信
   *
   * @param userSession UserSession
   * @param authCode    認証コード
   * @throws Exception
   */
  public void sendTwoFactorMail(UserSession userSession, String authCode) throws Exception {
    // コードマスタ.認証コード有効期限（分）
    Long limitTime = getCodeMasterByCodeMasterEnum(
        CodeMasterEnums.SYSTEM_AUTH_CODE_LIMIT_TIME).getCodeValueToLong();

    sendMailService.sendMailToUser(MailDivisionEnums.USERS_SEND_TWO_FACTOR,
        new UserTwoFactorMailContentsDto(userSession.getUserName(), authCode, limitTime.toString()),
        userSession.getEmail());
  }

  /**
   * 2要素認証が必要であるか
   *
   * @param userSession UserSession
   * @return TRUE：要、FALSE：不要
   */
  public boolean isNeedTwoFactorAuth(UserSession userSession) {
    // 2要素認証有効期限を取得
    Long limitDate = getCodeMasterByCodeMasterEnum(
        CodeMasterEnums.SYSTEM_TWO_FACTOR_AUTH_LIMIT_DATE).getCodeValueToLong();
    return userSession.isNeedTwoFactorAuth(limitDate);
  }


  /**
   * 指定された桁数のRandom英数字文字列を生成
   *
   * @param len 桁数
   * @return Random英数字文字列
   */
  public String createRandomAlphanumeric(int len) {
    Random random = new Random();
    List<String> list = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      int n = random.nextInt(35);
      if (n > 25)
        list.add(String.valueOf(n - 25));
      else
        list.add(String.valueOf((char) (n + 65)));
    }
    return String.join("", list);
  }

  /**
   * 管理者2要素認証履歴更新
   *
   * @param userSession UserSession
   * @param authCode    認証コード
   */
  public void updateTwoFactorHistory(UserSession userSession, String authCode) {
    // 履歴削除
    TwoFactorAuthHistoriesEntity entity = new TwoFactorAuthHistoriesEntity(userSession);
    twoFactorAuthHistoriesMapper.deleteTwoFactorAuthHistoryByUserId(entity);

    // 履歴登録
    entity.setAuthCode(authCode);
    twoFactorAuthHistoriesMapper.insertTwoFactorHistory(entity);
  }

  /**
   * 再送信処理
   *
   * @param userSession UserSession
   * @throws Exception
   */
  @Transactional
  public void resendAuthCode(UserSession userSession) throws Exception {
    // 新しい認証コードを生成
    String authCode = this.createRandomAlphanumeric(6);
    // DB更新
    updateTwoFactorHistory(userSession, authCode);
    // メール送信
    sendTwoFactorMail(userSession, authCode);
  }

  /**
   * 有効な認証コードであるか
   *
   * @param userId   ユーザID
   * @param authCode 認証コード
   * @return TRUE：有効、FALSE：無効
   */
  public boolean isValidAuthCode(Long userId, String authCode) {
    Long limitTime = getCodeMasterByCodeMasterEnum(
        CodeMasterEnums.SYSTEM_AUTH_CODE_LIMIT_TIME).getCodeValueToLong();

    return twoFactorAuthHistoriesMapper.countValidTwoFactorAuth(userId, authCode,
        ExDateUtils.addMinsToDateTime(ExDateUtils.getCurrentDateTime(),
            -1 * limitTime.intValue())) == 1;
  }

  /**
   * 2要素認証情報更新
   *
   * @param userSession UserSession
   * @param request     HttpServletRequest
   * @param response    HttpServletResponse
   */
  @Transactional
  public void updateUserTwoFactorAuthInfo(UserSession userSession, HttpServletRequest request,
      HttpServletResponse response) {

    if (usersMapper.updateUserTwoFactorAuthInfo(
        new UsersEntity(userSession.getUserId(), userSession)) != 1) {
      throw new CommonDbException("【2要素認証】2要素認証情報更新エラー");
    }
    // セッションの2要素認証済みフラグをTRUEに上書きする
    userSession.setAuthenticatedFlag(true);
    this.writeAutheticationUserSession(userSession, request, response);
  }
}
