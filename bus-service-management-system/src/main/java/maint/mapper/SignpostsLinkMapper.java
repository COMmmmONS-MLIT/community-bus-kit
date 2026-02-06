package maint.mapper;

import java.util.List;

import maint.dto.baseInfo.busStopSignposts.SignpostsLinkDetailDto;
import maint.dto.baseInfo.busStopSignposts.SignpostsSelectLinkDto;
import maint.entity.SignpostInsertEntity;
import maint.entity.SignpostsLinkEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 標柱MAPPER(停留所・標柱管理画面用)
 *
 * @author k.takeuchi
 */
@Mapper
public interface SignpostsLinkMapper {

  /**
   * 標柱数取得
   *
   * @param dto 検索DTO
   * @return 標柱数
   */
  Long countSignpostsBySearch(SignpostsSelectLinkDto dto);

  /**
   * 標柱情報取得
   *
   * @param Id PK
   * @return 標柱情報
   */
  SignpostsLinkDetailDto getSignpostByPk(Long Id);

  /**
   * 登録
   *
   * @param signpostsEntity 標柱ENTITY
   * @return 登録数
   */
  int registerSignpost(SignpostsLinkEntity signpostsEntity);

  /**
   * 登録
   *
   * @param signpostInsertEntity 標柱ENTITY
   * @return 登録数
   */
  int insertSignpost(SignpostInsertEntity signpostInsertEntity);

  /**
   * 変更
   *
   * @param signpostsEntity 標柱ENTITY
   * @return 変更数
   */
  int updateSignpost(SignpostsLinkEntity signpostsEntity);

  /**
   * 標柱一覧取得
   *
   * @param id 停留所ID
   * @return 標柱一覧
   */
  List<SignpostsLinkDetailDto> getSignpostList(Long id);
  
}
