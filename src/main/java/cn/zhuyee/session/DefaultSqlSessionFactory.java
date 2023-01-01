package cn.zhuyee.session;

import cn.zhuyee.executor.Executor;
import cn.zhuyee.executor.SimpleExecutor;
import cn.zhuyee.pojo.Configuration;

/**
 * <h2>SqlSessionFactory工厂接口的默认实现类</h2>
 * <br>
 * Created by zhuye at 2023/1/1 1:23.
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{
  // 配置信息类，要向下传递
  private Configuration configuration;

  public DefaultSqlSessionFactory(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public SqlSession openSession() {
    // 1.创建执行器对象
    Executor simpleExecutor = new SimpleExecutor();

    // 2.生产 SqlSession 对象（对象会用到configuration，所以这里进行了传递）
    DefaultSqlSession defaultSqlSession = new DefaultSqlSession(configuration, simpleExecutor);
    return defaultSqlSession;
  }
}
