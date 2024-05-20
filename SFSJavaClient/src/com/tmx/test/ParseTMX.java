package com.tmx.test;

import java.io.InputStream;

import tiled.simple.core.MapLayer;
import tiled.simple.reader.TMXReader;

public class ParseTMX {

	public static void main(String[] args) {
		//System.out.println(new File(".").getAbsolutePath());
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(".").getPath());
		InputStream is = ParseTMX.class.getResourceAsStream("/map1.tmx");
				 
		TMXReader tmxReader = new TMXReader();
		tiled.simple.core.Map map = null;
		 
		try {
			map = tmxReader.readMap(is);
			System.out.println(map.getHeight() + " -> " + map.getWidth());
			
			MapLayer layer = map.getLayer("bacteriatiles");
			    
			System.out.println(layer.getTile(0, 0));
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
