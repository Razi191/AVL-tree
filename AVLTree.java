


import java.util.Iterator;

/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {
    private final AVLNode EXTERNAL_LEAF = new AVLNode(); // Whenever we need to point to an external leaf, we'll always use this object.
    private AVLNode root;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree() {
        // When making a new empty tree, we make sure it only contains an external leaf.
        // We also need to edit EXTERNAL_LEAF fields so they match the requirements.
        this.EXTERNAL_LEAF.rank = -1;
        
        this.EXTERNAL_LEAF.key = -1;
        
        this.EXTERNAL_LEAF.size = 0;
        
        this.root = EXTERNAL_LEAF;
    }

    private AVLTree(AVLNode root) {
        this();
        this.root = root;
    }

    /**
     * updates the height of a given node, according to its children's heights.
     *
     * @param node
     */
    private static int updateHeight(AVLNode node) {
    	
        if (node.isRealNode()) {
        	
            AVLNode right = node.getRight();
             // O(1)
            AVLNode left = node.getLeft();
            
            int height1 = 0;
            int height2 = 0;
            if (right != null)
                height1 = right.getHeight();
            if (left != null)
                height2 = left.getHeight();
            int expectedHeight = Math.max(height1, height2) + 1;
            
            if (node.getHeight() != expectedHeight) {
                node.setHeight(expectedHeight);
                return 1;
            }
        }
        return 0;
        
    }

    /**
     * returns the balance factor of a given node.
     *
     * @param node
     * @return node.getLeft().getHeight() - node.getRight().getHeight()
     * @pre node.getLeft() != null && node.getRight() != null
     */
    private static int BalanceFactor(AVLNode node) {
        return node.getLeft().getHeight() - node.getRight().getHeight() ;
    }

    
    private static void updateSize(AVLNode node) {
    	
        if (node != null && node.isRealNode()) {
        	
            AVLNode right = node.getRight();
            
            AVLNode left = node.getLeft();
            // O(1)
            int size1 = 0;
            int size2 = 0;
            if (right != null)
                size1 = right.getSize();
            if (left != null)
                size2 = left.getSize();
            
            node.setSize(size1 + size2 + 1);
        }

    }
    
    
    
    private static void updatenumtrue(AVLNode node) { 
    	
    	
    	if ( node != null && node.isRealNode()) { 
    		
    		AVLNode right = node.getRight();
    		AVLNode left = node.getLeft();
    		int num1 = 0; //O(1)
    		int num2 = 0;
    		 if (right != null)
                 num1 = right.getnumoftrue();
             if (left != null)
                 num2 = left.getnumoftrue();
             if (node.getValue() == true) { 
             node.setnumoftrue(num1 + num2  + 1);}
             else { node.setnumoftrue(num1 + num2);}
    		
    	}
    	
    	
    	
    	
    }

    /**
     * Returns the height of the tree, by accessing its root properties.
     * If the tree is empty, the function will return -1 (as the height of an external leaf).
     *Time Complexity: O(1)
     * @return this.root.getHeight()
     */
    public int getTreeHeight() {
        if (this.root == null) // That shouldn't ever happen, but we want to be sure.
            return -1;
        else
            return this.root.getHeight();
    }


    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */

    public boolean empty() {
        return  ( root == null || !root.isRealNode() || root == EXTERNAL_LEAF  || root.getHeight() == -1 || root.getKey() == -1);
    }



    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        AVLNode node = this.searchByKey(k);
        if (node == null)
            return null;
        else
            return node.getValue();
    }

    /**
     * 
     * 
     * public AVLNode getNodeByKey(int k)
     * <p>
     * returns the item with key k if it exists in the tree
     * otherwise, returns null.
     *
     * @param k Time Complexity: O(log(n)) where n is the total number of nodes within the tree.
     */
    private AVLNode searchByKey(int k) {
        if (this.empty())
            return null; // O(LOG N )
        AVLNode node = root;
        while (node.isRealNode()) {
        	
            if (node.getKey() == k)
                return node;
            else {
            	
                if (node.getKey() > k)
                    node = node.getLeft();
                else {
                    node = node.getRight();
                }
                
            }
        }
           return null;
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
     
    	// O(log n)
        AVLNode insertionPoint = this.findInsertionPoint(k , i);

        if (this.empty()) {   // Tree is empty
            this.root = new AVLNode(k, i);
            return 0;
        } else if (insertionPoint == null) { // Key already exists
            return -1;
        }
        boolean isLeftChild = (insertionPoint.getKey() > k); // Indicates whether or not newNode should be inserted as a left child of insertionPoint.
        AVLNode newNode = new AVLNode(k, i);
        newNode.parent = insertionPoint;
        
        if (isLeftChild) {
            newNode.setLeft(insertionPoint.getLeft());
            newNode.setRight(this.EXTERNAL_LEAF);
            insertionPoint.setLeft(newNode);
        } else {
            newNode.setRight(insertionPoint.getRight());
            
            newNode.setLeft(this.EXTERNAL_LEAF);
            
            insertionPoint.setRight(newNode);
        }
       updateHeight(newNode);

        

        /* The following code re-balances back the tree, after the insertion has been made */
        if (insertionPoint.getHeight() == 0) { // Case A: The parent is a leaf
            return rebalanceFromNode(newNode);
        } else {
            /**
             * Case B: The parent is not a leaf, and thus an unary node.
             * No further rebalancing action is needed.
             */
            return 0;
        }
    }

    /**
     * Right rotates node, so node becomes the left child of its current right child.
     *
     * @param node
     */
    private int rightRotation(AVLNode node) {
        return rightRotation(this, node);
    }
    private static int rightRotation(AVLTree T, AVLNode node) {
        AVLNode parent = node.getParent();
        AVLNode leftChild = node.getLeft();
        //O(1)
        AVLNode rightChild = node.getRight();
        AVLNode leftLGrandson = leftChild.getLeft();
        
        AVLNode leftRGrandson = leftChild.getRight();

        
        //  AVLNode rightGrandson=rightChild.getLeft();
        int leftLGrandsonSize = leftLGrandson.getSize();    // ****** update the numtruesize ****** 
        int leftLGrandsontr = leftLGrandson.getnumoftrue();
        
        int LeftRGrandsonSize = leftRGrandson.getSize();
        int LeftRGrandsontr = leftRGrandson.getnumoftrue();
        
       
        int rightChildSize = rightChild.getSize();
        int rightChildtr = rightChild.getnumoftrue();
        
        node.setLeft(leftChild.getRight());
        leftChild.getRight().setParent(node);
        leftChild.setRight(node);
        node.setParent(leftChild);
        leftChild.setParent(parent);
        
        node.setSize(rightChildSize + 1 + LeftRGrandsonSize);
        
        
        if ( node.getValue() == true)
             { node.setnumoftrue(rightChildtr + 1 + LeftRGrandsontr);}
        else {  node.setnumoftrue(rightChildtr +  LeftRGrandsontr);}
        
        
        leftChild.setSize(node.getSize() + 1 + leftLGrandsonSize);
        
        if ( leftChild.getValue() == true) {
            leftChild.setnumoftrue( node.getnumoftrue() + 1 + leftLGrandsontr);

        }
        else { leftChild.setnumoftrue( node.getnumoftrue() + leftLGrandsontr);}
        

        int steps = 1; // for the rotation itself.

        if (parent != null) {
            if (parent.getLeft() == node)
                parent.setLeft(leftChild);
            else
                parent.setRight(leftChild);
            steps += updateHeight(parent);
        }

        // We check if a height update is needed, and if so we increment the number of steps we made:
        steps += updateHeight(node);
        steps += updateHeight(leftChild);
        // Update Tree Root, if necessary:
        if (T.root == node)
            T.root = leftChild;
        return steps;
    }

    /**
     * Left rotates node, so node becomes the right child of its current left child.
     *
     * @param node
     */
    private int leftRotation(AVLNode node) {
        return leftRotation(this, node);
    }
    private static int leftRotation(AVLTree T, AVLNode node) {
        AVLNode parent = node.getParent();
        AVLNode leftChild = node.getLeft();
        AVLNode rightChild = node.getRight();
        AVLNode rightLGrandson = rightChild.getLeft();
        AVLNode rightRGrandson = rightChild.getRight();
        
        
        // ****** update the numtruesize ******
        int rightLGrandsonSize = rightLGrandson.getSize();
        int rightLGrandsontr = rightLGrandson.getnumoftrue();
        
        
        int rightRGrandsonSize = rightRGrandson.getSize();
        int rightRGrandsontr = rightRGrandson.getnumoftrue();
        
        
        int leftChildSize = leftChild.getSize();
        int leftChildtr = leftChild.getnumoftrue();
        
        
        
        node.setRight(rightChild.getLeft());
        rightChild.getLeft().setParent(node);
        rightChild.setLeft(node);
        node.setParent(rightChild);
        rightChild.setParent(parent);
        
        node.setSize(1 + leftChildSize + rightLGrandsonSize);
        
        if (node.getValue()==true) { node.setnumoftrue(1 + leftChildtr + rightLGrandsontr);}
        else {  node.setnumoftrue( leftChildtr + rightLGrandsontr);}
        
        
        
        rightChild.setSize(node.getSize() + 1 + rightRGrandsonSize);
        
        if (rightChild.getValue() ==true) 
              { rightChild.setnumoftrue(node.getnumoftrue() + 1 + rightRGrandsontr ) ;}
        else {  
        	rightChild.setnumoftrue(node.getnumoftrue() +  rightRGrandsontr ) ;}

        int steps = 1; // for the rotation itself.

        if (parent != null) {
            if (parent.getRight() == node)
                parent.setRight(rightChild);
            else
                parent.setLeft(rightChild);
            steps += updateHeight(parent);
        }

        // We check if a height update is needed, and if so we increment the number of steps we made:
        steps += updateHeight(node);
        steps += updateHeight(rightChild);
        // Update Tree Root, if necessary:
        if (T.root == node)
            T.root = rightChild;
        
        return steps;
    }

    /**
     * Rebalances the tree along the path from node to the root of the tree
     * after an insertion has been made.
     *
     * @param node
     * @pre node.getHeight() >= 0 && node.isRealNode()
     */
    private int rebalanceFromNode(AVLNode node) {
        if (node == null)
            return 0;
        AVLNode leftChild = node.getLeft();
        AVLNode rightChild = node.getRight();
        int nodeBalanceFactor = BalanceFactor(node);//get the BF 
        int steps = 0;
        if (Math.max(leftChild.getHeight(), rightChild.getHeight()) + 1 != node.getHeight()) {
            if (nodeBalanceFactor == 1 || nodeBalanceFactor == -1) { // Case 1
                node.setHeight(node.getHeight() + 1); // promote
                steps += 1;
                
                return steps + rebalanceFromNode((node.getParent()));
            } else if (nodeBalanceFactor == 2) { // node is left heavy.
                int leftChildBalanceFactor = BalanceFactor(leftChild);
                if (leftChildBalanceFactor >= 0) { // Case 2.1
                    steps += rightRotation(node);
                    steps += 1;
                    
                    return steps;
                    
                } else if (leftChildBalanceFactor == -1) { // Case 3.1
                    steps += leftRotation(leftChild);
                    
                    steps += rightRotation(node);
                    
                    steps += 1;    // remember the count steps
                    return steps;
                }
            } else if (nodeBalanceFactor == -2) { // node is right heavy.
                int rightChildBalanceFactor = BalanceFactor(rightChild);
                if (rightChildBalanceFactor <= 0) { // Case 2.2
                    steps += leftRotation(node);
                    return steps;
                } else if (rightChildBalanceFactor == 1) { // Case 3.2
                    steps += rightRotation(rightChild);
                    steps += leftRotation(node);
                    return steps;
                }
            }
        }
        return rebalanceFromNode(node.getParent());
    }

    /**
     * Given a key, this functions returns the last (real) node encountered while
     * searching for key in the tree. If the tree is empty, it returns null.
     
     * If a given key already exists in the tree, the function returns null.
     *
     * @param key
     */
    private AVLNode findInsertionPoint(int key, boolean info) {
        if (this.empty()) 
            return null;
        AVLNode node = this.root;
        AVLNode prev = null;
        //O(log n)
        
        while (node != null && node.isRealNode()) {
            int nodeSize = node.getSize();
            int truenum = node.getnumoftrue();
            node.setSize(nodeSize + 1);
            
            if (info == true) {
            node.setnumoftrue(truenum +1) ;}
            
            
            prev = node;
            int currentKey = node.getKey();
            if (currentKey == key) {
                // Key already exists
                // We must return the size changes we've made, and then return null.
                while (node != null) {
                    nodeSize = node.getSize();
                    truenum = node.getnumoftrue();
                    node.setSize(nodeSize - 1);
                    if (info == true) {
                        node.setnumoftrue(truenum -1) ;}
                    node = node.getParent();
                }
                return null;
            } else if (currentKey > key)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        if (node.isRealNode())
            return node;
        else
            return prev;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
    	int numOfOp=0;
    	//O(log n)
        AVLNode node = searchByKey(k);
        if (node == null || !node.isRealNode())
            return -1;
        AVLNode parent = node.getParent();
        if (isUnary(node) || isLeaf(node))
            deleteByNode(node);
        else {
            
        	
        	AVLNode suc = successor(node);
            parent = suc.getParent();
            swap(node, suc);
            upnum(node);
            upnum(suc);
            numOfOp+=deleteByNode(suc);
        }
        return numOfOp+rebalanceFromNode3(parent);
    }
    
    
    public void upnum ( AVLNode node) { 
    	
    	
    	updatenumtrue(node);
    	// this functions updates the number of true field
    	while(node.getParent() !=null) {
    		
    		updatenumtrue(node);
    		node=node.getParent();
    	}
    	
    	
    }

    /**
     * Replaces the keys and the values of two given nodes, without changing
     * any pointer in the tree.
     * @param node1
     * @param node2
     * @pre node1 != null && node2 != null
     * @post node1.getKey() == node2Key && node1.getValue() == node2Val
     * && node2.getKey() == node1Key && node2.getValue == node1Val
     */
    private void swap(AVLNode node1, AVLNode node2) {
        int node1Key = node1.getKey();
        int node2Key = node2.getKey();
        //O(1)
        boolean node1Val = node1.getValue();
        
        boolean node2Val = node2.getValue();
        node1.setValue(node2Val);
        
        node1.setKey(node2Key);
        
        node2.setValue(node1Val);
        node2.setKey(node1Key);
    }

    /**
     * Replaces two nodes in the tree, by reassigning their pointers,
     * and reassigning their parents' pointers.
     * If one of the nodes given is null, the function does nothing.
     * If one of the nodes given is an external leaf, the function treats
     * it like an ordinary node.
     * <p>
     * It should be used only for ancestor-successor replacement.
     * <p>
     * It makes problems if we try to swap to related nodes.
     *
     * @param ancestor
     * @param leaf
     * @pre $ancestor is an ancestor of $leaf && leaf.getHeight() == 0
     */
    

    /**
     *
     * @param node
     * @pre node != null
     * @post
     */
    private  int deleteByNode(AVLNode node) {   
    	int numOfOp=0;
        AVLNode parent = node.getParent();
        if (isLeaf(node) && parent == null) {// we are at the root set the root to be an external leaf
            this.root = EXTERNAL_LEAF;
        }
        if ((isLeaf(node) && parent != null)) {// a leaf that is not the root
            if (parent.getLeft() == node) {
                parent.setLeft(node.getLeft());
                node.getLeft().setParent(parent);
            } else {
                parent.setRight(node.getRight());
                node.getRight().setParent(parent);

            }
            parent.setSize(parent.getSize() - 1); //O(LOG N)
           
            if ( node.getValue() == true) {  parent.setnumoftrue(parent.getnumoftrue() -1);}
            //leave no pointers of node to other nodes/ external leaves
            node.setParent(null);
            node.setLeft(null);
            node.setRight(null);
            numOfOp+=updateHeight(parent);
        } else if (isUnary(node)) {//node has one child

            AVLNode leftChild = node.getLeft();
            
            AVLNode rightChild = node.getRight();
            
            if (parent != null) {//deletes the node and attaches the node's son to the node's parent
                if (parent.getLeft() == node) {
                    if (leftChild != null && leftChild.isRealNode()) {
                        parent.setLeft(leftChild);
                        leftChild.setParent(parent);
                    } else if (rightChild != null && rightChild.isRealNode()) {
                        parent.setLeft(rightChild);
                        rightChild.setParent(parent);
                    }
                } else if (parent.getRight() == node) {
                    if (leftChild != null && leftChild.isRealNode()) {
                        parent.setRight(leftChild);
                        leftChild.setParent(parent);
                    } else if (rightChild != null && rightChild.isRealNode()) { 
                        parent.setRight(rightChild);
                        rightChild.setParent(parent);
                    }
                }
                parent.setSize(parent.getSize() - 1);
                if (node.getValue() == true)
                    {parent.setnumoftrue(parent.getnumoftrue() -1);}

                
                
                AVLNode parentParent = parent.getParent();
                updateSize(parentParent);
                updatenumtrue(parentParent);
                numOfOp+=updateHeight(parent);
            } else {
                if (leftChild != null && leftChild.isRealNode()) {
                    leftChild.setParent(null);
                    this.root = leftChild;
                    numOfOp+=updateHeight(leftChild);
                } else if (rightChild != null && rightChild.isRealNode()) {
                    rightChild.setParent(null);
                    this.root = rightChild;
                    numOfOp+=updateHeight(rightChild);
                }
            }
        }
        return numOfOp;
    }


    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node) {
    	//O(log n)
        if (node == null)
            return null;
        // If possible, walk right once, and then always left.
        // Otherwise, walk up until you face a left parent.
        if (node.getRight().isRealNode()) {
            node = node.getRight();
            while (node.getLeft().isRealNode())
                node = node.getLeft();
        } else {
            AVLNode parent = node.getParent();
            while (parent != null && parent.getLeft() != node) {
                node = parent;
                parent = parent.getParent();
            }
            node = parent;
        }
        return node;
    }


    /**
     * Returns true if and only if a given node is a leaf.
     *
     * @param node
     */
    private boolean isLeaf(AVLNode node) {
        return node.getLeft() == EXTERNAL_LEAF && node.getRight() == EXTERNAL_LEAF;
    }

    /**
     * Returns true if and only if a given node has only one child.
     *
     * @param node
     */
    private boolean isUnary(AVLNode node) {
        return !isLeaf(node) && (node.getRight() == EXTERNAL_LEAF || node.getLeft() == EXTERNAL_LEAF);
    }

    /**
     * Demotes a given node's rank, by re-setting its rank field.
     *
     * @param node
     * @return the total cost of the operation
     */
    public int demote(AVLNode node) {
        node.setHeight(node.getHeight() - 1);
        return 1;
    }

    /**
     * Promotes a given node's rank, by re-setting its rank field.
     *
     * @param node
     * @return the total cost of the operation
     */
    public int promote(AVLNode node) {
        node.setHeight(node.getHeight() + 1);
        return 1;
    }

    /**
     * in delete we have four different cases we need to rebalance according to wich case
     *
     * @param node
     * @return
     */
    private int rebalanceFromNode3(AVLNode node) {
        if (node == null || !node.isRealNode())
            return 0;
        AVLNode parent = node.getParent();
        
        //O(log n )
        
        int numOfOp = 0;
        
        int bf = BalanceFactor(node);
        if (bf == 0 && node.getLeft().getRankDifference() == 1) {
            if (parent != null) {
                updateSize(parent);
                updatenumtrue(parent);
                
                numOfOp+=updateHeight(parent);
            }
        }
        if (bf == 1 || bf == -1) {
            if (parent != null) {
            	
                updateSize(parent);
                updatenumtrue(parent);
                numOfOp+=updateHeight(parent);
            }
            //   return 0;
        } else if (bf == 0 && node.getLeft().getRankDifference() == 2) {//case 1
            demote(node);
            updateSize(parent);
            updatenumtrue(parent);
            numOfOp+=updateHeight(parent);
            numOfOp++;
        } else if (bf == -2 && BalanceFactor(node.getRight()) == 0) {//case 2
            demote(node);
            promote(node.getRight());//cheek
            leftRotation(node);
            numOfOp += 3;

        } else if (bf == -2 && BalanceFactor(node.getRight()) == -1) {//case 3
            demote(node);
            demote(node);
            leftRotation(node);
            
            numOfOp += 2;
        } else if (bf == -2 && BalanceFactor(node.getRight()) == 1) {//case 3 symmetry
            rightRotation(node.getRight());
            leftRotation(node);
            numOfOp += 2;
        } else if (bf == 2 && BalanceFactor(node.getLeft()) == 0) {//case 2 symmetry
            demote(node);
            promote(node.getLeft());
            
            rightRotation(node);
            numOfOp += 3;
        } else if (bf == 2 && BalanceFactor(node.getLeft()) == 1) {//case 4
            demote(node);
            demote(node);
            
            rightRotation(node);
            numOfOp += 3;
        } else if ((bf == 2 && BalanceFactor(node.getLeft()) == -1)) {//case 4 symmetry
            leftRotation(node.getLeft());
            rightRotation(node);
            numOfOp += 2;
        }
        updateSize(parent);
        updatenumtrue(parent);
        return numOfOp + rebalanceFromNode3(parent);
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public boolean min() {
        return this.minNode().getValue();
    }
/**
 * 
 * @return the node with minimum key
 */
    private AVLNode minNode() {
        if (this.empty())
            return null;
        AVLNode node = root;
        while (node.getLeft().isRealNode()) {
        	
            node = node.getLeft();
        }
        return node;
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public boolean max() {
        return this.maxNode().getValue();
    }
    /**
     * 
     * @return node with max key
     */
    private AVLNode maxNode() {
        if (this.empty())
            return null;
        AVLNode node = root;
        
        while (node.getRight().isRealNode()) {
        	
            node = node.getRight() ;
            
        }

        return node;
    }


    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];
        //O(n)
        // Initiate In-Order Traversal Iterator
        Iterator<AVLNode> iter = this.getInOrderIterator();
        int index = 0;
        while (iter.hasNext() && index < arr.length) {
        	
            AVLNode next = iter.next();
            int key = next.getKey();
            
            arr[index] = key;
            
            index++;
            
        }
        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size()];
        // O(N)
        // Initiate In-Order Traversal Iterator
        Iterator<AVLNode> iter = this.getInOrderIterator();
        int index = 0;
        while (iter.hasNext() && index < arr.length) {
            AVLNode next = iter.next();
            boolean value = next.getValue();
            arr[index] = value;
            index++;
        }
        return arr;
    }

    /**
     * @return new in-order iterator.
     * Time Complexity: O(log n) - the time required for getting the minimal node in the tree.
     */
    public Iterator<AVLNode> getInOrderIterator() {
        // We find the minimal node, where we want to start.
        AVLNode temp = this.getRoot();
        while (temp != null && temp.isRealNode() && temp.getLeft().isRealNode())
            temp = temp.getLeft();
        // We initiate a final pointer to the minimal node.
        final AVLNode start = temp;
        Iterator<AVLNode> iter = new Iterator<AVLNode>() {
            AVLNode node = start;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public AVLNode next() {
                if (node == null)
                    return null;
                AVLNode prevNode = node;
                // If possible, walk right once, and then fully left.
                // Otherwise, walk up until you encounter a left parent.
                if (node.getRight().isRealNode()) {
                    node = node.getRight();
                    while (node.getLeft().isRealNode())
                        node = node.getLeft();
                } else {
                    AVLNode parent = node.getParent();
                    while (parent != null && parent.getLeft() != node) {
                        node = parent;
                        parent = parent.getParent();
                    }
                    node = parent;
                }
                return prevNode;
            }
        };
        return iter;
    }


    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        AVLNode root = this.root;
        
        if (root != null)
            return Math.max(0, root.getSize());
        else
            return 0;
    }
    
    
    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     *
     */
    public boolean prefixXor(int k){
    	
    	// O(log n )
    	 int c = 0;
    	 AVLNode node = searchByKey(k);
    	 c =c + node.getLeft().getnumoftrue();
         if ( node.getValue() ==true) { c ++;}
    	  AVLNode y = node;
    	  while ( y !=this.root) { 
    		  // here we use the field to go up til the root and as long as this is the right child we do count
    		  
    		  if ( y == y.getParent().getRight()) { 
    			  if ( y.getParent().getValue() == true ) {
    				  c = c + y.getParent().getLeft().getnumoftrue() + 1;
    			  }
    			  else {  c = c + y.getParent().getLeft().getnumoftrue() ;}
    		  }
    		y = y.getParent();
    		  
    	  }
    
    	 if ( c%2 == 1) { 
        return true;
    } 
    	 else { return false  ;}
    }
    
    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k){
       
    	// O(nlogn)
    	AVLNode node = this.minNode();
    	
    	if ( node.getKey() ==k) { 
    		
    		if ( node.getValue() == true) { return true;} 
    		  else { return false;}
    	}
    	
    	int c = 0 ;
    	if (node.getValue() == true ) {c++;}
    	AVLNode y = successor (node);
    	if (y!=null) { 
    	while (y.getKey()!= k && y !=null) {
    		
    		if (y.getValue() ==true) { c++;}
    		y = successor(y);
    	}}
    	
    	// we start from the smallest key, doing successor calls til we reach the node with the needed key k and count
    	if (y.getValue() == true) {c++;}
    	
    	
    	 if ( c%2 == 1) { 
    	        return true;
    	    } 
    	    	 else { return false  ;}
    	
    	
    }

  
    

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.root;
    }


    public AVLNode find(int k) {
		//Search for node k - go down the tree in the correct directions as seen in class, until node with key k is found (or null returned if k not in tree).
		if (this.empty()) {
			return null;
		}
		AVLNode currNode = (AVLNode) this.root;
		while (currNode != EXTERNAL_LEAF){
			if (k<currNode.key) {
				currNode = (AVLNode) currNode.left;
			}
			else if (k>currNode.key) {
				currNode = (AVLNode) currNode.right;
			}
			else if (k==currNode.key) {
				return currNode;
			}
		}
	return null; 
	}
   

 
    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode  {
        private AVLNode left;
        private AVLNode right;
        private AVLNode parent;
        private int key;
        private boolean info;
        private int rank;
        private int size;
        private int numoftrue;

        private AVLNode() {
        }

        public AVLNode(int key, boolean info) {
            this.key = key;
            this.info = info;
            this.size = 1;
            if (info == true) { this.numoftrue = 1;}
            else {this.numoftrue = 0;}
            this.setLeft(EXTERNAL_LEAF);
            this.setRight(EXTERNAL_LEAF);
            EXTERNAL_LEAF.setSize(0);
            EXTERNAL_LEAF.setnumoftrue(0);
            
        }

        public int getKey() {
            return this.key;
        }

        public void setKey(int newKey) {
            this.key = newKey;

        }

        public boolean getValue() {
            return this.info;
        }

        public void setValue(boolean newValue) {
            this.info = newValue;

        }

        public AVLNode getLeft() {
            return this.left;
        }

        public void setLeft(AVLNode node) {
            this.left = node;
        }

        public AVLNode getRight() {
            return this.right;
        }

        public void setRight(AVLNode node) {
            this.right = node;
        }

        public AVLNode getParent() {
            return this.parent;
        }

        public void setParent(AVLNode node) {
            this.parent = node;
        }

        // Returns True if this is a real AVL node
        public boolean isRealNode() {

            return this.key != -1;
        }

        public int getHeight() {
            return rank;
        }

        public void setHeight(int height) {
            rank = height;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }
        
        public int getnumoftrue() {
        	return this.numoftrue;
        	
        }
        public void setnumoftrue(int numoftrue) {
            this.numoftrue = numoftrue ;
        }
        
        

        /**
         * Returns the rank difference between this node to its parent.
         * If this node has no parent, it returns -1.
         */
        public int getRankDifference() {
            AVLNode parent = this.getParent();
            if (parent == null)  // O(1)
                return -1;
            else {
                int parentRank = parent.getHeight();
                int thisRank = this.getHeight();
                return parentRank - thisRank;
            }
        }
    }
    

}