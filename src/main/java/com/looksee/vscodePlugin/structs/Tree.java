package com.looksee.vscodePlugin.structs;

public class Tree<E> {
	TreeNode<E> root_node;
	
	public Tree(TreeNode<E> root){
		this.root_node = root;
	}
}
