package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event> {

	public enum EventType {
		DA_INTERVISTARE,
		FERIE
	}
	
	private int giorno;
	private EventType tipo;
	private int intervistatore;
	private User utenteIntervistato;
	
	public Event(int giorno, EventType tipo, int intervistatore, User utenteIntervistato) {
		super();
		this.giorno = giorno;
		this.tipo = tipo;
		this.intervistatore = intervistatore;
		this.utenteIntervistato = utenteIntervistato;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	public int getIntervistatore() {
		return intervistatore;
	}

	public void setIntervistatore(int intervistatore) {
		this.intervistatore = intervistatore;
	}

	public User getUtenteIntervistato() {
		return utenteIntervistato;
	}

	public void setUtenteIntervistato(User utenteIntervistato) {
		this.utenteIntervistato = utenteIntervistato;
	}

	@Override
	public String toString() {
		return "Event [giorno=" + giorno + ", tipo=" + tipo + ", intervistatore=" + intervistatore
				+ ", utenteIntervistato=" + utenteIntervistato + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.getGiorno()-o.getGiorno();
	}
	
	
}
