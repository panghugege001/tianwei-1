package com.gsmc.png.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BgBetRecordResult<T> {

	  @JsonProperty(value = "total")
	  private Integer total;
	  @JsonProperty(value = "items")
	  private List<T> items;

	  public Integer getTotal() {
	    return total;
	  }

	  public void setTotal(Integer total) {
	    this.total = total;
	  }

	  public List<T> getItems() {
	    return items;
	  }

	  public void setItems(List<T> items) {
	    this.items = items;
	  }

	  @Override
	  public String toString() {
	    return "BgBetRecordResult{" +
	        "total=" + total +
	        ", items=" + items +
	        '}';
	  }
	}
