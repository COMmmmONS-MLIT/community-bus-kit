package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.holidayCalendars.DailyBusDaiyaDto;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;
import maint.form.dailyBusDaiyas.DailyBusDaiyasDetailForm;

/**
 * 日毎便ダイヤEntity
 *
 * @author jeon
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyBusDaiyasEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = 6427958531L;

  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 便ID */
  private Long busServiceId;
  /** 運行年月日 */
  private String serviceDate;
  /** 運行年月日の00:00 */
  private Long serviceStartTime;
  /** 運行年月日の23:59 */
  private Long serviceEndTime;
  /** 運休フラグ */
  private Integer salesCancelFlg;
  /** 運行対象外フラグ */
  private Integer operationCancelFlg;
  /** 車両ID */
  private Long vehicleId;
  /** 車両名 */
  private String vehicleName;
  /** お知らせ */
  private String notification;
  /** ユーザーセッション */
  private UserSession session;
  /** ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 日毎便ダイヤ開始日 */
  private String serviceStartDate;
  /** 日毎便ダイヤ終了日 */
  private String serviceEndDate;
  /** 路線ID */
  private Long rosenId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 始発出発時刻 */
  private String busStopDepartureTime;
  /** 往復区分 */
  private Integer lineDetailServiceDivision;
  /** 便番号 */
  private String busServiceTripId;

  /**
   * コンストラクタ
   *
   * @param session ユーザーセッション
   * @param dailyBusDaiyaId 日毎便ダイヤID
   */
  public DailyBusDaiyasEntity(UserSession session, Long dailyBusDaiyaId) {
    this.session = session;
    this.dailyBusDaiyaId = dailyBusDaiyaId;
  }

  /**
   * コンストラクタ（登録用）
   *
   * @param busServiceId 便ID
   * @param serviceDate 運行年月日
   * @param vehicleId 車両ID
   */
  public DailyBusDaiyasEntity(Long busServiceId, String serviceDate, Long vehicleId) {
    this.busServiceId = busServiceId;
    this.serviceDate = serviceDate;
    this.salesCancelFlg = 0;
    this.vehicleId = vehicleId;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ（更新用）
   *
   * @param busServiceId 便ID
   * @param dto 日毎便ダイヤDTO
   */
  public DailyBusDaiyasEntity(Long busServiceId, DailyBusDaiyaDto dto) {
    this.dailyBusDaiyaId = dto.getDailyBusDaiyaId();
    this.busServiceId = busServiceId;
    this.serviceDate = dto.getServiceDate();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ（削除用）
   *
   * @param dailyBusDaiyaId 日毎便ダイヤID
   */
  public DailyBusDaiyasEntity(Long dailyBusDaiyaId) {
    this.dailyBusDaiyaId = dailyBusDaiyaId;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param form 日毎便ダイヤ詳細FORM
   */
  public DailyBusDaiyasEntity(DailyBusDaiyasDetailForm form) {
    if (form == null) return;
    this.rosenId = form.getRosenId();
    this.dailyBusDaiyaId = form.getDailyBusDaiyaId();
    this.rosenName = form.getRosenName();
    this.businessCorpId = form.getBusinessCorpId();
    this.vehicleId = form.getVehicleId();
    this.vehicleName = form.getVehicleName();
    this.operatingCompanyId = form.getOperatingCompanyId();
    this.serviceStartDate = form.getServiceDate();
    this.serviceEndDate = form.getServiceDate();
    this.serviceStartTime = form.getServiceStartTime();
    this.serviceEndTime = form.getServiceEndTime();
    this.busServiceTripId = form.getBusServiceTripId();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
