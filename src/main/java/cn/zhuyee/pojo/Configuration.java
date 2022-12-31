package cn.zhuyee.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>全局配置类：存放核心配置文件解析出来的内容</h2>
 * <br>
 * Created by zhuye at 2022/12/31 20:19.
 */
public class Configuration {

  /**
   * 数据源对象
   */
  private DataSource dataSource;

  /**
   * key: statementId == namespace.id
   * <br>
   * value: 封装好的 MappedStatement 对象
   */
  private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Map<String, MappedStatement> getMappedStatementMap() {
    return mappedStatementMap;
  }

  public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
    this.mappedStatementMap = mappedStatementMap;
  }
}
