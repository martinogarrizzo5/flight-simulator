package Aereoporto.ZonaEntrata;

import Aereo.Aereo;
import Aereoporto.Aereoporto;
import Aereoporto.ZonaArrivi.ZonaArrivi;
import Aereoporto.ZonaCheckIn.ZonaCheckIn;
import Aereoporto.ZonaControlli.ZonaControlli;
import Aereoporto.ZonaNegozi.ZonaNegozi;
import Aereoporto.ZonaPartenze.ZonaPartenze;
import Aereoporto.ZonaCheckIn.ZonaCheckIn;
import Persona.*;
import Aereoporto.Common.ListaOggetti;
import Aereoporto.Common.ZonaAeroporto;
import Utils.Coda;
import Aereoporto.ZonaControlli.ZonaControlli;
import Aereoporto.ZonaNegozi.ZonaNegozi;
import Aereoporto.ZonaPartenze.ZonaPartenze;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ZonaEntrata extends ZonaAeroporto {
    public ZonaPartenze zonaPartenze;
    public ZonaCheckIn zonaCheckIn;
    public ZonaControlli zonaControlli;
    public ZonaNegozi zonaNegozi;
    public ZonaArrivi zonaArrivi;
    private ArrayList<Aereo> lista_aerei;
    private Coda<Turista> codaTuristi;

    // qui entrano i turisti generati
    public ZonaEntrata(ArrayList<Aereo> lista_aer,ZonaCheckIn zonaCheckIn,ZonaControlli zonaControlli, ZonaNegozi zonaNegozi, ZonaPartenze zonaPartenze) {
        this.lista_aerei = lista_aer;
        codaTuristi = generaTuristi(zonaCheckIn,zonaControlli,zonaNegozi,zonaPartenze);
    }

    public Coda<Turista> generaTuristi(ZonaCheckIn zonaCheckIn, ZonaControlli zonaControlli, ZonaNegozi zonaNegozi, ZonaPartenze zonaPartenze) {
        Coda<Turista> coda = new Coda<>();

        for (int i = 0; i < 1; i++) {

            ArrayList<Oggetto> listOggetti = generaListaOggetti(0, 6);

            Turista t = new Turista(generaDocumenti(), generaBagagli(), null, listOggetti, Random_Id_Aereo(),zonaCheckIn,zonaControlli,zonaNegozi,zonaPartenze);
            coda.push(t);
            t.start();
        }
        return coda;
    }

    public int Random_Id_Aereo(){
        return 0;
        //Random rand = new Random();
        //return lista_aerei.get((int)(Math.random()*lista_aerei.size())).Get_ID();
    }

    private Bagaglio generaBagagli() {

        boolean daStiva = false;

        Random rand = new Random();
        if (rand.nextInt(0, 5) == 0) {
            daStiva = true;
        }

        int peso = rand.nextInt(0, 30);
        int lunghezza = rand.nextInt(0, 20);
        int altezza = rand.nextInt(0, 30);
        int profondita = rand.nextInt(0, 10);

        String misure = lunghezza + "-" + altezza + "-" + profondita;

        ArrayList<Oggetto> listOggetti = generaListaOggetti(5, 30);

        Bagaglio bagaglio = new Bagaglio(daStiva, peso, misure, new Etichetta(0,"test"), listOggetti);
        return bagaglio;
    }

    private Documento generaDocumenti() {
        Random rand = new Random();

        ArrayList<String> listTipoDocumento = new ArrayList<String>();
        listTipoDocumento.add("Carta d'identità");
        listTipoDocumento.add("Passaporto");
        listTipoDocumento.add("Patente");

        ArrayList<String> listNome = new ArrayList<String>();
        listNome.add("Mario");
        listNome.add("Luigi");
        listNome.add("Giovanni");
        listNome.add("Giacomo");

        ArrayList<String> listCognome = new ArrayList<String>();
        listCognome.add("Rossi");
        listCognome.add("Verdi");
        listCognome.add("Bianchi");
        listCognome.add("Neri");
        listCognome.add("Gialli");
        listCognome.add("Blu");
        listCognome.add("Arancioni");
        listCognome.add("Viola");
        listCognome.add("Rosa");

        ArrayList<String> listSesso = new ArrayList<String>();
        listSesso.add("M");
        listSesso.add("F");

        ArrayList<String> listNazionalita = new ArrayList<String>();
        listNazionalita.add("Italiano");
        listNazionalita.add("Francese");
        listNazionalita.add("Spagnolo");
        listNazionalita.add("Inglese");
        listNazionalita.add("Senegalese");

        ArrayList<String> listIndirizzo = new ArrayList<String>();
        listIndirizzo.add("Via Roma");
        listIndirizzo.add("Via Milano");
        listIndirizzo.add("Via Napoli");
        listIndirizzo.add("Via Firenze");
        listIndirizzo.add("Via Venezia");
        listIndirizzo.add("Via Torino");
        listIndirizzo.add("Via Bologna");

        String tipoDocumento = listTipoDocumento.get(rand.nextInt(0, listTipoDocumento.size()));
        String nome = listNome.get(rand.nextInt(0, listNome.size()));
        String cognome = listCognome.get(rand.nextInt(0, listCognome.size()));
        LocalDate dataNascita = generaDataNascita();
        String sesso = listSesso.get(rand.nextInt(0, listSesso.size()));
        String nazionalita = listNazionalita.get(rand.nextInt(0, listNazionalita.size()));
        String indirizzo = listIndirizzo.get(rand.nextInt(0, listIndirizzo.size()));
        LocalDate dataScadenza = generaDataScadenza();

        return new Documento(tipoDocumento, nome, cognome, dataNascita, sesso, nazionalita, indirizzo, dataScadenza);
    }

    private LocalDate generaDataNascita() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private LocalDate generaDataScadenza() {
        long minDay = LocalDate.of(2026, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2058, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private ArrayList<Oggetto> generaListaOggetti(int numMin, int numMax) {

        Random rand = new Random();
        ArrayList<Oggetto> listOggetti = new ArrayList<>();

        int i = rand.nextInt(numMin, numMax);
        for (int numero = 0; numero < i; numero++) {
            if (rand.nextInt(0, 40) == 0) {
                // oggetto pericoloso
                int posizione = rand.nextInt(0, ListaOggetti.getOggettiPericolosi().size());
                listOggetti.add(new Oggetto(ListaOggetti.getOggettiPericolosi().get(posizione)));
            } else {
                // oggetto safe
                int posizione = rand.nextInt(0, ListaOggetti.getOggetti().size());
                listOggetti.add(new Oggetto(ListaOggetti.getOggetti().get(posizione)));
            }
        }

        return listOggetti;
    }
}
