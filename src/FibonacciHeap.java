public class FibonacciHeap
{
    private HeapNode min;
    private HeapNode first;
    private int size;
    private int mark_cnt;
    private static int cuts_cnt;
    private static int links_cnt;
    private int trees_cnt;

    public HeapNode getFirst() {
        return this.first;
    }
    public HeapNode findMin(){
        return this.min;
    }

    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */

    public boolean isEmpty()
    {
        return this.size ==0 || this.first == null;
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
        if (this.isEmpty()){  //check if this the first node to be inserted
            this.min = node;
            this.min.right = min;
            this.min.left = min;
            this.first = node;
        }
        else{  // add new node to the left of the first node
            this.first.add_latest(node);
            this.first = node;
            //this.min.add_brother(node);

        }
        if (node.getKey()<this.min.getKey()){ //update min if necessary
            this.min = node;
        }
        this.trees_cnt+=1;
        this.size+=1; // update size
        return node;

    }
    public HeapNode KminInsert(HeapNode node){
        HeapNode temp = this.insert(node.getKey());
        temp.setSource(node);
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

        if (this.min.right == this.min && this.min.left == this.min && this.min.child == null) { // check if heap has only one node
            this.min = null;
            this.size-=1;
            this.first = null;
            this.trees_cnt-=1;
            return;
        }
        else if (this.min.child == null){ // check if min has no children, and then delete from the heap
            if (this.min == this.first){
                this.first = this.first.right;
            }
            HeapNode temp_min = this.min.right;
            this.min.removeMin();
            this.min = temp_min;
            this.size-=1;
        }
        else{ // if min has children, iterate over them while disconnecting them from min and adding them to the heap as independent trees
            HeapNode temp = this.min.child;
            temp.parent = null;
            if (temp.marked){
                this.mark_cnt -= 1;
                temp.marked = false;
            }
            HeapNode temp2 = temp.right;
            this.min.add_latest2(temp);
            HeapNode origMin = this.min;
            this.min = temp;
            //this.first.add_latest(temp);
            //this.first = temp;
            //this.trees_cnt+=1;
            int i=0;
            HeapNode temp3 = null;
            //HeapNode temp4 = null;
            while(temp2 != origMin.child && temp3!=origMin.child){
                if (i==0){
                    temp2.parent = null;
                    temp3 = temp2.right;
                   // this.first.add_latest(temp2);
                    this.min.add_latest2(temp2);
                    this.trees_cnt+=1;
                    //this.first = temp2;
                    this.min = temp2;
                    if (temp2.marked){
                        this.mark_cnt -= 1;
                        temp2.marked = false;
                    }
                    i=1;
                }
                else {
                    temp3.parent = null;
                    temp2 = temp3.right;
                    //this.first.add_latest(temp3);
                    this.min.add_latest2(temp3);
                    this.trees_cnt+=1;
                    //this.first = temp3;
                    this.min = temp3;
                    if (temp3.marked){
                        this.mark_cnt -= 1;
                        temp3.marked = false;
                    }
                    i=0;
                }
            }

            //HeapNode temp_min = this.min.right;
            if (origMin == this.first){
                this.first = origMin.right;
            }
            origMin.removeMin();
            //this.min = temp_min;
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
    public HeapNode findMin(HeapNode[] arr){ // iterate over all roots to find new min
        //this.min = this.first;
        //int min_val=0;
        boolean flag = true;
        for (HeapNode node: arr){
            if (node != null){
                if (flag){
                    //min_val = node.getKey();
                    this.min = node;
                    flag = false;
                }
                else{
                    if(node.getKey()<min.getKey()){
                        min = node;
                    }
                }
            }

        }
        return min;
    }
    public void Successive_Linking (){
        int cnt=0;
        HeapNode[] rank_arr = new HeapNode[((int)Math.ceil(Math.log(this.size)/Math.log(2)))+1];
        HeapNode[] roots_arr = new HeapNode[this.trees_cnt];
        int i=0;
        HeapNode temp = this.first;
        roots_arr[i] = temp;
        i+=1;
        //cnt=1;
        while ((temp!= this.first || i==1)&& i<roots_arr.length){// intialization of roots_arr
            if (temp.right == this.first){
                i=roots_arr.length+1;
                continue;
            }
            cnt+=1;
            temp = temp.right;
            roots_arr[i] = temp;
            i+=1;
        }
        i=0;

        temp = this.first;
        while ((temp != this.first || i==0)&& i<roots_arr.length){ // fill buckets and link if necessary as seen in class
            if (temp == null){
                //System.out.println(i);
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
                    int rank = temp.getRank();
                    HeapNode new_node = Link(rank_arr[temp.getRank()], temp);
                    rank_arr[rank] = null;
                    temp = new_node;
                }
            }
            rank_arr[temp.getRank()] = temp;
            i+=1;
            if (i<=cnt)
                temp = roots_arr[i];
            else {
                    i = roots_arr.length;

            }
        }
        this.findMin(rank_arr);
        //rank_arr[temp.getRank()] = temp;
        HeapNode check = null;
        int new_size=0;
        for (int j=rank_arr.length-1;j>=0;j--){ // rearranging attributes of the new roots
            HeapNode root = rank_arr[j];
            if (root == null)
                continue;
            if (check==null){
                new_size+=1;
                this.first = root;
                this.first.left = root;
                this.first.right = root;
                if (root.marked){
                    this.mark_cnt -= 1;
                    root.marked = false;
                }
                check = root;
                continue;
            }
            if (root!= null){
                new_size+=1;
                this.first.add_latest(root);
                this.first = root;
                if (root.marked){
                    this.mark_cnt -= 1;
                    root.marked = false;
                }

            }
        }
        this.trees_cnt = new_size;
    }
    public HeapNode Link(HeapNode node1, HeapNode node2){ // make the smaller root a child of the bigger root
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
            //smaller.setRank(smaller.getRank()+1);
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


    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2) // add heap2 to the end of current heap
    {
        if (heap2.isEmpty()) {
            return;
        }
        if (this.isEmpty()){
            this.first = heap2.first;
            this.min = heap2.min;
            this.size = heap2.size();
            this.trees_cnt = heap2.trees_cnt;
            this.mark_cnt = heap2.mark_cnt;
            return;
        }
        HeapNode last1 = this.first;
        HeapNode last2 = heap2.first;
        HeapNode first1 = last1.left;
        HeapNode first2 = last2.left;
        first1.right = last2;
        first2.right = last1;
        last1.left = first2;
        last2.left = first1;
        if (this.min.key>heap2.min.key)
            this.min = heap2.min;
        this.size+= heap2.size();
        this.trees_cnt += heap2.trees_cnt;
        this.mark_cnt += heap2.mark_cnt;
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
        if (this.isEmpty() || this.min == null){
            return new int[0];
        }
        int max_rank = this.min.getRank();
        HeapNode temp = this.min.right;
        while (temp!=this.min){ // find max_rank
            if (temp.getRank()>max_rank){
                max_rank = temp.getRank();
            }
            temp = temp.right;
        }
        int[] arr = new int[max_rank+1];
        temp = this.min.right;
        arr[this.min.getRank()]+=1;
        while (temp!=this.min){ // iterating over all roots and filling arr
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
        if (this.min.getKey() == x.key){
            this.deleteMin();
            return;
        }
        this.decreaseKey(x,x.getKey()- min.getKey()+1);
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
        x.key-=delta; // update key
        if (x.parent == null) {
            if (x.getKey() < this.min.getKey()) { // update min if necessary
                this.min = x;
            }
            return;
        }
        if (x.getKey()<x.parent.getKey()){ // cascading cut from parent if necessary
            cascading_cut(x,x.parent);
        }

    }
    public void cascading_cut(HeapNode x, HeapNode y){
        cut(x,y);
        if (y.parent!= null){
            if (!y.marked){ // mark y if wasn't marked
                this.mark_cnt+=1;
                y.marked = true;
            }
            else
                cascading_cut(y,y.parent); // call cascading_cut with parent

        }
    }
    public void cut(HeapNode x, HeapNode y){
        FibonacciHeap.cuts_cnt+=1;
        x.parent = null;
        if (x.marked){ // unmark x if it was marked
            this.mark_cnt -= 1;
            x.marked = false;
        }
        y.rank-=1;
        if (x.right == x){
            y.child = null;
        }
        else{
            y.child = x.right;
            x.left.right = x.right;
            x.right.left = x.left;
        }
        this.first.add_latest(x); // add x as root
        this.first = x;
        this.trees_cnt+=1;
        if (x.getKey()<min.getKey()) // update min if necessary
            min = x;
    }

    /**
     * public int nonMarked()
     *
     * This function returns the current number of non-marked items in the heap
     */
    public int nonMarked()
    {
        return this.size - this.mark_cnt;
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
        return FibonacciHeap.links_cnt;
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
        int[] ret = new int[k];
        if (k==0)
            return new int[0];
        FibonacciHeap aid = new FibonacciHeap();
        aid.KminInsert(H.min);
        int i=0;
        while (i<k){
            int minKey = aid.min.key;
            HeapNode source = aid.min.getSource();
            ret[i] = minKey;
            HeapNode child = source.child;
            if (child == null){
                i+=1;
                aid.deleteMin();
                continue;
            }
            else{
                aid.KminInsert(child);
                child = child.right;
                while (child!= source.child){
                    aid.KminInsert(child);
                    child = child.right;
                }
            }
            aid.deleteMin();
            i+=1;

        }
        return ret; // should be replaced by student code
    }

    public HeapNode first_node(){return  this.first;} // returns the first node of the heap

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */
    public static class HeapNode{

        private int key;
        private HeapNode child;
        private HeapNode parent;
        private HeapNode right;
        private HeapNode left;
        private boolean marked;
        private int rank;
        private HeapNode source; // for kMin


        public HeapNode(int key) {
            this.key = key;
            this.marked = false;
            this.rank = 0;
        }

        public HeapNode getSource() {
            return source;
        }

        public void setSource(HeapNode source) {
            this.source = source;
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

        public void add_latest(HeapNode node){
            node.right = this;
            this.left.right = node;
            node.left = this.left;
            this.left = node;
            if (this.parent!= null)
                this.parent.rank+=1;
        }
        public void add_latest2(HeapNode node){
            node.left = this;
            this.right.left = node;
            node.right = this.right;
            this.right = node;
            if (this.parent!= null)
                this.parent.rank+=1;
        }
        public void removeMin(){
            this.right.left = this.left;
            this.left.right = this.right;
        }

        public HeapNode getChild() {
            return child;
        }

        public boolean getMarked() {
            return this.marked;
        }

        public HeapNode getParent() {
            return parent;
        }

        public HeapNode getPrev() {
            return left;
        }

        public HeapNode getNext() {
            return right;
        }
    }
}
