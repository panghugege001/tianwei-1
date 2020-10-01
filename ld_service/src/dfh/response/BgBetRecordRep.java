package dfh.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BgBetRecordRep<T,E> {
	  @JsonProperty(value = "result")
	  private T result;
	  @JsonProperty(value = "error")
	  private E error;

	  public T getResult() {
	    return result;
	  }

	  public void setResult(T result) {
	    this.result = result;
	  }

	  public E getError() {
	    return error;
	  }

	  public void setError(E error) {
	    this.error = error;
	  }

	  @Override
	  public String toString() {
	    return "BgBetRecordRep{" +
	        "result=" + result +
	        ", error=" + error +
	        '}';
	  }
	}
