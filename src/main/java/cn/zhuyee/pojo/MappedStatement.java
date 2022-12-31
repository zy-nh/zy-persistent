package cn.zhuyee.pojo;

/**
 * <h2>映射配置类：存放Mapper.xml解析的内容</h2>
 * <p>一个类对应Mapper.xml文件中的一个SQL标签语句</p>
 * <br>
 * Created by zhuye at 2022/12/31 20:06.
 */
public class MappedStatement {
  /**
   * 唯一标识：statementId == namespace.id
   */
  private String statementId;
  /**
   * 返回值类型
   */
  private String resultType;
  /**
   * 参数值类型
   */
  private String parameterType;
  /**
   * SQL语句
   */
  private String sql;

  public String getStatementId() {
    return statementId;
  }

  public void setStatementId(String statementId) {
    this.statementId = statementId;
  }

  public String getResultType() {
    return resultType;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  public String getParameterType() {
    return parameterType;
  }

  public void setParameterType(String parameterType) {
    this.parameterType = parameterType;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }
}
