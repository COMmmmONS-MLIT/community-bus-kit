package maint.provider;

import maint.enums.AccountDivision;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import maint.dto.UserPermissionDto;
import maint.entity.UsersEntity;
import maint.exception.AdminAuthException;
import maint.message.AdminAuthErrorMessage;
import maint.service.AuthService;
import maint.session.UserSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Securityの認証時に呼ばれる拡張Provider
 *
 * @author administrator
 */
@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

  /** ロガー */
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

  /** ログインSERVICE */
  @Autowired
  private AuthService authService;

  /** ログイン及びSession作成 */
  @Override
  public Authentication authenticate(Authentication auth) {
    String email = (String) auth.getPrincipal();
    String loginPass = (String) auth.getCredentials();

    // 入力チェック
    if (StringUtils.isBlank(email) || StringUtils.isBlank(loginPass)) {
      throw new AdminAuthException("Input Error", AdminAuthErrorMessage.BLANK_INPUT);
    }

    // 管理者Entity
    UsersEntity usersEntity;
    List<UserPermissionDto> userPermissionDto;
    String businessCorpName;

    // 管理者情報取得
    try {
      // 管理者情報取得
      usersEntity = authService.getUserInfo(email, loginPass);
      // 権限取得
      userPermissionDto = authService.getPermissionList(usersEntity.getPermRoleId());
      // 事業者名・自治体名
      businessCorpName = authService.getBusinessCorpNameByBusinessCorpId(usersEntity.getBusinessCorpId());
    } catch (AdminAuthException e) {
      LOGGER.error(e.getMessage());
      throw e;
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new AdminAuthException("System Exception", AdminAuthErrorMessage.OTHER);
    }

    // ログインユーザ情報をセッション情報として設定
    UserSession userSession = new UserSession(usersEntity, userPermissionDto, businessCorpName);
    // 管理者の場合のみ、事業者選択の為の事業者リストをセッションに保管
    if (AccountDivision.SYSTEM_ADMIN.getValue().equals(userSession.getAccountDivision()))
      userSession.setAgencies(authService.getAllAgency());
    Collection<GrantedAuthority> authorityList = new ArrayList<>();
    return new UsernamePasswordAuthenticationToken(userSession, auth.getCredentials(),
        authorityList);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }

}
