// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericDfhRuntimeException.java

package dfh.exception;


public class GenericDfhRuntimeException extends RuntimeException
{

	private static final long serialVersionUID = 0x8191708ce1d20758L;
	private String error;

	public GenericDfhRuntimeException(String error)
	{
		super(error);
		this.error = error;
	}

	public String getError()
	{
		return error;
	}
}
