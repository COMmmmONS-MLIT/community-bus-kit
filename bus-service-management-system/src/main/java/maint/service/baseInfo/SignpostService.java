package maint.service.baseInfo;

import maint.entity.SignpostsEntity;
import maint.exception.CommonDbException;
import maint.form.signposts.SignpostsDetailForm;
import maint.mapper.SignpostsMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 標柱SERVICE
 *
 * @author g.ko
 */
@Service
public class SignpostService extends BaseService {

  /** 標柱 MAPPER */
  @Autowired
  SignpostsMapper signpostsMapper;

  /**
   * 標柱登録
   *
   * @param form 詳細FORM
   * @return 標柱ID
   */
  @Transactional
  public Long insert(SignpostsDetailForm form) {
    // 標柱登録情報の設定
    SignpostsEntity signpostsEntity = new SignpostsEntity(form);

    // 登録失敗時、エラーとする
    if (signpostsMapper.registerSignpost(signpostsEntity) != 1) {
      throw new CommonDbException("【標柱管理】登録エラー");
    }

    return signpostsEntity.getSignpostId();
  }

  /**
   * 標柱変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(SignpostsDetailForm form) {
    // 標柱変更
    SignpostsEntity signpostsEntity = new SignpostsEntity(form);

    if (signpostsMapper.updateSignpost(signpostsEntity) != 1) {
      throw new CommonDbException("【標柱管理】変更エラー");
    }
  }

}
