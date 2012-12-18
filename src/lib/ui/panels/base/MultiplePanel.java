package lib.ui.panels.base;

import lib.Utils;
import lib.types.PAD;
import lib.types.PADState;

import java.awt.*;
import java.util.ArrayList;

public abstract class MultiplePanel extends Panel {

    protected ArrayList<PADState> values = new ArrayList<PADState>();

    public MultiplePanel(int width, int height) {
        super(PAD.Type.PAD, width, height);
    }

    @Override
    protected void feedState(PADState state) {
        this.values.add(state);
    }

    @Override
    public abstract void customPaintComponent(Graphics2D g2d);

    public ArrayList<PADState> getValuesForTime(long startTime, long endTime) {
        ArrayList<PADState> list = new ArrayList<PADState>();

        boolean found = false;

        for (PADState state: Utils.reversePSList(values)) {
            if (state.getTimestamp() >= startTime && state.getTimestamp() <= endTime) {
                list.add(state);
                found = true;
            } else if (found) {
                break;
            }
        }

        return Utils.reversePSList(list);
    }

    public ArrayList<PADState> getValuesForCurrentBuffer() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - buffer * 1000;

        return getValuesForTime(startTime, endTime);
    }
}