package cn.zhuyee.executor;

import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>执行器接口</h2>
 *
 * <br>
 * Created by zhuye at 2023/1/1 13:26.
 */
public interface Executor {

  /**
   * 执行底层 JDBC
   *
   * @param configuration 数据库配置信息
   * @param mappedStatement 映射配置信息
   * @param param SQL参数，对应表的映射实体类
   * @param <E> 集合泛型类型
   * @return 返回结果集合
   */
  <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception;

  /**
   * 关闭连接，释放资源
   */
  void close();
}
