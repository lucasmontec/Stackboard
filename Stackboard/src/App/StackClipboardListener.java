package App;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.Stack;

/**
 * A listener that handles clipboard actions making the clipboard a stack.<br>
 * Use the system clip as the top of the stack. Doesn't use the stack on a simple<br>
 * ctrl+c, you'll need to hold it for some time.<br>
 * NEEDS FIXING
 * 
 * @author LucasM.Carvalhaes(Zombie)
 * 
 */
public class StackClipboardListener extends ClipboardListener {

	/**
	 * The internal stack for data after the system clip
	 */
	Stack<Transferable>			clipStack;
	/**
	 * Store if the last command was a store command
	 */
	private boolean				lastWasCtrlC;
	/**
	 * Store if the paste condition was valid for the release
	 */
	private boolean				lastWasValidPasteCondition;
	/**
	 * Hold a single event for callback
	 */
	private StackClipboardEvent	event;

	/**
	 * @param sysclip
	 *            The system clipboard
	 */
	public StackClipboardListener(Clipboard sysclip) {
		super(sysclip);
		clipStack = new Stack<>();
	}

	/**
	 * Receive a single event listener to listen for changes
	 * 
	 * @param ev
	 *            The listener to be installed, null to remove
	 */
	public void setEventListener(StackClipboardEvent ev) {
		event = ev;
	}

	@Override
	public void copyPressed() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Push");
		Transferable elem = sysClip.getContents(null);
		// Store the copy
		clipStack.push(elem);
		// Call the event
		if (event != null)
			event.stacked(elem);
		// We did a full copy action
		lastWasCtrlC = true;
	}

	@Override
	public void copyReleased() {}

	@Override
	public void pastePressed() {
		lastWasValidPasteCondition = true;
	}

	@Override
	public void pasteReleased() {
		if (lastWasValidPasteCondition) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// If last was a contrl c, we need to pop once to use the system clip as
			// header
			if (lastWasCtrlC) {
				// Control v action done
				lastWasCtrlC = false;
				// If the stack size is valid we pop the element
				if (clipStack.size() > 1)
					clipStack.pop();
			}
			// Now if we have elements in the stack, pop them to the next past
			// action
			if (clipStack.size() > 0) {
				Transferable elem = null;
				System.out.println("pop");
				elem = clipStack.pop();
				sysClip.setContents(elem, null);
				// Call the event
				if (event != null)
					event.popped(elem);
			}
			// reset lock
			lastWasValidPasteCondition = false;
		}
	}

}
