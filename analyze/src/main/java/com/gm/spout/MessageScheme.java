package com.gm.spout;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class MessageScheme implements Scheme {
	
	private static final long serialVersionUID = 8423372426211017613L;

	@Override
	public List<Object> deserialize(ByteBuffer bytes) {
			try {
				String msg = new String(bytes.array(), "UTF-8");
				return new Values(msg); 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("msg");
	}

}