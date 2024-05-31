package texample;

/**
 * Enumeration of the fonts in the assets folder for the project.
 *
 * @author Derek Mulvihill
 */
public enum GLTextType {
	Sans("FreeSans.otf"),
	SansBold("FreeSansBold.otf"),
	;

	public final String FileName;

	GLTextType(String fileName) {
		this.FileName = fileName;
	}
}
