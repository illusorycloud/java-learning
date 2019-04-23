package sort;

import java.util.Arrays;

/**
 * Java常见排序算法
 *
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/21
 */
public class Sort {
    public static void main(String[] args) {
        int[] arr = {10, 22, 33, 11, 44, 21, 40, 81, 19, 5};
        System.out.println(Arrays.toString(arr));
//        bubbleSort(arr);
//        insertSort(arr);
//        selectSort(arr);
//        quickSort(arr, 0, arr.length - 1);
        simpleSelectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void heapSort(int[] arr) {
    }

    /**
     * 简单选择排序 每次找出最小的一个数往前放
     * <p>
     * 首先确定循环次数，并且记住当前数字和当前位置。
     * <p>
     * 将当前位置后面所有的数与当前数字进行对比，小数赋值给key，并记住小数的位置。
     * <p>
     * 比对完成后，将最小的值与第一个数的值交换。
     * <p>
     * 重复2、3步。
     *
     * @param arr
     */
    public static void simpleSelectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int key = arr[i];
            int position = i;
            for (int j = i + 1; j < arr.length; j++) {
                // 如果比其他数大就交换位置
                // 找到最小的值和位置
                if (arr[j] < key) {
                    key = arr[j];
                    position = j;
                }
            }
            //进行交换
            arr[position] = arr[i];
            arr[i] = key;
        }
    }

    /**
     * 快速排序
     * <p>
     * 该方法的基本思想是：
     * <p>
     * 1．先从数列中取出一个数作为基准数。
     * <p>
     * 2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
     * <p>
     * 3．再对左右区间重复第二步，直到各区间只有一个数。
     *
     * @param arr
     * @param start
     * @param end
     */
    public static void quickSort(int[] arr, int start, int end) {
        if (end <= start || arr == null) {
            return;
        }
        int low = start;
        int high = end;
        //初始化基元
        int pivot = arr[low];
        while (low < high) {
            // 从右向左找第一个小于基元的数
            while (low < high && arr[high] >= pivot) {
                high--;
            }
            // 不满足上述条件 即小于 pivot 的数放在低位
            arr[low] = arr[high];

            // 从左向右找第一个大于基元的数
            while (low < high && arr[low] <= pivot) {
                low++;
            }
            // 不满足上述条件 即大于 pivot 的数放在高位
            arr[high] = arr[low];
        }
        //退出时，low等于high。将pivot填到这个坑中
        arr[low] = pivot;
        // 递归排序左半部分
        quickSort(arr, start, low - 1);
        // 递归排序右半部分
        quickSort(arr, low + 1, end);
    }


    /**
     * 选择排序
     *
     * @param arr
     */
    public static void selectSort(int[] arr) {
        int index;
        for (int i = 0; i < arr.length; i++) {
            index = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[index] > arr[j]) {
                    index = j;
                }
            }
            if (i != index) {
                int temp = arr[i];
                arr[i] = arr[index];
                arr[index] = temp;
            }
        }

    }

    /**
     * 插入排序
     *
     * @param arr
     */
    public static void insertSort(int[] arr) {
        int insertNum;
        for (int i = 1; i < arr.length; i++) {
            insertNum = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > insertNum) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = insertNum;
        }
    }

    /**
     * 冒泡排序
     *
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
