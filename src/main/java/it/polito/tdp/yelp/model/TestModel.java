package it.polito.tdp.yelp.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		m.creaGrafo(200, 2007);
		m.simulate(5,30);

	}

}
