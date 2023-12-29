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

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Color;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Properties of a boids population/group.
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class Population {
  public static final double DEFAULT_REPULSION_FORCE = 5.0;

  public static final double DEFAULT_SEPARATION_FORCE = 1.0;

  public static final double DEFAULT_COHESION_FORCE = 0.0001;

  public static final double DEFAULT_ALIGNMENT_FORCE = 1.0;

  public static final double DEFAULT_REPULSION_DIST = 100.0;

  public static final double DEFAULT_SEPARATION_DIST = 10.0;

  public static final double DEFAULT_COHESION_DIST = 100.0;

  public static final double DEFAULT_ALIGNMENT_DIST = 100.0;

  public static final int DEFAULT_BOIDS_NB = 100;

  public Color color;

  public double maxSpeed;

  public double maxForce;

  public double distSeparation;

  public double distCohesion;

  public double distAlignment;

  public double distRepulsion;

  public double visibleAngleCos;

  public double separationForce;

  public double cohesionForce;

  public double alignmentForce;

  public double repulsionForce;

  public double mass;

  public boolean cohesionOn = true;

  public boolean repulsionOn = true;

  public boolean alignmentOn = true;

  public boolean separationOn = true;

  public int popSize;

  public Population(final Color col, final int n) {
    this.color = col;
    this.maxSpeed = 2;
    this.maxForce = 1.7;
    this.distSeparation = Population.DEFAULT_SEPARATION_DIST;
    this.distCohesion = Population.DEFAULT_COHESION_DIST;
    this.distAlignment = Population.DEFAULT_ALIGNMENT_DIST;
    this.distRepulsion = Population.DEFAULT_REPULSION_DIST;
    this.separationForce = Population.DEFAULT_SEPARATION_FORCE;
    this.cohesionForce = Population.DEFAULT_COHESION_FORCE;
    this.alignmentForce = Population.DEFAULT_ALIGNMENT_FORCE;
    this.repulsionForce = Population.DEFAULT_REPULSION_FORCE;
    this.mass = 1.0;
    this.visibleAngleCos = Math.cos(90.0);
    this.popSize = n;
  }

  public Population(final Color col) {
    this(col, Population.DEFAULT_BOIDS_NB);
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
    Population other = (Population) obj;
    if (Double.doubleToLongBits(other.maxSpeed) != Double.doubleToLongBits(this.maxSpeed))
      return false;
    if (Double.doubleToLongBits(other.maxForce) != Double.doubleToLongBits(this.maxForce))
      return false;
    if (Double.doubleToLongBits(other.distSeparation) != Double.doubleToLongBits(this.distSeparation))
      return false;
    if (Double.doubleToLongBits(other.distCohesion) != Double.doubleToLongBits(this.distCohesion))
      return false;
    if (Double.doubleToLongBits(other.distAlignment) != Double.doubleToLongBits(this.distAlignment))
      return false;
    if (Double.doubleToLongBits(other.distRepulsion) != Double.doubleToLongBits(this.distRepulsion))
      return false;
    if (Double.doubleToLongBits(other.visibleAngleCos) != Double.doubleToLongBits(this.visibleAngleCos))
      return false;
    if (Double.doubleToLongBits(other.separationForce) != Double.doubleToLongBits(this.separationForce))
      return false;
    if (Double.doubleToLongBits(other.cohesionForce) != Double.doubleToLongBits(this.cohesionForce))
      return false;
    if (Double.doubleToLongBits(other.alignmentForce) != Double.doubleToLongBits(this.alignmentForce))
      return false;
    if (Double.doubleToLongBits(other.repulsionForce) != Double.doubleToLongBits(this.repulsionForce))
      return false;
    if (Double.doubleToLongBits(other.mass) != Double.doubleToLongBits(this.mass))
      return false;
    if (other.cohesionOn != this.cohesionOn)
      return false;
    if (other.repulsionOn != this.repulsionOn)
      return false;
    if (other.alignmentOn != this.alignmentOn)
      return false;
    if (other.separationOn != this.separationOn)
      return false;
    if (other.popSize != this.popSize)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Double.hashCode(this.maxSpeed);
    result = prime * result + Double.hashCode(this.maxForce);
    result = prime * result + Double.hashCode(this.distSeparation);
    result = prime * result + Double.hashCode(this.distCohesion);
    result = prime * result + Double.hashCode(this.distAlignment);
    result = prime * result + Double.hashCode(this.distRepulsion);
    result = prime * result + Double.hashCode(this.visibleAngleCos);
    result = prime * result + Double.hashCode(this.separationForce);
    result = prime * result + Double.hashCode(this.cohesionForce);
    result = prime * result + Double.hashCode(this.alignmentForce);
    result = prime * result + Double.hashCode(this.repulsionForce);
    result = prime * result + Double.hashCode(this.mass);
    result = prime * result + Boolean.hashCode(this.cohesionOn);
    result = prime * result + Boolean.hashCode(this.repulsionOn);
    result = prime * result + Boolean.hashCode(this.alignmentOn);
    result = prime * result + Boolean.hashCode(this.separationOn);
    result = prime * result + Integer.hashCode(this.popSize);
    return result;
  }
}
