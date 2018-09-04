package org.giavacms.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Map;

/**
 * "consumer-count" : 0,
 * 
 * "dead-letter-address" : "jms.queue.DLQ",
 * 
 * "delivering-count" : 0,
 * 
 * "durable" : true,
 * 
 * "entries" : ["java:/jms/queue/DLQ"],
 * 
 * "expiry-address" : "jms.queue.ExpiryQueue",
 * 
 * "legacy-entries" : null,
 * 
 * "message-count" : 0,
 * 
 * "messages-added" : 0,
 * 
 * "paused" : false,
 * 
 * "queue-address" : "jms.queue.DLQ",
 * 
 * "scheduled-count" : 0,
 * 
 * "selector" : null,
 * 
 * "temporary" : false
 * 
 * @author pisi79
 *
 */
public class QueueStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long deliveringCount = 0L;
	private long consumerCount = 0L;
	private long messageCount = 0L;
	private long messageAdded = 0L;
	private long scheduledCount = 0L;
	private boolean paused = false;
	private String queueAddress = null;

	public QueueStatus(String json) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = new ObjectMapper().readValue(json.toString(),
				Map.class);
		this.deliveringCount = Long.parseLong(map.get("delivering-count")
				.toString());
		this.consumerCount = Long.parseLong(map.get("consumer-count")
				.toString());
		this.messageCount = Long.parseLong(map.get("message-count").toString());
		this.messageAdded = Long
				.parseLong(map.get("messages-added").toString());
		this.scheduledCount = Long.parseLong(map.get("scheduled-count")
				.toString());
		this.deliveringCount = Long.parseLong(map.get("delivering-count")
				.toString());
		this.paused = Boolean.parseBoolean(map.get("delivering-count")
				.toString());
		this.queueAddress = map.get("queue-address").toString();
	}

	public long getDeliveringCount() {
		return deliveringCount;
	}

	public void setDeliveringCount(long deliveringCount) {
		this.deliveringCount = deliveringCount;
	}

	public long getConsumerCount() {
		return consumerCount;
	}

	public void setConsumerCount(long consumerCount) {
		this.consumerCount = consumerCount;
	}

	public long getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}

	public long getMessageAdded() {
		return messageAdded;
	}

	public void setMessageAdded(long messageAdded) {
		this.messageAdded = messageAdded;
	}

	public long getScheduledCount() {
		return scheduledCount;
	}

	public void setScheduledCount(long scheduledCount) {
		this.scheduledCount = scheduledCount;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public String getQueueAddress() {
		return queueAddress;
	}

	public void setQueueAddress(String queueAddress) {
		this.queueAddress = queueAddress;
	}

}
