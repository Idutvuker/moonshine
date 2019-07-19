package common;

import java.util.LinkedList;

public class Node
{
	private Node parent;

	private void setParent(Node parent) {
		this.parent = parent;
	}

	private LinkedList<Node> children = new LinkedList<>();

	public Node getParent() { return parent; }

	public LinkedList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		children.add(child);
		child.setParent(this);
	}
}
