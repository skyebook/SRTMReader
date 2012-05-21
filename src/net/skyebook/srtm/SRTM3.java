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
    private static final int ROWS = 1201;
    private int lat;
    private int lon;
    
    // SRTM is stored as signed 2 byte integers
    private short[] data;
    
    public SRTM3(int lat, int lon){
	this.lat = lat;
	this.lon = lon;
    }
    
    public void load(ByteBuffer byteBuffer){
	data = new short[ROWS * ROWS];
	int currentValue = 0;
	ByteBuffer convertBuffer = ByteBuffer.allocate(2);
	convertBuffer.order(ByteOrder.BIG_ENDIAN);
	
	byteBuffer.rewind();
	while(byteBuffer.hasRemaining()){
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
	
	for(int i=0; i<data.length; i++){
	    if(data[i]<0){
		System.out.println(i+":\t"+data[i]);
	    }
	}
    }
    
    
}
