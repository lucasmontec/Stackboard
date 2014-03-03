package App;

import java.awt.datatransfer.Transferable;

public interface StackClipboardEvent {

	public void stacked(Transferable t);

	public void popped(Transferable t);

}
