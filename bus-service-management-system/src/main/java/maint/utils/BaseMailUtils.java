package maint.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;

/**
 * Base Mail Util
 *
 * @author administrator
 */
@Component
public class BaseMailUtils {

  private static String FREEMARKER_BASE_DIR = "/mail/";

  /** Free marker configure */
  @Autowired
  private Configuration freemarkerConfig;

  /**
   * 本文取得
   *
   * @param ftlFileName
   * @param dto
   * @return
   * @throws Exception
   */
  public String createContents(String ftlFileName, Object dto) throws Exception {
    this.freemarkerConfig.setClassForTemplateLoading(this.getClass(), FREEMARKER_BASE_DIR);
    return FreeMarkerTemplateUtils.processTemplateIntoString(
        this.freemarkerConfig.getTemplate(ftlFileName), dto);
  }

}
