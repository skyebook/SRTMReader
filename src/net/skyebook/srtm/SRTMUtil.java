package net.skyebook.srtm;

/**
 * Utilities for wokring with SRTM data
 * @author Skye Book
 */
public class SRTMUtil {
    
    /**
     * Generate the name of an SRTM block
     * @param lat
     * @param lon
     * @return 
     */
    public static String generateBlockName(int lat, int lon) {
	String name = "";
	if (lat < 0) {
	    name += "S";
	    lat *= -1;
	}
	else {
	    name += "N";
	}
	name += (lat < 10) ? ("0" + lat) : lat;

	if (lon < 0) {
	    name += "W";
	    lon *= -1;
	}
	else {
	    name += "E";
	}
	if (lon < 10) {
	    name += "00" + lon;
	}
	else if (lon < 100) {
	    name += "0" + lon;
	}
	else {
	    name += lon;
	}

	name += ".hgt.zip";

	return name;
    }
}
