package org.jschwarz.alfredServer.entities.devices;

import java.io.IOException;

import org.jschwarz.alfredServer.Logger;
import org.jschwarz.alfredServer.entities.Device;

public class Receiver extends Device {

	private int volume = 0;
	private int newVolume = 0;

	public static int COMMAND_MUTE = 2;
	public static int COMMAND_UNMUTE = 3;
	public static int COMMAND_INPUT_BT = 4;
	public static int COMMAND_INPUT_TV = 5;
	public static int COMMAND_INPUT_NB = 6;
	public static int COMMAND_INPUT_FIRE = 7;

	public Receiver(int deviceID) {
		super(deviceID);
	}

	@Override
	public boolean handleCommand(int cmd) throws IOException,
			InterruptedException {
		boolean result = false;
		if (cmd >= COMMAND_OFF && cmd <= COMMAND_INPUT_FIRE) {
			Process p = new ProcessBuilder("sudo",
					"/etc/skripte/controlReceiver.sh", cmd + "").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo /etc/skripte/controlReceiver.sh" + cmd);
		} else if (cmd > 8) {
			newVolume = (cmd < 100) ? (cmd - 90) : (cmd - 900);
			// Process p = new ProcessBuilder("sudo",
			// "/etc/skripte/setReceiverVolume.sh", volume + "").start();
			if (newVolume - volume > 0) {
				Process p = new ProcessBuilder("sudo",
						"/etc/skripte/increaseVolume.sh", (newVolume - volume)
								+ "").start();
				result = p.waitFor() == 0 ? true : false;
			} else if (newVolume - volume < 0) {
				Process p = new ProcessBuilder("sudo",
						"/etc/skripte/decreaseVolume.sh", (volume - newVolume)
								+ "").start();
				result = p.waitFor() == 0 ? true : false;
			}
			volume = newVolume;
			logSuccess("sudo /etc/skripte/setReceiverVolume.sh " + volume);
		} else {
			Logger.log("FEHLER. Unknown command " + cmd + " for DeviceID "
					+ deviceID + "(Receiver).");
			result = false;
		}
		return result;
	}
}