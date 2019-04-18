package spring;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18 0018
 */
public class BookFactory {
    //非静态方法
    public Book createBook() {
        Book book = new Book();
        book.setName("图解HTTP");
        book.setType("HTTP");
        return book;
    }

    //静态方法
    public static Book createBookStatic() {
        Book book = new Book();
        book.setName("大话数据结构");
        book.setType("数据结构");
        return book;
    }
}
