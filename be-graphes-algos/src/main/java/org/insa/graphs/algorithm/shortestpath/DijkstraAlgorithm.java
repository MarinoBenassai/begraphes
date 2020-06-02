package org.insa.graphs.algorithm.shortestpath;
import java.util.*;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public Label makeLabel (ShortestPathData data, Node sommet , boolean marque, double coût, Arc père) {
    	 return new Label(sommet, marque, coût, père);
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
        	Label label = makeLabel(data, nodes.get(i), false, Double.POSITIVE_INFINITY, null);
        	labels.add(label);
        }
        int idOrigine = data.getOrigin().getId();
        labels.get(idOrigine).setCost(0.);
        //System.out.print(labels.get(idOrigine)+"\n");
        notifyOriginProcessed(data.getOrigin());
        tas.insert(labels.get(idOrigine));
        int sommetsMarqués = 0;
        double coûtPrécédent = labels.get(idOrigine).getTotalCost();
        while (sommetsMarqués<nbNodes && !tas.isEmpty() && !labels.get(data.getDestination().getId()).getMarque()) {
        	Node sommetCourant = tas.deleteMin().getSommet();
        	labels.get(sommetCourant.getId()).setMarque(true);
        	if (coûtPrécédent > labels.get(sommetCourant.getId()).getTotalCost()) System.out.print("Problème de coût décroissant\n");;
        	coûtPrécédent = labels.get(sommetCourant.getId()).getCost();
        	notifyNodeMarked(sommetCourant);
        	List<Arc> successeurs = sommetCourant.getSuccessors();
        	for (Arc arc : successeurs) {
        		int idFils = arc.getDestination().getId();
        		if (!labels.get(idFils).getMarque() & data.isAllowed(arc)) {
        			double coût = data.getCost(arc);
        			double coûtCourant = labels.get(sommetCourant.getId()).getCost();
        			if (labels.get(idFils).getCost() > coûtCourant + coût) {
        				try {tas.remove(labels.get(idFils));}
                		catch (ElementNotFoundException e) {
                			notifyNodeReached(arc.getDestination());
                		}
        				labels.get(idFils).setCost(coûtCourant + coût);
        				labels.get(idFils).setPère(arc);
        				tas.insert(labels.get(idFils));
        				if (!tas.isValid()) {
        					System.out.print("Tas incorrect\n");
        				}
        			}
        		}
        	}
        	//System.out.print(tas.toStringTree());
        	
        }
        List<Arc> cheminFinal = new ArrayList<Arc>();        
        Arc arcCourant;
        Label labelCourant = labels.get(data.getDestination().getId());

        while (!labelCourant.getSommet().equals(data.getOrigin())) {
        	arcCourant = labelCourant.getPère();
            cheminFinal.add(0,arcCourant);
        	labelCourant = labels.get(arcCourant.getOrigin().getId());
        }

        
        if (labels.get(data.getDestination().getId()).getCost() == Double.POSITIVE_INFINITY) solution = new ShortestPathSolution(data,Status.INFEASIBLE);
        else {
        	notifyDestinationReached(data.getDestination());
        	if (cheminFinal.isEmpty()) solution = new ShortestPathSolution(data,Status.OPTIMAL,new Path(graph,data.getOrigin()));
        	else solution = new ShortestPathSolution(data,Status.OPTIMAL,new Path(graph,cheminFinal));
        }
        if (solution.getPath().isValid()) System.out.print("Chemin valide\n");
        else System.out.print("Chemin invalide\n");
        System.out.print("Fini !\n");
        return solution;
    }

}
