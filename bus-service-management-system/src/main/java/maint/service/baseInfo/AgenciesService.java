package maint.service.baseInfo;

import maint.dto.ListPager;
import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.dto.baseInfo.agencies.SearchAgenciesDto;
import maint.entity.AgenciesEntity;
import maint.exception.CommonDbException;
import maint.form.agencies.AgenciesDetailForm;
import maint.form.agencies.AgenciesSearchForm;
import maint.mapper.AgenciesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

/**
 * 事業者SERVICE
 *
 * @author y.kim
 */
@Service
public class AgenciesService extends BaseService {


  /** 事業者 MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /**
   * 全事業者取得（SELECT用）
   *
   * @return 事業者DTOリスト
   */
  public List<AgencySelectDto> getAllAgenciesForSelectOption() {
    return agenciesMapper.getAllAgenciesForSelectOption();
  }

  /**
   * 事業者一覧リスト取得
   *
   * @param form  事業者検索FORM
   * @param pager ListPager
   */
  public void getAgencyList(AgenciesSearchForm form, ListPager pager) {

    SearchAgenciesDto dto = new SearchAgenciesDto(form, pager.getStartIndex());

    pager.setTotalListSize(agenciesMapper.countAgencies(dto));

    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(agenciesMapper.getAgencies(dto));
    }
  }

  /**
   * 事業者登録
   *
   * @param form 事業者詳細FORM
   */
  @Transactional
  public void insert(AgenciesDetailForm form) {

    AgenciesEntity agenciesEntity = form.getAgenciesEntity();
    agenciesEntity.setAccessKey(generateAccessKey());

    if (agenciesMapper.insert(agenciesEntity) != 1) {
      throw new CommonDbException("【事業者管理】事業者登録エラー:" + agenciesEntity);
    }
  }

  /**
   * 事業者更新
   *
   * @param form 事業者詳細FORM
   */
  @Transactional
  public void update(AgenciesDetailForm form) {

    AgenciesEntity agenciesEntity = form.getAgenciesEntity();

    if (agenciesMapper.update(agenciesEntity) != 1) {
      throw new CommonDbException("【事業者管理】事業者更新エラー:" + agenciesEntity);
    }
  }

  /**
   * 事業者取得
   *
   * @param businessCorpId PKID
   * @return 事業者
   */
  public AgenciesEntity getAgencyByPkId(Long businessCorpId) {
    return agenciesMapper.getAgencyByPkId(businessCorpId);
  }

  /**
   * 法人番号重複チェック
   *
   * @param form 事業者詳細FORM
   * @return true : 重複有り、false : 重複無し
   */
  public boolean isDupleOperationCompanyNo(AgenciesDetailForm form) {
    return agenciesMapper.isDupleOperationCompanyNo(form.getBusinessCorpId(),
        form.getOperationCompanyNo());
  }

  /**
   * 利用中の事業者か？
   *
   * @param businessCorpId 事業者PKID
   * @return true : 利用中、false : 利用無し
   */
  public boolean isUsedAgency(Long businessCorpId) {
    return agenciesMapper.isUsedAgency(businessCorpId);
  }

  /**
   * アクセスキー作成
   *
   * @return ランダム16桁のアクセスキー
   */
  public static String generateAccessKey() {

    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    int LENGTH = 16;
    SecureRandom random = new SecureRandom();

    StringBuilder sb = new StringBuilder(LENGTH);
    for (int i = 0; i < LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }
    return sb.toString();
  }
}
