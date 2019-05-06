package jvm.integer;

/**
 * Interger测试
 */
public class IntegerTest {
    public static void main(String[] args) {
        Integer i1 = 40;
        Integer i2 = 40;
        Integer i3 = 0;
        Integer i4 = new Integer(40);
        Integer i5 = new Integer(40);
        Integer i6 = new Integer(0);

        System.out.println("i1=i2   " + (i1 == i2));	//true 缓存
        /*
        语句 i1 == i2 + i3，因为+这个操作符不适用于 Integer 对象，首先 i2 和 i3 进行自动拆箱操作，进行数值相加，即 i1 == 40。
        然后 Integer 对象无法与数值进行直接比较，所以 i1 自动拆箱转为 int 值 40，最终这条语句转为 40 == 40 进行数值比较。
         */
        System.out.println("i1=i2+i3   " + (i1 == i2 + i3));	//true
        System.out.println("i1=i4   " + (i1 == i4)); 	//false i4为new的对象
        System.out.println("i4=i5   " + (i4 == i5));	//false i4 i5都为new的对象
        /*
        语句 i4 == i5 + i6，因为+这个操作符不适用于 Integer 对象，首先 i5 和 i6 进行自动拆箱操作，进行数值相加，即 i4 == 40。
        然后 Integer 对象无法与数值进行直接比较，所以 i4 自动拆箱转为 int 值 40，最终这条语句转为 40 == 40 进行数值比较。
         */
        System.out.println("i4=i5+i6   " + (i4 == i5 + i6));  //true 自动拆箱
        System.out.println("40=i5+i6   " + (40 == i5 + i6)); //true  自动拆箱

    }
}
