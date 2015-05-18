
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author amjaalto
 */
public class Huffman 
{

    public static void main(String[] args) throws Exception
    {
        
        int[] taulu;
        Scanner lukija = new Scanner(System.in);
        String komento="", tiedosto=null;


		System.out.println("Komennot: 1:Pakkaa 2:Pura 3:Molemmat 0:Lopetus. Esimerkiksi: 1 essee");

        while (!komento.equalsIgnoreCase("0"))
        {
                komento = lukija.next();
			if (komento.equalsIgnoreCase("1"))
			{
				tiedosto = lukija.next();
				File pakattava = new File(tiedosto);
				HuffmanTutkiTiedosto tutkija = new HuffmanTutkiTiedosto(pakattava);
				taulu=tutkija.tutki();
				HuffmanKeko keko = new HuffmanKeko(taulu);
				keko.yhdistaPienimmat();
				HuffmanSolmu puunRoot = keko.palautaPuu();
				HuffmanKaannaTiedosto huffman = new HuffmanKaannaTiedosto();
				huffman.pakkaa(pakattava, puunRoot);
			}
			if (komento.equalsIgnoreCase("2"))
			{
				tiedosto = lukija.next();
				File purkua = new File(tiedosto);
				HuffmanKaannaTiedosto huffman = new HuffmanKaannaTiedosto();
				huffman.pura(purkua);
			}
            if (komento.equalsIgnoreCase("3"))
            {
				tiedosto = lukija.next();
                File pakattava = new File(tiedosto);
                HuffmanTutkiTiedosto tutkija = new HuffmanTutkiTiedosto(pakattava);
                taulu=tutkija.tutki();
                HuffmanKeko keko = new HuffmanKeko(taulu);
                keko.yhdistaPienimmat();
                HuffmanSolmu puunRoot = keko.palautaPuu();
                HuffmanKaannaTiedosto huffman = new HuffmanKaannaTiedosto();
                huffman.pakkaa(pakattava, puunRoot);
                    
                File purkua = new File(pakattava.getName());
                huffman.pura(purkua);
		}
        }
    }
}


