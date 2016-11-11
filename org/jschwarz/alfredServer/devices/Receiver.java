package org.jschwarz.alfredServer.devices;

import java.io.IOException;

import org.jschwarz.alfredServer.AlfredServer;
import org.jschwarz.alfredServer.Device;
import org.jschwarz.alfredServer.Logger;

public class Receiver extends Device {

	private int volume = 0;

	public Receiver(int deviceID) {
		super(deviceID);
	}

	@Override
	public boolean handleCommand(int cmd) throws IOException,
			InterruptedException {
		boolean result = false;
		if (cmd >= AlfredServer.COMMAND_OFF
				&& cmd <= AlfredServer.COMMAND_INPUT_FIRE) {
			Process p = new ProcessBuilder("sudo",
					"/etc/skripte/controlReceiver.sh", cmd + "").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo /etc/skripte/controlReceiver.sh" + cmd);
		} else if (cmd > 8) {
			volume = (cmd < 100) ? (cmd - 90) : (cmd - 900);
			Process p = new ProcessBuilder("sudo",
					"/etc/skripte/setReceiverVolume.sh", volume + "").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo /etc/skripte/setReceiverVolume.sh " + volume);
		} else {
			Logger.log("FEHLER. Unknown command " + cmd + " for DeviceID "
					+ deviceID + "(Receiver).");
			result = false;
		}
		return result;
	}
}