package TorreDiControllo;
import Aereo.Aereo;
import Aereo.Gate;

import java.util.Random;

public class Parcheggio {
    public int nome;
    public Aereo aereo;
    public Gate gate;
    public Boolean distanza;

    //public Manutentore manu; ancora da mettere

    public Parcheggio(int n, Gate g)
    {
        this.nome = n;
        this.gate = g;
        //this.manu = new Manutentore();ancora da mettere
        Random r = new Random();
        distanza = r.nextBoolean();

    }

    //controlla se il parcheggio è libero
    public Boolean isFree()
    {
        return aereo == null;
    }

    //passa l'aereo al manutentore e fa la manutenzione
    public void aereoArrivato(Aereo A)
    {
        //manu.a = A;
        //manu.Manutenzione();
    }

    //libera il parcheggio
    public void AereoInPartenza()
    {
        aereo = null;
        //manu.a = null;
    }
}