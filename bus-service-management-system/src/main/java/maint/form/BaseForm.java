package maint.form;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.enums.DispMode;
import maint.exception.BadRequestException;
import maint.exception.DoubleTabException;
import maint.utils.ExObjectUtils;

/**
 * 共通フォーム
 *
 * @author administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseForm implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 2756907963858736917L;

  /** 削除フラグ */
  protected Long delFlag;

  /** 登録日 */
  protected String regDate;

  /** 登録機能 */
  protected String regFunc;

  /** 登録者ID */
  protected Long regUserId;

  /** 変更日 */
  protected String updDate;

  /** 変更機能 */
  protected String updFunc;

  /** 変更者ID */
  protected Long updUserId;

  /** ページ */
  protected Long page = 1L;

  /** 画面モード */
  protected DispMode dispMode;

  /** Session Key */
  protected String sessionKey;

  /** Original Session Key */
  protected String originSessionKey;

  /**
   * コンストラクタ
   *
   * @param sessionKey セッションキー
   */
  public BaseForm(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  /**
   * コンストラクタ
   *
   * @param dispMode 画面モード
   */
  public BaseForm(DispMode dispMode) {
    this.dispMode = dispMode;
  }


  /**
   * SessionKey 不具合チェック
   */
  public void isInvalidSession() {
    // セッションキーが存在しない場合、400エラーにする
    if (StringUtils.isBlank(this.sessionKey)) {
      throw new BadRequestException();
    }
    if (!this.sessionKey.equals(this.originSessionKey)) {
      throw new DoubleTabException();
    }
  }

  /**
   * 既存情報設定
   *
   * @param orgForm 既存FORM
   */
  public void setOriginFormData(BaseForm orgForm) {
    setSessionKey(orgForm.getSessionKey());
    if (ExObjectUtils.isNotBlank(orgForm.getDispMode())) {
      setDispMode(orgForm.getDispMode());
    }
  }
}
