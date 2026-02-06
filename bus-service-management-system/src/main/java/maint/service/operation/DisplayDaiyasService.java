package maint.service.operation;

import maint.dto.operation.displayDaiyas.DisplayDaiyasDto;
import maint.dto.operation.displayDaiyas.DisplayTimelineResult;
import maint.form.displayDaiyas.DisplayDaiyasSearchForm;
import maint.mapper.*;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisplayDaiyasService extends BaseService {

  /** 路線 MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 棒ダイヤ情報取得
   *
   * @param form 棒ダイヤ検索FORM
   * @return 棒ダイヤ取得
   */
  public List<DisplayDaiyasDto> getDisplayDaiyas(DisplayDaiyasSearchForm form) {
    return linesMapper.getDisplayDaiyas(form);
  }

  /**
   * タイムライン表示用のデータ（色・時間ラベル・時間範囲）を作成する
   *
   * @param data ダイヤ情報リスト
   * @return タイムライン描画に必要なデータを格納した結果DTO
   */
  public DisplayTimelineResult buildTimelineData(List<DisplayDaiyasDto> data) {

    // 仕業番号ごとにダイヤデータをグループ化する
    Map<String, List<DisplayDaiyasDto>> grouped = data.stream().collect(
        Collectors.groupingBy(DisplayDaiyasDto::getWorkNumber, LinkedHashMap::new,
            Collectors.toList()));

    // 表示対象の最小時刻（始発出発時刻）を分単位で取得
    int minMinute =
        data.stream().mapToInt(dto -> toMinutes(dto.getBusStopDepartureTime())).min().orElse(0);

    // 表示対象の最大時刻（最終到着時刻）を分単位で取得
    int maxMinute =
        data.stream().mapToInt(dto -> toMinutes(dto.getBusStopArrivalTime())).max().orElse(1440);

    // タイムライン全体の表示時間（分数）
    int totalMinutes = maxMinute - minMinute + 1;

    // レイアウト調整の為、最低12時間分を表示
    if (totalMinutes < 720) {totalMinutes = 720;}

    // 色とラベルのマップ
    Map<String, List<String>> colorsMap = new LinkedHashMap<>();
    // 出発時刻・到着時刻のラベルマップ
    Map<String, List<String>> departureTimeLabelsMap = new LinkedHashMap<>();
    Map<String, List<String>> arrivalTimeLabelsMap = new LinkedHashMap<>();

    // 各仕業番号ごとに処理
    for (Map.Entry<String, List<DisplayDaiyasDto>> entry : grouped.entrySet()) {
      String workNumber = entry.getKey();  // 仕業番号
      List<DisplayDaiyasDto> dtos = entry.getValue();  // 該当するダイヤデータ

      List<String> colors = new ArrayList<>();           // 各分ごとの色
      List<String> departureLabels = new ArrayList<>();  // 出発時刻ラベル
      List<String> arrivalLabels = new ArrayList<>();    // 到着時刻ラベル

      // 分単位でタイムラインを構築
      for (int i = 0; i < totalMinutes; i++) {
        int currentMinute = minMinute + i;  // 現在の分

        // 現在の分に運行中の便リストを抽出
        List<DisplayDaiyasDto> runningDtos = dtos.stream().filter(d -> {
          int dep = toMinutes(d.getBusStopDepartureTime());
          int arr = toMinutes(d.getBusStopArrivalTime());
          return currentMinute >= dep && currentMinute <= arr;
        }).toList();

        int count = runningDtos.size();  // 運行中の便の数
        long cancelledCount = runningDtos.stream().filter(d -> d.getSalesCancelFlg() == 1).count();  // 運休中の便の数
        long activeCount = count - cancelledCount;  // 運行中（運休ではない）の便の数

        // 状況に応じて色を設定
        if (count == 0) {
          colors.add("");  // 運行なし
        } else if (cancelledCount == count) {
          colors.add("yellow");  // 全て運休中
        } else if (count > 1 && activeCount == 1 && cancelledCount > 0) {
          colors.add("lightblue");  // 運休と運行(重複無)が重複している場合
        } else if (count > 1) {
          colors.add("red");  // 運行が重複（運休なし）
        } else {
          colors.add("lightblue");  // 単独で通常運行
        }

        // 出発時刻／到着時刻のラベルを設定
        String depLabel = "";
        String arrLabel = "";
        for (DisplayDaiyasDto d : dtos) {
          int dep = toMinutes(d.getBusStopDepartureTime());
          int arr = toMinutes(d.getBusStopArrivalTime());

          if (currentMinute == dep) {
            depLabel = toTimeLabel(currentMinute);  // 出発時刻ラベル設定
          }
          if (currentMinute == arr) {
            arrLabel = toTimeLabel(currentMinute);  // 到着時刻ラベル設定
          }
        }

        // 各リストに追加
        departureLabels.add(depLabel);
        arrivalLabels.add(arrLabel);
      }

      // 各仕業番号ごとにデータをマップへ登録
      colorsMap.put(workNumber, colors);
      departureTimeLabelsMap.put(workNumber, departureLabels);
      arrivalTimeLabelsMap.put(workNumber, arrivalLabels);
    }

    // 結果をDTOとして返却
    return new DisplayTimelineResult(grouped, colorsMap, departureTimeLabelsMap,
        arrivalTimeLabelsMap, totalMinutes);
  }

  // 変換関数たち
  private int toMinutes(String hhmm) {
    int hour = Integer.parseInt(hhmm.substring(0, 2));
    int minute = Integer.parseInt(hhmm.substring(2));
    return hour * 60 + minute;
  }

  private String toTimeLabel(int minutes) {
    int h = minutes / 60;
    int m = minutes % 60;
    return String.format("%02d:%02d", h, m);
  }
}
