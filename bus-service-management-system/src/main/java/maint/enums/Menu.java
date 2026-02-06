package maint.enums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import maint.dto.UserPermissionDto;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * メニュー
 *
 * @author kang
 */
@Getter
public enum Menu {

  /** システム管理 */
  SYSTEM("システム管理", "01", "SYSTEM000", null, "#", null, "fa fa-cog"),
  /** 権限管理 */
  PERMISSION("権限管理", "02", "SYSTEM001", "SYSTEM000", "/system/permissions/?init=1", SYSTEM, "UI002"),
  /** アカウント管理 */
  ACCOUNT("アカウント管理", "02", "SYSTEM002", "SYSTEM000", "/system/users/?init=1", SYSTEM, "UI003"),

  /** 基本情報 */
  BASE_INFO("基本情報", "01", "BASEINFO000", null, null, null, "null"),
  /** 事業者管理 */
  AGENCY("事業者管理", "02", "BASEINFO001", "BASEINFO000", "/baseInfo/agencies/?init=1", BASE_INFO, "UI004"),
  /** 運行会社管理 */
  OP_COMPANY("運行会社管理", "02", "BASEINFO002", "BASEINFO000", "/baseInfo/operatingCompanies/?init=1", BASE_INFO, "UI005"),
  /** 営業所管理 */
  OFFICE("営業所管理", "02", "BASEINFO003", "BASEINFO000", "/baseInfo/offices/?init=1", BASE_INFO, "UI006"),
//  /** 停留所管理 */
//  STOP("停留所管理", "02", "BASEINFO004", "BASEINFO000", "/baseInfo/busStops/?init=1", BASE_INFO, "UI008"),
//  /** 標柱管理 */
//  MARKER("標柱管理", "02", "BASEINFO005", "BASEINFO000", "/baseInfo/signposts/?init=1", BASE_INFO, "UI009"),
  /** 停留所・標柱管理 */
  STOP_MARKER("停留所・標柱管理", "02", "BASEINFO006", "BASEINFO000", "/baseInfo/busStopSignposts/?init=1", BASE_INFO, "UI050"),

  /** 運行管理 */
  OPERATION("運行管理", "01", "OPERATION000", null, null, null, "null"),
  /** 運行カレンダー管理 */
  OP_CALENDAR("運行カレンダー管理", "02", "OPERATION001", "OPERATION000", "/operation/calendars/?init=1", OPERATION, "UI010"),
  /** 祝日カレンダー管理 */
  HOLIDAY_CALENDAR("祝日カレンダー管理", "02", "OPERATION002", "OPERATION000", "/operation/holidayCalendar/", OPERATION, "UI011"),
  /** 路線管理 */
  LINES("路線管理", "02", "OPERATION003", "OPERATION000", "/operation/lines/?init=1", OPERATION, "UI012"),
  /** 系統管理 */
  SYSTEM_LINE("系統管理", "02", "OPERATION004", "OPERATION000", "/operation/lineRoutes/?init=1", OPERATION, "UI013"),
  /** 便ダイヤ管理 */
  TIMETABLE("便ダイヤ管理", "02", "OPERATION005", "OPERATION000", "/operation/busDaiyas/?init=1", OPERATION, "UI015"),
  /** 運賃管理 */
  FARE("運賃管理", "02", "OPERATION006", "OPERATION000", "/operation/lineCharges/?init=1", OPERATION, "UI018"),
  /** 棒ダイヤ表示 */
  DIAGRAM_VIEW("棒ダイヤ表示", "02", "OPERATION007", "OPERATION000", "/operation/displayDaiyas/", OPERATION, "UI019"),
  /** 車両動態管理 */
  VEHICLE_MOVEMENT("車両動態管理", "02", "OPERATION008", "OPERATION000", "/operation/vehicleTrackings/?init=1", OPERATION, "UI020"),
  /** 停留所情報インポート */
  IMPORT_STOP("停留所情報インポート", "02", "OPERATION009", "OPERATION000", "/operation/lineBusStopsReport/?init=1", OPERATION, "UI025"),
  /** 便情報インポート */
  IMPORT_TRIP("便情報インポート", "02", "OPERATION010", "OPERATION000", "/operation/importBusServices/?init=1", OPERATION, "UI026"),
  /** 運賃情報インポート */
  IMPORT_FARE("運賃情報インポート", "02", "OPERATION011", "OPERATION000","/operation/lineChargesInfoReport/?init=1", OPERATION, "UI027"),

  /** 計画ダイヤ */
  PLAN_DAIYA("計画ダイヤ", "01", "PLANDAIYA000", null, null, null, "null"),
  /** 便ダイヤ改正管理 */
  TIMETABLE_REVISION("便ダイヤ改正管理", "02", "PLANDAIYA001", "PLANDAIYA000","/planDaiya/daiyaRevisions/?init=1", PLAN_DAIYA, "UI014"),
  /** 日毎便ダイヤ管理 */
  DAILY_TIMETABLE("日毎便ダイヤ管理", "02", "PLANDAIYA002", "PLANDAIYA000", "/planDaiya/dailyBusDaiyas/?init=1", PLAN_DAIYA, "UI016"),
  /** 車両管理 */
  VEHICLE("車両管理", "02", "PLANDAIYA003", "PLANDAIYA000", "/planDaiya/vehicles/?init=1", PLAN_DAIYA, "UI007"),
  /** 運賃改定管理 */
  FARE_REVISION("運賃改定管理", "02", "PLANDAIYA004", "PLANDAIYA000", "/planDaiya/chargeRevisions/?init=1", PLAN_DAIYA, "UI017"),

  /** 販売管理 */
  SALES("販売管理", "01", "SALES000", null, null, null, "null"),
  /** 収入情報インポート */
  IMPORT_REVENUE("収入情報インポート", "02", "SALES001", "SALES000", "/sales/importIncomes/?init=1", SALES, "UI024"),

  /** 申請書類出力 */
  DOCUMENT("申請書類出力", "01", "DOCUMENT000", null, null, null, "null"),
  /** キロ程表出力 */
  DISTANCE_REPORT("キロ程表出力", "02", "DOCUMENT001", "DOCUMENT000", "/document/distanceReport/", DOCUMENT, "UI029"),
  /** 停留所付近図出力 */
  STOP_MAP("停留所付近図出力", "02", "DOCUMENT002", "DOCUMENT000", "/document/stopMap/?init=1", DOCUMENT, "UI030"),

  /** 計画（旅行案内） */
  PLAN("計画（旅行案内）", "01", "PLAN000", null, null, null, "null"),
  /** 路線時刻表出力 */
  ROUTE_TIMETABLE("路線時刻表出力", "02", "PLAN001", "PLAN000", "/plan/routeTimetable/?init=1", PLAN, "UI031"),
  /** 運賃表出力 */
  FARE_TABLE("運賃表出力", "02", "PLAN002", "PLAN000", "/plan/fareReport/?init=1", PLAN, "UI039"),

  /** 運行情報 */
  OPE_INFO("運行情報", "01", "OPEINFO000", null, null, null, "null"),
  /** スターフ出力 */
  STAFF_REPORT("スターフ出力", "02", "OPEINFO001", "OPEINFO000", "/opeInfo/staffReport/?init=1", OPE_INFO, "UI032"),

  /** 実績確認 */
  RECORD("実績確認", "01", "RECORD000", null, null, null, "null"),
  /** 運行実績管理 */
  OPERATION_RECORD("運行実績管理", "02", "RECORD001", "RECORD000", "/record/operationRecords/?init=1", RECORD, "UI021"),
  /** 運行回数出力 */
  RUN_COUNT("運行回数出力", "02", "RECORD002", "RECORD000", "/record/operatingReport/?init=1", RECORD, "UI038"),

  /** マスタ出力 */
  EXPORT("マスタ出力", "01", "EXPORT000", null, null, null, "null"),
  /** 一覧表出力 */
  REPORT_LIST("一覧表出力", "02", "EXPORT001", "EXPORT000", "/export/lists/?init=1", EXPORT, "UI033"),

  /** 外部システム連携 */
  INTEGRATE("外部システム連携", "01", "INTEGRATE000", null, null, null, "null"),
  /** GTFS-Schedule */
  GTFS("GTFS-Schedule", "02", "INTEGRATE001", "INTEGRATE000", "/integrate/gtfsReport/?init=1", INTEGRATE, "UI022"),
  ;

  /** 名称 */
  private final String name;
  /** メニュー区分 */
  private final String menuDivision;
  /** メニューコード */
  private final String menuCode;
  /** 親メニューコード */
  private final String parentCode;
  /** URI */
  private final String uri;
  /** 親メニュー */
  private final Menu parentsMenu;
  /** Function (画面ID) */
  private final String function;

  /**
   * コンストラクタ
   *
   * @param name 名称
   * @param menuDivision メニュー区分
   * @param menuCode メニューコード
   * @param parentCode 親メニューコード
   * @param uri URI
   * @param parentsMenu 親メニュー
   * @param function Function（画面ID）
   */
  Menu(String name, String menuDivision, String menuCode, String parentCode, String uri, Menu parentsMenu, String function) {
    this.name = name;
    this.menuDivision = menuDivision;
    this.menuCode = menuCode;
    this.parentCode = parentCode;
    this.uri = uri;
    this.parentsMenu = parentsMenu;
    this.function = function;
  }

  /**
   * 親メニューが存在するかを判定する。
   *
   * @return true:親メニューあり, false:親メニューなし
   */
  public boolean hasParentMenu() {
    return this.parentsMenu != null;
  }

  /**
   * 該当メニューコードのメニュー取得
   *
   * @param menuCode メニューコード
   * @return menuValues
   */
  public static Menu getByMenuCode(String menuCode) {
    if (StringUtils.isBlank(menuCode))
      return null;
    for (Menu menuValues : Menu.values()) {
      if (menuValues.getMenuCode().equals(menuCode)) {
        return menuValues;
      }
    }
    return null;
  }

  /**
   * 第1階層のメニューを返す
   *
   * @return 第1階層のメニュー
   */
  public static Menu[] getTopMenuValues(List<UserPermissionDto> userPermList) {
    List<Menu> topMenuValuesList = new ArrayList<>();
    for (Menu menuValues : Menu.values()) {
      if (menuValues.parentsMenu == null) {
        topMenuValuesList.add(menuValues);
      }
    }
    // menuCodeを基準にMap変換
    Map<String, Integer> permMap = userPermList.stream().collect(
        Collectors.toMap(UserPermissionDto::getParentMenuCode,
            UserPermissionDto::getParentDisplayOrder, (v1, v2) -> v1
        ));

    // topMenuListフィルター（表示順ソート）
    List<Menu> filteredAndSortedMenuList =
        topMenuValuesList.stream().filter(menu -> permMap.containsKey(menu.getMenuCode()))
            .sorted(Comparator.comparingInt(menu -> permMap.get(menu.getMenuCode()))).toList();

    return filteredAndSortedMenuList.toArray(new Menu[filteredAndSortedMenuList.size()]);
  }

  /**
   * 権限に関係なく、すべてのトップメニューを返す
   * @return 第1階層のメニュー
   */
  public static Menu[] getAllTopMenuValues() {
    List<Menu> topMenuList = new ArrayList<>();
    for (Menu menuValue : Menu.values()) {
      if (menuValue.getParentsMenu() == null) { // 親がnull＝トップメニュー
        topMenuList.add(menuValue);
      }
    }
    return topMenuList.toArray(new Menu[0]);
  }

  /**
   * 権限に関係なく、指定メニューの全サブメニューを返す
   * @param parentMenu 親メニュー
   * @return 子メニュー
   */
  public static Menu[] getAllSubMenu(Menu parentMenu) {
    List<Menu> subMenuList = new ArrayList<>();
    for (Menu menuValue : Menu.values()) {
      if (menuValue.getParentsMenu() == parentMenu) {
        subMenuList.add(menuValue);
      }
    }
    return subMenuList.toArray(new Menu[0]);
  }

  /**
   * 親メニューの子メニューを返す
   *
   * @param targetMenu 親メニュー
   * @return 子メニュー
   */
  public static Menu[] getSubMenu(Menu targetMenu, List<UserPermissionDto> userPermList) {

    List<Menu> subMenuList = new ArrayList<>();

    for (Menu menuValue : Menu.values()) {
      if (menuValue.getParentsMenu() == targetMenu) {
        if (!subMenuList.contains(menuValue)) {
          subMenuList.add(menuValue);
        }
      }
    }

    // menuCodeを基準にMap変換
    Map<String, Integer> permMap = userPermList.stream().collect(
        Collectors.toMap(UserPermissionDto::getMenuCode, UserPermissionDto::getDisplayOrder,
            (v1, v2) -> v1
        ));

    // subMenuListフィルター（表示順ソート）
    List<Menu> filteredAndSortedMenuList =
        subMenuList.stream().filter(menu -> permMap.containsKey(menu.getMenuCode()))
            .sorted(Comparator.comparingInt(menu -> permMap.get(menu.getMenuCode()))).toList();

    return filteredAndSortedMenuList.toArray(new Menu[filteredAndSortedMenuList.size()]);
  }

  /**
   * 全ての子メニューを返す
   *
   * @return 全ての子メニュー
   */
  public static Menu[] getAllSubMenu() {
    List<Menu> subMenuList = new ArrayList<>();

    for (Menu menuValue : Menu.values()) {
      if (menuValue.hasParentMenu()) {
        subMenuList.add(menuValue);
      }
    }
    return subMenuList.toArray(new Menu[subMenuList.size()]);
  }

  /**
   * SideMenu Active設定(TOPMENU)
   *
   * @param topMenu Menu
   * @param url     url
   * @return true: 選択TOPMENU, false: 未選択TOPMENU
   */
  public static boolean isActiveTopMenu(Menu topMenu, String url) {
    UserSession session = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    for (Menu sub : getSubMenu(topMenu, session.getUserPermList())) {
      if (isActiveSubMenu(sub, url)) {
        return true;
      }
    }
    return false;
  }

  /**
   * SideMenu Active設定(SUBMENU)
   *
   * @param subMenu Menu
   * @param url     url
   * @return true: 選択SUBMENU, false: 未選択SUBMENU
   */
  public static boolean isActiveSubMenu(Menu subMenu, String url) {
    return ExObjectUtils.isNotBlank(subMenu.uri) && url.startsWith(subMenu.uri.split("\\?")[0]);
  }

}
