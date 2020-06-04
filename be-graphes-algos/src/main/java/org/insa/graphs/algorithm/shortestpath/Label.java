package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class Label implements Comparable<Label>{
	private Node sommet;
	private boolean marque;
	private double cout;
	private Arc pere;

	public Label(Node sommet , boolean marque, double cout, Arc pere) {
		this.sommet = sommet;
		this.marque = marque;
		this.cout = cout;
		this.pere = pere;
		
	}

	public double getCost() {
		return this.cout;
	}
	
	public double getTotalCost() {
		return this.cout;
	}
	
	public Node getSommet() {
		return this.sommet;
	}
	
	public void setCost(double cout) {	
		this.cout = cout;
	}
	
	public boolean getMarque() {
		return this.marque;
	}
	
	public void setMarque(boolean marque) {
		this.marque = marque;
	}
	
	public Arc getPere() {
		return this.pere;
	}
	
	public void setPere(Arc pere) {
		this.pere = pere;
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
		if (this.marque) return texte+", marqué, de coût "+cout+"et de père "+pere+".\n";
		else return texte+", non marqué, de coût "+cout+" et de père "+pere+".\n";
	}
}
