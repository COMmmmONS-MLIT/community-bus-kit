package communitybus.lambda;

import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import communitybus.lambda.exception.DatabaseFatalException;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import communitybus.lambda.constants.CommConstant;
import communitybus.lambda.dto.InputDto;
import communitybus.lambda.dto.OutputDto;
import communitybus.lambda.enums.ErrorInfoEnums;
import communitybus.lambda.enums.ExitCode;
import communitybus.lambda.factory.SessionFactory;
import communitybus.lambda.service.MainService;
import communitybus.lambda.utils.ExObjectUtils;

/**
 * Main class
 *
 * @author y.kim
 *
 */
public class LambdaRequestHandler implements RequestHandler<Map<String, Object>, OutputDto> {

  /** Logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** SessionFactory（singleton） */
  private static SessionFactory sessionFactory = SessionFactory.getInstance();

  private static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Main Lambda実行時、開始関数 InputDto：Json形式をDtoとして受け取る
   */
  @Override
  public OutputDto handleRequest(Map<String, Object> event, Context context) {
    LOGGER.info("starting {}", CommConstant.FUNCTION_NAME);
    String body = (String) event.get("body");
    InputDto input = new InputDto();
    try {
      input = objectMapper.readValue(body, InputDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    ExitCode statusCode = ExitCode.FAILED;

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    LOGGER.info("Input Info:{}", input.toString());

    try {
      // 整合性チェック
      List<String> errors = input.validate();
      if (ExObjectUtils.isNotEmpty(errors)) {
        return new OutputDto(ErrorInfoEnums.INVALID_PARAMETER);
      }

      // 緯度、経度設定
      if ((input.getLatitude() + "," + input.getLongitude()).length() > 50) {
        return new OutputDto(ErrorInfoEnums.INVALID_PARAMETER);
      } else {
        input.setLatitudeLongitude(input.getLatitude() + "," + input.getLongitude());
      }

      // 送信日時整合性チェック
      if (ExObjectUtils.isNotBlank(input.getCreatedDatetime())) {
        input.setCreatedDatetimeParsed(parseValidTimestamp(input.getCreatedDatetime()));
        if (ExObjectUtils.isBlank(input.getCreatedDatetimeParsed())) {
          return new OutputDto(ErrorInfoEnums.INVALID_PARAMETER);
        }
      }

      MainService service = new MainService(sessionFactory);

      // アクセスキーチェック
      if (service.isNotExistAccessKey(input.getAccessKey())) {
        return new OutputDto(ErrorInfoEnums.INVALID_ACCESS_KEY);
      }

      // MainService実行
      service.execute(input);

      statusCode = ExitCode.SUCCESS;

    } catch (DatabaseFatalException e) {
      return new OutputDto(ErrorInfoEnums.DATABASE_REGISTRATION_FAILED);
    } catch (Exception e) {
      LOGGER.error("{} unexpected error.", CommConstant.FUNCTION_NAME, e);
      return new OutputDto(ErrorInfoEnums.SYSTEM_ERROR);
    } finally {
      stopWatch.stop();
      LOGGER.info("{} TIME:{}ms", CommConstant.FUNCTION_NAME, stopWatch.getTime());
      LOGGER.info(" finished {} function. ExitCode:{}", CommConstant.FUNCTION_NAME, statusCode);
    }
    return new OutputDto();
  }

  /**
   * Main テスト用
   *
   * @param args 引数
   */
  public static void main(String[] args) {
    LOGGER.info("starting {}", CommConstant.FUNCTION_NAME);
    ExitCode exitCode = ExitCode.FAILED;

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      String testData = "{"
          + "\"gpsSerial\": \"25252\","
          + "\"workNumber\": \"WK20251223\","
          + "\"latitude\": 35.689487,"
          + "\"longitude\": 139.691706,"
          + "\"direction\": \"NORTH\","
          + "\"createdDatetime\": \"2025-12-23T20:30:00+09\","
          + "\"accessKey\": \"fb81d2cbc717eb26\""
          + "}";

      InputDto inputDto = objectMapper.readValue(testData, InputDto.class);

      // 整合性チェック
      List<String> errors = inputDto.validate();
      if (ExObjectUtils.isNotEmpty(errors)) {
        System.exit(exitCode.getCode());
      }

      // 緯度、経度設定
      if ((inputDto.getLatitude() + "," + inputDto.getLongitude()).length() > 50) {
        System.exit(exitCode.getCode());
      } else {
        inputDto.setLatitudeLongitude(inputDto.getLatitude() + "," + inputDto.getLongitude());
      }

      // 送信日時整合性チェック
      if (ExObjectUtils.isNotBlank(inputDto.getCreatedDatetime())) {
        inputDto.setCreatedDatetimeParsed(parseValidTimestamp(inputDto.getCreatedDatetime()));
        if (ExObjectUtils.isBlank(inputDto.getCreatedDatetimeParsed())) {
          System.exit(exitCode.getCode());
        }
      }

      // MainService実行
      MainService service = new MainService(sessionFactory);

      // アクセスキーチェック
      if (service.isNotExistAccessKey(inputDto.getAccessKey())) {
        System.exit(exitCode.getCode());
      }

      service.execute(inputDto);

      exitCode = ExitCode.SUCCESS;

    } catch (Exception e) {
      LOGGER.error("{} unexpected error.", CommConstant.FUNCTION_NAME, e);
    } finally {
      stopWatch.stop();
      LOGGER.info("{} TIME:{}ms", CommConstant.FUNCTION_NAME, stopWatch.getTime());
      LOGGER.info(" finished {} function. ExitCode:{}", CommConstant.FUNCTION_NAME, exitCode);
      System.exit(exitCode.getCode());
    }
  }

  /**
   * Requestの送信日時をparse
   *
   * @param createdDatetime 送信日時
   * @return parsed送信日時
   */
  public static OffsetDateTime parseValidTimestamp(String createdDatetime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
    try {
      return OffsetDateTime.parse(createdDatetime, formatter);
    } catch (Exception e) {
      return null;
    }
  }
}
