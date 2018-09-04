package org.giavacms.api.model;

import java.io.Serializable;
import java.util.List;

public class HostStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HeapStatus heapStatus;
	private NonHeapStatus nonHeapStatus;
	private List<QueueStatus> queueStatuses;

	public HostStatus(HeapStatus heapStatus, NonHeapStatus nonHeapStatus,
                      List<QueueStatus> queueStatuses) {
		super();
		this.heapStatus = heapStatus;
		this.nonHeapStatus = nonHeapStatus;
		this.queueStatuses = queueStatuses;
	}

	public HostStatus() {
		super();
	}

	public HeapStatus getHeapStatus() {
		return heapStatus;
	}

	public void setHeapStatus(HeapStatus heapStatus) {
		this.heapStatus = heapStatus;
	}

	public NonHeapStatus getNonHeapStatus() {
		return nonHeapStatus;
	}

	public void setNonHeapStatus(NonHeapStatus nonHeapStatus) {
		this.nonHeapStatus = nonHeapStatus;
	}

	public List<QueueStatus> getQueueStatuses() {
		return queueStatuses;
	}

	public void setQueueStatuses(List<QueueStatus> queueStatuses) {
		this.queueStatuses = queueStatuses;
	}

}
