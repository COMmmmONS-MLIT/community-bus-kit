package maint.form.busStopSignposts;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.dto.baseInfo.busStopSignposts.SignpostsLinkDetailDto;
import maint.enums.DispMode;
import maint.enums.IndoorDivision;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.utils.date.ExDateUtils;

/**
 * 停留所・標柱詳細FORM
 *
 * @author k.takeuchi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusStopSignpostsDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 266994562369053L;

  /** PKID */
  private Long busStopId;

  /** 画面モード */
  private DispMode dispMode;

  /** 押下された「標柱の追加」ボタン番号 */
  private Integer pressDownAddSignpostsButtonNo;

  /** 停留所リスト */
  @Valid
  private List<BusStopLinkDetailDto> busStopLinkDetailDtoList;

  /** 停留所リストのサイズ */
  private Integer busStopSize;

  /**
   * コンストラクタ（新規登録時）
   *
   * @param page ページ番号
   */
  public BusStopSignpostsDetailForm(Long page) {
    this.page = page;
    this.dispMode = DispMode.INSERT_MODE;

    this.busStopLinkDetailDtoList = new ArrayList<>();
    BusStopLinkDetailDto busStopLinkDetailDto = new BusStopLinkDetailDto();
    busStopLinkDetailDto.setBusStopListNo("01");
    busStopLinkDetailDto.newSignposts();
    busStopLinkDetailDto.setSignpostsSize(0);
    busStopLinkDetailDto.setIndoorBusTerminalFlag(IndoorDivision.OUTDOOR.getValue());
    busStopLinkDetailDto.setAvailableFlag(UsageDivision.ACTIVE.getValue());
    busStopLinkDetailDto.setServiceStartDate(ExDateUtils.getCurrentDate());
    busStopLinkDetailDto.setServiceEndDate("9999-12-31");
    busStopLinkDetailDto.setModeFlag(0);
    this.busStopLinkDetailDtoList.add(busStopLinkDetailDto);
  }

  /**
   * コンストラクタ（更新時）
   *
   * @param dto            停留所・標柱情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(BusStopLinkDetailDto dto, Long page, DispMode updateDispMode) {
    // 基本情報設定
    this.page = page;
    this.dispMode = updateDispMode;
    this.busStopId = dto.getBusStopId();
    this.busStopLinkDetailDtoList = new ArrayList<>();

    // DTOから値の設定
    dto.setBusStopListNo("01");
    dto.setModeFlag(1);
    this.busStopLinkDetailDtoList.add(dto);
  }

  /**
   * 新規停留所の追加処理
   */
  public void addBusStop() {
    BusStopLinkDetailDto busStopLinkDetailDto = new BusStopLinkDetailDto();
    busStopLinkDetailDto.setBusStopListNo(String.format("%02d", this.busStopLinkDetailDtoList.size() + 1));
    busStopLinkDetailDto.newSignposts();
    busStopLinkDetailDto.setSignpostsSize(0);
    busStopLinkDetailDto.setIndoorBusTerminalFlag(IndoorDivision.OUTDOOR.getValue());
    busStopLinkDetailDto.setAvailableFlag(UsageDivision.ACTIVE.getValue());
    busStopLinkDetailDto.setModeFlag(0);
    busStopLinkDetailDto.setServiceStartDate(ExDateUtils.getCurrentDate());
    busStopLinkDetailDto.setServiceEndDate("9999-12-31");
    this.busStopLinkDetailDtoList.add(busStopLinkDetailDto);
  }

  /**
   * 新規標柱の追加処理
   *
   * @param recordNo            編集番号
   */
  public void addSignposts(int recordNo) {
    BusStopLinkDetailDto busStopLinkDetailDto = this.busStopLinkDetailDtoList.get(recordNo);
    this.busStopLinkDetailDtoList.get(recordNo).addSignposts(String.format("%02d", busStopLinkDetailDto.getSignpostsLinkDetailDtoList().size() + 1));
    setSignpostDefaultValue(busStopLinkDetailDto, busStopLinkDetailDto.getSignpostsLinkDetailDtoList().get(busStopLinkDetailDto.getSignpostsLinkDetailDtoList().size() - 1));
  }

  /**
   * 標柱件数の挿入処理
   *
   * @param recordNo            編集番号
   */
  public void setSignpostsSize(int recordNo) {
    this.busStopLinkDetailDtoList.get(recordNo).setSignpostsSize();
  }

  /**
   * 標柱追加時の初期情報のセット
   *
   * @param busStopDto 初期情報のもととなる停留所のDTO
   * @param signpostDto 初期情報をセットする標柱のDTO
   */
  private void setSignpostDefaultValue(BusStopLinkDetailDto busStopDto, SignpostsLinkDetailDto signpostDto) {
    // 参照する停留所情報から標柱の値をセット
    // 標柱名
    if (busStopDto.getBusStopName() != null) {
      signpostDto.setSignpostName(busStopDto.getBusStopName());
    }
    // 標柱名かな
    if (busStopDto.getBusStopKanaName() != null) {
      signpostDto.setSignpostKanaName(busStopDto.getBusStopKanaName());
    }
    // 標柱名英語
    if (busStopDto.getBusStopEnName() != null) {
      signpostDto.setSignpostEnName(busStopDto.getBusStopEnName());
    }
    // 緯度経度
    if (busStopDto.getLatitudeLongitude() != null) {
      signpostDto.setLatitudeLongitude(busStopDto.getLatitudeLongitude());
    }
  }
}
