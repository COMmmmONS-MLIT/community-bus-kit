package maint.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import maint.entity.TwoFactorAuthHistoriesEntity;

/**
 * 管理者2要素認証履歴MAPPER
 *
 * @author sangsoo.lee
 */
@Mapper
public interface TwoFactorAuthHistoriesMapper {

  /**
   * 履歴登録
   *
   * @param entity 管理者2要素認証履歴Entity
   * @return 処理件数
   */
  int insertTwoFactorHistory(TwoFactorAuthHistoriesEntity entity);

  /**
   * 履歴削除
   *
   * @param entity 管理者2要素認証履歴Entity
   * @return 処理件数
   */
  int deleteTwoFactorAuthHistoryByUserId(TwoFactorAuthHistoriesEntity entity);

  /**
   * 有効な2要素認証件数を取得
   *
   * @param userId          ユーザID
   * @param authCode        認証コード
   * @param compareDateTime 比較日時
   * @return 件数
   */
  Long countValidTwoFactorAuth(@Param("userId") Long userId, @Param("authCode") String authCode,
      @Param("compareDateTime") String compareDateTime);

}
