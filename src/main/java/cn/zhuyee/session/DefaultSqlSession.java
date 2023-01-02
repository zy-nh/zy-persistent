package cn.zhuyee.session;

import cn.zhuyee.executor.Executor;
import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;

import java.util.List;

/**
 * <h2>SqlSession默认实现方法</h2>
 * <br>
 * Created by zhuye at 2023/1/1 13:21.
 */
public class DefaultSqlSession implements SqlSession{
  // 配置信息（需要向下传递）
  private Configuration configuration;

  // 上层需要调用底层的Executor对象，所以这里也需要向下传递
  private Executor executor;

  public DefaultSqlSession(Configuration configuration, Executor executor) {
    this.configuration = configuration;
    this.executor = executor;
  }

  @Override
  public <E> List<E> selectList(String statementId, Object param) throws Exception {
    // 将查询操作委派给底层执行器
    // query方法：执行底层JDBC （1.数据库配置信息Configuration中；2.SQL配置信息：MappedStatement中）
    MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
    List<E> list = executor.query(configuration,mappedStatement,param);
    return list;
  }

  @Override
  public <T> T selectOne(String statementId, Object param) throws Exception {
    // 调用selectList()方法
    List<Object> list = this.selectList(statementId, param);
    if (list.size() == 1) {
      return (T) list.get(0);
    } else if (list.size() > 1) {
      throw new RuntimeException("返回结果过多");
    } else {
      return null;
    }
  }

  @Override
  public void close() {
    // 委派给底层执行器去操作
    executor.close();
  }
}
