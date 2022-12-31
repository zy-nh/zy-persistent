package cn.zhuyee.session;

import cn.zhuyee.config.XMLConfigBuilder;
import cn.zhuyee.pojo.Configuration;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * <h2>SqlSessionFactory构造工厂</h2>
 * <br>
 * Created by zhuye at 2022/12/31 20:41.
 */
public class SqlSessionFactoryBuilder {

  /**
   * <h3>SqlSessionFactory 工厂方法</h3>
   * <ol>
   *   <li>解析配置文件，封装容器对象</li>
   *   <li>创建 SqlSessionFactory 工厂对象</li>
   * </ol>
   * @param inputStream 字节输入流
   * @return SqlSessionFactory 工厂对象
   */
  public SqlSessionFactory build(InputStream inputStream) throws DocumentException {

    // 1.解析配置文件，封装容器对象（XMLConfigBuilder：专门解析核心配置文件的解析类）
    XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
    // 使用 dom4j XML表达式来完成解析工作
    // 执行parse方法后，会把核心配置文件&映射配置文件都进行解析，并全部封装到configuration中
    Configuration configuration = xmlConfigBuilder.parse(inputStream);

    // 2.创建 SqlSessionFactory 工厂对象
    // 注意：我们通过 defaultSqlSessionFactory 来创建 SqlSession 对象，
    // 而 SqlSession 对象在调用方法与SQL进行交互时，它实际是把这个工作委派给底层的执行器，
    // 在执行器中执行真正的JDBC代码，这就要用到configuration配置信息。
    DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
    return defaultSqlSessionFactory;
  }
}
