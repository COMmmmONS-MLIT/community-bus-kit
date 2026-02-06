package maint.service.baseInfo;

import java.util.List;

import maint.dto.ListPager;
import maint.dto.baseInfo.operatingCompanies.OperatingCompaniesDetailDto;
import maint.dto.baseInfo.operatingCompanies.OperatingCompanySelectDto;
import maint.dto.baseInfo.operatingCompanies.SearchOperatingCompaniesDto;
import maint.entity.OperatingCompaniesEntity;
import maint.exception.CommonDbException;
import maint.form.operatingCompanies.OperatingCompaniesDetailForm;
import maint.form.operatingCompanies.OperatingCompaniesSearchForm;
import maint.mapper.OperatingCompaniesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 運行会社SERVICE
 *
 * @author g.ko
 */

@Service
public class OperatingCompaniesService extends BaseService {

  /** 運行会社MAPPER */
  @Autowired
  OperatingCompaniesMapper operatingCompaniesMapper;

  /**
   *  運行会社一覧
   *
   * @param form  運行会社検索FORM
   * @param pager ページャ
   */
  public void setOperatingCompaniesList(OperatingCompaniesSearchForm form, ListPager pager) {

    SearchOperatingCompaniesDto dto = new SearchOperatingCompaniesDto(form, pager.getStartIndex());
    // セッションから事業者IDを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 運行会社一覧を取得
    pager.setTotalListSize(operatingCompaniesMapper.countOperatingCompanies(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(operatingCompaniesMapper.getOperatingCompaniesBySearch(dto));
    }
  }

  /**
   * 運行会社情報取得
   *
   * @param id 運行会社ID
   * @return 運行会社情報DTO
   */
  public OperatingCompaniesDetailDto getOperatingCompaniesByPk(Long id) {
    return operatingCompaniesMapper.getOperatingCompaniesByPk(id);
  }

  /**
   * 全運行会社取得（SELECT用）
   */
  public List<OperatingCompanySelectDto> getAllOperatingCompaniesForSelectOption() {
    return operatingCompaniesMapper.getAllOperatingCompaniesForSelectOption();
  }

  /**
   * 指定した事業者に属する運行会社一覧を取得
   *
   * @param businessCorpId 事業者ID
   * @return 運行会社選択用DTOリスト
   */
  public List<OperatingCompanySelectDto> getOperatingCompaniesForSelectOption(Long businessCorpId) {
    return operatingCompaniesMapper.getOperatingCompaniesForSelectOption(businessCorpId);
  }

  /**
   * 運行会社登録
   *
   * @param form 運行会社詳細FORM
   */
  @Transactional
  public void insert(OperatingCompaniesDetailForm form) {

    OperatingCompaniesEntity operatingCompaniesEntity = new OperatingCompaniesEntity(form);
    // 事業者IDを取得
    operatingCompaniesEntity.setBusinessCorId(getUserSession().getBusinessCorpId());
    // 登録失敗時、エラーとする
    if (operatingCompaniesMapper.registerOperatingCompanies(operatingCompaniesEntity) != 1) {
      throw new CommonDbException("【運行会社管理】登録エラー");
    }
  }

  /**
   * 運行会社変更
   *
   * @param form 運行会社詳細FORM
   */
  @Transactional
  public void update(OperatingCompaniesDetailForm form) {

    OperatingCompaniesEntity operatingCompaniesEntity = new OperatingCompaniesEntity(form);
    // 運行会社更新
    if (operatingCompaniesMapper.updateOperatingCompanies(operatingCompaniesEntity) != 1) {
      throw new CommonDbException("【運行会社管理】変更エラー");
    }
  }

}

