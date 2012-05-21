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
	
	System.out.println(bytesRead + " bytes read from " + file.toString());
	
	SRTM3 srtm = new SRTM3();
	long start = System.currentTimeMillis();
	srtm.load(data);
	System.out.println("Conversion took " + (System.currentTimeMillis()-start) + "ms");
    }
}
