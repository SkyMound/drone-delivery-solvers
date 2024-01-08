package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class Parcel {
  @Accessors
  private Vector2d housPos;

  @Accessors
  private float weight;

  private int ordertime;

  private int idOrder;

  public Parcel(final Parcel other) {
    this.housPos = other.housPos;
    this.weight = other.weight;
    this.ordertime = other.ordertime;
    this.idOrder = other.idOrder;
  }

  public Parcel(final Vector2d ihousPos, final float iweight, final int iordertime, final int iidOrder) {
    this.housPos = ihousPos;
    this.weight = iweight;
    this.ordertime = iordertime;
    this.idOrder = iidOrder;
  }

  @Pure
  public Vector2d getHousePos() {
    return this.housPos;
  }

  @Pure
  public int getOrderTime() {
    return this.ordertime;
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
    Parcel other = (Parcel) obj;
    if (Float.floatToIntBits(other.weight) != Float.floatToIntBits(this.weight))
      return false;
    if (other.ordertime != this.ordertime)
      return false;
    if (other.idOrder != this.idOrder)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Float.hashCode(this.weight);
    result = prime * result + Integer.hashCode(this.ordertime);
    result = prime * result + Integer.hashCode(this.idOrder);
    return result;
  }

  @Pure
  public Vector2d getHousPos() {
    return this.housPos;
  }

  public void setHousPos(final Vector2d housPos) {
    this.housPos = housPos;
  }

  @Pure
  public float getWeight() {
    return this.weight;
  }

  public void setWeight(final float weight) {
    this.weight = weight;
  }
}
