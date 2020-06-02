package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
	private double coûtEstimé;
	
	public LabelStar(Node sommet , boolean marque, double coût, Arc père, double coûtEstimé) {
		super(sommet,marque,coût,père);
		this.coûtEstimé = coûtEstimé;		
	}
	
	public double getEstimatedCost () {
		return this.coûtEstimé;
	}
	
	public void setEstimatedCost (double coûtEstimé) {
		this.coûtEstimé = coûtEstimé;
	}
	
	public double getTotalCost () {
		return this.coûtEstimé + this.getCost();
	}
	
	//public int compareTo (LabelStar autreLabel) {
		
	//	double coûtTotalAutreLabel = autreLabel.getCost() + autreLabel.coûtEstimé;
		
		//On compare les coûts totaux
	//	if (this.getTotalCost() < coûtTotalAutreLabel) return -1;
	//	else if (this.getTotalCost() > coûtTotalAutreLabel) return 1;
	//	else {
			
			//Les coûts totaux sont égaux, on compare les coûts estimés à la destination
	//		if (this.coûtEstimé < autreLabel.coûtEstimé) return -1;
	//		else if (this.coûtEstimé > autreLabel.coûtEstimé) return 1;
	//		else return 0;
	//	}
	//}
	
	public String toString() {
		String texte = "LabelStar associé au sommet "+this.getSommet();
		if (this.getMarque()) return texte+", marqué, de coût total"+this.getTotalCost()+", de coût estimé "+this.coûtEstimé+" et de père "+this.getPère()+".\n";
		else return texte+", non marqué, de coût total "+this.getTotalCost()+", de coût estimé "+this.coûtEstimé+" et de père "+this.getPère()+".\n";
	}
}
