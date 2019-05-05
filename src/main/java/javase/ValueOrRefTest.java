package javase;

/**
 * 值传递还是引用传递？
 * <p>
 * Java中只有值传递
 * Java中只有值传递
 * Java中只有值传递
 */
public class ValueOrRefTest {
    public static void main(String[] args) {
        first();
        second();
        third();
    }

    /**
     * 基本类型的变量保存原始值，即它代表的值就是数值本身；
     */
    private static void first() {
        System.out.println("----------- first 基本数据类型 --------------");
        int a = 22;
        //22
        System.out.println(a);
        firstChange(a);
        //22
        System.out.println(a);
        //没有改变
    }

    /**
     * 而引用类型的变量保存引用值，"引用值"指向内存空间的地址，代表了某个对象的引用，而不是对象本身，
     * 对象本身存放在这个引用值所表示的地址的位置。
     */
    private static void second() {
        System.out.println("----------- second 引用类型 --------------");
        Person person = new Person("illusory", 22);
        //Person{name='illusory', age=22}
        System.out.println(person);
        secondChange(person);
        //Person{name='NewPerson', age=22}
        System.out.println(person);
        //name变了 难道是引用传递？
    }

    /**
     * 和 second一样 只是threeChange中加了一句
     */
    private static void third() {
        System.out.println("----------- third 引用类型 --------------");
        Person person = new Person("illusory", 22);
        //Person{name='illusory', age=22}
        System.out.println(person);
        threeChange(person);
        //Person{name='illusory', age=22}
        System.out.println(person);
        //没有发生变化 说明是值传递
    }

    private static void firstChange(int i) {
        i = 23;
        System.out.println("i=23");
    }

    private static void secondChange(Person person) {
        person.setName("NewPerson");
    }

    /**
     * 对形参的修改不会影响到实参。形参只是实参的一个拷贝，是两个不同的东西。
     * 但是因为形参和实参的值相同，都指向同一个对象，所以对形参所指向的对象的修改会影响到实参所指向的对象。
     *
     * @param person
     */
    private static void threeChange(Person person) {
        //加了这一句 name就不会变了
        //说明不是引用传递而是值传递
        //传递的值是原对象的拷贝 只是都指向了同一个对象 所以对参数的修改会导致对象发生变化
        person = new Person();
        person.setName("NewPerson");
    }

    static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
