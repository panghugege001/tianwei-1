// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericDfhException.java

package com.gsmc.png.exception;


public class GenericDfhException extends Exception
{

	private static final long serialVersionUID = 0x2b0a5bba2e52a69fL;
	private String error;

	public GenericDfhException()
	{
		error = "Unknown exception.";
	}

	public GenericDfhException(String error)
	{
		super(error);
		this.error = error;
	}

	public String getError()
	{
		return error;
	}
}
