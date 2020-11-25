package resources.ordine.xml.generatorxml;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file GeneratorXMLOrdine.java
 *
 * @brief Classe generator per i file XML con struttura "Ordine"
 */
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import resources.ordine.Ordine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class GeneratorXMLOrdine
 *
 * @brief Classe generator per i file XML con struttura "Ordine"
 *
 * La seguente classe fornisce un generatore di file XML con struttura "Ordine"
 */
public class GeneratorXMLOrdine {

    // Attributo di classe Document rappresentante il documento
    // "virtuale" sul quale andremo a creare il nostro albero DOM.
    // Il documento è classificato come "virtuale" in quanto non rappresenta
    // un vero e proprio file fisico ma solamente una struttura dati temporanea
    // allocata in memoria RAM (spazio dedicato a questo programma).
    // Ogni dato contenuto in questo documento "virtuale" verrà perso al termine
    // di questo programma.
    /**
     * Rappresenta il documento "virtuale" sul quale andremo a creare il nostro
     * albero DOM
     */
    private Document document;

    // Costruttore di default
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @see GeneratorXMLOrdine.document
     */
    public GeneratorXMLOrdine() {
        // Document is abstract
        // Nessuna inizializzazione. Per una inizializzazione
        // corretta fare riferimento al metodo createDOMTree
        this.document = null;
    }

    // Crea il documento "virtuale" supporto per il nostro albero DOM
    /**
     * @brief Crea il documento "virtuale" supporto per il nostro albero DOM
     *
     * @throws ParserConfigurationException
     *
     * @see GeneratorXMLOrdine.document
     */
    private void createDocument() throws ParserConfigurationException {
        // Le istanze di classi:
        // - DocumentBuilderFactory
        // - DocumentBuilder
        // - Document
        // servono per istanziare l'oggetto document di classe Document
        // Questo oggetto può essere visto come una sorta di rappresentazione "virtuale" di un file XML
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    // Crea l'albero DOM
    /**
     * @brief Crea l'albero DOM
     *
     * @param data Lista di oggetti da convertire in "nodi dell'albero DOM"
     *
     * @throws ParserConfigurationException
     *
     * @see GeneratorXMLOrdine.createDocument()
     * @see GeneratorXMLOrdine.document
     */
    public void createDOMTree(ArrayList<Ordine> data) throws ParserConfigurationException {
        // Creo il documento "virtuale" supporto per l'albero DOM
        this.createDocument();

        // Le istanze di classe:
        // - Element
        // rappresentano gli elementi del documento XML
        // L'istanza 'rootTag' rappresenterà la radice del documento XML (cioè il tag che contiene
        // tutti gli altri tag)
        // L'istanza 'singleTag' rappresenterà (ad uno ad uno) tutti gli altri tag del file XML (contenuti
        // all'interno del "tag di root")
        Element rootTag;
        Element singleTag;

        // Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)
        rootTag = document.createElement("ordinazioni");

        // Aggiungo questo tag/elemento al mio albero DOM.
        // Per fare questo utilizzo il metodo appendChild che viene fornito dal supporto
        // del nostro albero DOM, cioè il documento "virtuale" sopracitato.
        document.appendChild(rootTag);

        // Scorro tutto il vettore dinamico (lista) contenente le istanze
        // di classe ""MyClass"" da aggiungere all'albero DOM.
        // Per ognuna di esse (istanza) effettuo una operazione di "estrazione delle informazioni".
        // Questo mi permetterà di creare/COSTRUIRE tutti i tag necessari con il corretto nome e contenenti la
        // corretta informazione. Per applicare queste operazioni utilizzo il metodo get di questa classe.
        for (var each : data) {
            // Salvo il tag/elemento XML risultante dall'operazione di get 
            // realizzata con la chiamata al metodo get di questa classe
            singleTag = get(each);

            // Aggiungo questo tag/elemento al mio tag/elemento root.
            // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
            // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".  
            rootTag.appendChild(singleTag);
        }
    }

    // "Estrae le informazioni" dall'attributo singleElement di classe MyClass
    // e costruisce un rispettivo tag/elemento XML associato.
    /**
     * @brief Converte un oggetto in "nodo dell'albero DOM"
     *
     * @param singleElement L'oggetto da convertire in "nodo dell'albero DOM"
     *
     * @return Il nodo dell'albero DOM (L'oggetto convertito in)
     *
     * @see GeneratorXMLOrdine.document
     */
    private Element get(Ordine singleElement) {
        // L'elemento/tag XML risultato di questa operazione
        Element getOperationResult = document.createElement("ordinazione");

        // Elemento di appoggio per supportare diversi "layer"/"fasi" di 
        // "estrazione delle informazioni"
        Element supportiveTag;

        // Elemento di classe Text utilizzato per aggiungere una informazione 
        // testuale ad un tag/elemento XML.       
        Text text;

        // Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)        
        supportiveTag = document.createElement("indirizzoDiConsegna");

        // Attraverso l'uso del metodo createTextNode creo un elemento testuale che potrà
        // essere inserito all'interno di un preesistente tag/elemento XML.
        // Il testo è specificato dall'unico parametro String specificato al metodo in questione.
        text = document.createTextNode(singleElement.getIndirizzoConsegna());

        // Aggiungo il testo al tag/elemento XML prima creato (supportiveTag).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
        supportiveTag.appendChild(text);

        // Aggiungo questo tag/elemento al mio tag/elemento XML risultante di questa operazione
        // (quello che ritornerò).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".       
        getOperationResult.appendChild(supportiveTag);

        /////////////////////////////////////////////////////////////////////////////////////////// 
        // Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)        
        supportiveTag = document.createElement("piano");

        // Attraverso l'uso del metodo createTextNode creo un elemento testuale che potrà
        // essere inserito all'interno di un preesistente tag/elemento XML.
        // Il testo è specificato dall'unico parametro String specificato al metodo in questione.
        text = document.createTextNode(singleElement.getPiano());

        // Aggiungo il testo al tag/elemento XML prima creato (supportiveTag).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
        supportiveTag.appendChild(text);

        // Aggiungo questo tag/elemento al mio tag/elemento XML risultante di questa operazione
        // (quello che ritornerò).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".       
        getOperationResult.appendChild(supportiveTag);

        ///////////////////////////////////////////////////////////////////////////////////////////
        // Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)        
        supportiveTag = document.createElement("orarioDiConsegna");

        // Attraverso l'uso del metodo createTextNode creo un elemento testuale che potrà
        // essere inserito all'interno di un preesistente tag/elemento XML.
        // Il testo è specificato dall'unico parametro String specificato al metodo in questione.
        text = document.createTextNode(singleElement.getOrarioConsegna());

        // Aggiungo il testo al tag/elemento XML prima creato (supportiveTag).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
        supportiveTag.appendChild(text);

        // Aggiungo questo tag/elemento al mio tag/elemento XML risultante di questa operazione
        // (quello che ritornerò).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".       
        getOperationResult.appendChild(supportiveTag);

        ///////////////////////////////////////////////////////////////////////////////////////////    
// Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)        
        supportiveTag = document.createElement("numeroDiTelefono");

        // Attraverso l'uso del metodo createTextNode creo un elemento testuale che potrà
        // essere inserito all'interno di un preesistente tag/elemento XML.
        // Il testo è specificato dall'unico parametro String specificato al metodo in questione.
        text = document.createTextNode(singleElement.getNumeroTelefonico());

        // Aggiungo il testo al tag/elemento XML prima creato (supportiveTag).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
        supportiveTag.appendChild(text);

        // Aggiungo questo tag/elemento al mio tag/elemento XML risultante di questa operazione
        // (quello che ritornerò).
        // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
        // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".       
        getOperationResult.appendChild(supportiveTag);

        /////////////////////////////////////////////////////////////////////////////////////////// 
        //////////////////////////////////////////////////////////////////////////////////////////
        // Risorse
        Element supportiveTag2;

        // Attraverso l'uso del metodo createElement creo un tag che ha come nome
        // l'unico parametro String specificato al metodo in questione.
        // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)        
        supportiveTag = document.createElement("cibi");

        for (String cibo : singleElement.getCibi()) {
            supportiveTag2 = document.createElement("cibo");
            text = document.createTextNode(cibo);
            supportiveTag2.appendChild(text);
            supportiveTag.appendChild(supportiveTag2);
        }

        getOperationResult.appendChild(supportiveTag);

        supportiveTag = document.createElement("bevande");

        for (String bevanda : singleElement.getBevande()) {
            supportiveTag2 = document.createElement("bevanda");
            text = document.createTextNode(bevanda);
            supportiveTag2.appendChild(text);
            supportiveTag.appendChild(supportiveTag2);
        }

        getOperationResult.appendChild(supportiveTag);
        ///////////////////////////////////////////////////////////////////////////////////////////  

        /*
        for (String cibo : singleElement.getCibi()) {
            // Attraverso l'uso del metodo createElement creo un tag che ha come nome
            // l'unico parametro String specificato al metodo in questione.
            // Il tag XML ottenuto è di tipo Element (infatti un tag XML è un elemento XML)
            supportiveTag = document.createElement("vaschetta");
            // Creo un secondo tag @Child di "vaschetta"
            atChildVaschetta = document.createElement("gusti");
            // Aggiungo tanti tag "gusto" quanti sono i gusti
            for (java.lang.String each : vaschette.getCibi()) {
                atChildGusti = document.createElement("gusto");
                // Attraverso l'uso del metodo createTextNode creo un elemento testuale che potrà
                // essere inserito all'interno di un preesistente tag/elemento XML.
                // Il testo è specificato dall'unico parametro String specificato al metodo in questione.
                text = document.createTextNode(each);

                // Aggiungo il testo al tag/elemento XML prima creato (atChildGusti).
                // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
                // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
                atChildGusti.appendChild(text);

                // Aggiungo questo tag/elemento al mio tag/elemento XML risultante di questa operazione
                // (quello che ritornerò).
                // Per fare questo utilizzo il metodo appendChild che viene fornito dalla classe Element,
                // cioè che viene fornito dallo stesso tag/elemento XML per "farsi riempire".
                atChildVaschetta.appendChild(atChildGusti);
            }
            supportiveTag.appendChild(atChildVaschetta);
            // Aggiungi dimensione
            atChildVaschetta2 = document.createElement("dimensione");
            text = document.createTextNode(vaschette.getDimensioni().toString());
            atChildVaschetta2.appendChild(text);
            supportiveTag.appendChild(atChildVaschetta2);

            // As usually
            getOperationResult.appendChild(supportiveTag);
        }
         */
        ///////////////////////////////////////////////////////////////////////////////////////////          
        // Ritorno il tag/elemento XML risultante di questa operazione.
        return getOperationResult;
    }

    // Trasforma il documento "virtuale" in effettivo file XML "fisico" (creandolo)
    /**
     * @brief Crea l'effettivo file XML sulla base dell'albero DOM
     *
     * @param filenameNpath Path del file XML di salvataggio
     *
     * @throws TransformerConfigurationException
     * @throws TransformerException
     *
     * @see GeneratorXMLOrdine.document
     */
    public void applyDOMonXMLFile(String filenameNpath) throws TransformerConfigurationException, TransformerException {
        // Per trasformare il documento "virtuale" sul quale è presente l'albero DOM in un file XML "vero e proprio",
        // necessito di 3 risorse:
        // - trasformatore/convertitore: colui che eseguirà l'effettiva conversione da documento "virtuale" a file XML "vero e proprio"
        // - source -> documento "virtuale": il documento "virtuale" che fa da supporto all'albero DOM. In dettaglio ho bisogno solo dell'albero DOM 
        // contenuto/"appoggiato" su questo documento "virtuale"
        // - target -> il file XML "vero e proprio": il file XML "vero e proprio" nel quale scrivere l'albero DOM convertito.
        // Se il file XML "vero e proprio" già esiste, allora si andrà solamente a scrivere in esso
        // Se il file XML "vero e proprio" NON esiste, allora verrà creato per poi andare a scrivere in esso

        // Creo un'istanza di classe Transformer che rappresenta il mio trasformatore/convertitore.
        // La conversione segue: [documento "virtuale"{albero DOM}] ---> [file XML "vero e proprio"]
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        // Creo un'istanza di classe DOMSource che rappresenta la mia source.
        // Non posso utilizzare direttamente il documento "virtuale" come source per la trasformazione/conversione,
        // ma devo "estrarre" l'albero DOM, che è la vera informazione di cui necessito.
        DOMSource source = new DOMSource(this.document);

        // Creo un'istanza di classe StreamResult che rappresenta l'handler (colui che mi 
        // permette di relazionarmi con) il mio target.
        // A questo handler fornisco come parametro la path+nome del file "vero e proprio" sul quale operare.
        // Se il file "vero e proprio" già esiste, allora si andrà solamente a scrivere in esso.
        // Se il file "vero e proprio" NON esiste, allora verrà creato per poi andare a scrivere in esso.     
        StreamResult stream = new StreamResult(new File(filenameNpath));

        // Eseguo la trasformazione/conversione effettiva attraverso il metodo transform
        // della classe Transformer
        transformer.transform(source, stream);
    }
}
