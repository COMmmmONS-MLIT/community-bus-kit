package maint;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * Boot Application class.
 *
 * @author masaya.tanaka
 */
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10800)
public class BootApplication implements WebMvcConfigurer {

  @Autowired
  private MessageSource messageSource;

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setProviderClass(HibernateValidator.class);
    Map<String, String> props = new HashMap<>();
    props.put("hibernate.validator.fail_fast", "false"); // 전체 위반 수집
    bean.setValidationPropertyMap(props);
    bean.setValidationMessageSource(messageSource);      // 메시지소스 연동
    return bean;
  }


  public static void main(String[] args) {
    SpringApplication.run(BootApplication.class, args);
  }

  @Override
  public org.springframework.validation.Validator getValidator() {
    return validator();
  }

  /**
   * Spring SessionがAWS上のRedisのCONFIGを実行しないようにする
   *
   * @return Spring SessionがAWS上のRedisのCONFIGを実行しない設定
   */
  @Bean
  public static ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
  }

}
