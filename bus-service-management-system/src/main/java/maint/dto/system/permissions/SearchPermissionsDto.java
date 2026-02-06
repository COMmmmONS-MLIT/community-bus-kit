package maint.dto.system.permissions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import maint.dto.BaseDto;
import maint.form.permissions.PermissionsSearchForm;

/**
 * 権限検索DTO
 *
 * @author y.kim
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchPermissionsDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 4953457923407598708L;

  /** 権限ロール名 */
  private String permRoleName;

  /**
   * コンストラクタ
   *
   * @param form       権限検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchPermissionsDto(PermissionsSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.permRoleName =
        StringUtils.isNotBlank(form.getSearchPermRoleName()) ? form.getSearchPermRoleName() : "";
  }

  /**
   * 部分一致Like検索用権限ロール名取得
   *
   * @return 部分一致Like検索用権限ロール名
   */
  public String getLikeSearchPermRoleName() {
    return makeMiddleMatchString(this.permRoleName);
  }

}
