package maint.mapper;

import maint.dto.sales.importIncomes.ImportIncomesDto;
import maint.dto.sales.importIncomes.SearchImportIncomesDto;
import maint.entity.IncomeInfosEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 収入情報MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface IncomeInfoMapper {

  /**
   * 収入情報一覧取得
   *
   * @param dto 収入情報インポート検索DTO
   * @return 収入情報インポート一覧
   */
  ImportIncomesDto getImportIncomesBySearch(SearchImportIncomesDto dto);

  /**
   * 登録
   *
   * @param incomeInfosEntity 収入情報Entity
   * @return 登録数
   */
  int registerIncomeInfo(IncomeInfosEntity incomeInfosEntity);

  /**
   * 変更
   *
   * @param incomeInfosEntity 収入情報Entity
   * @return 変更数
   */
  int updateIncomeInfo(IncomeInfosEntity incomeInfosEntity);

}
