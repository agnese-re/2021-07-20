package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulator {

	// parametri in ingresso
	private int intervistatori;			// x1
	private int utentiDaIntervistare;	// x2
	
	// output della simulazione
	private List<Integer> utentiIntervistatiDa;		/* indice lista -> id intervistatore */
	private int durataSimulazione;
	
	// stato del mondo
	private Graph<User,DefaultWeightedEdge> grafo;
	private List<User> intervistabili;
	private List<User> intervistati;
	
	// coda prioritaria 
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<User,DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public void init(int intervistatori, int utentiDaIntervistare) {
		
		// inizializzazione parametri in input
		this.intervistatori = intervistatori;
		this.utentiDaIntervistare = utentiDaIntervistare;
		
		// inizializzazione output 
		this.utentiIntervistatiDa = new ArrayList<Integer>();
		for(int indice = 0; indice < this.intervistatori; indice++)
			this.utentiIntervistatiDa.add(0);
		this.durataSimulazione = 0;
		
		// inizializzazione stato del mondo
		this.intervistati = new ArrayList<User>();
		this.intervistabili = new ArrayList<User>(this.grafo.vertexSet());
		
		// precarico la coda degli eventi
		/* se x1 > x2 non potrei neanche assegnare un utente diverso a ciascun intervistatore il primo
		 * giorno. Andando un numero casuale si sceglierebbe sicuramente piu' volte lo stesso utente.
		 * Da qui la richiesta che 'il numero di intervistatori sia sempre (molto) minore del numero 
		 * di utenti da intervistare' */
		this.queue = new PriorityQueue<Event>();
		Random random = new Random();
		for(int intervistatore = 0; intervistatore < this.intervistatori; intervistatore++) {
			int indexIntervista = random.nextInt(intervistabili.size());	// da 0 a intervistabili.size()-1
			User utenteIntervistato = this.intervistabili.get(indexIntervista);
			queue.add(new Event(1,EventType.DA_INTERVISTARE,intervistatore,utenteIntervistato));
			this.utentiIntervistatiDa.set(intervistatore, 1);
			this.intervistati.add(utenteIntervistato);
			this.intervistabili.remove(indexIntervista);
		}
			
		System.out.println(this.queue);		// per debug
	}
	
	public void run() {
		while(!queue.isEmpty() && this.intervistati.size() < this.utentiDaIntervistare) {
			Event e = this.queue.poll();
			this.durataSimulazione = e.getGiorno();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case DA_INTERVISTARE: 
			double caso = Math.random();
			if(caso >= 0.40) {		// 60% dei casi -> intervista completata
				User nuovo = scegliNuovoIntervistato(e.getUtenteIntervistato());
				queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, 
						e.getIntervistatore(), nuovo));
				this.intervistati.add(nuovo);
				this.intervistabili.remove(nuovo);
				this.utentiIntervistatiDa.set(e.getIntervistatore(),
						this.utentiIntervistatiDa.get(e.getIntervistatore())+1);
			} else if(caso >= 0.20 && caso < 0.40) {	// 20% dei casi
				queue.add(new Event(e.getGiorno()+1, EventType.FERIE,
						e.getIntervistatore(), e.getUtenteIntervistato()));
			} else {	// restante 20%
				queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE,
					e.getIntervistatore(), e.getUtenteIntervistato()));
				}
			break;
		case FERIE:
			User nuovo = scegliNuovoIntervistato(e.getUtenteIntervistato());
			queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, 
					e.getIntervistatore(), nuovo));
			this.intervistati.add(nuovo);
			this.intervistabili.remove(nuovo);
			this.utentiIntervistatiDa.set(e.getIntervistatore(),
					this.utentiIntervistatiDa.get(e.getIntervistatore())+1);
			break;
		}	// CHIUDE LO SWITCH!
	}

	private User scegliNuovoIntervistato(User utenteIntervistato) {
		
		List<User> migliori = new ArrayList<User>();
		int pesoMassimoArco = 0;
		User nuovo = null;
		
		List<User> vicini = Graphs.neighborListOf(this.grafo, utenteIntervistato);
		vicini.removeAll(intervistati);	// vicini ancora intervistabili
		
		if(vicini.size() == 0) {
			// vertice isolato oppure tutti i vicini gia' intervistati
			Random rand = new Random();
			int indiceIntervista = rand.nextInt(this.intervistabili.size());
			nuovo = this.intervistabili.get(indiceIntervista);
		} else {
			if(vicini.size() == 1)	// un solo vicino ancora intervistabile
				nuovo = vicini.get(0);
			else {
				for(User u: vicini) {
					DefaultWeightedEdge e = this.grafo.getEdge(u, utenteIntervistato);
					if(this.grafo.getEdgeWeight(e) > pesoMassimoArco)
						pesoMassimoArco = (int)this.grafo.getEdgeWeight(e);
				}
				for(User u: vicini) {
					DefaultWeightedEdge e = this.grafo.getEdge(u, utenteIntervistato);
					if(this.grafo.getEdgeWeight(e) == pesoMassimoArco)
						migliori.add(u);
				}
			Random rand = new Random();
			nuovo = migliori.get(rand.nextInt(migliori.size()));
			}
		}	// chiude ELSE

	return nuovo;
	}

	public List<Integer> getUtentiIntervistatiDa() {
		return utentiIntervistatiDa;
	}

	public int getDurataSimulazione() {
		return durataSimulazione;
	}
	
}
