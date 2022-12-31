package cn.zhuyee.config;

import cn.zhuyee.io.Resources;
import cn.zhuyee.pojo.Configuration;
import com.alibaba.druid.pool.DruidDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * <h2>专门解析核心配置文件的解析类</h2>
 * <br>
 * Created by zhuye at 2022/12/31 20:48.
 */
public class XMLConfigBuilder {

  private Configuration configuration;

  // 创建该对象时，初始化配置类对象，并赋给成员变量
  public XMLConfigBuilder(){
    this.configuration = new Configuration();
  }

  /**
   * <h3>使用 dom4j+xpath 解析配置文件，封装 configuration 对象</h3>
   * @param inputStream 字节输入流
   * @return Configuration 对象
   */
  public Configuration parse(InputStream inputStream) throws DocumentException {
    // 文档对象：对XML结构的封装
    Document document = new SAXReader().read(inputStream);
    // 获取根节点
    Element rootElement = document.getRootElement();

    // ==> 1.根据 xpath 表达式拿到数据库配置信息
    // 一个【<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>】对应一个【Element】
    List<Element> list = rootElement.selectNodes("//property");
    // 存储数据库配置
    Properties properties = new Properties();
    for (Element element : list) {
      String name = element.attributeValue("name");
      String value = element.attributeValue("value");
      properties.setProperty(name, value);
    }
    // 创建数据源对象（用druid连接池解决频繁连接数据库问题）
    DruidDataSource druidDataSource = new DruidDataSource();
    druidDataSource.setDriverClassName(properties.getProperty("driverClassName"));
    druidDataSource.setUrl(properties.getProperty("url"));
    druidDataSource.setUsername(properties.getProperty("username"));
    druidDataSource.setPassword(properties.getProperty("password"));

    // 创建好的数据源对象封装到 Configuration 对象中
    configuration.setDataSource(druidDataSource);


    // ==> 2.解析映射配置文件
    // 2.1.获取映射配置文件路径
    // <mapper resource="mapper/UserMapper.xml"></mapper> 对应一个 Element
    List<Element> mapperList = rootElement.selectNodes("//mapper");
    for (Element element : mapperList) {
      String mapperPath = element.attributeValue("resource");
      // 2.2.根据路径进行映射配置文件的加载解析
      InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
      // mapper解析类：专门解析映射配置文件的对象
      XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
      // 2.3.把解析的SQL语句封装到MappedStatement对象，最终封装到configuration的map集合中
      xmlMapperBuilder.parse(resourceAsStream);
    }
    return configuration;
  }
}
