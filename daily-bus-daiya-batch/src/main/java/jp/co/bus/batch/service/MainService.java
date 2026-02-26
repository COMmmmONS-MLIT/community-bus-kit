package jp.co.bus.batch.service;

import static jp.co.bus.batch.utils.date.ExDateUtils.getCurrentDate;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jp.co.bus.batch.dto.BusServiceInfoDto;
import jp.co.bus.batch.dto.CalendarInfoDto;
import jp.co.bus.batch.dto.DailyBusDaiyaDto;
import jp.co.bus.batch.dto.DailyBusDaiyaUpsertDto;
import jp.co.bus.batch.enums.DayWeekType;
import jp.co.bus.batch.enums.OperationCancelFlag;
import jp.co.bus.batch.enums.WeekDesignatableFlag;
import jp.co.bus.batch.enums.WeekDesignationType;
import jp.co.bus.batch.exception.DatabaseFatalException;
import jp.co.bus.batch.utils.ExObjectUtils;
import jp.co.bus.batch.utils.date.ExDateUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.bus.batch.dao.CommonDao;
import jp.co.bus.batch.dao.MainDao;
import jp.co.bus.batch.factory.SessionFactory;

/**
 * Main service
 *
 * @author hr.kim
 */
public class MainService {

  /** log */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** SQL session */
  private SqlSession sqlSession;

  /** CommonDao */
  private CommonDao commonDao = new CommonDao();

  /** MainDao */
  private MainDao mainDao = new MainDao();

  /**
   * 処理実行
   */
  public void execute() {

    // 便情報取得
    sqlSession = SessionFactory.getInstance().getSession();
    List<BusServiceInfoDto> busServiceList = new ArrayList<BusServiceInfoDto>();
    try {
      busServiceList = this.mainDao.getBusServices(sqlSession);
    } catch (Exception e) {
      throw new DatabaseFatalException("便情報取得エラー", e);
    } finally {
      sqlSession.close();
    }

    // 処理対象がない場合、終了
    if (ExObjectUtils.isBlank(busServiceList)) {
      LOGGER.info("対象便件数： 0件");
      return;
    }

    // 日毎便ダイヤデータ登録処理
    for (BusServiceInfoDto busServiceDto : busServiceList) {

      sqlSession = SessionFactory.getInstance().getSession();

      try {
        // 運行カレンダーデータ取得
        List<CalendarInfoDto> calendarList =
            this.mainDao.getCalendarsById(sqlSession, busServiceDto);

        // 便情報から運行カレンダーが取得できない場合、処理をスキップする。
        if (ExObjectUtils.isBlank(calendarList)) {
          continue;
        }

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
              this.mainDao.getFutureCancelDatesByCalendarId(sqlSession, busServiceDto);
          cancelDateSet = cancelDates == null ? Collections.emptySet() : new HashSet<>(cancelDates);
        }

        // 運行カレンダー指定日設定データ取得
        designatedDates =
            this.mainDao.getFutureDesignatedDatesByCalendarId(sqlSession, busServiceDto);
        designatedDateSet =
            designatedDates == null ? Collections.emptySet() : new HashSet<>(designatedDates);

        // 曜日指定可否区分が「指定不可」かつ、運行指定日（未来日）が存在しない場合、処理をスキップする。
        if (weekDesignatableFlag.equals(
            WeekDesignatableFlag.DESIGNATION_DISABLED.getValue()) && ExObjectUtils.isBlank(
            designatedDates)) {
          continue;
        }

        // 処理対象日
        List<String> targetDates = new ArrayList<>();
        // 更新対象の日毎便ダイヤデータ
        Map<String, DailyBusDaiyaDto> dailyByDate = new HashMap<>();

        // 処理対象日、更新対象の日毎便ダイヤデータ取得（曜日指定可否区分が「指定可」の場合）
        if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
          String startDate;
          String endDate;
          // 開始日：便ダイヤ改正開始日。過去日の場合はシステム日付
          if (ExDateUtils.compareLocalDate(busServiceDto.getServiceStartDate(), getCurrentDate())
              < 0) {
            startDate = getCurrentDate();
          } else {
            startDate = busServiceDto.getServiceStartDate();
          }
          // 終了日：便ダイヤ改正終了日とシステム日付+2か月の近い方
          if (ExDateUtils.compareLocalDate(busServiceDto.getServiceEndDate(),
              ExDateUtils.addMonthsToDate(getCurrentDate(), 2)) < 0) {
            endDate = busServiceDto.getServiceEndDate();
          } else {
            endDate = ExDateUtils.addMonthsToDate(getCurrentDate(), 2);
          }
          targetDates = ExDateUtils.listDatesInclusive(startDate, endDate);

          // 更新対象の日毎便ダイヤデータ取得
          List<DailyBusDaiyaDto> DailyBusDaiyaList =
              this.mainDao.getDailyBusDaiyasByBusServiceId(sqlSession,
                  busServiceDto.getBusServiceId(), startDate, endDate);
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
              this.mainDao.getDailyBusDaiyasByServiceIdAndDateList(sqlSession,
                  busServiceDto.getBusServiceId(), targetDates);
          if (ExObjectUtils.isNotBlank(DailyBusDaiyaList)) {
            for (DailyBusDaiyaDto d : DailyBusDaiyaList) {
              if (ExObjectUtils.isNotBlank(d) && ExObjectUtils.isNotBlank(d.getServiceDate())) {
                dailyByDate.put(d.getServiceDate(), d);
              }
            }
          }
        }

        // 日毎便ダイヤデータの登録・更新用Entity作成（対象日の1日毎に繰り返す）
        List<DailyBusDaiyaUpsertDto> dailyBusDaiyaUpsertDtoList = new ArrayList<>();
        for (String date : targetDates) {
          if (dailyByDate.containsKey(date)) {
            // 日毎便ダイヤデータに運行年月日=処理対象日のものが存在する場合、更新用Entity
            DailyBusDaiyaUpsertDto dto =
                new DailyBusDaiyaUpsertDto(busServiceDto.getBusServiceId(), dailyByDate.get(date));
            dailyBusDaiyaUpsertDtoList.add(dto);
          } else {
            // 日毎便ダイヤデータに運行年月日=処理対象日のものが存在しない場合、登録用Entity
            DailyBusDaiyaUpsertDto dto =
                new DailyBusDaiyaUpsertDto(busServiceDto.getBusServiceId(), date,
                    busServiceDto.getVehicleId());
            dailyBusDaiyaUpsertDtoList.add(dto);
          }
        }

        // 曜日指定可否区分が「指定可」の場合の運行対象外フラグ設定
        if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
          for (Iterator<DailyBusDaiyaUpsertDto> it =
               dailyBusDaiyaUpsertDtoList.iterator(); it.hasNext(); ) {
            DailyBusDaiyaUpsertDto dto = it.next();

            // 運行日
            String svcDate = dto.getServiceDate();

            // 運行カレンダー.曜日指定区分が「0：祝日優先」か
            boolean isHolidayPriority = false;
            if (ExObjectUtils.isNotBlank(calendarList)) {
              String weekDesignationType = calendarList.get(0).getWeekDesignationType();
              isHolidayPriority =
                  WeekDesignationType.HOLIDAY_PRIORITY.getValue().equals(weekDesignationType);
            }
            // 処理対象日が祝日カレンダーに登録されているか
            boolean isHoliday =
                this.mainDao.isDatePresent(sqlSession, svcDate, busServiceDto.getBusinessCorpId());
            // 運行カレンダー詳細に曜日区分が「10：祝日」のデータが存在する
            boolean hasHolidayDetail =
                this.mainDao.isDayWeekTypePresent(sqlSession, busServiceDto.getCalendarId(),
                    DayWeekType.HOLIDAY.getValue());
            // 運行カレンダー詳細に曜日区分が処理対象日の曜日のデータが存在するか
            boolean hasDowDetail =
                this.mainDao.isDayWeekTypePresent(sqlSession, busServiceDto.getCalendarId(),
                    DayWeekType.getValueByJapanese(ExDateUtils.getJapaneseDayOfWeek(svcDate)));
            // 処理対象日が運行カレンダー除外日設定に含まれるか
            boolean isCanceled = cancelDateSet.contains(svcDate);

            if (isHolidayPriority) {
              if (isHoliday) {
                if (hasHolidayDetail) {
                  applyOperationCancelFlag(dto, isCanceled);
                } else {
                  retainIfDesignatedOrDelete(it, dto, designatedDateSet);
                }
              } else {
                if (hasDowDetail) {
                  applyOperationCancelFlag(dto, isCanceled);
                } else {
                  retainIfDesignatedOrDelete(it, dto, designatedDateSet);
                }
              }
            } else {
              if (hasDowDetail) {
                applyOperationCancelFlag(dto, isCanceled);
              } else {
                retainIfDesignatedOrDelete(it, dto, designatedDateSet);
              }
            }
          }
        } else {
          // 曜日指定可否区分が「指定不可」の場合の運行対象外フラグ設定
          for (DailyBusDaiyaUpsertDto dto : dailyBusDaiyaUpsertDtoList) {
            dto.setOperationCancelFlg(OperationCancelFlag.OPERATING.getValue());
          }
        }

        // 登録・更新処理
        for (DailyBusDaiyaUpsertDto dto : dailyBusDaiyaUpsertDtoList) {
          if (ExObjectUtils.isBlank(dto.getDailyBusDaiyaId())) {
            this.mainDao.insertDailyBusDaiya(sqlSession, dto);
          } else {
            this.mainDao.updateDailyBusDaiya(sqlSession, dto);
          }
        }

        // 曜日指定可否区分が「指定不可」の場合、運行指定日以外の日毎便ダイヤは削除する
        if (weekDesignatableFlag.equals(WeekDesignatableFlag.DESIGNATION_DISABLED.getValue())) {
          List<Long> deleteTargets =
              this.mainDao.getDailyBusDaiyasByServiceIdAndDateListNotIn(sqlSession,
                  busServiceDto.getBusServiceId(), targetDates);
          for (Long id : deleteTargets) {
            DailyBusDaiyaUpsertDto dto = new DailyBusDaiyaUpsertDto(id);
            this.mainDao.deleteDailyBusDaiya(sqlSession, dto);
          }
        }

        commonDao.commit(sqlSession);
      } catch (Exception e) {
        commonDao.rollback(sqlSession);
        LOGGER.error("日毎便ダイヤ登録エラー 便ID：" + busServiceDto.getBusServiceId(), e);
      } finally {
        sqlSession.close();
      }

    }

  }

  /**
   * 運行フラグを設定
   *
   * @param dto 日毎便ダイヤ更新・登録用DTO
   * @param isCanceled 対処除外日フラグ
   */
  private void applyOperationCancelFlag(DailyBusDaiyaUpsertDto dto, boolean isCanceled) {
    dto.setOperationCancelFlg(isCanceled
        ? OperationCancelFlag.EXCLUDED.getValue()
        : OperationCancelFlag.OPERATING.getValue());
  }

  /**
   * 新規はリストから除外、既存はDB削除してから除外
   *
   * @param it 登録・更新対象オブジェクト
   * @param dto 日毎便ダイヤ更新・登録用DTO
   * @param designatedDateSet 運行指定日
   */
  private void retainIfDesignatedOrDelete(Iterator<DailyBusDaiyaUpsertDto> it, DailyBusDaiyaUpsertDto dto,
      Set<String> designatedDateSet) {
    // 運行指定日の場合、更新・登録対象とする
    if (ExObjectUtils.isNotBlank(designatedDateSet)) {
      if (designatedDateSet.contains(dto.getServiceDate())) {
        dto.setOperationCancelFlg(OperationCancelFlag.OPERATING.getValue());
        return;
      }
    }

    if (ExObjectUtils.isBlank(dto.getDailyBusDaiyaId())) {
      it.remove();
    } else {
      this.mainDao.deleteDailyBusDaiya(sqlSession, dto);
      it.remove();
    }
  }

}
