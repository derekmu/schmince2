package dgui.textfield;

import dgui.GUIItem;
import dopengl.DInputHandler.DTextInputHolder;
import texample.GLTextType;
import thed.DColor;

/**
 * GUI editable text field.
 *
 * @author Derek Mulvihill
 */
public class TextField extends GUIItem implements DTextInputHolder {
	private static final TextFieldDrawer drawer = new TextFieldDrawer();
	public final DColor BackgroundColor = new DColor(0.25f, 0.25f, 0.25f, 1f);
	public final DColor TextColor = new DColor(1f, 1f, 1f, 1f);
	public GLTextType TextType = GLTextType.Sans;
	public float TextScale = 1f;
	private CharSequence text = "";

	public TextField() {
		super(drawer);
	}

	@Override
	public CharSequence getTextValue() {
		return text;
	}

	@Override
	public void setTextValue(CharSequence value) {
		this.text = value;
	}
}
