package communitybus.lambda.properties;

/**
 * SFN Properties
 *
 * @author administrator
 *
 */
public class SfnProperties {

  /** region */
  public static final String REGION = System.getenv("REGION");

  /** state-machine-arn */
  public static final String STATE_MACHINE_ARN = System.getenv("STATE_MACHINE_ARN");

  /** tenant-code */
  public static final String TENANT_CODE = System.getenv("TENANT_CODE");

  /** profile-name */
  public static final String PROFILE_NAME = System.getenv("PROFILE_NAME");

  /** role-arn */
  public static final String ROLE_ARN = System.getenv("ROLE_ARN");

  /** role-session */
  public static final String ROLE_SESSION = System.getenv("ROLE_SESSION");

  /** aws-session-token */
  public static final String AWS_SESSION_TOKEN = System.getenv("AWS_SESSION_TOKEN");

  /** parameters-secrets-extension-http-port */
  public static final String PARAMETERS_SECRETS_EXTENSION_HTTP_PORT =
      System.getenv("PARAMETERS_SECRETS_EXTENSION_HTTP_PORT");

  /** secret-manager-id */
  public static final String SECRET_MANAGER_ID = System.getenv("SECRET_MANAGER_ID");
}
