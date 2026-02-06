package maint.service.operation;

import static maint.utils.date.ExDateUtils.getCurrentDate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.dto.operation.holidayCalendars.HolidayCalendarCsvDto;
import maint.dto.operation.holidayCalendars.HolidayCalendarInfoDto;
import maint.exception.CommonDbException;
import maint.mapper.BusServicesMapper;
import maint.mapper.HolidayCalendarMapper;
import maint.message.HolidayCalendarsMessage;
import maint.service.BaseService;
import maint.service.planDaiya.DailyBusDaiyasService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 祝日カレンダーSERVICE
 *
 * @author g.ko
 */

@Service
public class HolidayCalendarService extends BaseService {

  /** 月配列 (先月~2ヶ月後) */
  private final String[] MONTH_ARRAY = {"lastMonth", "currentMonth", "nextMonth", "twoMonthsLater"};

  /** 祝日カレンダーMAPPER */
  @Autowired
  private HolidayCalendarMapper holidayCalendarMapper;

  /** 便MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;

  /**
   * 祝日カレンダー取得(指定した日付を基準)
   *
   * @param targetDate 対象日付
   * @return 祝日カレンダーMAP
   */
  public Map<String, List<HolidayCalendarInfoDto>> getHolidayCalendar(String targetDate) {

    Map<String, List<HolidayCalendarInfoDto>> resultMap =
        new HashMap<String, List<HolidayCalendarInfoDto>>();

    // 月別Map
    Map<String, String> monthMap = new HashMap<String, String>();

    // DB取得情報
    String[] targetDateFromTo = targetDate.trim().split("~");
    List<HolidayCalendarInfoDto> calendarInfoDtoList =
        holidayCalendarMapper.getHolidayCalendarByTargetDate(targetDateFromTo[0],
            getUserSession().getBusinessCorpId());

    LocalDateTime ldt = LocalDateTime.now();
    // 月を設定
    String month = targetDate.substring(5, 7);
    if (ExObjectUtils.isNotBlank(month)) {
      ldt = ldt.withMonth(Integer.parseInt(month));
    }

    // 月別Map設定
    for (String sMonth : MONTH_ARRAY) {
      ldt = sMonth.equals(MONTH_ARRAY[0]) ? ldt.minusMonths(1) : ldt.plusMonths(1);
      monthMap.put(sMonth, StringUtils.leftPad(String.valueOf(ldt.getMonthValue()), 2, "0"));
    }

    // 月別Mapに祝日設定
    for (String sMonth : MONTH_ARRAY) {
      // リストに設定
      resultMap.put(sMonth, calendarInfoDtoList.stream()
          .filter(e -> e.getHolidayDate().substring(5, 7).equals(monthMap.get(sMonth)))
          .collect(Collectors.toList()));
    }

    return resultMap;
  }

  /**
   * 祝日カレンダー登録
   *
   * @param file CSVファイル
   * @throws IOException IOException
   */
  @Transactional
  public void register(MultipartFile file) throws IOException {

    // 祝日カレンダー削除
    holidayCalendarMapper.deleteHoliday(getUserSession());

    // ファイルからデータを取得
    List<HolidayCalendarCsvDto> dtoList = parseHolidayCsv(file);
    // 登録対象データ（未来日）を取得
    List<HolidayCalendarCsvDto> dtoListForRegister = filterFutureHolidays(dtoList);
    // 祝日カレンダー登録
    if (ExObjectUtils.isNotBlank(dtoListForRegister)) {
      for (HolidayCalendarCsvDto dto : dtoListForRegister) {
        if (holidayCalendarMapper.registerHoliday(dto) != 1) {
          throw new CommonDbException("【祝日カレンダー管理】祝日カレンダー登録エラー");
        }
      }
    }

    // 日毎便ダイヤの再作成
    List<BusServiceInfoDto> busServiceList =
        busServicesMapper.getBusServicesByBusinessCorpId(getUserSession().getBusinessCorpId());
    if (ExObjectUtils.isNotBlank(busServiceList)) {
      for (BusServiceInfoDto busServiceDto : busServiceList) {
        dailyBusDaiyasService.regenerateDailyBusDaiya(busServiceDto);
      }
    }

  }

  /**
   * ファイル入力チェック（UTF-8 / SJIS 自動判別）
   *
   * @param file CSVファイル
   * @return エラーメッセージリスト
   */
  public List<String> validateHolidayCsv(MultipartFile file) {
    List<String> errors = new ArrayList<>();

    // ファイルのフォーマットチェック（CSV）
    String filename = file.getOriginalFilename();
    String contentType = file.getContentType();
    boolean extOk = ExObjectUtils.isNotBlank(filename) && filename.toLowerCase().endsWith(".csv");
    boolean mimeOk = ExObjectUtils.isBlank(contentType) || contentType.equalsIgnoreCase("text/csv")
        || contentType.equalsIgnoreCase("application/vnd.ms-excel") || contentType.equalsIgnoreCase(
        "application/csv") || contentType.equalsIgnoreCase("text/plain");
    if (!extOk || !mimeOk) {
      errors.add(getMessage(HolidayCalendarsMessage.ERROR_FILE_FORMAT.getMessage()));
      return errors;
    }
    if (file.isEmpty()) {
      errors.add(getMessage(HolidayCalendarsMessage.ERROR_EMPTY_FILE.getMessage()));
      return errors;
    }

    // 共通CSV設定
    CSVFormat format =
        CSVFormat.DEFAULT.builder().setTrim(true).setIgnoreEmptyLines(true).setHeader().build();

    // 想定ヘッダー
    final String expectedH0 = getMessage(HolidayCalendarsMessage.HEADER_FIRST.getMessage());
    final String expectedH1 = getMessage(HolidayCalendarsMessage.HEADER_SECOND.getMessage());

    // 日付フォーマット
    DateTimeFormatter IN =
        DateTimeFormatter.ofPattern("uuuu/M/d").withResolverStyle(ResolverStyle.STRICT);

    try {
      byte[] data = file.getBytes();

      // UTF-8 → MS932(=Windows-31J) → Shift_JIS
      List<Charset> candidates = Arrays.asList(StandardCharsets.UTF_8, Charset.forName("MS932"),
          Charset.forName("Windows-31J"), Charset.forName("Shift_JIS"));

      Charset chosen = null;
      List<CSVRecord> records = null;
      List<String> header = null;

      // エンコーディング自動判別（ヘッダー一致で判定）
      for (Charset cs : candidates) {
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(data), cs);
            CSVParser parser = format.parse(reader)) {
          header = parser.getHeaderNames();
          String h0 = stripBom(!header.isEmpty() ? header.get(0) : "");
          String h1 = header.size() > 1 ? header.get(1) : "";
          if (expectedH0.equals(h0) && expectedH1.equals(h1)) {
            chosen = cs;
            records = parser.getRecords();
            break;
          }
        } catch (Exception ignore) {
        }
      }

      // どの候補でもヘッダー一致しない場合は UTF-8 で一旦パースしてエラー扱いにする
      if (ExObjectUtils.isBlank(chosen)) {
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(data),
            StandardCharsets.UTF_8); CSVParser parser = format.parse(reader)) {
          header = parser.getHeaderNames();
          String h0 = stripBom(!header.isEmpty() ? header.get(0) : "");
          String h1 = header.size() > 1 ? header.get(1) : "";
          errors.add(getMessage(HolidayCalendarsMessage.ERROR_HEADER.getMessage()));
          records = parser.getRecords();
        }
      }

      // データの検証
      if (ExObjectUtils.isNotBlank(records)) {
        for (CSVRecord rec : records) {
          long row = rec.getRecordNumber();
          int cols = rec.size();
          if (cols != 2) {
            errors.add(row + getMessage(HolidayCalendarsMessage.ERROR_COLUMN_COUNT.getMessage()));
            continue;
          }

          String dateStr = rec.get(0) != null ? rec.get(0).trim() : "";
          dateStr = dateStr.replace('-', '/').replace('.', '/');

          try {
            LocalDate.parse(dateStr, IN);
          } catch (DateTimeParseException e) {
            errors.add(row + getMessage(HolidayCalendarsMessage.ERROR_DATE_FORMAT.getMessage()));
          }
        }
      }
    } catch (Exception e) {
      errors.add(getMessage(HolidayCalendarsMessage.ERROR_INPUT_FILE.getMessage()));
    }

    return errors;
  }

  private String stripBom(String s) {
    if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
      return s.substring(1);
    }
    return s;
  }

  /**
   * CSVファイルから祝日カレンダーデータを取得
   *
   * @param file CSVファイル
   * @return 祝日カレンダーDTO（CSV用）リスト
   */
  public List<HolidayCalendarCsvDto> parseHolidayCsv(MultipartFile file) throws IOException {
    Charset charset = StandardCharsets.UTF_8;
    List<HolidayCalendarCsvDto> rows = new ArrayList<>();

    CSVFormat format =
        CSVFormat.DEFAULT.builder().setTrim(true).setIgnoreEmptyLines(true).setHeader().build();

    final DateTimeFormatter IN = DateTimeFormatter.ofPattern("uuuu/M/d");
    final DateTimeFormatter OUT = DateTimeFormatter.ISO_LOCAL_DATE;

    try (Reader reader = new InputStreamReader(file.getInputStream(), charset);
        CSVParser parser = format.parse(reader)) {

      for (CSVRecord rec : parser) {
        String holidayDateRaw = rec.size() > 0 ? rec.get(0) : "";
        String holidayName = rec.size() > 1 ? rec.get(1) : "";

        String normalizedDate = "";
        if (holidayDateRaw != null && !holidayDateRaw.isBlank()) {
          String s = holidayDateRaw.trim();
          try {
            s = s.replace('.', '/').replace('-', '/');
            LocalDate d = LocalDate.parse(s, IN);
            normalizedDate = d.format(OUT);
          } catch (DateTimeParseException e) {
            normalizedDate = holidayDateRaw.trim().replace('/', '-');
            LOGGER.warn("CSV日付の正規化に失敗: {}", holidayDateRaw);
          }
        }

        HolidayCalendarCsvDto dto = new HolidayCalendarCsvDto(getUserSession());
        dto.setHolidayDate(normalizedDate);
        dto.setHolidayName(holidayName != null ? holidayName.trim() : "");
        rows.add(dto);
      }
    }
    return rows;
  }

  /**
   * 処理対象データ（未来日）を取得
   *
   * @param dtoList 祝日カレンダーDTO（CSV用）リスト
   * @return 祝日カレンダーDTO（CSV用）リスト
   */
  public List<HolidayCalendarCsvDto> filterFutureHolidays(List<HolidayCalendarCsvDto> dtoList) {
    String today = getCurrentDate();
    return dtoList.stream().filter(Objects::nonNull).filter(dto -> {
      String date = dto.getHolidayDate();
      if (ExObjectUtils.isBlank(date)) {
        return false;
      }
      try {
        return ExDateUtils.compareLocalDate(date, today) > 0;
      } catch (DateTimeParseException e) {
        LOGGER.warn("不正な祝日: {}", date);
        return false;
      }
    }).toList();
  }

}
