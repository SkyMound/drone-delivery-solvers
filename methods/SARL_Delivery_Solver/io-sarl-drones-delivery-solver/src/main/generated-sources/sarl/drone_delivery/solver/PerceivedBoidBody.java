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
import java.util.Objects;
import java.util.UUID;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * The representation of a boid in the environment : his physical body
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class PerceivedBoidBody {
  @Accessors
  private Vector2d position;

  @Accessors
  private UUID owner;

  @Accessors
  private Vector2d vitesse;

  @Accessors
  private Vector2d acceleration;

  @Accessors
  private Population group;

  public PerceivedBoidBody(final Population igroup, final UUID iowner, final Vector2d iposition, final Vector2d ispeed) {
    this.position = iposition;
    this.owner = iowner;
    this.vitesse = ispeed;
    Vector2d _vector2d = new Vector2d();
    this.acceleration = _vector2d;
    this.group = igroup;
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
    PerceivedBoidBody other = (PerceivedBoidBody) obj;
    if (!Objects.equals(this.owner, other.owner))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.owner);
    return result;
  }

  @Pure
  public Vector2d getPosition() {
    return this.position;
  }

  public void setPosition(final Vector2d position) {
    this.position = position;
  }

  @Pure
  public UUID getOwner() {
    return this.owner;
  }

  public void setOwner(final UUID owner) {
    this.owner = owner;
  }

  @Pure
  public Vector2d getVitesse() {
    return this.vitesse;
  }

  public void setVitesse(final Vector2d vitesse) {
    this.vitesse = vitesse;
  }

  @Pure
  public Vector2d getAcceleration() {
    return this.acceleration;
  }

  public void setAcceleration(final Vector2d acceleration) {
    this.acceleration = acceleration;
  }

  @Pure
  public Population getGroup() {
    return this.group;
  }

  public void setGroup(final Population group) {
    this.group = group;
  }
}
