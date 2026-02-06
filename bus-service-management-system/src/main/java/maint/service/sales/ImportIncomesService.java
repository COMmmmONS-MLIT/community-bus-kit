package maint.service.sales;

import java.util.List;
import maint.dto.sales.importIncomes.BusServiceOptionsDto;
import maint.dto.sales.importIncomes.ImportIncomesDto;
import maint.dto.sales.importIncomes.SearchImportIncomesDto;
import maint.entity.IncomeInfosEntity;
import maint.exception.CommonDbException;
import maint.form.importIncomes.ImportIncomesForm;
import maint.mapper.BusServicesMapper;
import maint.mapper.IncomeInfoMapper;
import maint.message.ImportIncomesMessage;
import maint.service.BaseService;
import maint.utils.ExNumberUtils;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * 収入情報インポートSERVICE
 *
 * @author g.ko
 */
@Service
public class ImportIncomesService extends BaseService {

  /** 収入情報MAPPER */
  @Autowired
  IncomeInfoMapper incomeInfoMapper;

  /** 便MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /**
   * 収入情報一覧取得
   *
   * @param form 検索FORM
   */
  public ImportIncomesDto getImportIncomes(ImportIncomesForm form) {
    SearchImportIncomesDto dto = new SearchImportIncomesDto(form, getUserSession());
    return incomeInfoMapper.getImportIncomesBySearch(dto);
  }

  /**
   * 収入情報登録
   *
   * @param form 収入情報インポートFORM
   * @return 収入情報ID
   */
  @Transactional
  public Long insert(ImportIncomesForm form) {
    IncomeInfosEntity incomeInfosEntity = new IncomeInfosEntity(form);
    if (incomeInfoMapper.registerIncomeInfo(incomeInfosEntity) != 1) {
      throw new CommonDbException("【収入情報インポート管理】登録エラー");
    }

    return incomeInfosEntity.getIncomeInfoId();
  }

  /**
   * 収入情報変更
   *
   * @param form 収入情報インポートFORM
   */
  @Transactional
  public void update(ImportIncomesForm form) {
    IncomeInfosEntity incomeInfosEntity = new IncomeInfosEntity(form);
    if (incomeInfoMapper.updateIncomeInfo(incomeInfosEntity) != 1) {
      throw new CommonDbException("【収入情報インポート管理】変更エラー");
    }
  }

  /**
   * 登録Validationチェック
   *
   * @param form 収入情報インポートFORM
   * @param bindingResult bindingResult
   */
  public void registerValidate(ImportIncomesForm form, BindingResult bindingResult) {
    // 乗車人数（必須、桁数、型チェック）
    if (ExObjectUtils.isBlank(form.getPassengersCount())) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passengersCount",
          form.getPassengersCount(), false, null, null,
          getMessage(ImportIncomesMessage.ERROR_REQUIRED.getMessage())));
    } else if (!ExNumberUtils.isValidNumber(form.getPassengersCount())) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passengersCount",
          form.getPassengersCount(), false, null, null,
          getMessage(ImportIncomesMessage.ERROR_NUMERIC.getMessage())));
    } else if (form.getPassengersCount().length() > 10) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passengersCount",
          form.getPassengersCount(), false, null, null,
          getMessage(ImportIncomesMessage.ERROR_MAX_LENGTH.getMessage())));
    }

    // 現金（必須、桁数、型チェック）
    if (ExObjectUtils.isBlank(form.getCashIncome())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "cashIncome", form.getCashIncome(), false,
              null, null, getMessage(ImportIncomesMessage.ERROR_REQUIRED.getMessage())));
    } else if (!ExNumberUtils.isValidNumber(form.getCashIncome())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "cashIncome", form.getCashIncome(), false,
              null, null, getMessage(ImportIncomesMessage.ERROR_NUMERIC.getMessage())));
    } else if (form.getCashIncome().length() > 10) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "cashIncome", form.getCashIncome(), false,
              null, null, getMessage(ImportIncomesMessage.ERROR_MAX_LENGTH.getMessage())));
    }

    // ICカード類（必須、桁数、型チェック）
    if (ExObjectUtils.isBlank(form.getIcAmount())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "icAmount", form.getIcAmount(), false, null,
              null, getMessage(ImportIncomesMessage.ERROR_REQUIRED.getMessage())));
    } else if (!ExNumberUtils.isValidNumber(form.getIcAmount())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "icAmount", form.getIcAmount(), false, null,
              null, getMessage(ImportIncomesMessage.ERROR_NUMERIC.getMessage())));
    } else if (form.getIcAmount().length() > 10) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "icAmount", form.getIcAmount(), false, null,
              null, getMessage(ImportIncomesMessage.ERROR_MAX_LENGTH.getMessage())));
    }

    // 回数券（必須、桁数、型チェック）
    if (ExObjectUtils.isBlank(form.getCouponTicket())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "couponTicket", form.getCouponTicket(),
              false, null, null, getMessage(ImportIncomesMessage.ERROR_REQUIRED.getMessage())));
    } else if (!ExNumberUtils.isValidNumber(form.getCouponTicket())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "couponTicket", form.getCouponTicket(),
              false, null, null, getMessage(ImportIncomesMessage.ERROR_NUMERIC.getMessage())));
    } else if (form.getCouponTicket().length() > 10) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), "couponTicket", form.getCouponTicket(),
              false, null, null, getMessage(ImportIncomesMessage.ERROR_MAX_LENGTH.getMessage())));
    }
  }

  /**
   * 便オプション取得
   *
   * @param rosenId 路線ID
   * @param serviceDate 運行日
   * @return 便オプション
   */
  public List<BusServiceOptionsDto> getBusServiceOptions(Long rosenId, String serviceDate) {
    return busServicesMapper.getBusServiceOptions(rosenId, serviceDate,
        getUserSession().getBusinessCorpId());
  }

}
