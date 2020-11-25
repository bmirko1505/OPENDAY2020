package guiGelataio;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file InterfacciaPrincipaleGelataio.java
 *
 * @brief Classe per la gestione della gui dell'interfaccia principale del
 * barista
 *
 * @mainpage InterfacciaPrincipaleBarista di damimmo
 * @section Introduzione Introduzione
 *
 * Questa è la documentazione del codice sorgente InterfacciaPrincipaleBarista
 * di damimmo. Questa è l'interfaccia principale per il batista, alla quale esso
 * farà riferimento in ogni momento per controllare le ordinazioni in arrivo e,
 * successivamente evaderle. Inoltre potrà modificare varie impostazioni di
 * sistema.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import resources.compositionlistapredefinitagusti.CompositionListaPredefinitaGusti;
import resources.compositionlistapredefinitagusti.xml.generatorxml.GeneratorXMLCompositionListaPredefinitaGusti;
import resources.compositionlistapredefinitagusti.xml.parserxml.ParserXMLCompositionListaPredefinitaGusti;
import resources.cronology.CronologyUpdater;
import resources.currenttime.CurrentTime;
import resources.ftpserver.FTPServer;
import resources.listtextwrapper.ListTextWrapper;
import resources.orariodelnegozio.OrarioDelNegozio;
import resources.orariodelnegozio.xml.generatorxml.GeneratorXMLModificaOrarioDelNegozio;
import resources.orariodelnegozio.xml.parserxml.ParserXMLModificaOrarioDelNegozio;
import resources.ordine.Ordine;
import resources.ordine.OrdineUpdater;
import resources.ordine.xml.generatorxml.GeneratorXMLOrdine;
import resources.ordine.xml.generatorxmlv2.GeneratorXMLOrdinev2;
import resources.ordine.xml.generatorxmlv3.GeneratorXMLOrdinev3;
import resources.ordine.xml.parserxml.ParserXMLOrdine;
import resources.prezzovaschette.xml.generatorxml.GeneratorXMLModificaPrezzoVaschette;
import resources.prezzovaschette.xml.parserxml.ParserXMLModificaPrezzoVaschette;
import resources.prezzovaschette.PrezzoVaschette;

/**
 *
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class InterfacciaPrincipaleGalataio
 *
 * @brief Classe per la gestione della gui dell'interfaccia principale del
 * barista
 *
 * La seguente classe fornisce tutti i metodi necessari alla realizzazione di
 * un'interfaccia grafica responsive per il barista.
 */
public class InterfacciaPrincipaleGalataio extends javax.swing.JFrame {

    // Attributi
    /**
     * Rappresenta la path di locazione dell'immagine utilizzata come logo della
     * gelateria
     *
     * @see InterfacciaPrincipaleGalataio.logo
     */
    private final String pathLogo;

    /**
     * Rappresenta l'oggetto di classe ImageIcon utilizzato per inserire il logo
     * all'interno di una label per permetterne la corretta visualizzazione
     * sull'interfaccia
     *
     * @see InterfacciaPrincipaleGalataio.pathLogo
     */
    private final ImageIcon logo;

    /**
     * Rappresenta il modello lista della lista
     * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti
     *
     * @see InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti
     */
    private final DefaultListModel listaPredefinitaGustiModel;

    /**
     * Rappresenta il modello lista della lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     */
    private final DefaultListModel listaGustiVenditaModel;

    /**
     * Rappresenta il modello lista backup della lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     */
    private final DefaultListModel listaGustiVenditaModelBackup;

    /**
     * Rappresenta l'oggetto di classe CurrentTime necessario per visualizzare
     * correttamente l'orario corrente sull'interfaccia
     *
     * @see InterfacciaPrincipaleGalataio.timerCurrentTime
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente1
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente2
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente3
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente4
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente5
     * @see resources.currenttime.CurrentTime
     */
    private final CurrentTime currentTime;

    /**
     * Rappresenta l'oggetto di classe Timer necessario per l'aggiornamento
     * continuo in background dell'ora corrente, per mantenerla "corrente"
     *
     * @see InterfacciaPrincipaleGalataio.currentTime
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente1
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente2
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente3
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente4
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente5
     */
    private final Timer timerCurrentTime;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaPiccola
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaPiccola
     */
    private final SpinnerNumberModel spinnerVaschettaPiccolaModel;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaMedia
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaMedia
     */
    private final SpinnerNumberModel spinnerVaschettaMediaModel;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaGrande
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaGrande
     */
    private final SpinnerNumberModel spinnerVaschettaGrandeModel;

    /**
     * Rappresenta lo spinner di backup dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaPiccola
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaPiccola
     */
    private final JSpinner jSpinnerVaschettaPiccolaBackup;

    /**
     * Rappresenta lo spinner di backup dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaMedia
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaMedia
     */
    private final JSpinner jSpinnerVaschettaMediaBackup;

    /**
     * Rappresenta lo spinner di backup dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerVaschettaGrande
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaGrande
     */
    private final JSpinner jSpinnerVaschettaGrandeBackup;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiApertura
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiApertura
     */
    private final SpinnerDateModel spinnerOraDiAperturaModel;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusura
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusura
     */
    private final SpinnerDateModel spinnerOraDiChiusuraModel;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiAperturaBackup
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiAperturaBackup
     */
    private final SpinnerDateModel spinnerOraDiAperturaBackupBlankModel;

    /**
     * Rappresenta il modello spinner dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusuraBackup
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusuraBackup
     */
    private final SpinnerDateModel spinnerOraDiChiusuraBackupBlankModel;

    /**
     * Rappresenta lo spinner di backup dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiApertura
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiApertura
     */
    private final JSpinner jSpinnerOraDiAperturaBackup;

    /**
     * Rappresenta lo spinner di backup dello spinner
     * InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusura
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusura
     */
    private final JSpinner jSpinnerOraDiChiusuraBackup;

    /**
     * Rappresenta il vero e proprio orario aggiornato in modo automatico
     * ("corrente")
     *
     * @see InterfacciaPrincipaleGalataio.currentTime
     * @see InterfacciaPrincipaleGalataio.timerCurrentTime
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente1
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente2
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente3
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente4
     * @see InterfacciaPrincipaleGalataio.jLabelOraCorrente5
     */
    private final Date currentDateAndTime;

    /**
     * Rappresenta il parser del file XML contenente la struttura
     * "OrarioDelNegozio"
     *
     * @see InterfacciaPrincipaleGelataio.spinnerOraDiAperturaModel
     * @see InterfacciaPrincipaleGelataio.spinnerOraDiChiusuraModel
     * @see resources.orariodelnegozio.OrarioDelNegozio
     * @see
     * resources.orariodelnegozio.xml.parserxml.ParserXMLModificaOrarioDelNegozio
     */
    private final ParserXMLModificaOrarioDelNegozio parserXMLModificaOrarioDelNegozio;

    /**
     * Rappresenta il generator del file XML contenente la struttura
     * "OrarioDelNegozio"
     *
     * @see InterfacciaPrincipaleGelataio.spinnerOraDiAperturaModel
     * @see InterfacciaPrincipaleGelataio.spinnerOraDiChiusuraModel
     * @see resources.orariodelnegozio.OrarioDelNegozio
     * @see
     * resources.orariodelnegozio.xml.generatorxml.GeneratorXMLModificaOrarioDelNegozio
     */
    private final GeneratorXMLModificaOrarioDelNegozio generatorXMLModificaOrarioDelNegozio;

    /**
     * Rappresenta il generator del file XML contenente la struttura
     * "PrezzoVaschette"
     *
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaPiccolaModel
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaMediaModel
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaGrandeModel
     * @see resources.prezzovaschette.PrezzoVaschette
     * @see
     * resources.prezzovaschette.xml.generatorxml.GeneratorXMLModificaPrezzoVaschette
     */
    private final GeneratorXMLModificaPrezzoVaschette generatorXMLModificaPrezzoVaschette;

    /**
     * Rappresenta il parser del file XML contenente la struttura
     * "PrezzoVaschette"
     *
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaPiccolaModel
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaMediaModel
     * @see InterfacciaPrincipaleGelataio.spinnerVaschettaGrandeModel
     * @see resources.prezzovaschette.PrezzoVaschette
     * @see
     * resources.prezzovaschette.xml.parserxml.ParserXMLModificaPrezzoVaschette
     */
    private final ParserXMLModificaPrezzoVaschette parserXMLModificaPrezzoVaschette;

    /**
     * Rappresenta il parser del file XML contenente la struttura "Ordine"
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.Ordine
     * @see resources.ordine.xml.parserxml.ParserXMLOrdine
     */
    private final ParserXMLOrdine parserXMLOrdine;

    /**
     * Rappresenta il generator del file XML contenente la struttura "Ordine"
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.Ordine
     * @see resources.ordine.xml.generatorxml.GeneratorXMLOrdine
     */
    private final GeneratorXMLOrdine generatorXMLOrdine;

    /**
     * Rappresenta il parser del file XML contenente la struttura
     * "CompositionListaPredefinitaGusti"
     *
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     * @see
     * resources.compositionlistapredefinitagusti.CompositionListaPredefinitaGusti
     * @see
     * resources.compositionlistapredefinitagusti.xml.parserxml.ParserXMLCompositionListaPredefinitaGusti
     */
    private final ParserXMLCompositionListaPredefinitaGusti parserXMLCompositionListaPredefinitaGusti;

    /**
     * Rappresenta il generator del file XML contenente la struttura
     * "CompositionListaPredefinitaGusti"
     *
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     * @see
     * resources.compositionlistapredefinitagusti.CompositionListaPredefinitaGusti
     * @see
     * resources.compositionlistapredefinitagusti.xml.generatorxml.GeneratorXMLCompositionListaPredefinitaGusti
     */
    private final GeneratorXMLCompositionListaPredefinitaGusti generatorXMLCompositionListaPredefinitaGusti;

    /**
     * Rappresenta il modello lista della lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     */
    private final DefaultListModel listaOrdiniModel;

    /**
     * Rappresenta l'oggetto di classe OrdineUpdater necessario per
     * l'aggiornamento continuo in background degli ordini, automaticamente
     * reperiti dal server
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.OrdineUpdater
     */
    private final OrdineUpdater ordineUpdater;

    /**
     * Rappresenta l'oggetto di classe Timer necessario per l'aggiornamento
     * continuo in background degli ordini, automaticamente reperiti dal server
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.OrdineUpdater
     */
    private final Timer timerOrdineUpdater;

    /**
     * Rappresenta l'oggetto di classe ListTextWrapper per consentire una
     * visualizzazione più ordinata degli ordini nella lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.listtextwrapper.ListTextWrapper
     */
    private final ListTextWrapper listTextWrapper;

    /**
     * Rappresenta l'oggetto di classe FTPServer per permettere la comunicazione
     * bidirezionale con il server FTP
     *
     * @see resources.ftpserver.FTPServer
     */
    private final FTPServer server;

    /**
     * Rappresenta il generator v2 del file XML contenente la struttura "Ordine"
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.Ordine
     * @see resources.ordine.xml.generatorxmlv2.GeneratorXMLOrdinev2
     */
    private final GeneratorXMLOrdinev2 generatorXMLOrdinev2;

    /**
     * Rappresenta il generator v3 del file XML contenente la struttura "Ordine"
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.Ordine
     * @see resources.ordine.xml.generatorxmlv3.GeneratorXMLOrdinev3
     */
    private GeneratorXMLOrdinev3 generatorXMLOrdinev3;

    /**
     * Rappresenta la lista per la gestione degli ordini non evasi
     *
     * @see InterfacciaPrincipaleGalataio.InterfacciaPrincipaleGalataio()
     * @see InterfacciaPrincipaleGalataio.loadFromServer()
     */
    private ArrayList<Ordine> resultOrdiniNonEvasi;

    /**
     * Rappresenta una lista utilizzata a scopo di rollback di modifiche da
     * annullare
     *
     * @see InterfacciaPrincipaleGalataio.jButtonIndietro1ActionPerformed()
     * @see InterfacciaPrincipaleGalataio.jButtonRimuoviActionPerformed()
     * @see
     * InterfacciaPrincipaleGalataio.jButtonModificaListaPredefinitaGustiActionPerformed()
     */
    private final ArrayList<CompositionListaPredefinitaGusti> flusher;

    /**
     * Rappresenta l'oggetto di classe CronologyUpdater necessario per
     * l'aggiornamento continuo del valore del conteggio di frammenti
     * cronologici degli ordini evasi in background
     *
     * @see resources.cronology.CronologyUpdater
     */
    private final CronologyUpdater cronologyUpdater;

    /**
     * Rappresenta l'oggetto di classe Timer necessario per l'aggiornamento
     * continuo del valore del conteggio di frammenti cronologici degli ordini
     * evasi in background
     *
     * @see InterfacciaPrincipaleGalataio.cronologyUpdater
     */
    private final Timer timerCronologyUpdater;
    // Fine dichiarazione attributi

    /**
     * Creates new form InterfacciaPrincipaleGalataio
     *
     */
    // Costruttore
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @see InterfacciaPrincipaleGalataio.pathLogo
     * @see InterfacciaPrincipaleGalataio.logo
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModel
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModelBackup
     * @see InterfacciaPrincipaleGalataio.currentTime
     * @see InterfacciaPrincipaleGalataio.timerCurrentTime
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaPiccolaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaMediaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaGrandeModel
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaPiccolaBackup
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaMediaBackup
     * @see InterfacciaPrincipaleGalataio.jSpinnerVaschettaGrandeBackup
     * @see InterfacciaPrincipaleGalataio.currentDateAndTime
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiAperturaModel
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiChiusuraModel
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiAperturaBackupBlankModel
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiChiusuraBackupBlankModel
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiAperturaBackup
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusuraBackup
     * @see InterfacciaPrincipaleGalataio.parserXMLModificaOrarioDelNegozio
     * @see InterfacciaPrincipaleGalataio.generatorXMLModificaOrarioDelNegozio
     * @see InterfacciaPrincipaleGalataio.parserXMLModificaPrezzoVaschette
     * @see InterfacciaPrincipaleGalataio.generatorXMLModificaPrezzoVaschette
     * @see InterfacciaPrincipaleGalataio.parserXMLOrdine
     * @see InterfacciaPrincipaleGalataio.generatorXMLOrdine
     * @see
     * InterfacciaPrincipaleGalataio.parserXMLCompositionListaPredefinitaGusti
     * @see
     * InterfacciaPrincipaleGalataio.generatorXMLCompositionListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.listaOrdiniModel
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.ordineUpdater
     * @see InterfacciaPrincipaleGalataio.timerOrdineUpdater
     * @see InterfacciaPrincipaleGalataio.listTextWrapper
     * @see InterfacciaPrincipaleGalataio.generatorXMLOrdinev2
     * @see InterfacciaPrincipaleGalataio.generatorXMLOrdinev3
     * @see InterfacciaPrincipaleGalataio.ordineUpdaterv2
     * @see InterfacciaPrincipaleGalataio.initComponents()
     * @see InterfacciaPrincipaleGalataio.stableState()
     * @see InterfacciaPrincipaleGalataio.cronologyUpdater
     * @see InterfacciaPrincipaleGalataio.timerCronologyUpdater
     *
     */
    public InterfacciaPrincipaleGalataio() {
        initComponents();

        System.out.println("Avvio del software...");

        // Inizializzazione manuale
        this.pathLogo = "./data/logo/logo.png";
        this.logo = new ImageIcon(this.pathLogo);
        this.listaPredefinitaGustiModel = new DefaultListModel();
        this.listaGustiVenditaModel = new DefaultListModel();
        this.listaGustiVenditaModelBackup = new DefaultListModel();
        this.currentTime = new CurrentTime(jLabelOraCorrente, jLabelOraCorrente5, jLabelOraCorrente1, jLabelOraCorrente2, jLabelOraCorrente3);
        this.timerCurrentTime = new Timer(true);
        this.spinnerVaschettaPiccolaModel = new SpinnerNumberModel();
        this.spinnerVaschettaMediaModel = new SpinnerNumberModel();
        this.spinnerVaschettaGrandeModel = new SpinnerNumberModel();
        this.jSpinnerVaschettaPiccolaBackup = new JSpinner();
        this.jSpinnerVaschettaMediaBackup = new JSpinner();
        this.jSpinnerVaschettaGrandeBackup = new JSpinner();
        this.currentDateAndTime = new Date();
        this.spinnerOraDiAperturaModel = new SpinnerDateModel(this.currentDateAndTime, null, null, Calendar.HOUR_OF_DAY);
        this.spinnerOraDiChiusuraModel = new SpinnerDateModel(this.currentDateAndTime, null, null, Calendar.HOUR_OF_DAY);
        this.spinnerOraDiAperturaBackupBlankModel = new SpinnerDateModel(this.currentDateAndTime, null, null, Calendar.HOUR_OF_DAY);
        this.spinnerOraDiChiusuraBackupBlankModel = new SpinnerDateModel(this.currentDateAndTime, null, null, Calendar.HOUR_OF_DAY);
        this.jSpinnerOraDiAperturaBackup = new JSpinner();
        this.jSpinnerOraDiChiusuraBackup = new JSpinner();
        this.parserXMLModificaOrarioDelNegozio = new ParserXMLModificaOrarioDelNegozio();
        this.generatorXMLModificaOrarioDelNegozio = new GeneratorXMLModificaOrarioDelNegozio();
        this.parserXMLModificaPrezzoVaschette = new ParserXMLModificaPrezzoVaschette();
        this.generatorXMLModificaPrezzoVaschette = new GeneratorXMLModificaPrezzoVaschette();
        this.parserXMLOrdine = new ParserXMLOrdine(this.jListListaPredefinitaGusti);
        this.generatorXMLOrdine = new GeneratorXMLOrdine();
        this.parserXMLCompositionListaPredefinitaGusti = new ParserXMLCompositionListaPredefinitaGusti();
        this.generatorXMLCompositionListaPredefinitaGusti = new GeneratorXMLCompositionListaPredefinitaGusti();
        this.listaOrdiniModel = new DefaultListModel();
        this.server = new FTPServer(false, false, false);
        this.ordineUpdater = new OrdineUpdater(this.server, this.parserXMLOrdine, this.listaOrdiniModel, this.generatorXMLOrdine);
        this.timerOrdineUpdater = new Timer(true);
        this.listTextWrapper = new ListTextWrapper(420);
        this.generatorXMLOrdinev2 = new GeneratorXMLOrdinev2();
        try {
            this.generatorXMLOrdinev3 = new GeneratorXMLOrdinev3();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            this.generatorXMLOrdinev3 = null;
            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.resultOrdiniNonEvasi = null;
        this.flusher = new ArrayList<>();
        this.cronologyUpdater = new CronologyUpdater(this.server, this.jLabelFrammentiCronologici);
        this.timerCronologyUpdater = new Timer(true);

        this.stableState();
    }

    /**
     * @brief Inizializzatore stabile
     *
     * Stabilisce una connessione con il server FTP e gestisce eventuali errori
     * di connessione. Recupera informazioni necessarie ad inizializzare le
     * necessarie risorse per il corretto funzionamento del software dal server
     * e gestisce eventuali errori di recupero delle informazioni. Assegna
     * valori stabili alle risorse che necessitano di un valore forzato per il
     * corretto funzionamento.
     *
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.timerCurrentTime
     * @see InterfacciaPrincipaleGalataio.timerOrdineUpdater
     * @see InterfacciaPrincipaleGalataio.jLabelLogo
     * @see InterfacciaPrincipaleGalataio.jLabelLogo1
     * @see InterfacciaPrincipaleGalataio.jLabelLogo2
     * @see InterfacciaPrincipaleGalataio.jLabelLogo3
     * @see InterfacciaPrincipaleGalataio.jLabelLogo4
     * @see InterfacciaPrincipaleGalataio.jLabelLogo5
     * @see InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see InterfacciaPrincipaleGalataio.jListOrdini
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaPiccolaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaMediaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaGrandeModel
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     * @see InterfacciaPrincipaleGalataio.spinnerValueCopy()
     * @see InterfacciaPrincipaleGalataio.loadFromServer()
     * @see InterfacciaPrincipaleGalataio.cronologyUpdater
     * @see InterfacciaPrincipaleGalataio.timerCronologyUpdater
     */
    private void stableState() {
        // Impostazione dei parametri del server e tentativo
        // di connessione
        System.out.println("Tentativo di connessione al server...");
        Boolean[] serverState = new Boolean[2];
        this.server.setCoords("ftp.damimmo.altervista.org", 21);
        serverState[0] = this.server.connect();
        serverState[1] = this.server.login("damimmo", "KBwCx3G7PT7D");

        if (!serverState[0] || !serverState[1]) {
            System.out.println("Tentativo di connessione fallito.");

            JOptionPane.showMessageDialog(null, "Errore di connessione al server FTP. Impossibile avviare il software."
                    + " Riprovare in seguito.",
                    "Errore connessione server", JOptionPane.ERROR_MESSAGE);

            System.exit(-1);
        } else {
            System.out.println("Connessione stabilita.");
            System.out.println("Servizio keep alive avviato.");
        }

        // Collego i modelli delle liste alle rispettive liste
        this.jListListaPredefinitaGusti.setModel(this.listaPredefinitaGustiModel);
        this.jListListaPredefinitaGusti1.setModel(this.listaPredefinitaGustiModel);
        this.jListListaGustiVendita.setModel(this.listaGustiVenditaModel);
        this.jListOrdini.setModel(this.listaOrdiniModel);
        this.jListOrdini.setCellRenderer(this.listTextWrapper);

        // Caricamenti dal server
        // Questa DEVE ESSERE la prima operazione ad essere eseguita
        // per garantire la consistenza dei dati trattati nelle operazioni
        // successive sempre nel contesto di questo metodo
        this.loadFromServer();

        System.out.println("Servizio ora corrente avviato.");
        this.timerCurrentTime.schedule(this.currentTime, 0, 2000);

        System.out.println("Servizio ordine updater avviato.");
        this.timerOrdineUpdater.schedule(this.ordineUpdater, 5000, 5000);

        System.out.println("Servizio cronology updater avviato.");
        this.timerCronologyUpdater.schedule(this.cronologyUpdater, 5000, 5000);

        // Visualizzazione del logo in tutti i pannelli
        this.jLabelLogo.setIcon(logo);
        this.jLabelLogo1.setIcon(logo);
        this.jLabelLogo2.setIcon(logo);
        this.jLabelLogo3.setIcon(logo);
        this.jLabelLogo5.setIcon(logo);

        // Imposto le variabili spinnerVaschettaPiccolaModel, spinnerVaschettaMediaModel,
        // spinnerVaschettaGrandeModel
        this.spinnerVaschettaPiccolaModel.setMinimum(0f);
        this.spinnerVaschettaPiccolaModel.setStepSize(0.50f);
        this.spinnerVaschettaMediaModel.setMinimum(0f);
        this.spinnerVaschettaMediaModel.setStepSize(0.50f);
        this.spinnerVaschettaGrandeModel.setMinimum(0f);
        this.spinnerVaschettaGrandeModel.setStepSize(0.50f);

        // Imposto le variabili di backup spinnerVaschettaPiccolaModelBackup, spinnerVaschettaMediaModelBackup
        // spinnerVaschettaGrandeModelBackup
        // Formatto la visualizzazione corretta della data/ora
        DateEditor jSpinnerOraDiAperturaDateEditor = new DateEditor(this.jSpinnerOraDiApertura, "HH:mm");
        DateEditor jSpinnerOraDiChiusuraDateEditor = new DateEditor(this.jSpinnerOraDiChiusura, "HH:mm");
        this.jSpinnerOraDiApertura.setEditor(jSpinnerOraDiAperturaDateEditor);
        this.jSpinnerOraDiChiusura.setEditor(jSpinnerOraDiChiusuraDateEditor);

        // Imposto la variabile di backup listaGustiVenditaModelBackup
        defaultListModelCopy(this.listaGustiVenditaModel, this.listaGustiVenditaModelBackup);

        // Imposto le variabili di backup jSpinnerOraDiAperturaBackup, jSpinnerOraDiChiusuraBackup
        spinnerValueCopy(this.jSpinnerOraDiApertura, this.jSpinnerOraDiAperturaBackup);
        spinnerValueCopy(this.jSpinnerOraDiChiusura, this.jSpinnerOraDiChiusuraBackup);

        // Imposto le variabili di backup jTextFieldVaschettaPiccolaBackup, jTextFieldVaschettaMediaBackup,
        // jTextFieldVaschettaGrandeBackup
        // Disattivazione dei tabbed pane in quanto la navigazione
        // tra i vari pannelli/interfaccie del software deve essere
        // effettuata tramite appositi bottoni
        this.jTabbedPaneMain.setEnabledAt(0, true);
        this.jTabbedPaneMain.setEnabledAt(1, false);
        this.jTabbedPaneMain.setEnabledAt(2, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(0, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(1, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(2, false);

    }

    /**
     * @brief Recupera informazioni necessarie dal server
     *
     * Recupera informazioni necessarie ad inizializzare le necessarie risorse
     * per il corretto funzionamento del software dal server e gestisce
     * eventuali errori di recupero delle informazioni.
     *
     * @pre La connessione con il server FTP deve essere già stata stabilita
     *
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiAperturaModel
     * @see InterfacciaPrincipaleGalataio.spinnerOraDiChiusuraModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaPiccolaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaMediaModel
     * @see InterfacciaPrincipaleGalataio.spinnerVaschettaGrandeModel
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModel
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiAperturaBackup
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusuraBackup
     * @see InterfacciaPrincipaleGalataio.listaOrdiniModel
     * @see InterfacciaPrincipaleGalataio.parserXMLModificaOrarioDelNegozio
     * @see InterfacciaPrincipaleGalataio.parserXMLModificaPrezzoVaschette
     * @see
     * InterfacciaPrincipaleGalataio.parserXMLCompositionListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.parserXMLOrdine
     */
    private void loadFromServer() {
        // TUTTI I VALORI QUI INSERITI MANUALMENTE DEVONO ESSERE SOSTITUITI
        // CON OPERAZIONI DI RICAVO DI VALORI DAL SERVER!!!

        // Imposto la directory static
        this.server.setCurrentDirectory("../../../../update07/static");

        try {
            System.out.println("Download file: 'OrarioDelNegozio.xml'");

            // Download in locale
            this.server.downloadFile("OrarioDelNegozio.xml", new FileOutputStream(new File("./data/static/OrarioDelNegozio.xml")));

            try {
                // SEQUENZA DEGLI EVENTI
                // Caricamento dati dal server per il pannello jPanelModificaOrarioNegozio
                ArrayList<OrarioDelNegozio> result = this.parserXMLModificaOrarioDelNegozio.parseDocument("./data/static/OrarioDelNegozio.xml");

                // Imposto le variabili spinnerOraDiAperturaModel, spinnerOraDiChiusuraModel
                this.spinnerOraDiAperturaModel.setValue(result.get(0).getOraDiAperturaDate());
                this.spinnerOraDiChiusuraModel.setValue(result.get(0).getOraDiChiusuraDate());
            } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
                // SEQUENZA ALTERNATIVA
                OrarioDelNegozio orarioDelNegozioDefault = new OrarioDelNegozio("00:00", "00:00");
                this.spinnerOraDiAperturaModel.setValue(orarioDelNegozioDefault.getOraDiAperturaDate());
                this.spinnerOraDiChiusuraModel.setValue(orarioDelNegozioDefault.getOraDiChiusuraDate());
                JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                        + "Si consiglia il riavvio del software.",
                        "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            // SEQUENZA ALTERNATIVA
            OrarioDelNegozio orarioDelNegozioDefault = new OrarioDelNegozio("00:00", "00:00");
            this.spinnerOraDiAperturaModel.setValue(orarioDelNegozioDefault.getOraDiAperturaDate());
            this.spinnerOraDiChiusuraModel.setValue(orarioDelNegozioDefault.getOraDiChiusuraDate());

            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("Download file: 'PrezzoVaschette.xml'");

            // Download in locale
            this.server.downloadFile("PrezzoVaschette.xml", new FileOutputStream(new File("./data/static/PrezzoVaschette.xml")));

            try {
                // SEQUENZA DEGLI EVENTI
                // Caricamento dati dal server per il pannello jPanelModificaPrezzi
                /// DOWNLOAD
                ArrayList<PrezzoVaschette> result = this.parserXMLModificaPrezzoVaschette.parseDocument("./data/static/PrezzoVaschette.xml");

                // Imposto le variabili spinnerVaschettaPiccolaModel, spinnerVaschettaMediaModel,
                // spinnerVaschettaGrandeModel
                this.spinnerVaschettaPiccolaModel.setValue(result.get(0).getPrezzoVaschettaPiccola());
                this.spinnerVaschettaMediaModel.setValue(result.get(0).getPrezzoVaschettaMedia());
                this.spinnerVaschettaGrandeModel.setValue(result.get(0).getPrezzoVaschettaGrande());
            } catch (IOException | ParseException | ParserConfigurationException | SAXException ex) {
                // SEQUENZA ALTERNATIVA
                PrezzoVaschette prezzoVaschetteDefault = new PrezzoVaschette(0f, 0f, 0f);
                this.spinnerVaschettaPiccolaModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaPiccola());
                this.spinnerVaschettaMediaModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaMedia());
                this.spinnerVaschettaGrandeModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaGrande());

                JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                        + "Si consiglia il riavvio del software.",
                        "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            // SEQUENZA ALTERNATIVA
            PrezzoVaschette prezzoVaschetteDefault = new PrezzoVaschette(0f, 0f, 0f);
            this.spinnerVaschettaPiccolaModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaPiccola());
            this.spinnerVaschettaMediaModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaMedia());
            this.spinnerVaschettaGrandeModel.setValue(prezzoVaschetteDefault.getPrezzoVaschettaGrande());

            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("Download file: 'ListaPredefinitaGusti.xml'");

            // Download in locale
            this.server.downloadFile("ListaPredefinitaGusti.xml", new FileOutputStream(new File("./data/static/ListaPredefinitaGusti.xml")));

            try {
                //SEQUENZA DEGLI EVENTI
                // Parsing
                ArrayList<CompositionListaPredefinitaGusti> result = this.parserXMLCompositionListaPredefinitaGusti.parseDocument("./data/static/ListaPredefinitaGusti.xml");

                // Imposto valori della lista ottenuta alla lista predefinita dei gusti
                this.listaPredefinitaGustiModel.addAll(result);
            } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
                // SEQUENZA ALTERNATIVA
                JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                        + "Si consiglia il riavvio del software.",
                        "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            // SEQUENZA ALTERNATIVA
            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("Download file: 'ListaGustiInVendita.xml'");

            // Download in locale
            this.server.downloadFile("ListaGustiInVendita.xml", new FileOutputStream(new File("./data/static/ListaGustiInVendita.xml")));

            // SEQUENZA DEGLI EVENTI
            // Parsing
            ArrayList<CompositionListaPredefinitaGusti> result = this.parserXMLCompositionListaPredefinitaGusti.parseDocument("./data/static/ListaGustiInVendita.xml");

            // Imposto valori della lista ottenuta alla lista predefinita dei gusti
            this.listaGustiVenditaModel.addAll(result);
        } catch (FileNotFoundException ex) {
            // SEQUENZA ALTERNATIVA
            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException | ParseException | IOException ex) {
            // SEQUENZA ALTERNATIVA
            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("Download file: 'OrdiniNonEvasi.xml'");

            // Download in locale
            this.server.downloadFile("OrdiniNonEvasi.xml", new FileOutputStream(new File("./data/static/OrdiniNonEvasi.xml")));

            // SEQUENZA DEGLI EVENTI
            // Parsing
            this.resultOrdiniNonEvasi = this.parserXMLOrdine.parseDocument("./data/static/OrdiniNonEvasi.xml");

            // Imposto valori della lista ottenuta alla lista predefinita dei gusti
            this.listaOrdiniModel.addAll(this.resultOrdiniNonEvasi);
        } catch (FileNotFoundException ex) {
            // SEQUENZA ALTERNATIVA
            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException | ParseException | IOException ex) {
            // SEQUENZA ALTERNATIVA
            JOptionPane.showMessageDialog(null, "Importazione dati XML non riuscita. Valori stabili di default utilizzati. "
                    + "Si consiglia il riavvio del software.",
                    "Importazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Collego i modelli degli spinner ai rispettivi spinner
        this.jSpinnerPrezzo.setModel(this.spinnerVaschettaPiccolaModel);

        this.jSpinnerOraDiApertura.setModel(this.spinnerOraDiAperturaModel);
        this.jSpinnerOraDiChiusura.setModel(this.spinnerOraDiChiusuraModel);
        this.jSpinnerOraDiAperturaBackup.setModel(this.spinnerOraDiAperturaBackupBlankModel);
        this.jSpinnerOraDiChiusuraBackup.setModel(this.spinnerOraDiChiusuraBackupBlankModel);

    }

    /**
     * @brief Copia il valore contenuto nello spinner src nello spinner dest
     * @param[in] src Lo spinner dal quale prelevare il valore da copiare
     * @param[out] dest Lo spinner nel quale copiare il valore da copiare
     */
    private static void spinnerValueCopy(JSpinner src, JSpinner dest) {
        // Copia del valore
        dest.setValue(src.getValue());
    }

    /**
     * @brief Copia il valore dal modello lista src nel modello lista dest
     * @param[in] src Il modello lista dal quale prelevare il valore da copiare
     * @param[out] dest Il modello lista nel quale copiare il valore da copiare
     */
    private static void defaultListModelCopy(DefaultListModel src, DefaultListModel dest) {
        // Pulisco la dest
        dest.removeAllElements();

        // Copio
        for (int i = 0; i < src.size(); i++) {
            dest.addElement(src.getElementAt(i));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelInterfacciaPrincipaleGelataio = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelOrdini = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        jListOrdini = new javax.swing.JList<>();
        jButtonInoltroAlFattorino = new javax.swing.JButton();
        jButtonModificaListaGustiInVendita = new javax.swing.JButton();
        jButtonImpostazioni = new javax.swing.JButton();
        jLabelOraCorrente = new javax.swing.JLabel();
        jPanelModificaListaGustiInVendita = new javax.swing.JPanel();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelListaPredefinitaGusti1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListListaPredefinitaGusti1 = new javax.swing.JList<>();
        jLabelListaGustiVendita = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListListaGustiVendita = new javax.swing.JList<>();
        jButtonInserisci = new javax.swing.JButton();
        jButtonRimuovi1 = new javax.swing.JButton();
        jButtonConferma2 = new javax.swing.JButton();
        jButtonAnnulla2 = new javax.swing.JButton();
        jLabelOraCorrente5 = new javax.swing.JLabel();
        jPanelImpostazioni = new javax.swing.JPanel();
        jTabbedPaneImpostazioni = new javax.swing.JTabbedPane();
        jPanelMenu = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jButtonModificaListaPredefinitaGusti = new javax.swing.JButton();
        jButtonModificaOrarioNegozio = new javax.swing.JButton();
        jButtonIndietro = new javax.swing.JButton();
        jLabelOraCorrente1 = new javax.swing.JLabel();
        jButtonScaricaCronologiaOrdinazioni = new javax.swing.JButton();
        jButtonEliminaCronologiaOrdinazioni = new javax.swing.JButton();
        jLabelFrammentiCronologici = new javax.swing.JLabel();
        jPanelModificaListaPredefinita = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelListaPredefinitaGusti = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListListaPredefinitaGusti = new javax.swing.JList<>();
        jButtonAggiungi = new javax.swing.JButton();
        jButtonRimuovi = new javax.swing.JButton();
        jButtonIndietro1 = new javax.swing.JButton();
        jTextFieldGustoListaPredefinita = new javax.swing.JTextField();
        jLabelOraCorrente2 = new javax.swing.JLabel();
        jComboBoxTipo = new javax.swing.JComboBox<>();
        jSpinnerPrezzo = new javax.swing.JSpinner();
        jPanelModificaOrarioNegozio = new javax.swing.JPanel();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelOraDiApertura = new javax.swing.JLabel();
        jLabelOraDiChiusura = new javax.swing.JLabel();
        jSpinnerOraDiApertura = new javax.swing.JSpinner();
        jSpinnerOraDiChiusura = new javax.swing.JSpinner();
        jButtonConferma = new javax.swing.JButton();
        jButtonAnnulla = new javax.swing.JButton();
        jLabelOraCorrente3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelLogo.setText("Logo");
        jLabelLogo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelOrdini.setText("Ordini");

        jListOrdini.setToolTipText("PROVA");
        jListOrdini.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane.setViewportView(jListOrdini);

        jButtonInoltroAlFattorino.setText("Evadi ordine");
        jButtonInoltroAlFattorino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInoltroAlFattorinoActionPerformed(evt);
            }
        });

        jButtonModificaListaGustiInVendita.setText("Modifica prodotti in vendita");
        jButtonModificaListaGustiInVendita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificaListaGustiInVenditaActionPerformed(evt);
            }
        });

        jButtonImpostazioni.setText("Impostazioni");
        jButtonImpostazioni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImpostazioniActionPerformed(evt);
            }
        });

        jLabelOraCorrente.setFont(new java.awt.Font("Tahoma", 0, 60)); // NOI18N
        jLabelOraCorrente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelInterfacciaPrincipaleGelataioLayout = new javax.swing.GroupLayout(jPanelInterfacciaPrincipaleGelataio);
        jPanelInterfacciaPrincipaleGelataio.setLayout(jPanelInterfacciaPrincipaleGelataioLayout);
        jPanelInterfacciaPrincipaleGelataioLayout.setHorizontalGroup(
            jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                        .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonModificaListaGustiInVendita, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonInoltroAlFattorino, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonImpostazioni, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                        .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelOrdini, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 565, Short.MAX_VALUE)
                        .addComponent(jLabelOraCorrente, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelInterfacciaPrincipaleGelataioLayout.setVerticalGroup(
            jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelOraCorrente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabelOrdini)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                        .addComponent(jButtonInoltroAlFattorino, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonModificaListaGustiInVendita, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonImpostazioni, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 335, Short.MAX_VALUE))
                    .addGroup(jPanelInterfacciaPrincipaleGelataioLayout.createSequentialGroup()
                        .addComponent(jScrollPane)
                        .addContainerGap())))
        );

        jTabbedPaneMain.addTab("Main", jPanelInterfacciaPrincipaleGelataio);

        jLabelLogo5.setText("Logo");
        jLabelLogo5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelListaPredefinitaGusti1.setText("Tutti i prodotti");

        jListListaPredefinitaGusti1.setToolTipText("PROVA");
        jScrollPane1.setViewportView(jListListaPredefinitaGusti1);

        jLabelListaGustiVendita.setText("Prodotti in vendita");

        jListListaGustiVendita.setToolTipText("PROVA");
        jScrollPane2.setViewportView(jListListaGustiVendita);

        jButtonInserisci.setText(">>");
        jButtonInserisci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInserisciActionPerformed(evt);
            }
        });

        jButtonRimuovi1.setText("X");
        jButtonRimuovi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRimuovi1ActionPerformed(evt);
            }
        });

        jButtonConferma2.setText("Conferma");
        jButtonConferma2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConferma2ActionPerformed(evt);
            }
        });

        jButtonAnnulla2.setText("Annulla");
        jButtonAnnulla2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnnulla2ActionPerformed(evt);
            }
        });

        jLabelOraCorrente5.setFont(new java.awt.Font("Tahoma", 0, 60)); // NOI18N
        jLabelOraCorrente5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelModificaListaGustiInVenditaLayout = new javax.swing.GroupLayout(jPanelModificaListaGustiInVendita);
        jPanelModificaListaGustiInVendita.setLayout(jPanelModificaListaGustiInVenditaLayout);
        jPanelModificaListaGustiInVenditaLayout.setHorizontalGroup(
            jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                        .addComponent(jLabelLogo5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelOraCorrente5, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                        .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelListaPredefinitaGusti1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelListaGustiVendita, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonInserisci, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRimuovi1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonConferma2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAnnulla2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanelModificaListaGustiInVenditaLayout.setVerticalGroup(
            jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelLogo5, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(jLabelOraCorrente5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelListaGustiVendita)
                            .addComponent(jLabelListaPredefinitaGusti1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(jPanelModificaListaGustiInVenditaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelModificaListaGustiInVenditaLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButtonInserisci, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonRimuovi1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonConferma2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAnnulla2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Prodottii in vendita", jPanelModificaListaGustiInVendita);

        jLabelLogo1.setText("Logo");
        jLabelLogo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonModificaListaPredefinitaGusti.setText("Aggiungi / Rimuovi prodotti dal database");
        jButtonModificaListaPredefinitaGusti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificaListaPredefinitaGustiActionPerformed(evt);
            }
        });

        jButtonModificaOrarioNegozio.setText("Modifica orario ordinazioni");
        jButtonModificaOrarioNegozio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificaOrarioNegozioActionPerformed(evt);
            }
        });

        jButtonIndietro.setText("Indietro");
        jButtonIndietro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIndietroActionPerformed(evt);
            }
        });

        jLabelOraCorrente1.setFont(new java.awt.Font("Tahoma", 0, 60)); // NOI18N
        jLabelOraCorrente1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButtonScaricaCronologiaOrdinazioni.setText("Scarica cronologia ordinazioni");
        jButtonScaricaCronologiaOrdinazioni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScaricaCronologiaOrdinazioniActionPerformed(evt);
            }
        });

        jButtonEliminaCronologiaOrdinazioni.setText("Elimina cronologia ordinazioni");
        jButtonEliminaCronologiaOrdinazioni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminaCronologiaOrdinazioniActionPerformed(evt);
            }
        });

        jLabelFrammentiCronologici.setText("Frammenti Cronologici: 0");

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelMenuLayout.createSequentialGroup()
                                .addComponent(jLabelLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 540, Short.MAX_VALUE)
                                .addComponent(jLabelOraCorrente1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonIndietro, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonEliminaCronologiaOrdinazioni, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonScaricaCronologiaOrdinazioni, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonModificaOrarioNegozio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonModificaListaPredefinitaGusti, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFrammentiCronologici, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelLogo1, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(jLabelOraCorrente1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 35, Short.MAX_VALUE)
                .addComponent(jButtonModificaListaPredefinitaGusti, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonModificaOrarioNegozio, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonScaricaCronologiaOrdinazioni, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelFrammentiCronologici, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonEliminaCronologiaOrdinazioni, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                .addComponent(jButtonIndietro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPaneImpostazioni.addTab("Menù", jPanelMenu);

        jLabelLogo2.setText("Logo");
        jLabelLogo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelListaPredefinitaGusti.setText("Tutti i prodotti");

        jListListaPredefinitaGusti.setToolTipText("PROVA");
        jScrollPane3.setViewportView(jListListaPredefinitaGusti);

        jButtonAggiungi.setText("Aggiungi");
        jButtonAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAggiungiActionPerformed(evt);
            }
        });

        jButtonRimuovi.setText("X");
        jButtonRimuovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRimuoviActionPerformed(evt);
            }
        });

        jButtonIndietro1.setText("Indietro");
        jButtonIndietro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIndietro1ActionPerformed(evt);
            }
        });

        jLabelOraCorrente2.setFont(new java.awt.Font("Tahoma", 0, 60)); // NOI18N
        jLabelOraCorrente2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cibo", "Bevanda" }));

        jSpinnerPrezzo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanelModificaListaPredefinitaLayout = new javax.swing.GroupLayout(jPanelModificaListaPredefinita);
        jPanelModificaListaPredefinita.setLayout(jPanelModificaListaPredefinitaLayout);
        jPanelModificaListaPredefinitaLayout.setHorizontalGroup(
            jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                        .addComponent(jLabelLogo2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelOraCorrente2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                        .addGroup(jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelListaPredefinitaGusti, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
                        .addGroup(jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButtonRimuovi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(jButtonIndietro1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(jButtonAggiungi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(jTextFieldGustoListaPredefinita, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(jComboBoxTipo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSpinnerPrezzo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanelModificaListaPredefinitaLayout.setVerticalGroup(
            jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelLogo2, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(jLabelOraCorrente2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanelModificaListaPredefinitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelListaPredefinitaGusti)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(jPanelModificaListaPredefinitaLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jButtonAggiungi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinnerPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldGustoListaPredefinita, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jButtonRimuovi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(jButtonIndietro1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPaneImpostazioni.addTab("Database prodotti", jPanelModificaListaPredefinita);

        jLabelLogo3.setText("Logo");
        jLabelLogo3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelOraDiApertura.setText("Ora di apertura:");
        jLabelOraDiApertura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelOraDiChiusura.setText("Ora di chiusura:");
        jLabelOraDiChiusura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jSpinnerOraDiApertura.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jSpinnerOraDiChiusura.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jButtonConferma.setText("Conferma");
        jButtonConferma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfermaActionPerformed(evt);
            }
        });

        jButtonAnnulla.setText("Annulla");
        jButtonAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnnullaActionPerformed(evt);
            }
        });

        jLabelOraCorrente3.setFont(new java.awt.Font("Tahoma", 0, 60)); // NOI18N
        jLabelOraCorrente3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelModificaOrarioNegozioLayout = new javax.swing.GroupLayout(jPanelModificaOrarioNegozio);
        jPanelModificaOrarioNegozio.setLayout(jPanelModificaOrarioNegozioLayout);
        jPanelModificaOrarioNegozioLayout.setHorizontalGroup(
            jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAnnulla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonConferma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                        .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                                .addComponent(jLabelOraDiApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSpinnerOraDiApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                                .addComponent(jLabelOraDiChiusura, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSpinnerOraDiChiusura)))
                        .addGap(0, 701, Short.MAX_VALUE))
                    .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                        .addComponent(jLabelLogo3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelOraCorrente3, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelModificaOrarioNegozioLayout.setVerticalGroup(
            jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificaOrarioNegozioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelOraCorrente3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelLogo3, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSpinnerOraDiApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabelOraDiApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelModificaOrarioNegozioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSpinnerOraDiChiusura, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabelOraDiChiusura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE)
                .addComponent(jButtonConferma, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAnnulla, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPaneImpostazioni.addTab("Orario ordinazioni", jPanelModificaOrarioNegozio);

        javax.swing.GroupLayout jPanelImpostazioniLayout = new javax.swing.GroupLayout(jPanelImpostazioni);
        jPanelImpostazioni.setLayout(jPanelImpostazioniLayout);
        jPanelImpostazioniLayout.setHorizontalGroup(
            jPanelImpostazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelImpostazioniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneImpostazioni)
                .addContainerGap())
        );
        jPanelImpostazioniLayout.setVerticalGroup(
            jPanelImpostazioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelImpostazioniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneImpostazioni)
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Impostazioni", jPanelImpostazioni);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @brief Bottone annulla dell'interfaccia "Modifica Lista Gusti In Vendita"
     *
     * Il metodo viene richiamato alla pressione del bottone annulla
     * dell'interfaccia "Modifica Lista Gusti In Vendita" e annulla tutte le
     * modifiche effettuate ripristinando l'ultimo stato stabile. Infine,
     * ritorna all'interfaccia "Main".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.defaultListModelCopy()
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     */
    private void jButtonAnnulla2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnulla2ActionPerformed
        // TODO add your handling code here:
        // Annullo le modifiche apportate al modello listaGustiVenditaModel
        // ripristinando la situazione precedente salvata nel modello modellolistaGustiVenditaModelBackup
        defaultListModelCopy(this.listaGustiVenditaModelBackup, this.listaGustiVenditaModel);

        // Apro il pannello main
        this.jTabbedPaneMain.setSelectedComponent(this.jPanelInterfacciaPrincipaleGelataio);

        this.jTabbedPaneMain.setEnabledAt(0, true);
        this.jTabbedPaneMain.setEnabledAt(1, false);
    }//GEN-LAST:event_jButtonAnnulla2ActionPerformed

    /**
     * @brief Bottone conferma dell'interfaccia "Modifica Lista Gusti In
     * Vendita"
     *
     * Il metodo viene richiamato alla pressione del bottone conferma
     * dell'interfaccia "Modifica Lista Gusti In Vendita" e salva tutte le
     * modifiche effettuate, definendo di conseguenza l'ultimo stato stabile.
     * Effettua successivamente il caricamento dei nuovi parametri nel server,
     * gestendo eventuali errori di comunicazione. Infine, ritorna
     * all'interfaccia "Main".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.defaultListModelCopy()
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModel
     * @see
     * InterfacciaPrincipaleGalataio.generatorXMLCompositionListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     */
    private void jButtonConferma2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConferma2ActionPerformed
        // TODO add your handling code here:
        // Confermo le modifiche apportate al modello listaGustiVenditaModel
        // riportandole anche nel modellolistaGustiVenditaModelBackup
        defaultListModelCopy(this.listaGustiVenditaModel, this.listaGustiVenditaModelBackup);

        // Salvo il tutto nel file XML
        Object[] listaPredefinitaGustiModelElementsArrayGeneric = listaGustiVenditaModel.toArray();
        ArrayList<CompositionListaPredefinitaGusti> data = new ArrayList<>();

        for (var each : listaPredefinitaGustiModelElementsArrayGeneric) {
            data.add((CompositionListaPredefinitaGusti) each);
        }

        try {
            this.generatorXMLCompositionListaPredefinitaGusti.createDOMTree(data);
            this.generatorXMLCompositionListaPredefinitaGusti.applyDOMonXMLFile("./data/static/ListaGustiInVendita.xml");

            // UPLOAD
            // Imposto la directory static
            this.server.setCurrentDirectory("../../../../update07/static");

            System.out.println("Upload file: 'ListaGustiInVendita.xml'");
            this.server.uploadFile("ListaGustiInVendita.xml", new FileInputStream(new File("./data/static/ListaGustiInVendita.xml")));
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Esportazione dati XML non riuscita. Tutte le modifiche effettuate non esportate "
                    + "verranno perse alla chiusura del software. Si consiglia di ripetere le modifiche in modo da causare un nuovo "
                    + "tentativo di esportazione.",
                    "Esportazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Apro il pannello main
        this.jTabbedPaneMain.setSelectedComponent(this.jPanelInterfacciaPrincipaleGelataio);

        this.jTabbedPaneMain.setEnabledAt(0, true);
        this.jTabbedPaneMain.setEnabledAt(1, false);
    }//GEN-LAST:event_jButtonConferma2ActionPerformed

    /**
     * @brief Bottone rimuovi dell'interfaccia "Modifica Lista Gusti In Vendita"
     *
     * Il metodo viene richiamato alla pressione del bottone rimuovi
     * dell'interfaccia "Modifica Lista Gusti In Vendita" e rimuove l'elemento
     * selezionato nella lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita e gestisce eventuali
     * errori durante la rimozione.
     *
     * @param evt Evento
     *
     * @pre L'elemento da rimuovere deve essere già stato selezionato
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModel
     */
    private void jButtonRimuovi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRimuovi1ActionPerformed
        // TODO add your handling code here:
        // Rimuovere l'elemento selezionato dalla lista jListListaGustiVendita

        // Controlli
        if (this.jListListaGustiVendita.getSelectedIndex() != -1) {
            this.listaGustiVenditaModel.remove(this.jListListaGustiVendita.getSelectedIndex());
        } else {
            JOptionPane.showMessageDialog(null, "Nessun valore selezionato da rimuovere dalla lista.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonRimuovi1ActionPerformed

    /**
     * @brief Bottone inserisci dell'interfaccia "Modifica Lista Gusti In
     * Vendita"
     *
     * Il metodo viene richiamato alla pressione del bottone inserisci
     * dell'interfaccia "Modifica Lista Gusti In Vendita" e aggiunge un nuovo
     * elemento nella lista InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * e gestisce eventuali errori durante l'aggiunta. L'elemento da aggiungere
     * viene selezionato dalla lista
     * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1.
     *
     * @param evt Evento
     *
     * @pre L'elemento da aggiungere deve essere già stato selezionato
     *
     * @see InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti1
     * @see InterfacciaPrincipaleGalataio.listaGustiVenditaModel
     */
    private void jButtonInserisciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInserisciActionPerformed
        // TODO add your handling code here:
        // Aggiungere l'elemento selezionato dalla lista jListListaPredefinitaGusti1 alla lista
        // jListListaGustiVendita

        Boolean status = true;

        // Controlli
        if (this.jListListaPredefinitaGusti1.getSelectedIndex() != -1) {
            for (int i = 0; i < this.listaGustiVenditaModel.size(); i++) {
                if (this.listaGustiVenditaModel.getElementAt(i).equals(this.jListListaPredefinitaGusti1.getSelectedValue())) {
                    status = false;
                    break;
                }
            }

            if (status) {
                this.listaGustiVenditaModel.addElement(this.jListListaPredefinitaGusti1.getSelectedValue());
                this.jListListaPredefinitaGusti1.setSelectedValue(null, false);
            } else {
                JOptionPane.showMessageDialog(null, "Valore già presente nella lista.",
                        "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nessun valore selezionato da aggiungere alla lista.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonInserisciActionPerformed

    /**
     * @brief Bottone impostazioni dell'interfaccia "Main"
     *
     * Il metodo viene richiamato alla pressione del bottone impostazioni
     * dell'interfaccia "Main" e apre l'interfaccia "Impostazioni".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonImpostazioniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImpostazioniActionPerformed
        // TODO add your handling code here:
        // Apro il pannello Impostazioni
        this.jTabbedPaneMain.setSelectedComponent(this.jPanelImpostazioni);

        this.jTabbedPaneMain.setEnabledAt(0, false);
        this.jTabbedPaneMain.setEnabledAt(2, true);
        this.jTabbedPaneImpostazioni.setEnabledAt(0, true);
    }//GEN-LAST:event_jButtonImpostazioniActionPerformed

    /**
     * @brief Bottone modifica lista gusti in vendita dell'interfaccia "Main"
     *
     * Il metodo viene richiamato alla pressione del bottone modifica lista
     * gusti in vendita dell'interfaccia "Main" e apre l'interfaccia "Modifica
     * Lista Gusti In Vendita".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     */
    private void jButtonModificaListaGustiInVenditaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificaListaGustiInVenditaActionPerformed
        // TODO add your handling code here:
        // Apro il pannello modifica lista gusti in vendita
        this.jTabbedPaneMain.setSelectedComponent(this.jPanelModificaListaGustiInVendita);
        this.jTabbedPaneMain.setEnabledAt(0, false);
        this.jTabbedPaneMain.setEnabledAt(1, true);
    }//GEN-LAST:event_jButtonModificaListaGustiInVenditaActionPerformed

    /**
     * @brief Bottone inoltro al fattorino dell'interfaccia "Main"
     *
     * Il metodo viene richiamato alla pressione del bottone inoltro al
     * fattorino dell'interfaccia "Main" ed effettua l'inoltro dell'ordinazione
     * selezionata dalla lista InterfacciaPrincipaleGalataio.jListOrdini al
     * fattorino e gestisce eventuli errori durante l'inoltro.
     *
     * @param evt Evento
     *
     * @pre L'ordine da inoltrare deve essere già stato selezionato
     *
     * @see InterfacciaPrincipaleGalataio.jListOrdini
     * @see InterfacciaPrincipaleGalataio.listaOrdiniModel
     * @see InterfacciaPrincipaleGalataio.generatorXMLOrdinev2
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.generatorXMLOrdine
     */
    private void jButtonInoltroAlFattorinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInoltroAlFattorinoActionPerformed

        // TODO add your handling code here:
        // Controllo che sia stato selezionato un elemento
        // dalla lista jListOrdini
        if (this.jListOrdini.getSelectedIndex() != -1) {
            try {
                // Genero file casuale nel server
                // Imposto la directory static
                this.server.setCurrentDirectory("../../../../update07/cronologia");

                Ordine get = (Ordine) this.listaOrdiniModel.get(this.jListOrdini.getSelectedIndex());
                ArrayList<Ordine> arrayListOrdine = new ArrayList<>();
                arrayListOrdine.add(get);

                this.generatorXMLOrdinev2.createDOMTree(arrayListOrdine);
                this.generatorXMLOrdinev2.applyDOMonXMLFile("./data/supportive/Database.xml");

                System.out.println("Upload unique file");
                this.server.uploadUniqueFile(new FileInputStream(new File("./data/supportive/Database.xml")));

                // A questo punto, se siamo arrivati fin qui significa che l'inoltro è andato a buon fine.
                // Rimuovo l'ordine appena inoltrato dalla lista jListOrdini
                this.listaOrdiniModel.remove(this.jListOrdini.getSelectedIndex());

                /*} catch (TransformerException | ParserConfigurationException | FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Evasione ordinazione non riuscita.",
                "Errore evasione ordinazione", JOptionPane.ERROR_MESSAGE);
                
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                ArrayList<Ordine> ordini = new ArrayList<>();
                for (int i = 0; i < this.listaOrdiniModel.getSize(); i++) {
                    ordini.add((Ordine) this.listaOrdiniModel.getElementAt(i));
                }

                try {
                    this.generatorXMLOrdine.createDOMTree(ordini);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    this.generatorXMLOrdine.applyDOMonXMLFile("./data/static/OrdiniNonEvasi.xml");
                } catch (TransformerException ex) {
                    Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Upload
                this.server.setCurrentDirectory("../../../../update07/static");
                try {
                    System.out.println("Upload file: 'OrdiniNonEvasi.xml'");
                    this.server.uploadFile("OrdiniNonEvasi.xml", new FileInputStream(new File("./data/static/OrdiniNonEvasi.xml")));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nessun valore selezionato da evadere.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonInoltroAlFattorinoActionPerformed

    /**
     * @brief Bottone annulla dell'interfaccia "Modifica Orario Negozio"
     *
     * Il metodo viene richiamato alla pressione del bottone annulla
     * dell'interfaccia "Modifica Orario Negozio" e annulla tutte le modifiche
     * effettuate ripristinando l'ultimo stato stabile. Infine, ritorna
     * all'interfaccia "Menù".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.spinnerValueCopy()
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnullaActionPerformed
        // TODO add your handling code here:
        // Reimposto valori precedenti

        spinnerValueCopy(this.jSpinnerOraDiAperturaBackup, this.jSpinnerOraDiApertura);
        spinnerValueCopy(this.jSpinnerOraDiChiusuraBackup, this.jSpinnerOraDiChiusura);

        // Apro il pannello menù
        this.jTabbedPaneImpostazioni.setSelectedComponent(this.jPanelMenu);

        this.jTabbedPaneImpostazioni.setEnabledAt(2, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(0, true);
    }//GEN-LAST:event_jButtonAnnullaActionPerformed

    /**
     * @brief Bottone conferma dell'interfaccia "Modifica Orario Negozio"
     *
     * Il metodo viene richiamato alla pressione del bottone conferma
     * dell'interfaccia "Modifica Orario Negozio" e salva tutte le modifiche
     * effettuate, definendo di conseguenza l'ultimo stato stabile. Effettua
     * successivamente il caricamento dei nuovi parametri nel server, gestendo
     * eventuali errori di comunicazione. Infine, ritorna all'interfaccia
     * "Menù".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiApertura
     * @see InterfacciaPrincipaleGalataio.jSpinnerOraDiChiusura
     * @see InterfacciaPrincipaleGalataio.spinnerValueCopy()
     * @see InterfacciaPrincipaleGalataio.generatorXMLModificaOrarioDelNegozio
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonConfermaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfermaActionPerformed
        // TODO add your handling code here:        
        // Controlli        
        String xray = ((Date) this.jSpinnerOraDiApertura.getValue()).toString().split(" ")[3];
        String xray2 = xray.substring(0, xray.indexOf(":", xray.indexOf(":") + 1));
        Integer H1 = Integer.parseInt(xray2.substring(0, xray2.indexOf(":")));
        Integer M1 = Integer.parseInt(xray2.substring(xray2.indexOf(":") + 1));

        String xray1 = ((Date) this.jSpinnerOraDiChiusura.getValue()).toString().split(" ")[3];
        String xray21 = xray1.substring(0, xray1.indexOf(":", xray1.indexOf(":") + 1));
        Integer H2 = Integer.parseInt(xray21.substring(0, xray21.indexOf(":")));
        Integer M2 = Integer.parseInt(xray21.substring(xray21.indexOf(":") + 1));

        if (H1 > H2) {
            JOptionPane.showMessageDialog(null, "L'orario di apertura deve essere antecedente all'orario di chiusura.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);

            return;
        } else if (Objects.equals(H1, H2)) {
            if (M1 > M2) {
                JOptionPane.showMessageDialog(null, "L'orario di apertura deve essere antecedente all'orario di chiusura.",
                        "Errore checkout valore", JOptionPane.ERROR_MESSAGE);

                return;
            }
        }

        if (((Date) this.jSpinnerOraDiApertura.getValue()).equals((Date) this.jSpinnerOraDiChiusura.getValue())) {
            JOptionPane.showMessageDialog(null, "Gli orari di apertura e chiusura non possono coincidere.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);

        } else {

            // Salvo i nuovi valori
            spinnerValueCopy(this.jSpinnerOraDiApertura, this.jSpinnerOraDiAperturaBackup);
            spinnerValueCopy(this.jSpinnerOraDiChiusura, this.jSpinnerOraDiChiusuraBackup);

            // Carico i nuovi valori nel file XML
            ArrayList<OrarioDelNegozio> intoFileXML = new ArrayList<>();
            intoFileXML.add(new OrarioDelNegozio((Date) this.jSpinnerOraDiApertura.getValue(), (Date) this.jSpinnerOraDiChiusura.getValue()));
            try {
                // SEQUENZA DEGLI EVENTI
                this.generatorXMLModificaOrarioDelNegozio.createDOMTree(intoFileXML);
                this.generatorXMLModificaOrarioDelNegozio.applyDOMonXMLFile("./data/static/OrarioDelNegozio.xml");

                // UPLOAD
                // Imposto la directory static
                this.server.setCurrentDirectory("../../../../update07/static");

                System.out.println("Upload file: 'OrarioDelNegozio.xml'");

                this.server.uploadFile("OrarioDelNegozio.xml", new FileInputStream(new File("./data/static/OrarioDelNegozio.xml")));
            } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
                // SEQUENZA ALTERNATIVA
                JOptionPane.showMessageDialog(null, "Esportazione dati XML non riuscita. Tutte le modifiche effettuate non esportate "
                        + "verranno perse alla chiusura del software. Si consiglia di ripetere le modifiche in modo da causare un nuovo "
                        + "tentativo di esportazione.",
                        "Esportazione dati non riuscita", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Apro il pannello menù
            this.jTabbedPaneImpostazioni.setSelectedComponent(this.jPanelMenu);

            this.jTabbedPaneImpostazioni.setEnabledAt(2, false);
            this.jTabbedPaneImpostazioni.setEnabledAt(0, true);
        }
    }//GEN-LAST:event_jButtonConfermaActionPerformed

    /**
     * @brief Bottone indietro dell'interfaccia "Modifica Lista Predefinita"
     *
     * Il metodo viene richiamato alla pressione del bottone indietro
     * dell'interfaccia "Modifica Lista Predefinita" e salva tutte le modifiche
     * effettuate, definendo di conseguenza l'ultimo stato stabile. Effettua
     * successivamente il caricamento dei nuovi parametri nel server, gestendo
     * eventuali errori di comunicazione. Infine, ritorna all'interfaccia
     * "Menù".
     *
     * @param evt Evento
     *
     * @see
     * InterfacciaPrincipaleGalataio.generatorXMLCompositionListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.server
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonIndietro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIndietro1ActionPerformed
        // TODO add your handling code here:
        // Ritorno al pannello menù
        this.jTabbedPaneImpostazioni.setSelectedComponent(this.jPanelMenu);

        ArrayList<Object> toRemove = new ArrayList<>();
        this.flusher.forEach(each -> {
            for (int i = 0; i < this.listaGustiVenditaModel.size(); i++) {
                if (((CompositionListaPredefinitaGusti) this.listaGustiVenditaModel.getElementAt(i)).toString() == null ? (each.toString())
                        == null : ((CompositionListaPredefinitaGusti) this.listaGustiVenditaModel.getElementAt(i)).toString().equals(each.toString())) {
                    toRemove.add(this.listaGustiVenditaModel.elementAt(i));
                }
            }
        });
        toRemove.forEach(each -> {
            this.listaGustiVenditaModel.removeElement(each);
        });
        if (toRemove.size() > 0) {
            defaultListModelCopy(this.listaGustiVenditaModel, this.listaGustiVenditaModelBackup);

            // Salvo il tutto nel file XML
            Object[] listaPredefinitaGustiModelElementsArrayGeneric = listaGustiVenditaModel.toArray();
            ArrayList<CompositionListaPredefinitaGusti> data = new ArrayList<>();

            for (var each : listaPredefinitaGustiModelElementsArrayGeneric) {
                data.add((CompositionListaPredefinitaGusti) each);
            }

            try {
                this.generatorXMLCompositionListaPredefinitaGusti.createDOMTree(data);
                this.generatorXMLCompositionListaPredefinitaGusti.applyDOMonXMLFile("./data/static/ListaGustiInVendita.xml");

                // UPLOAD
                // Imposto la directory static
                this.server.setCurrentDirectory("../../../../update07/static");

                System.out.println("Upload file: 'ListaGustiInVendita.xml'");
                this.server.uploadFile("ListaGustiInVendita.xml", new FileInputStream(new File("./data/static/ListaGustiInVendita.xml")));
            } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Esportazione dati XML non riuscita. Tutte le modifiche effettuate non esportate "
                        + "verranno perse alla chiusura del software. Si consiglia di ripetere le modifiche in modo da causare un nuovo "
                        + "tentativo di esportazione.",
                        "Esportazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

                Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Salvo il tutto nel file XML
        Object[] listaPredefinitaGustiModelElementsArrayGeneric = listaPredefinitaGustiModel.toArray();
        ArrayList<CompositionListaPredefinitaGusti> data = new ArrayList<>();

        for (var each : listaPredefinitaGustiModelElementsArrayGeneric) {
            data.add((CompositionListaPredefinitaGusti) each);
        }

        try {
            this.generatorXMLCompositionListaPredefinitaGusti.createDOMTree(data);
            this.generatorXMLCompositionListaPredefinitaGusti.applyDOMonXMLFile("./data/static/ListaPredefinitaGusti.xml");

            // UPLOAD
            // Imposto la directory static
            this.server.setCurrentDirectory("../../../../update07/static");

            System.out.println("Upload file: 'ListaPredefinitaGusti.xml'");
            this.server.uploadFile("ListaPredefinitaGusti.xml", new FileInputStream(new File("./data/static/ListaPredefinitaGusti.xml")));
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Esportazione dati XML non riuscita. Tutte le modifiche effettuate non esportate "
                    + "verranno perse alla chiusura del software. Si consiglia di ripetere le modifiche in modo da causare un nuovo "
                    + "tentativo di esportazione.",
                    "Esportazione dati non riuscita", JOptionPane.WARNING_MESSAGE);

            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.jTabbedPaneImpostazioni.setEnabledAt(1, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(0, true);
    }//GEN-LAST:event_jButtonIndietro1ActionPerformed

    /**
     * @brief Bottone rimuovi dell'interfaccia "Modifica Lista Predefinita"
     *
     * Il metodo viene richiamato alla pressione del bottone rimuovi
     * dell'interfaccia "Modifica Lista Predefinita" e rimuove l'elemento
     * selezionato nella lista
     * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti e gestisce
     * eventuali errori durante la rimozione.
     *
     * @param evt Evento
     *
     * @pre L'elemento da rimuovere deve essere già stato selezionato
     *
     * @see InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     * @see InterfacciaPrincipaleGalataio.defaultListModelCopy()
     */
    private void jButtonRimuoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRimuoviActionPerformed
        // TODO add your handling code here:
        // Rimuovere l'elemento selezionato dalla lista jListListaPredefinitaGusti/jListListaPredefinitaGusti1

        // Controlli
        if (this.jListListaPredefinitaGusti.getSelectedIndex() != -1) {
            Object remove = this.listaPredefinitaGustiModel.remove(this.jListListaPredefinitaGusti.getSelectedIndex());
            this.flusher.add((CompositionListaPredefinitaGusti) remove);

        } else {
            JOptionPane.showMessageDialog(null, "Nessun valore selezionato da rimuovere dalla lista.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButtonRimuoviActionPerformed

    /**
     * @brief Bottone aggiungi dell'interfaccia "Modifica Lista Predefinita"
     *
     * Il metodo viene richiamato alla pressione del bottone aggiungi
     * dell'interfaccia "Modifica Lista Predefinita" e aggiunge un nuovo
     * elemento nella lista
     * InterfacciaPrincipaleGalataio.jListListaPredefinitaGusti con valore
     * letterale specificato in
     * InterfacciaPrincipaleGalataio.jTextFieldGustoListaPredefinita e gestisce
     * eventuali errori durante l'aggiunta.
     *
     * @param evt Evento
     *
     * @pre InterfacciaPrincipaleGalataio.jTextFieldGustoListaPredefinita deve
     * contenere un valore testuale valido
     *
     * @see InterfacciaPrincipaleGalataio.jTextFieldGustoListaPredefinita
     * @see InterfacciaPrincipaleGalataio.listaPredefinitaGustiModel
     */
    private void jButtonAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAggiungiActionPerformed
        // TODO add your handling code here:
        // Aggiungere l'informazione presente nella jTextFieldGustoListaPredefinita
        // alla lista jListListaPredefinitaGusti/jListListaPredefinitaGusti1

        Boolean status = true;

        // Controlli
        if (this.jTextFieldGustoListaPredefinita.getText().trim() != null && !this.jTextFieldGustoListaPredefinita.getText().trim().isEmpty()) {
            if (Pattern.matches("^[A-Za-z, ]+$", this.jTextFieldGustoListaPredefinita.getText().trim())) {
                for (int i = 0; i < this.listaPredefinitaGustiModel.size(); i++) {
                    if (this.listaPredefinitaGustiModel.getElementAt(i).toString() == null ? (new CompositionListaPredefinitaGusti(this.jTextFieldGustoListaPredefinita.getText().trim(), this.jComboBoxTipo.getSelectedItem().toString(),
                            (Float) this.jSpinnerPrezzo.getValue())).toString() == null : this.listaPredefinitaGustiModel.getElementAt(i).toString().equals((new CompositionListaPredefinitaGusti(this.jTextFieldGustoListaPredefinita.getText().trim(), this.jComboBoxTipo.getSelectedItem().toString(),
                            (Float) this.jSpinnerPrezzo.getValue())).toString())) {
                        status = false;
                        break;
                    }
                }
            } else {
                status = null;
            }

            if (status == null) {
                JOptionPane.showMessageDialog(null, "Valori non ammessi inseriti nel campo di testo.",
                        "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
            } else if (status) {
                this.listaPredefinitaGustiModel.addElement(new CompositionListaPredefinitaGusti(this.jTextFieldGustoListaPredefinita.getText().trim(), this.jComboBoxTipo.getSelectedItem().toString(),
                        (Float) this.jSpinnerPrezzo.getValue()));
                this.jTextFieldGustoListaPredefinita.setText(null);
                this.jComboBoxTipo.setSelectedIndex(0);
                this.jSpinnerPrezzo.setValue(1f);

            } else {

                JOptionPane.showMessageDialog(null, "Valore già presente nella lista.",
                        "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nessun valore valido da aggiungere alla lista.",
                    "Errore checkout valore", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAggiungiActionPerformed

    /**
     * @brief Bottone indietro dell'interfaccia "Menù"
     *
     * Il metodo viene richiamato alla pressione del bottone indietro
     * dell'interfaccia "Menù" e ritorna all'interfaccia "Main".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneMain
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonIndietroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIndietroActionPerformed
        // TODO add your handling code here:
        // Ritorno al pannello main
        this.jTabbedPaneMain.setSelectedComponent(this.jPanelInterfacciaPrincipaleGelataio);

        this.jTabbedPaneImpostazioni.setEnabledAt(0, false);
        this.jTabbedPaneMain.setEnabledAt(2, false);
        this.jTabbedPaneMain.setEnabledAt(0, true);
    }//GEN-LAST:event_jButtonIndietroActionPerformed

    /**
     * @brief Bottone modifica orario negozio dell'interfaccia "Menù"
     *
     * Il metodo viene richiamato alla pressione del bottone modifica orario
     * negozio dell'interfaccia "Menù" e apre l'interfaccia "Modifica Orario
     * Negozio".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonModificaOrarioNegozioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificaOrarioNegozioActionPerformed
        // TODO add your handling code here:
        // Apro il pannello modifica orario negozio
        this.jTabbedPaneImpostazioni.setSelectedComponent(this.jPanelModificaOrarioNegozio);

        this.jTabbedPaneImpostazioni.setEnabledAt(0, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(2, true);
    }//GEN-LAST:event_jButtonModificaOrarioNegozioActionPerformed

    /**
     * @brief Bottone modifica lista predefinita gusti dell'interfaccia "Menù"
     *
     * Il metodo viene richiamato alla pressione del bottone modifica lista
     * predefinita gusti dell'interfaccia "Menù" e apre l'interfaccia "Modifica
     * Lista Predefinita".
     *
     * @param evt Evento
     *
     * @see InterfacciaPrincipaleGalataio.jTabbedPaneImpostazioni
     */
    private void jButtonModificaListaPredefinitaGustiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificaListaPredefinitaGustiActionPerformed
        // TODO add your handling code here:
        // Apro il pannello modifica lista predefinita
        this.jTabbedPaneImpostazioni.setSelectedComponent(this.jPanelModificaListaPredefinita);

        this.flusher.clear();

        this.jTabbedPaneImpostazioni.setEnabledAt(0, false);
        this.jTabbedPaneImpostazioni.setEnabledAt(1, true);
    }//GEN-LAST:event_jButtonModificaListaPredefinitaGustiActionPerformed

    private void jButtonScaricaCronologiaOrdinazioniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScaricaCronologiaOrdinazioniActionPerformed
        // TODO add your handling code here:

        try {
            // TODO add your handling code here:

            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex);
        }

        Boolean keepOnTrying = true;
        Integer limit = 0;

        while (keepOnTrying && limit < 11) {
            limit++;

            // Download
            // Imposto la directory static
            this.server.setCurrentDirectory("../../../../update07/cronologia");

            // Ottengo lista di elementi nella directory del server
            // outFattorinoInDatabase
            String[] listElements = this.server.getListElements();

            ArrayList<Ordine> parsingResult;

            try {

                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./data/static/Database.xml")))) {
                    writer.print("<ordinazioni></ordinazioni>");
                }

                for (var each : listElements) {
                    // Download in locale
                    System.out.println("Download unique file.");
                    this.server.downloadFile(each, new FileOutputStream(new File("./data/supportive/outFattorinoInDatabase.xml")));

                    Thread.sleep(200);

                    // Effettua il parsing dei file XML
                    parsingResult = this.parserXMLOrdine.parseDocument("./data/supportive/outFattorinoInDatabase.xml");

                    // Appendere i risultati nel file XML Database.xml
                    this.generatorXMLOrdinev3.createDOMTree("./data/static/Database.xml", parsingResult);
                    this.generatorXMLOrdinev3.applyDOMonXMLFile("./data/static/Database.xml");
                }

                JOptionPane.showMessageDialog(null, "Download del database completato con successo.",
                        "Download database completato", JOptionPane.INFORMATION_MESSAGE);

                keepOnTrying = false;

            } catch (FileNotFoundException ex) {
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./data/static/Database.xml")))) {
                    writer.print("<ordinazioni></ordinazioni>");
                } catch (IOException ex1) {
                    Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex1);
                }

                // JOptionPane.showMessageDialog(null, "Errore durante il download del database.",
                //   "Errore download database", JOptionPane.ERROR_MESSAGE);
            } catch (ParserConfigurationException | SAXException | IOException | ParseException | TransformerException | InterruptedException ex) {
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./data/static/Database.xml")))) {
                    writer.print("<ordinazioni></ordinazioni>");
                } catch (IOException ex1) {
                    // Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(Level.SEVERE, null, ex1);
                }

                //JOptionPane.showMessageDialog(null, "Errore durante il download del database.",
                //"Errore download database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonScaricaCronologiaOrdinazioniActionPerformed

    private void jButtonEliminaCronologiaOrdinazioniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminaCronologiaOrdinazioniActionPerformed
        // TODO add your handling code here:

        // Imposto la directory static
        this.server.setCurrentDirectory("../../../../update07/cronologia");

        // Ottengo lista di elementi nella directory del server
        // outSiteInGelataio
        String[] listElements = this.server.getListElements();

        for (var each : listElements) {
            this.server.deleteFile(each);
        }

        JOptionPane.showMessageDialog(null, "Eliminazione cronologia completata con successo.",
                "Eliminazione cronologia completata", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonEliminaCronologiaOrdinazioniActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfacciaPrincipaleGalataio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new InterfacciaPrincipaleGalataio().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAggiungi;
    private javax.swing.JButton jButtonAnnulla;
    private javax.swing.JButton jButtonAnnulla2;
    private javax.swing.JButton jButtonConferma;
    private javax.swing.JButton jButtonConferma2;
    private javax.swing.JButton jButtonEliminaCronologiaOrdinazioni;
    private javax.swing.JButton jButtonImpostazioni;
    private javax.swing.JButton jButtonIndietro;
    private javax.swing.JButton jButtonIndietro1;
    private javax.swing.JButton jButtonInoltroAlFattorino;
    private javax.swing.JButton jButtonInserisci;
    private javax.swing.JButton jButtonModificaListaGustiInVendita;
    private javax.swing.JButton jButtonModificaListaPredefinitaGusti;
    private javax.swing.JButton jButtonModificaOrarioNegozio;
    private javax.swing.JButton jButtonRimuovi;
    private javax.swing.JButton jButtonRimuovi1;
    private javax.swing.JButton jButtonScaricaCronologiaOrdinazioni;
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JLabel jLabelFrammentiCronologici;
    private javax.swing.JLabel jLabelListaGustiVendita;
    private javax.swing.JLabel jLabelListaPredefinitaGusti;
    private javax.swing.JLabel jLabelListaPredefinitaGusti1;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelOraCorrente;
    private javax.swing.JLabel jLabelOraCorrente1;
    private javax.swing.JLabel jLabelOraCorrente2;
    private javax.swing.JLabel jLabelOraCorrente3;
    private javax.swing.JLabel jLabelOraCorrente5;
    private javax.swing.JLabel jLabelOraDiApertura;
    private javax.swing.JLabel jLabelOraDiChiusura;
    private javax.swing.JLabel jLabelOrdini;
    private javax.swing.JList<String> jListListaGustiVendita;
    private javax.swing.JList<String> jListListaPredefinitaGusti;
    private javax.swing.JList<String> jListListaPredefinitaGusti1;
    private javax.swing.JList<String> jListOrdini;
    private javax.swing.JPanel jPanelImpostazioni;
    private javax.swing.JPanel jPanelInterfacciaPrincipaleGelataio;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelModificaListaGustiInVendita;
    private javax.swing.JPanel jPanelModificaListaPredefinita;
    private javax.swing.JPanel jPanelModificaOrarioNegozio;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinnerOraDiApertura;
    private javax.swing.JSpinner jSpinnerOraDiChiusura;
    private javax.swing.JSpinner jSpinnerPrezzo;
    private javax.swing.JTabbedPane jTabbedPaneImpostazioni;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTextField jTextFieldGustoListaPredefinita;
    // End of variables declaration//GEN-END:variables
}
