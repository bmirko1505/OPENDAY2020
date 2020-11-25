package resources.prezzovaschette;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file PrezzoVaschette.java
 *
 * @brief Classe che rappresenta la struttura "PrezzoVaschette"
 */
/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class PrezzoVaschette
 *
 * @brief Classe che rappresenta la struttura "PrezzoVaschette"
 *
 * La seguente classe rappresenta l'astrazione della struttura "PrezzoVaschette"
 * e funge da punto di riferimento per i rispettivi parser XML e generator XML
 * che operano su quest'ultima.
 *
 * @see
 * resources.prezzovaschette.xml.generatorxml.GeneratorXMLModificaPrezzoVaschette
 * @see resources.prezzovaschette.xml.parserxml.ParserXMLModificaPrezzoVaschette
 */
public class PrezzoVaschette {

    /**
     * Rappresenta il prezzo di una vaschetta piccola
     */
    private final Float prezzoVaschettaPiccola;

    /**
     * Rappresenta il prezzo di una vaschetta media
     */
    private final Float prezzoVaschettaMedia;

    /**
     * Rappresenta il prezzo di una vaschetta grande
     */
    private final Float prezzoVaschettaGrande;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param prezzoVaschettaPiccola Il prezzo di una vaschetta piccola
     * @param prezzoVaschettaMedia Il prezzo di una vaschetta media
     * @param prezzoVaschettaGrande Il prezzo di una vaschetta grande
     *
     * @see PrezzoVaschette.prezzoVaschettaPiccola
     * @see PrezzoVaschette.prezzoVaschettaMedia
     * @see PrezzoVaschette.prezzoVaschettaGrande
     */
    public PrezzoVaschette(Float prezzoVaschettaPiccola, Float prezzoVaschettaMedia, Float prezzoVaschettaGrande) {
        this.prezzoVaschettaPiccola = prezzoVaschettaPiccola;
        this.prezzoVaschettaMedia = prezzoVaschettaMedia;
        this.prezzoVaschettaGrande = prezzoVaschettaGrande;
    }

    /**
     * @brief Ritorna il valore dell'attributo
     * PrezzoVaschette.prezzoVaschettaPiccola
     *
     * @return Il valore dell'attributo PrezzoVaschette.prezzoVaschettaPiccola
     *
     * @see PrezzoVaschette.prezzoVaschettaPiccola
     */
    public Float getPrezzoVaschettaPiccola() {
        return prezzoVaschettaPiccola;
    }

    /**
     * @brief Ritorna il valore dell'attributo
     * PrezzoVaschette.prezzoVaschettaMedia
     *
     * @return Il valore dell'attributo PrezzoVaschette.prezzoVaschettaMedia
     *
     * @see PrezzoVaschette.prezzoVaschettaMedia
     */
    public Float getPrezzoVaschettaMedia() {
        return prezzoVaschettaMedia;
    }

    /**
     * @brief Ritorna il valore dell'attributo
     * PrezzoVaschette.prezzoVaschettaGrande
     *
     * @return Il valore dell'attributo PrezzoVaschette.prezzoVaschettaGrande
     *
     * @see PrezzoVaschette.prezzoVaschettaGrande
     */
    public Float getPrezzoVaschettaGrande() {
        return prezzoVaschettaGrande;
    }
}
