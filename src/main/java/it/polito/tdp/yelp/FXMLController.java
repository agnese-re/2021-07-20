/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	cmbUtente.getItems().clear();
    	int numeroReviews;
    	try {
    		numeroReviews = Integer.parseInt(this.txtN.getText());
    		if(numeroReviews <= 0) {
    			txtResult.setText("Il numero di recensioni deve essere positivo");
    			return;
    		} else {
    			Integer anno = this.cmbAnno.getValue();
    			if(anno != null) {
    				this.model.creaGrafo(numeroReviews, anno);
    				this.cmbUtente.getItems().addAll(this.model.getVertici());
    				txtResult.appendText("Grafo creato con " + this.model.nVertici() + 
    						" vertici e " + this.model.nArchi() + " archi");
    			} else {
    				txtResult.setText("Devi scegliere un anno dal menu' a tendina");
    				return;
    			}
    		}
    	} catch(NumberFormatException nfe) {
    		txtResult.setText("Devi inserire un numero intero positivo nel campo '# recensioni (n)'");
    		return;
    	}
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	txtResult.clear();
    	User utente = this.cmbUtente.getValue();
    	if(utente != null) {
    		List<User> result = this.model.utentiSimili(utente);
    		txtResult.appendText("Utenti piu' simili a " + utente + ":\n\n");
    		for(User u: result)
    			txtResult.appendText(u.toString() + "\n");
    	}
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	int x1;
    	int x2;
    	try {
    		x1 = Integer.parseInt(this.txtX1.getText());
    		x2 = Integer.parseInt(this.txtX2.getText());
    		if(x2 > this.model.nVertici()) {
    			txtResult.setText("Il numero di utenti da intervistare deve essere minore del numero\n" +
    					" di utenti contenuti nel grafo (" + this.model.nVertici() + ")");
    			return;
    		} else {
    			if(x1 > x2) {
    				txtResult.setText("x1 deve essere (molto) minore di x2");
    				return;
    			} else {
		    		this.model.simulate(x1, x2);
		    		txtResult.appendText("Simulazione completata!\n");
		    		txtResult.appendText("DURATA: " + this.model.durataSimulazione() + " giorno/i\n");
		    		List<Integer> utentiIntervistatiDa = this.model.utentiIntervistatiDa();
		    		int intervistatore = 0;
		    		for(Integer intervistati: utentiIntervistatiDa) {
		    			txtResult.appendText("Intervistatore n." + intervistatore + " : " + intervistati + " utenti intervistati\n");
		    			intervistatore++;
		    		}
    			}
    		}
    	} catch(NumberFormatException nfe) {
    		txtResult.setText("x1 e x2 devono essere interi");
    		return;
    	}
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        for(int anno = 2005; anno <= 2013; anno++)
        	this.cmbAnno.getItems().add(anno);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
