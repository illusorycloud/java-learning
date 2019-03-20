package jvm.classload;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * 注意：
 * <p>
 * 如果你是直接在当前项目里面创建，待Test.java编译后，请把Test.class文件拷贝走，再将Test.java删除或者改个名字。
 * 因为如果Test.class存放在当前项目中，根据双亲委派模型可知，会通过sun.misc.Launcher$AppClassLoader 类加载器加载。
 * 为了让我们自定义的类加载器加载，我们把Test.class文件放入到其他目录。
 *
 * @author illusory
 */
public class ClassLoadTest {
    @Test
    public void test() throws Exception {
        MyClassLoader classLoader = new MyClassLoader("D:\\lillusory\\Java\\work_idea\\java-learning\\src\\main\\java");
        Class clazz = classLoader.loadClass("jvm.classload.Test");
        Object obj = clazz.newInstance();
        Method helloMethod = clazz.getDeclaredMethod("hello", null);
        helloMethod.invoke(obj, null);
        //恩，是的，我是由 class jvm.classload.MyClassLoader 加载进来的

    }
}
