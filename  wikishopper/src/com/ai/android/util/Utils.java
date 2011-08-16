package com.ai.android.util;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static void closeStreamQuietly(InputStream inputStream) { // handle
																		// exceptions
																		// which
																		// might
																		// occur
																		// when
																		// closing
																		// an
																		// InputStream.
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			// ignore exception
		}
	}

}
