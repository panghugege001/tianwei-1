// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResponseFailedException.java

package dfh.exception;


// Referenced classes of package dfh.exception:
//			GenericDfhException

public class RemoteApiException extends GenericDfhException
{

	public RemoteApiException()
	{
		super("exception in remote api,need to roll data  back");
	}

	public RemoteApiException(String error)
	{
		super("exception in remote api,need to roll data  back;"+error);
	}
}
