package com.looksee.models.message;

public interface IMessage {
	public long getAccountId();
	public long getDomainAuditRecordId();
	public long getDomainId();
	public String getMessageId();
	public String getPublishTime();
}
