package maint.mapper;

import java.util.List;
import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.dto.integrate.gtfsReport.GtfsAgencyInfoDto;
import org.apache.ibatis.annotations.Mapper;
import maint.dto.baseInfo.agencies.SearchAgenciesDto;
import maint.entity.AgenciesEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 事業者MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface AgenciesMapper {

  /**
   * 全事業者取得（SELECT用）
   *
   * @return 全事業者
   */
  List<AgencySelectDto> getAllAgenciesForSelectOption();

  /**
   * 事業者名取得
   *
   * @param businessCorpId 事業者ID
   * @return 事業者名
   */
  String getBusinessCorpNameByBusinessCorpId(Long businessCorpId);

  /**
   * 事業者ロール件数取得(Page用)
   *
   * @param dto 事業者検索DTO
   * @return 事業者ロール件数
   */
  Long countAgencies(SearchAgenciesDto dto);

  /**
   * 事業者ロールリスト取得(Page用)
   *
   * @param dto 事業者検索DTO
   * @return 事業者ロールリスト
   */
  List<AgenciesEntity> getAgencies(SearchAgenciesDto dto);

  /**
   * 事業者登録
   *
   * @param entity 事業者 Entity
   * @return 登録件数
   */
  int insert(AgenciesEntity entity);

  /**
   * 事業者更新
   *
   * @param entity 事業者 Entity
   * @return 更新件数
   */
  int update(AgenciesEntity entity);

  /**
   * 事業者取得
   *
   * @param businessCorpId PKID
   * @return 事業者
   */
  AgenciesEntity getAgencyByPkId(Long businessCorpId);

  /**
   * 法人番号が重複されているか？
   *
   * @param businessCorpId 事業者PKID
   * @param operationCompanyNo 法人番号
   * @return true : 重複有り、false : 重複無し
   */
  boolean isDupleOperationCompanyNo(Long businessCorpId, String operationCompanyNo);

  /**
   * 利用中の事業者か？
   *
   * @param businessCorpId 事業者PKID
   * @return true : 利用中、false : 利用無し
   */
  boolean isUsedAgency(Long businessCorpId);

  /**
   * 有効な事業者数を取得
   *
   * @param businessCorpId 事業者ID
   * @return 件数
   */
  int countBusinessActive(@Param("businessCorpId") String businessCorpId);

  /**
   * 事業者IDからGTFS-schedule用の事業者情報を取得
   *
   * @param businessCorpId 事業者ID
   * @return 事業者情報出力DTO
   */
  List<GtfsAgencyInfoDto> getAgencyInfoList(@Param("businessCorpId") Long businessCorpId);
}

