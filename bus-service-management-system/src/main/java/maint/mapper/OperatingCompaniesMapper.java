package maint.mapper;

import maint.dto.baseInfo.operatingCompanies.OperatingCompaniesDetailDto;
import maint.dto.baseInfo.operatingCompanies.OperatingCompanySelectDto;
import maint.dto.baseInfo.operatingCompanies.SearchOperatingCompaniesDto;
import maint.entity.OperatingCompaniesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 運行会社MAPPER
 *
 * @author jeon
 */

@Mapper
public interface OperatingCompaniesMapper {

  /**
   * 運行会社一覧取得
   *
   * @param dto 運行会社検索DTO
   * @return 運行会社管理検索結果リスト
   */
  List<OperatingCompaniesDetailDto> getOperatingCompaniesBySearch(SearchOperatingCompaniesDto dto);

  /**
   * 運行会社一覧件数取得
   *
   * @param dto 運行会社検索DTO
   * @return 全件件数
   */
  Long countOperatingCompanies(SearchOperatingCompaniesDto dto);

  /**
   * 運行会社情報取得
   *
   * @param id 運行会社ID
   * @return 運行会社情報DTO
   */
  OperatingCompaniesDetailDto getOperatingCompaniesByPk(Long id);

  /**
   * 運行会社登録
   *
   * @param operatingCompaniesEntity 運行会社 Entity
   * @return insert件数
   */
  int registerOperatingCompanies(OperatingCompaniesEntity operatingCompaniesEntity);

  /**
   * 運行会社変更
   *
   * @param operatingCompaniesEntity 運行会社 Entity
   * @return 更新件数
   */
  int updateOperatingCompanies(OperatingCompaniesEntity operatingCompaniesEntity);
  /**
   * 全運行会社取得（SELECT用）
   *
   * @return 運行会社リスト
   */
  List<OperatingCompanySelectDto> getAllOperatingCompaniesForSelectOption();

  /**
   * 指定した事業者に属する運行会社一覧を取得
   *
   * @param businessCorpId 事業者ID
   * @return 運行会社選択用DTOリスト
   */
  List<OperatingCompanySelectDto> getOperatingCompaniesForSelectOption(Long businessCorpId);

}
