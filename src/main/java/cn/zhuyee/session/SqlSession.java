package cn.zhuyee.session;

import java.util.List;

/**
 * <h2>SqlSession接口</h2>
 *
 * <br>
 * Created by zhuye at 2023/1/1 13:21.
 */
public interface SqlSession {
  /**
   * <h3>查询多个结果</h3>
   * <p>1.要定位到要执行的SQL语句，从而执行</p>
   * <p>2.如 select * from user where username like '% ? %' 中的参数是待定的</p>
   *
   * @param statementId 唯一标识
   * @param param SQL参数
   * @param <E> 集合中泛型类型待定时用E代替
   * @return 返回查询结果集合
   */
  <E> List<E> selectList(String statementId, Object param);

  /**
   * <h3>查询单个结果</h3>
   *
   * @param statementId 唯一标识
   * @param param SQL参数
   * @param <T> 返回值类型待定时用泛型T代替
   * @return 返回查询结果
   */
  <T> T selectOne(String statementId, Object param);

  /**
   * 清除资源
   */
  void close();
}
