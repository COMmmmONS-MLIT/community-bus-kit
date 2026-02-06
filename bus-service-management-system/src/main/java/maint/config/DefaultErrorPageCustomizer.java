package maint.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * DefaultErrorPageCustomizer
 *
 * @author administrator
 */
@Component
public class DefaultErrorPageCustomizer
    implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {

    // 400
    factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400/"));
    // 403
    factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403/"));
    // 404
    factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404/"));
    // 405
    factory.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405/"));
    // 500
    factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500/"));
    // 501
    factory.addErrorPages(new ErrorPage(HttpStatus.NOT_IMPLEMENTED, "/error/501/"));
    // 503
    factory.addErrorPages(new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, "/error/503/"));
  }
}
