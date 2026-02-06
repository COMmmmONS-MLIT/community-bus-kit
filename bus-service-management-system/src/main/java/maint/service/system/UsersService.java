package maint.service.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.dto.ListPager;
import maint.dto.system.users.SearchUsersDto;
import maint.dto.system.users.UsersDetailDto;
import maint.entity.UsersEntity;
import maint.enums.PwdChangedFlag;
import maint.exception.CommonDbException;
import maint.form.users.UsersDetailForm;
import maint.form.users.UsersSearchForm;
import maint.mapper.UsersMapper;
import maint.service.BaseService;
import maint.session.UserSession;
import maint.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ユーザSERVICE
 *
 * @author ryuichi.kashiwai
 */
@Service
public class UsersService extends BaseService {

  /** パスワードUTIL */
  @Autowired
  PasswordUtils passwordUtil;

  /** ユーザ MAPPER */
  @Autowired
  UsersMapper usersMapper;

  /**
   * ユーザ一覧取得
   *
   * @param form  検索FORM
   * @param pager ListPager
   */
  public void getUsers(UsersSearchForm form, ListPager pager) {
    SearchUsersDto dto = new SearchUsersDto(form, pager.getStartIndex(), getUserSession());
    pager.setTotalListSize(usersMapper.countUsersBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(usersMapper.getUsersBySearch(dto));
    }
  }

  /**
   * ユーザ情報取得
   *
   * @param id PK
   * @return ユーザ情報
   */
  public UsersDetailDto getUserByPk(Long id) {
    return usersMapper.getUserByPk(id);
  }

  /**
   * ユーザ件数取得
   *
   * @param pk    PK
   * @param email ログインID
   * @return ユーザ件数
   */
  public boolean countUsers(Long pk, String email) {
    return usersMapper.countUsersByEmail(pk, email) > 0;
  }

  /**
   * ユーザ登録
   *
   * @param form 詳細FORM
   * @return ユーザID
   */
  @Transactional
  public Long insert(UsersDetailForm form) {
    // ユーザ登録
    UsersEntity usersEntity = new UsersEntity(form);

    // 登録失敗時、エラーとする
    if (usersMapper.registerUsers(usersEntity) != 1) {
      throw new CommonDbException("【アカウント管理】登録エラー");
    }

    return usersEntity.getUserId();
  }

  /**
   * ユーザ変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(UsersDetailForm form) {
    // ユーザー変更
    UsersEntity usersEntity = new UsersEntity(form);

    if (usersMapper.updateUsers(usersEntity) != 1) {
      throw new CommonDbException("【アカウント管理】変更エラー");
    }
  }

  /**
   * パスワード形式チェック
   *
   * @param pass  パスワード文字列
   * @param email ログイン
   * @return true:正常、false:エラー
   */
  public boolean isValidPasswordForm(String pass, String email) {
    return PasswordUtils.isStrongPassword(pass) && !PasswordUtils.equalsToEmail(pass, email);
  }

  /**
   * 現在のパスワードがDBと一致するかチェック
   *
   * @param currentPass 現在のパスワード
   * @return True：一致、False：不一致
   */
  public boolean validationCurrentPassword(String currentPass) {
    return passwordUtil.isMatchPassword(currentPass,
        usersMapper.getUserPasswordByPK(getUserSession().getUserId()));
  }

  /**
   * ユーザーパスワード変更処理
   *
   * @param newPass  新パスワード
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   */
  @Transactional
  public void changePassword(String newPass, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // ユーザーの変更
    UserSession userSession = getUserSession();
    UsersEntity usersEntity = new UsersEntity();
    usersEntity.setSession(userSession);
    usersEntity.setUserId(userSession.getUserId());
    usersEntity.setPassword(PasswordUtils.getBCryptStr(newPass));

    if (usersMapper.updateUserPassword(usersEntity) != 1) {
      throw new CommonDbException("【ユーザ管理】パスワード変更エラー");
    }
    // セッション情報の変更
    userSession.setPasswordChangedFlag(PwdChangedFlag.CHANGED.getValue());
    // セッション情報の変更
    writeAutheticationUserSession(userSession, request, response);
  }

}
