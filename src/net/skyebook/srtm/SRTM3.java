/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skyebook.srtm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Skye Book
 */
public class SRTM3 {

    public static enum Mode {

	NearestNeighbor,
	LinearInterpolation
    }
    private static final int ROWS = 1201;
    private double lat;
    private double lon;
    // SRTM is stored as signed 2 byte integers
    private short[] data;

    public SRTM3(int lat, int lon) {
	this.lat = lat;
	this.lon = lon;
    }

    public void load(ByteBuffer byteBuffer) {
	data = new short[ROWS * ROWS];
	int currentValue = 0;
	ByteBuffer convertBuffer = ByteBuffer.allocate(2);
	convertBuffer.order(ByteOrder.BIG_ENDIAN);

	byteBuffer.rewind();
	while (byteBuffer.hasRemaining()) {
	    // Get the big endian value and convert it to little endian
	    short bigEndianValue = byteBuffer.getShort();
	    convertBuffer.putShort(bigEndianValue);
	    convertBuffer.order(ByteOrder.LITTLE_ENDIAN);

	    // Store the little endian result
	    short littleEndianValue = convertBuffer.getShort(0);
	    data[currentValue] = littleEndianValue;
	    //if(currentValue%100==0)System.out.println(bigEndianValue + " converted to " + littleEndianValue);
	    currentValue++;

	    // Reset the conversion buffer for use on the next run
	    convertBuffer.clear();
	    convertBuffer.rewind();
	}

	for (int i = 0; i < data.length; i++) {
	    if (data[i] < 0) {
		System.out.println(i + ":\t" + data[i]);
	    }
	}
    }

    public short getDataAt(int column, int row) {
	if (column > ROWS || row > ROWS) {
	    throw new IllegalArgumentException("Column and row has a maximum of " + ROWS);
	}
	return data[(column * ROWS) + row];
    }

    public short getElevationAt(double latitude, double longitude, Mode mode) {
	if (latitude < lat || latitude > lat + 1 || longitude < lon || longitude > lon + 1) {
	    throw new IllegalArgumentException("Outside of tile bounds");
	}
	
	

	double latWeight = latitude % 1;
	double lonWeight = longitude % 1;
	//double latWeight = lat+1 - latitude;
	//double lonWeight = lon+1 - longitude;
	
	if (latitude < 0) {
	    // invert
	    latWeight *= -1;
	    latWeight = 1 - latWeight;
	}
	if (longitude < 0) {
	    // invert
	    lonWeight *= -1;
	    lonWeight = 1 - lonWeight;
	}
	
	System.out.println("-----");
	System.out.println("lat weight: " + latWeight);
	System.out.println("lon weight: " + lonWeight);
	
	double preciseRow = latWeight * ROWS;
	double preciseColumn = lonWeight * ROWS;
	
	System.out.println("-----");
	System.out.println("precise column: " + preciseColumn);
	System.out.println("precise row: " + preciseRow);
	
	int column = 0;
	int row = 0;

	if (mode.equals(Mode.NearestNeighbor)) {
	    if (preciseColumn % 1 > .5d) {
		column = (int) Math.floor(preciseColumn) + 1;
		System.out.println("Column rounded up to " + column);
	    }
	    else {
		column = (int) Math.floor(preciseColumn);
		System.out.println("Column rounded down to " + column);
	    }
	    if (preciseRow % 1 > .5d) {
		row = (int) Math.floor(preciseRow) + 1;
		System.out.println("Row rounded up to " + row);
	    }
	    else {
		row = (int) Math.floor(preciseRow);
		System.out.println("Row rounded down to " + row);
	    }
	}
	else if (mode.equals(Mode.LinearInterpolation)) {
	}

	System.out.println("Getting at " + column + "," + row);
	return data[(row * ROWS) + column];
    }
}