package App;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class StackBoard {

	/**
	 * A frame for the interface
	 */
	private JFrame				frame;
	/**
	 * A trayicon as interface
	 */
	private TrayIcon			ticon;
	/**
	 * Store if was created as tray icon
	 */
	private static boolean		isTrayInterface;

	/**
	 * Create our selves a stacklistener
	 */
	StackClipboardListener		stackboard;

	/**
	 * Get our sounds
	 */
	private final InputStream	stacked		= new BufferedInputStream(
			StackBoard.class
			.getResourceAsStream("/App/res/stack.wav"));
	private final InputStream	unstacked	= new BufferedInputStream(
			StackBoard.class
			.getResourceAsStream("/App/res/stackoff.wav"));

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new StackBoard();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StackBoard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Initialize the GUI
		if (!SystemTray.isSupported())
			initializeAsJFrame();
		else
			initializeAsTrayIcon();

		// Tell the user the program started
		if (isTrayInterface) {
			Sound.play(stacked);
			ticon.displayMessage("Stackboard", "Hi!", TrayIcon.MessageType.INFO);
		}

		// Create our hero
		stackboard = new StackClipboardListener(Toolkit.getDefaultToolkit().getSystemClipboard());
		// Make sound and effects!
		stackboard.setEventListener(new StackClipboardEvent() {
			@Override
			public void stacked(Transferable t) {
				Sound.play(stacked);

				// Display a nice 'pushed' message
				if (isTrayInterface) {
					ticon.displayMessage("Stack", "Pushed the item!", TrayIcon.MessageType.INFO);
				}
			}

			@Override
			public void popped(Transferable t) {
				Sound.play(unstacked);

				// Display a nice 'pushed' message
				if (isTrayInterface) {
					ticon.displayMessage("Stack", "Poped to next item!", TrayIcon.MessageType.INFO);
				}
			}
		});
		// Bind our man
		stackboard.bind();
	}

	/**
	 * Create the GUI for the stackboard as a trayicon
	 */
	private void initializeAsTrayIcon() {
		// Create the tray icon
		Image icon = null;
		try {
			icon = ImageIO.read(ClassLoader.class.getResourceAsStream("/App/res/StackboardIcon_16.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ticon = new TrayIcon(icon, "StackBoard");

		// Get the system tray to install the icon
		final SystemTray systray = SystemTray.getSystemTray();

		// Add a popupmenu
		addTrayPopupMenu(ticon);

		// Install the trayicon
		try {
			systray.add(ticon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}

		// Mark as tray icon interface
		isTrayInterface = true;
	}

	private void addTrayPopupMenu(TrayIcon trayi) {
		final PopupMenu popup = new PopupMenu();

		// Create items
		MenuItem helpItem = new MenuItem("Help");
		MenuItem exitItem = new MenuItem("Exit");

		// Add items
		popup.add(helpItem);
		popup.add(exitItem);

		// Actions
		helpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane
				.showMessageDialog(
						null,
						"How to use:\nIt is easy to use stackboard!\nYou can still use your clipboard "
								+ "normally when stackboard\nis active. If you want to stack something instead,\njust use the Meta key"
								+ " of your system (Windows key on windows for example).\n\nOn windows: Ctrl+Win+C stacks and Ctrl+Win+V pastes and pops",
								"Stackboard help",
								JOptionPane.QUESTION_MESSAGE);
			}
		});
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});

		// Set the menu to thye tray
		trayi.setPopupMenu(popup);
	}

	/**
	 * Correctly close the app
	 */
	private void close() {
		// Deathsound
		Sound.play(unstacked);
		// Unbind the hook
		stackboard.unbind();
		// Dispose the software
		if (isTrayInterface)
			SystemTray.getSystemTray().remove(ticon);
		else
			frame.dispose();
		// Now some ugly code to make shure we are dead
		// Kill the jvm
		System.exit(0);
	}

	/**
	 * If the system doesn't support tray icon, create as a jframe
	 */
	private void initializeAsJFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add our custom clearing for the bind
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent ev) {
				stackboard.unbind();
			}
		});

		// Make visible
		frame.setVisible(true);

		// Tag as frame
		isTrayInterface = false;
	}

}
