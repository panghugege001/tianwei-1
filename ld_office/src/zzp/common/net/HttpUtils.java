package zzp.common.net;

import java.io.IOException;
import java.io.InputStream;

import zzp.common.StreamReader;

public class HttpUtils {
	public final static String getString(HttpResponse response)
			throws IOException {
		String rtn = null;
		rtn = StreamReader.getStringFromStream(response.getInputStream(),
				response.getEncoding());
		return rtn;
	}
}