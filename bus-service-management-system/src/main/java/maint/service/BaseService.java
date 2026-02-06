package maint.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.entity.CodeMasterEntity;
import maint.enums.CodeMasterEnums;
import maint.mapper.CodeMasterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import maint.session.UserSession;

/**
 * Base service class.
 *
 * @author administrator
 */
@Service
public class BaseService {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** メッセージ */
  @Autowired
  protected MessageSource messageSource;

  /** メッセージResource */
  protected static ResourceBundle resourceBundle =
      ResourceBundle.getBundle("message", Locale.ENGLISH);
  /** コードマスタMAPPER */
  @Autowired
  protected CodeMasterMapper codeMasterMapper;

  /**
   * get a message by property name.
   *
   * @param name
   * @return
   */
  public String getMessageByPropertyName(String name) {
    return resourceBundle.getString(name);
  }

  /**
   * メッセージ取得
   *
   * @param key メッセージKEY
   * @return メッセージ
   */
  protected String getMessage(String key) {
    return messageSource.getMessage(key, null, Locale.JAPAN);
  }

  /**
   * UserSession情報の取得
   *
   * @return UserSession
   */
  public UserSession getUserSession() {
    return (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  /**
   * セッション情報の上書き
   *
   * @param newUserSession 新セッション情報
   * @param request        HttpServletRequest
   * @param response       HttpServletResponse
   */
  public void writeAutheticationUserSession(UserSession newUserSession, HttpServletRequest request,
      HttpServletResponse response) {
    // 現在のセッション情報の取得
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // セッション情報の書き換え
    Authentication newAuth =
        new UsernamePasswordAuthenticationToken(newUserSession, auth.getCredentials(),
            auth.getAuthorities());

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(newAuth);
    SecurityContextHolder.setContext(context);

    // 認証情報を永続可する（SecutiryContextRepositoryに保存）
    // Spring Security 6.0以降からは明記する必要になった。
    SecurityContextRepository securityContextRepository =
        new HttpSessionSecurityContextRepository();
    securityContextRepository.saveContext(context, request, response);

  }

  /**
   * コードマスタ一覧取得
   *
   * @return コードマスタ一覧取得
   */
  private List<CodeMasterEntity> getCodeMasterIndex() {
    return codeMasterMapper.getCodeMasterIndex();
  }

  /**
   * コードマスタEnumからObject取得
   *
   * @param codeMasterEnum コードマスタENUM
   * @return コードマスタEntity
   */
  public CodeMasterEntity getCodeMasterByCodeMasterEnum(CodeMasterEnums codeMasterEnum) {
    return getCodeMasterIndex().stream().filter(
        e -> codeMasterEnum.getMainCode().equals(e.getMainCode()) && codeMasterEnum.getSubCode()
            .equals(e.getSubCode())).findFirst().orElse(null);
  }
}
