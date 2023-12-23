package drone_delivery.solver.gui;

import drone_delivery.solver.PerceivedBoidBody;
import io.sarl.api.core.OpenEventSpace;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.Map;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Graphical user interface for boids.
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class EnvironmentGui extends Frame {
  private Closer handler;

  private EnvironmentGuiPanel panel;

  public EnvironmentGui(final OpenEventSpace comspace, final int iheight, final int iwidth, final Map<UUID, PerceivedBoidBody> iboids) {
    super();
    Closer _closer = new Closer(this, comspace);
    this.handler = _closer;
    EnvironmentGuiPanel _environmentGuiPanel = new EnvironmentGuiPanel(iheight, iwidth, iboids);
    this.panel = _environmentGuiPanel;
    this.setTitle("Boids Simulation");
    this.setSize(iwidth, iheight);
    this.addWindowListener(this.handler);
    this.add("Center", this.panel);
    this.setVisible(true);
  }

  public void setBoids(final Map<UUID, PerceivedBoidBody> boids) {
    this.panel.setBoids(boids);
  }

  @Override
  public void paint(final Graphics g) {
    super.paint(g);
    this.panel.paint(g);
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

  @SyntheticMember
  private static final long serialVersionUID = 4018038839L;
}
