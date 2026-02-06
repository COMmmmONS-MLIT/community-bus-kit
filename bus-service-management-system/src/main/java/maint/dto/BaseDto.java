package maint.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import maint.constants.CommConstant;
import maint.utils.ExObjectUtils;

/**
 * DTO基底クラス
 *
 * @author administrator
 */
@Data
public class BaseDto implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = -1308302469809989920L;

  /** 取得開始位置(0始まり) */
  protected Long startIndex;

  /** 表示件数 */
  protected Long displayNum = CommConstant.PAGER_DISPLAY_MAX_ROW;

  /** メッセージ */
  protected List<String> message;

  /**
   * 取得開始位置から取得開始行数を取得
   *
   * @return 取得開始行数
   */
  public Long getStartRowNum() {
    return startIndex;
  }

  /**
   * 前方一致検索用文字列生成
   *
   * @return 前方一致検索用文字列
   */
  public String makeForwardMatchString(String searchText) {
    return ExObjectUtils.isNotBlank(searchText) ?
        searchText.replaceAll("[\\\\|%|_]", "\\\\$0") + "%" :
        "";
  }

  /**
   * 部分一致検索用文字列生成
   *
   * @return 部分一致検索用文字列
   */
  public String makeMiddleMatchString(String searchText) {
    return ExObjectUtils.isNotBlank(searchText) ?
        "%" + searchText.replaceAll("[\\\\|%|_]", "\\\\$0") + "%" :
        "";
  }

}
