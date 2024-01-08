package drone_delivery.solver;

import drone_delivery.solver.gui.EnvironmentGui;
import io.sarl.api.core.OpenEventSpace;
import io.sarl.lang.core.AgentContext;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.EventListener;
import io.sarl.lang.core.SREBootstrap;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class DroneSimulation implements EventListener {
  public static final UUID id = UUID.randomUUID();

  /**
   * SRE Kernel instance
   */
  private SREBootstrap kernel;

  /**
   * The default SARL context where environment and boids are spawned
   */
  private AgentContext defaultSARLContext;

  /**
   * Identifier of the environment
   */
  private UUID environment;

  private int width = Settings.EnvtWidth;

  private int height = Settings.EnvtHeight;

  /**
   * Map buffering boids before launch/start
   */
  private int dronesToLaunch;

  /**
   * Map buffering boids' bodies before launch/start
   */
  private ConcurrentHashMap<UUID, PerceivedDroneBody> droneBodies;

  private int dronesCount;

  /**
   * Boolean specifying id the simulation is started or not.
   */
  private boolean isSimulationStarted = false;

  /**
   * the vent space used to establish communication between GUI and agents,
   * Especially enabling GUI to forward start event to the environment,
   * respectively the environment to send GUIRepain at each simulation step to the GUI
   */
  private OpenEventSpace space;

  /**
   * The Graphical user interface
   */
  private EnvironmentGui myGUI;

  public DroneSimulation() {
    this.dronesCount = 0;
    ConcurrentHashMap<UUID, PerceivedDroneBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedDroneBody>();
    this.droneBodies = _concurrentHashMap;
    this.dronesToLaunch = 0;
  }

  public void start() {
    this.isSimulationStarted = true;
  }

  public void stop() {
    this.isSimulationStarted = false;
  }

  private void launchAllDrones() {
    for (int i = 0; (i < this.dronesToLaunch); i++) {
      this.launchDrone(("Drone" + Integer.valueOf(i)));
    }
  }

  private void launchDrone(final String droneName) {
    Vector2d initialPosition = new Vector2d();
  }

  private void killAllAgents() {
  }

  @Override
  @Pure
  public UUID getID() {
    return DroneSimulation.id;
  }

  /**
   * Methods managing event coming from agents
   */
  @Override
  public void receiveEvent(final Event event) {
    if ((event instanceof GuiRepaint)) {
      this.myGUI.setDrones(((GuiRepaint)event).perceivedAgentBody);
      this.myGUI.repaint();
    }
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DroneSimulation other = (DroneSimulation) obj;
    if (!Objects.equals(this.environment, other.environment))
      return false;
    if (other.width != this.width)
      return false;
    if (other.height != this.height)
      return false;
    if (other.dronesToLaunch != this.dronesToLaunch)
      return false;
    if (other.dronesCount != this.dronesCount)
      return false;
    if (other.isSimulationStarted != this.isSimulationStarted)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.environment);
    result = prime * result + Integer.hashCode(this.width);
    result = prime * result + Integer.hashCode(this.height);
    result = prime * result + Integer.hashCode(this.dronesToLaunch);
    result = prime * result + Integer.hashCode(this.dronesCount);
    result = prime * result + Boolean.hashCode(this.isSimulationStarted);
    return result;
  }
}
