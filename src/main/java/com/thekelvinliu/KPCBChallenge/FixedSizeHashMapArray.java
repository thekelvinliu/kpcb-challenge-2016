package com.thekelvinliu.KPCBChallenge;

public class FixedSizeHashMapArray<T> {
    //HELPER CLASS
    public class Node<T> {
        //INSTANCE VARIABLES
        public int key;
        public T value;
        public int height;
        public int left;
        public int right;

        //CONSTRUCTOR
        public Node() {
            this.key = 0;
            this.value = null;
            this.height = 0;
            this.left = -1;
            this.right = -1;
        }

        //METHODS
        public String toString() {
            String retval = "Node with key " + this.key + " and value ";
            if (this.value != null) retval += this.value.toString() + ".";
            else retval += "NULL.";
            return retval;
        }
    }

    //INSTANCE VARIABLES
    public Node[] tree;
    public int rootInd;
    public final int size;
    public int items;

    //CONSTRUCTOR
    public FixedSizeHashMapArray(int size) {
        this.tree = new Node[size];
        for (int i = 0; i < size; i++) this.tree[i] = new Node();
        this.rootInd = -1;
        this.size = size;
        this.items = 0;
    }

    //USER METHODS
    public boolean set(String key, T value) {
        this.tree[this.items].key = key.hashCode();
        this.tree[this.items].value = value;
        if (this.rootInd == -1) {
            this.rootInd = this.items++;
            return true;
        }
        else if (this.items < this.size && value != null) {
            try {
                this.rootInd = this.insert(this.items, this.rootInd); //might fight fail if key is already in use.
                this.items++;
                return true;
            } catch (Exception e) {
                this.cleanNode(this.items);
                return false;
            }
        }
        else {
            this.cleanNode(this.items);
            return false;
        }
    }
    public T get(String key) {
        return null;
    }
    public T delete(String key) {
        return null;
    }
    /**
     * Returns the load (ratio of items to size) of this fixed-size hash map.
     * @return      the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.items/this.size;
    }

    //TREE METHODS
    private int insert(int newInd, int parInd) throws Exception {
        int newParInd = parInd;
        //insert into left subtree
        if (this.tree[newInd].key < this.tree[parInd].key) {
            if (this.tree[parInd].left == -1)
                this.tree[parInd].left = newInd;
            else {
                this.tree[parInd].left = this.insert(newInd, this.tree[parInd].left);
                if (this.balanceFactor(parInd) == 2) {
                    if (this.tree[newInd].key < this.tree[this.tree[parInd].left].key)
                        newParInd = this.rotateCaseLeftLeft(parInd);
                    else
                        newParInd = this.rotateCaseLeftRight(parInd);
                }
            }
        //insert into right subtree
        } else if (this.tree[newInd].key > this.tree[parInd].key) {
            if (this.tree[parInd].right == -1)
                this.tree[parInd].right = newInd;
            else {
                this.tree[parInd].right = this.insert(newInd, this.tree[parInd].right);
                if (this.balanceFactor(parInd) == -2) {
                    if (this.tree[newInd].key < this.tree[this.tree[parInd].left].key)
                        newParInd = this.rotateCaseRightLeft(parInd);
                    else
                        newParInd = this.rotateCaseRightRight(parInd);
                }
            }
        //bad key
        } else throw new Exception("Key already used.");
        this.updateHeight(parInd);
        return newParInd;
    }

    //TREE ROTATIONS
    private int rotateCaseLeftLeft(int startInd) {
        return startInd;
    }
    private int rotateCaseRightRight(int startInd) {
        return startInd;
    }
    private int rotateCaseLeftRight(int startInd) {
        return startInd;
    }
    private int rotateCaseRightLeft(int startInd) {
        return startInd;
    }

    //UTILITIES
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }
    //return true if node at index i is a leaf, false otherwise
    private boolean isLeaf(int i) {
        return (this.tree[i].left == -1 && this.tree[i].right == -1);
    }
    private void cleanNode(int i) {
        this.tree[i].key = 0;
        this.tree[i].value = null;
        this.tree[i].height = 0;
        this.tree[i].left = -1;
        this.tree[i].right = -1;
    }
    //returns balance factor of subtree rooted by node at index i
    private int balanceFactor(int i) {
        return this.tree[i].left - this.tree[i].right;
    }
    //get height of a node at index i
    private int height(int i) {
        return (this.tree[i].value != null) ? this.tree[i].height : -1;
    }
    //update the height of a node at index i
    private void updateHeight(int i) {
        int lInd = this.tree[i].left;
        int rInd = this.tree[i].right;
        if (lInd != -1 && rInd == -1)
            this.tree[i].height = this.tree[lInd].height + 1;
        else if (lInd == -1 && rInd != -1)
            this.tree[i].height = this.tree[rInd].height + 1;
        else
            this.tree[i].height = this.max(this.height(lInd), this.height(rInd)) + 1;
    }
    //inorder printing of subtree rooted by node at index i
    public void inorderPrint() {
        this.inorderPrint(this.rootInd);
    }
    private void inorderPrint(int i) {
        if (i >= 0) {
            this.inorderPrint(this.tree[i].left);
            System.out.println(this.tree[i]);
            this.inorderPrint(this.tree[i].right);
        }
    }
}
