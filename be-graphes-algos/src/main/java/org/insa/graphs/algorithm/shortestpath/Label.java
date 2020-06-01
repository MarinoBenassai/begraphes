package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class Label implements Comparable<Label>{
	private Node sommet;
	private boolean marque;
	private double coût;
	private Arc père;

	public Label(Node sommet , boolean marque, double coût, Arc père) {
		this.sommet = sommet;
		this.marque = marque;
		this.coût = coût;
		this.père = père;
		
	}

	public double getCost() {
		return this.coût;
	}
	
	public Node getSommet() {
		return this.sommet;
	}
	
	public void setCost(double coût) {	
		this.coût = coût;
	}
	
	public boolean getMarque() {
		return this.marque;
	}
	
	public void setMarque(boolean marque) {
		this.marque = marque;
	}
	
	public Arc getPère() {
		return this.père;
	}
	
	public void setPère(Arc père) {
		this.père = père;
	}
	
	public int compareTo(Label label) {
		if (this.getCost() <label.getCost())
			return -1;
		else if (this.getCost() == label.getCost())
			return 0;
		else
			return 1;
	}
}
