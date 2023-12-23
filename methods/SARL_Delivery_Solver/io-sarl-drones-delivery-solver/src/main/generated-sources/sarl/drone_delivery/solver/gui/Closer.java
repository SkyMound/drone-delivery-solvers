package drone_delivery.solver.gui;

import drone_delivery.solver.Die;
import io.sarl.api.core.OpenEventSpace;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
class Closer extends WindowAdapter {
  private OpenEventSpace space;

  private Frame iframe;

  public Closer(final Frame parent, final OpenEventSpace comspace) {
    this.space = comspace;
    this.iframe = parent;
  }

  /**
   * Clean the simulation asking the agents to die before disposing the window
   */
  @Override
  public void windowClosing(final WindowEvent event) {
    UUID _randomUUID = UUID.randomUUID();
    Die _die = new Die();
    this.space.emit(_randomUUID, _die, null);
    this.iframe.dispose();
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
}
