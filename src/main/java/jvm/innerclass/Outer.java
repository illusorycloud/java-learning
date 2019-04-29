package jvm.innerclass;

/**
 * 测试内部类
 * Java中为什么内部类可以访问外部类的成员
 */
public class Outer {
    int outerField = 0;

    class Inner {
        void InnerMethod() {
            int i = outerField;
        }
    }
}
