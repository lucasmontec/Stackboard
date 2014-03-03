package App;

public class OSHelper {

	/**
	 * Autodetect OS
	 */
	private static String	OS	= System.getProperty("os.name").toLowerCase();

	/**
	 * @return True if the code is running on windows
	 */
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	/**
	 * @return True if the code is running on mac
	 */
	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	/**
	 * @return True if the code is running on linux
	 */
	public static boolean isLinux() {
		return (OS.indexOf("Linux") >= 0);
	}

	/**
	 * @return True if the code is running on unix
	 */
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	/**
	 * @return True if the code is running on solaris
	 */
	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

}
