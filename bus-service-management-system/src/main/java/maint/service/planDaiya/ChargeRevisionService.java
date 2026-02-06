package maint.service.planDaiya;

import static maint.utils.date.ExDateUtils.getCurrentDate;

import java.util.List;
import maint.dto.ListPager;
import maint.dto.planDaiya.chargeRevisions.ChargeRevisionDto;
import maint.dto.planDaiya.chargeRevisions.SearchChargeRevisionDto;
import maint.dto.operation.lineCharges.LineChargeIdDivisionDto;
import maint.entity.ChargeRevisionEntity;
import maint.enums.LineChargeDivision;
import maint.exception.CommonDbException;
import maint.form.ChargeRevisions.ChargeRevisionsDetailForm;
import maint.form.ChargeRevisions.ChargeRevisionsSearchForm;
import maint.mapper.ChargeRevisionsMapper;
import maint.mapper.LineChargeDetailsMapper;
import maint.mapper.LineChargesMapper;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 運賃改定SERVICE
 *
 * @author g.ko
 */
@Service
public class ChargeRevisionService extends BaseService {

  /** 運賃改定MAPPER */
  @Autowired
  ChargeRevisionsMapper chargeRevisionsMapper;

  /** 路線MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /** 運賃MAPPER */
  @Autowired
  LineChargesMapper lineChargesMapper;

  /** 運賃明細MAPPER */
  @Autowired
  LineChargeDetailsMapper lineChargeDetailsMapper;

  /**
   * 運賃改定一覧取得
   *
   * @param form 検索FORM
   * @param pager ListPager
   */
  public void getChargeRevisions(ChargeRevisionsSearchForm form, ListPager pager) {
    SearchChargeRevisionDto dto =
        new SearchChargeRevisionDto(form, pager.getStartIndex(), getUserSession());
    pager.setTotalListSize(chargeRevisionsMapper.countChargeRevisionsBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(chargeRevisionsMapper.getChargeRevisionsBySearch(dto));
    }
  }

  /**
   * 路線利用期間チェック
   *
   * @param serviceStartDate 運賃適用開始日
   * @param rosenId 路線ID
   * @return チェック結果（true：エラー, false：OK）
   */
  public boolean checkLinePeriod(String serviceStartDate, Long rosenId) {
    return linesMapper.countLinesByIdDate(serviceStartDate, rosenId,
        getUserSession().getBusinessCorpId()) <= 0;
  }

  /**
   * 登録処理
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void register(ChargeRevisionsDetailForm form) {

    // 既存の運賃改定情報の更新
    this.updateChargeRevision(form);

    // 運賃改定登録
    this.registerChargeRevision(form);

  }

  /**
   * 既存の運賃改定情報の更新
   *
   * @param form 詳細FORM
   */
  public void updateChargeRevision(ChargeRevisionsDetailForm form) {

    // 既存の運賃改定情報の取得
    List<ChargeRevisionDto> chargeRevisionInfo =
        chargeRevisionsMapper.getChargeRevisionsByRosenId(form.getRosenId(),
            form.getServiceStartDate(), getUserSession().getBusinessCorpId());

    // 既存運賃改定情報の変更・削除
    if (ExObjectUtils.isNotBlank(chargeRevisionInfo)) {

      for (ChargeRevisionDto chargeRevision : chargeRevisionInfo) {

        if (ExDateUtils.compareLocalDate(chargeRevision.getServiceStartDate(), getCurrentDate())
            <= 0) {

          // 運賃改定開始日が未来日付のデータは、運賃改定終了日を画面.運賃改定開始日の前日に設定
          ChargeRevisionEntity entity =
              new ChargeRevisionEntity(getUserSession(), chargeRevision.getChargeRevisionId(),
                  form.getServiceStartDate());
          if (chargeRevisionsMapper.updateServiceEndDate(entity) != 1) {
            throw new CommonDbException("【運賃改定管理】運賃改定変更エラー");
          }

          // 運賃改定開始日が未来日付のデータは、運賃改定、運賃、運賃明細を削除
        } else {
          // 運賃改定削除
          ChargeRevisionEntity entity =
              new ChargeRevisionEntity(getUserSession(), chargeRevision.getChargeRevisionId());
          if (chargeRevisionsMapper.deleteChargeRevision(entity) != 1) {
            throw new CommonDbException("【運賃改定管理】運賃改定削除エラー");
          }

          List<LineChargeIdDivisionDto> lineChargeDtoList =
              lineChargesMapper.getLineChargesByRevisionId(chargeRevision.getChargeRevisionId());
          if (ExObjectUtils.isNotBlank(lineChargeDtoList)) {

            for (LineChargeIdDivisionDto lineChargeDto : lineChargeDtoList) {
              // 運賃削除
              if (lineChargesMapper.deleteLineCharge(getUserSession(),
                  lineChargeDto.getLineChargeId()) != 1) {
                throw new CommonDbException("【運賃改定管理】運賃削除エラー");
              }
              // 運賃明細削除（運賃区分が「2:三角運賃」の場合のみ、運賃明細も削除）
              if (lineChargeDto.getLineChargeDivision()
                  .equals(LineChargeDivision.TRIANGULAR_FARE.getValue())) {
                if (lineChargeDetailsMapper.deleteChargeDetails(getUserSession(), lineChargeDto.getLineChargeId()) == 0) {
                  throw new CommonDbException("【運賃改定管理】運賃明細削除エラー");
                }
              }
            }

          }

        }
      }

    }
  }

  /**
   * 運賃改定登録
   *
   * @param form 運賃改定詳細FORM
   */
  public void registerChargeRevision(ChargeRevisionsDetailForm form) {
    // 路線の利用終了日を取得
    String serviceEndDate = linesMapper.getServiceEndDateById(form.getRosenId());
    ChargeRevisionEntity chargeRevisionEntity =
        new ChargeRevisionEntity(getUserSession(), form, serviceEndDate);
    // 登録失敗時、エラーとする
    if (chargeRevisionsMapper.registerChargeRevision(chargeRevisionEntity) != 1) {
      throw new CommonDbException("【運賃改定管理】登録エラー");
    }
  }

}
