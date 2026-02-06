package maint.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import maint.constants.CommConstant;
import maint.utils.ExNumberUtils;

/**
 * ListPager
 *
 * @author administrator
 */
@Data
public class ListPager implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 1L;

  /** 現在のページ番号 */
  private Long currentPageNo = 1L;

  /** 1画面の最大表示件数 */
  private Long displayMaxRow = CommConstant.PAGER_DISPLAY_MAX_ROW;

  /** 全体のデータ件数 */
  private Long totalListSize = 0L;

  /** 表示するデータ */
  private List<?> list = Collections.EMPTY_LIST;

  /** 表示頁数 */
  private Long displayPageWidth = CommConstant.PAGER_DISPLAY_PAGE_WIDTH;

  /**
   * 表示する行の開始インデックスを取得する
   *
   * @return 行の開始インデックス
   */
  public Long getStartIndex() {
    return (currentPageNo - 1) * displayMaxRow;
  }

  /**
   * 最後ページ値取得
   *
   * @return 最後ページ値
   */
  public Long getMaxPage() {
    return ((totalListSize - 1) / displayMaxRow) + 1;
  }

  /**
   * 表示開始頁を取得する
   *
   * @return 表示開始頁
   */
  public Long getDisplayMinPage() {

    Long tmpCurrentPage = currentPageNo;
    Long minPage = 1L;
    Long maxPage = getMaxPage();

    // 不正な値の場合の調整
    if (currentPageNo > maxPage) {
      tmpCurrentPage = maxPage;
    } else if (currentPageNo < minPage) {
      tmpCurrentPage = minPage;
    }

    Long tmpMinPage = tmpCurrentPage - displayPageWidth;
    Long tmpMaxPage = tmpCurrentPage + displayPageWidth;

    if (tmpMaxPage > maxPage) {
      tmpMinPage = tmpMinPage - (tmpMaxPage - maxPage);
    }

    // 最小値を下回る場合は最小値にする
    if (tmpMinPage < minPage) {
      tmpMinPage = minPage;
    }

    return tmpMinPage;

  }

  /**
   * 表示終了頁を取得する
   *
   * @return 表示終了頁
   */
  public Long getDisplayMaxPage() {

    Long maxPage = getMaxPage();

    Long tmpMaxPage = getDisplayMinPage() + (2 * displayPageWidth);

    if (tmpMaxPage > maxPage) {
      tmpMaxPage = maxPage;
    }

    return tmpMaxPage;

  }

  /**
   * 画面表示用リスト設定
   *
   * @param list 取得リスト
   */
  public void setDispList(List<?> list) {
    this.list = list;
  }

  /**
   * 現在表示件数表示
   *
   * @return
   */
  public String getDspCurrentCount() {
    if (ExNumberUtils.isNullOrZero(this.totalListSize)) {
      return String.format("%d ~ %d 件 / 総 %d 件", 0, 0, this.totalListSize);
    }

    Long startCount = (this.currentPageNo - 1) * this.displayMaxRow + 1;
    Long endCount = (this.currentPageNo) * this.displayMaxRow;
    if (endCount.compareTo(this.totalListSize) > 0) {
      endCount = this.totalListSize;
    }

    return String.format("%d ~ %d 件 / 総 %d 件", startCount, endCount, this.totalListSize);
  }
}
