package resources.ftpserver;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file FTPServer.java
 *
 * @brief Classe per la gestione della comunicazione con un server FTP
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class FTPServer
 *
 * @brief Classe per la gestione della comunicazione con un server FTP
 *
 * La seguente classe fornisce tutti i metodi necessari a stabilire una
 * connessione e ad relazionarsi con un server FTP.
 */
// Classe FTPServer
public class FTPServer {

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

    // Oggetto di classe Keeper
    // La classe Keeper rappresenta un thread, sempre in esecuzione
    // per mantenere attiva la comunicazione client-server, anche in
    // stato di idle
    /**
     * Rappresenta un oggetto di classe resources.ftpserver.Keeper, il quale
     * rappresenta un thread daemon per mantenere attiva la comunicazione
     * client-server, anche in stato di idle
     *
     * @see resources.ftpserver.Keeper
     */
    private final Keeper keeper;

    // Oggetto di classe Timer
    // La classe Timer (java.util) è necessaria
    // per creare una schedulazione personalizzata nel tempo
    // in ripetizione di un thread
    /**
     * Rappresenta un oggetto di classe Timer, il quale è necessario per
     * garantire la corretta esecuzione schedulata del thread FTPServer.keeper
     *
     * @see FTPServer.keeper
     */
    private final Timer timer;

    // Variabile di backup utilizzata per implementare
    // la protezione contro messaggi duplicati di risposta
    // da parte del server
    /**
     * Rappresenta una variabile di backup utilizzata per implementare la
     * protezione contro messaggi duplicati di risposta da parte del server
     *
     * @see FTPServer.getAndPrintServerReply()
     */
    private String[] backupReplies;

    // Prima coordinata: host
    /**
     * Rappresenta la prima coordinata per la comunicazione con il server FTP:
     * host
     */
    private String host;

    // Seconda coordinata: port
    /**
     * Rappresenta la seconda coordinata per la comunicazione con il server FTP:
     * port
     */
    private Integer port;

    // Flag booleano indicante se o se non
    // si è loggati nel server
    /**
     * Rappresenta un flag booleano indicante se o se non si è loggati nel
     * server
     */
    private Boolean isLoggedIn;

    // Costruttore parametrico
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param showServerReplies Flag booleano indicante se o se non visualizzare
     * su console le risposte da parte del server
     * @param isNoOp Flag booleano indicante se o se non il server supporta la
     * funzionalità keep alive: noop
     * @param isLogBidirectional Flag booleano indicante se o se non il server
     * supporta la funzionalità di login/logout bidirezionale
     *
     * @see FTPServer.manager
     * @see FTPServer.keeper
     * @see FTPServer.timer
     * @see FTPServer.backupReplies
     * @see FTPServer.host
     * @see FTPServer.port
     * @see FTPServer.isLoggedIn
     * @see FTPServer.startKeeper
     */
    public FTPServer(final Boolean showServerReplies, final Boolean isNoOp, final Boolean isLogBidirectional) {
        // Inizializzazione
        // Creazione oggetto di classe Manager
        this.manager = new Manager(showServerReplies, isNoOp, isLogBidirectional);

        // Creazione oggetto di classe Keeper
        // Al costruttore viene fornito come unico parametro
        // l'oggetto di classe Manager prima costruito
        this.keeper = new Keeper(manager);

        // Creazione oggetto di classe Timer
        // Al costruttore viene fornito un flag booleano
        // indicante se o se non schedulare thread deamon.
        // In questo caso, tutti i thread schedulati con questo
        // oggetto di classe Timer saranno daemon
        this.timer = new Timer(true);

        this.backupReplies = null;
        this.host = null;
        this.port = null;
        this.isLoggedIn = false;

        this.startKeeper();
    }

    // Metodo per avviare il thread rappresentato
    // dalla classe Keeper
    /**
     * @brief Avvia il thread rappresentato dall'oggetto FTPServer.keeper
     *
     * Il seguente metodo è necessario per avviare il thread rappresentato
     * dall'oggetto FTPServer.keeper
     *
     * @see FTPServer.timer
     * @see FTPServer.keeper
     */
    private void startKeeper() {
        // Avvio della schedulazione del thread rappresentato
        // dall'oggetto keeper di classe Keeper
        // La schedulazione avviene secondo le seguenti regole:
        // - primo ciclo di esecuzione: dopo 3 minuti
        // - dal secondo ciclo di esecuzione in poi: ogni 3 minuti
        /// this.timer.schedule(keeper, 180000, 180000);
        this.timer.schedule(keeper, 5000, 5000);
    }

    // Metodo per impostare le coordinate del server
    // con il quale comunicare
    /**
     * @brief Imposta le coordinate del server FTP con il quale comunicare
     *
     * Il seguente metodo imposta le coordinate dal server FTP con il quale
     * comunicare
     *
     * @param host La prima coordinata per la comunicazione con il server FTP:
     * host
     * @param port La seconda coordinata per la comunicazione con il server FTP:
     * port
     *
     * @return Un valore booleano indicante la corretta esecuzione
     * dell'impostazione delle coordinate del server FTP con il quale comunicare
     *
     * @pre La connessione con il server non deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.host
     * @see FTPServer.port
     */
    public Boolean setCoords(String host, Integer port) {
        // Variabile di stato
        Boolean status = false;

        // Imposto le nuove coordinate solo e solo se
        // non sono già connesso ad un server
        if (!this.manager.getIsConnected()) {
            // Imposto le coordinate
            this.host = host;
            this.port = port;

            // Operazione riuscita
            status = true;
        }

        return status;
    }

    // Metodo per impostare le coordinate del server
    // con il quale comunicare
    /**
     * @brief Imposta le coordinate del server FTP con il quale comunicare
     *
     * Il seguente metodo imposta le coordinate dal server FTP con il quale
     * comunicare
     *
     * @param host La prima coordinata per la comunicazione con il server FTP:
     * host
     * @param port La seconda coordinata per la comunicazione con il server FTP:
     * port
     *
     * @pre La connessione con il server non deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.host
     * @see FTPServer.port
     */
    public void setCoordsAM(String host, Integer port) {
        // Imposto le nuove coordinate solo e solo se
        // non sono già connesso ad un server
        if (!this.manager.getIsConnected()) {
            // Imposto le coordinate
            this.host = host;
            this.port = port;

            System.out.println("Coords changed.");
        } else {
            System.out.println("Already connected. Please disconnect first.");
        }
    }

    // Metodo per tentare di stabilire una connessione
    // con il server
    /**
     * @brief Tenta di stabilire una connessione con il server FTP
     *
     * Il seguente metodo tenta di stabilire una connessione con il server FTP
     *
     * @return Un valore booleano indicante la corretta esecuzione del tentativo
     * di stabilire una connessione con il server FTP
     *
     * @pre La connessione con il server non deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.host
     * @see FTPServer.port
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean connect() {
        // Variabile di stato
        Boolean status = false;

        // Provo a connettermi solo e solo se
        // non sono già connesso ad un server
        if (!this.manager.getIsConnected()) {
            // Controllo validità parametri
            if (this.host != null && this.port != null) {

                try {
                    // Utilizzo del metodo connect per connettermi al server
                    // E' necessaria la specificazione di 2 parametri:
                    // - host
                    // - port (la porta default per server FTP è 21)

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    this.manager.getServer().connect(this.host, this.port);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Sono connesso
                    this.manager.setIsConnected(true);

                    // Attivo la modalità passiva della connessione
                    // ci sono due modalità per stabilire una connessione
                    // client - server
                    // Modalità attiva:
                    // Viene aperta una porta sul client per permettere
                    // al server di connettersi al client attraverso quest'ultima.
                    // Il problema è che questo tipo di connessione solitamente non
                    // funziona a causa di protezioni firewall sul client.
                    // Modalità passiva:
                    // Viene aperta una porta sul server per permettere
                    // al client di connettersi al server attraverso quest'ultima.
                    // Questo aggira qualsiasi problema generato dal firewall sul client.
                    this.manager.getServer().enterLocalPassiveMode();

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Operazione riuscita
                    status = true;

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return status;
    }

    // Metodo per tentare di stabilire una connessione
    // con il server
    /**
     * @brief Tenta di stabilire una connessione con il server FTP
     *
     * Il seguente metodo tenta di stabilire una connessione con il server FTP
     *
     * @pre La connessione con il server non deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.host
     * @see FTPServer.port
     * @see FTPServer.getAndPrintServerReply()
     */
    public void connectAM() {
        // Provo a connettermi solo e solo se
        // non sono già connesso ad un server
        if (!this.manager.getIsConnected()) {
            // Controllo validità parametri
            if (this.host != null && this.port != null) {
                System.out.println("Trying to connect...");
                try {
                    // Utilizzo del metodo connect per connettermi al server
                    // E' necessaria la specificazione di 2 parametri:
                    // - host
                    // - port (la porta default per server FTP è 21)

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    this.manager.getServer().connect(this.host, this.port);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Sono connesso
                    this.manager.setIsConnected(true);

                    System.out.println("Connected.");

                    // Attivo la modalità passiva della connessione
                    // ci sono due modalità per stabilire una connessione
                    // client - server
                    // Modalità attiva:
                    // Viene aperta una porta sul client per permettere
                    // al server di connettersi al client attraverso quest'ultima.
                    // Il problema è che questo tipo di connessione solitamente non
                    // funziona a causa di protezioni firewall sul client.
                    // Modalità passiva:
                    // Viene aperta una porta sul server per permettere
                    // al client di connettersi al server attraverso quest'ultima.
                    // Questo aggira qualsiasi problema generato dal firewall sul client.
                    this.manager.getServer().enterLocalPassiveMode();

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Connection refused. Please check coords again.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Cannot connect. Invalid [null] coords.");
            }
        } else {
            System.out.println("Already connected. Please disconnect first.");
        }

    }

    // Metodo per tentare di effettuare il login
    // sul server
    /**
     * @brief Tenta di effettuare il login sul server FTP
     *
     * Il seguente metodo tenta di effettuare il login sul server FTP
     *
     * @param username Username per effettuare il login
     * @param password Password per effettuare il login
     *
     * @return Un valore booleano indicante la corretta esecuzione del tentativo
     * di effettuare il login sul server FTP
     *
     * @pre La connessione con il server deve essere attiva
     * @pre Il login sul server non deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean login(String username, String password) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad effettuare il login solo
        // e solamente se non sono già loggato
        if (!this.isLoggedIn) {
            // Provo a fare il login solo e solo se
            // sono già connesso ad un server
            if (this.manager.getIsConnected()) {
                // Controllo validità parametri
                if (username != null && password != null) {

                    try {
                        // Utilizzo del metodo login per effettuare l'effettivo login
                        // al server
                        // Il metodo restituisce un valore booleano:
                        // - true: login riuscito
                        // - false: login non riuscito
                        // E' necessaria la specificazione di 2 parametri:
                        // - username
                        // - password

                        // Acquisisco il permesso di uso del server
                        this.manager.getServerMutex().acquire();
                        boolean login = this.manager.getServer().login(username, password);

                        // Visualizzo eventuali risposte del server
                        this.getAndPrintServerReply();

                        // Controllo lo stato del login
                        if (login) {
                            // Sono loggato
                            this.isLoggedIn = true;

                            // Operazione riuscita
                            status = true;
                        }

                        // Rilascio il permesso di uso del server
                        this.manager.getServerMutex().release();
                    } catch (IOException ex) {
                        // Rilascio il permesso di uso del server
                        this.manager.getServerMutex().release();
                        Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }

        return status;
    }

    // Metodo per tentare di effettuare il login
    // sul server
    /**
     * @brief Tenta di effettuare il login sul server FTP
     *
     * Il seguente metodo tenta di effettuare il login sul server FTP
     *
     * @param username Username per effettuare il login
     * @param password Password per effettuare il login
     *
     * @pre La connessione con il server deve essere attiva
     * @pre Il login sul server non deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public void loginAM(String username, String password) {

        // Provo ad effettuare il login solo
        // e solamente se non sono già loggato
        if (!this.isLoggedIn) {
            // Provo a fare il login solo e solo se
            // sono già connesso ad un server
            if (this.manager.getIsConnected()) {
                // Controllo validità parametri
                if (username != null && password != null) {
                    System.out.println("Trying to login...");
                    try {
                        // Utilizzo del metodo login per effettuare l'effettivo login
                        // al server
                        // Il metodo restituisce un valore booleano:
                        // - true: login riuscito
                        // - false: login non riuscito
                        // E' necessaria la specificazione di 2 parametri:
                        // - username
                        // - password

                        // Acquisisco il permesso di uso del server
                        this.manager.getServerMutex().acquire();
                        boolean login = this.manager.getServer().login(username, password);

                        // Visualizzo eventuali risposte del server
                        this.getAndPrintServerReply();

                        // Controllo lo stato del login
                        if (login) {
                            // Sono loggato
                            this.isLoggedIn = true;

                            System.out.println("Login was successfull.");
                        } else {
                            System.out.println("Login wasn't successfull.");
                        }

                        // Rilascio il permesso di uso del server
                        this.manager.getServerMutex().release();
                    } catch (IOException ex) {
                        // Rilascio il permesso di uso del server
                        this.manager.getServerMutex().release();
                        System.out.println("Login wasn't successfull.");
                        Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    System.out.println("Cannot login. Invalid [null] username or/and password.");
                }
            } else {
                System.out.println("Not connected. Please connect first.");
            }
        } else {
            System.out.println("Already logged in. Please log out first.");
        }

    }

    // Metodo per ottenere la directory
    // di lavoro corrente
    /**
     * @brief Ottiene la directory di lavoro corrente
     *
     * Il seguente metodo ottiene la directory di lavoro corrente
     *
     * @return La directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public String getCurrentDirectory() {
        // Variabile per salvare il valore (stringa)
        // della directory di lavoro corrente
        String printWorkingDirectory = null;

        // Provo ad interrogare il server solo
        // e solamente se sono già loggato
        if (this.manager.getIsConnected()) {

            try {
                // Utilizzo del metodo printWorkingDirectory per
                // interrogare il server per ottenere la directory di lavoro
                // corrente

                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();
                printWorkingDirectory = this.manager.getServer().printWorkingDirectory();

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return printWorkingDirectory;
    }

    // Metodo per ottenere la directory
    // di lavoro corrente
    /**
     * @brief Ottiene la directory di lavoro corrente
     *
     * Il seguente metodo ottiene la directory di lavoro corrente
     *
     * @return La directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public String getCurrentDirectoryAM() {
        // Variabile per salvare il valore (stringa)
        // della directory di lavoro corrente
        String printWorkingDirectory = null;

        // Provo ad interrogare il server solo
        // e solamente se sono già loggato
        if (this.isLoggedIn) {
            try {
                System.out.println("Retrieving current working directory...");

                // Utilizzo del metodo printWorkingDirectory per
                // interrogare il server per ottenere la directory di lavoro
                // corrente
                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();
                printWorkingDirectory = this.manager.getServer().printWorkingDirectory();

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                System.out.println("Error retrieving current working directory. Please try again.");
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

        return printWorkingDirectory;
    }

    // Metodo per cambiare la directory
    // di lavoro corrente
    /**
     * @brief Cambia la directory di lavoro corrente
     *
     * Il seguente metodo cambia la directory di lavoro corrente
     *
     * @param directoryPath La nuova directory di lavoro corrente
     *
     * @return Un valore booleano indicante il corretto cambiamento della
     * directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean setCurrentDirectory(String directoryPath) {
        // Variabile di stato
        Boolean status = false;

        // Provo a cambiare la directory di lavoro corrente
        // solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (directoryPath != null) {

                try {
                    // Utilizzo il metodo changeWorkingDirectory per effettuare
                    // il cambio di directory.
                    // Il metodo ritorna un valore booleano:
                    // - true: la directory è stata cambiata
                    // - false: la directory non è stata cambiata

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean changeWorkingDirectory = this.manager.getServer().changeWorkingDirectory(directoryPath);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo lo stato del cambio di directory
                    if (changeWorkingDirectory) {
                        // Operazione riuscita
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return status;
    }

    // Metodo per cambiare la directory
    // di lavoro corrente
    /**
     * @brief Cambia la directory di lavoro corrente
     *
     * Il seguente metodo cambia la directory di lavoro corrente
     *
     * @param directoryPath La nuova directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public void setCurrentDirectoryAM(String directoryPath) {
        // Provo a cambiare la directory di lavoro corrente
        // solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (directoryPath != null) {
                try {
                    System.out.println("Changing current working directory...");

                    // Utilizzo il metodo changeWorkingDirectory per effettuare
                    // il cambio di directory.
                    // Il metodo ritorna un valore booleano:
                    // - true: la directory è stata cambiata
                    // - false: la directory non è stata cambiata
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean changeWorkingDirectory = this.manager.getServer().changeWorkingDirectory(directoryPath);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo lo stato del cambio di directory
                    if (changeWorkingDirectory) {
                        System.out.println("Current working directory changed.");
                    } else {
                        System.out.println("Current working directory not changed.");
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Current working directory not changed.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Cannot change current working directory. Invalid [null] directory path.");
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

    }

    // Metodo per ottenere una lista di elementi
    // presenti nella directory di lavoro corrente
    /**
     * @brief Ottiene una lista di elementi presenti nella directory di lavoro
     * corrente
     *
     * Il seguente metodo ottiene una lista di elementi presenti nella directory
     * di lavoro corrente
     *
     * @return Una lista di elementi presenti nella directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public String[] getListElements() {
        // Lista
        String[] listNames = null;
        ArrayList<String> updatedListNames;

        // Provo ad interrogare il server solo
        // e solamente se sono già loggato
        if (this.isLoggedIn) {

            try {
                // Utilizzo il metodo listNames per provare
                // ad ottenere una lista di elementi presenti nella
                // directory di lavoro corrente

                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();
                listNames = this.manager.getServer().listNames();
                updatedListNames = new ArrayList<>(Arrays.asList(listNames));
                updatedListNames.remove("..");
                updatedListNames.remove(".");

                listNames = Arrays.copyOf(updatedListNames.toArray(), updatedListNames.size(), String[].class);

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return listNames;
    }

    // Metodo per ottenere una lista di elementi
    // presenti nella directory di lavoro corrente
    /**
     * @brief Ottiene una lista di elementi presenti nella directory di lavoro
     * corrente
     *
     * Il seguente metodo ottiene una lista di elementi presenti nella directory
     * di lavoro corrente
     *
     * @return Una lista di elementi presenti nella directory di lavoro corrente
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public String[] getListElementsAM() {
        // Lista
        String[] listNames = null;
        ArrayList<String> updatedListNames;

        // Provo ad interrogare il server solo
        // e solamente se sono già loggato
        if (this.isLoggedIn) {
            try {
                System.out.println("Retrieving list elements...");

                // Utilizzo il metodo listNames per provare
                // ad ottenere una lista di elementi presenti nella
                // directory di lavoro corrente
                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();
                listNames = this.manager.getServer().listNames();
                updatedListNames = new ArrayList<>(Arrays.asList(listNames));
                updatedListNames.remove("..");
                updatedListNames.remove(".");

                listNames = Arrays.copyOf(updatedListNames.toArray(), updatedListNames.size(), String[].class);

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                System.out.println("Error retrieving list elements. Please try again.");
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

        return listNames;
    }

    // Metodo per caricare un file con nome unico
    // dal client al server
    /**
     * @brief Carica un file con nome unico dal client al server
     *
     * Il seguente metodo carica un file con nome unico dal client al server
     *
     * @param uploadStream Il file da caricare dal client al server
     *
     * @return Un valore booleano indicante il corretto caricamento del file con
     * nome unico dal client al server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean uploadUniqueFile(InputStream uploadStream) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad effettuare l'upload del file dal client
        // al server solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (uploadStream != null) {

                try {
                    // Effettuo l'effettivo trasferimento del uploadStream
                    // Ho due modi di trasferire il uploadStream
                    // Attraverso il metodo storeFile(fileName, uploadStream)
                    // - specifico il nome del uploadStream sul server
                    // - specifico il uploadStream da trasferire
                    // Attraverso il metodo storeUniqueFile(uploadStream)
                    // - specifico il uploadStream da trasferire
                    // - un nome univoco verrà assegnato automaticamente
                    // al uploadStream sul server
                    // Entrambi i metodi ritornano solo e solamente alla fine
                    // del trasferimento (in buona o cattiva sorte)
                    // Entrambi i metodo forniscono come valore di ritorno
                    // un valore booleano
                    // - true: il trasferimento è andato a buon fine
                    // - false: il trasferimento non è andato a buon fine

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean storeUniqueFile = this.manager.getServer().storeUniqueFile(uploadStream);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo se il trasferimento è andato a buon fine
                    if (storeUniqueFile) {
                        // Operazione riuscita
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return status;
    }

    // Metodo per caricare un file con nome unico
    // dal client al server
    /**
     * @brief Carica un file con nome unico dal client al server
     *
     * Il seguente metodo carica un file con nome unico dal client al server
     *
     * @param uploadStream Il file da caricare dal client al server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public void uploadUniqueFileAM(InputStream uploadStream) {

        // Provo ad effettuare l'upload del file dal client
        // al server solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (uploadStream != null) {
                try {
                    System.out.println("Uploading...");

                    // Effettuo l'effettivo trasferimento del uploadStream
                    // Ho due modi di trasferire il uploadStream
                    // Attraverso il metodo storeFile(fileName, uploadStream)
                    // - specifico il nome del uploadStream sul server
                    // - specifico il uploadStream da trasferire
                    // Attraverso il metodo storeUniqueFile(uploadStream)
                    // - specifico il uploadStream da trasferire
                    // - un nome univoco verrà assegnato automaticamente
                    // al uploadStream sul server
                    // Entrambi i metodi ritornano solo e solamente alla fine
                    // del trasferimento (in buona o cattiva sorte)
                    // Entrambi i metodo forniscono come valore di ritorno
                    // un valore booleano
                    // - true: il trasferimento è andato a buon fine
                    // - false: il trasferimento non è andato a buon fine
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean storeUniqueFile = this.manager.getServer().storeUniqueFile(uploadStream);
                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo se il trasferimento è andato a buon fine
                    if (storeUniqueFile) {
                        System.out.println("Upload was successfull.");
                    } else {
                        System.out.println("Upload wasn't successfull.");
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Upload wasn't successfull.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Cannot upload. Invalid [null] file handler.");
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

    }

    // Metodo per caricare un file dal client al server
    /**
     * @brief Carica un file dal client al server
     *
     * Il seguente metodo carica un file dal client al server
     *
     * @param nameRemoteFile Nome da assegnare al file salvato nella memoria del
     * server
     * @param uploadStream Il file da caricare dal client al server
     *
     * @return Un valore booleano indicante il corretto caricamento del file dal
     * client al server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean uploadFile(String nameRemoteFile, InputStream uploadStream) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad effettuare l'upload del file dal client
        // al server solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (uploadStream != null && nameRemoteFile != null) {

                try {
                    // Effettuo l'effettivo trasferimento del uploadStream
                    // Ho due modi di trasferire il uploadStream
                    // Attraverso il metodo storeFile(fileName, uploadStream)
                    // - specifico il nome del uploadStream sul server
                    // - specifico il uploadStream da trasferire
                    // Attraverso il metodo storeUniqueFile(uploadStream)
                    // - specifico il uploadStream da trasferire
                    // - un nome univoco verrà assegnato automaticamente
                    // al uploadStream sul server
                    // Entrambi i metodi ritornano solo e solamente alla fine
                    // del trasferimento (in buona o cattiva sorte)
                    // Entrambi i metodo forniscono come valore di ritorno
                    // un valore booleano
                    // - true: il trasferimento è andato a buon fine
                    // - false: il trasferimento non è andato a buon fine

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean storeFile = this.manager.getServer().storeFile(nameRemoteFile, uploadStream);
                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo se il trasferimento è andato a buon fine
                    if (storeFile) {
                        // Operazione riuscita
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return status;
    }

    // Metodo per caricare un file dal client al server
    /**
     * @brief Carica un file dal client al server
     *
     * Il seguente metodo carica un file dal client al server
     *
     * @param server Server
     * @param nameRemoteFile Nome da assegnare al file salvato nella memoria del
     * server
     * @param uploadStream Il file da caricare dal client al server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @return Un valore booleano indicante il corretto caricamento del file dal
     * client al server
     */
    static Boolean uploadFile(FTPClient server, String nameRemoteFile, InputStream uploadStream) {
        // Variabile di stato
        Boolean status = false;

        // Controllo validità parametri
        if (uploadStream != null && nameRemoteFile != null) {

            try {
                // Effettuo l'effettivo trasferimento del uploadStream
                // Ho due modi di trasferire il uploadStream
                // Attraverso il metodo storeFile(fileName, uploadStream)
                // - specifico il nome del uploadStream sul server
                // - specifico il uploadStream da trasferire
                // Attraverso il metodo storeUniqueFile(uploadStream)
                // - specifico il uploadStream da trasferire
                // - un nome univoco verrà assegnato automaticamente
                // al uploadStream sul server
                // Entrambi i metodi ritornano solo e solamente alla fine
                // del trasferimento (in buona o cattiva sorte)
                // Entrambi i metodo forniscono come valore di ritorno
                // un valore booleano
                // - true: il trasferimento è andato a buon fine
                // - false: il trasferimento non è andato a buon fine

                boolean storeFile = server.storeFile(nameRemoteFile, uploadStream);

                // Controllo se il trasferimento è andato a buon fine
                if (storeFile) {
                    // Operazione riuscita
                    status = true;
                }

            } catch (IOException ex) {

                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return status;
    }

    // Metodo per caricare un file dal client al server
    /**
     * @brief Carica un file dal client al server
     *
     * Il seguente metodo carica un file dal client al server
     *
     * @param nameRemoteFile Nome da assegnare al file salvato nella memoria del
     * server
     * @param uploadStream Il file da caricare dal client al server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public void uploadFileAM(String nameRemoteFile, InputStream uploadStream) {
        // Provo ad effettuare l'upload del file dal client
        // al server solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (uploadStream != null && nameRemoteFile != null) {
                try {
                    System.out.println("Uploading...");

                    // Effettuo l'effettivo trasferimento del uploadStream
                    // Ho due modi di trasferire il uploadStream
                    // Attraverso il metodo storeFile(fileName, uploadStream)
                    // - specifico il nome del uploadStream sul server
                    // - specifico il uploadStream da trasferire
                    // Attraverso il metodo storeUniqueFile(uploadStream)
                    // - specifico il uploadStream da trasferire
                    // - un nome univoco verrà assegnato automaticamente
                    // al uploadStream sul server
                    // Entrambi i metodi ritornano solo e solamente alla fine
                    // del trasferimento (in buona o cattiva sorte)
                    // Entrambi i metodo forniscono come valore di ritorno
                    // un valore booleano
                    // - true: il trasferimento è andato a buon fine
                    // - false: il trasferimento non è andato a buon fine
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean storeFile = this.manager.getServer().storeFile(nameRemoteFile, uploadStream);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo se il trasferimento è andato a buon fine
                    if (storeFile) {
                        System.out.println("Upload was successfull.");
                    } else {
                        System.out.println("Upload wasn't successfull.");
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Upload wasn't successfull.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Cannot upload. Invalid [null] file handler or/and file name.");
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

    }

    // Metodo per scaricare un file dal server al client
    /**
     * @brief Scarica un file dal server al client
     *
     * Il seguente metodo scarica un file dal server al client
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * scaricare nella memoria locale
     * @param downloadStream File salvato nella memoria locale utilizzato come
     * target per lo scaricamento dei dati
     *
     * @return Un valore booleano indicante il corretto scaricamento di un file
     * dal server al client
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean downloadFile(String nameRemoteFile, OutputStream downloadStream) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad effettuare il download del file dal server
        // al client solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (nameRemoteFile != null && downloadStream != null) {
                try {
                    // Effettuo l'effettivo scaricamento del file nameRemoteFile
                    // Utilizzo il metodo retrieveFile.
                    // Il download consiste in una copia delle informazioni contenute
                    // nel file che si trova nel server in un file che si trova nel client.
                    // Dunque, alla fine, il download è una copia del contenuto del file.
                    // Il metodo richiede la specificazione di 2 parametri:
                    // - nome del file che si trova nel server da scaricare (del quale
                    // copiare il contenuto)
                    // - nome del file che si trova nel client che funge da supporto
                    // per il download (sul quale incollare il contenuto copiato dal file
                    // che si sta "scaricando")

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean retrieveFile = this.manager.getServer().retrieveFile(nameRemoteFile, downloadStream);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo stato download
                    if (retrieveFile) {
                        // Operazione riuscita
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return status;
    }

    // Metodo per scaricare un file dal server al client
    /**
     * @brief Scarica un file dal server al client
     *
     * Il seguente metodo scarica un file dal server al client
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * scaricare nella memoria locale
     * @param downloadStream File salvato nella memoria locale utilizzato come
     * target per lo scaricamento dei dati
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     * @see FTPServer.getAndPrintServerReply()
     */
    public void downloadFileAM(String nameRemoteFile, OutputStream downloadStream) {
        // Provo ad effettuare il download del file dal server
        // al client solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (nameRemoteFile != null && downloadStream != null) {
                try {
                    System.out.println("Downloading...");

                    // Effettuo l'effettivo scaricamento del file nameRemoteFile
                    // Utilizzo il metodo retrieveFile.
                    // Il download consiste in una copia delle informazioni contenute
                    // nel file che si trova nel server in un file che si trova nel client.
                    // Dunque, alla fine, il download è una copia del contenuto del file.
                    // Il metodo richiede la specificazione di 2 parametri:
                    // - nome del file che si trova nel server da scaricare (del quale
                    // copiare il contenuto)
                    // - nome del file che si trova nel client che funge da supporto
                    // per il download (sul quale incollare il contenuto copiato dal file
                    // che si sta "scaricando")
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();
                    boolean retrieveFile = this.manager.getServer().retrieveFile(nameRemoteFile, downloadStream);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo stato download
                    if (retrieveFile) {
                        System.out.println("Download was successfull.");
                    } else {
                        // Analizzo e trovo la causa dell'errore di download
                        Boolean found = false;
                        String[] listElements = this.getListElements();
                        for (var each : listElements) {
                            if (each.equals(nameRemoteFile)) {
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            // Se l'elemento esiste nel server ma non è stato scaricato,
                            // allora quell'elemento non può essere scaricato perchè
                            // non è un file
                            System.out.println("The element to be downloaded is not a file. Download won't continue.");

                        } else {
                            // File non trovato
                            System.out.println("File not found. Cannot download.");
                        }

                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Download wasn't successfull.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                System.out.println("Cannot download. Invalid [null] file handler or/and file name.");
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }

    }

    // Metodo per automatizzare le operazioni necessarie
    // ad effettuare l'update di un file nel server
    /**
     * @brief Aggiorna un file nel server dal client al server
     *
     * Il seguente metodo effettua un aggiornamento di un file già presente
     * nella memoria del server con le informazioni di un file locale presente
     * nella memoria del client
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * aggiornare
     * @param uploadStream Il file locale con le quali informazioni aggiornare
     * il file salvato nella memoria del server
     * @param downloadStream File salvato nella memoria locale utilizzato come
     * target per lo scaricamento dei dati
     * @param localWriteStream Stream per effettuare la scrittura di un file in
     * locale
     * @param toAppend Informazioni da scrivere nel file
     *
     * @return Un valore booleano indicante la corretta esecuzione
     * dell'aggiornamento del file nel server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.downloadFile()
     * @see FTPServer.uploadFile()
     */
    public Boolean updateFileAppending(String nameRemoteFile, InputStream uploadStream, OutputStream downloadStream,
            PrintWriter localWriteStream, String toAppend) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad effettuare il download del file dal server
        // al client solo e solamente se sono già loggato
        if (this.isLoggedIn) {

            // Controllo validità parametri
            if (nameRemoteFile != null && uploadStream != null && downloadStream != null
                    && localWriteStream != null && toAppend != null) {

                // Scarico il file. Continuo solo se l'operazione
                // di download è andata a buon fine
                if (this.downloadFile(nameRemoteFile, downloadStream)) {
                    // Aggiorno il file localmente effettuando un'operazione
                    // di append
                    try (localWriteStream) {
                        localWriteStream.println(toAppend);
                    }
                    // Effettuo l'upload del file nel server
                    if (this.uploadFile(nameRemoteFile, uploadStream)) {
                        // Operazione riuscita
                        status = true;
                    }
                }
            }
        }

        return status;
    }

    // Metodo per automatizzare le operazioni necessarie
    // ad effettuare l'update di un file nel server
    /**
     * @brief Aggiorna un file nel server dal client al server
     *
     * Il seguente metodo effettua un aggiornamento di un file già presente
     * nella memoria del server con le informazioni di un file locale presente
     * nella memoria del client
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * aggiornare
     * @param uploadStream Il file locale con le quali informazioni aggiornare
     * il file salvato nella memoria del server
     * @param downloadStream File salvato nella memoria locale utilizzato come
     * target per lo scaricamento dei dati
     * @param localWriteStream Stream per effettuare la scrittura di un file in
     * locale
     * @param toAppend Informazioni da scrivere nel file
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.downloadFile()
     * @see FTPServer.uploadFile()
     */
    public void updateFileAppendingAM(String nameRemoteFile, InputStream uploadStream, OutputStream downloadStream,
            PrintWriter localWriteStream, String toAppend) {
        // Provo ad effettuare il download del file dal server
        // al client solo e solamente se sono già loggato
        if (this.isLoggedIn) {

            // Controllo validità parametri
            if (nameRemoteFile != null && uploadStream != null && downloadStream != null
                    && localWriteStream != null && toAppend != null) {

                System.out.println("Updating...");

                // Scarico il file. Continuo solo se l'operazione
                // di download è andata a buon fine
                if (this.downloadFile(nameRemoteFile, downloadStream)) {
                    // Aggiorno il file localmente effettuando un'operazione
                    // di append
                    try (localWriteStream) {
                        localWriteStream.println(toAppend);
                    }
                    // Effettuo l'upload del file nel server
                    if (this.uploadFile(nameRemoteFile, uploadStream)) {
                        System.out.println("Update was successfull.");
                    } else {
                        System.out.println("Upload wasn't successfull.");
                    }
                } else {
                    System.out.println("Download wasn't successfull.");
                }
            } else {
                System.out.println("Cannot update. Invalid [null] file handler/s, remote file name or/and"
                        + "appending information.");
            }
        }
    }

    // Metodo per tentare di effettuare il logout
    // dal server
    /**
     * @brief Tenta di effettuare il logout dal server
     *
     * Il seguente metodo tenta di effettuare il logout dal server
     *
     * @return Un valore booleano indicante la corretta esecuzione del tentativo
     * di effettuare il logout dal server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.manager
     * @see FTPServer.isLoggedIn()
     */
    public Boolean logout() {
        // Variabile di stato
        Boolean status = false;

        // Procedo con le operazioni di logout
        // solo se supportate dal server secondo
        // l'indicazione del flag booleano isLogBidirectional
        if (this.manager.getIsLogBidirectional()) {
            // Effettuo il logout solo e solamente
            // se sono già loggato
            if (this.isLoggedIn) {
                try {
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();

                    // Effettuo il logout
                    boolean logout = this.manager.getServer().logout();

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo stato operazione
                    if (logout) {
                        // Imposto il flag booleano isLoggedIn
                        this.isLoggedIn = false;

                        // Operazione riuscita
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return status;
    }

    // Metodo per tentare di effettuare il logout
    // dal server
    /**
     * @brief Tenta di effettuare il logout dal server
     *
     * Il seguente metodo tenta di effettuare il logout dal server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.manager
     * @see FTPServer.isLoggedIn()
     */
    public void logoutAM() {
        // Procedo con le operazioni di logout
        // solo se supportate dal server secondo
        // l'indicazione del flag booleano isLogBidirectional
        if (this.manager.getIsLogBidirectional()) {
            // Effettuo il logout solo e solamente
            // se sono già loggato
            if (this.isLoggedIn) {
                try {
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();

                    System.out.println("Logging out...");

                    // Effettuo il logout
                    boolean logout = this.manager.getServer().logout();

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    // Controllo stato operazione
                    if (logout) {
                        // Imposto il flag booleano isLoggedIn
                        this.isLoggedIn = false;

                        System.out.println("Logged out.");

                    } else {
                        System.out.println("Couldn't log out.");
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    System.out.println("Couldn't log out.");
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Already logged out.");
            }
        } else {
            System.out.println("Bidirectional log not supported. To logout disconnect from the server using "
                    + "disconnect()/disconnectAM().");
        }
    }

    // Disconnessione
    /**
     * @brief Tenta di effettuare la disconnesione dal server
     *
     * Il seguente metodo tenta di effattuare la disconnesione dal server
     *
     * @return Un valore booleano indicante la corretta esecuzione del tentativo
     * di disconnessione dal server
     *
     * @pre La connessione con il server deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.isLoggedIn
     * @see FTPServer.getAndPrintServerReply()
     */
    public Boolean disconnect() {

        // Variabile di stato
        Boolean status = false;

        // Effettuo la disconnessione solo e solamente
        // se sono già connesso
        if (this.manager.getIsConnected()) {
            try {
                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();

                // Effettuo la disconnessione
                this.manager.getServer().disconnect();

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Imposto il flag booleano isConnected
                this.manager.setIsConnected(false);

                // Imposto il flag booleano isLoggedIn
                this.isLoggedIn = false;

                // Operazione riuscita
                status = true;

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return status;
    }

    // Disconnessione
    /**
     * @brief Tenta di effettuare la disconnesione dal server
     *
     * Il seguente metodo tenta di effattuare la disconnesione dal server
     *
     * @pre La connessione con il server deve essere attiva
     *
     * @see FTPServer.manager
     * @see FTPServer.isLoggedIn
     * @see FTPServer.getAndPrintServerReply()
     */
    public void disconnectAM() {
        // Effettuo la disconnessione solo e solamente
        // se sono già connesso
        if (this.manager.getIsConnected()) {

            try {
                System.out.println("Disconnecting...");

                // Acquisisco il permesso di uso del server
                this.manager.getServerMutex().acquire();

                // Effettuo la disconnessione
                this.manager.getServer().disconnect();

                System.out.println("Disconnected.");

                // Visualizzo eventuali risposte del server
                this.getAndPrintServerReply();

                // Imposto il flag booleano isConnected
                this.manager.setIsConnected(false);

                // Imposto il flag booleano isLoggedIn
                this.isLoggedIn = false;

                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
            } catch (IOException ex) {
                // Rilascio il permesso di uso del server
                this.manager.getServerMutex().release();
                System.out.println("Couldn't disconnect.");
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Already disconnected.");
        }

    }

    // Metodo per eliminare il file
    /**
     * @brief Elimina un file dal server
     *
     * Il seguente metodo effettua l'eliminazione di un file dalla memoria del
     * server
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * eliminare
     *
     * @return Un valore booleano indicante la corretta esecuzione
     * dell'eliminazione del file dalla memoria del server
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     */
    public Boolean deleteFile(String nameRemoteFile) {
        // Variabile di stato
        Boolean status = false;

        // Provo ad eliminare il file specificato 
        // solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (nameRemoteFile != null) {
                try {
                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();

                    // Provo ad eliminare il file
                    boolean deleteFile = this.manager.getServer().deleteFile(nameRemoteFile);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    if (deleteFile) {
                        status = true;
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return status;
    }

    // Metodo per eliminare il file
    /**
     * @brief Elimina un file dal server
     *
     * Il seguente metodo effettua l'eliminazione di un file dalla memoria del
     * server
     *
     * @param nameRemoteFile Nome del file salvato nella memoria del server da
     * eliminare
     *
     * @pre Il login sul server deve essere già stato eseguito
     *
     * @see FTPServer.isLoggedIn
     * @see FTPServer.manager
     */
    public void deleteFileAM(String nameRemoteFile) {
        // Provo ad eliminare il file specificato 
        // solo e solamente se sono già loggato
        if (this.isLoggedIn) {
            // Controllo validità parametri
            if (nameRemoteFile != null) {
                try {
                    System.out.println("Deleting...");

                    // Acquisisco il permesso di uso del server
                    this.manager.getServerMutex().acquire();

                    // Provo ad eliminare il file
                    boolean deleteFile = this.manager.getServer().deleteFile(nameRemoteFile);

                    // Visualizzo eventuali risposte del server
                    this.getAndPrintServerReply();

                    if (deleteFile) {
                        System.out.println("Delete was successfull.");
                    } else {
                        System.out.println("Delete wasn't successfull.");
                    }

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                } catch (IOException ex) {
                    System.out.println("Delete wasn't successfull.");

                    // Rilascio il permesso di uso del server
                    this.manager.getServerMutex().release();
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Cannot delete. Invalid [null] file name.");
            }
        } else {
            System.out.println("Not logged in. Please log in first.");
        }
    }

    // Metodo per visualizzare gli eventuali messaggi
    // di risposta del server
    /**
     * @brief Visualizza su console le eventuali risposte del server a comandi
     *
     * Il seguente metodo visualizza su console le eventuali risposte del server
     * dopo lo svolgimento di azioni con esso. E' consigliabile richiamare
     * questo metodo dopo ogni comando inviato al server, in quanto eventuali
     * messaggi generati da un comando sovrascrivono quelli precedenti
     *
     * @see FTPServer.manager
     * @see FTPServer.backupReplies
     */
    private void getAndPrintServerReply() {
        if (this.manager.getShowServerReplies()) {
            // Utilizzo del metodo getReplyStrings per ottenere
            // gli eventuali messaggi di risposta del server
            // Il metodo ritorna un'array di stringhe.
            // Dunque, i messaggi del server vengono "accumulati"
            // in una lista che viene interamente restituita alla chiamata
            // del metodo in questione.
            // La lista non viene svuotata alla chiamata del metodo 
            // in questione.
            // La lista viene sovrascritta alla chiamata di qualsiasi altro
            // metodo che produca risposte da parte del server: per questo motivo,
            // è consigliato richiamare il metodo getAndPrintServerReply dopo
            // ogni chiamata di qualsiasi metodo di interazione con il server.
            String[] replies = this.manager.getServer().getReplyStrings();
            if (!Arrays.equals(this.backupReplies, replies)) {
                for (var each : replies) {
                    System.out.println(each);
                }
            }

            this.backupReplies = replies;
        }
    }

    /**
     * @brief Ritorna il valore dell'attributo FTPServer.host
     *
     * @return Il valore dell'attributo FTPServer.host
     *
     * @see FTPServer.host
     */
    public String getHost() {
        return host;
    }

    /**
     * @brief Ritorna il valore dell'attributo FTPServer.port
     *
     * @return Il valore dell'attributo FTPServer.port
     *
     * @see FTPServer.port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @brief Ritorna il valore dell'attributo FTPServer.isLoggedIn
     *
     * @return Il valore dell'attributo FTPServer.isLoggedIn
     *
     * @see FTPServer.isLoggedIn
     */
    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    // Ritorna il valore dell'attributo isConnected
    /**
     * @brief Ritorna il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getIsConnected()
     *
     * Il seguente metodo ritorna il valore ottenuto attraverso l'invocazione
     * del metodo resources.ftpserver.Manager.getIsConnected()
     *
     * @return Il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getIsConnected()
     *
     * @see resources.ftpserver.Manager
     * @see FTPServer.manager
     */
    public Boolean getIsConnected() {
        return this.manager.getIsConnected();
    }

    // Ritorna il valore dell'attributo isNoOp
    /**
     * @brief Ritorna il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getIsNoOp()
     *
     * Il seguente metodo ritorna il valore ottenuto attraverso l'invocazione
     * del metodo resources.ftpserver.Manager.getIsNoOp()
     *
     * @return Il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getIsNoOp()
     *
     * @see resources.ftpserver.Manager
     * @see FTPServer.manager
     */
    public Boolean getIsNoOp() {
        return this.manager.getIsNoOp();
    }

    // Ritorna il valore dell'attributo getShowServerReplies
    /**
     * @brief Ritorna il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getShowServerReplies()
     *
     * Il seguente metodo ritorna il valore ottenuto attraverso l'invocazione
     * del metodo resources.ftpserver.Manager.getShowServerReplies()
     *
     * @return Il valore ottenuto attraverso l'invocazione del metodo
     * resources.ftpserver.Manager.getShowServerReplies()
     *
     * @see resources.ftpserver.Manager
     * @see FTPServer.manager
     */
    public Boolean getShowServerReplies() {
        return this.manager.getShowServerReplies();
    }

    // Backdoor
    public FTPClient getServerBackdoor() {
        return this.manager.getServer();
    }
}
