package dfh.utils;

import org.apache.commons.httpclient.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mars on 2016/6/13.
 */
public class NameValuePairBuilder {

    private List<NameValuePair> pairList = new ArrayList<NameValuePair>();

    private NameValuePairBuilder() {
    }

    public static NameValuePairBuilder anPair() {
        return new NameValuePairBuilder();
    }

    public NameValuePairBuilder with(String name, String value) {
        pairList.add(new NameValuePair(name, value));
        return this;
    }

    public NameValuePairBuilder with(String name, Object value) {
        String valueString = String.valueOf(value);
        pairList.add(new NameValuePair(name, valueString));
        return this;
    }

    public NameValuePair[] build() {
        return pairList.toArray(new NameValuePair[pairList.size()]);
    }

}