package com.gsmc.png.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PNGMsgBuffer {

	public static final BlockingQueue<String> pngMsgBuffer = new LinkedBlockingQueue<String>();
	
}
