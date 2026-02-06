package maint.service.operation;

import maint.dto.ListPager;
import maint.dto.operation.lines.LinesSelectDto;
import maint.dto.operation.lines.SearchLinesDto;
import maint.entity.LineBusStopsEntity;
import maint.entity.LinesEntity;
import maint.exception.CommonDbException;
import maint.form.lines.LinesDetailForm;
import maint.form.lines.LinesSearchForm;
import maint.mapper.*;

import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 路線SERVICE
 *
 * @author jeon
 */

@Service
public class LinesService extends BaseService {

  /** 路線 MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /** 路線停留所 MAPPER */
  @Autowired
  LineBusStopsMapper lineBusStopsMapper;

  /** 系統 MAPPER */
  @Autowired
  LineRoutesMapper lineRoutesMapper;

  /** ダイヤ改正 MAPPER */
  @Autowired
  DaiyaRevisionsMapper daiyaRevisionsMapper;

  /** 運賃改正 MAPPER */
  @Autowired
  ChargeRevisionsMapper chargeRevisionsMapper;

  /**
   * 全路線取得（SELECT用）
   * - 引数の事業者IDでフィルタ
   *
   * @param businessCorpId 事業者ID
   * @return 路線リスト
   */
  public List<LinesSelectDto> getAllLinesForSelectOption(Long businessCorpId) {
    if (businessCorpId == null) {
      return Collections.emptyList();
    }
    return linesMapper.getAllLinesForSelectOption(businessCorpId);
  }

  /**
   * 路線取得（SELECT用）
   *
   * @return 路線DTOリスト
   */
  public List<LinesSelectDto> getBusStopsForSelectOption() {
    return linesMapper.getLinesForSelectOption(getUserSession().getBusinessCorpId());
  }

  /**
   * 全路線取得（SELECT用）
   */
  public List<LinesSelectDto> getAllLinesForSelectOption() {
    return linesMapper.getAllLinesForSelectOption(getUserSession().getBusinessCorpId());
  }

  /**
   * 路線一覧リスト取得
   *
   * @param form  路線検索FORM
   * @param pager ListPager
   */
  public void getLineList(LinesSearchForm form, ListPager pager) {

    SearchLinesDto dto = new SearchLinesDto(form, pager.getStartIndex());

    pager.setTotalListSize(linesMapper.countLines(dto));

    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(linesMapper.getLines(dto));
    }
  }

  /**
   * 路線登録
   *
   * @param form 路線詳細FORM
   */
  @Transactional
  public void insert(LinesDetailForm form) {

    LinesEntity linesEntity = form.getLinesEntity();
    linesEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());

    if (linesMapper.insert(linesEntity) != 1) {
      throw new CommonDbException("【路線管理】路線登録エラー:" + linesEntity);
    }

    List<LineBusStopsEntity> lineBusStopsEntity = form.getLineBusStopsEntity(linesEntity.getRosenId(), getUserSession().getUserId());

    if (lineBusStopsMapper.insert(lineBusStopsEntity) == 0) {
      throw new CommonDbException("【路線管理】路線停留所登録エラー:" + lineBusStopsEntity);
    }
  }

  /**
   * 路線更新
   *
   * @param form 路線詳細FORM
   */
  @Transactional
  public void update(LinesDetailForm form) {

    LinesEntity linesEntity = form.getLinesEntity();
    linesEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());

    if (linesMapper.update(linesEntity) != 1) {
      throw new CommonDbException("【路線管理】路線更新エラー:" + linesEntity);
    }

    List<LineBusStopsEntity> lineBusStopsEntity = form.getLineBusStopsEntity(linesEntity.getRosenId(), getUserSession().getUserId());

    // 既存の行は更新、新しい行は登録
    List<LineBusStopsEntity> existingRows = lineBusStopsEntity.stream()
        .filter(e -> e.getRosenBusStopId() != null)
        .toList();

    List<LineBusStopsEntity> newRows = lineBusStopsEntity.stream()
        .filter(e -> e.getRosenBusStopId() == null)
        .toList();

    if (lineBusStopsMapper.update(existingRows) == 0) {
      throw new CommonDbException("【路線管理】路線停留所更新エラー:" + linesEntity);
    }

    if (ExObjectUtils.isNotEmpty(newRows)) {
      if (lineBusStopsMapper.insert(newRows) == 0) {
        throw new CommonDbException("【路線管理】路線停留所更新登録エラー:" + lineBusStopsEntity);
      }
    }
  }

  /**
   * 路線取得
   *
   * @param rosenId PKID
   * @return 路線情報
   */
  public LinesDetailForm getLineByPkId(Long rosenId) {
    return linesMapper.getLineByPkId(rosenId);
  }

  /**
   * 利用開始日変更可否チェック
   *
   * @param rosenId 路線ID
   * @return true : 変更可 false : 不可
   */
  public boolean isServiceStartDateChangeAllowed (Long rosenId) {
    boolean countLineRoutes = lineRoutesMapper.countLineRoutesForLine(rosenId) == 0;
    boolean countDaiyaRevision = daiyaRevisionsMapper.countDaiyaRevisionForLine(rosenId) == 0;
    boolean countChargeRevision = chargeRevisionsMapper.countChargeRevisionsForLine(rosenId) == 0;
    return countLineRoutes && countDaiyaRevision && countChargeRevision;
  }

  /**
   * 系統の利用期間チェック
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param rosenId 路線ID
   * @return true : 不可 false : 登録可能
   */
  public boolean checkLineRouteByDateAndLine(String serviceStartDate, String serviceEndDate,
      Long rosenId) {
    return lineRoutesMapper.countLineRouteByDateAndLine(serviceStartDate, serviceEndDate, rosenId) != 0;
  }

  /**
   * 路線の利用期間チェック
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param rosenId 路線ID
   * @return true : 登録可能 false : 不可
   */
  public boolean checkLineByDateAndLine(String serviceStartDate, String serviceEndDate,
      Long rosenId) {
    return linesMapper.countLineByDateAndLine(rosenId, serviceStartDate, serviceEndDate) != 0;
  }
}
