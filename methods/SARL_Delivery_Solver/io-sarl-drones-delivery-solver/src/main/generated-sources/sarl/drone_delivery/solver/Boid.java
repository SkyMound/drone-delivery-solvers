/**
 * $Id$
 * 
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 * 
 * Copyright (C) 2014-2023 SARL.io, the Original Authors and Main Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A boid agent evolving according C. Reynolds basic behavioral rules
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SuppressWarnings("potential_field_synchronization_problem")
@SarlSpecification("0.13")
@SarlElementType(19)
public class Boid extends Agent {
  private UUID environment;

  private Vector2d position;

  private Vector2d speed;

  private Population group;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size > 4)) {
      Object _get = occurrence.parameters[0];
      if ((_get instanceof UUID)) {
        Object _get_1 = occurrence.parameters[0];
        this.environment = (_get_1 == null ? null : PrimitiveCastExtensions.toUUID(_get_1));
      }
      Object _get_2 = occurrence.parameters[1];
      if ((_get_2 instanceof Population)) {
        Object _get_3 = occurrence.parameters[1];
        this.group = ((Population) _get_3);
      }
      Object _get_4 = occurrence.parameters[2];
      if ((_get_4 instanceof Vector2d)) {
        Object _get_5 = occurrence.parameters[2];
        this.position = ((Vector2d) _get_5);
      }
      Object _get_6 = occurrence.parameters[3];
      if ((_get_6 instanceof Vector2d)) {
        Object _get_7 = occurrence.parameters[3];
        this.speed = ((Vector2d) _get_7);
        this.speed.setLength(0.25);
        Vector2d _vector2d = new Vector2d(0, 0.75);
        this.speed.operator_add(_vector2d);
        this.speed.scale(this.group.maxSpeed);
      }
      Object _get_8 = occurrence.parameters[4];
      if ((_get_8 instanceof String)) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        Object _get_9 = occurrence.parameters[4];
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName((_get_9 == null ? null : _get_9.toString()));
      }
    }
    if (Settings.isLogActivated) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info("Boids activated");
    }
  }

  private void $behaviorUnit$Perception$1(final Perception occurrence) {
    ConcurrentHashMap<UUID, PerceivedBoidBody> boids = occurrence.perceivedAgentBody;
    PerceivedBoidBody myBody = boids.get(this.getID());
    if (((myBody != null) && Objects.equal(myBody.getOwner(), this.getID()))) {
      this.position = myBody.getPosition();
      this.speed = myBody.getVitesse();
    }
    Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
    final Procedure1<Agent> _function = (Agent it) -> {
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      Action _action = new Action();
      final Procedure1<Action> _function_1 = (Action it_1) -> {
        it_1.influence = this.think(boids.values());
      };
      Action _doubleArrow = ObjectExtensions.<Action>operator_doubleArrow(_action, _function_1);
      class $SerializableClosureProxy implements Scope<Address> {
        
        private final UUID $_environment;
        
        public $SerializableClosureProxy(final UUID $_environment) {
          this.$_environment = $_environment;
        }
        
        @Override
        public boolean matches(final Address it_1) {
          UUID _iD = it_1.getID();
          return Objects.equal(_iD, $_environment);
        }
      }
      final Scope<Address> _function_2 = new Scope<Address>() {
        @Override
        public boolean matches(final Address it_1) {
          UUID _iD = it_1.getID();
          return Objects.equal(_iD, Boid.this.environment);
        }
        private Object writeReplace() throws ObjectStreamException {
          return new SerializableProxy($SerializableClosureProxy.class, Boid.this.environment);
        }
      };
      _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_doubleArrow, _function_2);
      if (Settings.isLogActivated) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("Sending Influences.");
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(Settings.pause, _function);
  }

  private void $behaviorUnit$Die$2(final Die occurrence) {
    Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
  }

  /**
   * The core boids behavior : aggregating all forces into a influence
   */
  protected Vector2d think(final Collection<PerceivedBoidBody> perception) {
    if ((perception != null)) {
      Vector2d force = null;
      Vector2d influence = new Vector2d();
      influence.set(0, 0);
      if (this.group.separationOn) {
        force = this.separation(perception);
        force.scale(this.group.separationForce);
        influence.operator_add(force);
      }
      if (this.group.cohesionOn) {
        force = this.cohesion(perception);
        force.scale(this.group.cohesionForce);
        influence.operator_add(force);
      }
      if (this.group.alignmentOn) {
        force = this.alignment(perception);
        force.scale(this.group.alignmentForce);
        influence.operator_add(force);
      }
      if (this.group.repulsionOn) {
        force = this.repulsion(perception);
        force.scale(this.group.repulsionForce);
        influence.operator_add(force);
      }
      double _length = influence.getLength();
      if ((_length > this.group.maxForce)) {
        influence.setLength(this.group.maxForce);
      }
      influence.scale((1 / this.group.mass));
      return influence;
    }
    return null;
  }

  /**
   * Determine whether a body is visible or not according to the perception range
   */
  @Pure
  protected boolean isVisible(final PerceivedBoidBody otherBoid, final double distance) {
    Vector2d _position = otherBoid.getPosition();
    Vector2d tmp = _position.operator_minus(this.position);
    double _length = tmp.getLength();
    if ((_length > distance)) {
      return false;
    }
    Vector2d tmp2 = this.speed.clone();
    tmp2.normalize();
    double _multiply = tmp2.operator_multiply(tmp);
    if ((_multiply < this.group.visibleAngleCos)) {
      return false;
    }
    return true;
  }

  /**
   * Compute the separation force.
   */
  protected Vector2d separation(final Collection<PerceivedBoidBody> otherBoids) {
    Vector2d force = new Vector2d();
    double len = 0.0;
    for (final PerceivedBoidBody otherBoid : otherBoids) {
      if (((((otherBoid != null) && (!Objects.equal(otherBoid.getOwner(), this.getID()))) && Objects.equal(otherBoid.getGroup(), this.group)) && this.isVisible(otherBoid, this.group.distSeparation))) {
        Vector2d _position = otherBoid.getPosition();
        Vector2d tmp = this.position.operator_minus(_position);
        len = tmp.getLength();
        double _power = Math.pow(len, 2);
        tmp.scale((1.0 / _power));
        force.operator_add(tmp);
      }
    }
    return force;
  }

  /**
   * Compute the cohesion force.
   */
  protected Vector2d cohesion(final Collection<PerceivedBoidBody> otherBoids) {
    int nbTot = 0;
    Vector2d force = new Vector2d();
    for (final PerceivedBoidBody otherBoid : otherBoids) {
      if (((((otherBoid != null) && (!Objects.equal(otherBoid.getOwner(), this.getID()))) && Objects.equal(otherBoid.getGroup(), this.group)) && this.isVisible(otherBoid, this.group.distCohesion))) {
        nbTot++;
        Vector2d _position = otherBoid.getPosition();
        force.operator_add(_position);
      }
    }
    if ((nbTot > 0)) {
      force.scale((1.0 / nbTot));
      force.operator_remove(this.position);
    }
    return force;
  }

  /**
   * Compute the alignment force.
   */
  protected Vector2d alignment(final Collection<PerceivedBoidBody> otherBoids) {
    int nbTot = 0;
    Vector2d tmp = new Vector2d();
    Vector2d force = new Vector2d();
    for (final PerceivedBoidBody otherBoid : otherBoids) {
      if (((((otherBoid != null) && (!Objects.equal(otherBoid.getOwner(), this.getID()))) && Objects.equal(otherBoid.getGroup(), this.group)) && this.isVisible(otherBoid, this.group.distAlignment))) {
        nbTot++;
        tmp.set(otherBoid.getVitesse());
        double _length = tmp.getLength();
        tmp.scale((1.0 / _length));
        force.operator_add(tmp);
      }
    }
    if ((nbTot > 0)) {
      force.scale((1.0 / nbTot));
    }
    return force;
  }

  /**
   * Compute the repulsion force.
   */
  protected Vector2d repulsion(final Collection<PerceivedBoidBody> otherBoids) {
    Vector2d force = new Vector2d();
    double len = 0.0;
    for (final PerceivedBoidBody otherBoid : otherBoids) {
      if (((((otherBoid != null) && (!Objects.equal(otherBoid.getOwner(), this.getID()))) && (!Objects.equal(otherBoid.getGroup(), this.group))) && 
        this.isVisible(otherBoid, this.group.distRepulsion))) {
        Vector2d _position = otherBoid.getPosition();
        Vector2d tmp = this.position.operator_minus(_position);
        len = tmp.getLength();
        double _power = Math.pow(len, 2);
        tmp.scale((1 / _power));
        force.operator_add(tmp);
      }
    }
    return force;
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
  @PerceptGuardEvaluator
  private void $guardEvaluator$Die(final Die occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Die$2(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Die.class);
    toBeFilled.add(Perception.class);
    toBeFilled.add(Initialize.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Die.class.isAssignableFrom(event)) {
      return true;
    }
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
    if (event instanceof Die) {
      final Die occurrence = (Die) event;
      $guardEvaluator$Die(occurrence, callbacks);
    }
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
    Boid other = (Boid) obj;
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
  public Boid(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public Boid(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
