package com.base.baselib.common.subutils.bank.tlv;

import java.io.Serializable;

public class TLV implements Serializable {
	private String tag;
	  
    private int length;  
  
    private String value;
  
    public TLV(String tag, int length, String value)
    {  
        this.length = length;  
        this.tag = tag;  
        this.value = value;  
    }

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}  
  
    
    
    
}
