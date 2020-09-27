// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesLoader.java

package dfh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader
{

	public static Properties load(File propsFile)
		throws IOException
	{
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(propsFile);
		props.load(fis);
		fis.close();
		return props;
	}

	public static Properties load(String propsName)
		throws IOException
	{
		Properties props = new Properties();
		java.io.InputStream url = ClassLoader.getSystemResourceAsStream(propsName);
		props.load(url);
		return props;
	}

	private PropertiesLoader()
	{
	}
}
