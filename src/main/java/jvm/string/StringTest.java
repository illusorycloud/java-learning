package jvm.string;

/**
 * String 字符串测试
 */
public class StringTest {
    public static void main(String[] args) {
        StringTest();
        addString();
        toStrings();
    }

    /**
     * String类型优化测试
     */
    private static void StringTest() {
        System.out.println("--------  字符串相加 -----------");
        String a = "hello illusory";
        String b = "hello " + "illusory";
        //  true
        System.out.println(a == b);
        System.out.println("--------  String对象相加 -----------");
        String c = "hello ";
        String d = "illusory";
        String e = c + d;
        //false
        System.out.println(a == e);
        System.out.println("-------- +号两边都是常量 -----------");
        final String c2 = "hello ";
        final String d2 = "illusory";
        String e2 = c2 + d2;
        //     true
        System.out.println(a == e2);
        System.out.println("-------- +号有一边不是常量 -----------");
        String f = c2 + getName();
        //     false
        System.out.println(a == f);

        System.out.println("-------- String.intern -----------");
        String g = a.intern();
        System.out.println(a == g);
    }

    private static String getName() {
        return "illusory";
    }

    /**
     * 转换为String时的效率测试
     * num+""  118ms
     * String.valueOf(num) 31ms
     * num.toString()   30ms
     */
    private static void toStrings() {
        Integer num = 0;
        int loop = 10000000;  // 将结果放大10000000倍，以便于观察结果
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            String s = num + "";
        }
        long endTime = System.currentTimeMillis();
        System.out.println("+\"\"的方式耗时: " + (endTime - beginTime) + "ms");


        beginTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            String s = String.valueOf(num);
        }
        endTime = System.currentTimeMillis();
        System.out.println("String.valueOf()的方式耗时: " + (endTime - beginTime) + "ms");

        beginTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            String s = num.toString();
        }
        endTime = System.currentTimeMillis();
        System.out.println("toString()的方式耗时: " + (endTime - beginTime) + "ms");
    }

    /**
     * 字符串拼接方式效率测试
     * num+""   3379ms
     * StringBuffer  3ms
     * StringBuilder 1ms
     */
    private static void addString() {
        String s = "";
        long sBeginTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            s += "s";
        }
        long sEndTime = System.currentTimeMillis();
        System.out.println("s拼接100000遍s耗时: " + (sEndTime - sBeginTime) + "ms");

        StringBuffer s1 = new StringBuffer();
        long s1BeginTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            s1.append("s");
        }
        long s1EndTime = System.currentTimeMillis();
        System.out.println("s1拼接100000遍s耗时: " + (s1EndTime - s1BeginTime) + "ms");

        StringBuilder s2 = new StringBuilder();
        long s2BeginTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            s2.append("s");
        }
        long s2EndTime = System.currentTimeMillis();
        System.out.println("s2拼接100000遍s耗时: " + (s2EndTime - s2BeginTime) + "ms");
    }
}
