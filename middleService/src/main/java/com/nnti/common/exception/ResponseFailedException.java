// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResponseFailedException.java

package com.nnti.common.exception;


// Referenced classes of package dfh.exception:
//			GenericDfhException

public class ResponseFailedException extends GenericDfhException
{

	public ResponseFailedException()
	{
		super("fail to get the http response,it's sucessful to send http request");
	}

	public ResponseFailedException(String error)
	{
		super("fail to get the http response,it's sucessful to send http request;"+error);
	}
}
