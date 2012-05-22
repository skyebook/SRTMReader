/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skyebook.srtm.test;

import java.io.*;
import java.nio.ByteBuffer;
import net.skyebook.srtm.SRTM3;

/**
 *
 * @author Skye Book
 */
public class TestSRTM3 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
	File file = new File("test/N40W090.hgt");
	DataInputStream dis = new DataInputStream(new FileInputStream(file));
	
	byte[] bytes = new byte[(int)file.length()];
	int bytesRead = dis.read(bytes);
	
	ByteBuffer data = ByteBuffer.allocate(bytes.length);
	data.put(bytes);
	
	String fileString = file.getName().substring(0, file.getName().indexOf("."));
	
	String latString = fileString.substring(0,3);
	String lonString = fileString.substring(3,7);
	int lat = Integer.parseInt(latString.substring(1));
	int lon = Integer.parseInt(lonString.substring(1));
	if(latString.startsWith("S")){
	    lat*=-1;
	}
	if(lonString.startsWith("W")){
	    lon*=-1;
	}
	
	
	System.out.println(lat+","+lon);
	
	System.out.println(bytesRead + " bytes read from " + fileString);
	
	
	
	SRTM3 srtm = new SRTM3(lat, lon);
	long start = System.currentTimeMillis();
	srtm.load(data);
	System.out.println("Conversion took " + (System.currentTimeMillis()-start) + "ms");
	
	System.out.println(srtm.getElevationAt(40.776, -89.50, SRTM3.Mode.NearestNeighbor));
    }
}
