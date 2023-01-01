package cn.zhuyee.session;

/**
 * <h2>SqlSession工厂接口</h2>
 *
 * <br>
 * Created by zhuye at 2022/12/31 20:43.
 */
public interface SqlSessionFactory {

  /**
   * <ol>
   *   <li>生产SqlSession对象</li>
   *   <li>创建执行器对象</li>
   * </ol>
   * @return SqlSession 对象
   */
  SqlSession openSession();
}
