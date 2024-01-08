package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.Objects;
import java.util.UUID;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class PerceivedDroneBody {
  @Accessors
  private Vector2d position;

  @Accessors
  private UUID owner;

  @Accessors
  private Vector2d vitesse;

  @Accessors
  private Vector2d acceleration;

  @Accessors
  private Objectiv objectiv;

  @Accessors
  private float battery;

  @Accessors
  private Vector2d targetPos;

  @Accessors
  private float weight;

  public PerceivedDroneBody(final UUID iowner, final Vector2d iposition, final Vector2d ispeed, final Objectiv iobjectiv, final Vector2d itargetPos, final float ibattery, final float iweight) {
    this.position = iposition;
    this.owner = iowner;
    this.vitesse = ispeed;
    Vector2d _vector2d = new Vector2d();
    this.acceleration = _vector2d;
    this.objectiv = iobjectiv;
    this.targetPos = itargetPos;
    this.battery = ibattery;
    this.weight = iweight;
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
    PerceivedDroneBody other = (PerceivedDroneBody) obj;
    if (!Objects.equals(this.owner, other.owner))
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
    result = prime * result + Objects.hashCode(this.owner);
    result = prime * result + Float.hashCode(this.battery);
    result = prime * result + Float.hashCode(this.weight);
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
  public Objectiv getObjectiv() {
    return this.objectiv;
  }

  public void setObjectiv(final Objectiv objectiv) {
    this.objectiv = objectiv;
  }

  @Pure
  public float getBattery() {
    return this.battery;
  }

  public void setBattery(final float battery) {
    this.battery = battery;
  }

  @Pure
  public Vector2d getTargetPos() {
    return this.targetPos;
  }

  public void setTargetPos(final Vector2d targetPos) {
    this.targetPos = targetPos;
  }

  @Pure
  public float getWeight() {
    return this.weight;
  }

  public void setWeight(final float weight) {
    this.weight = weight;
  }
}
