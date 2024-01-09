package drone_delivery.solver;

import com.google.common.base.Objects;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.Schedules;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.Scope;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.scoping.extensions.cast.PrimitiveCastExtensions;
import io.sarl.lang.core.util.SerializableProxy;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class Depot extends Agent {
  private UUID environment;

  private Vector2d position;

  private ConcurrentHashMap<UUID, PerceivedDroneBody> drones;

  private List<Parcel> todeliver;

  private List<Parcel> parcelToCreate;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size > 4)) {
      Object _get = occurrence.parameters[0];
      if ((_get instanceof UUID)) {
        Object _get_1 = occurrence.parameters[0];
        this.environment = (_get_1 == null ? null : PrimitiveCastExtensions.toUUID(_get_1));
      }
      Object _get_2 = occurrence.parameters[1];
      if ((_get_2 instanceof Vector2d)) {
        Object _get_3 = occurrence.parameters[1];
        this.position = ((Vector2d) _get_3);
      }
      Object _get_4 = occurrence.parameters[2];
      if ((_get_4 instanceof String)) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        Object _get_5 = occurrence.parameters[2];
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName((_get_5 == null ? null : _get_5.toString()));
      }
      Object _get_6 = occurrence.parameters[3];
      if ((_get_6 instanceof List)) {
        Object _get_7 = occurrence.parameters[3];
        this.parcelToCreate = ((List<Parcel>) _get_7);
      }
      Object _get_8 = occurrence.parameters[4];
      if ((_get_8 instanceof ConcurrentHashMap)) {
        Object _get_9 = occurrence.parameters[4];
        this.drones = ((ConcurrentHashMap<UUID, PerceivedDroneBody>) _get_9);
      }
    }
    this.todeliver = CollectionLiterals.<Parcel>newArrayList();
  }

  private void $behaviorUnit$Perception$1(final Perception occurrence) {
    synchronized (this.drones) {
      this.drones = occurrence.perceivedAgentBody;
      while ((this.parcelToCreate.get(0).getOrdertime() < occurrence.time)) {
        this.todeliver.add(this.parcelToCreate.remove(0));
      }
      boolean _isEmpty = this.todeliver.isEmpty();
      if ((!_isEmpty)) {
        ArrayList<Parcel> toremoveFromList = new ArrayList<Parcel>();
        ArrayList<PerceivedDroneBody> dronesAvailables = this.getDronesAtDepot();
        for (final Parcel p : this.todeliver) {
          {
            int indexBestDrone = 0;
            boolean isreallyBest = false;
            for (int i = 0; (i < ((Object[])Conversions.unwrapArray(dronesAvailables, Object.class)).length); i++) {
              {
                int energyneed = this.energyneeded(p, dronesAvailables.get(i));
                float _battery = dronesAvailables.get(i).getBattery();
                if ((_battery > energyneed)) {
                  if (isreallyBest) {
                    float _battery_1 = dronesAvailables.get(i).getBattery();
                    float _battery_2 = dronesAvailables.get(indexBestDrone).getBattery();
                    if ((_battery_1 < _battery_2)) {
                      indexBestDrone = i;
                      isreallyBest = true;
                    }
                  } else {
                    indexBestDrone = i;
                    isreallyBest = true;
                  }
                }
              }
            }
            if (isreallyBest) {
              Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
              PerceivedDroneBody _get = dronesAvailables.get(indexBestDrone);
              int _ordertime = p.getOrdertime();
              _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(((("Le Drone " + _get) + "à été affecté au colis : ") + Integer.valueOf(_ordertime)));
              this.affecterDrone(dronesAvailables.get(indexBestDrone).getOwner(), p);
              dronesAvailables.remove(dronesAvailables.get(indexBestDrone));
              toremoveFromList.add(p);
            }
          }
        }
        this.todeliver.removeAll(toremoveFromList);
      }
      Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        UpdateAction _updateAction = new UpdateAction(this.drones);
        _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_updateAction);
      };
      _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(50, _function);
    }
  }

  protected void affecterDrone(final UUID id, final Parcel p) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    AffectOrder _affectOrder = new AffectOrder(p);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID id;
      
      public $SerializableClosureProxy(final UUID id) {
        this.id = id;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, id);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, id);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, id);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_affectOrder, _function);
  }

  @Pure
  protected ArrayList<PerceivedDroneBody> getDronesAtDepot() {
    ArrayList<PerceivedDroneBody> dronesAvailable = new ArrayList<PerceivedDroneBody>();
    Collection<PerceivedDroneBody> _values = this.drones.values();
    for (final PerceivedDroneBody drone : _values) {
      Objectiv _objectiv = drone.getObjectiv();
      boolean _equals = Objects.equal(_objectiv, Objectiv.Charge);
      if (_equals) {
        dronesAvailable.add(drone);
      }
    }
    return dronesAvailable;
  }

  @Pure
  protected int energyneeded(final Parcel p, final PerceivedDroneBody drone) {
    float _weight = drone.getWeight();
    float _weight_1 = drone.getWeight();
    float _weight_2 = p.getWeight();
    float distance_max_with_package = ((Settings.DistMaxDrone * _weight) / (_weight_1 + _weight_2));
    double _x = p.getHousePos().getX();
    double _x_1 = drone.getPosition().getX();
    double _pow = Math.pow((_x - _x_1), 2);
    double _y = p.getHousePos().getY();
    double _y_1 = drone.getPosition().getY();
    double _pow_1 = Math.pow((_y - _y_1), 2);
    double distance = Math.sqrt((_pow + _pow_1));
    double percentage_battery_needed = ((distance * 100) / distance_max_with_package);
    float _weight_3 = drone.getWeight();
    float _weight_4 = p.getWeight();
    percentage_battery_needed = (percentage_battery_needed + 
      (((_weight_3 + _weight_4) * Settings.DroneTakeoffRatio) + Settings.DroneTakeoffBatteryLoss));
    return ((int) (percentage_battery_needed + 0.99));
  }

  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LOGGING;

  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING);
  }

  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS;

  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS);
  }

  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES;

  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES);
  }

  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE;

  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Perception(final Perception occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Perception$1(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Perception.class);
    toBeFilled.add(Initialize.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Perception.class.isAssignableFrom(event)) {
      return true;
    }
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof Perception) {
      final Perception occurrence = (Perception) event;
      $guardEvaluator$Perception(occurrence, callbacks);
    }
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
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
    Depot other = (Depot) obj;
    if (!java.util.Objects.equals(this.environment, other.environment))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.environment);
    return result;
  }

  @SyntheticMember
  public Depot(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public Depot(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
