package maint.enums;

import lombok.Getter;
import maint.message.CommonMessage;

/**
 * 画面モード
 *
 * @author administrator
 */
@Getter
public enum DispMode implements ValidateEnumsInterFace {

  /** 登録 */
  INSERT_MODE("登録", "0") {
    @Override
    public CommonMessage getCommonSuccessMessage() {
      return CommonMessage.SUCCESS_REGISTER;
    }
  }
  /** 変更 */
  , UPDATE_MODE("変更", "1") {
    @Override
    public CommonMessage getCommonSuccessMessage() {
      return CommonMessage.SUCCESS_UPDATE;
    }
  }
  /** 削除 */
  , DELETE_MODE("削除", "2") {
    @Override
    public CommonMessage getCommonSuccessMessage() {
      return CommonMessage.SUCCESS_DELETE;
    }
  }
  /** 確認 */
  , CONTENT_MODE("", "3") {
    @Override
    public CommonMessage getCommonSuccessMessage() {
      return null;
    }
  };

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  DispMode(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /** 名称 */
  private final String text;

  /** 値 */
  private final String value;

  /**
   * 共通成功メッセージ取得
   *
   * @return 共通成功メッセージ
   */
  public abstract CommonMessage getCommonSuccessMessage();

}
