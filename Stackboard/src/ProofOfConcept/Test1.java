package ProofOfConcept;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.Stack;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

class BoardListener extends Thread implements ClipboardOwner, NativeKeyListener {
	Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	Stack<Transferable> clipStack = new Stack<>();

	public void run() {
		Transferable trans = sysClip.getContents(this);
		regainOwnership(trans);
		System.out.println("Listening to board...");
		while (true) {
		}
	}

	public void lostOwnership(Clipboard c, Transferable t) {
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		Transferable contents = sysClip.getContents(this); // EXCEPTION
		processContents(contents);
		regainOwnership(contents);
	}

	void processContents(Transferable t) {
		System.out.println("Processing: " + t);
		//Store in the stack the value
		clipStack.push(t);
		System.out.println("Storing element: "+t.getTransferDataFlavors()[0].getHumanPresentableName());
	}

	void regainOwnership(Transferable t) {
		sysClip.setContents(t, this);
	}

	public static void main(String[] args) {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		BoardListener b = new BoardListener();
		// Construct the example object and initialze native hook.
		GlobalScreen.getInstance().addNativeKeyListener(b);
		b.start();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent ev) {
		if(ev.getKeyCode() == NativeKeyEvent.VK_V && NativeInputEvent.getModifiersText(ev.getModifiers()).equals("Ctrl")){
			System.out.println("Ctrl+V : call");
			//Pop the clip
			if (clipStack.size() > 1) {
				System.out.println("Ctrl+V : pop");
				sysClip.setContents(clipStack.pop(), null);
			} else{
				System.out.println("Ctrl+V : peek");
				sysClip.setContents(clipStack.peek(), null);
			}
			
			System.out.println("Ctrl+V : finished status Stack(" + clipStack.size() + ")");
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		
	}
}