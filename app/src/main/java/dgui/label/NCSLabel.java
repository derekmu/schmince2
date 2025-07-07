package dgui.label;

import thed.NumCharSequence;

/**
 * Extension of Label with a NumCharSequence, a common GUI pattern.
 *
 * @author Derek Mulvihill - Oct 30, 2013
 */
public class NCSLabel extends Label {
    public final NumCharSequence NCS;

    public NCSLabel(NumCharSequence ncs) {
        super(ncs);
        this.NCS = ncs;
    }
}
