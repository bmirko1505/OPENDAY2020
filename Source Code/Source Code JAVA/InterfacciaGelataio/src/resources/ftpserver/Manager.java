package resources.ftpserver;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file Manager.java
 *
 * @brief Classe contenente dati condivisi tra le classi
 * resources.ftpserver.FTPServer e resources.ftpserver.Keeper
 */
import java.util.concurrent.Semaphore;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class Manager
 *
 * @brief Classe contenente dati condivisi tra le classi
 * resources.ftpserver.FTPServer e resources.ftpserver.Keeper
 *
 * La seguente classe contiene dati condivisi tra le classi
 * resources.ftpserver.FTPServer e resources.ftpserver.Keeper. Fornisce,
 * inoltre, la gestione della concorrenza a codesti dati condivisi.
 */
public class Manager {

    // Oggetto di classe FTPClient
    // La classe FTPClient della libreria org.apache.commons.net.ftp.FTPClient
    // fornisce vari servizi per la comunicazione con un server FTP
    /**
     * Rappresenta un oggetto di classe FTPClient, la quale fornisce vari
     * servizi per la comunicazione con un server FTP
     */
    private final FTPClient server;

    // Oggetto di classe Semaphore per gestire
    // l'accesso concorrente alla risorsa server
    /**
     * Rappresenta un oggetto di classe Semaphore per gestire la concorrenza
     * nell'accesso alla risorsa Manager.server
     */
    private final Semaphore serverMutex;

    // Flag booleano indicante se o se non
    // si è connessi al server
    /**
     * Rappresenta un flag booleano indicante se o se non si è connessi al
     * server
     */
    private Boolean isConnected;

    // Flag booleano indicante se o se non
    // visualizzare le risposte del server su
    // console
    /**
     * Rappresenta un flag booleano indicante se o se non visualizzare le
     * risposte del server su console
     */
    private final Boolean showServerReplies;

    // Flag booleano indicante se o se non
    // il server supporta la funziona keep alive
    // tramite comandi "noop"
    /**
     * Rappresenta un flag booleano indicante se o se non il server supporta la
     * funzione keep alive tramite comandi "noop"
     */
    private final Boolean isNoOp;

    // Flag booleano indicante se o se non
    // il server supporta il logout con possibile
    // login successivo senza disconnessione
    /**
     * Rappresenta un flag booleano indicante se o se non il server supporta il
     * logout con possibile login successivo senza disconnessione
     */
    private final Boolean isLogBidirectional;

    // Costruttore parametrico
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param showServerReplies Flag booleano indicante se o se non visualizzare
     * le risposte del server su console
     * @param isNoOp Flag booleano indicante se o se non il server supporta la
     * funzione keep alive tramite comandi "noop"
     * @param isLogBidirectional Flag booleano indicante se o se non il server
     * supporta il logout con possibile login successivo senza disconnessione
     *
     * @see Manager.server
     * @see Manager.serverMutex
     * @see Manager.isConnected
     * @see Manager.showServerReplies
     * @see Manager.isNoOp
     * @see Manager.isLogBidirectional
     */
    Manager(final Boolean showServerReplies, final Boolean isNoOp, final Boolean isLogBidirectional) {
        // Inizializzazione
        this.server = new FTPClient();
        this.serverMutex = new Semaphore(1);
        this.isConnected = false;
        this.showServerReplies = showServerReplies;
        this.isNoOp = isNoOp;
        this.isLogBidirectional = isLogBidirectional;
    }

    // Ritorna il valore dell'attributo server
    /**
     * @brief Ritorna il valore dell'attributo Manager.server
     *
     * @return Il valore dell'attributo Manager.server
     *
     * @see Manager.server
     */
    FTPClient getServer() {
        return server;
    }

    // Ritorna il valore dell'attributo serverMutex
    /**
     * @brief Ritorna il valore dell'attributo Manager.serverMutex
     *
     * @return Il valore dell'attributo Manager.serverMutex
     *
     * @see Manager.serverMutex
     */
    Semaphore getServerMutex() {           
        return serverMutex;
    }

    // Ritorna il valore dell'attributo isConnected
    /**
     * @brief Ritorna il valore dell'attributo Manager.isConnected
     *
     * @return Il valore dell'attributo Manager.isConnected
     *
     * @see Manager.isConnected
     */
    Boolean getIsConnected() {
        return isConnected;
    }

    // Imposta il valore dell'attributo isConnected
    /**
     * @brief Imposta il valore dell'attributo Manager.isConnected
     *
     * @param isConnected Il valore da assegnare all'attributo
     * Manager.isConnected
     *
     * @see Manager.isConnected
     */
    void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    // Ritorna il valore dell'attributo isNoOp
    /**
     * @brief Ritorna il valore dell'attributo Manager.isNoOp
     *
     * @return Il valore dell'attributo Manager.isNoOp
     *
     * @see Manager.isNoOp
     */
    Boolean getIsNoOp() {
        return isNoOp;
    }

    // Ritorna il valore dell'attributo getShowServerReplies
    /**
     * @brief Ritorna il valore dell'attributo Manager.showServerReplies
     *
     * @return Il valore dell'attributo Manager.showServerReplies
     *
     * @see Manager.showServerReplies
     */
    Boolean getShowServerReplies() {
        return showServerReplies;
    }

    // Ritorna il valore dell'attributo isLogBidirectional
    /**
     * @brief Ritorna il valore dell'attributo Manager.isLogBidirectional
     *
     * @return Il valore dell'attributo Manager.isLogBidirectional
     *
     * @see Manager.isLogBidirectional
     */
    Boolean getIsLogBidirectional() {
        return isLogBidirectional;
    }

}
