// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RemoteCallException.java

package dfh.exception;


// Referenced classes of package dfh.exception:
//			GenericDfhException

public class RemoteCallException extends GenericDfhException
{

	private String error;

	public RemoteCallException()
	{
		super("call the remote interface failed!");
	}

	public RemoteCallException(String error)
	{
		super(error);
		this.error = error;
	}

	@Override
	public String getError()
	{
		return error;
	}
}
