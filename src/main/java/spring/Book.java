package spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18
 */
@Data
@Component(value = "book")
public class Book {
    @Value(value = "defaultType")
    private String type;
    @Value(value = "defaultName")
    private String name;
    public Book() {
    }

    public Book(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
