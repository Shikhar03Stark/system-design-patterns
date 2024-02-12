package com.shikhar03stark.behavioural.iterator.container;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import lombok.Getter;

@Getter
public class BinaryTree<T> implements Iterable<T> {

    private final BinaryTreeNode<T> root;

    public BinaryTree(BinaryTreeNode<T> root){
        this.root = root;
    }

    public static <T> BinaryTree<T> fromLevelOrder(List<T> levelTraversal){
        final BinaryTreeNode<T> root = generateFromLevelOrder(levelTraversal, 0);
        return new BinaryTree<>(root);
    }

    private static <T> BinaryTreeNode<T> generateFromLevelOrder(List<T> levelOrder, int nodeIdx){
        if(nodeIdx >= levelOrder.size() || Objects.isNull(levelOrder.get(nodeIdx))){
            return null;
        }

        final BinaryTreeNode<T> root = new BinaryTreeNode<T>(levelOrder.get(nodeIdx));
        root.setLeft(generateFromLevelOrder(levelOrder, 2*nodeIdx+1));
        root.setRight(generateFromLevelOrder(levelOrder, 2*nodeIdx+2));
        return root;
    }

    public static class BinaryTreeIterator<T> implements Iterator<T> {

        private final Stack<BinaryTreeNode<T>> dfs = new Stack<>();

        private BinaryTreeIterator(BinaryTree<T> binaryTree){
            fillStackWithLeftSubtree(binaryTree.root);
        }

        private void fillStackWithLeftSubtree(BinaryTreeNode<T> node){
            if(Objects.isNull(node)){
                return;
            }

            dfs.add(node);
            fillStackWithLeftSubtree(node.getLeft());
        }

        @Override
        public boolean hasNext() {
            return !dfs.empty();
        }

        @Override
        public T next() {
            BinaryTreeNode<T> topNode = dfs.pop();
            if(Objects.nonNull(topNode.getRight())){
                fillStackWithLeftSubtree(topNode.getRight());
            }
            return topNode.getValue();

        }

    }

    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator<T>(this);
    }

}
