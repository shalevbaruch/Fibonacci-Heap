import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import static java.lang.Math.log;
import static java.lang.Math.pow;

public class theoretical_part {
    public static void main(String[] args) {
//        System.out.println("question1 - m == 2**1" + build((int)pow(2,1)));
//        System.out.println("question1 - m == 2**2" + build((int)pow(2,2)));
        System.out.println("question1 - m == 2**3" + build((int)pow(2,3)));
//       System.out.println("question1 - m == 2**5" + build((int)pow(2,5)));
//   System.out.println("question1 - m == 2**10" + build((int)pow(2,10)));
//       System.out.println("question1 - m == 2**15" + build((int)pow(2,15)));
//        System.out.println("question1 - m == 2**20" + build((int)pow(2,20)));
//        System.out.println("end of question 1");
//        System.out.println("starting question 2");
//        System.out.println("question 2 - m == 3**6 - 1" + build2((int)pow(3,6) - 1));
//        System.out.println("question 2 - m == 3**8 - 1" + build2((int)pow(3,8) - 1));
//        System.out.println("question 2 - m == 3**10 - 1" + build2((int)pow(3,10) - 1));
//        System.out.println("question 2 - m == 3**12 - 1" + build2((int)pow(3,12) - 1));
//        System.out.println("question 2 - m == 3**14 - 1" + build2((int)pow(3,14) - 1));
    }
    public static List<Double> build(int m){
        long startTime = System.currentTimeMillis();
        List<Double> result = new ArrayList<>(); // result includes the following: runtime,totallinks,totalcuts,potential
        FibonacciHeap.HeapNode[] list_of_nodes = new FibonacciHeap.HeapNode[m];
        FibonacciHeap heap = new FibonacciHeap();
        int j = 0;
        for (int i = m-1; i > -2; i--){
            heap.insert(i);
            if (i != -1){
                list_of_nodes[j] = heap.first_node();
            }
            j += 1;
        }
        heap.deleteMin();
        double k = log(m) / log(2);
        int start_of_loop = (int)k;
        for (double i = start_of_loop; i>0; i--){
            int y = (int)pow(2,i);
            heap.decreaseKey(list_of_nodes[y - 2], m + 1);
        }
    heap.decreaseKey(list_of_nodes[1], m+1);
        long elapsedTime = System.currentTimeMillis() - startTime;
        result.add(elapsedTime *  1.0);
        result.add((double) (heap.total_links()));
        result.add((double) FibonacciHeap.totalCuts());
        result.add((double) heap.potential());
        return result;
    }
    public static List<Double> build2(int m){
        long startTime = System.currentTimeMillis();
        List<Double> result = new ArrayList<>();
        FibonacciHeap heap = new FibonacciHeap();
    for (int k = 0; k < m+1; k++){
        heap.insert(k);
    }
    for (int i = 1; i < ((3*m)/4) + 1; i++){
        heap.deleteMin();
    }
    long elapsedTime = System.currentTimeMillis() - startTime;
    result.add(elapsedTime * 1.0);
        result.add((double) (heap.total_links()));
        result.add((double) FibonacciHeap.totalCuts());
        result.add((double) heap.potential());
        return result;
    }
}
