package App;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.Stack;

public class StackClipboardListenerSimple extends ClipboardListener {
	/**
	 * The internal stack for data after the system clip
	 */
	Stack<Transferable> clipStack;
	/**
	 * Store if the last command was a store command
	 */
	private boolean lastWasCtrlC;

	/**
	 * 
	 * @param stackTimeout
	 */
	public StackClipboardListenerSimple(Clipboard sysclip) {
		super(sysclip);
		clipStack = new Stack<>();
	}

	@Override
	public void copyPressed() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Store the copy
		clipStack.push(sysClip.getContents(null));
		// We did a full copy action
		lastWasCtrlC = true;
	}

	@Override
	public void copyReleased() {
	}

	@Override
	public void pastePressed() {
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
		if (clipStack.size() > 1)
			sysClip.setContents(clipStack.pop(), null);
	}

	@Override
	public void pasteReleased() {
	}

}
