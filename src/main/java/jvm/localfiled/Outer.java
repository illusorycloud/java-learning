package jvm.localfiled;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

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

    public static void main(String[] args) {
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
}
