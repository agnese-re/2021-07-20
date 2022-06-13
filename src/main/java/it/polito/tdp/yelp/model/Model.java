package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Simulator sim;
	private Graph<User,DefaultWeightedEdge> grafo;
	
	private Map<String,User> mappaUtenti;
	
	public Model() {
		dao = new YelpDao();
		
		mappaUtenti = new HashMap<String,User>();
		for(User u: this.dao.getAllUsers())
			mappaUtenti.put(u.getUserId(), u);
	}
	
	public void creaGrafo(int numeroReviews, int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiunta vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(mappaUtenti, numeroReviews));
		
		// aggiunta archi
		List<Adiacenza> archi = this.dao.getArchi(numeroReviews, anno, mappaUtenti);
		for(Adiacenza a: archi)
			if(this.grafo.vertexSet().contains(a.getUser1()) &&
					this.grafo.vertexSet().contains(a.getUser2()))
				Graphs.addEdgeWithVertices(this.grafo, a.getUser1(), a.getUser2(), a.getSimDegree());
	}
	
	public List<User> utentiSimili(User utente) {
		List<User> simili = new ArrayList<User>();
		int pesoMassimoArco = 0;
		
		for(User u: Graphs.neighborListOf(this.grafo, utente)) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, utente);
			if(this.grafo.getEdgeWeight(e) > pesoMassimoArco)
				pesoMassimoArco = (int)this.grafo.getEdgeWeight(e);
		}
		
		// for(DeafultWeightedEdge e: this.grafo.edgeSet().size()) NO. Voglio solo archi con i vicini
		for(User u: Graphs.neighborListOf(this.grafo, utente)) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, utente);
			if(this.grafo.getEdgeWeight(e) == pesoMassimoArco)
				simili.add(u);
		}
		
		Collections.sort(simili);
		return simili;
			
	}
	
	public void simulate(int intervistatori, int utentiDaIntervistare) {
		sim = new Simulator(this.grafo);
		sim.init(intervistatori,utentiDaIntervistare);
		sim.run();
	}
	
	public List<Integer> utentiIntervistatiDa() {
		return sim.getUtentiIntervistatiDa();
	}
	
	public int durataSimulazione() {
		return sim.getDurataSimulazione();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<User> getVertici() {
		List<User> users = new ArrayList<User>(this.grafo.vertexSet());
		Collections.sort(users);
		return users;
	}
}
