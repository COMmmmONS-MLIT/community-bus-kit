package maint.service.planDaiya;

import maint.dto.ListPager;
import maint.dto.planDaiya.vehicles.VehiclesDetailDto;
import maint.dto.operation.calendars.CalendarInfoDto;
import maint.dto.planDaiya.dailyBusDaiyas.DailyBusDaiyasDetailDto;
import maint.dto.planDaiya.dailyBusDaiyas.DailyDaiyaOperationHistoryDto;
import maint.dto.planDaiya.dailyBusDaiyas.SearchDailyBusDaiyasDto;
import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.dto.operation.holidayCalendars.DailyBusDaiyaDto;
import maint.entity.DailyBusDaiyasEntity;
import maint.enums.*;
import maint.exception.CommonDbException;
import maint.form.dailyBusDaiyas.DailyBusDaiyasDetailForm;
import maint.form.dailyBusDaiyas.DailyBusDaiyasSearchForm;
import maint.mapper.*;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static maint.utils.date.ExDateUtils.getCurrentDate;

/**
 * 日毎便ダイヤSERVICE
 *
 * @author jeon
 */

@Service
public class DailyBusDaiyasService extends BaseService {

  /** 祝日カレンダーMAPPER */
  @Autowired
  private HolidayCalendarMapper holidayCalendarMapper;

  /** 日毎便ダイヤMAPPER */
  @Autowired
  DailyBusDaiyasMapper dailyBusDaiyasMapper;

  /** 運行カレンダーMAPPER */
  @Autowired
  CalendarsMapper calendarsMapper;

  /** 運行カレンダー明細MAPPER */
  @Autowired
  CalendarDetailsMapper calendarDetailsMapper;

  /** 運行除外日設定MAPPER */
  @Autowired
  CalendarCancelDatesMapper calendarCancelDatesMapper;

  /** 運行指定日設定MAPPER */
  @Autowired
  CalendarDesignatedDatesMapper calendarDesignatedDatesMapper;

  /** 車両 MAPPER */
  @Autowired
  VehiclesMapper vehiclesMapper;

  /**
   * 日毎便ダイヤ再作成
   *
   * @param busServiceDto 便情報DTO
   */
  @Transactional
  public void regenerateDailyBusDaiya(BusServiceInfoDto busServiceDto) {
    // 運行カレンダーデータ取得
    List<CalendarInfoDto> calendarList =
        calendarsMapper.getCalendarsById(busServiceDto.getCalendarId(),
            getUserSession().getBusinessCorpId());

    // 曜日指定可否区分
    Integer weekDesignatableFlag = calendarList.get(0).getWeekDesignatableFlag();
    // 運行カレンダー除外日設定データ
    Set<String> cancelDateSet = new HashSet<>();
    // 運行カレンダー指定日設定データ
    List<String> designatedDates = new ArrayList<>();
    Set<String> designatedDateSet = new HashSet<>();

    // 運行カレンダー除外日設定データ取得
    if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
      List<String> cancelDates =
          calendarCancelDatesMapper.getFutureCancelDatesByCalendarId(busServiceDto.getCalendarId(),
              getUserSession().getBusinessCorpId());
      cancelDateSet = cancelDates == null ? Collections.emptySet() : new HashSet<>(cancelDates);
    }

    // 運行カレンダー指定日設定データ取得
    designatedDates =
        calendarDesignatedDatesMapper.getFutureDesignatedDatesByCalendarId(busServiceDto.getCalendarId(),
            getUserSession().getBusinessCorpId());
    designatedDateSet =
        designatedDates == null ? Collections.emptySet() : new HashSet<>(designatedDates);

    // 曜日指定可否区分が「指定不可」かつ、運行指定日（未来日）が存在しない場合、処理をスキップする。
    if (weekDesignatableFlag.equals(
        WeekDesignatableFlag.DESIGNATION_DISABLED.getValue()) && ExObjectUtils.isBlank(
        designatedDates)) {
      return;
    }

    // 処理対象日
    List<String> targetDates = new ArrayList<>();
    // 更新対象の日毎便ダイヤデータ
    Map<String, DailyBusDaiyaDto> dailyByDate = new HashMap<>();

    // 処理対象日、更新対象の日毎便ダイヤデータ取得（曜日指定可否区分が「指定可」の場合）
    if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
      String startDate;
      String endDate;
      // 開始日：便ダイヤ改正.便ダイヤ改正開始日とシステム日付+1日の大きい方の日付
      if (ExDateUtils.compareLocalDate(busServiceDto.getServiceStartDate(),
          ExDateUtils.addDaysToDate(getCurrentDate(), 1)) < 0) {
        startDate = ExDateUtils.addDaysToDate(getCurrentDate(), 1);
      } else {
        startDate = busServiceDto.getServiceStartDate();
      }
      // 終了日：便ダイヤ改正.便ダイヤ改正終了日とシステム日付+1か月の小さい方の日付
      if (ExDateUtils.compareLocalDate(busServiceDto.getServiceEndDate(),
          ExDateUtils.addMonthsToDate(getCurrentDate(), 1)) < 0) {
        endDate = busServiceDto.getServiceEndDate();
      } else {
        endDate = ExDateUtils.addMonthsToDate(getCurrentDate(), 1);
      }
      targetDates = ExDateUtils.listDatesInclusive(startDate, endDate);

      // 更新対象の日毎便ダイヤデータ取得
      List<DailyBusDaiyaDto> DailyBusDaiyaList =
          dailyBusDaiyasMapper.getDailyBusDaiyasByBusServiceId(busServiceDto.getBusServiceId(),
              startDate, endDate);
      if (ExObjectUtils.isNotBlank(DailyBusDaiyaList)) {
        for (DailyBusDaiyaDto d : DailyBusDaiyaList) {
          if (ExObjectUtils.isNotBlank(d) && ExObjectUtils.isNotBlank(d.getServiceDate())) {
            dailyByDate.put(d.getServiceDate(), d);
          }
        }
      }
    } else {
      // 処理対象日、更新対象の日毎便ダイヤデータ取得（曜日指定可否区分が「指定不可」の場合）
      targetDates = designatedDates;

      // 更新対象の日毎便ダイヤデータ取得
      List<DailyBusDaiyaDto> DailyBusDaiyaList =
          dailyBusDaiyasMapper.getDailyBusDaiyasByServiceIdAndDateList(busServiceDto.getBusServiceId(),
              targetDates);
      if (ExObjectUtils.isNotBlank(DailyBusDaiyaList)) {
        for (DailyBusDaiyaDto d : DailyBusDaiyaList) {
          if (ExObjectUtils.isNotBlank(d) && ExObjectUtils.isNotBlank(d.getServiceDate())) {
            dailyByDate.put(d.getServiceDate(), d);
          }
        }
      }
    }

    // 日毎便ダイヤデータの登録・更新用Entity作成（対象日の1日毎に繰り返す）
    List<DailyBusDaiyasEntity> busDaiyasEntities = new ArrayList<>();
    for (String date : targetDates) {
      if (dailyByDate.containsKey(date)) {
        // 日毎便ダイヤデータに運行年月日=処理対象日のものが存在する場合、更新用Entity
        DailyBusDaiyasEntity entity =
            new DailyBusDaiyasEntity(busServiceDto.getBusServiceId(), dailyByDate.get(date));
        busDaiyasEntities.add(entity);
      } else {
        // 日毎便ダイヤデータに運行年月日=処理対象日のものが存在しない場合、登録用Entity
        DailyBusDaiyasEntity entity =
            new DailyBusDaiyasEntity(busServiceDto.getBusServiceId(), date,
                busServiceDto.getVehicleId());
        busDaiyasEntities.add(entity);
      }
    }

    // 曜日指定可否区分が「指定可」の場合の運行対象外フラグ設定
    if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
      for (Iterator<DailyBusDaiyasEntity> it = busDaiyasEntities.iterator(); it.hasNext(); ) {
        DailyBusDaiyasEntity entity = it.next();

        // 運行日
        String svcDate = entity.getServiceDate();

        // 運行カレンダー.曜日指定区分が「0：祝日優先」かどうか
        boolean isHolidayPriority = false;
        if (ExObjectUtils.isNotBlank(calendarList)) {
          String weekDesignationType = calendarList.get(0).getWeekDesignationType();
          isHolidayPriority =
              WeekDesignationType.HOLIDAY_PRIORITY.getValue().equals(weekDesignationType);
        }
        // 処理対象日が祝日カレンダーに登録されているかどうか
        boolean isHoliday =
            holidayCalendarMapper.isDatePresent(svcDate, getUserSession().getBusinessCorpId());
        // 運行カレンダー詳細に曜日区分が「10：祝日」のデータが存在する
        boolean hasHolidayDetail =
            calendarDetailsMapper.isDayWeekTypePresent(busServiceDto.getCalendarId(),
                DayWeekType.HOLIDAY.getValue());
        // 運行カレンダー詳細に曜日区分が処理対象日の曜日のデータが存在するかどうか
        boolean hasDowDetail =
            calendarDetailsMapper.isDayWeekTypePresent(busServiceDto.getCalendarId(),
                DayWeekType.getValueByJapanese(ExDateUtils.getJapaneseDayOfWeek(svcDate)));
        // 処理対象日が運行カレンダー除外日設定に含まれるかどうか
        boolean isCanceled = cancelDateSet.contains(svcDate);

        if (isHolidayPriority) {
          if (isHoliday) {
            if (hasHolidayDetail) {
              applyOperationCancelFlag(entity, isCanceled);
            } else {
              retainIfDesignatedOrDelete(it, entity, designatedDateSet);
            }
          } else {
            if (hasDowDetail) {
              applyOperationCancelFlag(entity, isCanceled);
            } else {
              retainIfDesignatedOrDelete(it, entity, designatedDateSet);
            }
          }
        } else {
          if (hasDowDetail) {
            applyOperationCancelFlag(entity, isCanceled);
          } else {
            retainIfDesignatedOrDelete(it, entity, designatedDateSet);
          }
        }

      }
    } else {
      // 曜日指定可否区分が「指定不可」の場合の運行対象外フラグ設定
      for (DailyBusDaiyasEntity entity : busDaiyasEntities) {
        entity.setOperationCancelFlg(OperationCancelFlag.OPERATING.getValue());
      }
    }

    // 登録・更新処理
    for (DailyBusDaiyasEntity entity : busDaiyasEntities) {
      if (ExObjectUtils.isBlank(entity.getDailyBusDaiyaId())) {
        if (dailyBusDaiyasMapper.registerDailyBusDaiya(entity) != 1) {
          throw new CommonDbException("【日毎便ダイヤ再作成】日毎便ダイヤ登録エラー");
        }
      } else {
        if (dailyBusDaiyasMapper.updateDailyBusDaiyaForRebuild(entity) != 1) {
          throw new CommonDbException("【日毎便ダイヤ再作成】日毎便ダイヤ更新エラー");
        }
      }
    }

    // 曜日指定可否区分が「指定不可」の場合、運行指定日以外の日毎便ダイヤは削除する
    if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_DISABLED.getValue())) {
      List<Long> deleteTargets = dailyBusDaiyasMapper.getDailyBusDaiyasByServiceIdAndDateListNotIn(
          busServiceDto.getBusServiceId(), targetDates);
      for (Long id : deleteTargets) {
        DailyBusDaiyasEntity entity = new DailyBusDaiyasEntity(id);
        if (dailyBusDaiyasMapper.deleteDailyBusDaiya(entity) != 1) {
          throw new CommonDbException("【日毎便ダイヤ再作成】日毎便ダイヤ削除エラー");
        }
      }
    }
  }

  /**
   * 運行フラグを設定
   *
   * @param entity 日毎便ダイヤEntity
   * @param isCanceled 対処除外日フラグ
   */
  private void applyOperationCancelFlag(DailyBusDaiyasEntity entity, boolean isCanceled) {
    entity.setOperationCancelFlg(isCanceled
        ? OperationCancelFlag.EXCLUDED.getValue()
        : OperationCancelFlag.OPERATING.getValue());
  }

  /**
   * 指定日でない場合は、新規は作成せず除外、既存はDB削除のうえ除外（指定日は保持・運行扱い）
   *
   * @param it     登録・更新対象オブジェクト
   * @param entity 日毎便ダイヤEntity
   * @param designatedDateSet 運行指定日
   */
  private void retainIfDesignatedOrDelete(Iterator<DailyBusDaiyasEntity> it,
      DailyBusDaiyasEntity entity, Set<String> designatedDateSet) {
    // 運行指定日の場合、更新・登録対象とする
    if (ExObjectUtils.isNotBlank(designatedDateSet)) {
      if (designatedDateSet.contains(entity.getServiceDate())) {
        entity.setOperationCancelFlg(OperationCancelFlag.OPERATING.getValue());
        return;
      }
    }

    // 運行指定日ではない場合、データ作成なしor削除
    if (ExObjectUtils.isBlank(entity.getDailyBusDaiyaId())) {
      it.remove();
    } else {
      if (dailyBusDaiyasMapper.deleteDailyBusDaiya(entity) != 1) {
        throw new CommonDbException("【日毎便ダイヤ再作成】日毎便ダイヤ削除エラー");
      }
      it.remove();
    }
  }

  /**
   * 一覧取得
   *
   * @param form 日毎便ダイヤ検索FORM
   */
  public void setDailyBusDaiyasList(DailyBusDaiyasSearchForm form, ListPager pager) {

    SearchDailyBusDaiyasDto dto = new SearchDailyBusDaiyasDto(form, pager.getStartIndex());
    // セッションから事業者IDを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    dto.setOperatingCompanyId(getUserSession().getOperatingCompanyId());
    dto.setSalesCancelFlg(form.getSearchSalesCancelFlg());

    // 日毎便ダイヤ一覧を取得
    pager.setTotalListSize(dailyBusDaiyasMapper.countDailyBusDaiyas(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(dailyBusDaiyasMapper.getDailyBusDaiyasBySearch(dto));
    }
  }

  /**
   * 日毎便ダイヤを更新する
   *
   * @param form 日毎便ダイヤ詳細FORM
   */
  @Transactional
  public void update(DailyBusDaiyasDetailForm form) {

    DailyBusDaiyasDetailDto beforeData = getDailyBusDaiyasByPk(form);
    DailyBusDaiyasEntity dailyBusDaiyasEntity = new DailyBusDaiyasEntity(form);
    dailyBusDaiyasEntity.setSalesCancelFlg(form.getSalesCancelFlg());
    dailyBusDaiyasEntity.setVehicleId(form.getVehicleId());
    dailyBusDaiyasEntity.setNotification(form.getNotification());

    VehiclesDetailDto temp = vehiclesMapper.getVehiclesByPk(beforeData.getVehicleId(), getUserSession().getBusinessCorpId());
    if (temp != null) {
      beforeData.setVehicleName(temp.getVehicleName());
    }

    // 登録失敗時、エラーとする
    if (dailyBusDaiyasMapper.updateDailyBusDaiya(dailyBusDaiyasEntity) != 1) {
      throw new CommonDbException("【日毎便ダイヤ管理】登録エラー");
    }

    registerSuspendFlagChangeHistory(beforeData, form);
  }

  /**
   * 運行状態変更の操作履歴を登録する
   *
   * @param beforeData 日毎便ダイヤ情報DTO
   * @param form       日毎便ダイヤ詳細FORM
   * @return 件数
   */
  @Transactional
  public void registerSuspendFlagChangeHistory(DailyBusDaiyasDetailDto beforeData,
      DailyBusDaiyasDetailForm form) {

    boolean changedOperation = !Objects.equals(beforeData.getSalesCancelFlg(), form.getSalesCancelFlg());
    String beforeReason = ExObjectUtils.isBlank(beforeData.getNotification()) ? "" : beforeData.getNotification();
    String afterReason = ExObjectUtils.isBlank(form.getNotification()) ? "" : form.getNotification();
    boolean changedReason = !Objects.equals(beforeReason, afterReason);

    String beforeVehicle = ExObjectUtils.isBlank(beforeData.getVehicleName()) ? "" : beforeData.getVehicleName();
    String afterVehicle = ExObjectUtils.isBlank(form.getVehicleName()) ? "" : form.getVehicleName();
    boolean changedVehicle = !Objects.equals(beforeVehicle, afterVehicle);

    // 操作内容（変更点の要約）
    List<String> ops = new ArrayList<>();
    if (changedOperation) ops.add("運行状態の変更");
    if (changedReason)    ops.add("運休理由の変更");
    if (changedVehicle)   ops.add("車両名の変更");

    // 備考(変更前)
   List<String> notesBefore = new ArrayList<>();
    if (changedOperation) {
      notesBefore.add(SuspensionDivision.getTextByValue(beforeData.getSalesCancelFlg())); // 0:運行, 1:運休
    }
    if (changedReason && beforeData.getNotification() != null && !beforeData.getNotification().isEmpty()) {
      notesBefore.add(beforeData.getNotification());
    }
    if (changedVehicle && beforeData.getVehicleName() != null && !beforeData.getVehicleName().isEmpty()) {
      notesBefore.add(beforeData.getVehicleName());
    }

    // 備考(変更後)
    List<String> notesAfter = new ArrayList<>();
    if (changedOperation) {
      notesAfter.add(SuspensionDivision.getTextByValue(form.getSalesCancelFlg())); // 0:運行, 1:運休
    }
    if (changedReason && form.getNotification() != null && !form.getNotification().isEmpty()) {
      notesAfter.add(form.getNotification());
    }
    if (changedVehicle && form.getVehicleName() != null && !form.getVehicleName().isEmpty()) {
      notesAfter.add(form.getVehicleName());
    }

    StringBuilder operationText = new StringBuilder(String.join("、", ops));
    if (!notesBefore.isEmpty()) {
      operationText.append(",備考(変更前): ").append(String.join(", ", notesBefore));
    }
    if (!notesAfter.isEmpty()) {
      operationText.append(",備考(変更後): ").append(String.join(", ", notesAfter));
    }

    // 変更がない場合は登録しない
    if (ops.isEmpty() && operationText.isEmpty()) {
      return;
    }

    DailyDaiyaOperationHistoryDto dto = new DailyDaiyaOperationHistoryDto();
    dto.setDailyBusDaiyaId(form.getDailyBusDaiyaId());

    String operationSummary = String.join("、", ops);
    dto.setOperation(operationSummary);

    String beforeSummary = String.join(",", notesBefore);
    dto.setBefore(beforeSummary);

    String afterSummary = String.join(",", notesAfter);
    dto.setAfter(afterSummary);

    dto.setCreatedUser(getUserSession().getUserName());
    dto.setUpdatedUser(getUserSession().getUserName());
    dailyBusDaiyasMapper.insertDailyDaiyaOperationHistory(dto);
  }

  /**
   * 日毎便ダイヤ情報取得
   *
   * @param form 日毎便ダイヤ詳細FORM
   * @return 日毎便ダイヤ情報DTO
   */
  public DailyBusDaiyasDetailDto getDailyBusDaiyasByPk(DailyBusDaiyasDetailForm form) {
    DailyBusDaiyasEntity dailyBusDaiyasEntity = new DailyBusDaiyasEntity(form);
    //前画面の選択行の日毎便ダイヤID
    dailyBusDaiyasEntity.setDailyBusDaiyaId(form.getDailyBusDaiyaId());
    //前画面の選択行の往復区分
    dailyBusDaiyasEntity.setLineDetailServiceDivision(form.getLineDetailServiceDivision());
    //セッション.事業者ID
    dailyBusDaiyasEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());
    //セッション.運行会社ID
    dailyBusDaiyasEntity.setOperatingCompanyId(getUserSession().getOperatingCompanyId());
    return dailyBusDaiyasMapper.getDailyBusDaiyasByPk(dailyBusDaiyasEntity);
  }

  /**
   * 日毎便ダイヤ存在確認
   *
   * @param form 日毎便ダイヤ詳細FORM
   * @return 存在すれば true、存在しなければ false
   */
  public boolean existsDailyBusDaiyasByPk(DailyBusDaiyasDetailForm form) {
    DailyBusDaiyasEntity dailyBusDaiyasEntity = new DailyBusDaiyasEntity(form);
    // 前画面の選択行の日毎便ダイヤID
    dailyBusDaiyasEntity.setDailyBusDaiyaId(form.getDailyBusDaiyaId());
    // 前画面の選択行の往復区分
    dailyBusDaiyasEntity.setLineDetailServiceDivision(form.getLineDetailServiceDivision());
    // セッション.事業者ID
    dailyBusDaiyasEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // セッション.運行会社ID
    dailyBusDaiyasEntity.setOperatingCompanyId(getUserSession().getOperatingCompanyId());

    Long cnt = dailyBusDaiyasMapper.existsDailyBusDaiyasByPk(dailyBusDaiyasEntity);
    return cnt != null && cnt > 0;
  }

  /**
   * 登録停留所を取得
   *
   * @param form 日毎便ダイヤ詳細FORM
   */
  public List<DailyBusDaiyasDetailDto> getlineRouteBusStopsList(DailyBusDaiyasDetailForm form) {
    DailyBusDaiyasEntity dailyBusDaiyasEntity = new DailyBusDaiyasEntity(form);
    //前画面の選択行の日毎便ダイヤID
    dailyBusDaiyasEntity.setDailyBusDaiyaId(form.getDailyBusDaiyaId());
    //前画面の選択行の往復区分
    dailyBusDaiyasEntity.setLineDetailServiceDivision(form.getLineDetailServiceDivision());
    //セッション.事業者ID
    dailyBusDaiyasEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());
    //セッション.運行会社ID
    dailyBusDaiyasEntity.setOperatingCompanyId(getUserSession().getOperatingCompanyId());
    return dailyBusDaiyasMapper.getlineRouteBusStopsList(dailyBusDaiyasEntity);
  }

  /**
   * 日毎便ダイヤ操作履歴を取得する
   *
   * @param form 日毎便ダイヤ詳細FORM
   * @return 操作履歴リスト
   */
  public List<DailyDaiyaOperationHistoryDto> getDailyDaiyaOperationHistories(
      DailyBusDaiyasDetailForm form) {
    return dailyBusDaiyasMapper.getDailyDaiyaOperationHistories(form.getDailyBusDaiyaId());
  }

}
