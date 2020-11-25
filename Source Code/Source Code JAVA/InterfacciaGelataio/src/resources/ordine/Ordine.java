package resources.ordine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file Ordine.java
 *
 * @brief Classe che rappresenta la struttura "Ordine"
 */
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JList;
import resources.compositionlistapredefinitagusti.CompositionListaPredefinitaGusti;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class Ordine
 *
 * @brief Classe che rappresenta la struttura "Ordine"
 *
 * La seguente classe rappresenta l'astrazione della struttura "Ordine" e funge
 * da punto di riferimento per i rispettivi parser XML e generator XML che
 * operano su quest'ultima.
 *
 * @see resources.ordine.xml.generatorxml.GeneratorXMLOrdine
 * @see resources.ordine.xml.generatorxmlv2.GeneratorXMLOrdinev2
 * @see resources.ordine.xml.parserxml.ParserXMLOrdine
 */
public class Ordine {

    /**
     * Rappresenta una lista contenente tutte le vaschette ordinate
     * dall'ordinatore
     *
     * @see resources.ordine.Vaschetta
     */
    private final String[] cibi;
    private final String[] bevande;

    /**
     * Rappresenta il numero di telefono dell'ordinatore
     */
    private final String numeroTelefonico;

    /**
     * CLASSE (5BI)
     */
    private final String indirizzoConsegna;

    /**
     * PIANO (Lotto 1 Piano 2)
     */
    private final String piano;

    /**
     * Rappresenta l'orario di consegna dell'ordinatore
     */
    private final String orarioConsegna;

    /**
     * Rappresenta il prezzo totale dell'ordina corrente
     */
    private Float prezzoTotale;

    private final JList jListListaPredefinitaGusti;

    /**
     * @param jListListaPredefinitaGusti
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param vaschette Lista contenente tutte le vaschette ordinate
     * dall'ordinatore
     * @param cognome Cognome dell'ordinatore
     * @param numeroTelefonico Numero di telefono dell'ordinatore
     * @param indirizzoConsegna Indirizzo di consegna dell'ordinatore
     * @param orarioConsegna Orario di consegna dell'ordinatore
     * @param jSpinnerVaschettaPiccola Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaPiccola per ottenere il
     * prezzo delle vaschette piccole per procedere con il calcolo del
     * Ordine.prezzoTotale
     * @param jSpinnerVaschettaMedia Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaMedia per ottenere il
     * prezzo delle vaschette medie per procedere con il calcolo del
     * Ordine.prezzoTotale
     * @param jSpinnerVaschettaGrande Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaGrande per ottenere il
     * prezzo delle vaschette grandi per procedere con il calcolo del
     * Ordine.prezzoTotale
     *
     * @see Ordine.vaschette
     * @see Ordine.cognome
     * @see Ordine.numeroTelefonico
     * @see Ordine.indirizzoConsegna
     * @see Ordine.orarioConsegna
     * @see Ordine.jSpinnerVaschettaPiccola
     * @see Ordine.jSpinnerVaschettaMedia
     * @see Ordine.jSpinnerVaschettaGrande
     * @see Ordine.calcolaPrezzo()
     */
    public Ordine(String[] cibi, String[] bevande, String indirizzoConsegna, String piano, String numeroTelefonico, String orarioConsegna,
            JList jListListaPredefinitaGusti) {
        this.cibi = cibi;
        this.bevande = bevande;
        this.numeroTelefonico = numeroTelefonico;
        this.indirizzoConsegna = indirizzoConsegna;
        this.piano = piano;
        this.orarioConsegna = orarioConsegna;
        this.jListListaPredefinitaGusti = jListListaPredefinitaGusti;
        this.calcolaPrezzo();
    }

    /**
     * @brief Calcola il Ordine.prezzoTotale dell'ordine
     *
     * Il seguente metodo calcola il Ordine.prezzoTotale dell'ordine in base ai
     * prezzi ricavati dagli spinner Ordine.jSpinnerVaschettaPiccola,
     * Ordine.jSpinnerVaschettaMedia, Ordine.jSpinnerVaschettaGrande
     *
     * @see Ordine.Ordine()
     * @see Ordine.vaschette
     * @see Ordine.jSpinnerVaschettaPiccola
     * @see Ordine.jSpinnerVaschettaMedia
     * @see Ordine.jSpinnerVaschettaGrande
     * @see Ordine.prezzoTotale
     */
    private void calcolaPrezzo() {
        Float prezzoTotaleTemp = 0f;

        for (var cibo : this.cibi) {
            for (int i = 0; i < this.jListListaPredefinitaGusti.getModel().getSize(); i++) {
                if (cibo == null ? (((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getGusto()) == null : cibo.equals(((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getGusto())) {
                    prezzoTotaleTemp += ((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getPrezzo();
                }
            }
        }

        for (var bevanda : this.bevande) {
            for (int i = 0; i < this.jListListaPredefinitaGusti.getModel().getSize(); i++) {
                if (bevanda == null ? (((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getGusto()) == null : bevanda.equals(((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getGusto())) {
                    prezzoTotaleTemp += ((CompositionListaPredefinitaGusti) this.jListListaPredefinitaGusti.getModel().getElementAt(i)).getPrezzo();
                }
            }
        }

        this.prezzoTotale = prezzoTotaleTemp;
    }

    /**
     * @brief Ritorna il valore dell'attributo Ordine.numeroTelefonico
     *
     * @return Il valore dell'attributo Ordine.numeroTelefonico
     *
     * @see Ordine.numeroTelefonico
     */
    public String getNumeroTelefonico() {
        return numeroTelefonico;
    }

    /**
     * @brief Ritorna il valore dell'attributo Ordine.indirizzoConsegna
     *
     * @return Il valore dell'attributo Ordine.indirizzoConsegna
     *
     * @see Ordine.indirizzoConsegna
     */
    public String getIndirizzoConsegna() {
        return indirizzoConsegna;
    }

    /**
     * @brief Ritorna il valore dell'attributo Ordine.orarioConsegna
     *
     * @return Il valore dell'attributo Ordine.orarioConsegna
     *
     * @see Ordine.orarioConsegna
     */
    public String getOrarioConsegna() {
        return orarioConsegna;
    }

    /**
     * @brief Ritorna il valore dell'attributo Ordine.prezzoTotale
     *
     * @return Il valore dell'attributo Ordine.prezzoTotale
     *
     * @see Ordine.prezzoTotale
     */
    public Float getPrezzoTotale() {
        return prezzoTotale;
    }

    public String[] getCibi() {
        return cibi;
    }

    public String[] getBevande() {
        return bevande;
    }

    public String getPiano() {
        return piano;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Arrays.deepHashCode(this.cibi);
        hash = 23 * hash + Arrays.deepHashCode(this.bevande);
        hash = 23 * hash + Objects.hashCode(this.numeroTelefonico);
        hash = 23 * hash + Objects.hashCode(this.indirizzoConsegna);
        hash = 23 * hash + Objects.hashCode(this.piano);
        hash = 23 * hash + Objects.hashCode(this.orarioConsegna);
        hash = 23 * hash + Objects.hashCode(this.prezzoTotale);
        hash = 23 * hash + Objects.hashCode(this.jListListaPredefinitaGusti);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ordine other = (Ordine) obj;
        if (!Objects.equals(this.numeroTelefonico, other.numeroTelefonico)) {
            return false;
        }
        if (!Objects.equals(this.indirizzoConsegna, other.indirizzoConsegna)) {
            return false;
        }
        if (!Objects.equals(this.piano, other.piano)) {
            return false;
        }
        if (!Objects.equals(this.orarioConsegna, other.orarioConsegna)) {
            return false;
        }
        if (!Arrays.deepEquals(this.cibi, other.cibi)) {
            return false;
        }
        if (!Arrays.deepEquals(this.bevande, other.bevande)) {
            return false;
        }
        if (!Objects.equals(this.prezzoTotale, other.prezzoTotale)) {
            return false;
        }
        if (!Objects.equals(this.jListListaPredefinitaGusti, other.jListListaPredefinitaGusti)) {
            return false;
        }
        return true;
    }

    /**
     * @brief Returns a hash code value for the object
     *
     * @return A hash code value for the object
     *
     * @see Ordine.vaschette
     * @see Ordine.cognome
     * @see Ordine.numeroTelefonico
     * @see Ordine.indirizzoConsegna
     * @see Ordine.orarioConsegna
     * @see Ordine.prezzoTotale
     */
    /**
     * @brief Indicates whether some other object is "equal to" this one
     *
     * @param obj The reference object with which to compare
     *
     * @return Whether some other object is "equal to" this one
     *
     * @see Ordine.vaschette
     * @see Ordine.cognome
     * @see Ordine.numeroTelefonico
     * @see Ordine.indirizzoConsegna
     * @see Ordine.orarioConsegna
     * @see Ordine.prezzoTotale
     */
    /**
     * @brief Genera e ritorna il toString di questa classe
     *
     * @return Il toString di questa classe
     *
     * @see Ordine.vaschette
     * @see Ordine.cognome
     * @see Ordine.numeroTelefonico
     * @see Ordine.indirizzoConsegna
     * @see Ordine.orarioConsegna
     * @see Ordine.prezzoTotale
     */
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "Indirizzo di consegna: " + "<b>" + this.indirizzoConsegna + "</b>" + "<br>";
        toReturn += "Lotto e piano di consegna: " + "<b>" + this.piano + "</b>" + "<br>";
        toReturn += "Ora di consegna: " + "<b>" + this.orarioConsegna + "</b>" + "<br>";
        toReturn += "Numero di telefono: " + "<b>" + this.numeroTelefonico + "</b>" + "<br>";
        toReturn += "Cibi: <b>{ </b>";
        for (var each : this.cibi) {
            toReturn += "<b>" + each + ", </b>";
        }
        toReturn += "<b>}</b><br>";

        toReturn += "Bevande: <b>{ </b>";
        for (var each : this.bevande) {
            toReturn += "<b>" + each + ", </b>";
        }
        toReturn += "<b>}</b><br>";

        toReturn += "Prezzo: " + "<b>" + this.prezzoTotale.toString() + "€</b>" + "<br>";
        toReturn += "---------------------------------------------------------------------------------------------------------------------------------------------";
        
        return toReturn;
    }

}
