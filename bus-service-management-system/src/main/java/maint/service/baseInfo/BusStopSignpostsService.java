package maint.service.baseInfo;

import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.dto.baseInfo.busStopSignposts.SignpostsLinkDetailDto;
import maint.entity.BusStopsLinkEntity;
import maint.entity.SignpostsLinkEntity;
import maint.exception.CommonDbException;
import maint.form.busStopSignposts.BusStopSignpostsDetailForm;
import java.util.*;
import maint.mapper.BusStopLinkMapper;
import maint.mapper.SignpostsLinkMapper;
import maint.message.BusStopsMessage;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static maint.utils.date.ExDateUtils.getCurrentDate;

/**
 * 停留所・標柱SERVICE
 *
 * @author k.takeuchi
 */
@Service
public class BusStopSignpostsService extends BaseService {

  /** 停留所MAPPER */
  @Autowired
  BusStopLinkMapper busStopLinkMapper;

  /** 標柱 MAPPER */
  @Autowired
  SignpostsLinkMapper signpostsLinkMapper;

  /**
   * 停留所情報取得
   *
   * @param id PK
   * @return 停留所情報
   */
  public BusStopLinkDetailDto getBusStopByPk(Long id) {
    return busStopLinkMapper.getBusStopByPk(id);
  }

  /**
   * 停留所登録
   *
   * @param form 詳細FORM
   * @return 停留所IDのリスト
   */
  @Transactional
  public List<Long> insert(BusStopSignpostsDetailForm form) {

    // 登録された停留所IDのリスト
    List<Long> completeBusStopIDs = new ArrayList<Long>();

    // 一覧リストから各情報の登録
    for (int i = 0; i < form.getBusStopLinkDetailDtoList().size(); i++) {
      // 停留所登録
      BusStopsLinkEntity busStopsEntity = new BusStopsLinkEntity(form, i);
      BusStopLinkDetailDto busStopLinkDetailDto = form.getBusStopLinkDetailDtoList().get(i);

      // 登録失敗時エラーとする
      if (busStopLinkMapper.registerBusStop(busStopsEntity) != 1) {
        throw new CommonDbException("【停留所管理】登録エラー");
      }

      // 登録成功した停留所IDの取得
      completeBusStopIDs.add(busStopsEntity.getBusStopId());

      for (int j = 0; j < busStopLinkDetailDto.getSignpostsLinkDetailDtoList().size(); j++) {
        // 新レコードに停留所IDを設定する
        SignpostsLinkDetailDto signpostsDetailLinkDto =
            busStopLinkDetailDto.getSignpostsLinkDetailDtoList().get(j);
        signpostsDetailLinkDto.setBusStopId(busStopsEntity.getBusStopId());

        // 到着判定距離(半径)に入力がある場合は整数に直して登録する値の挿入
        if (!signpostsDetailLinkDto.getArrivalPointRadiusStr().isBlank()) {
          signpostsDetailLinkDto.setArrivalPointRadius(
              Integer.parseInt(signpostsDetailLinkDto.getArrivalPointRadiusStr()));
        }

        // 標柱登録
        SignpostsLinkEntity signpostsEntity = new SignpostsLinkEntity(form, i, j);

        // 登録失敗時エラーとする
        if (signpostsLinkMapper.registerSignpost(signpostsEntity) != 1) {
          throw new CommonDbException("【標柱管理】登録エラー");
        }
      }
    }

    return completeBusStopIDs;
  }

  /**
   * 停留所変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(BusStopSignpostsDetailForm form) {
    // 一覧リストから各情報の更新
    for (int i = 0; i < form.getBusStopLinkDetailDtoList().size(); i++) {
      // 停留所更新
      BusStopsLinkEntity busStopsEntity = new BusStopsLinkEntity(form, i);
      BusStopLinkDetailDto busStopLinkDetailDto = form.getBusStopLinkDetailDtoList().get(i);

      // 更新失敗時エラーとする
      if (busStopLinkMapper.updateBusStop(busStopsEntity) != 1) {
        throw new CommonDbException("【停留所管理】変更エラー");
      }

      for (int j = 0; j < busStopLinkDetailDto.getSignpostsLinkDetailDtoList().size(); j++) {
        // 更新か登録かの判定を行い、更新の場合はupdateを登録の場合はinsertの処理を行う
        SignpostsLinkEntity signpostsEntity;
        SignpostsLinkDetailDto signpostsDetailLinkDto =
            busStopLinkDetailDto.getSignpostsLinkDetailDtoList().get(j);
        // 到着判定距離(半径)に入力がある場合は整数に直して登録する値の挿入
        if (!signpostsDetailLinkDto.getArrivalPointRadiusStr().isBlank()) {
          signpostsDetailLinkDto.setArrivalPointRadius(
              Integer.parseInt(signpostsDetailLinkDto.getArrivalPointRadiusStr()));
        }

        if (signpostsDetailLinkDto.getModeFlag() == 0) {
          // 新レコードに停留所IDを設定する
          signpostsDetailLinkDto.setBusStopId(busStopLinkDetailDto.getBusStopId());

          // 標柱登録
          signpostsEntity = new SignpostsLinkEntity(form, i, j);

          // 登録失敗時エラーとする
          if (signpostsLinkMapper.registerSignpost(signpostsEntity) != 1) {
            throw new CommonDbException("【標柱管理】登録エラー");
          }
        } else if (signpostsDetailLinkDto.getModeFlag() == 1) {
          // 標柱更新
          signpostsEntity = new SignpostsLinkEntity(form, i, j);

          // 更新失敗時エラーとする
          if (signpostsLinkMapper.updateSignpost(signpostsEntity) != 1) {
            throw new CommonDbException("【標柱管理】変更エラー");
          }
        }
      }
    }
  }

  /**
   * 利用期間チェック（系統）
   *
   * @param busStopId        停留所ID
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate   利用終了日
   * @return チェック結果
   */
  public boolean checkLineServicePeriod(Long busStopId, String serviceStartDate,
      String serviceEndDate) {
    return busStopLinkMapper.countBusStopsByRouteDate(busStopId, serviceStartDate,
        serviceEndDate) > 0;
  }

  /**
   * 標柱取得（更新画面用）
   *
   * @return 標柱DTOリスト
   */
  public List<SignpostsLinkDetailDto> getSignpostsLinkDetailDtoList(Long busStopId) {
    return signpostsLinkMapper.getSignpostList(busStopId);
  }

  /**
   * 停留所コード重複チェック
   *
   * @param busStopId   停留所ID
   * @param busStopCode 停留所コード
   * @return true：重複、false：重複なし
   */
  public boolean isDuplicateCode(Long busStopId, String busStopCode) {
    String excelStart = getCurrentDate();
    String excelEnd = "9999-12-31";
    return busStopLinkMapper.countDuplicateStopCode(getUserSession().getBusinessCorpId(), busStopId,
        busStopCode, excelStart, excelEnd) > 0;
  }

  /**
   * 新規登録時に、画面入力された停留所一覧内の停留所コード重複を検証
   *
   * @param busStopList   画面入力された停留所情報一覧
   * @param bindingResult BindingResult
   */
  public void validateDuplicateBusStopCodesOnInsert(List<BusStopLinkDetailDto> busStopList,
      BindingResult bindingResult) {

    Map<String, Integer> firstIndexByCode = new HashMap<>();
    Set<Integer> errorAddedIndex = new HashSet<>();

    for (int i = 0; i < busStopList.size(); i++) {
      String raw = busStopList.get(i).getBusStopCode();
      if (ExObjectUtils.isBlank(raw)) {
        continue;
      }

      String code = raw.trim();
      if (code.isEmpty()) {
        continue;
      }

      Integer firstIndex = firstIndexByCode.putIfAbsent(code, i);
      if (firstIndex != null) {
        String msg = getMessage(BusStopsMessage.ERROR_INPUT_BUS_STOP_CODE_DUPLE.getMessage());

        String firstField = "busStopLinkDetailDtoList[" + firstIndex + "].busStopCode";
        if (errorAddedIndex.add(firstIndex)) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), firstField,
              busStopList.get(firstIndex).getBusStopCode(), false, null, null, msg));
        }

        String curField = "busStopLinkDetailDtoList[" + i + "].busStopCode";
        if (errorAddedIndex.add(i)) {
          bindingResult.addError(new FieldError(bindingResult.getObjectName(), curField,
              busStopList.get(i).getBusStopCode(), false, null, null, msg));
        }
      }
    }
  }
}
