package jp.co.bus.batch.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * Common DAO
 * 
 * @author hr.kim
 *
 */
public class CommonDao extends BaseDao {

  /**
   * Start transaction
   * 
   * @param sqlSession
   * @return
   */
  public void startTransaction(SqlSession sqlSession) {
    update(sqlSession, "CommonMapper.startTransaction", null);
  }

  /**
   * Commit
   * 
   * @param sqlSession
   * @return
   */
  public void commit(SqlSession sqlSession) {
    update(sqlSession, "CommonMapper.commit", null);
  }

  /**
   * RollBack
   * 
   * @param sqlSession
   * @return
   */
  public void rollback(SqlSession sqlSession) {
    update(sqlSession, "CommonMapper.rollback", null);
  }

}
