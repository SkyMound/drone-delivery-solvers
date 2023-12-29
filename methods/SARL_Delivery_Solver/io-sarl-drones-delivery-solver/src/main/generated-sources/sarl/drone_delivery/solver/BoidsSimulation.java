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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * The boids simulation launching the SARL environment with the corresponding agent and ensuring the communication between agents and the GUI
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class BoidsSimulation implements EventListener {
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
  private Map<Population, Integer> boidsToLaunch;

  /**
   * Map buffering boids' bodies before launch/start
   */
  private ConcurrentHashMap<UUID, PerceivedBoidBody> boidBodies;

  private int boidsCount;

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

  public BoidsSimulation() {
    this.boidsCount = 0;
    ConcurrentHashMap<UUID, PerceivedBoidBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedBoidBody>();
    this.boidBodies = _concurrentHashMap;
    this.boidsToLaunch = CollectionLiterals.<Population, Integer>newHashMap();
  }

  public void start() {
    this.launchAllAgents();
    this.isSimulationStarted = true;
  }

  public void stop() {
    this.killAllAgents();
    this.isSimulationStarted = false;
  }

  /**
   * Add the boids of a population to the simulation.
   * 
   * @param p - the population to add.
   */
  public void addBoid(final Population p) {
    this.boidsCount++;
    if ((!this.isSimulationStarted)) {
      Integer currentBoidCount = this.boidsToLaunch.get(p);
      if ((currentBoidCount != null)) {
        currentBoidCount++;
      } else {
        Integer _integer = new Integer(1);
        currentBoidCount = _integer;
      }
      this.boidsToLaunch.put(p, currentBoidCount);
    } else {
      this.launchBoid(p, ("Boid" + Integer.valueOf(this.boidsCount)));
    }
  }

  private void launchAllAgents() {
    try {
      this.kernel = SRE.getBootstrap();
      this.defaultSARLContext = this.kernel.startWithoutAgent();
      this.environment = UUID.randomUUID();
      this.kernel.startAgentWithID(Environment.class, this.environment, Integer.valueOf(this.height), Integer.valueOf(this.width));
      this.launchAllBoids();
      EventSpace _defaultSpace = this.defaultSARLContext.getDefaultSpace();
      this.space = ((OpenEventSpace) _defaultSpace);
      EnvironmentGui _environmentGui = new EnvironmentGui(this.space, this.height, this.width, this.boidBodies);
      this.myGUI = _environmentGui;
      this.space.registerWeakParticipant(this);
      Start _start = new Start(this.boidBodies);
      this.space.emit(BoidsSimulation.id, _start, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void launchAllBoids() {
    int boidNum = 0;
    Set<Map.Entry<Population, Integer>> _entrySet = this.boidsToLaunch.entrySet();
    for (final Map.Entry<Population, Integer> e : _entrySet) {
      for (int i = 0; (i < e.getValue().doubleValue()); i++) {
        {
          boidNum++;
          this.launchBoid(e.getKey(), ("Boid" + Integer.valueOf(boidNum)));
        }
      }
    }
  }

  @SuppressWarnings({ "constant_condition", "discouraged_reference" })
  private void launchBoid(final Population p, final String boidName) {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from Class<Boid> to Class<? extends Agent>");
  }

  private void killAllAgents() {
  }

  @Override
  @Pure
  public UUID getID() {
    return BoidsSimulation.id;
  }

  /**
   * Methods managing event coming from agents
   */
  @Override
  public void receiveEvent(final Event event) {
    if ((event instanceof GuiRepaint)) {
      this.myGUI.setBoids(((GuiRepaint)event).perceivedAgentBody);
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
    BoidsSimulation other = (BoidsSimulation) obj;
    if (!Objects.equals(this.environment, other.environment))
      return false;
    if (other.width != this.width)
      return false;
    if (other.height != this.height)
      return false;
    if (other.boidsCount != this.boidsCount)
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
    result = prime * result + Integer.hashCode(this.boidsCount);
    result = prime * result + Boolean.hashCode(this.isSimulationStarted);
    return result;
  }
}
