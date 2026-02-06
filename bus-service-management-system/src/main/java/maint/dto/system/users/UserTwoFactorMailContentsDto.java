package maint.dto.system.users;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 管理者2要素認証メール本文DTO
 *
 * @author sangsoo.lee
 *
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTwoFactorMailContentsDto implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = -8873187115479687651L;
  /** ユーザ名 */
  private String userName;
  /** 認証コード */
  private String authCode;
  /** 認証コード有効期限 */
  private String limitTime;

}
