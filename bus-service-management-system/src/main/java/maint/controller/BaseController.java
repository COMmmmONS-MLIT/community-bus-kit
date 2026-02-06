package maint.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import maint.enums.PwdChangedFlag;
import maint.enums.message.MessageEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import maint.constants.CommConstant;
import maint.controller.ErrorController.StatusCode;
import maint.dto.ListPager;
import maint.dto.UserPermissionDto;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.exception.BadRequestException;
import maint.exception.CommonDbException;
import maint.exception.DoubleTabException;
import maint.exception.NeedPasswordChangeException;
import maint.exception.PermissionException;
import maint.form.BaseForm;
import maint.message.CommonMessage;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;

/**
 * ベースコントローラ
 *
 * @author administrator
 */
@Controller
public abstract class BaseController {

  /** リソースバンドル */
  protected static ResourceBundle RESOURCE_BUNDLE =
      ResourceBundle.getBundle("message", Locale.JAPANESE);
  /** ロガー */
  public final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  /** メッセージ */
  @Autowired
  protected MessageSource messageSource;
  /** エラーメッセージリスト */
  protected List<String> errorMsgList = new ArrayList<>();
  /** モーダルエラーメッセージリスト */
  protected List<String> modalErrorMsgList = new ArrayList<>();
  /** 正常メッセージリスト */
  protected List<String> successMsgList = new ArrayList<>();

  /**
   * ServletPathをModelに設定<br> Thymeleaf3.1から#HttpServletRequestを使えないため、別途設定が必要
   *
   * @param request HttpServletRequest
   * @return ServletPath
   */
  @ModelAttribute("servletPath")
  public String getRequestServletPath(HttpServletRequest request) {
    return request.getServletPath();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   * @param menu Menu
   */
  protected void execCommonProc(Model model, Menu menu) {
    execCommonProc(model, false);
    UserSession userSession = getUserSession();
    userSession.setMenu(menu);
  }

  /**
   * 共通処理
   *
   * @param model               モデル
   * @param passChangeCheckFlag パスワード変更チェックフラグ
   */
  protected void execCommonProc(Model model, boolean passChangeCheckFlag) {
    // メッセージ初期化
    successMsgList = new ArrayList<>();
    errorMsgList = new ArrayList<>();
    modalErrorMsgList = new ArrayList<>();

    model.addAttribute("errorMessages", errorMsgList);
    model.addAttribute("errorMsgList", errorMsgList);
    model.addAttribute("modalErrorMsgList", modalErrorMsgList);
    model.addAttribute("successMessages", successMsgList);
    model.addAttribute("errorMsgList", errorMsgList);
    model.addAttribute("successMsgList", successMsgList);

    // パスワード変更チェック
    if (!passChangeCheckFlag) {
      UserSession userSession = getUserSession();
      if (userSession.getPasswordChangedFlag().equals(PwdChangedFlag.NOT_CHANGED.getValue())) {
        throw new NeedPasswordChangeException();
      }
    }
  }

  /**
   * セッションキー設定（複数タブチェック用）
   *
   * @param model Model
   * @param form  BaseForm
   */
  protected void setSessionKey(Model model, BaseForm form) {
    // モデルとフォームに生成したUUIDを設定
    String uuid = UUID.randomUUID().toString();
    model.addAttribute("sessionKey", uuid);
    form.setSessionKey(uuid);
  }

  /**
   * UserSession情報の取得
   *
   * @return UserSession
   */
  public UserSession getUserSession() {
    return (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  /**
   * エラーメッセージリスト取得
   *
   * @return エラーメッセージリスト
   */
  protected List<String> getErrorMsgList() {
    return this.errorMsgList;
  }

  /**
   * エラーメッセージリストの設定
   *
   * @param msgList メッセージリスト
   */
  protected void setErrorMsgList(List<String> msgList) {
    for (String msg : msgList) {
      errorMsgList.add(msg);
    }
  }

  /**
   * エラーメッセージの設定(メッセージキー指定用)
   *
   * @param messageKey メッセージキー
   */
  protected void setErrorMsg(String messageKey) {
    errorMsgList.add(getMessage(messageKey));
  }

  /**
   * エラーメッセージの設定(メッセージキー指定用)
   *
   * @param messageKeys メッセージキー
   */
  protected void setErrorMsg(List<String> messageKeys) {
    messageKeys.forEach(e -> errorMsgList.add(getMessage(e)));
  }

  /**
   * エラーメッセージの設定(CommonMessageKeys指定用)
   *
   * @param commonMessage 共通メッセージ
   */
  protected void setErrorMsg(CommonMessage commonMessage) {
    errorMsgList.add(getMessage(commonMessage.getMessage()));
  }

  /**
   * エラーメッセージの設定(CommonMessageKeys指定用)
   *
   * @param commonMessage 共通メッセージ
   */
  protected void setErrorMsg(String title, CommonMessage commonMessage) {
    errorMsgList.add(title + getMessage(commonMessage.getMessage()));
  }

  /**
   * excelダウンロードエラーメッセージ設定
   *
   * @param excelName excel名
   */
  protected void setExcelDownloadErrorMessage(String excelName) {
    errorMsgList.add(excelName + getMessage(CommonMessage.FILE_DOWNLOAD_ERROR_EXCEL.getMessage()));
  }

  /**
   * 正常メッセージの設定(メッセージキー指定用)
   *
   * @param messageKey メッセージキー
   */
  protected void setSuccessMsg(String messageKey) {
    successMsgList.add(getMessage(messageKey));
  }

  /**
   * 正常メッセージの設定(メッセージキー指定用)
   *
   * @param commonMessage メッセージキー
   */
  protected void setSuccessMsg(String title, CommonMessage commonMessage) {
    successMsgList.add(title + getMessage(commonMessage.getMessage()));
  }

  /**
   * 正常メッセージの設定CommonMessageKeys指定用)
   *
   * @param commonoMsgEnum CommonMessageKeys
   */
  protected void setSuccessMsg(CommonMessage commonoMsgEnum) {
    successMsgList.add(getMessage(commonoMsgEnum.getMessage()));
  }

  /**
   * エラーメッセージの設定(MessageEnumsKeys指定用)
   *
   * @param messageEnums 画面メッセージEnum
   */
  protected void setErrorMsg(MessageEnums messageEnums) {
    errorMsgList.add(getMessage(messageEnums.getMessage()));
  }
  /**
   * モーダルエラーメッセージの設定(メッセージキー指定用)
   *
   * @param messageKey メッセージキー
   */
  protected void setModalErrorMsg(String messageKey) {
    modalErrorMsgList.add(getMessage(messageKey));
  }
  /**
   * モーダルエラーメッセージの設定(CommonMessageKeys指定用)
   *
   * @param commonMessage 共通メッセージ
   */
  protected void setModalErrorMsg(CommonMessage commonMessage) {
    modalErrorMsgList.add(getMessage(commonMessage.getMessage()));
  }

  /**
   * BindResultメッセージをエラーメッセージに設定
   *
   * @param errorList エラーメッセージリスト
   */
  public void setErrorMsgListByBindingError(Model model, List<FieldError> errorList) {
    this.errorMsgList = new ArrayList<>();
    for (FieldError error : errorList) {
      if (!errorMsgList.contains(error.getDefaultMessage())) {
        errorMsgList.add(error.getDefaultMessage());
      }
    }
    model.addAttribute("errorMessages", errorMsgList);
    model.addAttribute("errorMsgList", errorMsgList);
  }

  /**
   * エラーかどうか
   *
   * @return true：エラー、false：正常
   */
  protected boolean hasErrors() {
    return errorMsgList.size() > 0;
  }

  /**
   * ページャ生成
   *
   * @param model MODEL
   * @return ページャ
   */
  protected ListPager createListPager(Model model) {
    return createListPager(model, 1L);
  }

  /**
   * 一覧用の変数を作成する
   *
   * @param model         Model
   * @param currentPageNo 現在のページ
   * @return ページャ
   */
  protected ListPager createListPager(Model model, Long currentPageNo) {
    ListPager pager = new ListPager();
    pager.setCurrentPageNo(currentPageNo);
    pager.setDisplayMaxRow(CommConstant.PAGER_DISPLAY_MAX_ROW);
    pager.setDisplayPageWidth(CommConstant.PAGER_DISPLAY_PAGE_WIDTH);
    model.addAttribute("pager", pager);
    return pager;
  }

  /**
   * 一覧用の変数を作成する
   *
   * @param model         Model
   * @param currentPageNo 現在のページ
   * @param displayMaxRow 表示MAX行数
   * @return ページャ
   */
  protected ListPager createListPager(Model model, Long currentPageNo, Long displayMaxRow) {
    ListPager pager = new ListPager();
    pager.setCurrentPageNo(currentPageNo);
    pager.setDisplayMaxRow(displayMaxRow);
    pager.setDisplayPageWidth(CommConstant.PAGER_DISPLAY_PAGE_WIDTH);
    model.addAttribute("pager", pager);
    return pager;
  }

  /**
   * メッセージ取得
   *
   * @param key メッセージKEY
   * @return メッセージ
   */
  protected String getMessage(String key) {
    return messageSource.getMessage(key, null, Locale.JAPAN);
  }

  /**
   * メッセージ取得
   *
   * @param key  メッセージKEY
   * @param args {0},{1}... に対するメッセージ
   * @return メッセージ
   */
  protected String getMessage(String key, String... args) {
    return messageSource.getMessage(key, args, Locale.JAPAN);
  }

  /**
   * 登録/変更完了メッセージを取得
   *
   * @param dispMode 画面モード
   * @return 登録/変更完了メッセージ
   */
  protected CommonMessage getCompleteMessage(DispMode dispMode) {
    return DispMode.INSERT_MODE.equals(dispMode) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE;
  }

  /**
   * RedirectAttributesにエラーメッセージ設定(CommonMessage使用)
   *
   * @param commonMessage      基本メッセージ
   * @param redirectAttributes RedirectAttributes
   */
  public void addErrorMsgListToFlashAttr(CommonMessage commonMessage,
      RedirectAttributes redirectAttributes) {
    this.setErrorMsg(commonMessage);
    redirectAttributes.addFlashAttribute("errorFlashMessages", this.getErrorMsgList());
  }

  /**
   * RedirectAttributesにエラーメッセージ設定(messageKey指定)
   *
   * @param messageKey         メッセージキー
   * @param redirectAttributes RedirectAttributes
   */
  public void addErrorMsgListToFlashAttr(String messageKey, RedirectAttributes redirectAttributes) {
    this.setErrorMsg(messageKey);
    redirectAttributes.addFlashAttribute("errorFlashMessages", this.getErrorMsgList());
  }

  /**
   * RedirectAttributesにエラーメッセージ設定(CommonMessage使用・Modal使用)
   *
   * @param commonMessage      基本メッセージ
   * @param redirectAttributes RedirectAttributes
   */
  public void addModalErrorMsgListToFlashAttr(CommonMessage commonMessage,
      RedirectAttributes redirectAttributes) {
    this.setModalErrorMsg(commonMessage);
    redirectAttributes.addFlashAttribute("modalErrorMsgList", this.getModalErrorMsgList());
  }

  /**
   * RedirectAttributesにエラーメッセージ設定(messageKey指定・Modal使用)
   *
   * @param messageKey         メッセージキー
   * @param redirectAttributes RedirectAttributes
   */
  protected void addModalErrorMsgListToFlashAttr(String messageKey,
      RedirectAttributes redirectAttributes) {
    this.setModalErrorMsg(messageKey);
    redirectAttributes.addFlashAttribute("modalErrorMsgList", this.getModalErrorMsgList());
  }

  /**
   * エラーメッセージリスト取得(Modal用)
   *
   * @return エラーメッセージリスト
   */
  protected List<String> getModalErrorMsgList() {
    return this.modalErrorMsgList;
  }

  /**
   * 画面表示用BindingResult取得
   *
   * @param form          画面Form
   * @param bindingResult BindingResult
   * @param objectName    objectName
   * @return 画面表示用BindingResult
   */
  public BeanPropertyBindingResult getDispBindingResult(Object form, BindingResult bindingResult,
      String objectName) {
    BeanPropertyBindingResult dispBindingResult = new BeanPropertyBindingResult(form, objectName);
    if (bindingResult.hasErrors()) {
      bindingResult.getAllErrors().stream().forEach(e -> dispBindingResult.addError(e));
    }
    return dispBindingResult;
  }

  /**
   * 400エラーハンドル
   *
   * @param e BadRequestException
   * @return エラーページ
   */
  @ExceptionHandler(BadRequestException.class)
  private String handleBadRequestException(BadRequestException e) {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.REQUEST_INVALID.getPath())
        .build().toUri();
  }

  /**
   * 権限エラーハンドル
   *
   * @param e PermissionException
   * @return エラーページ
   */
  @ExceptionHandler(PermissionException.class)
  private String handlePermissionException(PermissionException e) {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.FORBIDDEN.getPath()).build()
        .toUri();
  }

  /**
   * 複数タブエラーハンドル
   *
   * @param e DoubleTabException
   * @return エラーページ
   */
  @ExceptionHandler(DoubleTabException.class)
  private String handlePermissionException(DoubleTabException e) {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.CONFLICT.getPath()).build()
        .toUri();
  }

  /**
   * 共通DBエラーハンドル
   *
   * @param e CommonException
   * @return エラーページ
   */
  @ExceptionHandler(CommonDbException.class)
  public String ExceptionHandler(CommonDbException e) {
    LOGGER.error(e.getMessage(), e);
    return "redirect:" + UriComponentsBuilder.fromUriString(
        StatusCode.INTERNAL_SERVER_ERROR.getPath()).build().toUri();
  }

  /**
   * 制約違反エラーハンドル
   *
   * @return エラーページ
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public String ConstraintViolationExceptionHandler() {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.REQUEST_INVALID.getPath())
        .build().toUri();
  }

  /**
   * リクエストパラメータエラーハンドル
   *
   * @return エラーページ
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public String MethodArgumentTypeMismatchExceptionHandler() {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.NOT_EXIST_DATA.getPath())
        .build().toUri();
  }

  /**
   * 共通Exceptionエラーハンドル
   *
   * @param e CommonException
   * @return エラーページ
   */
  @ExceptionHandler(Exception.class)
  public String ExceptionHandler(Exception e) {
    LOGGER.error(e.getMessage(), e);
    return "redirect:" + UriComponentsBuilder.fromUriString(
        StatusCode.INTERNAL_SERVER_ERROR.getPath()).build().toUri();
  }

  /**
   * 初期発行パスワードExceptionハンドリング
   *
   * @return パスワード変更画面
   */
  @ExceptionHandler({NeedPasswordChangeException.class})
  public String handlePasswordError(NeedPasswordChangeException e,
      RedirectAttributes redirectAttributes) {
    String uri = UriComponentsBuilder.fromUriString("/").build().toUri().toString();
    return "redirect:" + uri;
  }

  /**
   * ユーザ権限取得<br> セッションに保持している権限情報を取得
   *
   * @param menu メニューEnum
   * @return ユーザ権限DTO
   */
  private UserPermissionDto getUserPermission(Menu menu) {
    UserSession session = getUserSession();
    for (UserPermissionDto perm : session.getUserPermList()) {
      if (menu.getMenuCode().equals(perm.getMenuCode())) {
        return perm;
      }
    }
    return new UserPermissionDto();
  }

  /**
   * 書込権限チェック
   *
   * @param menu メニューEnum
   * @return チェック結果
   */
  public boolean hasWritePermission(Menu menu) {
    UserPermissionDto perm = getUserPermission(menu);
    return perm.hasWritePerm();
  }

  /**
   * ダウンロード権限チェック
   *
   * @param menu メニューEnum
   * @return チェック結果
   */
  public boolean hasDownloadPermission(Menu menu) {
    UserPermissionDto perm = getUserPermission(menu);
    return perm.hasDownloadPerm();
  }

  /**
   * アクセスの権限チェック 参照権限はSpringSecuryでURLベースで制限を行うため、チェックしない。
   *
   * @param menu              メニューEnum
   * @param checkWritePerm    書込権限チェック有無
   * @param checkDownloadPerm DL権限チェック有無
   */
  public void hasAccessPermission(Menu menu, boolean checkWritePerm, boolean checkDownloadPerm) {
    UserPermissionDto perm = getUserPermission(menu);

    // 権限設定の有無
    if (ExObjectUtils.isBlank(perm) || !menu.getMenuCode().equals(perm.getMenuCode())) {
      throw new PermissionException(menu.getName() + "への権限がありません。");
    }

    // 書込権限権限チェック
    if (checkWritePerm && !perm.hasWritePerm()) {
      throw new PermissionException(menu.getName() + "への書込権限がありません。");
    }

    // DL権限権限チェック
    if (checkDownloadPerm && !perm.hasDownloadPerm()) {
      throw new PermissionException(menu.getName() + "へのDL権限がありません。");
    }
  }

}
