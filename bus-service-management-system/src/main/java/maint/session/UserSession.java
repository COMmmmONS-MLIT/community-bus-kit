package maint.session;

import java.io.Serializable;
import java.util.List;

import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.enums.AccountDivision;
import maint.util.date.ExDateFormat;
import maint.util.date.ExDateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.dto.UserPermissionDto;
import maint.entity.UsersEntity;
import maint.enums.Menu;
import maint.utils.ExObjectUtils;

/**
 * UserSession
 *
 * @author masaya.tanaka
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
@NoArgsConstructor
public class UserSession implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 1072877065903535101L;

  /** ユーザID */
  private Long userId;
  /** メール */
  private String email;
  /** ユーザ名 */
  private String userName;
  /** パスワード変更済みフラグ */
  private Integer passwordChangedFlag;
  /** アカウント区分 */
  private String accountDivision;
  /** 事業者ID */
  private Long businessCorpId;
  /** 事業者名・自治体名 */
  private String businessCorpName;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 権限ID */
  private Long permRoleId;
  /** 権限リスト */
  private List<UserPermissionDto> userPermList;
  /** 事業者リストSELECT用 */
  private List<AgencySelectDto> agencies;
  /** 事業者選択画面遷移用メニュー */
  private Menu menu;
  /** 最終ログイン日時 */
  private String lastLoginDatetime;
  /** 2要素認証日時 */
  private String twoFactorAuthDatetime;
  /** 2要素認証済みフラグ */
  private boolean authenticatedFlag;


  /**
   * コンストラクタ
   *
   * @param usersEntity       管理者Entity
   * @param userPermissionDto ユーザ権限DTOリスト
   * @param businessCorpName 事業者名・自治体名
   */
  public UserSession(UsersEntity usersEntity, List<UserPermissionDto> userPermissionDto, String businessCorpName) {
    this.userId = usersEntity.getUserId();
    this.email = usersEntity.getEmail();
    this.userName = usersEntity.getLastName() + " " + usersEntity.getFirstName();
    this.permRoleId = usersEntity.getPermRoleId();
    this.passwordChangedFlag = usersEntity.getPasswordChangedFlag();
    this.userPermList = userPermissionDto;
    this.accountDivision = usersEntity.getAccountDivision();
    this.businessCorpId = usersEntity.getBusinessCorpId();
    this.businessCorpName = businessCorpName;
    this.operatingCompanyId = usersEntity.getOperatingCompanyId();
    this.twoFactorAuthDatetime = usersEntity.getTwoFactorAuthDatetime();
  }

  /**
   * 該当親メニューのサブメニューに権限をもっているかどうか
   *
   * @param topMenu 親メニュー
   * @return サブメニューに対する権限がある場合は true、そうでない場合は false
   */
  public boolean hasSubMenuPerm(Menu topMenu) {
    for (UserPermissionDto dto : userPermList) {
      Menu menu = Menu.getByMenuCode(dto.getMenuCode());
      if (ExObjectUtils.isNotBlank(menu) && menu.getParentsMenu() == topMenu)
        return true;
    }
    return false;
  }

  /**
   * 対象子メニューの権限をもっているかどうか
   *
   * @param subMenu 子メニュー
   * @return 子メニューに対する権限がある場合は true、そうでない場合は false
   */
  public boolean hasPerm(Menu subMenu) {
    for (UserPermissionDto dto : userPermList) {
      if (Menu.getByMenuCode(dto.getMenuCode()) == subMenu)
        return true;
    }
    return false;
  }

  /**
   * ダウンロード権限をもっているかどうか
   *
   * @param menu メニュー
   * @return true: ダウンロード権限あり, false: ダウンロード権限なし
   */
  public boolean hasDownloadPerm(Menu menu) {
    return userPermList.stream().filter(u -> u.getMenuCode().equals(menu.getMenuCode()))
        .anyMatch(u -> u.hasDownloadPerm());
  }


  /**
   * 共通ヘッダの表示情報 ユーザID＋所属
   *
   * @return ユーザID＋所属
   */
  public String getComHdDispStr() {
    return "ユーザID：" + this.userId;
  }

  /**
   * 共通ヘッダの表示情報 ユーザID＋所属のmax-widthを計算
   *
   * @return ユーザID＋所属のmax-width
   */
  public String getComHdMaxWidth() {
    // 900px - (ユーザ名の文字数 × 12px + 40px(アイコン))
    return (900 - (("ようこそ " + this.userName + " さん").length() * 12 + 40)) + "px";
  }

  /**
   * システム管理者であるか
   *
   * @return true:システム管理者 false:愛業者
   */
  public boolean isSystemAdmin() {
    return AccountDivision.SYSTEM_ADMIN.getValue().equals(this.accountDivision);
  }

  public String getTopUrlByMenu() {
	  return ExObjectUtils.isNotBlank(menu) ? menu.getUri() : "/";
  }

  /**
   * 2要素認証が必要かどうか
   *
   * @param limitDate 2要素認証日時
   * @return TRUE：必要、FALSE：不要
   */
  public boolean isNeedTwoFactorAuth(Long limitDate) {
    if (ExObjectUtils.isBlank(this.twoFactorAuthDatetime)) {
      return true;
    }
    // 2要素認証日時 <= システム日付 ー コードマスタ.2要素認証有効期限（※計算時に時分秒は切り捨て）
    String twoFactorAuthDate =
        ExDateUtils.convertDateTimeFormat(this.twoFactorAuthDatetime, ExDateFormat.uuuuMMdd_HYPEN);
    String compareDate =
        ExDateUtils.minusDaysToDate(ExDateUtils.getCurrentDate(), limitDate.intValue());
    return ExDateUtils.compareLocalDate(twoFactorAuthDate, compareDate) <= 0;
  }
}
