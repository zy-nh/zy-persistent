package cn.zhuyee.io;

import java.io.InputStream;

/**
 * <h2>获取配置文件</h2>
 *
 * <br>
 * Created by zhuye at 2022/12/31 19:40.
 */
public class Resources {

  /**
   * <h3>根据配置文件路径，加载配置文件为字节输入流，存到内容中</h3>
   *
   * @param path 配置文件路径
   * @return 字节输入流
   */
  public static InputStream getResourceAsStream(String path) {
    // 通过类加载器完成
    InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
    return resourceAsStream;
  }
}
