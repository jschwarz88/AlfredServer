package org.jschwarz.alfredServer;

import java.io.IOException;

public abstract class Device {

	protected int deviceID;

	public Device(int deviceID) {
		this.deviceID = deviceID;
	}

	public abstract boolean handleCommand(int cmd) throws IOException,
			InterruptedException;

	public int getDeviceID() {
		return deviceID;
	}

	protected void logSuccess(String cmdString) {
		Logger.log("Command succesfully executed: " + cmdString);
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}
}