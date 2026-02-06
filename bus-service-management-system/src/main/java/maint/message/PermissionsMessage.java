package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 権限メッセージ
 *
 * @author y.kim
 */
@Getter
@AllArgsConstructor
public enum PermissionsMessage {

  /** ERROR_NOT_SELECTED_PERMISSION */
  ERROR_NOT_SELECTED_PERMISSIONS("権限未選択エラー", "message.error.permissions.select.blank"),
  /** ERROR_PERMISSION_NAME_DUPLE */
  ERROR_PERMISSIONS_NAME_DUPLE("権限名重複エラー", "message.error.permissions.name.duple"),
  /** ERROR_USED_PERMISSIONS */
  ERROR_USED_PERMISSIONS("権限使用エラー", "message.error.used.permissions");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
