package uni.dc.util;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

public class UbsMousePlugin<V, E> extends PickingGraphMousePlugin<V, E> {

	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent e) {
		VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
		if (e.getModifiers() == this.modifiers) {
			if (this.down != null) {
				Point2D out = e.getPoint();
				if ((this.vertex == null) && (!heyThatsTooClose(this.down, out, 5.0D))) {
					pickContainedVertices(vv, this.down, out, true);
				}
				System.out.println(this.vertex);
			}
		} else if ((e.getModifiers() == this.addToSelectionModifiers) && (this.down != null)) {
			Point2D out = e.getPoint();
			if ((this.vertex == null) && (!heyThatsTooClose(this.down, out, 5.0D))) {
				pickContainedVertices(vv, this.down, out, false);
			}
		}
		this.down = null;
		this.vertex = null;
		this.edge = null;
		this.rect.setFrame(0.0D, 0.0D, 0.0D, 0.0D);
		vv.removePostRenderPaintable(this.lensPaintable);
		vv.repaint();
	}

	private boolean heyThatsTooClose(Point2D p, Point2D q, double min) {
		return (Math.abs(p.getX() - q.getX()) < min) && (Math.abs(p.getY() - q.getY()) < min);
	}

}
