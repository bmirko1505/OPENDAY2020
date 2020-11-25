package resources.orariodelnegozio;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file OrarioDelNegozio.java
 *
 * @brief Classe che rappresenta la struttura "OrarioDelNegozio"
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class OrarioDelNegozio
 *
 * @brief Classe che rappresenta la struttura "OrarioDelNegozio"
 *
 * La seguente classe rappresenta l'astrazione della struttura
 * "OrarioDelNegozio" e funge da punto di riferimento per i rispettivi parser
 * XML e generator XML che operano su quest'ultima.
 *
 * @see
 * resources.orariodelnegozio.xml.generatorxml.GeneratorXMLModificaOrarioDelNegozio
 * @see
 * resources.orariodelnegozio.xml.parserxml.ParserXMLModificaOrarioDelNegozio
 */
public class OrarioDelNegozio {

    /**
     * Rappresenta l'orario di apertura del negozio
     */
    private final String oraDiApertura;

    /**
     * Rappresenta l'orario di chiusura del negozio
     */
    private final String oraDiChiusura;

    /**
     * Rappresenta un oggetto di classe SimpleDateFormat per effettuare la
     * formattazione dell'orario per permetterne una corretta visualizzazione
     */
    private final SimpleDateFormat simpleDateFormat;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param oraDiApertura L'orario di apertura del negozio (String)
     * @param oraDiChiusura L'orario di chiusura del negozio (String)
     *
     * @see OrarioDelNegozio.simpleDateFormat
     * @see OrarioDelNegozio.oraDiApertura
     * @see OrarioDelNegozio.oraDiChiusura
     */
    public OrarioDelNegozio(String oraDiApertura, String oraDiChiusura) {
        this.simpleDateFormat = new SimpleDateFormat("HH:mm");
        this.oraDiApertura = oraDiApertura;
        this.oraDiChiusura = oraDiChiusura;

    }

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param oraDiApertura L'orario di apertura del negozio (Date)
     * @param oraDiChiusura L'orario di chiusura del negozio (Date)
     *
     * @see OrarioDelNegozio.simpleDateFormat
     * @see OrarioDelNegozio.oraDiApertura
     * @see OrarioDelNegozio.oraDiChiusura
     */
    public OrarioDelNegozio(Date oraDiApertura, Date oraDiChiusura) {
        this.simpleDateFormat = new SimpleDateFormat("HH:mm");
        this.oraDiApertura = this.simpleDateFormat.format(oraDiApertura);
        this.oraDiChiusura = this.simpleDateFormat.format(oraDiChiusura);

    }

    /**
     * @brief Ritorna il valore dell'attributo OrarioDelNegozio.oraDiApertura
     *
     * @return Il valore dell'attributo OrarioDelNegozio.oraDiApertura (String)
     *
     * @see OrarioDelNegozio.oraDiApertura
     */
    public String getOraDiApertura() {
        return oraDiApertura;
    }

    /**
     * @brief Ritorna il valore dell'attributo OrarioDelNegozio.oraDiChiusura
     *
     * @return Il valore dell'attributo OrarioDelNegozio.oraDiChiusura (String)
     *
     * @see OrarioDelNegozio.oraDiChiusura
     */
    public String getOraDiChiusura() {
        return oraDiChiusura;
    }

    /**
     * @brief Ritorna il valore dell'attributo OrarioDelNegozio.oraDiChiusura
     *
     * @return Il valore dell'attributo OrarioDelNegozio.oraDiChiusura (Date)
     *
     * @see OrarioDelNegozio.oraDiChiusura
     */
    public Date getOraDiAperturaDate() {
        try {
            return this.simpleDateFormat.parse(this.oraDiApertura);
        } catch (ParseException ex) {
            Logger.getLogger(OrarioDelNegozio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @brief Ritorna il valore dell'attributo OrarioDelNegozio.oraDiChiusura
     *
     * @return Il valore dell'attributo OrarioDelNegozio.oraDiChiusura (Date)
     *
     * @see OrarioDelNegozio.oraDiChiusura
     */
    public Date getOraDiChiusuraDate() {
        try {
            return this.simpleDateFormat.parse(this.oraDiChiusura);
        } catch (ParseException ex) {
            Logger.getLogger(OrarioDelNegozio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
