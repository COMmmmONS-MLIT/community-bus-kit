package maint.service.operation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import maint.dto.ListPager;
import maint.dto.baseInfo.offices.OfficesDetailDto;
import maint.dto.operation.busDaiyas.*;
import maint.dto.planDaiya.dailyBusDaiyas.SearchDailyBusDaiyasDto;
import maint.dto.planDaiya.daiyaRevisions.DaiyaRevisionsDetailDto;
import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.dto.planDaiya.vehicles.VehiclesDetailDto;
import maint.entity.BusDaiyasEntity;
import maint.entity.BusServiceEntity;
import maint.form.busDaiyas.BusDaiyasDetailForm;
import maint.form.busDaiyas.BusDaiyasSearchForm;
import maint.mapper.*;
import maint.service.BaseService;
import maint.service.planDaiya.DailyBusDaiyasService;
import maint.service.planDaiya.DaiyaRevisionsService;
import maint.utils.ExObjectUtils;
import maint.utils.ExRegUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 便ダイヤSERVICE
 *
 * @author jeon
 */

@Service
public class BusDaiyasService extends BaseService {

  /** 運行順序チェック用ノード */
  private record lineRouteOrderNode(int arrMin, int depMin, int rowNumber) {
  }


  /** 便ダイヤMAPPER */
  @Autowired
  BusDaiyasMapper busDaiyasMapper;

  /** 系統停留所MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /** 車両MAPPER */
  @Autowired
  VehiclesMapper vehiclesMapper;

  /** 営業所MAPPER */
  @Autowired
  OfficesMapper officesMapper;

  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;

  /** 便ダイヤ改正SERVICE */
  @Autowired
  DaiyaRevisionsService daiyaRevisionsService;

  /**
   * 一覧取得
   *
   * @param form 便ダイヤ検索FORM
   */
  public void setBusDaiyasList(BusDaiyasSearchForm form, ListPager pager) {
    SearchBusDaiyasDto dto = new SearchBusDaiyasDto(form, pager.getStartIndex());
    // セッションから事業者IDを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 便ダイヤ一覧を取得
    pager.setTotalListSize(busDaiyasMapper.countBusDaiyas(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(busDaiyasMapper.getBusDaiyasBySearch(dto));
    }
  }

  /**
   * 便ダイヤ情報取得
   *
   * @return 便ダイヤ情報DTO
   */
  public BusDaiyasDetailDto getBusDaiyasByPk(BusDaiyasDetailForm form) {
    form.setBusinessCorpId(getUserSession().getBusinessCorpId());
    return busDaiyasMapper.getBusDaiyasByPk(form);
  }

  /**
   * 期間(過去分)一覧取得
   *
   * @param form 検索フォーム
   * @return 期間一覧
   */
  public List<BusDaiyasOptionDto> getBusDaiyasOptionList(BusDaiyasDetailForm form) {
    SearchBusDaiyasDto dto = new SearchBusDaiyasDto(form, 0L);
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    return busDaiyasMapper.getBusDaiyasOptionList(dto);
  }

  /**
   * 便取得（SELECT用）
   *
   * @param dto 日毎便ダイヤ一覧DTO
   * @return 便DTOリスト
   */
  public List<BusServicesOptionDto> getBusServicesForSelectOption(SearchDailyBusDaiyasDto dto) {
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    dto.setOperatingCompanyId(getUserSession().getOperatingCompanyId());
    return busDaiyasMapper.getBusServicesForSelectOption(dto);
  }

  /**
   * 系統selectBoxに使用する情報を取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ変更画面の系統selectBox情報取得用DTO一覧
   */
  public List<BusDaiyasLineRouteOptionDto> getBusDaiyasLineRouteOption(BusDaiyasDetailForm form) {
    return busDaiyasMapper.getBusDaiyasLineRouteOption(form);
  }

  /**
   * 運行カレンダーselectBoxに使用する情報を取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ変更画面の運行カレンダーselectBox情報取得用DTO一覧
   */
  public List<BusDaiyasCalendarOptionDto> getBusDaiyasCalendarOption(BusDaiyasDetailForm form) {
    // セッションの事業者IDを設定
    BusDaiyasDetailSearchParamDto dto =
        new BusDaiyasDetailSearchParamDto(form, getUserSession().getBusinessCorpId(),
            getUserSession().getOperatingCompanyId());
    return busDaiyasMapper.getBusDaiyasCalendarOption(dto);

  }

  /**
   * 車両selectBoxに使用する情報を取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ変更画面の車両selectBox情報取得用DTO一覧
   */
  public List<BusDaiyasVehicleOptionDto> getBusDaiyasVehicleOption(BusDaiyasDetailForm form) {
    // セッションの事業者IDを設定
    BusDaiyasDetailSearchParamDto dto =
        new BusDaiyasDetailSearchParamDto(form, getUserSession().getBusinessCorpId(),
            getUserSession().getOperatingCompanyId());
    return busDaiyasMapper.getBusDaiyasVehicleOption(dto);

  }

  /**
   * 営業所selectBoxに使用する情報を取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ変更画面の営業所selectBox情報取得用DTO一覧
   */
  public List<BusDaiyasOfficeOptionDto> getBusDaiyasOfficeOption(BusDaiyasDetailForm form) {
    // セッションの事業者IDを設定
    BusDaiyasDetailSearchParamDto dto =
        new BusDaiyasDetailSearchParamDto(form, getUserSession().getBusinessCorpId(),
            getUserSession().getOperatingCompanyId());
    return busDaiyasMapper.getBusDaiyasOfficeOption(dto);

  }

  /**
   * 便ダイヤ登録済み情報一覧取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 登録済み便ダイヤ情報取得用DTO一覧
   */
  public List<BusDaiyasRegistedDto> getBusDaiyasRegistedList(BusDaiyasDetailForm form) {
    // セッションの事業者IDを設定
    BusDaiyasDetailSearchParamDto dto =
        new BusDaiyasDetailSearchParamDto(form, getUserSession().getBusinessCorpId(),
            getUserSession().getOperatingCompanyId());
    // 取得してきたリストを便ダイヤ情報と、便ダイヤ明細のリストに変更する
    List<BusDaiyasRegistedDto> baseDtoList = busDaiyasMapper.getBusDaiyasRegistedList(dto);
    List<BusDaiyasRegistedDto> busServiceDtoList = new ArrayList<>();

    Long busServiceId = 0L;
    BusDaiyasRegistedDto busDaiyaDto;
    Integer cnt = 1;
    // 取得してきたリストから便情報毎リストを生成する
    for (BusDaiyasRegistedDto obj : baseDtoList) {
      if (obj.getBusServiceId() != null && !busServiceId.equals(obj.getBusServiceId())) {
        busDaiyaDto = new BusDaiyasRegistedDto();
        // 便ID
        busDaiyaDto.setBusServiceId(obj.getBusServiceId());
        // 系統ID
        busDaiyaDto.setLineRouteId(obj.getLineRouteId());
        // 運行カレンダーID
        busDaiyaDto.setCalendarId(obj.getCalendarId());
        // 便連結区分
        busDaiyaDto.setBlockId(obj.getBlockId());
        // 仕業番号
        busDaiyaDto.setWorkNumber(obj.getWorkNumber());
        // 車両ID
        busDaiyaDto.setVehicleId(obj.getVehicleId());
        // 便番号
        busDaiyaDto.setBusServiceTripId(obj.getBusServiceTripId());
        // 便情報
        busDaiyaDto.setBusServiceTripDesc(obj.getBusServiceTripDesc());
        // 便行き先
        busDaiyaDto.setBusServiceTripHeadsign(obj.getBusServiceTripHeadsign());
        // 便記号
        busDaiyaDto.setBusServiceTripDescSymbol(obj.getBusServiceTripDescSymbol());
        // 便名称
        busDaiyaDto.setBusServiceName(obj.getBusServiceName());
        // 営業所ID
        busDaiyaDto.setOfficeId(obj.getOfficeId());
        // 車いす利用区分
        busDaiyaDto.setWheelchairAccessibleType(obj.getWheelchairAccessibleType());
        // 自転車持込区分
        busDaiyaDto.setBikesAllowedType(obj.getBikesAllowedType());
        // 便ダイヤ画面表示列番号
        busDaiyaDto.setBusDaiyaDispColNumber(cnt);
        // 返却リストに追加
        busServiceDtoList.add(busDaiyaDto);

        busServiceId = obj.getBusServiceId();
        cnt++;
      }
    }

    // 便ダイヤ明細情報Dto
    BusDaiyasRegistedDetailDto detailDto;

    // 画面表示データの成型用に路線停留所一覧を取得
    List<BusDaiyasRosenBusStopDto> lineBusStopDtoList =
        busDaiyasMapper.getBusDaiyasRosenBusStop(form);

    // 系統毎リストから便ダイヤ明細リストを生成する
    for (BusDaiyasRegistedDto dtoLr : busServiceDtoList) {
      // 便ダイヤ明細dtoの格納先を生成
      List<BusDaiyasRegistedDetailDto> detailDtoList = new ArrayList<>();
      // 路線停留所で、出発/到着/スキップ判定を行う
      for (BusDaiyasRosenBusStopDto dtoBs : lineBusStopDtoList) {
        // 便ダイヤ明細リストにセットするDtoを生成
        detailDto = new BusDaiyasRegistedDetailDto();
        detailDto.setSkipBusStopFlag(1);
        // 取得してきたリストと路線停留所リストを突き合わせて、出発/到着/スキップ判定を行う
        for (BusDaiyasRegistedDto dtoRegisted : baseDtoList) {
          // 系統IDが同一
          if (dtoRegisted.getLineRouteId()
              .equals(dtoLr.getLineRouteId()) && dtoRegisted.getRosenBusStopId()
              .equals(dtoBs.getRosenBusStopId()) && dtoRegisted.getBusServiceId()
              .equals(dtoLr.getBusServiceId())) {
            detailDto.setSkipBusStopFlag(0);
            detailDto.setBusStopArrivalTime(dtoRegisted.getBusStopArrivalTime());
            detailDto.setBusStopDepartureTime(dtoRegisted.getBusStopDepartureTime());
            // 便ダイヤ情報が、新規追加か更新なのか判定
            // 登録処理では、Dtoに便ダイヤ明細IDがあれば更新。なければ新規登録。としているため、
            // 一覧画面からの便ダイヤ改正ID == 画面で選択した便ダイヤ改正ID であれば更新とし、
            // 画面に送るDtoに便ダイヤ明細に値を設定する。
            if (form.getRevisionSelect().equals(form.getDaiyaRevisionId())) {
              detailDto.setBusDaiyaId(dtoRegisted.getBusDaiyaDetailId());
            }
            detailDto.setLineRouteBusStopId(dtoRegisted.getLineRouteBusStopId());
          }
        }
        // 便ダイヤ明細リストにDtoを追加
        detailDtoList.add(detailDto);
      }
      // 便ダイヤ明細リストを返却リスト内のDtoにセットする
      dtoLr.setDetailList(detailDtoList);
    }


    for (BusDaiyasRegistedDto setBusServiceId : busServiceDtoList) {
      // 便ダイヤ情報が新規登録か更新なのか判定
      // 一覧画面からの便ダイヤ改正ID != 画面で選択した便ダイヤ改正ID であれば新規登録
      if (!form.getRevisionSelect().equals(form.getDaiyaRevisionId())) {
        // 新規登録判定のため、便IDを空に
        setBusServiceId.setBusServiceId(null);
      }
      for (BusDaiyasRegistedDetailDto setBusServiceDetailId : setBusServiceId.getDetailList()) {
        // 便ダイヤ情報が新規登録か更新なのか判定
        // 一覧画面からの便ダイヤ改正ID != 画面で選択した便ダイヤ改正ID であれば新規登録
        if (!form.getRevisionSelect().equals(form.getDaiyaRevisionId())) {
          // 新規登録判定のため、便ダイヤ明細IDを空に
          setBusServiceDetailId.setBusDaiyaId(null);
        }
      }
    }


    return busServiceDtoList;
  }

  /**
   * 系統プルダウン選択項目の停留所取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 登録済み便ダイヤ情報取得用DTO一覧
   */
  public List<BusDaiyasRosenBusStopDto> getBusDaiyasRosenBusStop(BusDaiyasDetailForm form) {
    return busDaiyasMapper.getBusDaiyasRosenBusStop(form);
  }

  /**
   * 系統プルダウン選択項目の停留所取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 登録済み便ダイヤ情報取得用DTO一覧
   */
  public List<BusDaiyasLineRouteBusStopDto> getBusDaiyasLineRouteBusStop(BusDaiyasDetailForm form) {
    return busDaiyasMapper.getBusDaiyasLineRouteBusStop(form);
  }

  /**
   * 入力チェック
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ登録 Dto
   */
  public List<String> validateBusDaiyasDetail(BusDaiyasDetailForm form) {

    List<String> msgList = new ArrayList<>();

    Validator localValidator;
    try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure()
        .failFast(false).buildValidatorFactory()) {
      localValidator = factory.getValidator();
    }

    String errmsg_prefix = "No.";
    // 便ダイヤを画面表示一列ずつ検証する
    List<BusDaiyasRegistedDto> inputBusDaiyaList = form.getBusDaiyasRegistedList();
    for (int i = 0; i < inputBusDaiyaList.size(); i++) {
      int colNum = i + 1;

      for (ConstraintViolation<BusDaiyasRegistedDto> v : localValidator.validate(
          inputBusDaiyaList.get(i))) {
        String msg = v.getMessage();
        if (msg != null && msg.startsWith("{") && msg.endsWith("}")) {
          msg = messageSource.getMessage(msg.substring(1, msg.length() - 1), null, Locale.JAPAN);
        }
        msgList.add(errmsg_prefix + colNum + " " + msg);
      }

      // 営業所と車両に紐づく運行会社が同一であることのチェック
      String vehicleId = inputBusDaiyaList.get(i).getVehicleId();
      String officeId = inputBusDaiyaList.get(i).getOfficeId();
      // 営業所と車両が両方とも入力されている場合のみ実施
      if (ExObjectUtils.isNotBlank(vehicleId) && ExObjectUtils.isNotBlank(officeId)) {
        // 車両IDと事業者IDから車両マスタ検索
        VehiclesDetailDto vehiclesDetailDto =
            vehiclesMapper.getVehiclesByPk(Long.valueOf(vehicleId),
                getUserSession().getBusinessCorpId());
        // 車両マスタの検索結果が空の場合、有効な車両IDではないため、エラーメッセージ追加
        boolean isValidVehicle = ExObjectUtils.isNotBlank(vehiclesDetailDto);
        if (!isValidVehicle) {
          msgList.add(
              errmsg_prefix + colNum + " " + getMessage("validation.busDaiyas.error.invalidVehicleId"));
        }

        // 営業所IDと事業者IDから営業所マスタ検索
        OfficesDetailDto officesDetailDto = officesMapper.getOfficesByPk(Long.valueOf(officeId),
            getUserSession().getBusinessCorpId());

        // 営業所マスタの検索結果が空の場合、有効な営業所IDではないため、エラーメッセージ追加
        boolean isValidOffice = ExObjectUtils.isNotBlank(officesDetailDto);
        if (!isValidOffice) {
          msgList.add(
              errmsg_prefix + colNum + " " + getMessage("validation.busDaiyas.error.invalidOfficeeId"));
        }

        // 車両IDと営業所IDが有効なので、相関チェックを実施。
        if (isValidVehicle && isValidOffice && !ExObjectUtils.equals(
            vehiclesDetailDto.getOperatingCompanyId(), officesDetailDto.getOperatingCompanyId())) {
          msgList.add(
              errmsg_prefix + colNum + " " + getMessage("validation.busDaiyas.error.differentOperationCompany"));
        }
      }

      // 到着時刻、出発時刻の整合性チェック
      List<BusDaiyasRegistedDetailDto> detailList = inputBusDaiyaList.get(i).getDetailList();
      if (detailList != null) {
        int cnt = 0;
        // 運行順序チェック
        for (BusDaiyasRegistedDetailDto dto : detailList) {
          cnt++;
          String arr = dto.getBusStopArrivalTime();
          String dep = dto.getBusStopDepartureTime();
          if (isHHmm(arr) && isHHmm(dep) && toMinutes(arr) > toMinutes(dep)) {
            msgList.add(
                errmsg_prefix + colNum + " " + getMessage("message.error.departure.after.arrival"));
          }

          // 便ダイヤ情報明細のDtoバリデーション
          if (dto.getSkipBusStopFlag() == null || dto.getSkipBusStopFlag().equals(0)) {
            for (ConstraintViolation<BusDaiyasRegistedDetailDto> v : localValidator.validate(dto)) {
              String msg = v.getMessage();
              if (msg != null && msg.startsWith("{") && msg.endsWith("}")) {
                msg = messageSource.getMessage(msg.substring(1, msg.length() - 1), null,
                    Locale.JAPAN);
              }
              msgList.add(errmsg_prefix + colNum + " " + cnt + "番目の " + msg);
            }
          }
        }


        // 運行順序チェック用のノード
        List<lineRouteOrderNode> orderNodes = new ArrayList<>();
        // 運行順序格納
        lineRouteOrderNode(orderNodes, inputBusDaiyaList.get(i));
        // 運行順序チェック
        validateLineRouteOrder(orderNodes, msgList, colNum);
      }

      // 便番号重複チェック（便ダイヤ改正ID毎に）
      String busServiceTripId = inputBusDaiyaList.get(i).getBusServiceTripId();
      String busServiceId = ExObjectUtils.toS(inputBusDaiyaList.get(i).getBusServiceId(), null);
      if (ExObjectUtils.isNotBlank(busServiceTripId) && ExObjectUtils.isNotBlank(
          form.getDaiyaRevisionId()) && this.existsDuplicatedBusServiceTripId(
          form.getDaiyaRevisionId().toString(), busServiceTripId, busServiceId)) {
        msgList.add(
            errmsg_prefix + colNum + " " + getMessage("message.error.busServiceTripId.duplicate"));
      }
    }

    return msgList;
  }

  /**
   * 便ダイヤ改正IDから便番号が重複しているかをチェック
   *
   * @param daiyaRevisionId  改正ID
   * @param busServiceTripId 便番号
   * @param busServiceId     便ID
   * @return 存在すればtrue、存在しなければfalse
   */
  public boolean existsDuplicatedBusServiceTripId(String daiyaRevisionId, String busServiceTripId,
      String busServiceId) {
    return busServicesMapper.countBusServiceTripIdByRevisionId(daiyaRevisionId, busServiceTripId,
        busServiceId) > 0;
  }

  /**
   * 登録処理
   *
   * @param form 便ダイヤ詳細FORM
   */
  @Transactional
  public void register(BusDaiyasDetailForm form) {

    // 便情報登録
    for (BusDaiyasRegistedDto dto : form.getBusDaiyasRegistedList()) {
      // 便登録・更新
      Long busServiceId = this.registerBusService(dto);

      // 便ダイヤ詳細登録・更新
      dto.setBusServiceId(busServiceId);
      this.registerBusDaiyas(dto);

      // 日毎便ダイヤデータ再作成
      DaiyaRevisionsDetailDto data =
          daiyaRevisionsService.getDaiyaRevisionsByPk(dto.getDaiyaRevisionId());
      BusServiceInfoDto busServiceInfo = new BusServiceInfoDto(data, dto);
      dailyBusDaiyasService.regenerateDailyBusDaiya(busServiceInfo);
    }

  }

  /**
   * 便の登録・更新処理
   *
   * @param dto 登録済み便ダイヤ情報取得用DTO
   * @return 便ID
   */
  @Transactional
  public Long registerBusService(BusDaiyasRegistedDto dto) {
    Long serviceId;

    // Entity生成
    BusServiceEntity entity = new BusServiceEntity(dto);

    // 便ダイヤIDの有無から登録・更新
    if (ExObjectUtils.isBlank(dto.getBusServiceId())) {
      busServicesMapper.insertBusService(entity);
      serviceId = entity.getBusServiceId();
    } else {
      busServicesMapper.updateBusService(entity);
      serviceId = dto.getBusServiceId();
    }

    return serviceId;
  }

  /**
   * 便ダイヤ明細の登録・更新処理
   *
   * @param dto 登録済み便ダイヤ情報取得用DTO
   */
  @Transactional
  public void registerBusDaiyas(BusDaiyasRegistedDto dto) {
    if (ExObjectUtils.isBlank(dto)) {
      return;
    }

    for (BusDaiyasRegistedDetailDto busDaiya : dto.getDetailList()) {
      // 便ダイヤ明細情報にスキップフラグが立っていれば登録をスキップ
      if (busDaiya.getSkipBusStopFlag() != null && busDaiya.getSkipBusStopFlag() == 1) {
        continue;
      }
      // Entity生成
      BusDaiyasEntity entity = new BusDaiyasEntity(dto.getBusServiceId(), busDaiya);

      // 便ダイヤ明細IDの入力がなければ登録、あれば更新
      if (ExObjectUtils.isBlank(busDaiya.getBusDaiyaId())) {
        busDaiyasMapper.insertTripDetail(entity);
      } else {
        busDaiyasMapper.updateTripDetailById(entity);
      }
    }
  }


  /**
   * HHmm（24時間）形式かどうかを判定します。
   *
   * @param s 判定対象の文字列
   * @return HHmm 形式
   */
  private boolean isHHmm(String s) {
    return s != null && Pattern.matches(ExRegUtils.timeHHmmReg, s);
  }

  /**
   * HHmm 形式の文字列を分（minutes）に変換します。
   *
   * @param s HHmm 形式の文字列
   * @return 変換後の分数
   */
  private int toMinutes(String s) {
    int hh = Integer.parseInt(s.substring(0, 2));
    int mm = Integer.parseInt(s.substring(2, 4));
    return hh * 60 + mm;
  }

  /**
   * 運行時刻順序の整合性を検証します。
   *
   * @param nodes   収集済みノード
   * @param msgList 検証エラーメッセージ格納先
   */
  private void validateLineRouteOrder(List<lineRouteOrderNode> nodes, List<String> msgList,
      int colNo) {
    if (nodes == null || nodes.isEmpty())
      return;

    nodes.sort(Comparator.comparing(lineRouteOrderNode::rowNumber, Comparator.naturalOrder()));

    lineRouteOrderNode prev = null;
    for (lineRouteOrderNode cur : nodes) {
      if (prev != null) {
        // 現在行の到着時刻が前行の出発時刻より早い場合はエラー
        if (cur.arrMin() <= prev.depMin()) {
          msgList.add(
              "No." + colNo + " " + getMessage("message.error.arrival.time.operation.order"));
        }
      }
      prev = cur;
    }
  }

  /**
   * 運行順序チェック用ノードを収集する
   *
   * @param nodes 収集済みノードのリスト
   * @param dto   便ダイヤ情報Dto
   */
  private void lineRouteOrderNode(List<lineRouteOrderNode> nodes, BusDaiyasRegistedDto dto) {
    List<BusDaiyasRegistedDetailDto> dtlDto = dto.getDetailList();
    for (int i = 0; i < dtlDto.size(); i++) {
      // ノード追加
      String arr = dtlDto.get(i).getBusStopArrivalTime();
      String dep = dtlDto.get(i).getBusStopDepartureTime();
      // 時刻フォーマットチェック
      if (!isHHmm(arr) || !isHHmm(dep)) {
        return;
      }
      int arrMin = toMinutes(arr);
      int depMin = toMinutes(dep);

      nodes.add(new lineRouteOrderNode(arrMin, depMin, i));
    }
  }
}

