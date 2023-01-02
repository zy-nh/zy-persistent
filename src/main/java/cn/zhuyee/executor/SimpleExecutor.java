package cn.zhuyee.executor;

import cn.zhuyee.config.BoundSql;
import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;
import cn.zhuyee.utils.GenericTokenParser;
import cn.zhuyee.utils.ParameterMapping;
import cn.zhuyee.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>执行器实现类</h2>
 * <br>
 * Created by zhuye at 2023/1/1 13:27.
 */
public class SimpleExecutor implements Executor{
  /**
   * 数据库连接对象
   */
  private Connection connection = null;
  /**
   * 预编译对象
   */
  private PreparedStatement preparedStatement = null;
  /**
   * 结果集
   */
  private ResultSet resultSet = null;

  @Override
  public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception {
    // 1.加载驱动，获取数据库连接
    connection = configuration.getDataSource().getConnection();
    // 2.获取preparedStatement预编译对象
    /*
     * 获取要执行的SQL语句：MappedStatement对象中
     * 将参数组装到SQL语句中：通过自定义占位符 #{} 来定位
     * SQL：select * from user where id = #{id} and username = #{username}
     * 替换：select * from user where id = ? and username = ?
     * 注意：在解析替换过程中：#{id}里面的值需要保存下来
     */
    String sql = mappedStatement.getSql();
    BoundSql boundSql = getBoundSql(sql);
    String finalSql = boundSql.getFinalSql();
    // 通过处理后的SQL来获取预编译对象
    preparedStatement = connection.prepareStatement(finalSql);

    // 3.设置参数（反射）
    // parameterType值：cn.zhuyee.pojo.User
    String parameterType = mappedStatement.getParameterType();

    // parameterType不为空时才进行参数设置
    if (parameterType != null) {
      Class<?> parameterTypeClass = Class.forName(parameterType);

      List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
      for (int i = 0; i < parameterMappingList.size(); i++) {
        ParameterMapping parameterMapping = parameterMappingList.get(i);
        // 拿到#{}占位符里面的值
        String paramName = parameterMapping.getContent();
        // 通过反射来确定根据名称paramName找到Object对应的实体类里对应的属性值
        // 先要得到实体类的全限定名来拿到其字节码对象
        // 在字节码中根据名称获取到的这个属性对象
        Field declaredField = parameterTypeClass.getDeclaredField(paramName);
        // 字段可能是私有的，所以要设置暴力访问
        declaredField.setAccessible(true);

        // 获取到这个属性对应实体属性的值
        Object value = declaredField.get(param);
        // 赋值占位符
        preparedStatement.setObject(i+1,value);
      }
    }

    // 4.执行SQL，发起查询
    resultSet = preparedStatement.executeQuery();

    // 5.处理返回结果集（原始JDBC中是对结果集进行遍历，取出每个字段对应的值，再把值通过setter方法塞到实体中）
    // 要实现效果：通过反射技术，根据字段名和实体属性名的对应关系自动完成映射封装
    ArrayList<E> list = new ArrayList<>();
    while (resultSet.next()) {
      // 元数据信息，包含了：字段名、字段的值
      ResultSetMetaData metaData = resultSet.getMetaData();

      // 拿到要封装的实体类的全限定名
      String resultType = mappedStatement.getResultType();
      // 拿到对应的字节码对象
      Class<?> resultTypeClass = Class.forName(resultType);
      // 拿到对应的实例对象（也就是表对应的实体的对象）
      Object o = resultTypeClass.newInstance();

      // 遍历这个元数据对象，来获取字段名及对应的值
      for (int i = 1; i < metaData.getColumnCount(); i++) {
        // 字段名 getColumnName()下标是从1开始的，所以i初始值为1
        String columnName = metaData.getColumnName(i);
        // 字段值
        Object value = resultSet.getObject(columnName);

        // 现在的问题：不知道要把这个字段名和值封装到哪一个实体的哪个属性中
        // 封装：用到类型库：属性描述器，可通过API方法获取某个属性的读写方法
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
        // 得到这个属性的写方法
        Method writeMethod = propertyDescriptor.getWriteMethod();
        // 参数说明：参数1：实例对象；参数2：要设置的值
        writeMethod.invoke(o, value);
      }
      list.add((E) o);
    }
    return list;
  }

  /**
   * <h3>SQL处理</h3>
   * <p>1. #{} 占位符替换为 ? </p>
   * <p>2. 解析替换过程中，将 #{} 里的值保存下来 </p>
   *
   * @param sql SQL语句
   * @return BoundSql
   */
  private BoundSql getBoundSql(String sql) {
    // 1.创建标记处理器：配合标记解析器完成标记处理解析工作
    ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();

    // 2.创建标记解析器
    GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);

    // #{} 占位符替换为 ? ，在替换过程中，将#{}里面的值保存到 ParameterMapping 对象
    String finalSql = genericTokenParser.parse(sql);

    // #{}里面的值的一个集合
    List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

    BoundSql boundSql = new BoundSql(finalSql, parameterMappings);
    return boundSql;
  }

  @Override
  public void close() {
    if (resultSet != null) {
      try{
        resultSet.close();
      } catch (SQLException e){
        e.printStackTrace();
      }
    }
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
