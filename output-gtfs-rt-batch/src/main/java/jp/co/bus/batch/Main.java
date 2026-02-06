package jp.co.bus.batch;

import java.lang.invoke.MethodHandles;

import jp.co.bus.batch.exception.DatabaseFatalException;
import jp.co.bus.batch.exception.S3UploadException;
import jp.co.bus.batch.factory.SessionFactory;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.bus.batch.enums.ExitCode;
import jp.co.bus.batch.exception.UsageException;
import jp.co.bus.batch.service.MainService;

/**
 * Main class
 * 
 * @author g.ko
 *
 */
public class Main {

  /** log */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** batch name */
  public static final String BATCH_NAME = "output-gtfs-rt-batch";

  /** SessionFactory（singleton） */
  private static SessionFactory sessionFactory = SessionFactory.getInstance();
  /**
   * メイン
   *
   * @param args 引数
   */
  public static void main(String[] args) {
    LOGGER.info("starting " + BATCH_NAME);

    ExitCode exitCode = ExitCode.FAILED;

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {

      // バッチ実行
      MainService service = new MainService(sessionFactory);
      service.execute();

      exitCode = ExitCode.SUCCESS;

    } catch (UsageException e) {
      e.printUsage();
      System.exit(ExitCode.FAILED.getCode());
    } catch (DatabaseFatalException e) {
      LOGGER.error(BATCH_NAME + " データ取得エラー", e);
    } catch (S3UploadException e) {
      LOGGER.error(BATCH_NAME + " S3アップロードエラー", e);
    } catch (Exception e) {
      LOGGER.error(BATCH_NAME + " unexpected error.", e);
    } finally {
      stopWatch.stop();
      LOGGER.info(BATCH_NAME + " TIME:" + stopWatch.getTime() + "ms");
      LOGGER.info(" finished " + BATCH_NAME + " batch. ExitCode:" + exitCode);
      System.exit(exitCode.getCode());
    }
  }

}
