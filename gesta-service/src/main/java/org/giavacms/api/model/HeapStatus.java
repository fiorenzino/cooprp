package org.giavacms.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Map;

/**
 * "init" : 2555904, "used" : 124693344, "committed" : 136380416, "max" : -1
 * 
 * @author pisi79
 *
 */
public class HeapStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long init = 0L;
	private long used = 0L;
	private long committed = 0L;
	private long max = 0L;

	public HeapStatus(String json) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = new ObjectMapper().readValue(json.toString(),
				Map.class);
		this.init = Long.parseLong(map.get("init").toString());
		this.used = Long.parseLong(map.get("used").toString());
		this.committed = Long.parseLong(map.get("committed").toString());
		this.max = Long.parseLong(map.get("max").toString());
	}

	public long getInit() {
		return init;
	}

	public void setInit(long init) {
		this.init = init;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public long getCommitted() {
		return committed;
	}

	public void setCommitted(long committed) {
		this.committed = committed;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

}
