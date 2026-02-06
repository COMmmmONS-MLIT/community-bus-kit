package maint.mapper;

import maint.dto.baseInfo.offices.OfficesDetailDto;
import maint.dto.baseInfo.offices.SearchOfficesDto;
import maint.entity.OfficesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 営業所MAPPER
 *
 * @author jeon
 */

@Mapper
public interface OfficesMapper {

  /**
   * 営業所一覧取得
   *
   * @param dto 営業所管理検索DTO
   * @return 営業所管理情報リスト
   */
  List<OfficesDetailDto> getOfficesBySearch(SearchOfficesDto dto);

  /**
   * 営業所一覧件数取得
   *
   * @param dto 営業所管理検索DTO
   * @return 全件件数
   */
  Long countOffices(SearchOfficesDto dto);

  /**
   * 営業所情報取得
   *
   * @param officeId 営業所ID
   * @param businessCorpId 事業者ID（セッション）
   * @return 営業所詳細DTO
   */
  OfficesDetailDto getOfficesByPk(@Param("officeId") Long officeId, @Param("businessCorpId") Long businessCorpId);

  /**
   * 有効な営業所数を取得
   *
   * @param businessCorpId 事業者ID
   * @param officeId 営業所ID
   * @return 件数
   */
  int countOfficeActiveWithCompany(@Param("businessCorpId") String businessCorpId,
      @Param("officeId") String officeId);

  /**
   * 営業所登録
   *
   * @param officesEntity  営業所 Entity
   * @return insert件数
   */
  int registerOffices(OfficesEntity officesEntity);

  /**
   * 営業所変更
   *
   * @param officesEntity  営業所 Entity
   * @return 更新件数
   */
  int updateOffices(OfficesEntity officesEntity);

}
