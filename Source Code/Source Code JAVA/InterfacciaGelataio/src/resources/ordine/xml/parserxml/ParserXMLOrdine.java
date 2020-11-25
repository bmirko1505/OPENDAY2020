package resources.ordine.xml.parserxml;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file ParserXMLOrdine.java
 *
 * @brief Classe parser per i file XML con struttura "Ordine"
 */
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import resources.ordine.Ordine;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class ParserXMLOrdine
 *
 * @brief Classe parser per i file XML con struttura "Ordine"
 *
 * La seguente classe fornisce un parser di file XML con struttura "Ordine"
 */
public class ParserXMLOrdine {

    // Attributo di classe ArrayList rappresentante un
    // vettore dinamico (lista) di istanze di tipo MyClass
    /**
     * Lista di oggetti convertiti da "nodi dell'albero DOM"
     */
    private ArrayList<Ordine> parsingResult;

    private final JList jListListaPredefinitaGusti;

    // Costruttore default
    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param jSpinnerVaschettaPiccola Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaPiccola per ottenere il
     * prezzo delle vaschette piccole per procedere con il calcolo del
     * Ordine.prezzoTotale
     * @param jSpinnerVaschettaMedia Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaMedia per ottenere il
     * prezzo delle vaschette medie per procedere con il calcolo del
     * Ordine.prezzoTotale
     * @param jSpinnerVaschettaGrande Riferimento allo spinner
     * InterfacciaPrincipaleGelataio.jSpinnerVaschettaGrande per ottenere il
     * prezzo delle vaschette grandi per procedere con il calcolo del
     * Ordine.prezzoTotale
     *
     * @see ParserXMLOrdine.parsingResult
     * @see ParserXMLOrdine.jSpinnerVaschettaPiccola
     * @see ParserXMLOrdine.jSpinnerVaschettaMedia
     * @see ParserXMLOrdine.jSpinnerVaschettaGrande
     */
    public ParserXMLOrdine(JList jListListaPredefinitaGusti) {
        // Creazione istanza di classe ArrayList.
        // Vettore dinamico con dimensione iniziale: 0.
        this.parsingResult = null;
        this.jListListaPredefinitaGusti = jListListaPredefinitaGusti;
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
     * @see ParserXMLOrdine.parsingResult
     */
    public ArrayList<Ordine> parseDocument(String path) throws ParserConfigurationException, SAXException, IOException, ParseException {
        // Creazione istanza di classe ArrayList.
        // Vettore dinamico con dimensione iniziale: 0.
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
        nodelist = rootTag.getElementsByTagName("ordinazione");

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
                put(document, rootTag, singleTag);
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
     * @see ParserXMLOrdine.parsingResult
     */
    private void put(Document document, Element rootTag, Element singleTag) throws ParseException {
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
        String indirizzoDiConsegna = singleTag.getElementsByTagName("indirizzoDiConsegna").item(0).getTextContent();
        String piano = singleTag.getElementsByTagName("piano").item(0).getTextContent();
        String orarioDiConsegna = singleTag.getElementsByTagName("orarioDiConsegna").item(0).getTextContent();
        String numeroDiTelefono = singleTag.getElementsByTagName("numeroDiTelefono").item(0).getTextContent();

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // Aggiunta
        Element nodeListCibiTag = (Element) singleTag.getElementsByTagName("cibi").item(0);
        ArrayList<String> cibi = new ArrayList<>();
        String[] cibiCibo;
        NodeList nodeListCiboTag = nodeListCibiTag.getElementsByTagName("cibo");

        for (int i = 0; i < nodeListCiboTag.getLength(); i++) {
            cibi.add(nodeListCiboTag.item(i).getTextContent());
        }

        Element nodeListBevandeTag = (Element) singleTag.getElementsByTagName("bevande").item(0);
        ArrayList<String> bevande = new ArrayList<>();
        String[] bevandeBevanda;
        NodeList nodeListBevandaTag = nodeListBevandeTag.getElementsByTagName("bevanda");

        for (int i = 0; i < nodeListBevandaTag.getLength(); i++) {
            bevande.add(nodeListBevandaTag.item(i).getTextContent());
        }

        /*
        ArrayList<Vaschetta> vaschette = new ArrayList<>();
        Vaschetta[] vaschetteVaschetta;

        // Dynamic ptr movement in for loop
        ArrayList<String> gustiAsTagName5;
        String[] gustiAsTagName5String;
        String dimensioneAsTagName6;

        // Risorse
        NodeList nodeListVaschettaTag = rootTag.getElementsByTagName("vaschetta");
        NodeList nodeListGustiTag;
        NodeList nodeListGustoTag;

        for (int j = 0; j < nodeListVaschettaTag.getLength(); j++) {
            gustiAsTagName5 = new ArrayList<>();

            // Gusti
            nodeListGustiTag = ((Element) nodeListVaschettaTag.item(j)).getElementsByTagName("gusti");
            nodeListGustoTag = ((Element) nodeListGustiTag.item(0)).getElementsByTagName("gusto");
            for (int i = 0; i < nodeListGustoTag.getLength(); i++) {
                gustiAsTagName5.add(nodeListGustoTag.item(i).getTextContent());
            }

            // Dimensione
            dimensioneAsTagName6 = ((Element) nodeListVaschettaTag.item(j)).getElementsByTagName("dimensione").item(0).getTextContent();

            // Aggiungo vaschetta
            // Arrays.copyOf -> metodo utilizzato per trasformare l'array dinamico (ArrayList) gustiAsTagName5 in 
            // array raw gustiAsTagName5String effettuando nel contempo un'operazione di casting in modo da ottenere
            // come risultato il seguente tipo -> String[]
            gustiAsTagName5String = Arrays.copyOf(gustiAsTagName5.toArray(), gustiAsTagName5.size(), String[].class);
            vaschette.add(new Vaschetta(gustiAsTagName5String, dimensioneAsTagName6));
        }
         */
        // SISTEMARE ULTIMO PARAMETRO: PREZZO!!!
        // Fine della scansione di tutte le vaschette
        // Arrays.copyOf -> metodo utilizzato per trasformare l'array dinamico (ArrayList) vaschette in 
        // array raw vaschetteVaschetta effettuando nel contempo un'operazione di casting in modo da ottenere
        // come risultato il seguente tipo -> Vaschetta[]
        //vaschetteVaschetta = Arrays.copyOf(vaschette.toArray(), vaschette.size(), Vaschetta[].class);
        cibiCibo = Arrays.copyOf(cibi.toArray(), cibi.size(), String[].class);
        bevandeBevanda = Arrays.copyOf(bevande.toArray(), bevande.size(), String[].class);
        this.parsingResult.add(new Ordine(cibiCibo, bevandeBevanda, indirizzoDiConsegna, piano, numeroDiTelefono, orarioDiConsegna, this.jListListaPredefinitaGusti));
    }
}
