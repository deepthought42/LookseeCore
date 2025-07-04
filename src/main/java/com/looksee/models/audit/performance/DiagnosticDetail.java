package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Diagnostic detail
 */
@Getter
@Setter
@NoArgsConstructor
public class DiagnosticDetail extends AuditDetail {

	private Integer numStylesheets;
	private Double throughput;
	private Integer numTasksOver10ms;
	private Integer numTasksOver25ms;
	private Integer numTasksOver50ms;
	private Integer numTasksOver100ms;
	private Integer numTasksOver500ms;
	private Integer numRequests;
	private Double totalTaskTime;
	private Integer mainDocumentTransferSize;
	private Integer totalByteWeight;
	private Integer numTasks;
	private Double rtt;
	private Double maxRtt;
	private Integer numFonts;
	private Integer numScripts;
	
	/**
	 * Constructs a {@link DiagnosticDetail} object
	 *
	 * @param num_stylesheets the number of stylesheets
	 * @param throughput the throughput
	 * @param num_tasks_over_10ms the number of tasks over 10ms
	 * @param num_tasks_over_25ms the number of tasks over 25ms
	 * @param num_tasks_over_50ms the number of tasks over 50ms
	 * @param num_tasks_over_100ms the number of tasks over 100ms
	 * @param num_tasks_over_500ms the number of tasks over 500ms
	 * @param num_requests the number of requests
	 * @param total_task_time the total task time
	 * @param main_document_transfer_size the main document transfer size
	 * @param total_byte_weight the total byte weight
	 * @param num_tasks the number of tasks
	 * @param rtt the round trip time
	 * @param max_rtt the maximum round trip time
	 * @param num_fonts the number of fonts
	 * @param num_scripts the number of scripts
	 *
	 * precondition: num_stylesheets != null
	 * precondition: throughput != null
	 * precondition: num_tasks_over_10ms != null
	 * precondition: num_tasks_over_25ms != null
	 * precondition: num_tasks_over_50ms != null
	 * precondition: num_tasks_over_100ms != null
	 * precondition: num_tasks_over_500ms != null
	 * precondition: num_requests != null
	 * precondition: total_task_time != null
	 * precondition: main_document_transfer_size != null
	 * precondition: total_byte_weight != null
	 * precondition: num_tasks != null
	 * precondition: rtt != null
	 * precondition: max_rtt != null
	 * precondition: num_fonts != null
	 * precondition: num_scripts != null
	 */
	public DiagnosticDetail(
			Integer num_stylesheets,
			Double throughput,
			Integer num_tasks_over_10ms,
			Integer num_tasks_over_25ms,
			Integer num_tasks_over_50ms,
			Integer num_tasks_over_100ms,
			Integer num_tasks_over_500ms,
			Integer num_requests,
			Double total_task_time,
			Integer main_document_transfer_size,
			Integer total_byte_weight,
			Integer num_tasks,
			Double rtt,
			Double max_rtt,
			Integer num_fonts,
			Integer num_scripts) {
		setNumStylesheets(num_stylesheets);
		setThroughput(throughput);
		setNumTasksOver10ms(num_tasks_over_10ms);
		setNumTasksOver25ms(num_tasks_over_25ms);
		setNumTasksOver50ms(num_tasks_over_50ms);
		setNumTasksOver100ms(num_tasks_over_100ms);
		setNumTasksOver500ms(num_tasks_over_500ms);
		setNumRequests(num_requests);
		setTotalTaskTime(total_task_time);
		setMainDocumentTransferSize(main_document_transfer_size);
		setTotalByteWeight(total_byte_weight);
		setNumTasks(num_tasks);
		setRtt(rtt);
		setMaxRtt(max_rtt);
		setNumFonts(num_fonts);
		setNumScripts(num_scripts);
	}
}
