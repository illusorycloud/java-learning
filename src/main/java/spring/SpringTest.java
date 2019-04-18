package spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18
 */
public class SpringTest {

    public static void main(String[] args) {
        // 根据配置文件创建 IoC 容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 从容器中获取 bean 实例
        Student student = (Student) ac.getBean("student");
        // 使用bean
        System.out.println(student.getName());
        System.out.println(student.getBook().getName());
        //成功打印出 illusory
    }

}
