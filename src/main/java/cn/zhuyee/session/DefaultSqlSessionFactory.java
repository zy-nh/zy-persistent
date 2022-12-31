package cn.zhuyee.session;

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
}
