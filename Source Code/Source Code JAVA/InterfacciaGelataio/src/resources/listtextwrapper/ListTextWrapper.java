package resources.listtextwrapper;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file ListTextWrapper.java
 *
 * @brief Classe per formattare e visualizzare correttamente un elemento della
 * lista InterfacciaPrincipaleGelataio.jListOrdini
 */
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class ListTextWrapper
 *
 * @brief Classe per formattare e visualizzare correttamente un elemento della
 * lista InterfacciaPrincipaleGelataio.jListOrdini
 *
 * La seguente classe effettua una formattazione delle informazioni da
 * visualizzare nella lista InterfacciaPrincipaleGelataio.jListOrdini di un suo
 * elemento. Questo permette una visualizzazione di quest'ultime informazioni
 * più ordinata e accattivante.
 */
public class ListTextWrapper extends DefaultListCellRenderer {

    /**
     * Rappresenta un parametro di formattazione stilistica
     */
    public static final String HTML_1 = "<html><body style='width: ";

    /**
     * Rappresenta un parametro di formattazione stilistica
     */
    public static final String HTML_2 = "px'>";

    /**
     * Rappresenta un parametro di formattazione stilistica
     */
    public static final String HTML_3 = "</html>";

    /**
     * Rappresenta un parametro di formattazione stilistica
     */
    private final int width;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param width Larghezza in pixel della zona destinata a visualizzare le
     * informazioni formattate
     *
     * @see ListTextWrapper.width
     */
    public ListTextWrapper(int width) {
        this.width = width;
    }

    /**
     * @brief Effettua l'effettiva formattazione delle informazioni
     *
     * Il seguente metodo effettua effettivamente la formattazione delle
     * informazioni destinate alla visualizzazione
     *
     * @param list The JList we're painting
     * @param value The value returned by list.getModel().getElementAt(index)
     * @param index The cells index
     * @param isSelected True if the specified cell was selected
     * @param cellHasFocus True if the specified cell has the focus
     * @return A component whose paint() method will render the specified value
     *
     * @see ListTextWrapper.width
     * @see ListTextWrapper.HTML_1
     * @see ListTextWrapper.HTML_2
     * @see ListTextWrapper.HTML_3
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString()
                + HTML_3;
        return super.getListCellRendererComponent(list, text, index, isSelected,
                cellHasFocus);
    }

}
