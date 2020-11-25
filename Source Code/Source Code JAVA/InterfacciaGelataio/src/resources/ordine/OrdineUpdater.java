package resources.ordine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file OrdineUpdater.java
 *
 * @brief Classe per garantire un aggiornamento automatico e continuo della
 * lista delle ordinazioni in arrivo
 */
import guiGelataio.InterfacciaPrincipaleGalataio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import resources.ftpserver.FTPServer;
import resources.ordine.xml.generatorxml.GeneratorXMLOrdine;
import resources.ordine.xml.parserxml.ParserXMLOrdine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class OrdineUpdater
 *
 * @brief Classe per garantire un aggiornamento automatico e continuo della
 * lista delle ordinazioni in arrivo
 *
 * La seguente classe rappresenta un thread daemon per garantire un
 * aggiornamento automatico e continuo della lista delle ordinazioni in arrivo.
 */
public class OrdineUpdater extends TimerTask {

    /**
     * Rappresenta il parser del file XML contenente la struttura "Ordine"
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @see resources.ordine.Ordine
     * @see resources.ordine.xml.parserxml.ParserXMLOrdine
     */
    private final ParserXMLOrdine parserXMLOrdine;

    /**
     * Rappresenta il modello lista della lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     *
     * @see InterfacciaPrincipaleGalataio.jListListaGustiVendita
     */
    private final DefaultListModel listaOrdiniModel;

    /**
     * Rappresenta l'oggetto di classe FTPServer per permettere la comunicazione
     * bidirezionale con il server FTP
     *
     * @see resources.ftpserver.FTPServer
     */
    private final FTPServer server;

    /**
     * Lista di oggetti convertiti da "nodi dell'albero DOM"
     */
    private ArrayList<Ordine> parsingResult;
    private final GeneratorXMLOrdine generatorXMLOrdine;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param server L'oggetto di classe FTPServer per permettere la
     * comunicazione bidirezionale con il server FTP
     * @param parserXMLOrdine Il parser del file XML contenente la struttura
     * "Ordine"
     * @param listaOrdiniModel Il modello lista della lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita
     * @param generatorXMLOrdine Il generator del file XML contenente la
     * struttura "Ordine"
     *
     * @see OrdineUpdater.server
     * @see OrdineUpdater.parserXMLOrdine
     * @see OrdineUpdater.listaOrdiniModel
     * @see OrdineUpdater.parsingResult
     */
    public OrdineUpdater(FTPServer server, ParserXMLOrdine parserXMLOrdine, DefaultListModel listaOrdiniModel,
            GeneratorXMLOrdine generatorXMLOrdine) {
        this.server = server;
        this.parserXMLOrdine = parserXMLOrdine;
        this.listaOrdiniModel = listaOrdiniModel;
        this.parsingResult = null;
        this.generatorXMLOrdine = generatorXMLOrdine;
    }

    /**
     * @brief Codice eseguito dal thread
     *
     * Il seguente metodo controlla l'eventuale presenza di nuove ordinazioni
     * nel server in attesa di essere scaricate e caricate localmente nella
     * lista InterfacciaPrincipaleGalataio.jListListaGustiVendita. Se presenti,
     * le scarica, le carica nella lista
     * InterfacciaPrincipaleGalataio.jListListaGustiVendita e infine le elimina
     * dal server.
     *
     * @see OrdineUpdater.server
     * @see OrdineUpdater.parserXMLOrdine
     * @see OrdineUpdater.listaOrdiniModel
     * @see OrdineUpdater.parsingResult
     */
    @Override
    public void run() {
        String doppioBipolo = null;

        try {
            Boolean isChanged = false;

            // Imposto la directory outSiteInGelataio
            this.server.setCurrentDirectory("../../../../update07/outSiteInGelataio");

            // Ottengo lista di elementi nella directory del server
            // outSiteInGelataio
            String[] listElements = this.server.getListElements();

            for (var each : listElements) {
                doppioBipolo = each;

                isChanged = true;

                // Download in locale
                System.out.println("Download unique file.");
                this.server.downloadFile(each, new FileOutputStream(new File("./data/supportive/outSiteInGelataio.xml")));

                // Effettua il parsing dei file XML
                this.parsingResult = this.parserXMLOrdine.parseDocument("./data/supportive/outSiteInGelataio.xml");

                // Inserisco i risultati nel modello della lista listaOrdiniModel
                this.listaOrdiniModel.addAll(this.parsingResult);

                // Elimino il file dal server
                this.server.deleteFile(each);
            }

            if (isChanged) {
                ArrayList<Ordine> ordini = new ArrayList<>();
                for (int i = 0; i < this.listaOrdiniModel.getSize(); i++) {
                    ordini.add((Ordine) this.listaOrdiniModel.getElementAt(i));
                }

                this.generatorXMLOrdine.createDOMTree(ordini);
                this.generatorXMLOrdine.applyDOMonXMLFile("./data/static/OrdiniNonEvasi.xml");

                // Upload
                this.server.setCurrentDirectory("../../../../update07/static");
                this.server.uploadFile("OrdiniNonEvasi.xml", new FileInputStream(new File("./data/static/OrdiniNonEvasi.xml")));
            }

        } catch (ParserConfigurationException | SAXException | IOException | ParseException | TransformerException ex) {
            this.server.deleteFile(doppioBipolo);
            Logger.getLogger(OrdineUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
