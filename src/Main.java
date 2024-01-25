import Aereo.Aereo;
import Aereo.F_Turista;
import Utils.Coda;
import Aereo.Gate;
import Aereo.F_TorreControllo;

import java.util.LinkedList;
//AGGIUNGERE PRINTS NEI METODI/THREADS
public class Main {
   public static void main(String[] args) {
      //Creazione lista di  Aereoporti
      LinkedList<String> aereoporti = new LinkedList<String>();
      aereoporti.add("New York Airport");
      aereoporti.add("Tokyo Airport");
      aereoporti.add("Aereoporto Milano");
      aereoporti.add("Aereoporto Messina");

      //Creazione lista di Aerei
      LinkedList<Aereo> list_aerei = new LinkedList<Aereo>();
      for(int i = 0; i < 4; i++){
         Aereo a = new Aereo(i);
         list_aerei.add(a);
      }

      //Creazione lista di Turisti
      LinkedList<F_Turista> lista_turisti = new LinkedList<F_Turista>();
      int k = 0;
      int id_tur = 0;
      for(Aereo a : list_aerei){
         String dest = aereoporti.get(k);
         for(int i = 1; i <= 4; i++){
            for(int j = 1; i <= 10; i++){
               if(id_tur % 2 == 0){
                  F_Turista turista = new F_Turista(id_tur, a.Get_ID(), i, j, true, dest);
                  lista_turisti.add(turista);
               }
               else{
                  F_Turista turista = new F_Turista(id_tur, a.Get_ID(), i, j, false, dest);
                  lista_turisti.add(turista);
               }
               id_tur++;
            }
         }
         k++;
      }

      //Divisione dei turisti in base alla destinazione
      Coda<F_Turista> coda_gate_1 = new Coda<F_Turista>();
      Coda<F_Turista> coda_gate_2 = new Coda<F_Turista>();
      Coda<F_Turista> coda_gate_3 = new Coda<F_Turista>();
      Coda<F_Turista> coda_gate_4 = new Coda<F_Turista>();
      for(F_Turista t : lista_turisti){
         if(t.getDestinazione() == "New York Airport"){
            coda_gate_1.push(t);
         }
         else if(t.getDestinazione() == "Tokyo Airport"){
            coda_gate_2.push(t);
         }
         else if(t.getDestinazione() == "Aereoporto Milano"){
            coda_gate_3.push(t);
         }
         else if(t.getDestinazione() == "Aereoporto Messina"){
            coda_gate_4.push(t);
         }
      }

      //Crezione dei Gate
      LinkedList<Gate> lista_gate = new LinkedList<Gate>();
      lista_gate.add(new Gate(1, coda_gate_1, aereoporti.get(0), list_aerei.get(0)));
      lista_gate.add(new Gate(2, coda_gate_2, aereoporti.get(1), list_aerei.get(1)));
      lista_gate.add(new Gate(3, coda_gate_3, aereoporti.get(2), list_aerei.get(2)));
      lista_gate.add(new Gate(4, coda_gate_4, aereoporti.get(3), list_aerei.get(3)));

      //Creazione lista Torri di Controllo
      LinkedList<F_TorreControllo> lista_torre_controllo = new LinkedList<F_TorreControllo>();
      lista_torre_controllo.add(new F_TorreControllo(aereoporti.get(0), lista_gate));
      lista_torre_controllo.add(new F_TorreControllo(aereoporti.get(1), lista_gate));
      lista_torre_controllo.add(new F_TorreControllo(aereoporti.get(2), lista_gate));
      lista_torre_controllo.add(new F_TorreControllo(aereoporti.get(3), lista_gate));

      //Creare Aerei
      //Creare Turisti con i controllo già fatti al gate
      //2 possibilità per trasporto Gate -> Aereo
      //1 Teletrasportare turisti dentro aereos
      //2 creare navette che trasportano fino ad aereo
      //"Creare" ok della torre di controllo per permettere all'aereo di partire
      //Fare partire aereo
   }
}