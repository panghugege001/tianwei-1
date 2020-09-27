package dfh.skydragon.webservice.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class RampartPasswordCB implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
			for (int i = 0; i < callbacks.length; i++) {
				WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];
				String id = pwcb.getIdentifier();
				System.out.println("id====" + id);
				if ("client".equals(id)) {
					pwcb.setPassword("apache");
				} else if ("service".equals(id)) {
					pwcb.setPassword("apache");
				} else {
					System.out.println("Your are not a authorized user");
					throw new UnsupportedCallbackException(callbacks[i],
					"Your are not a authorized user");
				}
			}
	}
}