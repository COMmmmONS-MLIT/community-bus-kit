package communitybus.lambda.dao;

import java.lang.invoke.MethodHandles;
import java.util.List;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import communitybus.lambda.properties.AppProperties;

/**
 * DAOの基底クラス
 *
 * @author administrator
 *
 */
public abstract class BaseDao {

  /** Logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * SELECTを実行して該当データのリストを取得
   *
   * @param targetId
   * @param parameter
   * @return
   */
  public <T> List<T> getList(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    return sqlSession.selectList(targetId, parameter);
  }

  /**
   * SELECTを実行して該当データを取得
   *
   * @param sqlSession
   * @param targetId
   * @param parameter
   * @return
   */
  public <T> T getObject(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    return sqlSession.selectOne(targetId, parameter);
  }

  /**
   * SELECTを実行して数値を取得<br>
   * 該当なしの場合はNULLを返す
   *
   * @param sqlSession
   * @param targetId
   * @param parameter
   * @return
   */
  public Integer getInteger(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    Object result = getObject(sqlSession, targetId, parameter);
    return result == null ? null : (Integer) result;
  }

  /**
   * SELECTを実行して文字列を取得<br>
   * 該当なしの場合はNULLを返す
   *
   * @param sqlSession
   * @param targetId
   * @param parameter
   * @return
   * @throws Exception
   */
  public String getString(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    Object result = getObject(sqlSession, targetId, parameter);
    return result == null ? null : result.toString();
  }

  /**
   * INSERTを実行し件数を返す
   *
   * @param session
   * @param targetId
   * @param parameter
   * @return
   */
  public int insert(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    return sqlSession.insert(targetId, parameter);
  }

  /**
   * UPDATEを実行し件数を返す
   *
   * @param session
   * @param targetId
   * @param parameter
   * @return
   */
  public int update(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    return sqlSession.update(targetId, parameter);
  }

  /**
   * DELETEを実行し県数を返す
   *
   * @param session
   * @param targetId
   * @param parameter
   * @return
   */
  public int delete(SqlSession sqlSession, String targetId, Object parameter) {
    logQuery(sqlSession, targetId, parameter);
    return sqlSession.delete(targetId, parameter);
  }

  /**
   * デバッグのSQLを出力する
   *
   * @param session
   * @param targetId
   * @param parameter
   */
  private void logQuery(SqlSession session, String targetId, Object parameterObject) {

    if ("false".equals(AppProperties.SQL_DEBUG))
      return;

    try {

      MappedStatement mappedStatement = session.getConfiguration().getMappedStatement(targetId);
      BoundSql boundSql = session.getConfiguration().getMappedStatement(targetId).getSqlSource()
          .getBoundSql(parameterObject);
      String strQuery = boundSql.getSql();

      List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
      if (parameterMappings != null) {
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        MetaObject metaObject =
            parameterObject == null ? null : configuration.newMetaObject(parameterObject);
        for (int i = 0; i < parameterMappings.size(); i++) {
          ParameterMapping parameterMapping = parameterMappings.get(i);
          Object value = null;
          if (parameterMapping.getMode() != ParameterMode.OUT) {
            String propertyName = parameterMapping.getProperty();
            PropertyTokenizer prop = new PropertyTokenizer(propertyName);
            if (parameterObject == null) {
              value = null;
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
              value = parameterObject;
            } else if (boundSql.hasAdditionalParameter(propertyName)) {
              value = boundSql.getAdditionalParameter(propertyName);
            } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                && boundSql.hasAdditionalParameter(prop.getName())) {
              value = boundSql.getAdditionalParameter(prop.getName());
              if (value != null) {
                value = configuration.newMetaObject(value)
                    .getValue(propertyName.substring(prop.getName().length()));
              }
            } else {
              value = metaObject == null ? null : metaObject.getValue(propertyName);
            }
          }
          if (value != null) {
            if (parameterMapping.getJavaType().getTypeName().equals("java.lang.String")) {
              strQuery = strQuery.replaceFirst("\\?", "'" + String.valueOf(value) + "'");
            } else {
              strQuery = strQuery.replaceFirst("\\?", String.valueOf(value));
            }
          }
        }
      }

      // Make data of inserting log table
      strQuery = replaceLineFeeAndSpace(strQuery);
      // insert log table

      // 出力内容生成
      StringBuilder log = new StringBuilder();
      log.append("\n************************** START *******************************\n");
      log.append("SQL : " + targetId + "\n");
      log.append("QUERY : \n" + strQuery + "\n");
      log.append("************************** END *******************************");

      // System.out.println(log.toString());
      LOGGER.info(log.toString());
    } catch (Exception e) {
      LOGGER.info("デバッグ用SQLの出力に失敗");
    }
  }


  /**
   * Query改行と空白を整形する
   *
   * @param query
   * @return
   */
  private String replaceLineFeeAndSpace(String query) {
    String result = "";
    result = query;
    result = result.replaceAll("\t", " ");
    result = result.replaceAll("\n", " ");
    int i = 0;
    do {
      result = result.replaceAll("  ", " ");
      i++;
    } while (result.indexOf("  ") != -1 && i < 100); // continue to not exist double space or
                                                     // looping 100 count

    return result;
  }

}
