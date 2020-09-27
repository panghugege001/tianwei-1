// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PostFailedException.java

package dfh.exception;


// Referenced classes of package dfh.exception:
//			GenericDfhException

public class PostFailedException extends GenericDfhException
{

	public PostFailedException()
	{
		super("Post failed,please check the network connection");
	}

	public PostFailedException(String error)
	{
		super(error);
	}
}
