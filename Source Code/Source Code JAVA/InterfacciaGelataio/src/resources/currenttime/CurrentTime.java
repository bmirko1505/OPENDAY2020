package resources.currenttime;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @version 1.0
 * @file CurrentTime.java
 *
 * @brief Classe per mantenere "corrente" e visualizzare correttamente l'ora
 * nelle interfacce del software
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;
import javax.swing.JLabel;

/**
 * @author Grandieri Andrea g.andreus02@gmail.com
 * @author Barsotti Mirko bmirko1505@gmail.com
 * @author Brambilla Mirko brambobilla01@gmail.com
 * @class CurrentTime
 *
 * @brief Classe per mantenere "corrente" e visualizzare correttamente l'ora
 * nelle interfacce del software
 *
 * La seguente classe rappresenta un thread daemon per mantenere sincronizzata
 * l'ora visualizzata nelle interfacce del software con l'ora del sistema
 * operativo. Inoltre, permette di visualizzare in modo corretto quest'ultima.
 */
public class CurrentTime extends TimerTask {

    /**
     * Rappresenta un oggetto di classe Calendar per ottenere l'ora corrente del
     * sistema operativo
     */
    private Calendar calendar;

    /**
     * Rappresenta un oggetto di classe SimpleDateFormat per formattare
     * l'informazione ottenuta dall'oggetto CurrentTime.calendar per permettere
     * una corretta visualizzazione dell'ora corrente nelle interfacce del
     * software
     *
     * @see CurrentTime.calendar
     */
    private final SimpleDateFormat simpleDateFormat;

    /**
     * Rappresenta una zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     */
    private final JLabel jLabelOraCorrente;

    /**
     * Rappresenta una zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     */
    private final JLabel jLabelOraCorrente5;

  

    /**
     * Rappresenta una zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     */
    private final JLabel jLabelOraCorrente1;

    /**
     * Rappresenta una zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     */
    private final JLabel jLabelOraCorrente2;

    /**
     * Rappresenta una zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     */
    private final JLabel jLabelOraCorrente3;

    /**
     * @brief Costruttore
     *
     * Inizializza tutte le risorse necessarie ad un oggetto di questa classe
     *
     * @param jLabelOraCorrente Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     * @param jLabelOraCorrente5 Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     * @param jLabelOraCorrente4 Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     * @param jLabelOraCorrente1 Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     * @param jLabelOraCorrente2 Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     * @param jLabelOraCorrente3 Zona di visualizzazione dell'ora corrente nelle
     * interfacce del software
     *
     * @see CurrentTime.jLabelOraCorrente
     * @see CurrentTime.jLabelOraCorrente5
     * @see CurrentTime.jLabelOraCorrente4
     * @see CurrentTime.jLabelOraCorrente1
     * @see CurrentTime.jLabelOraCorrente2
     * @see CurrentTime.jLabelOraCorrente3
     */
    public CurrentTime(JLabel jLabelOraCorrente,
            JLabel jLabelOraCorrente5,
            JLabel jLabelOraCorrente1,
            JLabel jLabelOraCorrente2,
            JLabel jLabelOraCorrente3
    ) {
        this.calendar = null;
        this.simpleDateFormat = new SimpleDateFormat("HH:mm");

        this.jLabelOraCorrente = jLabelOraCorrente;
        this.jLabelOraCorrente1 = jLabelOraCorrente1;
        this.jLabelOraCorrente2 = jLabelOraCorrente2;
        this.jLabelOraCorrente3 = jLabelOraCorrente3;
        this.jLabelOraCorrente5 = jLabelOraCorrente5;
    }

    /**
     * @brief Codice eseguito dal thread
     *
     * Il seguente metodo ottiene l'ora corrente dal sistema operativo, la
     * formatta e la visualizza nelle rispettive interfacce
     *
     * @see CurrentTime.jLabelOraCorrente
     * @see CurrentTime.jLabelOraCorrente5
     * @see CurrentTime.jLabelOraCorrente4
     * @see CurrentTime.jLabelOraCorrente1
     * @see CurrentTime.jLabelOraCorrente2
     * @see CurrentTime.jLabelOraCorrente3
     * @see CurrentTime.calendar
     * @see CurrentTime.simpleDateFormat
     */
    @Override
    public void run() {
        this.calendar = Calendar.getInstance();
        jLabelOraCorrente.setText(simpleDateFormat.format(calendar.getTime()));
        jLabelOraCorrente5.setText(simpleDateFormat.format(calendar.getTime()));
       
        jLabelOraCorrente2.setText(simpleDateFormat.format(calendar.getTime()));
        jLabelOraCorrente1.setText(simpleDateFormat.format(calendar.getTime()));
        jLabelOraCorrente3.setText(simpleDateFormat.format(calendar.getTime()));

    }
}
