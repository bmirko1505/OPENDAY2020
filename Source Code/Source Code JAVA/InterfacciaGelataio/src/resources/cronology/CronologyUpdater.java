package resources.cronology;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file CronologyUpdater.java
 *
 * @brief Classe per garantire un aggiornamento automatico e continuo del
 * conteggio dei frammenti cronologici degli ordini evasi
 */
import guiGelataio.InterfacciaPrincipaleGalataio;
import java.util.TimerTask;
import javax.swing.JLabel;
import resources.ftpserver.FTPServer;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class CronologyUpdater
 *
 * @brief Classe per garantire un aggiornamento automatico e continuo del
 * conteggio dei frammenti cronologici degli ordini evasi
 *
 * La seguente classe rappresenta un thread daemon per garantire un
 * aggiornamento automatico e continuo del conteggio dei frammenti cronologici
 * degli ordini evasi.
 */
public class CronologyUpdater extends TimerTask {

    /**
     * Rappresenta la label sulla quale visualizzare il valore aggiornato del
     * conteggio
     *
     * @see InterfacciaPrincipaleGalataio.jLabelFrammentiCronologici
     */
    private final JLabel printLabel;

    /**
     * Rappresenta l'oggetto di classe FTPServer per permettere la comunicazione
     * bidirezionale con il server FTP
     *
     * @see resources.ftpserver.FTPServer
     */
    private final FTPServer server;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param server L'oggetto di classe FTPServer per permettere la
     * comunicazione bidirezionale con il server FTP
     * @param printLabel L'oggetto di classe label sul quale visualizzare il
     * valore del conteggio aggiornato
     *
     * @see resources.cronologyupdater.server
     * @see resources.cronologyupdater.printLabel
     */
    public CronologyUpdater(FTPServer server, JLabel printLabel) {
        this.server = server;
        this.printLabel = printLabel;
    }

    /**
     * @brief Codice eseguito dal thread
     *
     * Il seguente metodo conta il numero di frammenti cronologici degli ordini
     * evasi presenti nella directory "/update07/cronologia" del server. Questi
     * frammenti vengono utilizzati quando si scarica il database degli ordini
     * durante il processo della sua ricostruzione. Un numero di frammenti
     * troppo elevato potrebbe rendere il processo di ricostruzione lento e
     * lungo, dunque si consiglia di tenere osservato il conteggio di frammenti
     * ed effettuare un'eliminazione di questi ultimi se ritenuta necessaria.
     *
     * @see resources.cronologyupdater.CronologyUpdater.server
     * @see resources.cronologyupdater.CronologyUpdater.printLabel
     */
    @Override
    public void run() {

        // Imposto la directory cronologia
        this.server.setCurrentDirectory("../../../../update07/cronologia");

        // Ottengo lista di elementi nella directory del server
        // outSiteInGelataio
        String[] listElements = this.server.getListElements();

        this.printLabel.setText("Frammenti Cronologici: " + listElements.length);

    }
}
