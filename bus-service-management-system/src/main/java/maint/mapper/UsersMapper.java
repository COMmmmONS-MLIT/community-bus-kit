package maint.mapper;

import maint.dto.system.users.SearchUsersDto;
import maint.dto.system.users.UsersDetailDto;
import maint.entity.UsersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理者MAPPER
 *
 * @author administrator
 */
@Mapper
public interface UsersMapper {

  /**
   * メールから管理者情報取得
   *
   * @param email メール
   * @return 管理者情報
   */
  UsersEntity getUserByLoginInfo(String email);

  /**
   * ユーザ数取得
   *
   * @param dto ユーザ検索DTO
   * @return ユーザ数
   */
  Long countUsersBySearch(SearchUsersDto dto);

  /**
   * ユーザ一覧取得
   *
   * @param dto ユーザ検索DTO
   * @return ユーザ一覧
   */
  List<UsersDetailDto> getUsersBySearch(SearchUsersDto dto);

  /**
   * ユーザ情報取得
   *
   * @param Id PK
   * @return ユーザ情報
   */
  UsersDetailDto getUserByPk(Long Id);

  /**
   * メールによるユーザ数取得(メール重複チェック用)
   *
   * @param pk      PK
   * @param email メール
   * @return ユーザ数
   */
  int countUsersByEmail(@Param("userId") Long pk, @Param("email") String email);

  /**
   * 登録
   *
   * @param usersEntity ユーザENTITY
   * @return 登録数
   */
  int registerUsers(UsersEntity usersEntity);

  /**
   * 変更
   *
   * @param usersEntity ユーザENTITY
   * @return 変更数
   */
  int updateUsers(UsersEntity usersEntity);

  /**
   * ユーザパスワード取得
   *
   * @param userId ユーザID（PK）
   * @return パスワード
   */
  String getUserPasswordByPK(Long userId);

  /**
   * ユーザパスワード変更処理
   *
   * @param entity ユーザEntity
   * @return 処理件数
   */
  int updateUserPassword(UsersEntity entity);

  /**
   * 2要素認証情報更新
   *
   * @param entity 管理者ENTITY
   * @return 更新件数
   */
  int updateUserTwoFactorAuthInfo(UsersEntity entity);

  /**
   * パスワード間違いの場合、該当アカウントの認証失敗回数を更新
   *
   * @param usersEntity 管理者Entity
   */
  void updateAuthFailureCount(UsersEntity usersEntity);

  /**
   * ログイン成功の場合、ユーザーマスタの更新を行う
   *
   * @param usersEntity 管理者Entity
   */
  void updateUserInfoOnLoginSuccess(UsersEntity usersEntity);

  /**
   * アカウントがロックされているかどうかをチェックします。
   * ロック日時が設定されており、30分以内であれば true を返します。
   * @param userId ユーザーID
   * @return ロック中の場合 true、そうでなければ false
   */
  boolean isAccountLocked(@Param("userId") Long userId);
}
