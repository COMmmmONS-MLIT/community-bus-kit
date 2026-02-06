package maint.service.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import maint.dto.ListPager;
import maint.dto.operation.lineCharges.LineChargeDetailDto;
import maint.dto.operation.lineCharges.LineChargeIdDivisionDto;
import maint.dto.operation.lineCharges.SearchLineChargesDto;
import maint.entity.LineChargeDetailsEntity;
import maint.entity.LineChargesEntity;
import maint.enums.LineChargeDivision;
import maint.exception.CommonDbException;
import maint.form.lineCharges.LineChargeDetailForm;
import maint.form.lineCharges.LineChargeDetailForm.LineChargeTriangle;
import maint.form.lineCharges.LineChargeSearchForm;
import maint.mapper.LineChargeDetailsMapper;
import maint.mapper.LineChargesMapper;

import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 運賃SERVICE
 *
 * @author g.ko
 */
@Service
public class LineChargesService extends BaseService {

  /** 運賃MAPPER */
  @Autowired
  LineChargesMapper lineChargesMapper;

  /** 運賃明細MAPPER */
  @Autowired
  LineChargeDetailsMapper lineChargeDetailsMapper;

  /**
   * 運賃一覧取得
   *
   * @param form 検索FORM
   * @param pager ListPager
   */
  public void getLineCharges(LineChargeSearchForm form, ListPager pager) {
    SearchLineChargesDto dto =
        new SearchLineChargesDto(form, pager.getStartIndex(), getUserSession());
    pager.setTotalListSize(lineChargesMapper.countLineChargesBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(lineChargesMapper.getLineChargesBySearch(dto));
    }
  }

  /**
   * 運賃情報取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @return 運賃詳細情報DTO
   */
  public List<LineChargeDetailDto> getLineChargeDetailsByRevisionId(Long chargeRevisionId) {
    return lineChargeDetailsMapper.getLineChargeDetailsByRevisionId(chargeRevisionId);
  }

  /**
   * 登録(変更)処理
   *
   * @param form 運賃詳細FORM
   */
  @Transactional
  public void registerByForm(LineChargeDetailForm form) {

    // 登録・変更内容
    LineChargesEntity lineChargeEntity = new LineChargesEntity(form, getUserSession());
    // 選択した運賃区分
    Integer div = form.getLineChargeDivision();

    // 運賃マスタの存在確認
    List<LineChargeIdDivisionDto> headers =
        lineChargesMapper.getLineChargesByRevisionId(form.getChargeRevisionId());

    Long lineChargeId = headers.isEmpty() ? null : lineChargeEntity.getLineChargeId();

    //　運賃マスタ登録・変更
    if (lineChargeId == null) {
      if (lineChargesMapper.registerLineCharge(lineChargeEntity) != 1) {
        throw new CommonDbException("【運賃管理】運賃登録エラー");
      }
      // 新規登録後、lineChargeEntityからIDを取得
      lineChargeId = lineChargeEntity.getLineChargeId();

    } else {
      //　運賃区分が三角運賃の時は運賃マスタの一部を更新
      if (LineChargeDivision.TRIANGULAR_FARE.getValue().equals(div)) {
        // 運賃区分のセット
        lineChargeEntity.setLineChargeDivision(form.getLineChargeDivision());
        // 均一運賃、均一ICカード運賃、均一運賃乗換可否のリセット
        lineChargeEntity.setUniformCharge(null);
        lineChargeEntity.setUniformIcPrice(null);
        lineChargeEntity.setUniformTransferType(null);
      }
      lineChargeEntity.setLineChargeId(lineChargeId);
      if (lineChargesMapper.updateLineCharge(lineChargeEntity) != 1) {
        throw new CommonDbException("【運賃管理】運賃更新エラー");
      }
    }

    // 運賃区分に依存する処理
    // 均一運賃選択時の処理
    if (LineChargeDivision.FLAT_FARE.getValue().equals(div)) {
      // 運賃詳細を削除
      deleteAllDetails(lineChargeId);

    // 三角運賃選択時の処理
    } else {

      List<LineChargeTriangle> triangles = form.getTriangle();
      if (triangles == null) return;
      for (LineChargeTriangle t : triangles) {

        // ① まずIDがない場合はエラーとする
        if (t.getRosenBusStopIdFrom() == null || t.getRosenBusStopIdTo() == null) {
          throw new IllegalArgumentException(
              "路線停留所IDが未設定です。路線停留所ID From: " + t.getRosenBusStopIdFrom() + ", 路線停留所ID To: " + t.getRosenBusStopIdTo());
        }

        // ② lineCharge が null/空 は対象外
        if (t.getLineCharge() == null || t.getLineCharge().isBlank()) {
          continue;
        }

        // ③ name がないのも対象外
        if (t.getRosenBusStopNameFrom() == null || t.getRosenBusStopNameTo() == null) {
          continue;
        }

        // ④ detailId 探索（nullで呼ばれないようになった）
        Long detailId =
            lineChargeDetailsMapper.findDetailId(lineChargeId, t.getRosenBusStopIdFrom(),
                t.getRosenBusStopIdTo());

        LineChargeDetailsEntity detailEntity =
            new LineChargeDetailsEntity(lineChargeId, t, getUserSession());

        if (detailId == null) {
          //　運賃詳細マスタが存在しなければ登録
          if (lineChargeDetailsMapper.registerLineChargeDetail(detailEntity) != 1) {
            throw new CommonDbException("【運賃詳細管理】運賃詳細登録エラー");
          }
        } else {
          detailEntity.setLineChargeDetailId(detailId);
          if (lineChargeDetailsMapper.updateLineChargeDetail(detailEntity, getUserSession()) != 1) {
            throw new CommonDbException("【運賃詳細管理】運賃詳細更新エラー");
          }
        }
      }
    }
  }

  /**
   * 指定した運賃の全ての運賃詳細削除
   *
   * @param lineChargeId 運賃ID
   */
  private void deleteAllDetails(Long lineChargeId) {
    if (ExObjectUtils.isBlank(lineChargeId)) return;

    // 一括削除を実行
    lineChargeDetailsMapper.deleteChargeDetails(getUserSession(), lineChargeId);
  }

  /**
   * 三角運賃表の前処理
   *
   * @param dtoList 運賃改定ID
   * @return 運賃詳細情報DTO
   */
  @Transactional(readOnly = true)
  public Map<String, Object> calcTriangleTable(List<LineChargeDetailDto> dtoList) {

    // 順序保持しつつ重複排除（IDがキー）
    LinkedHashMap<Long, String> stopMap = new LinkedHashMap<>();

    // 全ての出発・到着バス停を重複なしで収集
    for (LineChargeDetailDto d : dtoList) {
      Long fromId = d.getRosenBusStopIdFrom();
      String fromName = d.getRosenBusStopNameFrom();
      if (fromId != null && fromName != null) {
        stopMap.putIfAbsent(fromId, fromName);
      }

      Long toId = d.getRosenBusStopIdTo();
      String toName = d.getRosenBusStopNameTo();
      if (toId != null && toName != null) {
        stopMap.putIfAbsent(toId, toName);
      }
    }

    // バス停IDとバス停名のリストを作成
    List<Long> stopIds = new ArrayList<>(stopMap.keySet());
    List<String> stopNames = new ArrayList<>(stopMap.values());

    int n = stopNames.size();

    // 三角表の行見出し（最後のバス停を除く
    List<String> rowStops = stopNames.subList(0, Math.max(0, n - 1));
    // 三角表の列見出し（最初のバス停を除く）
    List<String> colStops = (n >= 1) ? stopNames.subList(1, n) : Collections.emptyList();

    // バス停IDも同様に分割
    List<Long> rowStopIds = stopIds.subList(0, Math.max(0, n - 1));
    List<Long> colStopIds = (n >= 1) ? stopIds.subList(1, n) : Collections.emptyList();

    // 結果をマップに格納
    Map<String, Object> result = new HashMap<>();
    result.put("busStopNames", stopNames);    // 全バス停名リスト
    result.put("rowStops", rowStops);        // 行見出し用バス停名リスト
    result.put("colStops", colStops);        // 列見出し用バス停名リスト
    result.put("rowStopIds", rowStopIds);    // 行見出し用バス停IDリスト
    result.put("colStopIds", colStopIds);    // 列見出し用バス停IDリスト
    return result;
  }
}
