/**
 * HuffmanTutkiTiedosto tutkii tiedoston merkkien frekvenssit ja 
 * palauttaa ne merkeillä indeksoidussa taulukossa. Se on tiedoston
 * pakkaamisesta erillään koska se on vasta pohjustusta keon käyttöä varten
 * @author amjaalto
 */
import java.io.*;

public class HuffmanTutkiTiedosto
{
    File filu;
    int[] taulu = new int[256];
    BufferedReader in;

    public HuffmanTutkiTiedosto(File tiedosto) throws Exception
    {
        filu=tiedosto;
        in = new BufferedReader(new FileReader(filu));
    }

    public int[] tutki() throws Exception
    {
        int merkki;
        while ((merkki = in.read()) != -1)
        {
            taulu[merkki]+=1;
        }
        in.close();
        return taulu;
    }
        
}

