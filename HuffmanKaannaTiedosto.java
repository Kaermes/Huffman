import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;



/**
 * HuffmanKaannaTiedosto on väline annetun tiedoston pakkaamiseen kun
 * keko on jo muodostettu ja tiedoston purkamiseen (jolloin keko luodaan
 * kutsuilla tämän luokan sisältä).
 * @author amjaalto
 */
public class HuffmanKaannaTiedosto {

    File filu;
    String nimi;
    int tavupituus=8;
    String[] taulu= new String[256];
    PrintWriter kirjoittaja;
    String bitit="";
    int pituus=0, pisinBinaari=0;
    StringBuffer bufferi = new StringBuffer(64);
    HuffmanKeko keko;
    HuffmanSolmu rootti;
    DataOutputStream dataKirjoittaja;
    DataInputStream dataLukija;

    BufferedReader lukija;

    public HuffmanKaannaTiedosto() throws Exception {}
	/**
	 * teeTaulu luo taulukon josta tarkistaa kunkin merkin bittiesitys helposti.
	 * Se myös laskee tarvittavan bittimäärän, jotta roskanollat saadaan laskettua.
	 */ 
    private void teeTaulu(HuffmanSolmu a, String bitit)
    {
        if (a.annaLeft()==null)
        {
            char merkki = a.annaMerkki();
            taulu[(int)merkki] = bitit;
            pituus += (a.annaPaino() * bitit.length());
            return;
        }
        teeTaulu(a.annaLeft(), bitit+""+0);
        teeTaulu(a.annaRight(), bitit+""+1);

    }
    /**
     * Kirjoittaa pakkausvaiheessa puun luontiin tarvittavat tiedot
     * (merkin numeron sekä frekvenssin) pakkaustiedostoon.
     */
    private void kirjoitaPuuInfo(HuffmanSolmu solmu) throws Exception
    {
        if (solmu.annaLeft()==null)
        {
            int merkki =(int) solmu.annaMerkki();
            dataKirjoittaja.writeInt(merkki);
            dataKirjoittaja.writeChar(' ');
            dataKirjoittaja.writeInt(solmu.annaPaino());
            dataKirjoittaja.writeChar(' ');
            return;
        }
        kirjoitaPuuInfo(solmu.annaLeft());
        kirjoitaPuuInfo(solmu.annaRight());

    }

    private void kirjoitaBittienPituus() throws Exception
    {
        dataKirjoittaja.writeInt(pituus);
        dataKirjoittaja.writeChar(' ');
    }

    public String[] annaTaulu()
    {
        return taulu;
    }
    /**
     * Pakkauksessa luetaan alkuperäinen tiedosto läpi vielä kerran ja
     * korvataan jokainen merkki sitä vastaavalla binääriesityksellä,
     * joka löytyy taulukosta teeTaulu-metodin kutsun jälkeen.
     * "Binäärit" ovat aluksi Stringejä jotka muutetaan intiksi metodilla
     * muunnaIntiksi ja lopuksi ne vielä kirjoitetaan byteinä pakkaustiedostoon.
     */
    public void pakkaa(File tiedosto, HuffmanSolmu root) throws Exception
    {
		filu=tiedosto;
        nimi=filu.getName();
        int inttina;
		String tavu;

        lukija = new BufferedReader(new FileReader(filu));

        FileOutputStream outputKirjoitin = new FileOutputStream( new File(nimi+".huf"));
        dataKirjoittaja = new DataOutputStream(outputKirjoitin);

		this.teeTaulu(root, "");
		this.kirjoitaBittienPituus();
		this.kirjoitaPuuInfo(root);
        dataKirjoittaja.writeInt(-1);

        int merkki;
        while ((merkki = lukija.read()) != -1)
        {            
            bitit =""+ taulu[merkki];
            bufferi.append(bitit);
            while (bufferi.length()>tavupituus)
            {           
				tavu = bufferi.substring(0, tavupituus);
                bufferi.delete(0, tavupituus);
                inttina=muunnaIntiksi(tavu);
                outputKirjoitin.write((byte)inttina);
			}
        }
        lukija.close();
                     
        if (bufferi.length() > 0) //roskanollien lisäys
        {
            while (bufferi.length() < tavupituus)
            {
                bufferi.append("0");
            }
            tavu=bufferi.substring(0, tavupituus);
            inttina=this.muunnaIntiksi(tavu);
            outputKirjoitin.write((byte)inttina);
		}

        outputKirjoitin.close();
        dataKirjoittaja.close();
        bufferi.delete(0, bufferi.length()+1);
        System.out.println("***********Pakkaamisen loppu***************");
    }
     /**
     * Muuntaa annetun Stringjonon bittejä int-muotoon 
     */
	private int muunnaIntiksi(String bitteja)
	{
		int potenssi=0;
		int summa=0;
		for (int i = tavupituus-1; i > -1; i--)
		{
			if (bitteja.charAt(i)=='1')
			{
				potenssi=(int)Math.pow(2, i);
				summa+=potenssi;
			}
		}
		return summa;
	}
    /**
     * Tiedoston purku alkaa bittien pituudesta, niistä päätellään roskanollien määrä.
     * Seuraavaksi luetaan puun muodostukseen tarvittava informaatio ja luodaan niistä
     * keko, joka taas luo Huffmanpuun yhdistaPienimmat-metodillaan. Kun puu on valmis
     * lähdetään purkamaan itse tiedostoa.
     */
	public void pura(File tiedosto) throws Exception
	{
            int bittipituus=0;
            int taulukko[] = new int[256];
            nimi = tiedosto.getName().substring(0, tiedosto.getName().length());
            FileInputStream in = new FileInputStream(nimi+".huf");
            dataLukija = new DataInputStream(in);
            kirjoittaja = new PrintWriter(nimi+"-purettu");
            bittipituus = dataLukija.readInt();

            int roskanollia;
            if (bittipituus%tavupituus==0)
                roskanollia=0;
            else roskanollia = (tavupituus-bittipituus%tavupituus);

            luePuunTiedot(taulukko);

            keko = new HuffmanKeko(taulukko);
            keko.yhdistaPienimmat();
            rootti=keko.palautaPuu();
            this.teeTaulu(rootti, "");
            pisinBinaari = tutkiTaulu();
            if (pisinBinaari<8)
                pisinBinaari=8;
            bufferi = new StringBuffer(pisinBinaari*4);
            
            kirjoitaPurettu(roskanollia);

            kirjoittaja.close();
            in.close();
            dataLukija.close();
            System.out.println("************Purkamisen loppu*************");
	}
	
	private char kayPuuLapi(String bitteja, HuffmanSolmu solmu, StringBuffer buff)
	{
        char solmunMerkki = 'J';	//javan vaatima alustus
        if (solmu.annaLeft()==null)
            return solmu.annaMerkki();
        if (bitteja.charAt(0)=='0' && bitteja.length()>=1)
        {
			solmunMerkki = kayPuuLapi(bitteja.substring(1), solmu.annaLeft(), buff);
			buff.deleteCharAt(0);
        }
        if (bitteja.charAt(0)=='1' && bitteja.length()>=1)
        {
			solmunMerkki = kayPuuLapi(bitteja.substring(1), solmu.annaRight(), buff);
			buff.deleteCharAt(0);
        }
        return solmunMerkki;
	}
    /**
     * intin muunto String-binääriesitykseksi
     */        
	private String muutaBiteiksi(int nro)
	{
		String bitteja="", vastaus="";
        for (int i = tavupituus-1; i > -1; i--)
        {
			if (nro-Math.pow(2, i)>=0)
            {
				bitteja+="1";
                nro=nro-(int)Math.pow(2, i);
            }
            else bitteja+="0";
        }
		
        for (int i = tavupituus-1; i > -1 ; i--) 	//bittien kääntö ympäri
        {											//en tiedä miksi tämä vaadittiin
			vastaus+=bitteja.charAt(i);				//mutta sillä toimii
        }
            return vastaus;
	}
    /**
     * Tutkitaan mikä on pisin binääriesitys kaikista merkeistä.
     */
    private int tutkiTaulu()
    {
		int pisin=0;
        for (int i =0; i < taulu.length; i++)
        {
            if (taulu[i]==null)
				continue;
            if (pisin<taulu[i].length())
				pisin=taulu[i].length();
        }
            return pisin;
    }
    /**
     * Luetaan puun tiedot tiedostosta kunnes törmätään sovittuun merkkiin (-1).
     * Tiedot ovat muodossa (väli)merkki(väli)paino, merkki ja paino
     * ovat nelitavuisia ja ne luetaan readInt:llä.
     */	
	private void luePuunTiedot(int[] puunTaulukko) throws Exception
    {
		while (true)
        {
			dataLukija.readChar();	//lukee välilyönnin pois
            int merkki = dataLukija.readInt();
            if (merkki==-1)         //*****************BREAK*******************
                break;              //*****************BREAK*******************
            dataLukija.readChar();
            int paino = dataLukija.readInt();
            puunTaulukko[merkki]=paino;
        }
    }
    /**
     * Luetaan pakattua tiedostoa byte kerrallaan ja tulkitaan binäärit
     * puun mukaisesti merkeiksi käymällä puuta läpi aina kun StringBufferiin
     * on kertynyt enemmän bittejä kuin on pisimmässä yhdistelmässä.
     * Lopuksi Bufferi tyhjennetään siihen saakka kunnes jäljellä on vain roskanollat.
     */        
    private void kirjoitaPurettu(int roskanollia) throws Exception
    {
        int inputinLuku;

        while ((inputinLuku = dataLukija.read()) != -1)
        {
			bufferi.append(muutaBiteiksi(inputinLuku));
            while (bufferi.length()>pisinBinaari)
			{
				kirjoittaja.print(kayPuuLapi(bufferi.substring(0), rootti, bufferi));
			}
        }
        while (bufferi.length() > roskanollia)
        {
			kirjoittaja.print(kayPuuLapi(bufferi.substring(0), rootti, bufferi));
        }
        System.out.println("roskanollia jäljellä (bittiesitys): "+bufferi + " roskanollia pitäisi olla: "+roskanollia);
    }
}
