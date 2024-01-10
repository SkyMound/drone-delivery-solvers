/**
 * Drone simulation class
 * This class is the main class of the simulation, it manages the simulation's GUI and the agents' launch.
 * It also manages the communication between the GUI and the agents.
 * It is also the class that launch the simulation.
 * @author Martin Mickael https://github.com/Araphlen and Berne Thomas at conception
 */
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
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
   * the vent space used to establish communication between GUI and agents,
   * Especially enabling GUI to forward start event to the environment,
   * respectively the environment to send GUIRepain at each simulation step to the GUI
   */
  private OpenEventSpace space;

  /**
   * Boolean specifying id the simulation is started or not.
   */
  private boolean isSimulationStarted = false;

  /**
   * Identifier of the environment
   */
  private UUID environment;

  /**
   * Width and height of the environment
   */
  private int width = Settings.EnvtWidth;

  private int height = Settings.EnvtHeight;

  /**
   * The minimum value of the data in the city file
   */
  private double minDataX;

  private double minDataY;

  private Vector2d depotPos;

  private List<Vector2d> housesPos;

  private int dronesToLaunch;

  private int dronesCount;

  /**
   * Map buffering drones' bodies before launch/start
   */
  private ConcurrentHashMap<UUID, PerceivedDroneBody> droneBodies;

  private int nbParcelinSim;

  private List<Parcel> parcelToCreate;

  private List<Integer> deliveriesTime;

  /**
   * The Graphical user interface
   */
  private EnvironmentGui myGUI;

  /**
   * Width and height of the GUI
   */
  private int guiwidth = Settings.EnvtWidth;

  private int guiheight = Settings.EnvtHeight;

  /**
   * Constructor of the class DroneSimulation
   * @param nbDrones : number of drones to launch
   * @param nbParcel : number of parcels to deliver
   * @param cityfilePath : path to the city file
   * @param parcelfilePath : path to the parcel file
   */
  public DroneSimulation(final int nbDrones, final int nbParcel, final String cityfilePath, final String parcelfilePath) {
    this.dronesCount = 0;
    ConcurrentHashMap<UUID, PerceivedDroneBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedDroneBody>();
    this.droneBodies = _concurrentHashMap;
    this.dronesToLaunch = nbDrones;
    this.nbParcelinSim = nbParcel;
    this.housesPos = IterableExtensions.<Vector2d>toList(this.createHousePosList(cityfilePath));
    this.parcelToCreate = this.createParcelsList(cityfilePath, parcelfilePath);
    ArrayList<Double> newNnvBoudaries = this.resizeEnvToModel();
    this.translatehousesPosTo0(this.minDataX, this.minDataY);
    this.translateParcelsPosTo0(this.minDataX, this.minDataY);
    Double _get = newNnvBoudaries.get(0);
    this.width = ((Integer.valueOf((int) (((_get) == null ? 0 : (_get).doubleValue()) * 1.2))) == null ? 0 : (Integer.valueOf((int) (((_get) == null ? 0 : (_get).doubleValue()) * 1.2))).intValue());
    Double _get_1 = newNnvBoudaries.get(1);
    this.height = ((Integer.valueOf((int) (((_get_1) == null ? 0 : (_get_1).doubleValue()) * 1.2))) == null ? 0 : (Integer.valueOf((int) (((_get_1) == null ? 0 : (_get_1).doubleValue()) * 1.2))).intValue());
    this.deliveriesTime = CollectionLiterals.<Integer>newArrayList();
  }

  /**
   * Method that launch the simulation
   */
  public void start() {
    this.launchAllAgents();
    this.isSimulationStarted = true;
  }

  /**
   * Method that stop the simulation
   */
  public void stop() {
    this.killAllAgent();
    this.isSimulationStarted = false;
  }

  /**
   * Method that wait for the end of the simulation
   */
  @Pure
  public boolean finished() {
    while (this.isSimulationStarted) {
    }
    return true;
  }

  /**
   * Method that launch all the agents
   */
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
      double _x = this.depotPos.getX();
      double _y = this.depotPos.getY();
      Vector2d guidepotPos = new Vector2d(((_x * this.guiwidth) / this.width), ((_y * this.guiheight) / this.height));
      ConcurrentHashMap<UUID, PerceivedDroneBody> _scaleDronePosToGui = this.scaleDronePosToGui(this.droneBodies);
      ArrayList<Vector2d> _scaleHousesPosForGui = this.scaleHousesPosForGui(this.housesPos);
      EnvironmentGui _environmentGui = new EnvironmentGui(this.space, this.guiheight, this.guiwidth, _scaleDronePosToGui, guidepotPos, _scaleHousesPosForGui);
      this.myGUI = _environmentGui;
      this.space.registerWeakParticipant(this);
      Start _start = new Start(this.droneBodies);
      this.space.emit(DroneSimulation.id, _start, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Method that resize the environment to the model
   */
  private ArrayList<Double> resizeEnvToModel() {
    double minValueX = this.housesPos.get(0).getX();
    double minValueY = this.housesPos.get(0).getY();
    double maxValueX = this.housesPos.get(0).getX();
    double maxValueY = this.housesPos.get(0).getY();
    for (final Vector2d value : this.housesPos) {
      {
        double _x = value.getX();
        if ((_x < minValueX)) {
          minValueX = value.getX();
        }
        double _y = value.getY();
        if ((_y < minValueY)) {
          minValueY = value.getY();
        }
        double _x_1 = value.getX();
        if ((_x_1 > maxValueX)) {
          maxValueX = value.getX();
        }
        double _y_1 = value.getY();
        if ((_y_1 > maxValueY)) {
          maxValueY = value.getY();
        }
      }
    }
    this.minDataX = minValueX;
    this.minDataY = minValueY;
    ArrayList<Double> newboudaries = CollectionLiterals.<Double>newArrayList();
    newboudaries.add(Double.valueOf((maxValueX - minValueX)));
    newboudaries.add(Double.valueOf((maxValueY - minValueY)));
    return newboudaries;
  }

  /**
   * Method that translate the houses and parcels positions to 0 of the environment
   */
  private List<Vector2d> translatehousesPosTo0(final double minX, final double minY) {
    List<Vector2d> _xblockexpression = null;
    {
      ArrayList<Vector2d> newHousesPos = new ArrayList<Vector2d>();
      for (final Vector2d house : this.housesPos) {
        double _x = house.getX();
        double _y = house.getY();
        Vector2d _vector2d = new Vector2d((_x - minX), (_y - minY));
        newHousesPos.add(_vector2d);
      }
      _xblockexpression = this.housesPos = newHousesPos;
    }
    return _xblockexpression;
  }

  /**
   * Method that translate the houses and parcels positions to 0 of the environment
   */
  private List<Parcel> translateParcelsPosTo0(final double minX, final double minY) {
    List<Parcel> _xblockexpression = null;
    {
      ArrayList<Parcel> newParcelsToCreate = new ArrayList<Parcel>();
      for (final Parcel p : this.parcelToCreate) {
        double _x = p.getHousePos().getX();
        double _y = p.getHousePos().getY();
        Vector2d _vector2d = new Vector2d((_x - minX), (_y - minY));
        Parcel _parcel = new Parcel(p, _vector2d);
        newParcelsToCreate.add(_parcel);
      }
      _xblockexpression = this.parcelToCreate = newParcelsToCreate;
    }
    return _xblockexpression;
  }

  /**
   * Method that launch the depot agent
   */
  private void launchDepot() {
    try {
      double _x = Settings.DepotPos.getX();
      double _y = Settings.DepotPos.getY();
      Vector2d initialPosition = new Vector2d((_x - this.minDataX), (_y - this.minDataY));
      this.depotPos = initialPosition;
      UUID de = UUID.randomUUID();
      this.kernel.startAgentWithID(Depot.class, de, this.environment, initialPosition, "Depot", this.parcelToCreate, this.droneBodies);
      if (Settings.isLogActivated) {
        System.out.println(("Lancement du dépot à la position " + initialPosition));
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Method that launch all the drones
   */
  private void launchAllDrones() {
    for (int i = 0; (i < this.dronesToLaunch); i++) {
      this.launchDrone(("Drone" + Integer.valueOf(i)));
    }
  }

  /**
   * Method that launch a drone with default parameters
   * @param droneName : name of the drone to launch
   */
  private void launchDrone(final String droneName) {
    try {
      double _x = Settings.DepotPos.getX();
      double _y = Settings.DepotPos.getY();
      Vector2d initialPosition = new Vector2d((_x - this.minDataX), (_y - this.minDataY));
      Vector2d initSpeed = new Vector2d();
      Objectiv objectiv = Objectiv.Charge;
      Object targetPos = null;
      float battery = 100.0f;
      UUID d = UUID.randomUUID();
      int weight = 4;
      this.kernel.startAgentWithID(Drone.class, d, this.environment, initialPosition, initSpeed, objectiv, targetPos, Float.valueOf(battery), droneName);
      PerceivedDroneBody _perceivedDroneBody = new PerceivedDroneBody(d, initialPosition, initSpeed, objectiv, ((Vector2d)targetPos), battery, weight);
      this.droneBodies.put(d, _perceivedDroneBody);
      if (Settings.isLogActivated) {
        System.out.println(("Création d\'un drone à la position " + initialPosition));
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Method that kill all the agents and close the GUI
   */
  private void killAllAgent() {
    UUID _randomUUID = UUID.randomUUID();
    Die _die = new Die();
    this.space.emit(_randomUUID, _die, null);
    this.myGUI.dispose();
  }

  /**
   * Method that create the list of parcels to deliver organized by order of command
   * @param cityfilePath : path to the city file
   * @param parcelfilePath : path to the parcel file
   */
  public ArrayList<Parcel> createParcelsList(final String cityfilePath, final String parcelfilePath) {
    abstract class __DroneSimulation_0 implements Comparator<Parcel> {
      public abstract int compare(final Parcel p1, final Parcel p2);
    }

    ArrayList<Parcel> parcelList = CustomCSVReader.getparcelsFromCSV(cityfilePath, parcelfilePath, this.nbParcelinSim);
    __DroneSimulation_0 ___DroneSimulation_0 = new __DroneSimulation_0() {
      @Override
      public int compare(final Parcel p1, final Parcel p2) {
        return Integer.valueOf(p1.getOrdertime()).compareTo(Integer.valueOf(p2.getOrdertime()));
      }
    };
    Collections.<Parcel>sort(parcelList, ___DroneSimulation_0);
    return parcelList;
  }

  /**
   * Method that create the list of houses positions
   * @param cityfilePath : path to the city file
   */
  public Collection<Vector2d> createHousePosList(final String cityfilePath) {
    return CustomCSVReader.getHousesFromCSV(cityfilePath).values();
  }

  /**
   * Method that scale the drones positions to the GUI
   * @param drones : map of the drones' bodies
   */
  public ConcurrentHashMap<UUID, PerceivedDroneBody> scaleDronePosToGui(final ConcurrentHashMap<UUID, PerceivedDroneBody> drones) {
    ConcurrentHashMap<UUID, PerceivedDroneBody> guiPosDrones = new ConcurrentHashMap<UUID, PerceivedDroneBody>();
    Set<Map.Entry<UUID, PerceivedDroneBody>> _entrySet = drones.entrySet();
    for (final Map.Entry<UUID, PerceivedDroneBody> droneSet : _entrySet) {
      {
        PerceivedDroneBody droneBody = droneSet.getValue();
        double _x = droneBody.getPosition().getX();
        double _y = droneBody.getPosition().getY();
        Vector2d droneGuiPos = new Vector2d(((_x * this.guiwidth) / this.width), 
          ((_y * this.guiheight) / this.height));
        UUID _owner = droneBody.getOwner();
        Vector2d _vitesse = droneBody.getVitesse();
        Objectiv _objectiv = droneBody.getObjectiv();
        Vector2d _targetPos = droneBody.getTargetPos();
        float _battery = droneBody.getBattery();
        float _weight = droneBody.getWeight();
        PerceivedDroneBody guiPerceivedDrone = new PerceivedDroneBody(_owner, droneGuiPos, _vitesse, _objectiv, _targetPos, _battery, _weight);
        guiPosDrones.put(droneSet.getKey(), guiPerceivedDrone);
      }
    }
    return guiPosDrones;
  }

  /**
   * Method that scale the houses positions to the GUI
   * @param housesPosenv : list of the houses positions
   */
  public ArrayList<Vector2d> scaleHousesPosForGui(final List<Vector2d> housesPosenv) {
    ArrayList<Vector2d> newhp = new ArrayList<Vector2d>();
    for (final Vector2d hp : housesPosenv) {
      double _x = hp.getX();
      double _y = hp.getY();
      Vector2d _vector2d = new Vector2d(((_x * this.guiwidth) / this.width), ((_y * this.guiheight) / this.height));
      newhp.add(_vector2d);
    }
    return newhp;
  }

  @Override
  @Pure
  public UUID getID() {
    return DroneSimulation.id;
  }

  /**
   * event handler
   */
  @Override
  public void receiveEvent(final Event event) {
    if ((event instanceof GuiRepaint)) {
      this.myGUI.setDrones(this.scaleDronePosToGui(((GuiRepaint)event).perceivedAgentBody));
      this.myGUI.repaint();
    }
    if ((event instanceof GeneralSepuku)) {
      this.stop();
    }
    if ((event instanceof ParcelDelivered)) {
      this.deliveriesTime.add(Integer.valueOf(((ParcelDelivered)event).deliveredDuration));
      float meanDur = 0;
      for (final Integer d : this.deliveriesTime) {
        meanDur = (meanDur + ((d) == null ? 0 : (d).intValue()));
      }
      int _size = this.deliveriesTime.size();
      meanDur = (meanDur / _size);
      this.myGUI.updateDeliveriesMeanDuration(((int) meanDur));
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
    if (other.isSimulationStarted != this.isSimulationStarted)
      return false;
    if (!Objects.equals(this.environment, other.environment))
      return false;
    if (other.width != this.width)
      return false;
    if (other.height != this.height)
      return false;
    if (Double.doubleToLongBits(other.minDataX) != Double.doubleToLongBits(this.minDataX))
      return false;
    if (Double.doubleToLongBits(other.minDataY) != Double.doubleToLongBits(this.minDataY))
      return false;
    if (other.dronesToLaunch != this.dronesToLaunch)
      return false;
    if (other.dronesCount != this.dronesCount)
      return false;
    if (other.nbParcelinSim != this.nbParcelinSim)
      return false;
    if (other.guiwidth != this.guiwidth)
      return false;
    if (other.guiheight != this.guiheight)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Boolean.hashCode(this.isSimulationStarted);
    result = prime * result + Objects.hashCode(this.environment);
    result = prime * result + Integer.hashCode(this.width);
    result = prime * result + Integer.hashCode(this.height);
    result = prime * result + Double.hashCode(this.minDataX);
    result = prime * result + Double.hashCode(this.minDataY);
    result = prime * result + Integer.hashCode(this.dronesToLaunch);
    result = prime * result + Integer.hashCode(this.dronesCount);
    result = prime * result + Integer.hashCode(this.nbParcelinSim);
    result = prime * result + Integer.hashCode(this.guiwidth);
    result = prime * result + Integer.hashCode(this.guiheight);
    return result;
  }
}
