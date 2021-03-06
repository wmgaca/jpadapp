package lib.ui.panels;

import lib.types.PAD;
import lib.types.PADValue;
import lib.types.Palette;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Draw single value's values
 */
public class ValuePanel extends lib.ui.panels.base.Panel {

    public ValuePanel(PAD.Type type, int width, int height) {
        super(type, width, height);
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d Graphics object to be drawn
     */
    @Override
    public void customPaintComponent(Graphics2D g2d) {
        PADValue current = data.getLastValue(type);

        if (null == current) {
            return;
        }

        // Colored background for Positiveness
        if (PAD.Type.P == type) {
            Color color = (current.getValue() > 0) ? Palette.green : Palette.red;
            color = Palette.getTransparent(color, current.getCertainty());

            g2d.setColor(color);

            Rectangle2D rect = new Rectangle2D.Double(margin.left, margin.top, getW(), getH());
            g2d.draw(rect);
            g2d.fill(rect);
        }

        g2d.setColor(Palette.black);
        String s = String.format("%.2f / %.2f", current.getValue(), current.getCertainty());
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));

        int sLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        g2d.drawString(s, getCenterX() - sLen / 2, getCenterY());
    }
}
