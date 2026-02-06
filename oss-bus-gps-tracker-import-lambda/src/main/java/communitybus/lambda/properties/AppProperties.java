package communitybus.lambda.properties;

/**
 * APP Properties
 *
 * @author administrator
 *
 */
public class AppProperties {

  /** AWS FROM EMAIL */
  public static final String AWS_FROM_EMAIL = System.getenv("AWS_FROM_EMAIL");

  /** AWS FROM NAME */
  public static final String AWS_FROM_NAME = System.getenv("AWS_FROM_NAME");

  /** AWS SES PROFILE */
  public static final String AWS_SES_PROFILE = System.getenv("AWS_SES_PROFILE");

  /** ACTIVE ENV */
  public static final String ACTIVE_ENV = System.getenv("ACTIVE_ENV");

  /** SQL DEBUG */
  public static final String SQL_DEBUG = System.getenv("SQL_DEBUG");

  /** SES REGION */
  public static final String SES_REGION = System.getenv("SES_REGION");

}
