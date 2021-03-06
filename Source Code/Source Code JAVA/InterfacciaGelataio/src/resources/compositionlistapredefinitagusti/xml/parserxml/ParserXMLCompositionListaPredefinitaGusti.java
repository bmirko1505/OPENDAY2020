package resources.compositionlistapredefinitagusti.xml.parserxml;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file ParserXMLCompositionListaPredefinitaGusti.java
 *
 * @brief Classe parser per i file XML con struttura
 * "CompositionListaPredefinitaGusti"
 */
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import resources.compositionlistapredefinitagusti.CompositionListaPredefinitaGusti;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class ParserXMLCompositionListaPredefinitaGusti
 *
 * @brief Classe parser per i file XML con struttura
 * "CompositionListaPredefinitaGusti"
 *
 * La seguente classe fornisce un parser di file XML con struttura
 * "CompositionListaPredefinitaGusti"
 */
public class ParserXMLCompositionListaPredefinitaGusti {

    // Attributo di classe ArrayList rappresentante un
    // vettore dinamico (lista) di istanze di tipo MyClass
    /**
     * Lista di oggetti convertiti da "nodi dell'albero DOM"
     */
    private ArrayList<CompositionListaPredefinitaGusti> parsingResult;

    // Costruttore default
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @see ParserXMLCompositionListaPredefinitaGusti.parsingResult
     */
    public ParserXMLCompositionListaPredefinitaGusti() {
        // Creazione istanza di classe ArrayList.
        // Vettore dinamico con dimensione iniziale: 0.
        this.parsingResult = null;
    }

    // Metodo per effettuare il parsing del documento
    // XML di cui viene specificato il path
    /**
     * @brief Effettua il parsing del documento XML di cui viene specificato il
     * path
     *
     * @param path Path del file XML del quale effettuare il parsing
     *
     * @return Lista di oggetti convertiti da "nodi dell'albero DOM"
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     *
     * @see ParserXMLCompositionListaPredefinitaGusti.parsingResult
     */
    public ArrayList<CompositionListaPredefinitaGusti> parseDocument(String path) throws ParserConfigurationException, SAXException, IOException, ParseException {
        this.parsingResult = new ArrayList<>(0);

        // Le istanze di classi:
        // - DocumentBuilderFactory
        // - DocumentBuilder
        // servono per istanziare l'oggetto document di classe Document.
        // Questo oggetto rappresenta il documento "virtuale" sul quale andremo a creare il nostro albero DOM.
        // Il documento è classificato come "virtuale" in quanto non rappresenta
        // un vero e proprio file fisico ma solamente una struttura dati temporanea
        // allocata in memoria RAM (spazio dedicato a questo programma).
        // Ogni dato contenuto in questo documento "virtuale" verrà perso al termine
        // di questo programma.
        // L'albero DOM sarà una rappresentazione "virtuale" del file XML "fisico" del quale
        // stiamo effettuando il parsing.
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);

        // Le istanze di classe:
        // - Element
        // rappresentano gli elementi del documento XML
        // L'istanza 'rootTag' rappresenterà la radice del documento XML (cioè il tag che contiene
        // tutti gli altri tag)
        // L'istanza 'singleTag' rappresenterà (ad uno ad uno) tutti gli altri tag del file XML (contenuti
        // all'interno del "tag di root")
        Element rootTag;
        Element singleTag;

        // Istanza di classe NodeList con lo scopo di permettere LA NAVIGAZIONE dell'albero DOM del
        // documento XML
        NodeList nodelist;

        // Assegno il "tag di root" all'istanza 'rootTag'
        // Il metodo "getDocumentElement()" restituisce il nodo radice del
        // documento XML
        rootTag = document.getDocumentElement();

        // Assegno la LISTA DEI NODI all'istanza 'nodelist'
        // Questo significa salvare UNA LISTA DI TAG
        // Questo necessiterà specificare la TIPOLOGIA DI TAG
        // di cui si vuole creare una lista
        // Per procedere, si utilizza il metodo "getElementsByTagName()"
        // Come già spiegato prima, il metodo ritorna una LISTA DI TAG DEL
        // NOME SPECIFICATO COME PARAMETRO
        nodelist = rootTag.getElementsByTagName("prodotto");

        // Adesso SCORRO la LISTA DI NODI, lavorando ELEMENTO PER ELEMENTO
        // Procedo solo dopo essermi accertato che il metodo "getElementsByTagName()"
        // ha restituito un valore valido (una lista valida)
        if (nodelist != null) {
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Man mano che procedo con l'iterazione,
                // analizzo SINGOLARMENTE TAG PER TAG contenuti
                // nella LISTA DI NODI ricavata prima.
                // Il metodo "item()" mi permette di ottenere un SINGOLO NODO
                // contenuto nella LISTA DI NODI.
                // Il tipo di ritorno è 'Node', ma noi vogliamo lavorare
                // con istanze di tipo 'Element', il tipo che rispecchia 
                // un singolo TAG (chiamato anche ELEMENTO) di un file XML.
                // Per risolvere questa incorrispondeza, basterà effettuare un CAST
                // da 'Node' a 'Element'
                singleTag = (Element) nodelist.item(i);

                // Adesso, partendo da questo SINGOLO ELEMENTO/TAG, che rappresenta
                // (in questo caso) un singolo ""MyClass"", costruisco una nuova istanza
                // MyClass, "riempita" con le informazioni ricavate dall'ELEMENTO/TAG in analisi.
                // Successivamente aggiungo l'istanza di classe MyClass appena creata nel vettore dinamico
                // di istanze di classe MyClass.
                // Per fare questo, riferisco ad un metodo privato di questa classe (Parser) --> 'put'
                put(singleTag);
            }
        }

        return this.parsingResult;
    }

// Il metodo 'put' va ridefinito ogni volta che cambia il tipo di TAG che si sta trattando
// In questo caso, stiamo trattando TAG -> 'MyClass'
    /**
     * @brief Converte un "nodo dell'albero DOM" in oggetto
     *
     * @param singleTag Nodo dell'albero DOM da convertire in oggetto
     *
     * @throws ParseException
     *
     * @see ParserXMLCompositionListaPredefinitaGusti.parsingResult
     */
    private void put(Element singleTag) throws ParseException {
        // Si utilizzano i seguenti metodi per ottenere le varie
        // informazioni contenute all'interno del SINGOLO TAG (passato
        // come parametro)
        // - getAttribute(attributeName)
        // Ritorna il valore dell'attributo che ha come nome 'attributeName'
        // L'attributo è ovviamente riferito al TAG sul quale si sta richiamando questo metodo
        // esempio -> String value = singleTag.getAttribute(name);
        // - getElementsByTagName(tagName)
        // Già utilizzato precedentemente, questo metodo ritorna una LISTA DI NODI del tipo (nome) specificato
        // come parametro. In questo caso, stiamo ottenendo UNA LISTA DI UN SOLO ELEMENTO -> questa informazioni la si ricava
        // GUARDANDO il file XML per il quale stiamo realizzando questo parser --> questo è il motivo per il quale si specifica
        // manualmente 'item(0)'.
        // - getTextContent()
        // Ritorna, come STRINGA (type: String), il valore contenuto all'interno dell'ELEMENTO/TAG in analisi.          
        String tagName1 = singleTag.getElementsByTagName("gusto").item(0).getTextContent();
        String tagName2 = singleTag.getElementsByTagName("tipo").item(0).getTextContent();
        Float tagName3 = Float.parseFloat(singleTag.getElementsByTagName("prezzo").item(0).getTextContent());

        // Aggiunta al vettore dinamico
        // Usa la classe SimpleDateFormat per gestire le date (da String a Date)
        this.parsingResult.add(new CompositionListaPredefinitaGusti(tagName1, tagName2, tagName3));
    }
}
