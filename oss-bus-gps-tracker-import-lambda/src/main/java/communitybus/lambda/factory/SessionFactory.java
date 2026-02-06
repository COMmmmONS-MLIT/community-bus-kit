package communitybus.lambda.factory;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import communitybus.lambda.exception.DatabaseFatalException;

/**
 * SessionのFactoryクラス
 *
 * @author administrator
 *
 */
public class SessionFactory {

  /** MyBatis configuration */
  private static String resource = "mybatis-config.xml";

  /** singleton */
  private static final SessionFactory INSTANCE = new SessionFactory();

  /** session factory */
  private SqlSessionFactory sqlSessionFactory;

  /**
   * new不可コンストラクタ
   *
   * @throws IOException
   */
  private SessionFactory() {
    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream(resource);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "LOCAL");
    } catch (Exception e) {
      e.printStackTrace();
      throw new DatabaseFatalException("SqlSessionFactoryの初期化に失敗", e);
    } finally {
      try {
        inputStream.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * singletonインスタンスを取得
   *
   * @return
   */
  public static SessionFactory getInstance() {
    return INSTANCE;
  }

  /**
   * SqlSessionを取得<br>
   * コミットされるまで外から認識されないトランザクションレベルのSqlSessionを返す
   *
   * @return
   */
  public SqlSession getSession() {
    return getSqlSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * トランザクションレベルを指定してSqlSessionを取得
   *
   * @param level
   * @return
   */
  public SqlSession getSession(TransactionIsolationLevel level) {
    return getSqlSession(level);
  }

  /**
   * SqlSessionを取得<br>
   * PreparedStatementを再利用するSqlSessionを返す
   *
   * @return
   */
  protected SqlSession getSqlSession(TransactionIsolationLevel level) {
    return this.sqlSessionFactory.openSession(ExecutorType.REUSE, level);
  }
}
