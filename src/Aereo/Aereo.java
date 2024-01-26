package Aereo;

import Utils.Coda;

import java.util.ArrayList;
import java.util.Random;

public class Aereo extends  Thread {
    public int id;
    public String destinazione;
    public int posizione;
    public Gate gate;
    private Bagno bagnifronte;
    private Bagno bagnoretro;
    private ScatolaNera scatolaNera;
    private ArrayList<Turbina> turbine;
    private Stiva stiva;
    private Serbatoio serbatoio;
    private boolean pilotaAutomatico;
    public Alieni alieni;
    public boolean einvolo;
    private boolean maltempo;
    private Random r;
    private F_Turista[][] matricePostiAereo;
    private int nPosti;
    private Entrata entrata;
    private Uscita uscita;

    public Aereo(int Id) {
        this.id = Id;
        maltempo = false;

        r = new Random();

        bagnifronte = new Bagno();
        bagnoretro = new Bagno();

        scatolaNera = new ScatolaNera(this);
        turbine = new ArrayList<Turbina>();
        for (int i = 0; i < 4; i++) {
            Turbina n = new Turbina(this, i);
            turbine.add(n);
        }
        stiva = new Stiva(this);
        serbatoio = new Serbatoio();
        pilotaAutomatico = false;

        einvolo = false;

        alieni = new Alieni(this);
        //lieni.start();

        matricePostiAereo = new F_Turista[4][10];
        nPosti = 40;
        entrata = new Entrata();
        uscita = new Uscita(this);
    }

    public void run() {
        //Modificare metodo aereo nelle condizioni del while
        if (sciopero()) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }
        avvia();
        while (einvolo && serbatoio.Get_Capacità_Serbatoio() > 0 && posizione < 100 && ControllaTurbine()) {
            System.out.println("L'aereo è partito!");
            try {
                //Feature Riccardo Pettenuzzo
                if (alieni.aereo_rubato) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            serbatoio.consuma_carburante();
            if (maltempo) {
                posizione += 1;
            } else {
                posizione += 2;
            }

            Coda<F_Turista> gb = Givebagno();

            if (gb.size() % 2 == 0) {
                bagnifronte.DareBisogno(gb.pop());
            }
            if (gb.size() % 2 == 1) {
                bagnoretro.DareBisogno(gb.pop());
            }

            try {
                bagnifronte.run();
                bagnoretro.run();
                while (bagnifronte.finito().size() > 0) {
                    Imbarca(bagnifronte.finito());
                }
                while (bagnoretro.finito().size() > 0) {
                    Imbarca(bagnoretro.finito());
                }
            } catch (Exception e) {
            }
        }
    }

    //Metodo che accende le Turbine dell'aereo e accende la scatola nera
    public void avvia() {
        ImbarcaPasseggieri();
        for (int i = 0; i < 4; i++) {
            turbine.get(i).Attiva();
        }
        scatolaNera.start();
        einvolo = true;
        for(Turbina t : turbine){
            t.start();
        }
    }

    //Metodo per riparare le turbine e ricaricare la batteria della scatola nera
    public void Ripara() {
        for (int i = 0; i < 4; i++) {
            turbine.get(i).Ripara();
        }
        scatolaNera.Ricarica();
        System.out.println("L'aereo " + this.id + " è stato riparato.");
    }

    public void Rifornisci() {
        serbatoio.riempi();
    }

    public void atterra() {
        einvolo = false;
        for (int i = 0; i < 4; i++) {
            turbine.get(i).Disabilita();
        }
        System.out.println("L'aereo " + this.id + " è atterrato.");
    }

    //Metodo di Controllo che controlla lo stato delle turbine e nel caso 3 o più turbine siano
    //non funzionanti l'aereo precipita
    public boolean ControllaTurbine() {
        boolean ris = true;
        int n = 0;
        for (int i = 0; i < 4; i++) {
            if (!turbine.get(i).funzionante) {
                n++;
            }
        }
        if (n > 2) {
            ris = false;
        }
        return ris;
    }

    public void CambiaStatoMaltempo() {
        maltempo = !maltempo;
    }

    //Feature Alessio Campagnaro
    public boolean sciopero() {
        Random r = new Random();
        int i = r.nextInt(100);
        if (i == 69) {
            System.out.println("I piloti stanno scioperando.");
            return true;
        } else {
            return false;
        }
    }

    public Coda<F_Turista> Givebagno() {
        Coda<F_Turista> coda = new Coda<F_Turista>();
        Random random = new Random();

        int rr = random.nextInt() * entrata.Getnperson();
        while (rr > 0) {

            int c = random.nextInt() * matricePostiAereo.length;
            int r = random.nextInt() * matricePostiAereo[0].length;

            if (matricePostiAereo[c][r] != null) {
                rr = 0;
            } else {
                coda.push(matricePostiAereo[c][r]);
                rr -= 1;
            }

        }
        return coda;
    }

    public void ImbarcaPasseggieri() {
        Imbarca(entrata.GetsalitiDavanti());
        Imbarca(entrata.GetsalitiDietro());
    }

    public void Imbarca(Coda<F_Turista> c) {
        for (int i = 0; i < c.size(); i++) {
            F_Turista t = c.pop();
            matricePostiAereo[t.posto_colonna][t.posto_riga] = t;
        }
        System.out.println("I Turisti sono saliti nell'aereo " + this.id + " in direzione " + this.destinazione + ".");
    }

    public Coda<F_Turista> FaiScendere() {
        Coda<F_Turista> coda = new Coda<F_Turista>();
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 10; r++) {
                if (matricePostiAereo[c][r] != null) {
                    coda.push(matricePostiAereo[c][r]);
                    matricePostiAereo[c][r] = null;
                }

            }
        }
        System.out.println("I Turisti sono scesi dall'aereo " + this.id + " a " + this.destinazione + ".");
        return coda;

    }

    public Entrata Get_Entrata() {
        return entrata;
    }

    public Uscita Get_Uscita() {
        return uscita;
    }

    public F_Turista[][] Get_Posti_Aereo() {
        return this.matricePostiAereo;
    }

    public void Set_Gate(Gate gate) {
        this.gate = gate;
    }

    public Stiva Get_Stiva() {
        return this.stiva;
    }

    public ArrayList<Turbina> Get_Turbine() {
        return this.turbine;
    }

    public String Get_Destinazione() {
        return this.destinazione;
    }

    public Gate Get_Gate() {
        return this.gate;
    }

    public ScatolaNera Get_Scatola_Nera() {
        return this.scatolaNera;
    }

    public Serbatoio Get_Serbatoio() {
        return this.serbatoio;
    }

    public int Get_ID() {
        return this.id;
    }

    public int Get_Posizione() {
        return this.posizione;
    }

    public boolean Get_Stato_Aereo() {
        return this.einvolo;
    }

}