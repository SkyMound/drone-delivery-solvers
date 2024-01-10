/**
 * CustomCSVReader class is responsible for reading CSV files and extracting data.
 * It provides methods to read houses and parcels information from CSV files.
 */
package drone_delivery.solver;

import au.com.bytecode.opencsv.CSVReader;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Simple Java program to read CSV file in Java. In this program we will read
 * list of books stored in CSV file as comma separated values.
 * @author https://www.java67.com/2015/08/how-to-load-data-from-csv-file-in-java.html
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class CustomCSVReader {
  @Pure
  public static HashMap<String, Vector2d> getHousesFromCSV(final String cityfileName) {
    try {
      Path pathTocityFile = Paths.get(cityfileName);
      HashMap<String, Vector2d> mapInfoMaison = CollectionLiterals.<String, Vector2d>newHashMap();
      try (CSVReader br = new Function0<CSVReader>() {
        @Override
        public CSVReader apply() {
          try {
            FileReader _fileReader = new FileReader(cityfileName);
            return new CSVReader(_fileReader);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      }.apply()) {
        String[] cityline = br.readNext();
        cityline = br.readNext();
        while ((cityline != null)) {
          {
            String[] cityattributes = cityline;
            float x = Float.parseFloat(cityattributes[1]);
            float y = Float.parseFloat(cityattributes[2]);
            String numMaison = cityattributes[3];
            Vector2d _vector2d = new Vector2d(x, y);
            mapInfoMaison.put(numMaison, _vector2d);
            cityline = br.readNext();
          }
        }
        return mapInfoMaison;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * This method is used to read parcels from CSV file.
   * @param cityfileName : String
   * @param parcelfileName : String
   * @param nbParcel : int
   * @return ArrayList<Parcel>
   * 
   * @author Mickael Martin https://github.com/Araphlen
   */
  public static ArrayList<Parcel> getparcelsFromCSV(final String cityfileName, final String parcelfileName, final int nbParcel) {
    ArrayList<Parcel> parcelsList = new ArrayList<Parcel>();
    HashMap<String, Vector2d> mapInfoMaison = CustomCSVReader.getHousesFromCSV(cityfileName);
    Path pathToparcelFile = Paths.get(parcelfileName);
    int nbParcelrest = nbParcel;
    try (BufferedReader pr = new Function0<BufferedReader>() {
      @Override
      public BufferedReader apply() {
        try {
          return Files.newBufferedReader(pathToparcelFile, 
            StandardCharsets.US_ASCII);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    }.apply()) {
      String parcelline = pr.readLine();
      parcelline = pr.readLine();
      while (((parcelline != null) && (nbParcelrest >= 1))) {
        {
          String[] parcelattributes = parcelline.split(",");
          Parcel parcel = CustomCSVReader.createParcel(parcelattributes, mapInfoMaison);
          parcelsList.add(parcel);
          parcelline = pr.readLine();
          nbParcelrest--;
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException ioe = (IOException)_t;
        ioe.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return parcelsList;
  }

  /**
   * This method is used to create a parcel from a metadata.
   * @param metadaparcel : String[]
   * @param mapMaison : HashMap<String, Vector2d>
   * @return Parcel
   * @author Mickael Martin https://github.com/Araphlen
   */
  public static Parcel createParcel(final String[] metadaparcel, final HashMap<String, Vector2d> mapMaison) {
    int commande = Integer.parseInt(metadaparcel[0]);
    int maison = Integer.parseInt(metadaparcel[1]);
    String idMaisonReel = metadaparcel[3];
    Vector2d position = mapMaison.get(idMaisonReel);
    String[] ordertime = metadaparcel[2].split(":");
    Integer _valueOf = Integer.valueOf(ordertime[0]);
    int _multiply = (((_valueOf) == null ? 0 : (_valueOf).intValue()) * 3600);
    Integer _valueOf_1 = Integer.valueOf(ordertime[1]);
    int _plus = (_multiply + (((_valueOf_1) == null ? 0 : (_valueOf_1).intValue()) * 60));
    Integer _valueOf_2 = Integer.valueOf(ordertime[2]);
    int secFrommidnight = (_plus + ((_valueOf_2) == null ? 0 : (_valueOf_2).intValue()));
    float weight = Float.parseFloat(metadaparcel[4]);
    return new Parcel(position, weight, secFrommidnight, commande);
  }

  @SyntheticMember
  public CustomCSVReader() {
    super();
  }
}
