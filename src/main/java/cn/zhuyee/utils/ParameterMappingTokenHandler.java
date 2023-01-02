package cn.zhuyee.utils;

import cn.zhuyee.pojo.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterMappingTokenHandler implements TokenHandler {

  private List<ParameterMapping> parameterMappings = new ArrayList<>();

  /**
   * <h3>将占位符#{id}替换为?</h3>
   *
   * @param content 是参数名称，如 #{id} #{username}
   * @return ?
   */
  public String handleToken(String content) {
    parameterMappings.add(buildParameterMapping(content));
    return "?";
  }

  /**
   * 创建ParameterMapping对象
   *
   * @param content 占位符#{id}
   * @return ParameterMapping对象
   */
  private ParameterMapping buildParameterMapping(String content) {
    // 这里对象传入的是占位符#{}括号内的值：id
    ParameterMapping parameterMapping = new ParameterMapping(content);
    return parameterMapping;
  }

  public List<ParameterMapping> getParameterMappings() {
    return parameterMappings;
  }

  public void setParameterMappings(List<ParameterMapping> parameterMappings) {
    this.parameterMappings = parameterMappings;
  }
}