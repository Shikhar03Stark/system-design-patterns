package com.shikhar03stark.behavioural.iterator.container;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BinaryTreeNode<T> {
    private final T value;
    private BinaryTreeNode<T> left;
    private BinaryTreeNode<T> right;

    public BinaryTreeNode(T value, BinaryTreeNode<T> left, BinaryTreeNode<T> right){
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public BinaryTreeNode(T value){
        this(value, null, null);
    }
}
