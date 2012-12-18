package lib.ui.panels.base;

import lib.types.PAD;
import lib.types.PADState;
import lib.ui.Margin;

import javax.swing.*;
import java.awt.*;

/**
 * Basic type for all panels used by the visualiser.
 *
 * @author Wojciech Gaca
 */
public abstract class Panel extends JPanel {

    /**
     * Time span in seconds.
     */
    protected int buffer = 15;

    /**
     * Data type visualised by the widget.
     */
    protected PAD.Type type;

    /**
     * Widget's label.
     */
    protected String label;

    /**
     * Drawing margins.
     */
    protected Margin margin = new Margin(10, 10, 10, 10);

    /**
     * Initializes the widget.
     *
     * @param type Panel.type
     * @param width Widget's width resolution (in pixels)
     * @param height Widget's height resolution (in pixels)
     */
    public Panel(PAD.Type type, int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.type = type;
        this.label = PAD.getName(type);
    }

    /**
     * Update widget's data with a new state. This method is called by feed().
     *
     * @see #feed(lib.types.PADState)
     * @param state State
     */
    protected abstract void feedState(PADState state);

    /**
     * Update widget's data with a new state.
     *
     * @param state State
     */
    public final void feed(PADState state) {
        feedState(state);
        repaint();
    }

    /**
     * Widget's drawing logic. This method is called by Panel.paintComponent.
     *
     * @param g2d Graphics object to be drawn
     */
    public abstract void customPaintComponent(Graphics2D g2d);

    /**
     * Widget's drawing logic.
     *
     * @see #paintComponent(java.awt.Graphics)
     * @param graphics Graphics object to be drawn
     */
    @Override
    final public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D)graphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);


        this.customPaintComponent((Graphics2D)graphics);
    }

    /**
     * Drawing area width.
     *
     * @return Drawing area width in pixels.
     */
    public int getW() {
        return this.getWidth() - this.margin.left - this.margin.right;
    }

    /**
     * Drawing area height.
     *
     * @return Drawing area height in pixels.
     */
    public int getH() {
        return this.getHeight() - this.margin.top - this.margin.bottom;
    }

    /**
     * X coordinate of the drawing area's center.
     *
     * @see #getW()
     *
     * @return X coordinate of the drawing area's center.
     */
    public int getCenterX() {
        return margin.left + getW() / 2;
    }

    /**
     * Y coordinate of the drawing area's center.
     *
     * @see #getH()
     *
     * @return Y coordinate of the drawing area's center.
     */
    public int getCenterY() {
        return margin.top + getH() / 2;
    }

    /**
     * Event's X coordinate based on time of its occurrence.
     *
     * @param time Event's time stamp in milliseconds.
     * @param startTime Drawing area's start time.
     * @param endTime Drawing area's end time.
     * @return Event's X coordinate.
     */
    final public int getXForTime(long time, long startTime, long endTime) {
        double timeSpan = (double)(endTime - startTime);
        double relativeTime = (double)(time - startTime);
        return margin.left + (int)((relativeTime / timeSpan) * (double)getW());
    }

    /**
     * Event's Y coordinate based on its value (larger value -> lower Y coordinate -> higher on the screen)
     *
     * @param value Event's value.
     * @return Event's Y coordinate.
     */
    final public int getYForValue(float value) {
        return getCenterY() + (int)(getH() * value / 2) * -1;
    }
}