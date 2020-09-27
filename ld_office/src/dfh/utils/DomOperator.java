// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DomOperator.java

package dfh.utils;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DomOperator
{

	public static String doc2String(Document document)
	{
		String s = "";
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat("  ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return s;
	}

	public static Document string2Document(String s)
	{
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(s);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return doc;
	}

	public static String compileVerifyData(String pattern, String verifyData) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(verifyData);
		String result = "";
		while (m.find()) {
			result = m.group(1);
		}
		return result;

		// compileVerifyData("<element id='(.*?)'>", xmlString) Test the method
	}
	
	public DomOperator()
	{
	}
}
