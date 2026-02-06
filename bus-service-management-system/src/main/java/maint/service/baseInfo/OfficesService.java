package maint.service.baseInfo;

import maint.dto.ListPager;
import maint.dto.baseInfo.offices.OfficesDetailDto;
import maint.dto.baseInfo.offices.SearchOfficesDto;
import maint.entity.OfficesEntity;
import maint.exception.CommonDbException;
import maint.form.offices.OfficesDetailForm;
import maint.form.offices.OfficesSearchForm;
import maint.mapper.OfficesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 営業所SERVICE
 *
 * @author jeon
 */

@Service
public class OfficesService extends BaseService {

  /** 営業所MAPPER */
  @Autowired
  OfficesMapper officesMapper;

  /**
   * 営業所一覧
   *
   * @param form  営業所管理検索FORM
   * @param pager  ページャ
   */
  public void setOfficesList(OfficesSearchForm form, ListPager pager) {

    SearchOfficesDto dto = new SearchOfficesDto(form, pager.getStartIndex());
    // セッションからユーザーを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 営業所一覧を取得
    pager.setTotalListSize(officesMapper.countOffices(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(officesMapper.getOfficesBySearch(dto));
    }
  }

  /**
   * 営業所登録
   *
   * @param form 営業所管理詳細FORM
   */
  @Transactional
  public void insert(OfficesDetailForm form) {

    OfficesEntity officesEntity = new OfficesEntity(form);
    // 登録失敗時、エラーとする
    if (officesMapper.registerOffices(officesEntity) != 1) {
      throw new CommonDbException("【営業所管理】登録エラー");
    }
  }

  /**
   * 営業所情報取得
   *
   * @param id 営業所ID
   * @return 営業所情報DTO
   */
  public OfficesDetailDto getOfficesByPk(Long id) {
    Long businessCorpId = getUserSession().getBusinessCorpId();
    return officesMapper.getOfficesByPk(id, businessCorpId);
  }

  /**
   * 営業所変更
   *
   * @param form 営業所管理詳細FORM
   */
  @Transactional
  public void update(OfficesDetailForm form) {

    OfficesEntity officesEntity = new OfficesEntity(form);
    // 営業所更新
    if (officesMapper.updateOffices(officesEntity) != 1) {
      throw new CommonDbException("【営業所管理】変更エラー");
    }
  }


}

