package dopengl.drawable;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public interface GLDrawableProvider {
	Class<? extends GLDrawable> getDrawClass();
}
