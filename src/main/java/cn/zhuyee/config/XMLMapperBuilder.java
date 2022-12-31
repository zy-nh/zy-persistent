package cn.zhuyee.config;

import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * <h2>专门解析映射配置文件(XxxMapper.xml)的对象</h2>
 * <p>声明一个方法parse：解析映射配置文件 ==> mappedStatement ==> configuration里的map集合中</p>
 * <br>
 * Created by zhuye at 2023/1/1 0:25.
 */
public class XMLMapperBuilder {

  // 因为最终会封装到 configuration 中，所以这里定义属性，在调用有参构造时传入
  private Configuration configuration;

  public XMLMapperBuilder(Configuration configuration) {
    this.configuration = configuration;
  }

  public void parse(InputStream resourceAsStream) throws DocumentException {
    Document document = new SAXReader().read(resourceAsStream);
    Element rootElement = document.getRootElement();

    // 解析 select 标签
    List<Element> selectList = rootElement.selectNodes("//select");
    // 根标签值
    String namespace = rootElement.attributeValue("namespace");
    for (Element element : selectList) {
      // 遍历每个 Element
      String id = element.attributeValue("id");
      String resultType = element.attributeValue("resultType");
      String parameterType = element.attributeValue("parameterType");
      String sql = element.getTextTrim();

      // 封装 MappedStatement 对象
      MappedStatement mappedStatement = new MappedStatement();

      // statementId: namespace.id
      String statementId = namespace + "." + id;
      mappedStatement.setStatementId(statementId);
      mappedStatement.setResultType(resultType);
      mappedStatement.setParameterType(parameterType);
      mappedStatement.setSql(sql);

      // 把封装好的 MappedStatement对象，封装到 configuration 的map集合中
      configuration.getMappedStatementMap().put(statementId, mappedStatement);
    }
  }
}
