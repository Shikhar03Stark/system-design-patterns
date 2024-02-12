package com.shikhar03stark.behavioural.iterator.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.shikhar03stark.behavioural.iterator.container.BinaryTree.BinaryTreeIterator;
import com.shikhar03stark.creational.singleton.Logger;
import com.shikhar03stark.creational.singleton.LoggerFactory;

public class BinaryTreeTest {

    private final Logger log = LoggerFactory.getInstance();

    private <T> void collectPreOrderTraversal(BinaryTreeNode<T> root, List<T> path){
        if(Objects.isNull(root)){
            return;
        }
        path.add(root.getValue());
        collectPreOrderTraversal(root.getLeft(), path);
        collectPreOrderTraversal(root.getRight(), path);
    }

    private <T> void collectInOrderTraversal(BinaryTreeNode<T> root, List<T> path){
        if(Objects.isNull(root)){
            return;
        }
        collectInOrderTraversal(root.getLeft(), path);
        path.add(root.getValue());
        collectInOrderTraversal(root.getRight(), path);
    }

    private <T> List<T> preOrderTraversal(BinaryTreeNode<T> root){
        final List<T> path = new ArrayList<>();
        collectPreOrderTraversal(root, path);
        return path;
    }

    private <T> List<T> inOrderTraversal(BinaryTreeNode<T> root){
        final List<T> path = new ArrayList<>();
        collectInOrderTraversal(root, path);
        return path;
    }

    private <T> List<T> levelOrderTraversal(BinaryTreeNode<T> root){
        final List<T> path = new ArrayList<>();

        if(Objects.isNull(root)){
            return path;
        }

        final Queue<BinaryTreeNode<T>> levelQueue = new LinkedList<>();
        levelQueue.add(root);

        while(!levelQueue.isEmpty()){
            final BinaryTreeNode<T> node = levelQueue.poll();
            path.add(node.getValue());

            if(Objects.nonNull(node.getLeft())){
                levelQueue.add(node.getLeft());
            }

            if(Objects.nonNull(node.getRight())){
                levelQueue.add(node.getRight());
            }
        }

        return path;
    }

    @Test
    public void treeFromRootTest(){

        final BinaryTreeNode<Integer> root = new BinaryTreeNode<Integer>(12);
        root.setLeft(new BinaryTreeNode<Integer>(2));
        root.getLeft().setLeft(new BinaryTreeNode<Integer>(1));
        root.getLeft().setRight(new BinaryTreeNode<Integer>(5));

        root.setRight(new BinaryTreeNode<Integer>(23));
        root.getRight().setLeft(new BinaryTreeNode<Integer>(19));
        root.getRight().getLeft().setLeft(new BinaryTreeNode<Integer>(15));

        final List<Integer> actualPreOrderTraversal = preOrderTraversal(root);
        final List<Integer> actualLevelOrderTraversal = levelOrderTraversal(root);
        log.info(String.format("PreOrder: %s", actualPreOrderTraversal));
        log.info(String.format("InOrder: %s", actualLevelOrderTraversal));

        // New Binary Tree from root
        final BinaryTree<Integer> binaryTree = new BinaryTree<>(root);
        final List<Integer> binaryTreePreOrderTraversal = preOrderTraversal(binaryTree.getRoot());
        final List<Integer> binaryTreeLevelOrderTraversal = levelOrderTraversal(binaryTree.getRoot());

        Assertions.assertIterableEquals(actualPreOrderTraversal, binaryTreePreOrderTraversal);
        Assertions.assertIterableEquals(actualLevelOrderTraversal, binaryTreeLevelOrderTraversal);

    }

    @Test
    public void treeFromLevelOrderTest(){
        final List<Integer> levelOrder = Arrays.asList(12, 2, 23, 1, 5, 19, null, null, null, 15);
        final BinaryTree<Integer> binaryTree = BinaryTree.fromLevelOrder(levelOrder);

        final List<Integer> actualPreOrderTraversal = preOrderTraversal(binaryTree.getRoot());
        final List<Integer> actualInOrderTraversal = inOrderTraversal(binaryTree.getRoot());

        final List<Integer> expectedPreOrderTraversal = List.of(12, 2, 1, 5, 15, 23, 19);
        final List<Integer> expectedInOrderTraversal = List.of(1, 2, 15, 5, 12, 19, 23);

        log.info(String.format("Actual preOrder: %s", actualPreOrderTraversal));
        log.info(String.format("Actual inOrder: %s", actualInOrderTraversal));

        Assertions.assertIterableEquals(expectedPreOrderTraversal, actualPreOrderTraversal);
        Assertions.assertIterableEquals(expectedInOrderTraversal, actualInOrderTraversal);
    }

    @Test
    public void treeIteratorSameAsInOrderTraversalTest(){
        final List<Integer> levelOrder = Arrays.asList(12, 2, 23, 1, 5, 19, null, null, null, 15);
        final BinaryTree<Integer> binaryTree = BinaryTree.fromLevelOrder(levelOrder);

        final List<Integer> expectedInOrderTraversal = inOrderTraversal(binaryTree.getRoot());
        final List<Integer> actualInOrderTraversalFromIterator = new ArrayList<>();
        for(Integer value : binaryTree){
            actualInOrderTraversalFromIterator.add(value);
        }

        log.info(String.format("Actual inOrder: %s", expectedInOrderTraversal));
        log.info(String.format("Actual inOrder from iterator: %s", actualInOrderTraversalFromIterator));

        Assertions.assertIterableEquals(expectedInOrderTraversal, actualInOrderTraversalFromIterator);
    }

    @Test
    public void treeIteratorSizeTest(){
        final List<Integer> levelOrder = Arrays.asList(12, 2, 23, 1, 5, 19, null, null, null, 15);
        final BinaryTree<Integer> binaryTree = BinaryTree.fromLevelOrder(levelOrder);

        Iterator<Integer> iterator = binaryTree.iterator();
        int count = 0;
        while(iterator.hasNext()){
            iterator.next();
            count++;
        }
        Assertions.assertEquals(7, count);

        
    }

}
