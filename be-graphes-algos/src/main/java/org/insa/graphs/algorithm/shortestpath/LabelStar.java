package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
	private double coutEstime;
	
	public LabelStar(Node sommet , boolean marque, double cout, Arc pere, double coutEstime) {
		super(sommet,marque,cout,pere);
		this.coutEstime = coutEstime;		
	}
	
	public double getEstimatedCost () {
		return this.coutEstime;
	}
	
	public void setEstimatedCost (double coutEstime) {
		this.coutEstime = coutEstime;
	}
	
	public double getTotalCost () {
		return this.coutEstime + this.getCost();
	}
	
	
	public String toString() {
		String texte = "LabelStar associé au sommet "+this.getSommet();
		if (this.getMarque()) return texte+", marqué, de coût total"+this.getTotalCost()+", de coût estimé "+this.coutEstime+" et de père "+this.getPere()+".\n";
		else return texte+", non marqué, de coût total "+this.getTotalCost()+", de coût estimé "+this.coutEstime+" et de père "+this.getPere()+".\n";
	}
}
