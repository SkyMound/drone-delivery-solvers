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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class Drone extends Agent {
  private UUID environment;

  private Vector2d position;

  private Vector2d speed;

  private float battery;

  private float weight;

  private Vector2d targetPos;

  private Objectiv objectiv;

  private Parcel parcel;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size > 6)) {
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
      if ((_get_4 instanceof Vector2d)) {
        Object _get_5 = occurrence.parameters[2];
        this.speed = ((Vector2d) _get_5);
      }
      Object _get_6 = occurrence.parameters[3];
      if ((_get_6 instanceof Objectiv)) {
        Object _get_7 = occurrence.parameters[3];
        this.objectiv = ((Objectiv) _get_7);
      }
      Object _get_8 = occurrence.parameters[4];
      if ((_get_8 instanceof Vector2d)) {
        Object _get_9 = occurrence.parameters[4];
        this.targetPos = ((Vector2d) _get_9);
      }
      Object _get_10 = occurrence.parameters[5];
      if ((_get_10 instanceof Float)) {
        Object _get_11 = occurrence.parameters[5];
        this.battery = (((_get_11 == null ? null : PrimitiveCastExtensions.toFloat(_get_11))) == null ? 0 : ((_get_11 == null ? null : PrimitiveCastExtensions.toFloat(_get_11))).floatValue());
      }
      Object _get_12 = occurrence.parameters[6];
      if ((_get_12 instanceof String)) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        Object _get_13 = occurrence.parameters[6];
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName((_get_13 == null ? null : _get_13.toString()));
      }
      this.parcel = null;
    }
    if (Settings.isLogActivated) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info("Boids activated");
    }
  }

  @Pure
  protected boolean closeEnoughToTarget(final Vector2d v1, final Vector2d v2) {
    int distanceMin = Settings.distMinLiv;
    double _x = v2.getX();
    double _x_1 = v1.getX();
    double _pow = Math.pow((_x - _x_1), 2);
    double _y = v2.getY();
    double _y_1 = v1.getY();
    double _pow_1 = Math.pow((_y - _y_1), 2);
    double distance = Math.sqrt((_pow + _pow_1));
    return (distance <= distanceMin);
  }

  private void $behaviorUnit$UpdateAction$1(final UpdateAction occurrence) {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from void to Vector2d"
      + "\nType mismatch: cannot convert from void to Vector2d"
      + "\nType mismatch: cannot convert from void to Vector2d");
  }

  private void $behaviorUnit$AffectOrder$2(final AffectOrder occurrence) {
    this.parcel = occurrence.affectedparcel;
    this.targetPos = this.parcel.getHousePos();
    this.objectiv = Objectiv.GoLiv;
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    ValidateOrderReception _validateOrderReception = new ValidateOrderReception();
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_iD;
      
      public $SerializableClosureProxy(final UUID $_iD) {
        this.$_iD = $_iD;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_iD);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        UUID _iD_1 = occurrence.getSource().getID();
        return Objects.equal(_iD, _iD_1);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource().getID());
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_validateOrderReception, _function);
  }

  protected void moveTo(final Vector2d targetPos) {
    final Vector2d vector = targetPos.operator_minus(this.position);
    vector.setLength(2);
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
  private void $guardEvaluator$UpdateAction(final UpdateAction occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$UpdateAction$1(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$AffectOrder(final AffectOrder occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$AffectOrder$2(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(AffectOrder.class);
    toBeFilled.add(UpdateAction.class);
    toBeFilled.add(Initialize.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (AffectOrder.class.isAssignableFrom(event)) {
      return true;
    }
    if (UpdateAction.class.isAssignableFrom(event)) {
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
    if (event instanceof AffectOrder) {
      final AffectOrder occurrence = (AffectOrder) event;
      $guardEvaluator$AffectOrder(occurrence, callbacks);
    }
    if (event instanceof UpdateAction) {
      final UpdateAction occurrence = (UpdateAction) event;
      $guardEvaluator$UpdateAction(occurrence, callbacks);
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
    Drone other = (Drone) obj;
    if (!java.util.Objects.equals(this.environment, other.environment))
      return false;
    if (Float.floatToIntBits(other.battery) != Float.floatToIntBits(this.battery))
      return false;
    if (Float.floatToIntBits(other.weight) != Float.floatToIntBits(this.weight))
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
    result = prime * result + Float.hashCode(this.battery);
    result = prime * result + Float.hashCode(this.weight);
    return result;
  }

  @SyntheticMember
  public Drone(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public Drone(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}