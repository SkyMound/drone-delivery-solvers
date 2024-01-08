package drone_delivery.solver;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class GuiRepaint extends Event {
  public final ConcurrentHashMap<UUID, PerceivedDroneBody> perceivedAgentBody;

  public GuiRepaint(final ConcurrentHashMap<UUID, PerceivedDroneBody> bodies) {
    ConcurrentHashMap<UUID, PerceivedDroneBody> _concurrentHashMap = new ConcurrentHashMap<UUID, PerceivedDroneBody>(bodies);
    this.perceivedAgentBody = _concurrentHashMap;
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
   * Returns a String representation of the GuiRepaint event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("perceivedAgentBody", this.perceivedAgentBody);
  }

  @SyntheticMember
  private static final long serialVersionUID = -1145537443L;
}
