package com.looksee.models.audit.performance;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.CompositeProperty;

/**
 * Third party summary detail
 */
@Getter
@Setter
@NoArgsConstructor
public class ThirdPartySummaryDetail extends AuditDetail {

	private Integer transferSize;
	private Double blockingTime;
	private Double mainThreadTime;
	
	@CompositeProperty
	private Map<String, String> entity = new HashMap<>();
	
	/**
	 * Constructor for {@link ThirdPartySummaryDetail}
	 * 
	 * @param transfer_size the transfer size
	 * @param blocking_time the blocking time
	 * @param main_thread_time the main thread time
	 * @param entity the entity
	 */
	public ThirdPartySummaryDetail(int transferSize, double blockingTime, double mainThreadTime, Map<String, String> entity) {
		setTransferSize(transferSize);
		setBlockingTime(blockingTime);
		setMainThreadTime(mainThreadTime);
		setEntity(entity);
	}

	@Override
	public String generateKey() {
		return "thirdpartysummarydetail"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(transferSize.toString() + blockingTime.toString() + mainThreadTime.toString() + entity.toString());
	}
}
