package cn.zhuyee.config;

import cn.zhuyee.utils.ParameterMapping;

import java.util.List;

/**
 * <h2>绑定处理后的SQL</h2>
 * <br>
 * Created by zhuye at 2023/1/2 0:06.
 */
public class BoundSql {
  /**
   * 最终转换后要真正执行的SQL
   */
  private String finalSql;

  /**
   * 用来存放#{}解析后的值的内容的集合
   */
  private List<ParameterMapping> parameterMappingList;

  public BoundSql(String finalSql, List<ParameterMapping> parameterMappingList) {
    this.finalSql = finalSql;
    this.parameterMappingList = parameterMappingList;
  }

  public String getFinalSql() {
    return finalSql;
  }

  public void setFinalSql(String finalSql) {
    this.finalSql = finalSql;
  }

  public List<ParameterMapping> getParameterMappingList() {
    return parameterMappingList;
  }

  public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
    this.parameterMappingList = parameterMappingList;
  }
}
