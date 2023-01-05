/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */

public class FibonacciHeap
{
    private HeapNode min;
    private HeapNode first;
    private int size;
    private int mark_cnt;
    private static int cuts_cnt;
    private static int links_cnt;
    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */
    public boolean isEmpty()
    {
        return this.size ==0;
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     */
    public HeapNode insert(int key)
    {
        HeapNode node = new HeapNode(key);
        if (this.isEmpty()){
            this.min = node;
            this.min.right = min;
            this.min.left = min;
            this.first = node;
        }
        else{
            this.first.add_latest(node);
            first = node;
            //this.min.add_brother(node);

        }
        if (node.getKey()<this.min.getKey()){
            this.min = node;
        }
        this.size+=1;
        return node;

    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    public void deleteMin()
    {

        if (this.min.right == this.min && this.min.left == this.min && this.min.child == null) {
            this.min = null;
            this.size-=1;
            return;
        }
        else if (this.min.child == null){
            HeapNode temp_min = this.min.right;
            this.min.removeMin();
            this.min = temp_min;
            this.size-=1;
        }
        else{
            HeapNode temp = this.min.child;
            temp.parent = null;
            this.first.add_latest(temp);
            temp = temp.right;
            while(temp != this.min.child){
                temp.parent = null;
                this.first.add_latest(temp);
                temp = temp.right;
            }
            HeapNode temp_min = this.min.right;
            this.min.removeMin();
            this.min = temp_min;
            this.size-=1;
        }
        this.Successive_Linking();
        return;
    }

    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    public void findNewMin(HeapNode[] arr){
        this.min = this.min.right;
        for (HeapNode node: arr){
            if (node != null){
                if (node.getKey()<min.getKey()){
                    min = node;
                }
            }

        }
    }
    public void Successive_Linking (){
        HeapNode[] rank_arr = new HeapNode[this.size+1];
        HeapNode[] roots_arr = new HeapNode[this.size];
        int i=0;
        HeapNode temp = this.min;
        roots_arr[i] = temp;
        i+=1;
        while ((temp!= this.min || i==1)&& i<roots_arr.length){
            temp = temp.right;
            roots_arr[i] = temp;
            i+=1;
        }
        this.findNewMin(roots_arr);
        i=0;
        temp = this.min;
        while ((temp != this.min || i==0)&& i<roots_arr.length){
            if (temp == null){
                System.out.println(i);
                break;
            }
            //System.out.println("h");
            //if (temp.left == null || temp.right == null || temp == null)
            //   System.out.println(temp.getKey());
            if (rank_arr[temp.getRank()] == null){
                rank_arr[temp.getRank()] = temp;
            }
            else{
                while(rank_arr[temp.getRank()] != null){
                    HeapNode new_node = Link(rank_arr[temp.getRank()], temp);
                    rank_arr[temp.getRank()] = null;
                    temp = new_node;
                }
            }
            rank_arr[temp.getRank()] = temp;
            i+=1;
            if (i<roots_arr.length)
                temp = roots_arr[i];
        }
        HeapNode first_node = null;
        HeapNode last_node = null;
        for (HeapNode root: roots_arr){
            if (root == null)
                continue;
            if (first_node==null)
                first_node = root;
            if (last_node==null){
                last_node = root;
                continue;
            }
            if (root!= null){
                root.left = last_node;
                root.right = first_node;
            }
        }
    }
    public HeapNode Link(HeapNode node1, HeapNode node2){
        HeapNode smaller;
        HeapNode bigger;
        if (node1.getKey()<node2.getKey()){
            smaller = node1;
            bigger = node2;
        }
        else {
            smaller = node2;
            bigger = node1;
        }
        bigger.parent = smaller;
        if (smaller.child!= null){
            smaller.child.add_latest(bigger);
            smaller.child = bigger;
            smaller.setRank(smaller.getRank()+1);
        }
        else{
            smaller.child = bigger;
            bigger.right = bigger;
            bigger.left = bigger;
            smaller.setRank(smaller.getRank()+1);
        }
        links_cnt+=1;
        return smaller;
    }
    public HeapNode findMin()
    {
        return this.min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2)
    {
        HeapNode first1 = this.first;
        HeapNode first2 = heap2.first;
        HeapNode last1 = first1.left;
        HeapNode last2 = first2.left;
        last1.right = first2;
        last2.right = first1;
        first1.left = last2;
        first2.left = last1;
        if (this.min.key>heap2.min.key)
            this.min = heap2.min;
        return;
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    public int size()
    {
        return this.size;
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     *
     */
    public int[] countersRep()
    {
        int max_rank = this.min.getRank();
        HeapNode temp = this.min.right;
        while (temp!=this.min){
            if (temp.getRank()>max_rank){
                max_rank = temp.getRank();
            }
        }
        int[] arr = new int[max_rank+1];
        temp = this.min.right;
        arr[this.min.getRank()]+=1;
        while (temp!=this.min){
            arr[temp.getRank()]+=1;
            temp = temp.right;
        }
        return arr;
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    public void delete(HeapNode x)
    {
        this.decreaseKey(x,Integer.MAX_VALUE);
        this.deleteMin();
        return;
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta)
    {
        x.key-=delta;
        if (x.parent == null) {
            if (x.getKey() < this.min.getKey()) {
                this.min = x;
            }
            return;
        }
        if (x.getKey()<x.parent.getKey()){
            cascading_cut(x,x.parent);
        }

    }
    public void cascading_cut(HeapNode x, HeapNode y){
        cut(x,y);
        if (y.parent!= null){
            if (!y.marked){
                this.mark_cnt+=1;
            }
            else
                cascading_cut(y,y.parent);

        }
    }
    public void cut(HeapNode x, HeapNode y){
        FibonacciHeap.cuts_cnt+=1;
        x.parent = null;
        x.marked = false;
        y.rank-=1;
        if (x.right == x){
            y.child = null;
        }
        else{
            y.child = x.right;
            x.left.right = x.right;
            x.right.left = x.left;
        }
        this.first.add_latest(x);
        if (x.getKey()<min.getKey())
            min = x;
    }

    /**
     * public int nonMarked()
     *
     * This function returns the current number of non-marked items in the heap
     */
    public int nonMarked()
    {
        return this.size - this.mark_cnt; // should be replaced by student code
    }

    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     *
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    public int potential()
    {
        if (this.size == 0){
            return 0;
        }
        HeapNode temp = this.min.right;
        int cnt=1;
        while (temp!= this.min){
            temp = temp.right;
            cnt+=1;
        }
        return cnt + (2*this.mark_cnt) ;
    }

    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    public static int totalLinks()
    {
        return links_cnt;
    }

    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts()
    {
        return cuts_cnt;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }

    public HeapNode first_node(){return  this.first;} // returns the first node of the heap

    public int total_links(){return FibonacciHeap.links_cnt;} // Returns the number of links occurred throughout the program
    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */
    public static class HeapNode{

        public int key;
        private HeapNode child;
        private HeapNode parent;
        private HeapNode right;
        private HeapNode left;
        private boolean marked;
        private int rank;


        public HeapNode(int key) {
            this.key = key;
            this.marked = false;
            this.rank = 0;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getKey() {
            return this.key;
        }
        public void add_brother(HeapNode node){
            node.right = this.right;
            this.right.left = node;
            node.left = this;
            this.right = node;
            if (this.parent!= null)
                this.parent.rank+=1;
        }
        public void add_latest(HeapNode node){
            node.right = this;
            this.left.right = node;
            node.left = this.left;
            this.left = node;
            if (this.parent!= null)
                this.parent.rank+=1;
        }
        public void removeMin(){
            this.right.left = this.left;
            this.left.right = this.right;
        }
    }
}


