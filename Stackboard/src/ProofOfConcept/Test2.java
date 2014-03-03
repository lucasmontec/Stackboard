package ProofOfConcept;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.Stack;

import javax.swing.SwingUtilities;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Test2 implements NativeKeyListener {

	Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	Stack<Transferable> clipStack = new Stack<>();

	public static void main(String[] args) {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		Test2 t2 = new Test2();
		// Construct the example object and initialze native hook.
		GlobalScreen.getInstance().addNativeKeyListener(t2);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent ev) {
		// Copy
		if (ev.getKeyCode() == NativeKeyEvent.VK_C
				&& NativeInputEvent.getModifiersText(ev.getModifiers()).equals(
						"Ctrl")) {
			// Clip the pop
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					clipStack.push(sysClip.getContents(null));
				}
			});
			System.out.println("Ctrl+C : Stack(" + clipStack.size() + ")");
		}
		// Paste
		if (ev.getKeyCode() == NativeKeyEvent.VK_V
				&& NativeInputEvent.getModifiersText(ev.getModifiers()).equals(
						"Ctrl")) {
			// Clip the pop
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (clipStack.size() > 1) {
						sysClip.setContents(clipStack.pop(), null);
					} else
						sysClip.setContents(clipStack.peek(), null);
				}
			});
			System.out.println("Ctrl+V : Stack(" + clipStack.size() + ")");
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {

	}

}
