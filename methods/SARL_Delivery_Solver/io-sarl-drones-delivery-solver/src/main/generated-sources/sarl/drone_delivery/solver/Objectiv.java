/**
 * Drone agent
 * @param envt : UUID of the environment
 * @param initialPosition : initial position of the depot
 * @param name : name of the agent
 * @param listparcels : list of parcels to create
 * @param drones : list of drones perceived by the depot
 * @author Mickael Martin https://github.com/Araphlen and Berne Thomas at conception
 */
package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;

/**
 * Enum of the different objectives of the drone
 */
@SarlSpecification("0.13")
@SarlElementType(12)
@SuppressWarnings("all")
public enum Objectiv {
  GoLiv,

  BackLiv,

  Charge;
}
