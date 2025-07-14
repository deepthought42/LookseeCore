package com.looksee.vscodePlugin.structs;

/**
 * Tree class
 * @param <E> the type of the elements in the tree
 */
public class Tree<E> {
	/**
	 * The root node of the tree
	 */
	TreeNode<E> root_node;
	
	/**
	 * Constructor for {@link Tree}
	 * @param root the root node of the tree
	 */
	public Tree(TreeNode<E> root){
		this.root_node = root;
	}
}
