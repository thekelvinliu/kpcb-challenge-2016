package com.thekelvinliu.KPCBChallenge;

/**
 * A generic, homogeneous fixed-size hash map.
 *
 * This hash map uses an implicit AVL Tree to maintain a self-balancing binary
 * search tree. Each node of the tree represents an entry in the hash map. The
 * hash map accepts strings as keys and internally stores the hashed values of
 * these strings using {@link java.lang.String#hashCode()}. This class is
 * implemented with java generics, allowing the mapped values to be of any type,
 * however an individual instance of this hash map may only hold a single type.
 * <p>
 * Each node of the implicit tree is stored in an array. A Node has integer
 * fields, which hold the array indices of a node's left and right children in
 * the implicit tree. This hash map uses modified implementations of standard
 * AVL Tree operations to achieve O(log n) set, get, and delete time complexity.
 *
 * @param       <T>         the type of value that this hash map will hold
 * @see         com.thekelvinliu.KPCBChallenge.FixedSizeHashMap.Node
 */
public class FixedSizeHashMap<T> {
    //HELPER CLASS
    /**
     * A generic class that represents a node in a binary tree.
     *
     * Each node holds a key and a value, as well as indices for the node's
     * left and right children. Because this class is not accessible to the
     * outside world, fields will be accessed and modified directly by
     * FixedSizeHashMap for convenience.
     *
     * @param       <T>     the type of the value to be held by this node
     */
    private final class Node<T> {
        //INSTANCE VARIABLES
        /**
         * The key held by this node.
         */
        private int key;
        /**
         * The value held by this node.
         */
        private T value;
        /**
         * The height of this node.
         */
        private int height;
        /**
         * The index of this node's left child.
         */
        private int left;
        /**
         * The index of this node's right child.
         */
        private int right;

        //CONSTRUCTOR
        /**
         * Creates an empty node.
         *
         * All integer fields are set to -1, which in the context of this whole
         * class, refers to being null. The value field is actually set to null.
         */
        private Node() {
            this.clean();
        }

        //METHODS
        /**
         * Returns a string representation of this node.
         * @return      a string representation of this node
         */
        public String toString() {
            String retval = "(" + this.key + ", ";
            retval += (this.value != null) ? this.value.toString() : "NULL";
            retval += ", " + this.left + ", " + this.right + ")";
            return retval;
        }
        /**
         * Resets all fields of this node to their original value (-1 or null).
         */
        private void clean() {
            this.key = -1;
            this.value = null;
            this.height = -1;
            this.left = -1;
            this.right = -1;
        }
    }

    //INSTANCE VARIABLES
    /**
     * The array that holds all of this hash map's nodes.
     *
     * The nodes in this array may be active or inactive in the this hash map's
     * implicit tree. An active node must have a non null value and nonnegative
     * height field.
     */
    private Node[] tree;
    /**
     * The bitmap used to mark which nodes in the array are active.
     *
     * This bitmap is accomplished with a byte array. Because of this, up to 7
     * extra bits might be unused, as they point to indices outside the bounds
     * of the node array.
     */
    private byte[] bitmap;
    /**
     * The array index of this hash map's implicit root.
     */
    private int rootInd;
    /**
     * The array index the node most recently deleted from the implicit tree.
     *
     * This will typically be -1 if a node hasn't been deleted.
     */
    private int delInd;
    /**
     * The fixed size of this hash map.
     */
    private final int size;
    /**
     * The number of items currently in this hash map.
     */
    private int items;

    //CONSTRUCTOR
    /**
     * Creates an instance of a fixed-size hash map.
     *
     * At this time, all nodes are created and put into the array. Because there
     * are not yet any entries in the hash map, the root index is set to -1,
     * and the number of items is set to 0.
     *
     * @param       size    the fixed-size of this hash map
     * @throws      IllegalArgumentException    if size is zero or negative
     */
    public FixedSizeHashMap(int size) {
        if (size > 0) {
            this.tree = new Node[size];
            for (int i = 0; i < size; i++) this.tree[i] = new Node();
            this.bitmap = new byte[size/8 + 1];
            this.rootInd = -1;
            this.delInd = -1;
            this.size = size;
            this.items = 0;
        } else {
            throw new IllegalArgumentException("Size must be a positive integer.");
        }
    }

    //USER METHODS, PRESCRIBED BY KPCB (PUBLIC)
    /**
     * Associates given key to a given value in this hash map.
     *
     * Also returns a boolean indicating the success or failure of this
     * operation. Success depends on the following three constraints:<p>
     * (1) there must be at least one inactive node in this hash map,<p>
     * (2) the given value must not be null,<p>
     * (3) the given key must not already be associated with a value.
     *
     * @param       key     the key to be associated
     * @param       value   the value to be associated
     * @return      a boolean indicating success (true) or failure (false)
     */
    public boolean set(String key, T value) {
        if (this.items < this.size && value != null) {
            int newInd = this.getAvailableNode();
            this.tree[newInd].key = key.hashCode();
            this.tree[newInd].value = value;
            this.tree[newInd].height = 0;
            try {
                //throws an IllegalArgumentException if the key is already used
                this.rootInd = this.insert(newInd, this.rootInd);
                this.bitFlip(newInd);
                this.items++;
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.printf("%d    %d", newInd, this.rootInd);
                return false;
            } catch (IllegalArgumentException e) {
                // e.printStackTrace();
                //clean up and return false
                this.tree[newInd].clean();
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Returns the value associated with a given key.
     *
     * This will fail and return null if there are no entries in this hash map,
     * or if the key is not found.
     *
     * @param       key     the key associated with the returned value
     * @return      the value associated with key (or null)
     */
    public T get(String key) {
        if (this.items > 0 && this.rootInd != -1) {
            //get the index of the node with the given string
            int nodeInd = this.find(key.hashCode(), this.rootInd);
            return (nodeInd != -1) ? (T) this.tree[nodeInd].value : null;
        } else {
            return null;
        }
    }
    /**
     * Deletes the entry with the given key from this hash map.
     *
     * Also returns the keys associated value. This operation can fail if there
     * are no active nodes in the implicit tree, or if the given key is not
     * associated with any values in this hash map. If this happens, null is
     * returned.
     *
     * @param       key     the key of the entry to be deleted
     * @param       key     the key paired with the returned value
     * @return      the value associated with key or null
     */
    public T delete(String key) {
        if (this.items > 0 && this.rootInd != -1) {
            //attempt to remove the node with key from the implicit tree
            this.rootInd = this.remove(key.hashCode(), this.rootInd);
            //this.delInd will hold the index of the node that should be delted
            if (this.delInd != -1) {
                //save the return value
                T retval = (T) this.tree[delInd].value;
                //clean the deleted node and mark as inactive
                this.tree[this.delInd].clean();
                this.bitFlip(this.delInd);
                this.delInd = -1;
                this.items--;
                return retval;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the load (ratio of items to size) of this fixed-size hash map.
     *
     * @return      the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.items/this.size;
    }

    //EXTRAS (PUBLIC)
    /**
     * Returns the number of items currently in this hash map.
     *
     * @return      the number of items currently in this hash map
     */
    public int getItemCount() {
        return this.items;
    }
    /**
     * Returns the maximum number of items that this hash map can hold.
     *
     * @return      the maximum number of items that this hash map can hold
     */
    public int getSize() {
        return this.size;
    }
    /**
     * Clears this hash map by removing every key-value association.
     */
    public void clear() {
        this._clear(this.rootInd);
    }

    //TREE UTILITIES (PRIVATE)
    /**
     * Inserts the node at newInd to the subtree rooted by the node at startInd.
     *
     * This method recursively traverses the implicit subtree rooted by the node
     * at startInd in order to find the proper place to insert the new node. It
     * then calls {@link FixedSizeHashMap#rebalance} to enforce the height
     * invariant for AVL Trees.
     *
     * @param       newInd      the index of the node to be inserted
     * @param       startInd    the index of the subtree root
     * @return      the index of the (new) subtree root
     * @throws      IllegalArgumentException    if the key of the node at newInd
     *                                          is already used in the hash map
     */
    private int insert(int newInd, int startInd) throws IllegalArgumentException {
        //insert at startInd if startInd isn't actually part of the implicit tree
        if (startInd == -1) {
            return newInd;
        }
        //insert into left subtree
        else if (this.tree[newInd].key < this.tree[startInd].key) {
            this.tree[startInd].left = this.insert(newInd, this.tree[startInd].left);
        }
        //insert into right subtree
        else if (this.tree[newInd].key > this.tree[startInd].key) {
            this.tree[startInd].right = this.insert(newInd, this.tree[startInd].right);
        }
        //duplicate key
        else {
            throw new IllegalArgumentException("Key already used.");
        }
        //rebalance tree and return
        return this.rebalance(startInd);
    }
    /**
     * Returns the index of the node with the given key.
     *
     * This method recursively traverses the tree, beginning at the node at
     * index startInd. If startInd is not actually a node in the tree, -1 will
     * be returned, indicating that no node was found with the given key.
     *
     * @param       key         the key to search for
     * @param       startInd    the index of the subtree root
     * @return      the index of the node with the given key
     */
    private int find(int key, int startInd) {
        if (startInd == -1) {
            return -1;
        } else if (key == this.tree[startInd].key) {
            return startInd;
        } else if (key < this.tree[startInd].key) {
            return this.find(key, this.tree[startInd].left);
        } else {
            return this.find(key, this.tree[startInd].right);
        }
    }
    /**
     * Removes node with key from the subtree rooted by node at startInd.
     *
     * This method recursively traverses the implicit subtree rooted by the node
     * at startInd to find and remove the node with the given key. Depending on
     * the number of children the found node has, it may or may not rebalance
     * the implicit tree using {@link FixedSizeHashMap#rebalance}.
     *
     * @param       key         the key of the node to be removed
     * @param       startInd    the index of the subtree root
     * @return      the index of the (new) subtree root
     */
    private int remove(int key, int startInd) {
        //startInd isn't actually part of the implicit tree
        if (startInd == -1) {
            this.delInd = -1;
            return -1;
        }
        //remove from left subtree
        else if (key < this.tree[startInd].key) {
            this.tree[startInd].left = this.remove(key, this.tree[startInd].left);
            return this.rebalance(startInd);
        }
        //remove from right subtree
        else if (key > this.tree[startInd].key) {
            this.tree[startInd].right = this.remove(key, this.tree[startInd].right);
            return this.rebalance(startInd);
        }
        //startInd is the node to be removed
        else {
            int lInd = this.tree[startInd].left;
            int rInd = this.tree[startInd].right;
            this.delInd = startInd;
            //node is a leaf, simply remove it
            if (lInd == -1 && rInd == -1) {
                return -1;
            }
            //node has a single child, give the node it's grandchildren
            else if (lInd != -1 && rInd == -1) {
                return lInd;
            } else if (lInd == -1 && rInd != -1) {
                return rInd;
            }
            //node has two children, replace the node it's successor (smallest
            //node in right subtree), then remove the successor
            else {
                int smallestInd = this.getSmallest(rInd);
                int tempInd = startInd;
                this.nodeKVSwap(startInd, smallestInd);
                this.tree[startInd].right = this.remove(this.tree[smallestInd].key, rInd);
                this.delInd = smallestInd;
                return this.rebalance(startInd);
            }
        }
    }
    /**
     * Rebalances the subtree rooted by the node at index startInd.
     *
     * Based on the balance factor of the given node, this method will apply the
     * appropriate tree rotation. This method is NOT recursive and will only
     * apply the rotation to the node at index startInd.
     *
     * @param       startInd    the index of the subree root
     * @return      the index of the (new) subtree root
     * @see         FixedSizeHashMap#balanceFactor
     */
    private int rebalance(int startInd) {
        if (startInd == -1) System.out.println("shit");
        int newStartInd;
        int lInd = this.tree[startInd].left;
        int rInd = this.tree[startInd].right;
        //left subtree heavy
        if (this.balanceFactor(startInd) == 2) {
            if (this.tree[lInd].left == -1) {
                newStartInd = this.rotateCaseLL(startInd);
            } else {
                newStartInd = this.rotatecaseLR(startInd);
            }
        }
        //right subtree heavy
        else if (this.balanceFactor(startInd) == -2) {
            if (this.tree[rInd].right == -1) {
                newStartInd = this.rotateCaseRL(startInd);
            } else {
                newStartInd = this.rotateCaseRR(startInd);
            }
        }
        //no rebalancing needed
        else {
            newStartInd = startInd;
        }
        //update height if necessary
        this.updateHeight(startInd);
        return newStartInd;
    }
    /**
     * Returns the index of the smallest node in the subtree rooted by startInd.
     *
     * @param       startInd    the index of the subtree root
     * @return      the index of the node with the smallest key
     */
    private int getSmallest(int startInd) {
        if (this.tree[startInd].left != -1) {
            return this.getSmallest(this.tree[startInd].left);
        } else {
            return startInd;
        }
    }
    /**
     * Swaps the keys and values of the nodes at indeces a and b.
     * Only the key and value fields are swapped. All other fields are left
     * untouched.
     *
     * @param       a       the index of the node to be overwritten (dst)
     * @param       b       the index of the node with the data to write (src)
     */
    private void nodeKVSwap(int a, int b) {
        int tempKey = this.tree[a].key;
        this.tree[a].key = this.tree[b].key;
        this.tree[b].key = tempKey;
        T tempValue = (T) this.tree[a].value;
        this.tree[a].value = this.tree[b].value;
        this.tree[b].value = tempValue;
    }
    /**
     * Returns the balance factor of the subtree rooted by the node at index i.
     *
     * Balance factor is defined as the difference between a node's left and
     * right subtrees.
     *
     * @param       i       the index of a node
     * @return      the balance factor of the specified node
     */
    private int balanceFactor(int i) {
        return this.height(this.tree[i].left) - this.height(this.tree[i].right);
    }
    /**
     * Returns the height of the node at index i or -1 if i is not active.
     *
     * @param       i       the index of a node
     * @return      the height of the node at index i or -1
     */
    private int height(int i) {
        return (i != -1) ? this.tree[i].height : -1;
    }
    /**
     * Updates the height of the node at index i
     *
     * @param       i       the index of the node to be updated
     */
    private void updateHeight(int i) {
        if (i != -1) {
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
    }
    /**
     * Removes every node from the subtree rooted by the node at index startInd.
     *
     * This method does postorder traversal of the implicit tree so as to remove
     * key and value data from each node and flip all active bits in the bitmap.
     *
     * @param       startInd    the index of the subtree root
     */
    private void _clear(int startInd) {
        if (startInd != -1) {
            if (this.tree[startInd].left != -1) {
                this._clear(this.tree[startInd].left);
            }
            if (this.tree[startInd].right != -2) {
                this._clear(this.tree[startInd].right);
            }
            this.tree[startInd].clean();
            this.bitFlip(startInd);
            this.items--;
        }
    }

    //TREE ROTATIONS (PRIVATE)
    /**
     * Performs a tree rotation for the left left case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     *
     * @param       startInd    the index of the subtree root
     * @return      the new start index after rotation
     */
    private int rotateCaseLL(int startInd) {
        int newStartInd = this.tree[startInd].left;
        if (newStartInd == -1) {
            return startInd;
        } else {
            this.tree[startInd].left = this.tree[newStartInd].right;
            this.tree[newStartInd].right = startInd;
            //update heights
            this.updateHeight(startInd);
            this.updateHeight(newStartInd);
            return newStartInd;
        }
    }
    /**
     * Performs a tree rotation for the right right case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     *
     * @param       startInd    the index of the subtree root
     * @return      the new start index after rotation
     */
    private int rotateCaseRR(int startInd) {
        int newStartInd = this.tree[startInd].right;
        if (newStartInd == -1) {
            return startInd;
        } else {
            this.tree[startInd].right = this.tree[newStartInd].left;
            this.tree[newStartInd].left = startInd;
            //update heights
            this.updateHeight(startInd);
            this.updateHeight(newStartInd);
            return newStartInd;
        }
    }
    /**
     * Performs a tree rotation for the left right case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     *
     * @param       startInd    the index of the subtree root
     * @return      the new start index after rotation
     */
    private int rotatecaseLR(int startInd) {
        this.tree[startInd].left = this.rotateCaseRR(this.tree[startInd].left);
        return this.rotateCaseLL(startInd);
    }
    /**
     * Performs a tree rotation for the right left case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     *
     * @param       startInd    the index of the subtree root
     * @return      the new start index after rotation
     */
    private int rotateCaseRL(int startInd) {
        this.tree[startInd].right = this.rotateCaseLL(this.tree[startInd].right);
        return this.rotateCaseRR(startInd);
    }

    //BITMAP UTILITIES (PRIVATE)
    /**
     * Returns the index of the first available node in the internal array.
     *
     * Iterates over this hash map's internal bitmap and searches for the first
     * bit that is set to 0.
     *
     * @return      The index of the first available node
     */
    private int getAvailableNode() {
        int i = 0;
        for (; this.bitmap[i] == -1; i++);
        int j = 0;
        for (; j < 8; j++) {
            //break out of loop the first time a 0 is encountered
            if ((this.bitmap[i] & (1 << j)) == 0) {
                break;
            }
        }
        //ensure the returned value is less than the max size of this hashmap
        return (8*i + j < this.size) ? 8*i + j : -1;
    }

    /**
     * Flips the xth bit in the hash map's internal bitmap.
     *
     * @param       x       the bitmap index that should be flipped
     */
    private void bitFlip(int x) {
        int index = x/8;
        int offset = x%8;
        this.bitmap[index] ^= (1 << offset);
    }

    //MISC UTILITIES
    /**
     * Returns the max of two integers.
     *
     * @param       a       the first integer
     * @param       b       the second integer
     * @return      the larger of the two given integers
     */
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }
    /**
     * Prints the implicit tree in a preorder fashion. Only used for testing.
     */
    public void print() {
        this.preorderPrint(this.rootInd, "");
    }
    /**
     * Preorder prints this hash map's implicit tree
     *
     * @param       i       the root of the subtree to print
     * @param       indent  the string indentation to print
     */
    private void preorderPrint(int i, String indent) {
        if (i >= 0) {
            System.out.println(indent + this.tree[i] + " " + this.height(i));
            this.preorderPrint(this.tree[i].left, indent + "  ");
            // if (i == this.rootInd) System.out.print("* ");
            this.preorderPrint(this.tree[i].right, indent + "  ");
        }
    }
}
