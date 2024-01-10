package drone_delivery.solver;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class ParcelDelivered extends Event {
  public final int deliveredDuration;

  public ParcelDelivered(final int duration) {
    this.deliveredDuration = duration;
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
    ParcelDelivered other = (ParcelDelivered) obj;
    if (other.deliveredDuration != this.deliveredDuration)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.deliveredDuration);
    return result;
  }

  /**
   * Returns a String representation of the ParcelDelivered event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("deliveredDuration", this.deliveredDuration);
  }

  @SyntheticMember
  private static final long serialVersionUID = 1356538557L;
}
