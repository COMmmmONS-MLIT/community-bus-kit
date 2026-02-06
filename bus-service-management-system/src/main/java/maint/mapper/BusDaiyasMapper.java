package maint.mapper;

import maint.dto.operation.busDaiyas.*;
import maint.dto.planDaiya.dailyBusDaiyas.SearchDailyBusDaiyasDto;
import maint.dto.operation.importBusServices.ImportBusServicesDetailInsertDto;
import maint.entity.BusDaiyasEntity;
import maint.form.busDaiyas.BusDaiyasDetailForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 便ダイヤMAPPER
 *
 * @author jeon
 */
@Mapper
public interface BusDaiyasMapper {

  /**
   * 便ダイヤ一覧取得（検索条件＋ページング）
   *
   * @param dto 検索条件DTO（事業者ID、路線ID、系統ID、改正ID、期間、ページング情報 等）
   * @return 便ダイヤ情報検索結果一覧
   */
  List<BusDaiyasDetailDto> getBusDaiyasBySearch(SearchBusDaiyasDto dto);

  /**
   * 便ダイヤ一覧件数取得（検索条件に一致する総件数）
   *
   * @param dto 検索条件DTO
   * @return 総件数
   */
  Long countBusDaiyas(SearchBusDaiyasDto dto);

  /**
   * 便ダイヤ情報取得（PK相当の条件で1件取得）
   *
   * @param form 便ダイヤ詳細FORM（識別用の条件を保持）
   * @return 便ダイヤ情報DTO（該当がない場合はnull）
   */
  BusDaiyasDetailDto getBusDaiyasByPk(BusDaiyasDetailForm form);

  /**
   * 期間ラベル（過去分）一覧取得
   *
   * @param dto 検索条件DTO
   * @return 期間ラベル一覧
   */
  List<BusDaiyasOptionDto> getBusDaiyasOptionList(SearchBusDaiyasDto dto);

  /**
   * 便ダイヤ登録ダウンロード用一覧取得
   *
   * @param dto 日毎便ダイヤ一覧DTO
   * @return 便DTOリスト
   */
  List<BusServicesOptionDto> getBusServicesForSelectOption(SearchDailyBusDaiyasDto dto);

  /**
   * 便ダイヤ明細 登録
   *
   * @param entity 便ダイヤ明細Entity
   * @return 登録件数
   */
  int insertTripDetail(BusDaiyasEntity entity);

  /**
   * 指定された便ダイヤIDに基づいて便明細情報を更新します。
   *
   * @param entity 便ダイヤ明細Entity
   * @return 更新件数
   */
  int updateTripDetailById(BusDaiyasEntity entity);

  /**
   * 便ダイヤ明細データを一括登録します。
   *
   * @param list 登録対象の明細DTO一覧
   * @return 登録件数
   */
  int insertTripDetails(@Param("list") List<ImportBusServicesDetailInsertDto> list);

  /**
   * 系統selectBoxに使用する情報を取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 便ダイヤ変更画面の系統selectBox情報取得用DTO一覧
   */
  List<BusDaiyasLineRouteOptionDto> getBusDaiyasLineRouteOption(BusDaiyasDetailForm form);

  /**
   * 運行カレンダーselectBoxに使用する情報を取得
   *
   * @param dto 便ダイヤ検索情報
   * @return 便ダイヤ変更画面の運行カレンダーselectBox情報取得用DTO一覧
   */
  List<BusDaiyasCalendarOptionDto> getBusDaiyasCalendarOption(BusDaiyasDetailSearchParamDto dto);

  /**
   * 車両selectBoxに使用する情報を取得
   *
   * @param dto 便ダイヤ検索情報
   * @return 便ダイヤ変更画面の車両selectBox情報取得用DTO一覧
   */
  List<BusDaiyasVehicleOptionDto> getBusDaiyasVehicleOption(BusDaiyasDetailSearchParamDto dto);

  /**
   * 営業所selectBoxに使用する情報を取得
   *
   * @param dto 便ダイヤ検索情報
   * @return 便ダイヤ変更画面の営業所selectBox情報取得用DTO一覧
   */
  List<BusDaiyasOfficeOptionDto> getBusDaiyasOfficeOption(BusDaiyasDetailSearchParamDto dto);

  /**
   * 便ダイヤ登録済み情報一覧取得
   *
   * @param dto 便ダイヤ検索情報
   * @return 登録済み便ダイヤ情報取得用DTO一覧
   */
  List<BusDaiyasRegistedDto> getBusDaiyasRegistedList(BusDaiyasDetailSearchParamDto dto);

  /**
   * 系統プルダウン選択項目の停留所取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 路線停留所と付随する系統低留保取得用DTO一覧
   */
  List<BusDaiyasRosenBusStopDto> getBusDaiyasRosenBusStop(BusDaiyasDetailForm form);

  /**
   * 系統プルダウン選択項目の停留所取得
   *
   * @param form 便ダイヤ詳細FORM
   * @return 路線停留所と付随する系統低留保取得用DTO一覧
   */
  List<BusDaiyasLineRouteBusStopDto> getBusDaiyasLineRouteBusStop(BusDaiyasDetailForm form);

}
