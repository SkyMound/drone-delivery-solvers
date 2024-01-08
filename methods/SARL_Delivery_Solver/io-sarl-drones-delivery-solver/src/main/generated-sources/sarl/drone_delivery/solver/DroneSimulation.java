package drone_delivery.solver;

import drone_delivery.solver.gui.EnvironmentGui;
import io.sarl.api.core.OpenEventSpace;
import io.sarl.lang.core.AgentContext;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.EventListener;
import io.sarl.lang.core.EventSpace;
import io.sarl.lang.core.SRE;
import io.sarl.lang.core.SREBootstrap;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
   * The default SARL context where environment and drones are spawned
   */
  private AgentContext defaultSARLContext;

  /**
   * Identifier of the environment
   */
  private UUID environment;

  private int width = Settings.EnvtWidth;

  private int height = Settings.EnvtHeight;

  /**
   * Map buffering drones before launch/start
   */
  private int dronesToLaunch;

  private int nbParcelinSim;

  private List<Parcel> parcelToCreate;

  /**
   * Map buffering drones' bodies before launch/start
   */
  private ConcurrentHashMap<UUID, PerceivedDroneBody> droneBodies;

  private int dronesCount;

  private Vector2d depotPos;

  private List<Vector2d> housesPos;

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

  public DroneSimulation(final int nbDrones, final int nbParcel, final String cityfilePath, final String parcelfilePath) {
    this.dronesCount = 0;
    ConcurrentHashMap<UUID, PerceivedDroneBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedDroneBody>();
    this.droneBodies = _concurrentHashMap;
    this.dronesToLaunch = nbDrones;
    this.nbParcelinSim = nbParcel;
    this.housesPos = IterableExtensions.<Vector2d>toList(this.createHousePosList(cityfilePath));
    this.parcelToCreate = this.createParcelsList(cityfilePath, parcelfilePath);
  }

  public void start() {
    this.launchAllAgents();
    this.isSimulationStarted = true;
  }

  public void stop() {
    this.killAllAgents();
    this.isSimulationStarted = false;
  }

  private void launchAllAgents() {
    try {
      this.kernel = SRE.getBootstrap();
      this.defaultSARLContext = this.kernel.startWithoutAgent();
      this.environment = UUID.randomUUID();
      this.kernel.startAgentWithID(Environment.class, this.environment, Integer.valueOf(this.height), Integer.valueOf(this.width));
      this.launchAllDrones();
      this.launchDepot();
      EventSpace _defaultSpace = this.defaultSARLContext.getDefaultSpace();
      this.space = ((OpenEventSpace) _defaultSpace);
      EnvironmentGui _environmentGui = new EnvironmentGui(this.space, this.height, this.width, this.droneBodies, this.depotPos, this.housesPos);
      this.myGUI = _environmentGui;
      this.space.registerWeakParticipant(this);
      Start _start = new Start(this.droneBodies);
      this.space.emit(DroneSimulation.id, _start, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void launchDepot() {
    try {
      Vector2d initialPosition = Settings.DepotPos;
      UUID de = UUID.randomUUID();
      this.kernel.startAgentWithID(Depot.class, de, this.environment, initialPosition, "Depot", this.parcelToCreate);
      if (Settings.isLogActivated) {
        System.out.println(("Lancement du dépot à la position " + initialPosition));
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void launchAllDrones() {
    for (int i = 0; (i < this.dronesToLaunch); i++) {
      this.launchDrone(("Drone" + Integer.valueOf(i)));
    }
  }

  private void launchDrone(final String droneName) {
    try {
      Vector2d initialPosition = Settings.DepotPos;
      Vector2d initSpeed = new Vector2d();
      Objectiv objectiv = Objectiv.Charge;
      Object targetPos = null;
      float battery = 100.0f;
      UUID d = UUID.randomUUID();
      this.kernel.startAgentWithID(Drone.class, d, this.environment, initialPosition, initSpeed, objectiv, targetPos, Float.valueOf(battery), droneName);
      PerceivedDroneBody _perceivedDroneBody = new PerceivedDroneBody(d, initialPosition, initSpeed, objectiv, ((Vector2d)targetPos), battery);
      this.droneBodies.put(d, _perceivedDroneBody);
      if (Settings.isLogActivated) {
        System.out.println(("Création d\'un drone à la position " + initialPosition));
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void killAllAgents() {
  }

  public ArrayList<Parcel> createParcelsList(final String cityfilePath, final String parcelfilePath) {
    return CustomCSVReader.getparcelsFromCSV(cityfilePath, parcelfilePath, this.nbParcelinSim);
  }

  public Collection<Vector2d> createHousePosList(final String cityfilePath) {
    return CustomCSVReader.getHousesFromCSV(cityfilePath).values();
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
    if (other.nbParcelinSim != this.nbParcelinSim)
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
    result = prime * result + Integer.hashCode(this.nbParcelinSim);
    result = prime * result + Integer.hashCode(this.dronesCount);
    result = prime * result + Boolean.hashCode(this.isSimulationStarted);
    return result;
  }
}
