package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;

@SarlSpecification("0.13")
@SarlElementType(11)
@SuppressWarnings("all")
public interface Settings {
  /**
   * Environmental grid default width
   */
  static final int EnvtWidth = 800;

  /**
   * Environmental grid default height
   */
  static final int EnvtHeight = 600;

  /**
   * Boolean specifying whether message logs are activated or not
   */
  static final boolean isLogActivated = false;

  /**
   * Specify a pause delay before each drone sends his influence to the environment, and respectively before the environment sends perceptions to drones
   */
  static final int pause = 0;

  static final int DroneMaxSpeed = 30;

  static final int SecondsPerCycle = 20;

  static final float ChargePerSec = 0.01f;

  static final float BatteryLostPerSec = 0.03f;

  static final int distMinLiv = 20;

  static final Vector2d DepotPos = new Vector2d(2461228, 3984254);

  static final int DeliveryStartingHour = 8;

  static final int DistMaxDrone = 15000;

  static final int DroneTakeoffRatio = 1;

  static final int DroneTakeoffBatteryLoss = (1 / 4);
}
