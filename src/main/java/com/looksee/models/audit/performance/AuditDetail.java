package com.looksee.models.audit.performance;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Abstract class for audit details
 */
@Getter
@Setter
@NoArgsConstructor
@Node
public abstract class AuditDetail {

	@GeneratedValue
    @Id
	private Long id;
	private Date created_date = new Date();
	
	/**
	 * Gets the id of the audit detail
	 * 
	 * @return {@link Long} representing the id of the audit detail
	 */
	public Long getId() {
		return this.id;
	}
}
