package com.thekelvinliu.KPCBChallenge;

public class FixedSizeHashMap<T> {
    //HELPER CLASS
    class Node<T> {
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
            String retval = "(" + this.key + ", ";
            retval += (this.value != null) ? this.value.toString() + ")" : "NULL)";
            return retval;
        }
    }

    //INSTANCE VARIABLES
    private Node[] tree;
    private int rootInd;
    private final int size;
    private int items;

    //CONSTRUCTOR
    public FixedSizeHashMap(int size) {
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
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
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
        if (this.rootInd == -1)
            return null;
        else {
            int nodeInd = this.find(key.hashCode(), this.rootInd);
            if (nodeInd != -1) return (T) this.tree[nodeInd].value;
            else return null;
        }
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
    public int getItemCount() {
        return this.items;
    }
    public int getSize() {
        return this.size;
    }

    //TREE METHODS
    private int insert(int newInd, int parInd) throws IllegalArgumentException {
        int newParInd = parInd;
        //insert into left subtree
        if (this.tree[newInd].key < this.tree[parInd].key) {
            if (this.tree[parInd].left == -1) {
                this.tree[parInd].left = newInd;
            }
            else {
                this.tree[parInd].left = this.insert(newInd, this.tree[parInd].left);
                if (this.balanceFactor(parInd) == 2) {
                    int lInd = this.tree[parInd].left;
                    if (lInd != -1 && this.tree[newInd].key < this.tree[lInd].key) {
                        newParInd = this.rotateCaseLeftLeft(parInd);
                    }
                    else {
                        newParInd = this.rotateCaseLeftRight(parInd);
                    }
                }
            }
        //insert into right subtree
        } else if (this.tree[newInd].key > this.tree[parInd].key) {
            if (this.tree[parInd].right == -1) {
                this.tree[parInd].right = newInd;
            }
            else {
                this.tree[parInd].right = this.insert(newInd, this.tree[parInd].right);
                if (this.balanceFactor(parInd) == -2) {
                    int rInd = this.tree[parInd].right;
                    if (rInd != -1 && this.tree[newInd].key < this.tree[rInd].key) {
                        newParInd = this.rotateCaseRightLeft(parInd);
                    }
                    else {
                        newParInd = this.rotateCaseRightRight(parInd);
                    }
                }
            }
        //bad key
        } else {
            throw new IllegalArgumentException("Key already used.");
        }
        this.updateHeight(parInd);
        return newParInd;
    }
    private int find(int key, int startInd) {
        if (startInd == -1) {
            return startInd;
        } else if (key == this.tree[startInd].key) {
            return startInd;
        } else if (key < this.tree[startInd].key) {
            return this.find(key, this.tree[startInd].left);
        } else {
            return this.find(key, this.tree[startInd].right);
        }
    }

    //TREE ROTATIONS
    private int rotateCaseLeftLeft(int startInd) {
        int newStartInd = this.tree[startInd].left;
        this.tree[startInd].left = this.tree[newStartInd].right;
        this.tree[newStartInd].right = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    private int rotateCaseRightRight(int startInd) {
        int newStartInd = this.tree[startInd].right;
        this.tree[startInd].right = this.tree[newStartInd].left;
        this.tree[newStartInd].left = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    private int rotateCaseLeftRight(int startInd) {
        this.tree[startInd].left = this.rotateCaseRightRight(this.tree[startInd].left);
        return this.rotateCaseLeftLeft(startInd);
    }
    private int rotateCaseRightLeft(int startInd) {
        this.tree[startInd].right = this.rotateCaseLeftLeft(this.tree[startInd].right);
        return this.rotateCaseRightRight(startInd);
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
        return this.height(this.tree[i].left) - this.height(this.tree[i].right);
    }
    //get height of a node at index i
    private int height(int i) {
        return (i != -1) ? this.tree[i].height : -1;
    }
    //update the height of a node at index i
    private void updateHeight(int i) {
        int lInd = this.tree[i].left;
        int rInd = this.tree[i].right;
        if (lInd == -1 && rInd == -1)
            this.tree[i].height = 0;
        else if (lInd != -1 && rInd == -1)
            this.tree[i].height = this.tree[lInd].height + 1;
        else if (lInd == -1 && rInd != -1)
            this.tree[i].height = this.tree[rInd].height + 1;
        else
            this.tree[i].height = this.max(this.height(lInd), this.height(rInd)) + 1;
    }
    //inorder printing of subtree rooted by node at index i
    public void print() {
        System.out.println();
        this.preorderPrint(this.rootInd, "");
    }
    private void preorderPrint(int i, String indent) {
        if (i >= 0) {
            System.out.println(indent + this.tree[i] + " " + this.height(i));
            this.preorderPrint(this.tree[i].left, indent + "  ");
            // if (i == this.rootInd) System.out.print("* ");
            this.preorderPrint(this.tree[i].right, indent + "  ");
        }
    }
    // public void bfsPrint() {
    //     int x, currentLevel, nextLevel;
    //     ArrayDeque<Integer> q = new ArrayDeque<Integer>(this.size);
    //     q.add(this.rootInd);
    //     currentLevel = 1;
    //     nextLevel = 0;
    //     while (!q.isEmpty()) {
    //         x = q.remove();
    //         currentLevel--;
    //         System.out.print(" ");
    //         if (x != -1) {
    //             q.add(this.tree[x].left);
    //             q.add(this.tree[x].right);
    //             nextLevel += 2;
    //             System.out.print(this.tree[x]);
    //         }
    //         else System.out.print("(  ,  )");
    //         System.out.print(" ");
    //         if (currentLevel == 0) {
    //             currentLevel = nextLevel;
    //             nextLevel = 0;
    //             System.out.println();
    //         }
    //     }
    // }
    public Node<T> getRoot() {
        return this.tree[this.rootInd];
    }
}
