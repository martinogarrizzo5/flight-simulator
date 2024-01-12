package Persona;

import Aereoporto.ZonaCheckIn.CartaImbarco;
import java.util.List;

public class Turista extends Persona{
    private Bagaglio bag;
    private CartaImbarco cartaImbarco;
    private String destinazione;
    private List<Oggetto> oggetti;

    public Turista(Documento doc,Bagaglio bag, CartaImbarco cartaImbarco, List<Oggetto> oggetti) {
        super(doc);
        this.bag = bag;
        this.cartaImbarco = cartaImbarco;
        this.oggetti = oggetti;
    }

    public void run(){
    }

    public String getDestinazione(){
        return destinazione;
    }

    public Bagaglio getBag() {
        return bag;
    }

    public List<Oggetto> GetListaOggetti()
    {
        return oggetti;
    }

    public void setCartaImbarco(CartaImbarco c) { cartaImbarco = c; }
 }
