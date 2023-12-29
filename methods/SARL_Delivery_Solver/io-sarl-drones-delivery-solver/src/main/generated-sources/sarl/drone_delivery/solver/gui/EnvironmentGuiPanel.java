package drone_delivery.solver.gui;

import drone_delivery.solver.PerceivedBoidBody;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * The GUI of the Simulation
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
class EnvironmentGuiPanel extends Panel {
  /**
   * Double buffering management.
   */
  private Graphics myGraphics;

  /**
   * Double buffering management.
   */
  private Graphics myCanvas;

  /**
   * Double buffering management.
   */
  private Image myImage;

  private int width;

  private int height;

  private Map<UUID, PerceivedBoidBody> boids;

  public void setBoids(final Map<UUID, PerceivedBoidBody> boids) {
    this.boids = boids;
  }

  public EnvironmentGuiPanel(final int iheight, final int iwidth, final Map<UUID, PerceivedBoidBody> iboids) {
    super();
    this.width = iwidth;
    this.height = iheight;
    this.boids = iboids;
  }

  @Override
  public void paint(final Graphics g) {
    if (((this.myCanvas != null) && (this.myGraphics != null))) {
      final Color bgColor = new Color(0.6F, 0.6F, 0.6F);
      this.myCanvas.setColor(bgColor);
      this.myCanvas.fillRect(0, 0, ((this.width * 2) - 1), ((this.height * 2) - 1));
      this.myCanvas.setColor(Color.BLACK);
      this.myCanvas.drawRect(0, 0, ((this.width * 2) - 1), ((this.height * 2) - 1));
      Collection<PerceivedBoidBody> _values = this.boids.values();
      for (final PerceivedBoidBody boid : _values) {
        this.paintBoid(this.myCanvas, boid);
      }
      this.myGraphics.drawImage(this.myImage, 0, 0, this);
    }
  }

  public void update(final Graphics g) {
    this.paint(g);
  }

  @Override
  public void doLayout() {
    super.doLayout();
    this.width = (this.getSize().width / 2);
    this.height = (this.getSize().height / 2);
    this.myImage = this.createImage((this.width * 2), (this.height * 2));
    this.myCanvas = this.myImage.getGraphics();
    this.myGraphics = this.getGraphics();
  }

  public void paintBoid(final Graphics g, final PerceivedBoidBody boid) {
    double _x = boid.getPosition().getX();
    int posX = (this.width + ((int) _x));
    double _y = boid.getPosition().getY();
    int posY = (this.height + ((int) _y));
    double direction = EnvironmentGuiPanel.getAngle(boid.getVitesse());
    double cos = Math.cos(direction);
    double sin = Math.sin(direction);
    g.setColor(boid.getGroup().color);
    g.drawLine((posX + ((int) (5 * cos))), (posY + ((int) (5 * sin))), (posX - ((int) ((2 * cos) + (2 * sin)))), 
      (posY - ((int) ((2 * sin) - (2 * cos)))));
    g.drawLine((posX + ((int) (5 * cos))), (posY + ((int) (5 * sin))), (posX - ((int) ((2 * cos) - (2 * sin)))), 
      (posY - ((int) ((2 * sin) + (2 * cos)))));
    g.drawLine((posX - ((int) ((2 * cos) + (2 * sin)))), (posY - ((int) ((2 * sin) - (2 * cos)))), 
      (posX - ((int) ((2 * cos) - (2 * sin)))), (posY - ((int) ((2 * sin) + (2 * cos)))));
  }

  @Pure
  private static double getAngle(final Vector2d v) {
    double zero = 1E-9;
    double _x = v.getX();
    double _x_1 = v.getX();
    if (((_x * _x_1) < zero)) {
      double _y = v.getY();
      if ((_y >= 0)) {
        return (Math.PI / 2);
      }
      return (((-1) * Math.PI) / 2);
    }
    double _x_2 = v.getX();
    if ((_x_2 >= 0)) {
      double _y_1 = v.getY();
      double _x_3 = v.getX();
      return Math.atan((_y_1 / _x_3));
    }
    double _y_2 = v.getY();
    if ((_y_2 >= 0)) {
      double _y_3 = v.getY();
      double _x_4 = v.getX();
      double _atan = Math.atan((_y_3 / _x_4));
      return (Math.PI + _atan);
    }
    double _y_4 = v.getY();
    double _x_5 = v.getX();
    double _atan_1 = Math.atan((_y_4 / _x_5));
    return (_atan_1 - Math.PI);
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
    EnvironmentGuiPanel other = (EnvironmentGuiPanel) obj;
    if (other.width != this.width)
      return false;
    if (other.height != this.height)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.width);
    result = prime * result + Integer.hashCode(this.height);
    return result;
  }

  @SyntheticMember
  private static final long serialVersionUID = 334567416L;
}
