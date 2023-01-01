package cn.zhuyee.executor;

import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;

import java.util.List;

/**
 * <h2>执行器实现类</h2>
 * <br>
 * Created by zhuye at 2023/1/1 13:27.
 */
public class SimpleExecutor implements Executor{

  @Override
  public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) {
    return null;
  }

  @Override
  public void close() {

  }
}
