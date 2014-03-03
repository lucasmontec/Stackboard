package App;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

public class ClipboardConditions {

	/**
	 * Returns true if the event is equal to a windows copy event.
	 * 
	 * @param ev
	 *            The key event to analise
	 * @return True if it passes the condition
	 */
	public static boolean windowsCopyCondition(NativeKeyEvent ev) {
		return ev.getKeyCode() == NativeKeyEvent.VK_C
				&& NativeInputEvent.getModifiersText(ev.getModifiers()).equals("Ctrl+Meta");
	}

	/**
	 * Returns true if the key released meant that the user has done copying<br>
	 * All this code does is match the windows copy key without modifiers.
	 * 
	 * @param ev
	 *            The event to be analysed
	 * @return True if the key released was windows copy key 'c'
	 */
	public static boolean windowsCopyReleaseCondition(NativeKeyEvent ev) {
		return ev.getKeyCode() == NativeKeyEvent.VK_C;
	}

	/**
	 * Returns true if the event is equal to a windows paste event.
	 * 
	 * @param ev
	 *            The key event to analise
	 * @return True if it passes the condition
	 */
	public static boolean windowsPasteCondition(NativeKeyEvent ev) {
		return ev.getKeyCode() == NativeKeyEvent.VK_V
				&& NativeInputEvent.getModifiersText(ev.getModifiers()).equals("Ctrl+Meta");
	}

	/**
	 * Returns true if the key released meant that the user has done pasting<br>
	 * All this code does is match the windows copy key without modifiers.
	 * 
	 * @param ev
	 *            The event to be analysed
	 * @return True if the key released was windows copy key 'v'
	 */
	public static boolean windowsPasteReleaseCondition(NativeKeyEvent ev) {
		return ev.getKeyCode() == NativeKeyEvent.VK_V;
	}

}
