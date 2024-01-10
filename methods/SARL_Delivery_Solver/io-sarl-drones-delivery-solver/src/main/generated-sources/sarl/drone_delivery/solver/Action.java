/**
 * Events and shared data for the simulation
 */
package drone_delivery.solver;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * Event sent by the drone to the environment to inform it of its action
 */
@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class Action extends Event {
  public final Vector2d influence;

  public final PerceivedDroneBody newDroneState;

  public Action(final Vector2d inf, final PerceivedDroneBody inewDroneState) {
    Vector2d _vector2d = new Vector2d(inf);
    this.influence = _vector2d;
    PerceivedDroneBody _perceivedDroneBody = new PerceivedDroneBody(inewDroneState);
    this.newDroneState = _perceivedDroneBody;
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    return result;
  }

  /**
   * Returns a String representation of the Action event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("influence", this.influence);
    builder.add("newDroneState", this.newDroneState);
  }

  @SyntheticMember
  private static final long serialVersionUID = -77332869L;
}
