package Aereo;
import Utils.Coda;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Gate extends Thread{
    Timer timer;
    TimerTask timerTask;
    int nomeGate;
    String destinazione;
    Coda<F_Turista> codaPrioritaria;
    Coda<F_Turista> codaNormale;
    Boolean TerminatiIControlli;
    Coda<F_Turista> codaTurista;
    Boolean GateAperto;
    Coda<F_Turista> codaGenerale;

    //ImpiegatoControlliStiva impiegatoControlliStiva; fixare
    public Gate(int nomeGate, Coda<F_Turista> codaGenerale, String destinazione){ //ImpiegatoControlliStiva impiegatoControlliStiva){
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
        //this.impiegatoControlliStiva = impiegatoControlliStiva;
    }
    public void run(){
        try {
            timer.schedule(timerTask, 60000); //Programma il TimerTask per eseguirlo dopo un ritardo specificato

                    while(!codaGenerale.isEmpty()){   //creo la coda prioritaria e la coda normale
                        F_Turista t = codaGenerale.pop();
                        if(t.getPrioritario() || isPasseggeroInPrioritaria()){
                            codaPrioritaria.push(t);
                            sleep(100);
                            System.out.println("Il turista "+ t.get_id() + " è entrato nella coda prioritaria nel gate " + nomeGate);
                        }
                        else{
                            codaNormale.push(t);
                            sleep(100);
                            System.out.println("Il turista " + t.get_id() + " è entrato nella coda normale nel gate " + nomeGate);
                        }
                    }
                    while (!codaPrioritaria.isEmpty()) {  //prima la coda prioritaria
                        F_Turista t = codaPrioritaria.pop();
                        EffettuaControllo(t);

                    }
                    while (!codaNormale.isEmpty()) { //dopo la coda normale
                        F_Turista t = codaNormale.pop();
                        EffettuaControllo(t);
                    }
                    // TerminatiIControlli verrà impostato su true dal TimerTask
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }

    }
    public Boolean getTerminatiIControlli(){
        return TerminatiIControlli;
    }

    //Metodo che controllo se la destinazione del gate corrisponde alla destinazione segnata
    //nella carta d'imbarco dei turisti, e effettua controllo su turisti pericolosi
    public void EffettuaControllo(F_Turista t){
        try{
            if(destinazione.equals(t.getDestinazione())){
                sleep(100);
                System.out.println("    Il turista " + t.get_id() + " ha effettuato il controllo effettuato nel gate " + nomeGate);
            }
            else{
                sleep(100);
                System.out.println("    Il turista " + t.get_id() + " ha sbagliato gate");
            }
        }catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public Coda<F_Turista> getCodaTurista() {
        return codaTurista;
    }

    //Metodo che apre il gate
    public void openGate(){ //mi fa partire il gate
        GateAperto = true;
        this.start();
    }

    //Feature Marco Perin
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

    public Coda<F_Turista> getCodaGenerale()
    {
        return codaGenerale;
    }
}