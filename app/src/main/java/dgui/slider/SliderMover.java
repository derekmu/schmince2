package dgui.slider;

import dgui.GUIItem;
import dgui.GUIMover;

/**
 * GUIMover for the Slider GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public class SliderMover implements GUIMover {
    @Override
    public void moveGUI(GUIItem item, float x, float y) {
        Slider slider = (Slider) item;
        if (slider.Bounds.w != 0 && slider.Minimum <= slider.Maximum) {
            float percent = (x - slider.Bounds.x) / slider.Bounds.w;
            slider.Value = Math.max(
                    slider.Minimum,
                    Math.min(slider.Maximum, slider.Minimum + percent
                            * (slider.Maximum - slider.Minimum)));
            slider.updatedValue(slider.Value);
        }
    }
}
