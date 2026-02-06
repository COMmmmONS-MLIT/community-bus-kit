package maint.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;


/**
 * Error page Controller
 *
 * @author administrator
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController extends BaseController {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** ホームURL */
  private static final String HOME_URL = "/";

  /**
   * エラー
   *
   * @param model      Model
   * @param statusCode ステータスコード
   * @param response   HttpServletResponse
   * @param builder    UriComponentsBuilder
   * @return エラー画面
   */
  @RequestMapping(value = "/{statusCode}/", method = {RequestMethod.GET, RequestMethod.POST})
  public String notFound(Model model, @PathVariable Integer statusCode,
      HttpServletResponse response, UriComponentsBuilder builder) {
    // 403エラーの場合
    if (StatusCode.FORBIDDEN.getCode().equals(statusCode)) {
      // HOMEにリダイレクト
      return "redirect:" + builder.path(HOME_URL).build().toUri();
    }
    response.setStatus(statusCode);
    StatusCode errorCode = StatusCode.getByCode(statusCode);
    model.addAttribute("error_code", errorCode.getCode());
    model.addAttribute("error_text", errorCode.getCodeText());
    model.addAttribute("detail_message", errorCode.getDetailMsg());
    return "error";
  }

  /**
   * HTTP status code
   *
   * @author administrator
   */
  @Getter
  public enum StatusCode {
    /**  */
    REQUEST_INVALID(400, "Bad Request", "リクエスト内容に誤りがあります。", "/error/400/")
    /** */
    , FORBIDDEN(403, "Forbidden", "該当ページへのアクセス権限がありません。", "/error/403/")
    /** */
    , NOT_EXIST_DATA(404, "Not Found", "該当アドレスのページはありません。", "/error/404/")
    /** */
    , METHOD_NOT_ALLOWED(405, "Method Not Allowed",
        "許可されていないメソッドタイプのリクエストです。", "/error/405/")
    /** */
    , CONFLICT(409, "Conflict",
        "新規登録、変更は同時に複数タブで開いて登録は行えません。\\n複数で開いているタブを閉じて再度登録をし直してください。\\n（セッション情報に誤りがあります）",
        "/error/409/")
    /** */
    , LOGIN_TIME_OUT(440, "Login Time Out", "セッションが切れましたので再度ログインしてください。",
        "/error/440/")
    /** */
    , INTERNAL_SERVER_ERROR(500, "Internal Server Error", "システムエラーが発生しました。",
        "/error/500/")
    /** */
    , SERVICE_UNAVAILABLE(503, "Service Unavailable", "現在サービスへのアクセスができません。",
        "/error/503/")
    /** */
    ;

    /**
     * Constructor
     *
     * @param code
     * @param codeText
     * @param detailMsg
     * @param path
     */
    StatusCode(Integer code, String codeText, String detailMsg, String path) {
      this.code = code;
      this.codeText = codeText;
      this.detailMsg = detailMsg;
      this.path = path;
    }

    /** HTTP status code */
    private final Integer code;
    /** HTTP status Message */
    private final String codeText;
    /** Detail message */
    private final String detailMsg;
    /** path */
    private final String path;

    /**
     * Get status code
     *
     * @param code
     * @return
     */
    public static StatusCode getByCode(Integer code) {
      for (StatusCode statusCode : StatusCode.values()) {
        if (statusCode.getCode().equals(code))
          return statusCode;
      }
      LOGGER.error("Error Page Code:" + code);
      return StatusCode.SERVICE_UNAVAILABLE;
    }
  }

}
