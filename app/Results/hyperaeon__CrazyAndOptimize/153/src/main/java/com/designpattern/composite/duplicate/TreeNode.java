package com.designpattern.composite.duplicate;
 import java.util.Enumeration;
import java.util.Vector;
public class TreeNode {

 private  String name;

 private  TreeNode parent;

 private  Vector<TreeNode> children;

public TreeNode(String name) {
    this.name = name;
}
public void setName(String name){
    this.name = name;
}


public void add(TreeNode node){
    children.add(node);
}


public Enumeration<TreeNode> getChildren(){
    return children.elements();
}


public TreeNode getParent(){
    return parent;
}


public String getName(){
    return name;
}


public void setParent(TreeNode parent){
    this.parent = parent;
}


public void setChildren(Vector<TreeNode> children){
    this.children = children;
}


public void remove(TreeNode node){
    children.remove(node);
}


}