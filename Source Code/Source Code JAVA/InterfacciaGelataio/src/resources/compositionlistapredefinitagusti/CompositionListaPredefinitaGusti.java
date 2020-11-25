package resources.compositionlistapredefinitagusti;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file CompositionListaPredefinitaGusti.java
 *
 * @brief Classe per la gestione degli elementi della lista
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti e
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1
 */
/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class CompositionListaPredefinitaGusti
 *
 * @brief Classe per la gestione degli elementi della lista
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti e
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1
 *
 * La seguente classe rappresenta un elemento della lista
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti e
 * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1necessari e permette
 * una corretta visualizzazione di quest'ultimo nel contesto della lista.
 */
public class CompositionListaPredefinitaGusti {

    /**
     * Rappresenta il gusto; rappresenta l'elemento testuale che compone
     * l'elemento stesso della lista
     */
    private final String gusto;
    private final String tipo;
    private final Float prezzo;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param gusto Il valore con il quale inizializzare l'attributo
     * CompositionListaPredefinitaGusti.gusto
     *
     * @see CompositionListaPredefinitaGusti.gusto
     */
    public CompositionListaPredefinitaGusti(String gusto, String tipo, Float prezzo) {
        this.gusto = gusto;
        this.tipo = tipo;
        this.prezzo = prezzo;
    }

    /**
     * @brief Ritorna il valore dell'attributo
     * CompositionListaPredefinitaGusti.gusto
     *
     * @return Il valore dell'attributo CompositionListaPredefinitaGusti.gusto
     *
     * @see CompositionListaPredefinitaGusti.gusto
     */
    public String getGusto() {
        return gusto;
    }

    public String getTipo() {
        return tipo;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    @Override
    public String toString() {
        return "{" + gusto + "; " + tipo + "; " + prezzo.toString() + "}";
    }

    /**
     * @brief Genera e ritorna il toString di questa classe
     *
     * @return Il toString di questa classe
     *
     * @see CompositionListaPredefinitaGusti.gusto
     */
}
