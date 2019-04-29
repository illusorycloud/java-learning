package jvm.localfiled;

/**
 * 为什么Java中方法内定义的内部类可以访问方法中的局部变量
 */
public class Outer {

    void outerMethod(){
        final  String localVar = getString();

        /*定义在方法中的内部类*/
        class Inner{
            void innerMethod(){
                String a = localVar;
            }
        }

        new Inner();
    }

    String getString(){
        return "illusory";
    }
}
