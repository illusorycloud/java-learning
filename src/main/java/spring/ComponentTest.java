package spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18
 */
public class ComponentTest {
    @Autowired
    private Student student;
    @Autowired
    private Book book;
    @Test
    public void componentTest() {
        // 根据配置文件创建 IoC 容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//        Student student = (Student) ac.getBean("student");
//        Book book = (Book) ac.getBean("book");
        boolean isEquals = student.getBook() == book;
        System.out.println(isEquals);
    }
}
