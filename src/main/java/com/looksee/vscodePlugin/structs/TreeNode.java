package com.looksee.vscodePlugin.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree node class
 * @param <E> the type of the elements in the tree
 */
public class TreeNode<E> {
	/**
	 * The root element of the tree
	 */
	private E root;
	
	/**
	 * The child nodes of the tree
	 */
	private List<TreeNode<E>> child_nodes;
	
	/**
	 * Constructor for {@link TreeNode}
	 * @param root the root element of the tree
	 */
	public TreeNode(E root){
		this.root = root;
		this.child_nodes = new ArrayList<TreeNode<E>>();
	}
	
	/**
	 * Gets the root element of the tree
	 * @return the root element of the tree
	 */
	public E getRoot(){
		return this.root;
	}
	
	/**
	 * Adds a child node to the tree
	 * @param child_node the child node to add
	 * @return true if the child node was added, false otherwise
	 */
	public boolean addChildNode(TreeNode<E> child_node){
		return this.child_nodes.add(child_node);
	}
	
	/**
	 * Gets the child nodes of the tree
	 * @return the child nodes of the tree
	 */
	public List<TreeNode<E>> getChildNodes(){
		return this.child_nodes;
	}
	
	/**
	 * Adds a list of child nodes to the tree
	 * @param list the list of child nodes to add
	 */
	public void addChildNodes(List<TreeNode<E>> list){
		this.child_nodes.addAll(list);
	}
}
