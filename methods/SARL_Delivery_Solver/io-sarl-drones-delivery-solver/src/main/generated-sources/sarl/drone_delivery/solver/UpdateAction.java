package drone_delivery.solver;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class UpdateAction extends Event {
  public final ConcurrentHashMap<UUID, PerceivedDroneBody> perceivedAgentBody;

  public final Vector2d depotPos;

  public final int time;

  public UpdateAction(final ConcurrentHashMap<UUID, PerceivedDroneBody> bodies, final Vector2d newdepotPos, final int itime) {
    ConcurrentHashMap<UUID, PerceivedDroneBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedDroneBody>(bodies);
    this.perceivedAgentBody = _concurrentHashMap;
    Vector2d _vector2d = new Vector2d(newdepotPos);
    this.depotPos = _vector2d;
    this.time = itime;
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
    UpdateAction other = (UpdateAction) obj;
    if (other.time != this.time)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.time);
    return result;
  }

  /**
   * Returns a String representation of the UpdateAction event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("perceivedAgentBody", this.perceivedAgentBody);
    builder.add("depotPos", this.depotPos);
    builder.add("time", this.time);
  }

  @SyntheticMember
  private static final long serialVersionUID = -597208172L;
}
