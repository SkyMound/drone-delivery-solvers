package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;

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
}
