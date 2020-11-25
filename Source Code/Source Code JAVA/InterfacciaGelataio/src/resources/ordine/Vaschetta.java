package resources.ordine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file Vaschetta.java
 *
 * @brief Classe che rappresenta una vaschetta
 */
import java.util.Arrays;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class Vaschetta
 *
 * @brief Classe che rappresenta una vaschetta
 *
 * La seguente classe rappresenta un'astrazione di una vaschetta fisica,
 * componente costituente un ordine
 */
public class Vaschetta {

    /**
     * Rappresenta una lista di gusti contenuti all'interno della vaschetta
     */
    private final String[] cibi;
    private final String[] bevande;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @see Vaschetta.gustoGelato
     * @see Vaschetta.Dimensioni.dimensioni
     */
    public Vaschetta(String[] cibi, String[] bevande) {
        this.cibi = cibi;
        this.bevande = bevande;
    }

    /**
     * @brief Ritorna il valore dell'attributo Vaschetta.gustoGelato
     *
     * @return Il valore dell'attributo Vaschetta.cibi
     *
     * @see Vaschetta.gustoGelato
     */
    public String[] getCibi() {
        return cibi;
    }

    public String[] getBevande() {
        return bevande;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Arrays.deepHashCode(this.cibi);
        hash = 59 * hash + Arrays.deepHashCode(this.bevande);
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
        final Vaschetta other = (Vaschetta) obj;
        if (!Arrays.deepEquals(this.cibi, other.cibi)) {
            return false;
        }
        if (!Arrays.deepEquals(this.bevande, other.bevande)) {
            return false;
        }
        return true;
    }

    /**
     * @brief Genera e ritorna il toString di questa classe
     *
     * @return Il toString di questa classe
     *
     * @see Vaschetta.gustoGelato
     * @see Vaschetta.dimensioni
     */
    @Override
    public String toString() {

        String toReturn = "";
        toReturn += "Cibi: " + Arrays.toString(this.cibi) + "<br>";
        toReturn += "Bevande: " + Arrays.toString(this.bevande);

        return toReturn;
    }

}
