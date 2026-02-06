package maint.mapper;

import maint.entity.SignpostsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 標柱MAPPER
 *
 * @author g.ko
 */
@Mapper
public interface SignpostsMapper {

  /**
   * 登録
   *
   * @param signpostsEntity 標柱ENTITY
   * @return 登録数
   */
  int registerSignpost(SignpostsEntity signpostsEntity);

  /**
   * 変更
   *
   * @param signpostsEntity 標柱ENTITY
   * @return 変更数
   */
  int updateSignpost(SignpostsEntity signpostsEntity);

}
