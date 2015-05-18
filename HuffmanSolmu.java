/**
 * HuffmanSolmu on puun solmu jolla on paino ja vasen sekä oikea lapsi.
 * Solmulla on merkki siinä tapauksessa että se on lehtisolmu.
 * @author amjaalto
 */
public class HuffmanSolmu {

    int paino=0;
    char merkki;
    HuffmanSolmu vasen=null, oikea=null;
    
    public HuffmanSolmu() {}

    public HuffmanSolmu(char merkki, int paino) 
    {
		this.merkki=merkki;
		this.paino=paino;
	}
    
    public HuffmanSolmu(char merkki)
    {
	this.merkki=merkki;
	paino++;
    }

    public char annaMerkki()
    {
	return merkki;
    }

    public int annaPaino()
    {
        return paino;
    }

    public void kasvataPainoa()
    {
        paino++;
    }

    public void asetaPaino(int paino)
    {
        this.paino=paino;
    }

    public void asetaLeft(HuffmanSolmu s)
    {
        vasen=s;
    }
    public void asetaRight(HuffmanSolmu s)
    {
        oikea=s;
    }
    public HuffmanSolmu annaRight()
    {
        return oikea;
    }
    public HuffmanSolmu annaLeft()
    {
        return vasen;
    }

}
