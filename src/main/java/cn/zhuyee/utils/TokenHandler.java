package cn.zhuyee.utils;

public interface TokenHandler {
  /**
   * <h3>将占位符#{id}替换为?</h3>
   *
   * @param content 是参数名称，如 #{id} #{username}
   * @return ?
   */
  String handleToken(String content);
}