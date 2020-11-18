<!-- BBG -->

<!DOCTYPE html>
<html>
<title>DA MIMMO</title>
<!-- import JS -->
<script type="module" src="./js/main.js"></script>
<!-- <script src="https://use.ntpjs.org/ntp.js" async defer></script> -->
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="style/main.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<style>
  body,
  h1,
  h5 {
    font-family: "Raleway", sans-serif
  }

  body,
  html {
    height: 100%
  }

  .bgimg {
    background-image: url('./images/s.png');
    min-height: 100%;
    background-position: center;
    background-size: cover;
  }
</style>

<?php
$page = $_SERVER['PHP_SELF'];
$sec = "300";
?>

<head>
  <meta http-equiv="refresh" content="<?php echo $sec ?>;URL='<?php echo $page ?>'">
</head>

<body>
  <div class="bgimg w3-display-container w3-text-white">
    <div class="w3-display-middle w3-jumbo">
      <p><img src="images/logo/logo_small.png"></p>
    </div>
    <div class="w3-display-topleft w3-container w3-xlarge">
      <p><button onclick="document.getElementById('menu').style.display='block'" class="w3-button w3-black">Menù</button></p>
      <p><button onclick="document.getElementById('contact').style.display='block'" class="w3-button w3-black">Ordina</button></p>
    </div>
    <div class="w3-display-bottomleft w3-container">
      <p class="w3-xlarge">
        <!-- w/php&js script fill here -->

        <?php
        $xml = simplexml_load_file("../static/OrarioDelNegozio.xml");
        echo "Orario di apertura: " . "<b>" . $xml->orario->oraDiApertura . "</b>" . "<br>";
        echo "Orario di chiusura: " . "<b>" . $xml->orario->oraDiChiusura . "</b>";
        ?>


      </p>
      <p class="w3-large">Ci trovi qui: <a href="https://goo.gl/maps/GToYTfJcSrPoBPFHA" target="new">Via Santa Caterina da Siena, 3, 22066 Mariano Comense CO </a></p>
    </div>
  </div>

  <!-- Menu Modal -->
  <div id="menu" class="w3-modal">

    <div class="w3-modal-content w3-animate-zoom">


      <div class="w3-container w3-black w3-display-container">
        <span onclick="document.getElementById('menu').style.display='none'" class="w3-button w3-display-topright w3-large">x</span>
        <h1>Cibo</h1>
      </div>
      <div id="ListaGustiInVendita" class="w3-container">

        <!-- w/php&js script fill here -->
        <!--<h5>GUSTODAXML</h5>-->

        <!-- gusto = CIBO -->
        <?php
        $xml = simplexml_load_file("../static/ListaGustiInVendita.xml");
        $forEchoCibi = "";
        $forEchoBevande = "";
        foreach ($xml->children() as $prodotto) {
          if ($prodotto->tipo == "Cibo") {
            $forEchoCibi .= "<h5>" . $prodotto->gusto . ": <b>" . $prodotto->prezzo . "€</b></h5>";
          } else {
            $forEchoBevande .= "<h5>" . $prodotto->gusto . ": <b>" . $prodotto->prezzo . "€</b></h5>";
          }
        }

        echo $forEchoCibi;
        ?>

      </div>

      <div class="w3-container w3-black w3-display-container">
        <h1>Bevande</h1>
      </div>

      <!-- Prezzi vaschette = BEVANDE -->
      <div id="PrezziVaschette" class="w3-container">

        <!-- w/php&js script fill here -->
        <!--<h5>GUSTODAXML</h5>-->

        <?php
        echo $forEchoBevande;
        ?>

      </div>

    </div>
  </div>

  <!-- Contact Modal -->
  <div id="contact" class="w3-modal">
    <div class="w3-modal-content w3-animate-zoom">
      <div class="w3-container w3-black">
        <span onclick="document.getElementById('contact').style.display='none'" class="w3-button w3-display-topright w3-large">x</span>
        <h1>Ordina</h1>
      </div>
      <div class="w3-container">
        <p>Ordina online i prodotti del Bar del Rosso, infine conferma l'ordine.</p>
        <form id="informazioniPersonali">
          <p><input oninput="validate()" id="inputCognome" class="w3-input w3-padding-16 w3-border" type="text" placeholder="Classe (es. 5BI)" required name="Name"></p>

          <p class="w3-input w3-padding-16 w3-border">
            <label for="piano"> Selezionare il piano di consegna </label>
            <select id="inputPiano">
              <option value="Lotto Rosso Piano: -1 "> Lotto Rosso Piano: -1 </option>
              <option value="Lotto Rosso Piano:  Terra "> Lotto Rosso Piano: Terra </option>
              <option value="Lotto Rosso Piano:  1 "> Lotto Rosso Piano: 1 </option>
              <option value="Lotto Rosso Piano:  2 "> Lotto Rosso Piano: 2 </option>
              <option value="Lotto Rosso Piano:  3 "> Lotto Rosso Piano: 3 </option>
              <option value="Lotto Giallo Piano: -1   "> Lotto Giallo Piano: -1 </option>
              <option value="Lotto Giallo Piano:  Terra "> Lotto Giallo Piano: Terra </option>
              <option value="Lotto Giallo Piano:  1 "> Lotto Giallo Piano: 1 </option>
              <option value="Lotto Giallo Piano:  2 "> Lotto Giallo Piano: 2 </option>
              <option value="Lotto Arancione Piano: terra"> Lotto Arancione Piano: Terra</option>
              <option value="Lotto Arancione Piano: 1"> Lotto Arancione Piano: 1</option>
            </select>
          </p>


          <p><input oninput="validate()" id="inputNumeroDiTelefono" class="w3-input w3-padding-16 w3-border" type="tel" placeholder="Numero di telefono" required></p>
          <p class="w3-input w3-padding-16 w3-border">
            <?php
            $xml = simplexml_load_file("../static/OrarioDelNegozio.xml");
            echo '<label for="orario">Orario di consegna:</label><input ' .
              'oninput="validate()" id="orario" class="w3-input w3-padding-16 w3-border" type="time" required ' .
              'name="date" min="' . $xml->orario->oraDiApertura . '" max="' . $xml->orario->oraDiChiusura . '">';
            ?>

          </p>
          <!-- <p><input class="w3-input w3-padding-16 w3-border" type="" placeholder="Gusti" required name="People"></p>-->
        </form>

        <form target="print_popup" action="wow.php" method="post" id="informazioniArticoli" onsubmit="window.open('about:blank','print_popup','width=1000,height=800'); refresh()">
          <p class="w3-input w3-padding-16 w3-border">
            <label for="gusti">Selezionare un prodotto da mangiare:</label>
            <select id="gusti">

              <!-- w/php&js script fill here -->
              <!-- <option value="XYZ">XYZ</option> -->

              <?php
              $xml = simplexml_load_file("../static/ListaGustiInVendita.xml");
              $forComboboxCibi = "";
              $forComboboxBevande = "";
              foreach ($xml->children() as $prodotto) {
                if ($prodotto->tipo == "Cibo") {
                  // $forComboboxCibi .= "<h5>" . $prodotto->gusto . ": <b>" . $prodotto->prezzo . "€</b></h5>";
                  $forComboboxCibi .= '<option value="' . $prodotto->gusto . ": <b>" . $prodotto->prezzo . "€</b>" . '">' . $prodotto->gusto . ": <b>" . $prodotto->prezzo . '€</b></option>';
                } else {
                  $forComboboxBevande .= '<option value="' . $prodotto->gusto . ": <b>" . $prodotto->prezzo . "€</b>" . '">' . $prodotto->gusto . ": <b>" . $prodotto->prezzo . '€</b></option>';
                }
              }

              echo $forComboboxCibi;
              ?>
            </select>
            <input type="button" class="w3-button" value="AGGIUNGI CIBO" onclick="aggiungiCibo()">
            <input type="button" class="w3-button" value="RIMUOVI ULTIMO CIBO" onclick="rimuoviUltimoGusto()">
            <br>
            <span id="showArrayGusti">
              <!-- js auto listing here -->
            </span>
          </p>

          <p hidden>
            <input type="text" name="outputXMLPHP" id="inputXMLJS">
          </p>

          <p class="w3-input w3-padding-16 w3-border">
            <label for="bevande">Selezionare una bevanda:</label>
            <select id="bevande">

              <!-- w/php&js script fill here -->
              <!-- <option value="XYZ">XYZ</option> -->

              <?php
              echo $forComboboxBevande;
              ?>
            </select>
            <input type="button" class="w3-button" value="AGGIUNGI BEVANDA" onclick="aggiungiBevanda()">
            <input type="button" class="w3-button" value="RIMUOVI ULTIMA BEVANDA" onclick="rimuoviUltimaBevanda()">
            <br>
            <span id="showArrayBevande">
              <!-- js auto listing here -->
            </span>
          </p>

          <p class="w3-input w3-padding-16 w3-border" id="costoLive">
          </p>

            <!--
            <p class="w3-input w3-padding-16 w3-border">
              <label for="dimensione">Scegli la dimensione:</label>
              <select onchange="recheckLimiti()" id="dimensione">
                <option value="PICCOLA">Piccola</option>
                <option value="MEDIA">Media</option>
                <option value="GRANDE">Grande</option>
              </select>
            </p>

            <p class="w3-input w3-padding-16 w3-border">
              Vaschette:
              <br>
              <span id="showArrayVaschette">
                js auto listing here 
              </span>
            </p>   
			
			-->

            <p>
              <!--
              <input type="button" class="w3-button" value="AGGIUNGI VASCHETTA" onclick="aggiungiVaschetta()">
              <input type="button" class="w3-button" value="RIMUOVI ULTIMA VASCHETTA" onclick="rimuoviUltimaVaschetta()">
			  -->
              <span id="inviaOrdineButton">
                <input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">
              </span>
            </p>

        </form>

        <!-- TEST DATA -->
        <!-- <script>
          var serverDate = null;
          console.log(new Date(serverDate));
          var serverDate2 = new Date(serverDate + 1.59961e+12);
          console.log(`${serverDate2.getDate()}/${serverDate2.getMonth()}/${serverDate2.getFullYear()}`);

          // var dateDiff = new Date(new Date() - serverDate);
          // console.log(`${dateDiff.getDate()}/${dateDiff.getMonth()}/${dateDiff.getFullYear()}`);
        </script> -->

        <div>
          <p id="universalTime" hidden>
            <?php
            $date_utc = new \DateTime("now", new \DateTimeZone("UTC"));
            echo $date_utc->format(\DateTime::RFC850); # Saturday, 18-Apr-15 03:23:46 UTC
            ?></p>
        </div>
      </div>
    </div>
  </div>
<script type="module">initCostoLive()</script>
</body>

</html>