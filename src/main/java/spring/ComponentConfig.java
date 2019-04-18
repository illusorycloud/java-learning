package spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18
 */
@Configuration
public class ComponentConfig {
    @Bean
    public Student student() {
        Student student = new Student();
        student.setAge(23);
        student.setName("illusory");
        student.setBook(book());
        return student;
    }

    @Bean
    public Book book() {
        Book book = new Book();
        book.setName("Think in Java");
        book.setType("Java");
        return book;
    }
}
