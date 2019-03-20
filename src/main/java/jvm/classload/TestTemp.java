package jvm.classload;

/**
 * 编译完成后改名在测试 不然根据双亲委派模型可知，
 * 会通过sun.misc.Launcher$AppClassLoader 类加载器加载
 *
 * @author illusory
 */
public class TestTemp {
    public void hello() {
        System.out.println("恩，是的，我是由 " + getClass().getClassLoader().getClass()
                + " 加载进来的");
    }
}
