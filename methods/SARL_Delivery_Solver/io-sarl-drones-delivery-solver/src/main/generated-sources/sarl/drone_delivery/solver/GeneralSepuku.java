package drone_delivery.solver;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class GeneralSepuku extends Event {
  @SyntheticMember
  public GeneralSepuku() {
    super();
  }

  @SyntheticMember
  public GeneralSepuku(final Address source) {
    super(source);
  }

  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
