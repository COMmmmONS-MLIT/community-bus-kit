package maint.dto.operation.lines;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.form.lines.LinesSearchForm;

/**
 * 路線検索DTO
 *
 * @author y.kim
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchLinesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 990128047631446058L;

  //** 路線ID */
  Long rosenId;

  /** 事業者ID */
  Long businessCorpId;

  /** 路線コード */
  String rosenCode;

  /** 利用開始年月日 */
  String serviceStartDate;

  /** 利用終了年月日 */
  String serviceEndDate;

  /** 路線名 */
  String rosenName;

  /** 路線名 かな */
  String rosenKanaName;

  /** 路線名 英語 */
  String rosenEnName;

  /** 利用可否区分 */
  Integer availableFlag;

  /** 事業者名 */
  String name;

  /**
   * コンストラクタ
   *
   * @param form 路線検索 Form
   * @param startIndex 行の開始インデックス
   */
  public SearchLinesDto(LinesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.rosenId = form.getRosenId();
    this.rosenName = form.getRosenName();
    this.businessCorpId = form.getBusinessCorpId();
    this.availableFlag = form.getSearchAvailableFlag();
  }

  /**
   * 部分一致Like検索用路線名取得
   *
   * @return 部分一致Like検索用路線名
   */
  public String getLikeSearchRosenName() {
    return makeMiddleMatchString(this.rosenName);
  }

}
