package cn.zhuyee.session;

import cn.zhuyee.executor.Executor;
import cn.zhuyee.pojo.Configuration;
import cn.zhuyee.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

/**
 * <h2>SqlSession默认实现方法</h2>
 * <br>
 * Created by zhuye at 2023/1/1 13:21.
 */
public class DefaultSqlSession implements SqlSession{
  // 配置信息（需要向下传递）
  private Configuration configuration;

  // 上层需要调用底层的Executor对象，所以这里也需要向下传递
  private Executor executor;

  public DefaultSqlSession(Configuration configuration, Executor executor) {
    this.configuration = configuration;
    this.executor = executor;
  }

  @Override
  public <E> List<E> selectList(String statementId, Object param) throws Exception {
    // 将查询操作委派给底层执行器
    // query方法：执行底层JDBC （1.数据库配置信息Configuration中；2.SQL配置信息：MappedStatement中）
    MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
    List<E> list = executor.query(configuration,mappedStatement,param);
    return list;
  }

  @Override
  public <T> T selectOne(String statementId, Object param) throws Exception {
    // 调用selectList()方法
    List<Object> list = this.selectList(statementId, param);
    if (list.size() == 1) {
      return (T) list.get(0);
    } else if (list.size() > 1) {
      throw new RuntimeException("返回结果过多");
    } else {
      return null;
    }
  }

  @Override
  public void close() {
    // 委派给底层执行器去操作
    executor.close();
  }

  @Override
  public <T> T getMapper(Class<?> mapperClass) {
    // 使用JDK动态代理生成基于接口的代理对象
    /**
     * newProxyInstance方法参数说明：
     * 1.类加载器（传入当前类的类加载器
     * 2.字节数组（被代理类所实现的接口，这里指的是Dao层接口）
     * 3.传入处理器接口实现类（代理对象在调用方法时，实际是转发到这个实现类的invoke方法中）
     */
    Object proxy = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
      /**
       * 代理对象调用接口方法实际执行的逻辑
       *
       * @param proxy 代理对象的引用，很少用
       * @param method 被调用的方法的字节码对象（也就是Dao层接口对象）
       * @param args 调用方法的参数
       * @return 返回执行结果
       * @throws Throwable 抛出异常
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 执行具体的逻辑：最终执行底层的JDBC（现在封装在执行器executor中的）
        // 通过调用SqlSession里面的方法来完成具体的逻辑（即通过SqlSession来调用执行器里的方法，而不是直接调用执行器）
        // 进入到SqlSession接口中，发现无论什么方法，都需要参数；
        // 因此，需要先准备参数：1.statementId：cn.zhuyee.dao.IUserDao.findAll；2.param

        // 【问题1】：无法获取现有的 statementId【映射配置文件XxxMapper.xml中的 namespace.id】
        // 解决思路：通过被调用的方法的字节码对象，可以拿到被调用方法的名称 和 该方法所在类的类名称
        // 因此，采用Mapper代理方式时需要遵循一定规范，就是在编写映射配置文件时，namespace的值和标签id的值不能随便写了，要满足以下规范：
        // 1.dao层接口的全路径要和namespace的值保持一致；2.接口中的方法名要和id的值保持一致。

        // 获取被调用方法的名称：findAll
        String methodName = method.getName();
        // 获取当前被调用方法所在类的类名：cn.zhuyee.dao.IUserDao
        String className = method.getDeclaringClass().getName();
        // 组装 statementId
        String statementId = className + "." + methodName;

        // 方法调用：【问题2】：此时不知道要调用SqlSession中增删改查的具体哪一个方法？
        // 解决方法：改造当前工程：在Mapper映射配置对象中新增属性 sqlCommandType 来区分当前操作是什么操作
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        // select | update | delete | insert
        String sqlCommandType = mappedStatement.getSqlCommandType();
        switch (sqlCommandType) {
          case "select":
            // 执行查询方法的调用
            // 【问题3】：该调用selectList还是selectOne呢？
            Type genericReturnType = method.getGenericReturnType();
            // 判断是否实现了【泛型类型参数化】（也就是当前调用方法的返回值结果类型带不带泛型 List<E> ）
            if (genericReturnType instanceof ParameterizedType) {
              // 注意：这里调用的selectXxx方法的第二个参数都是Object对象，但这里传入的是Object数组，需要去第一个
              // 同时，我们调用selectList方法时，是没有参数的，所以直接去args[0]是有可能报空指针异常，需要判断
              if (args != null) {
                return selectList(statementId, args[0]);
              }
              return selectList(statementId, null);
            }
            return selectOne(statementId, args[0]);
          case "update":
            // 执行更新方法的调用
            break;
          case "delete":
            // 执行删除方法的调用
            break;
          case "insert":
            // 执行新增方法的调用
            break;
        }
        return null;
      }
    });
    return (T) proxy;
  }
}
