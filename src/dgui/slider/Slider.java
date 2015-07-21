package dgui.slider;

import dgui.GUIItem;
import thed.DColor;

/**
 * GUI slider.
 *
 * @author Derek Mulvihill
 */
public abstract class Slider extends GUIItem {
    private static final SliderDrawer drawer = new SliderDrawer();
    private static final SliderMover mover = new SliderMover();

    public float Minimum;
    public float Maximum;
    public float Value;

    public final DColor Color = new DColor(0f, 0f, 0f);

    public Slider() {
        super(drawer, mover);
    }

    protected abstract void updatedValue(float value);
}
