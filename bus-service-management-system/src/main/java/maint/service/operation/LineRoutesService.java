package maint.service.operation;

import maint.dto.ListPager;
import maint.dto.operation.lineRoutes.SearchLineRoutesDto;
import maint.dto.operation.lines.SearchLinesDto;
import maint.entity.LineRouteBusStopsEntity;
import maint.entity.LineRoutesEntity;
import maint.exception.CommonDbException;
import maint.form.lineRoutes.LineRoutesDetailForm;
import maint.form.lineRoutes.LineRoutesSearchForm;
import maint.mapper.*;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LineRoutesService extends BaseService {

  /** 路線 MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /** 系統 MAPPER */
  @Autowired
  LineRoutesMapper lineRoutesMapper;

  /** 系統停留所 MAPPER */
  @Autowired
  LineRouteBusStopsMapper lineRouteBusStopsMapper;

  /** ダイヤ改正 MAPPER */
  @Autowired
  DaiyaRevisionsMapper daiyaRevisionsMapper;

  /**
   * 系統一覧リスト取得
   *
   * @param form  系統検索FORM
   * @param pager ListPager
   */
  public void getLineRouteList(LineRoutesSearchForm form, ListPager pager) {

    SearchLineRoutesDto dto = new SearchLineRoutesDto(form, pager.getStartIndex());

    pager.setTotalListSize(lineRoutesMapper.countLineRoutes(dto));

    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(lineRoutesMapper.getLineRoutes(dto));
    }
  }

  /**
   * 路線リスト取得 (SELECT用)
   *
   * @param businessCorpId 事業者ID
   * @return 路線リスト
   */
  public List<SearchLinesDto> getSelectableLines(Long businessCorpId) {

    return linesMapper.getSelectableLines(businessCorpId);
  }

  /**
   * 系統登録
   *
   * @param form 系統詳細FORM
   */
  @Transactional
  public void insert(LineRoutesDetailForm form) {

    LineRoutesEntity lineRoutesEntity = form.getLineRoutesEntity();

    if (lineRoutesMapper.insert(lineRoutesEntity) != 1) {
      throw new CommonDbException("【系統管理】系統登録エラー:" + lineRoutesEntity);
    }

    List<LineRouteBusStopsEntity> lineRouteBusStopsEntity = form.getLineRouteBusStopsEntity(lineRoutesEntity.getLineRouteId(), getUserSession().getUserId());

    if (lineRouteBusStopsMapper.insert(lineRouteBusStopsEntity) == 0) {
      throw new CommonDbException("【系統管理】系統停留所登録エラー:" + lineRouteBusStopsEntity);
    }
  }

  /**
   * 系統更新
   *
   * @param form 系統詳細FORM
   */
  @Transactional
  public void update(LineRoutesDetailForm form) {

    LineRoutesEntity lineRoutesEntity = form.getLineRoutesEntity();

    if (lineRoutesMapper.update(lineRoutesEntity) != 1) {
      throw new CommonDbException("【系統管理】系統更新エラー:" + lineRoutesEntity);
    }

    List<LineRouteBusStopsEntity> lineRouteBusStopsEntity = form.getLineRouteBusStopsEntity(lineRoutesEntity.getLineRouteId(), getUserSession().getUserId());

    // 既存行は変更不可、新しい行は登録
    List<LineRouteBusStopsEntity> newRows = lineRouteBusStopsEntity.stream()
        .filter(e -> e.getLineRouteBusStopId() == null)
        .toList();

    if (ExObjectUtils.isNotEmpty(newRows)) {
      if (lineRouteBusStopsMapper.insert(newRows) == 0) {
        throw new CommonDbException("【系統管理】系統停留所更新登録エラー:" + lineRouteBusStopsEntity);
      }
    }
  }

  /**
   * 系統取得
   *
   * @param lineRouteId PKID
   * @return 系統情報
   */
  public LineRoutesDetailForm getLineRouteByPkId(Long lineRouteId) {
    return lineRoutesMapper.getLineRouteByPkId(lineRouteId);
  }

  /**
   * 便ダイヤ改正利用期間チェック
   *
   * @param rosenId 路線ID
   * @param lineRouteId 系統ID
   * @param servieEndDate 運行終了日
   * @return true : 不可 false : 変更可
   */
  public boolean validateDaiyaRevision (Long rosenId, Long lineRouteId, String servieEndDate) {
    return daiyaRevisionsMapper.countDaiyaRevisionForLineRoute(rosenId, lineRouteId, servieEndDate) != 0;
  }
}
