package drone_delivery.solver;

import au.com.bytecode.opencsv.CSVReader;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class DroneDeliveryLauncher {
  @Pure
  public static String getResourcePath(final String fileName) {
    File f = new File("");
    String _absolutePath = f.getAbsolutePath();
    String dossierPath = ((_absolutePath + File.separator) + fileName);
    return dossierPath;
  }

  @Pure
  public static File getResource(final String fileName) {
    String completeFileName = DroneDeliveryLauncher.getResourcePath(fileName);
    File file = new File(completeFileName);
    return file;
  }

  public static void main(final String... args) {
    try {
      int nbDrones = 20;
      int nbPackages = 20;
      String cityfilePath = DroneDeliveryLauncher.getResourcePath("src/main/resources/packagesSmallCity_100.csv");
      try (CSVReader reader = new Function0<CSVReader>() {
        @Override
        public CSVReader apply() {
          try {
            FileReader _fileReader = new FileReader(cityfilePath);
            return new CSVReader(_fileReader);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      }.apply()) {
        String[] nextLine = null;
        while (((nextLine = reader.readNext()) != null)) {
          System.out.println(Arrays.toString(nextLine));
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @SyntheticMember
  public DroneDeliveryLauncher() {
    super();
  }
}
