package org.jschwarz.alfredServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
	public static void log(String text) {
		@SuppressWarnings("deprecation")
		String timeAndDate = new Date().toGMTString();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("/var/log/alfredServer.log", true)));
			out.println(timeAndDate + ": +" + text);
			out.close();
		} catch (IOException e) {
		}
	}
}