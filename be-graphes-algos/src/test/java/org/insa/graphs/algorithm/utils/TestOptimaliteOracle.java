package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import java.util.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;


import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;




public class TestOptimaliteOracle {

	
	static List<ShortestPathData> carteCarree = new ArrayList<ShortestPathData>();
	static List<ShortestPathData> carteToulouseInsaBikini = new ArrayList<ShortestPathData>();	
	static List<ShortestPathData> carteToulouseMemeDepartArrivee = new ArrayList<ShortestPathData>();
	static List<ShortestPathData> carteMayotteTrajetInfaisable = new ArrayList<ShortestPathData>();
	static List<ShortestPathData> carteToulouseInterditVoitures = new ArrayList<ShortestPathData>();
	static List<ArcInspector> listeFiltres= ArcInspectorFactory.getAllFilters();
	
    @BeforeClass
    public static void initAll() throws Exception {
    	
    	//Cartes sur lesquelles seront effectues les tests
        final String map1Name = "C:\\Users\\hp\\Documents\\Marino\\Cartes\\carre.mapgr"; //Carte carree
        final String map2Name = "C:\\Users\\hp\\Documents\\Marino\\Cartes\\europe\\france\\Toulouse.mapgr"; //Carte Toulouse
        final String map3Name = "C:\\Users\\hp\\Documents\\Marino\\Cartes\\mayotte.mapgr"; //Carte Mayotte
        
        final GraphReader reader1 = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map1Name))));
        final GraphReader reader2 = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map2Name))));
        final GraphReader reader3 = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map3Name))));


        final Graph graph1 = reader1.read();
        final Graph graph2 = reader2.read();
        final Graph graph3 = reader3.read();

        

    	
    	
    	//Scénario 1 : Trajet sur la carte carrée, du coin supérieur droit au coin inférieur gauche
    	
    	Node depart = graph1.get(9) ; //Coin supérieur droit
    	Node arrivee = graph1.get(15) ; //Coin inférieur gauche
    	
    	//Création des donnees pour les différents types de trajet
    	for (int i=0;i<listeFiltres.size();i++) {
    		carteCarree.add(new ShortestPathData(graph1, depart, arrivee, listeFiltres.get(i)));
    	}
    	
    	//Scénario 2 : Trajet INSA -> Bikini sur la carte Toulouse
    	
    	depart = graph2.get(5911) ; //INSA
    	arrivee = graph2.get(14532) ; //Bikini

    	
    	//Création des donnees pour les différents types de trajet
    	for (int i=0;i<listeFiltres.size();i++) {
    		carteToulouseInsaBikini.add(new ShortestPathData(graph2, depart, arrivee, listeFiltres.get(i)));
    	}
    	
    	//Scénario 3 : Départ = Arrivée, sur une route accessible à tous, toujours sur la carte Toulouse
    	
    	depart = graph2.get(5911) ; 
    	arrivee = depart ; 
    	
    	//Création des donnees pour les différents types de trajet
    	for (int i=0;i<listeFiltres.size();i++) {
    		carteToulouseMemeDepartArrivee.add(new ShortestPathData(graph2, depart, arrivee, listeFiltres.get(i)));
    	}
    	
    	//Scénario 4 : Sommet de départ et d’arrivée dans des composantes connexes différentes, carte de Mayotte
    	
    	depart = graph3.get(65) ; 
    	arrivee = graph3.get(6119) ;
    	
    	//Création des donnees pour les différents types de trajet
    	for (int i=0;i<listeFiltres.size();i++) {
    		carteMayotteTrajetInfaisable.add(new ShortestPathData(graph3, depart, arrivee, listeFiltres.get(i)));
    	}
    	
    	//Scénario 5 : Sommet de départ dans une zone interdite aux voitures, carte de Toulouse
    	
    	depart = graph2.get(26411) ; //Point de départ sur un route interdite aux voitures
    	arrivee = graph2.get(14532) ; //Point d'arrivée sur un route sans restriction

    	
    	//Création des donnees pour les différents types de trajet
    	for (int i=0;i<listeFiltres.size();i++) {
    		carteToulouseInterditVoitures.add(new ShortestPathData(graph2, depart, arrivee, listeFiltres.get(i)));
    	}
    }
    
    @Test
    public void testLongueurTouteRoute() {
    	//Mode = length, on teste donc la longueur des chemins renvoyés
    	
    	//Dijkstra
        assertEquals(new DijkstraAlgorithm(carteCarree.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteCarree.get(0)).run().getPath().getLength(), 1e-6);
        assertEquals(new DijkstraAlgorithm(carteToulouseInsaBikini.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(0)).run().getPath().getLength(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new DijkstraAlgorithm(carteToulouseMemeDepartArrivee.get(0)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer les longueurs
        assertEquals(new DijkstraAlgorithm(carteMayotteTrajetInfaisable.get(0)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(0)).run().getPath());
        assertEquals(new DijkstraAlgorithm(carteToulouseInterditVoitures.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(0)).run().getPath().getLength(), 1e-6);

    	//AStar
        assertEquals(new AStarAlgorithm(carteCarree.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteCarree.get(0)).run().getPath().getLength(), 1e-6);
        assertEquals(new AStarAlgorithm(carteToulouseInsaBikini.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(0)).run().getPath().getLength(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new AStarAlgorithm(carteToulouseMemeDepartArrivee.get(0)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer les longueurs
        assertEquals(new AStarAlgorithm(carteMayotteTrajetInfaisable.get(0)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(0)).run().getPath());
        assertEquals(new AStarAlgorithm(carteToulouseInterditVoitures.get(0)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(0)).run().getPath().getLength(), 1e-6);
      
    }
    
    @Test
    public void testLongueurVoituresSeulement() {
    	
    	//Mode = length, on teste donc la longueur des chemins renvoyés
    	
    	//Dijkstra
        assertEquals(new DijkstraAlgorithm(carteCarree.get(1)).run().getPath().getLength(), new BellmanFordAlgorithm(carteCarree.get(1)).run().getPath().getLength(), 1e-6);
        assertEquals(new DijkstraAlgorithm(carteToulouseInsaBikini.get(1)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(1)).run().getPath().getLength(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new DijkstraAlgorithm(carteToulouseMemeDepartArrivee.get(1)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans les deux cas suivants, donc on ne peut pas comparer les longueurs
        assertEquals(new DijkstraAlgorithm(carteMayotteTrajetInfaisable.get(1)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(1)).run().getPath());
        assertEquals(new DijkstraAlgorithm(carteToulouseInterditVoitures.get(1)).run().getPath(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(1)).run().getPath());

    	//AStar
        assertEquals(new AStarAlgorithm(carteCarree.get(1)).run().getPath().getLength(), new BellmanFordAlgorithm(carteCarree.get(1)).run().getPath().getLength(), 1e-6);
        assertEquals(new AStarAlgorithm(carteToulouseInsaBikini.get(1)).run().getPath().getLength(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(1)).run().getPath().getLength(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new AStarAlgorithm(carteToulouseMemeDepartArrivee.get(1)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans les deux cas suivants, donc on ne peut pas comparer les longueurs
        assertEquals(new AStarAlgorithm(carteMayotteTrajetInfaisable.get(1)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(1)).run().getPath());
        assertEquals(new AStarAlgorithm(carteToulouseInterditVoitures.get(1)).run().getPath(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(1)).run().getPath());
        
    }
    
    @Test
    public void testTempsTouteRoute() {
    	//Mode = Time, on teste donc la durée des chemins renvoyés
    	
    	//Dijkstra
        assertEquals(new DijkstraAlgorithm(carteCarree.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new DijkstraAlgorithm(carteToulouseInsaBikini.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new DijkstraAlgorithm(carteToulouseMemeDepartArrivee.get(2)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer le temps de parcours
        assertEquals(new DijkstraAlgorithm(carteMayotteTrajetInfaisable.get(2)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(2)).run().getPath());
        assertEquals(new DijkstraAlgorithm(carteToulouseInterditVoitures.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);

    	//AStar
        assertEquals(new AStarAlgorithm(carteCarree.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new AStarAlgorithm(carteToulouseInsaBikini.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new AStarAlgorithm(carteToulouseMemeDepartArrivee.get(2)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer les longueurs
        assertEquals(new AStarAlgorithm(carteMayotteTrajetInfaisable.get(2)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(2)).run().getPath());
        assertEquals(new AStarAlgorithm(carteToulouseInterditVoitures.get(2)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(2)).run().getPath().getMinimumTravelTime(), 1e-6);
      
    }
    
    @Test
    public void testTempsVoituresSeulement() {
    	//Mode = Time, on teste donc la durée des chemins renvoyés
    	
    	//Dijkstra
        assertEquals(new DijkstraAlgorithm(carteCarree.get(3)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(3)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new DijkstraAlgorithm(carteToulouseInsaBikini.get(3)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(3)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new DijkstraAlgorithm(carteToulouseMemeDepartArrivee.get(3)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans les deux cas suivants, donc on ne peut pas comparer les temps de parcours
        assertEquals(new DijkstraAlgorithm(carteMayotteTrajetInfaisable.get(3)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(3)).run().getPath());
        assertEquals(new DijkstraAlgorithm(carteToulouseInterditVoitures.get(3)).run().getPath(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(3)).run().getPath());

    	//AStar
        assertEquals(new AStarAlgorithm(carteCarree.get(3)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(3)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new AStarAlgorithm(carteToulouseInsaBikini.get(3)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(3)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new AStarAlgorithm(carteToulouseMemeDepartArrivee.get(3)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans les deux cas suivants, donc on ne peut pas comparer les temps de parcours
        assertEquals(new AStarAlgorithm(carteMayotteTrajetInfaisable.get(3)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(3)).run().getPath());
        assertEquals(new AStarAlgorithm(carteToulouseInterditVoitures.get(3)).run().getPath(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(3)).run().getPath());
      
    }
    
    
    @Test
    public void testTempsPietons() {
    	//Mode = Time, on teste donc la durée des chemins renvoyés
    	
    	//Dijkstra
        assertEquals(new DijkstraAlgorithm(carteCarree.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new DijkstraAlgorithm(carteToulouseInsaBikini.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new DijkstraAlgorithm(carteToulouseMemeDepartArrivee.get(4)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer le temps de parcours
        assertEquals(new DijkstraAlgorithm(carteMayotteTrajetInfaisable.get(4)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(4)).run().getPath());
        assertEquals(new DijkstraAlgorithm(carteToulouseInterditVoitures.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);

    	//AStar
        assertEquals(new AStarAlgorithm(carteCarree.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteCarree.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(new AStarAlgorithm(carteToulouseInsaBikini.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInsaBikini.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);
        //Bellman-Ford ne fonctionne pas dans le cas suivant, donc on compare au résultat attendu directement
        assertEquals(new AStarAlgorithm(carteToulouseMemeDepartArrivee.get(4)).run().getPath().getLength(), 0.,1e-6);
        //Il n'existe pas de chemin faisable dans le cas suivant, donc on ne peut pas comparer les longueurs
        assertEquals(new AStarAlgorithm(carteMayotteTrajetInfaisable.get(4)).run().getPath(), new BellmanFordAlgorithm(carteMayotteTrajetInfaisable.get(4)).run().getPath());
        assertEquals(new AStarAlgorithm(carteToulouseInterditVoitures.get(4)).run().getPath().getMinimumTravelTime(), new BellmanFordAlgorithm(carteToulouseInterditVoitures.get(4)).run().getPath().getMinimumTravelTime(), 1e-6);
      
    }
    
}
