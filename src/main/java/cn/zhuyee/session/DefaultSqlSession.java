package cn.zhuyee.session;

import cn.zhuyee.executor.Executor;
import cn.zhuyee.pojo.Configuration;

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
}
