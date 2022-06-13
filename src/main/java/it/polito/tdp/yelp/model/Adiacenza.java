package it.polito.tdp.yelp.model;

public class Adiacenza {

	private User user1;
	private User user2;
	private int simDegree;
	
	public Adiacenza(User user1, User user2, int simDegree) {
		this.user1 = user1;
		this.user2 = user2;
		this.simDegree = simDegree;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public int getSimDegree() {
		return simDegree;
	}

	public void setSimDegree(int simDegree) {
		this.simDegree = simDegree;
	}
	
}
