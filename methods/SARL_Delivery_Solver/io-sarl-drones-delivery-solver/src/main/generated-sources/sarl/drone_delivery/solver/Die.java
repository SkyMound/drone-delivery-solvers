package drone_delivery.solver;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;

/**
 * Event from the GUI to kill each agent to end the simulation before closing the main window
 */
@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class Die extends Event {
  @SyntheticMember
  public Die() {
    super();
  }

  @SyntheticMember
  public Die(final Address source) {
    super(source);
  }

  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
