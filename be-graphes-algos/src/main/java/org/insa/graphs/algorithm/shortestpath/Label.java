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
	
	public double getTotalCost() {
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
		
		double x = this.getTotalCost();
		double y = label.getTotalCost();
		
		if (Math.abs(x-y) < 1e-6 || (x==y & Double.isNaN(x) & Double.isNaN(y))) {
			
			//Les coûts totaux des labels sont suffisamment proches pour être considérés comme égaux
			if (Math.abs(x-this.getCost()-(y-label.getCost())) < 1e-6) {
				
				//Les objets comparés sont des Labels, ou des LabelStars dont les coût estimés sont considérés égaux
				
				return 0;
			}
			
			else {
				//Les objets comparés sont des LabelStars dont les coûts estimés sont différents
				
				if (x-this.getCost() < y-label.getCost()) {
					
					//Le coût estimé de label est le plus grand
					return -1;
				}
				
				else {
					
					//Le coût estimé de label est le plus petit
					return 1;
				}
			}
			
			
		}
		else {
			
			//Les coûts totaux des labels sont différents
			
			if (x < y) 
				return -1; 
			else
				return 1;
		}
	}
	
	public String toString() {
		String texte = "Label associé au sommet "+sommet;
		if (this.marque) return texte+", marqué, de coût "+coût+"et de père "+père+".\n";
		else return texte+", non marqué, de coût "+coût+" et de père "+père+".\n";
	}
}
