package App;

import java.awt.datatransfer.Clipboard;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Listens to system events that are clipboard copy and paste.<br>
 * Windows only. Use singleton. Bind only one at a time.
 * 
 * @author LucasM.Carvalhaes(Zombie)
 * 
 */
public abstract class ClipboardListener implements NativeKeyListener {

	/**
	 * Use to define the condition of clipping for each system
	 * 
	 * @author LucasM.Carvalhaes(Zombie)
	 * 
	 */
	public enum systems {
		WINDOWS, LINUX, MAC
	}

	/**
	 * Store the current system the software is running
	 */
	private systems system = systems.WINDOWS;
	/**
	 * Store the system clipboard
	 */
	protected Clipboard sysClip;

	/**
	 * Booleans for the user. Helps the extending of functionality.
	 */
	protected boolean	CtrlCPressed, CtrlVPressed;

	/**
	 * Pass the system clipboard for this constructor
	 * 
	 * @param sysclip
	 *            The system clipboad
	 */
	public ClipboardListener(Clipboard sysclip){
		sysClip = sysclip;
	}

	/**
	 * Bind the ClipboardListener to the system key event hook
	 */
	public void bind() {
		// Register the hook
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err
			.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		// Bind the listener
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

	/**
	 * Unbind the ClipboardListener to the event hook
	 */
	public void unbind() {
		// Remove the listener
		GlobalScreen.getInstance().removeNativeKeyListener(this);
		// Free the hook
		GlobalScreen.unregisterNativeHook();
	}

	/**
	 * Implement to add an action after the system copy call
	 */
	public abstract void copyPressed();

	/**
	 * Implement to add an action after the system paste call
	 */
	public abstract void pastePressed();

	/**
	 * Implement to add an action after the system copy call
	 */
	public abstract void copyReleased();

	/**
	 * Implement to add an action after the system paste call
	 */
	public abstract void pasteReleased();

	/**
	 * Implementation for use in key press and release events
	 * 
	 * @param ev
	 *            The event of a key press or release
	 * @param status
	 *            set this to true if it was a press, false if was a release
	 */
	private void processEvent(NativeKeyEvent ev, boolean press) {
		// Implement for each system
		switch (system) {
			case WINDOWS:
				// Copy check
				if (ClipboardConditions.windowsCopyCondition(ev)) {
					if (press) {
						copyPressed();
						CtrlCPressed = true;
					}
				}
				if (ClipboardConditions.windowsCopyReleaseCondition(ev) && !press) {
					copyReleased();
					CtrlCPressed = false;
				}
				// Paste check
				if (ClipboardConditions.windowsPasteCondition(ev)) {
					if (press) {
						pastePressed();
						CtrlVPressed = true;
					}
				}
				if (ClipboardConditions.windowsPasteReleaseCondition(ev) && !press) {
					pasteReleased();
					CtrlVPressed = false;
				}
				break;
			case LINUX:
				break;
			case MAC:
				break;
			default:
				System.err
				.println("No system defined for the clipboard listener!");
				break;
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent ev) {
		processEvent(ev, true);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent ev) {
		processEvent(ev, false);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// Nothing
	}

	public systems getSystem() {
		return system;
	}

	public void setSystem(systems system) {
		this.system = system;
	}

	/**
	 * I tried to make this to set the system for this listener automatically
	 */
	public void setSystem() {
		if (OSHelper.isWindows())
			system = systems.WINDOWS;
		if (OSHelper.isSolaris())
			system = null;
		if (OSHelper.isLinux())
			system = systems.LINUX;
		if (OSHelper.isMac())
			system = systems.MAC;
	}

}
