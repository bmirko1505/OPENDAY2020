package resources.ftpserver;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file Keeper.java
 *
 * @brief Classe per la gestione del servizio keep alive
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class Keeper
 *
 * @brief Classe per la gestione del servizio keep alive
 *
 * La seguente classe rappresenta un thread daemon per fornire il servizio keep
 * alive. Il servizio keep alive permettere di mantenere una connessione con il
 * server anche quando non avviene uno scambio di informazioni client-server per
 * un periodo di tempo prolungato. Dunque, il servizio keep alive garantisce la
 * connessione anche in sessioni idle.
 */
// Classe Keeper extends TimerTask
public class Keeper extends TimerTask {

    // Oggetto di classe Manager, classe contenente
    // dati condivisi tra le classi FTPServer e Keeper
    /**
     * Rappresenta un oggetto di classe resources.ftpserver.Manager, classe
     * contenente dati condivisi tra le classi resources.ftpserver.FTPServer e
     * resources.ftpserver.Keeper
     *
     * @see resources.ftpserver.FTPServer
     * @see resources.ftpserver.Manager
     * @see resources.ftpserver.Keeper
     */
    private final Manager manager;

    // Flag booleano per indicare lo stato di upload
    // del file di noop
    /**
     * Rappresenta un flag booleano per indicare lo stato di upload del file di
     * noop
     */
    private Boolean uploadFile;

    // Costruttore parametrico
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param manager Oggetto di classe resources.ftpserver.Manager, classe
     * contenente dati condivisi tra le classi resources.ftpserver.FTPServer e
     * resources.ftpserver.Keeper
     *
     * @see Keeper.manager
     */
    Keeper(Manager manager) {
        // Inizializzazione
        this.manager = manager;
    }

    /**
     * @brief Codice eseguito dal thread
     *
     * Il seguente metodo è l'implementazione del servizio keep alive
     *
     * @see Keeper.manager
     * @see Keeper.uploadFile
     * @see FTPServer.uploadFile()
     */
    @Override
    public void run() {

        try {
            // Provo ad effettuare l'invio del segnale
            // di NOP al server solo e solamente se sono
            // connesso al server
            if (this.manager.getIsConnected()) {
                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();

                // Comportamenti diversi in base a se o se non
                // il server in uso supporta il comando keep alive
                // "noop"
                if (this.manager.getIsNoOp()) {
                    // Come operazione "noop" invio il comando keep alive "noop"
                    // Questo può essere effettuato solo e solamente se il server in uso
                    // supporti il comando keep alive "noop"
                    this.manager.getServer().sendNoOp();
                } else {
                    // Imposto la directory outSiteInGelataio
                    this.manager.getServer().changeWorkingDirectory("../../../../update07/noop");

                    // Come operazione "noop" eseguo il caricamento di un file nel caso il server
                    // in uso non supporti il comando keep alive "noop"
                    // L'upload continuo di un semplice file non comporta rallentamenti significati;
                    // essendo sempre lo stesso, il file viene sovrascritto ogni volta che viene ricaricato
                    // nel server: questo permette di non causare uno spreco di risorse.
                    // Il metodo FTPServer.uploadFile è in un contesto statico -> necessaria la specificazione
                    // del server di lavoro
                    this.uploadFile = FTPServer.uploadFile(this.manager.getServer(), "noop.txt", new FileInputStream(new File("./data/noop/noop.txt")));
                }

                // Visualizzo invio 
                if (this.manager.getShowServerReplies()) {
                    if (this.uploadFile) {
                        System.out.println("Sent NOP");
                    } else {
                        System.out.println("Failed to send NOP. Keep alive is not guaranteed.");
                    }
                }

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            }
        } catch (IOException ex) {
            // Rilascio il permesso di uso del server
            this.manager.getServerMutex().release();
            Logger.getLogger(Keeper.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore di connessione al server FTP. Questo potrebbe essere causato da\n"
                    + " instabilità della connessione Internet utilizzata. Il software verrà terminato.\n"
                    + " Tutte le modifiche effettuate finora sono state salvate nel server FTP. Tutti gli ordini non ancora\n"
                    + " evasi verranno recuperati al successivo avvio del software. Riprovare in seguito.",
                    "Errore connessione server", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Keeper.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore di connessione al server FTP. Questo potrebbe essere causato da\n"
                    + " instabilità della connessione Internet utilizzata. Il software verrà terminato.\n"
                    + " Tutte le modifiche effettuate finora sono state salvate nel server FTP. Tutti gli ordini non ancora\n"
                    + " evasi verranno recuperati al successivo avvio del software. Riprovare in seguito.",
                    "Errore connessione server", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

}
