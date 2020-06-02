package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    public LabelStar makeLabel (ShortestPathData data, Node sommet , boolean marque, double coût, Arc père) {
    	
    	Node destination = data.getDestination();
    	double distanceEstimée = sommet.getPoint().distanceTo(destination.getPoint());
    	double coûtEstimé;
    	
    	if (data.getMode() == Mode.LENGTH) {
    		coûtEstimé = distanceEstimée;
    	}
    	
    	else {
    		int vitesseMaxGraphe = data.getGraph().getGraphInformation().getMaximumSpeed();
    		int vitesseMaxData = data.getMaximumSpeed();
    		if (vitesseMaxData == -1) {
    			
    			//La vitesse max n'est pas définie dans les données en entrée, on s'intéresse à celle du graphe.
    			if (vitesseMaxGraphe == -1) {
    				
    				//La vitesse max du graphe n'est pas non plus définie, on fixe une valeur arbitraire de 130 km/h
    				coûtEstimé = distanceEstimée/(130*1000./3600.);
    			}
    			
    			else {
    				
    				//La vitesse max du graphe est définie, c'est elle que l'on utilise
    				coûtEstimé = distanceEstimée/(vitesseMaxGraphe*1000./3600.);
    			}

    		}
    		
    		else {
    			
    			//La vitesse max est définie dans les données en entrée
    			
    			if (vitesseMaxGraphe == -1) {
    				
    				//La vitesse max du graphe n'est pas définie, on utilise celle des données en entrée
    				coûtEstimé = distanceEstimée/(vitesseMaxData*1000./3600.);
    			}
    			
    			else {
    				
    				//Les deux vitesses sont définies, on prend la plus petite
    				coûtEstimé = distanceEstimée/(Math.min(vitesseMaxData,vitesseMaxGraphe)*1000./3600.);
    			}
    		}
    	}
    	
    	return new LabelStar(sommet, marque, coût, père, coûtEstimé);
   }

}
