/**
 * Launcher of the simulation.
 * @author Mickael Martin https://github.com/Araphlen and Berne Thomas at conception
 */
package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.io.File;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
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
    int nbDrones = Settings.nbDrones;
    int nbParcel = Settings.nbColis;
    String cityfilePath = DroneDeliveryLauncher.getResourcePath("src\\main\\resources\\smallCity_100.csv");
    String parcelfilePath = DroneDeliveryLauncher.getResourcePath("src\\main\\resources\\packagesSmallCity_100.csv");
    DroneSimulation simu = new DroneSimulation(nbDrones, nbParcel, cityfilePath, parcelfilePath);
    simu.start();
    try {
      simu.finished();
      System.out.flush();
      System.exit(0);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        InputOutput.<Exception>print(e);
        return;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  @SyntheticMember
  public DroneDeliveryLauncher() {
    super();
  }
}
