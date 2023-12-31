package drone_delivery.solver.gui;

import drone_delivery.solver.PerceivedDroneBody;
import io.sarl.api.core.OpenEventSpace;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class EnvironmentGui extends Frame {
  private Closer handler;

  private EnvironmentGuiPanel panel;

  public EnvironmentGui(final OpenEventSpace comspace, final int iheight, final int iwidth, final Map<UUID, PerceivedDroneBody> idrones, final Vector2d idepotPos, final List<Vector2d> ihousesPos) {
    super();
    Closer _closer = new Closer(this, comspace);
    this.handler = _closer;
    EnvironmentGuiPanel _environmentGuiPanel = new EnvironmentGuiPanel(iheight, iwidth, idrones, idepotPos, ihousesPos);
    this.panel = _environmentGuiPanel;
    this.setTitle("Drone Delivery Simulation");
    this.setSize(iwidth, iheight);
    this.addWindowListener(this.handler);
    this.add("Center", this.panel);
    this.setVisible(true);
  }

  public void setDrones(final Map<UUID, PerceivedDroneBody> drones) {
    this.panel.setDrones(drones);
  }

  public void setDepotPos(final Vector2d depotpos) {
    this.panel.setDepotPos(depotpos);
  }

  public void setHousesPos(final List<Vector2d> housesPos) {
    this.panel.setHousesPos(housesPos);
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
  private static final long serialVersionUID = -745199466L;
}
