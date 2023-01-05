import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import static java.lang.Math.log;
import static java.lang.Math.pow;

public class theoretical_part {
    public static void main(String[] args) {
        System.out.println((int)pow(2,5));
    //build((int)pow(2,5));
    }
    public static List<Double> build(int m){
        long startTime = System.nanoTime();
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
        long elapsedTime = System.nanoTime() - startTime;
        result.add(elapsedTime *  0.000001);
        result.add((double) (heap.total_links()));
        result.add((double) FibonacciHeap.totalCuts());
        result.add((double) heap.potential());
        return result;
    }
}
