package maint.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * ユーザ権限DTO
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPermissionDto implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 8405466599324523258L;

  /** メニューコード */
  private String menuCode;
  /** メニュー表示順 */
  private int displayOrder;
  /** 親メニューコード */
  private String parentMenuCode;
  /** 親メニュー表示順 */
  private int parentDisplayOrder;
  /** 書込権限フラグ */
  private int writePermFlg;
  /** 帳票DL権限フラグ */
  private int downloadPermFlg;
  /** 参照権限フラグ */
  private int readPermFlg;

  /**
   * 書き込み権限があるか？
   *
   * @return チェック結果
   */
  public boolean hasWritePerm() {
    return writePermFlg == 1;
  }

  /**
   * 帳票DL権限があるか？
   *
   * @return チェック結果
   */
  public boolean hasDownloadPerm() {
    return downloadPermFlg == 1;
  }

}
