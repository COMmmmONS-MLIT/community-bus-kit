package maint.service.planDaiya;

import maint.dto.ListPager;
import maint.dto.planDaiya.daiyaRevisions.DaiyaRevisionsDetailDto;
import maint.dto.planDaiya.daiyaRevisions.SearchDaiyaRevisionsDto;
import maint.entity.DaiyaRevisionsEntity;
import maint.enums.DispMode;
import maint.exception.CommonDbException;
import maint.form.daiyaRevisions.DaiyaRevisionsDetailForm;
import maint.form.daiyaRevisions.DaiyaRevisionsSearchForm;
import maint.mapper.DaiyaRevisionsMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 便ダイヤ改正SERVICE
 *
 * @author jeon
 */

@Service
public class DaiyaRevisionsService extends BaseService {

  /** 便ダイヤ改正MAPPER */
  @Autowired
  DaiyaRevisionsMapper daiyaRevisionsMapper;

  /**
   * 便ダイヤ改正一覧取得
   *
   * @param form 便ダイヤ改正検索FORM
   * @param pager ListPager
   */
  public void setDaiyaRevisionsList(DaiyaRevisionsSearchForm form, ListPager pager) {

    SearchDaiyaRevisionsDto dto = new SearchDaiyaRevisionsDto(form, pager.getStartIndex());
    // セッションから事業者IDを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 便ダイヤ改正一覧を取得
    pager.setTotalListSize(daiyaRevisionsMapper.countDaiyaRevisions(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(daiyaRevisionsMapper.getDaiyaRevisionsBySearch(dto));
    }
  }

  /**
   * 便ダイヤ改正登録
   *
   * @param form 便ダイヤ改正詳細FORM
   */
  @Transactional
  public void insert(DaiyaRevisionsDetailForm form) {
    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      //  便ダイヤ改正の更新（既存行の更新）
      updateDaiyaRevisionByCondition(form);
      // 一括論理削除(日毎便ダイヤ、便ダイヤ明細、便、便ダイヤ改正)を実施
      deleteCascadeByRosenAndStartDate(form.getRosenId(), form.getServiceStartDate());

    }

    DaiyaRevisionsEntity daiyaRevisionsEntity = new DaiyaRevisionsEntity(form);
    // 登録失敗時、エラーとする
    if (daiyaRevisionsMapper.registerDaiyaRevision(daiyaRevisionsEntity) != 1) {
      throw new CommonDbException("【便ダイヤ改正管理】登録エラー");
    }
  }

  /**
   * 便ダイヤ改正情報取得
   *
   * @param id 便ダイヤ改正ID
   * @return 便ダイヤ改正情報DTO
   */
  public DaiyaRevisionsDetailDto getDaiyaRevisionsByPk(Long id) {
    return daiyaRevisionsMapper.getDaiyaRevisionsByPk(id);
  }

  /**
   * 運行年月開始日は路線の利用期間内
   *
   * @param form 便ダイヤ改正詳細FORM
   * @return true: 利用期間内、false: 利用期間外
   */
  public boolean countRosenAvailableOnServiceStartDate(DaiyaRevisionsDetailForm form) {
    DaiyaRevisionsEntity daiyaRevisionsEntity = new DaiyaRevisionsEntity(form);
    int count = daiyaRevisionsMapper.countRosenAvailableOnServiceStartDate(daiyaRevisionsEntity);
    return count != 0;
  }

  /**
   * 一括論理削除(日毎便ダイヤ、便ダイヤ明細、便、便ダイヤ改正)を実施します。
   *
   * @param rosenId          路線ID
   * @param serviceStartDate 画面の便ダイヤ改正開始日
   */
  @Transactional
  public void deleteCascadeByRosenAndStartDate(Long rosenId, String serviceStartDate) {
    DaiyaRevisionsEntity condition = new DaiyaRevisionsEntity();
    condition.setRosenId(rosenId);
    condition.setServiceStartDate(serviceStartDate);
    daiyaRevisionsMapper.deleteCascadeByRosenAndStartDate(condition);
  }

  /**
   * 便ダイヤ改正の更新（既存行の更新）
   * service_start_date の前日を service_end_date に設定して更新
   */
  public void updateDaiyaRevisionByCondition(DaiyaRevisionsDetailForm form) {
    DaiyaRevisionsEntity daiyaRevisionsEntity = new DaiyaRevisionsEntity(form);
    // 便ダイヤ改正.便ダイヤ改正開始日= 画面.便ダイヤ改正開始日
    daiyaRevisionsEntity.setServiceStartDate(daiyaRevisionsEntity.getServiceStartDate());
    // 便ダイヤ改正.便ダイヤ改正終了日= 画面.便ダイヤ改正開始日 - 1
    daiyaRevisionsEntity.setServiceEndDate(daiyaRevisionsEntity.getServiceStartDate());
    daiyaRevisionsMapper.updateDaiyaRevisionByCondition(daiyaRevisionsEntity);
  }

}

