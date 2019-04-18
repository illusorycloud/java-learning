package utils;

import java.util.Arrays;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2019/4/17 0017
 */
public class ToHex {

    private static StringBuffer s=new StringBuffer();

    public static void main(String[] args) {
//        byte[] ten = new byte[]{20, 0, 2, 0, 6, 3, 0, 0, 0, 0, 0};
        byte[] ten = new byte[]{-70,32,0,10,0,39,0,27,20,0,1,0,5,3,0,0,0,0};
        for (int valueTen : ten) {
            //定义一个十进制值
            //将其转换为十六进制并输出
            String strHex = Integer.toHexString(valueTen);
            s.append(",").append(strHex);
        }
        System.out.println(s);
    }

}
