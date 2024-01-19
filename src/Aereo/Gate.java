package Aereo;
import Persona.ImpiegatoControlliStiva;
import Utils.Coda;
import Persona.Turista;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Gate extends Thread{
    Timer timer;
    TimerTask timerTask;
    int nomeGate;
    String destinazione;
    Coda<Turista> codaPrioritaria;
    Coda<Turista> codaNormale;
    Boolean TerminatiIControlli;
    Coda<Turista> codaTurista;
    Boolean GateAperto;
    Coda<Turista> codaGenerale;
    ImpiegatoControlliStiva impiegatoControlliStiva;
    public Gate(int nomeGate, Coda<Turista> codaGenerale,String destinazione, ImpiegatoControlliStiva impiegatoControlliStiva){
        GateAperto = false;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TerminatiIControlli = true;
                System.out.println("Il gate " + nomeGate + " si è chiuso");
            }
        };
        codaPrioritaria = new Coda<>();
        codaNormale = new Coda<>();
        codaTurista = new Coda<>();

        this.codaGenerale = codaGenerale;
        TerminatiIControlli = false;
        this.destinazione = destinazione;
        this.nomeGate = nomeGate;
        this.impiegatoControlliStiva = impiegatoControlliStiva;
    }
    public void run(){
        try {
            timer.schedule(timerTask, 60000); //Programma il TimerTask per eseguirlo dopo un ritardo specificato

            while(true) {
                if (!GateAperto) {  //controllo che il gate sia aperto
                    System.out.println("gate chiuso " + nomeGate);
                    sleep(100);
                }
                else{
                    while(!codaGenerale.isEmpty()){   //creo la coda prioritaria e la coda normale
                        Turista t = codaGenerale.pop();
                        if(t.GetCartaImbarco().getPrioritario() || isPasseggeroInPrioritaria()){
                            codaPrioritaria.push(t);
                            sleep(1000);
                            System.out.println("Il turista "+ t.GetCartaImbarco().getCognomePasseggero() + " " + t.GetCartaImbarco().getNomePasseggero() + " è entrato nella coda prioritaria nel gate " + nomeGate);
                        }
                        else{
                            codaNormale.push(t);
                            sleep(1000);
                            System.out.println("Il turista " + t.GetCartaImbarco().getCognomePasseggero() + " " + t.GetCartaImbarco().getNomePasseggero() + " è entrato nella coda normale nel gate " + nomeGate);
                        }
                    }
                    while (!codaPrioritaria.isEmpty()) {  //prima la coda prioritaria
                        Turista t = codaPrioritaria.pop();
                        EffettuaControllo(t);

                    }
                    while (!codaNormale.isEmpty()) { //dopo la coda normale
                        Turista t = codaNormale.pop();
                        EffettuaControllo(t);
                    }
                    // TerminatiIControlli verrà impostato su true dal TimerTask
                }
            }
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }

    }
    public Boolean getTerminatiIControlli(){
        return TerminatiIControlli;
    }
    public void EffettuaControllo(Turista t){
        try{
            if(destinazione.equals(t.GetCartaImbarco().getViaggio())){
                sleep(1000);
                System.out.println("    Il turista " + t.GetCartaImbarco().getCognomePasseggero() + " " + t.GetCartaImbarco().getNomePasseggero() + " ha effettuato il controllo effettuato nel gate " + nomeGate);
                t.setGateGiusto(true);
            }
            else{
                sleep(1000);
                t.setGateGiusto(false);
                System.out.println("    Il turista " + t.GetCartaImbarco().getCognomePasseggero() + " " + t.GetCartaImbarco().getNomePasseggero() + " ha sbagliato gate");
            }

            boolean controlloTPericoloso = false;

            for(Turista tPericoloso : impiegatoControlliStiva.getTuristiPericolosi())
            {
                if (tPericoloso == t) {
                    controlloTPericoloso = true;
                    break;
                }
            }

            if (!controlloTPericoloso)
            {
                t.setEsitoControlloGate(true);
                codaTurista.push(t);
            }
            else
            {
                t.setEsitoControlloGate(false);
            }

            t.setPassatoControlloGate(true);
            t.notify();
        }catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public Coda<Turista> getCodaTurista() {
        return codaTurista;
    }
    public void openGate(){ //mi fa partire il gate
        GateAperto = true;
        this.start();
    }

    //FEATURE
    private boolean isPasseggeroInPrioritaria() {
        int minRange = 0;
        int maxRange = 500;

        // Utilizzo una probabilità del 2% per spostare un passeggero nella coda prioritaria
        double probabilita = 0.02;

        int randomValue = new Random().nextInt(maxRange - minRange + 1) + minRange;

        // Restituisci true se il numero casuale è inferiore alla probabilità desiderata moltiplicata per il range massimo
        return randomValue < probabilita * maxRange;
    }
    public String getDestinazione(){return destinazione;}
    public boolean getGateAperto(){ return GateAperto;}

    public Coda<Turista> getCodaGenerale()
    {
        return codaGenerale;
    }
}