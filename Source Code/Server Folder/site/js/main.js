// BBG

// Risorse esterne
import * as utils from "./utils.js";

// Classi
class Vaschetta {
    gusti  /*Array*/
    dimensione

    bevande /*Array*/

    constructor(gusti, dimensione, bevande) {
        this.gusti = gusti;
        this.bevande = bevande;
        this.dimensione = dimensione;
    }

    get getGusti() { return this.gusti; }
    get getDimensione() { return this.dimensione; }
    get getBevande() { return this.bevande; }

    set setGusti(gusti) { this.gusti = gusti; }
    set setDimensione(dimensione) { this.dimensione = dimensione; }
    set setBevande(bevande) { this.bevande = bevande; }

    toString() {
        return `{Gusti: ${this.gusti}}` + `\n{Dimensione: ${this.dimensione}}`;
    }
}
globalThis.Vaschetta = Vaschetta;


class Ordinazione {
    //classe di consegna
    cognome
    //piano di consegna
    piano
    numeroTelefonico
    orarioDiConsegna
    //inutilizzato
    gusti  /*Array*/
    bevande	 /*Array*/

    constructor(cognome, numeroTelefonico, piano, orarioDiConsegna, gusti, bevande) {
        this.cognome = cognome;
        this.numeroTelefonico = numeroTelefonico;
        this.piano = piano;
        this.orarioDiConsegna = orarioDiConsegna;
        this.gusti = gusti;
        this.bevande = bevande;
    }

    get getCognome() { return this.cognome; }
    get getNumeroTelefonico() { return this.numeroTelefonico; }
    get getPiano() { return this.piano; }
    get getOrarioDiConsegna() { return this.orarioDiConsegna; }
    get getGusti() { return this.gusti; }
    get getBevande() { return this.bevande; }

    set setCognome(cognome) { this.cognome = cognome; }
    set setNumeroTelefonico(numeroTelefonico) { this.numeroTelefonico = numeroTelefonico; }
    set setPiano(piano) { this.piano = piano; }
    set setOrarioDiConsegna(orarioDiConsegna) { this.orarioDiConsegna = orarioDiConsegna; }
    set setGusti(gusti) { this.gusti = gusti; }
    set setBevande(bevande) { this.bevande = bevande; }

}
globalThis.Ordinazione = Ordinazione;

// Variabili globali
var ordinazione = null;
var vaschette = [];
var vaschetteCopy = null;

var gusti = [];
var gustiCopy = null;

var bevande = [];
var bevandeCopy = null;

var costoLive = 0.0;

function initCostoLive() {
    utils.clearAndInject("costoLive", "Prezzo: <b>" + costoLive.toString() + "€</b>");
}
globalThis.initCostoLive = initCostoLive;

function validate() {
    // OK
    if ((utils.testStringEmpty(utils.getValue("inputCognome"))) || (!utils.testStringNotEmptyContainsOnlyNumbers(utils.getValue("inputNumeroDiTelefono"))) ||
        (utils.testStringEmpty(utils.getValue("orario"))) || (gusti.length == 0 && bevande.length == 0)) {

        // disable
        utils.clearAndInject("inviaOrdineButton", `<input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);

        return;
    }


    if (utils.getValueRaw("universalTime").trim().split(" ").length == 4) {
        console.log("Univesal time should be matching the expected text format. Time integrity should be a promise.");

        var temp = utils.getValueRaw("universalTime").trim().split(" ")[2];
        var universalTimeNoOffset = temp.substring(0, temp.indexOf(":", temp.indexOf(":") + 1));
        var universalTimeOffset = (Number.parseInt(universalTimeNoOffset.substring(0, universalTimeNoOffset.indexOf(":"))) + 2);
        switch (universalTimeOffset) {
            case 24:
                universalTimeOffset = 0;
                break;
            case 25:
                universalTimeOffset = 1;
                break;
        }

        if (universalTimeOffset <= 9) {
            universalTimeOffset = "0" + universalTimeOffset.toString() + universalTimeNoOffset.substring(universalTimeNoOffset.indexOf(":"));
        }
        else {
            universalTimeOffset = universalTimeOffset.toString() + universalTimeNoOffset.substring(universalTimeNoOffset.indexOf(":"));
        }
        console.log(universalTimeOffset, typeof (universalTimeOffset));

        // Controllo che l'orario specificato per la consegna sia nel range di orari validi
        var min = document.getElementById("orario").min;
        var max = document.getElementById("orario").max;

        if (universalTimeOffset < min || universalTimeOffset > max) {
            // disable
            utils.clearAndInject("inviaOrdineButton", `<input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);

            return;
        }

        var universalTimeOffsetExtendedH = (Number.parseInt(universalTimeNoOffset.substring(0, universalTimeNoOffset.indexOf(":"))) + 2);
        switch (universalTimeOffsetExtendedH) {
            case 24:
                universalTimeOffsetExtendedH = 0;
                break;
            case 25:
                universalTimeOffsetExtendedH = 1;
                break;
        }
        var universalTimeOffsetExtendedMins = (Number.parseInt(universalTimeNoOffset.substring(universalTimeNoOffset.indexOf(":") + 1)));
        universalTimeOffsetExtendedMins += 5;
        switch (universalTimeOffsetExtendedMins) {
            case 60:
                universalTimeOffsetExtendedH++;
                universalTimeOffsetExtendedMins = 0;
                break;

            case 61:
                universalTimeOffsetExtendedH++;
                universalTimeOffsetExtendedMins = 1;
                break;

            case 62:
                universalTimeOffsetExtendedH++;
                universalTimeOffsetExtendedMins = 2;
                break;

            case 63:
                universalTimeOffsetExtendedH++;
                universalTimeOffsetExtendedMins = 3;
                break;

            case 64:
                universalTimeOffsetExtendedH++;
                universalTimeOffsetExtendedMins = 4;
                break;
        }
        switch (universalTimeOffsetExtendedH) {
            case 24:
                universalTimeOffsetExtendedH = 0;
                break;
            case 25:
                universalTimeOffsetExtendedH = 1;
                break;
        }

        var universalTimeOffsetExtended = "";

        if (universalTimeOffsetExtendedH <= 9) {
            universalTimeOffsetExtended += "0" + universalTimeOffsetExtendedH.toString() + ":";
        }
        else {
            universalTimeOffsetExtended += universalTimeOffsetExtendedH.toString() + ":";
        }

        if (universalTimeOffsetExtendedMins <= 9) {
            universalTimeOffsetExtended += "0" + universalTimeOffsetExtendedMins.toString();
        }
        else {
            universalTimeOffsetExtended += universalTimeOffsetExtendedMins.toString();
        }

        console.log(universalTimeOffsetExtended, typeof (universalTimeOffsetExtended));

       /* if (max < min) {
            var oldMax = max;
            var temp1 = max.substring(0, max.indexOf(":", max.indexOf(":") + 1));
            var temp2 = (Number.parseInt(temp1.substring(0, temp1.indexOf(":"))) + 24);
            max = temp2.toString() + oldMax.substring(oldMax.indexOf(":"));
        } */

        if (utils.getValue("orario") < min || utils.getValue("orario") > max || utils.getValue("orario") <= universalTimeOffsetExtended) {
            // disable
            utils.clearAndInject("inviaOrdineButton", `<input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);

            return;
        }
    }
    else {
        console.log("Universal time didn't return the expected text format. Validation case skipped! Time integrity is no longer a promise!");
    }

    if (!(utils.getValue("inputCognome").match(/^[A-Za-z1-9]+$/))) {
        // disable
        utils.clearAndInject("inviaOrdineButton", `<input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);

        return;
    }

    // enable
    utils.clearAndInject("inviaOrdineButton", `<input type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);
}
globalThis.validate = validate;

// inviaOrdine
function inviaOrdine() {
    // Istanzio oggetto ordinazione
    gustiCopy = copyArray(gusti);
    bevandeCopy = copyArray(bevande);
    var gustiFinal = [];
    var bevandeFinal = [];

    for (var i = 0; i < gustiCopy.length; i++) {
        gustiFinal.push(gustiCopy[i].substring(0, gustiCopy[i].indexOf(":")));
    }

    for (var i = 0; i < bevandeCopy.length; i++) {
        bevandeFinal.push(bevandeCopy[i].substring(0, bevandeCopy[i].indexOf(":")));
    }

    ordinazione = new Ordinazione(utils.getValue("inputCognome"), utils.getValue("inputNumeroDiTelefono"),
        utils.getValue("inputPiano"), utils.getValue("orario"), gustiFinal, bevandeFinal);

    // Controllo passato con successo. Possiamo procedere con il parsing
    var generatedXML = `<ordinazioni><ordinazione>`
    generatedXML += `<indirizzoDiConsegna>${ordinazione.getCognome}</indirizzoDiConsegna>`
    generatedXML += `<piano>${ordinazione.getPiano}</piano>`
    generatedXML += `<orarioDiConsegna>${ordinazione.getOrarioDiConsegna}</orarioDiConsegna>`;
    generatedXML += `<numeroDiTelefono>${ordinazione.getNumeroTelefonico}</numeroDiTelefono>`

    generatedXML += `<cibi>`
    // Riempimento dinamico per il tag "<gusto></gusto>"
    for (var j = 0; j < ordinazione.getGusti.length; j++) {
        // Apro tag "<vaschetta></vaschetta>"
        generatedXML += `<cibo>${ordinazione.getGusti[j]}</cibo>`;
    }

    generatedXML += `</cibi>`;

    generatedXML += `<bevande>`;

    for (var j = 0; j < ordinazione.getBevande.length; j++) {
        generatedXML += `<bevanda>${ordinazione.getBevande[j]}</bevanda>`;
    }

    generatedXML += `</bevande>`;

    generatedXML += `</ordinazione></ordinazioni>`;

    // Inietto la stringa generatedXML nel input con id "inputXMLJS"
    // Inietto la stringa generatedXML nel input con id "inputXMLJS"
    utils.setValue("inputXMLJS", generatedXML)

    // Reset
    reset();
}
globalThis.inviaOrdine = inviaOrdine;

// aggiungiGusto
function aggiungiCibo() {
    // Prima di aggiungere, controllo che c'è ancora spazio
    // per aggiungere
    /*var dimensione = utils.getValue("dimensione");
    switch (dimensione) {
        case "PICCOLA":
            if (gusti.length == 3) {
                window.alert("Numero massimo di gusti raggiunto.");
                return;
            }
            break;

        case "MEDIA":
            if (gusti.length == 4) {
                window.alert("Numero massimo di gusti raggiunto.");
                return;
            }
            break;

        case "GRANDE":
            if (gusti.length == 5) {
                window.alert("Numero massimo di gusti raggiunto.");
                return;
            }
            break;
    }*/

    // Prelevo valore dalla combobox e lo inserisco nell'array gusti
    var toPush = utils.getValue("gusti");
    gusti.push(toPush);

    // Aggiorno costo
    updateCostoLiveInAggiunta(toPush);

    // Aggiorno "showArrayGusti"
    updateShowArrayGusti();
    validate();
}
globalThis.aggiungiCibo = aggiungiCibo;

function aggiungiBevanda() {
    // Prelevo valore dalla combobox e lo inserisco nell'array gusti
    var toPush = utils.getValue("bevande");
    bevande.push(toPush);

    // Aggiorno costo
    updateCostoLiveInAggiunta(toPush);

    // Aggiorno "showArrayBevande"
    updateShowArrayBevande();
    validate();
}
globalThis.aggiungiBevanda = aggiungiBevanda;

function updateCostoLiveInAggiunta(prodotto) {
    costoLive += Number.parseFloat(prodotto.substring(prodotto.indexOf(">") + 1, prodotto.length - 4));
}
globalThis.updateCostoLiveInAggiunta = updateCostoLiveInAggiunta;

function updateCostoLiveInRimozione(prodotto) {
    costoLive -= Number.parseFloat(prodotto.substring(prodotto.indexOf(">") + 1, prodotto.length - 4));
}
globalThis.updateCostoLiveInRimozione = updateCostoLiveInRimozione;

// copyArray
function copyArray(src) {
    var dest = [];
    for (var i = 0; i < src.length; i++) {
        dest.push(src[i]);
    }

    return dest;
}
globalThis.copyArray = copyArray;

function updateShowArrayGusti() {
    utils.clear("showArrayGusti");
    for (var i = 0; i < gusti.length; i++) {
        utils.inject("showArrayGusti", `${gusti[i]}<br>`);
    }

    utils.clearAndInject("costoLive", "Prezzo: <b>" + costoLive.toString() + "€</b>");
}
globalThis.updateShowArrayGusti = updateShowArrayGusti;


function updateShowArrayBevande() {
    utils.clear("showArrayBevande");
    for (var i = 0; i < bevande.length; i++) {
        utils.inject("showArrayBevande", `${bevande[i]}<br>`);
    }

    utils.clearAndInject("costoLive", "Prezzo: <b>" + costoLive.toString() + "€</b>");
}
globalThis.updateShowArrayBevande = updateShowArrayBevande;

// Deprecated
function updateShowArrayVaschette() {
    utils.clear("showArrayVaschette");
    for (var i = 0; i < vaschette.length; i++) {
        utils.inject("showArrayVaschette", `${vaschette[i].toString()}<br>`);
    }
}
globalThis.updateShowArrayVaschette = updateShowArrayVaschette;

function reset() {
    ordinazione = null;
    vaschette = [];
    vaschetteCopy = null;
    gusti = [];
    gustiCopy = null;
    bevande = [];
    bevandeCopy = null;
    utils.resetForm("informazioniPersonali");
    utils.clear("gusti");
    utils.clear("bevande");
    // utils.clear("dimensione");
    utils.clear("showArrayGusti");
    utils.clear("showArrayBevande");
    //utils.clearAndInject("inviaOrdineButton", `<input disabled type="submit" class="w3-button" value="INVIA ORDINE" onclick="inviaOrdine()">`);
}
globalThis.reset = reset;

function recheckLimiti() {
    var dimensione = utils.getValue("dimensione");

    switch (dimensione) {
        case "PICCOLA":
            {
                while (gusti.length > 3) {
                    gusti.pop();
                }
            }
            break;

        case "MEDIA":
            {
                while (gusti.length > 4) {
                    gusti.pop();
                }
            }
            break;

        case "GRANDE":
            {
                while (gusti.length > 5) {
                    gusti.pop();
                }
            }
            break;
    }


    // Aggiorno "showArrayGusti"
    updateShowArrayGusti();
}
globalThis.recheckLimiti = recheckLimiti;

function refresh() {
    window.location.reload();
}
globalThis.refresh = refresh;

function rimuoviUltimaVaschetta() {
    // Controllo se la rimozione è possibile
    if (vaschette.length > 0) {
        // Rimuovo ultima vaschetta
        vaschette.pop();

        // Aggiorno visuale
        updateShowArrayVaschette();

        // Valido
        validate();
    }
}
globalThis.rimuoviUltimaVaschetta = rimuoviUltimaVaschetta;

function rimuoviUltimoGusto() {
    // Controllo se la rimozione è possibile
    if (gusti.length > 0) {
        // Rimuovo ultima gusto
        var popped = gusti.pop();

        // Aggiorno costo
        updateCostoLiveInRimozione(popped);

        // Aggiorno visuale
        updateShowArrayGusti();

        // Valido
        validate();
    }
}
globalThis.rimuoviUltimoGusto = rimuoviUltimoGusto;


function rimuoviUltimaBevanda() {
    // Controllo se la rimozione è possibile
    if (bevande.length > 0) {
        // Rimuovo ultima bevanda
        var popped = bevande.pop();

        // Aggiorno costo
        updateCostoLiveInRimozione(popped);

        // Aggiorno visuale
        updateShowArrayBevande();

        // Valido
        validate();
    }
}
globalThis.rimuoviUltimaBevanda = rimuoviUltimaBevanda;
