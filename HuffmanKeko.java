/**
 * HuffmanKeko on minimikeko joka tarjoaa yhdistaPienimmat-metodin
 * jolla Huffmanpuu luodaan.
 * @author amjaalto
 */

public class HuffmanKeko
{

    int koko=0;
    HuffmanSolmu[] taulu = new HuffmanSolmu[256], aputaulu = new HuffmanSolmu[256];
	/**
	 * Keon konstruktori joka ottaa parametrinään taulukon, 
	 * josta keko rakennetaan. Taulukon indeksi on merkki
	 * ja indeksin sisältämä informaatio on merkin paino.
	 * Näillä tiedoilla luodaan uusi taulukko joka sisältää
	 * HuffmanSolmuja, jotka sitten järjestetään keoksi kutsumalla
	 * rakennaKeko-metodia.
	 */
    public HuffmanKeko(int[] taulukko)
    {
        for (int i=0; i<taulukko.length; i++)
            if (taulukko[i]!=0)
            {
                aputaulu[i]=new HuffmanSolmu((char)i, taulukko[i]);
            }
        this.rakennaKeko();
    }
	/**
	 * Keon rakentamisessa käytetään aputaulukkoa, jotta keko muodostuisi
	 * aina samalla tavalla (rakennusvaiheessa alkiot lisätään kekoon aina
	 * samassa järjestyksessä)
	 */ 
    private void rakennaKeko()
    {
        for (int i=0; i<aputaulu.length; i++)
            if (aputaulu[i]!=null)
               this.lisaaSolmu(aputaulu[i]);
    }
    private HuffmanSolmu poista()
    {
        HuffmanSolmu pienin=taulu[0];
        taulu[0]=taulu[koko-1];
        taulu[koko-1]=pienin;
        koko--;
        heapify(0);
        return pienin;
    }
    private void heapify(int i) //indeksi i
    {
        int pienin=0;
        int l = i*2;
        int r = i*2+1;
        if (r < koko)
        {
            if (taulu[l].annaPaino() < taulu[r].annaPaino())
                pienin = l;
            else
                pienin = r;
            if (taulu[i].annaPaino() > taulu[pienin].annaPaino())
            {
                HuffmanSolmu apu = taulu[i];
                taulu[i]=taulu[pienin];
                taulu[pienin]=apu;
                heapify(pienin);
            }
        }
         else if (l == koko-1 && taulu[i].annaPaino() > taulu[l].annaPaino())
         {
            HuffmanSolmu apu2 = taulu[i];
            taulu[i]=taulu[l];
            taulu[l]=apu2;
         }
    }

    public void lisaaSolmu(HuffmanSolmu s)
    {
        koko++;
        int i = koko-1;
        while ((i>0) && (taulu[i/2].annaPaino() > s.annaPaino()))
        {
            taulu[i] = taulu[i/2];
            i = i/2;
        }
        taulu[i] = s;
    }
    /**
     * Metodi yhdistää keon kaksi pienintä uuden solmun lapsiksi ja
     * lisää tämän uuden takaisin kekoon. Kun jäljellä on vain yksi
     * solmu, on tämä solmu Huffmanpuun juuri.
     */
    public void yhdistaPienimmat()
    {
        HuffmanSolmu eka=null, toka=null, uusi=null;
        while (yliYksiPuuJaljella()==true)
        {
            eka=poista();
            toka=poista();
            uusi=new HuffmanSolmu();
            uusi.asetaPaino(eka.annaPaino()+toka.annaPaino());
            uusi.asetaLeft(eka);
            uusi.asetaRight(toka);
            lisaaSolmu(uusi);
        }
    }


    public boolean yliYksiPuuJaljella()
    {
        return koko>1;
    }
    public HuffmanSolmu palautaPuu()
    {
        return taulu[0];
    }
    /**
     * testaukseen käytetty
     */
    public HuffmanSolmu[] palautaTaulu()
    {
        return taulu;
    }
}
