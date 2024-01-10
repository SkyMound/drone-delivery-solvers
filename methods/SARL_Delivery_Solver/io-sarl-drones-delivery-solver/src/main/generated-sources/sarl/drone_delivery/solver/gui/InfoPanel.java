package drone_delivery.solver.gui;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Label;
import java.awt.Panel;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
class InfoPanel extends Panel {
  private Label meanTimeText;

  private int width;

  private int height;

  public InfoPanel(final int iwidth) {
    super();
    Label _label = new Label();
    this.meanTimeText = _label;
    this.meanTimeText.setText(((((" Le temps moyen de livraison esrt de :" + Integer.valueOf(0)) + " minutes et ") + Integer.valueOf(0)) + 
      "secondes"));
    this.add(this.meanTimeText);
  }

  public void updateMeanTime(final int newMean) {
    int hours = (newMean / 3600);
    int remaining_seconds = (newMean % 3600);
    int minutes = (remaining_seconds / 60);
    int seconds = (remaining_seconds % 60);
    this.meanTimeText.setText(((((((" Le temps moyen de livraison est de : " + Integer.valueOf(hours)) + " heures ") + Integer.valueOf(minutes)) + " minutes et ") + Integer.valueOf(seconds)) + 
      " secondes"));
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
    InfoPanel other = (InfoPanel) obj;
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
  private static final long serialVersionUID = -3135938055L;
}
