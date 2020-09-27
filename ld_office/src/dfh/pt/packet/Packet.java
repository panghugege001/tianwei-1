package dfh.pt.packet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Packet 
{
	public static final byte CHS = (byte) 253;
	public static final byte EOS = (byte) 254;
	public List<String> list;
	public byte code;
	
	public Packet(byte code)
	{
		this.code = code;
		list = new ArrayList<String>();
	}
	
	
	public Packet(byte[] bytes)
	{
		list = new ArrayList<String>();
		code = bytes[0];
		int position = 1;
		int length = 0;
		for (int i = 1; i<bytes.length; i++)
		{
			if (i == bytes.length - 1)
			{
				length++;
				list.add(new String(bytes, position, length));
				position += length;
				length = 0;
			}else
			if (bytes[i] == CHS)
			{
				list.add(new String(bytes, position, length));
				position += length+1;
				length = 0;
			}else
			{
				length++;
			}
		}
	}
	
	public void add(Object item)
	{
		String _item = String.valueOf(item);
		list.add(_item);
	}
	
	public void add(String item)
	{
		list.add(item);
	}
	
	public byte[] build()
	{
		try
		{
			ByteArrayOutputStream res = new ByteArrayOutputStream();
			res.write(code);
			for (int i = 0; i<list.size(); i++)
			{
				if (i!=0)
					res.write(CHS);
				res.write(list.get(i).getBytes());
			}
			res.write(EOS);
			return res.toByteArray();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}	
}
