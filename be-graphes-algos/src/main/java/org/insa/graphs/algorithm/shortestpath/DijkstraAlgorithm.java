package org.insa.graphs.algorithm.shortestpath;
import java.util.*;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Graph graph = data.getGraph();
        List<Node> nodes = graph.getNodes();
        int nbNodes = nodes.size();
        List<Label> labels = new ArrayList<Label>();
        
        for (int i = 0; i<nbNodes; i++) {
        	Label label = new Label(nodes.get(i), false, Double.POSITIVE_INFINITY, null);
        	labels.add(label);
        }
        int idOrigine = data.getOrigin().getId();
        labels.get(idOrigine).setCost(0.);
        tas.insert(labels.get(idOrigine));
        int sommetsMarqués = 0;
        while (sommetsMarqués<nbNodes && !tas.isEmpty()) {
        	Node sommetCourant = tas.deleteMin().getSommet();
        	labels.get(sommetCourant.getId()).setMarque(true);
        	List<Arc> successeurs = sommetCourant.getSuccessors();
        	for (Arc arc : successeurs) {
        		int idFils = arc.getDestination().getId();
        		if (!labels.get(idFils).getMarque()) {
        			double coût = data.getCost(arc);
        			double coûtCourant = labels.get(sommetCourant.getId()).getCost();
        			if (labels.get(idFils).getCost() > coûtCourant + coût) {
        				try {tas.remove(labels.get(idFils));}
                		catch (ElementNotFoundException e) {}
        				labels.get(idFils).setCost(coûtCourant + coût);
        				labels.get(idFils).setPère(arc);
        				tas.insert(labels.get(idFils));
        				
        			}
        		}
        	}
        	
        }
        List<Arc> cheminFinal = new ArrayList<Arc>();
        Arc arcCourant = labels.get(data.getDestination().getId()).getPère();
        while (arcCourant != null) {
            cheminFinal.add(0,arcCourant);
        	arcCourant = labels.get(arcCourant.getOrigin().getId()).getPère();
        }
        
        if (cheminFinal.isEmpty()) solution = new ShortestPathSolution(data,Status.INFEASIBLE);
        else solution = new ShortestPathSolution(data,Status.FEASIBLE,new Path(graph,cheminFinal));
        return solution;
    }

}
