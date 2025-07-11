package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is used to store the metrics details for a performance audit.
 */
@NoArgsConstructor
@Getter
@Setter
public class MetricsDetail extends AuditDetail {

	private Integer firstContentfulPaint;
	private Long observedFirstPaintTs;
	private Integer speedIndex;
	private Long observedSpeedIndexTs;
	private Integer observedFirstContentfulPaint;
	private Long observedNavigationStartTs;
	private Long observedLargestContentfulPaintTs;
	private Integer observedFirstVisualChange;
	private Long observedLoadTs;
	private Integer firstMeaningfulPaint;
	private Integer observedTraceEnd;
	private Integer observedFirstMeaningfulPaint;
	private Integer firstCpuIdle;
	private Long observedTraceEndTs;
	private Long observedFirstMeaningfulPaintTs;
	private Integer observedDomContentLoaded;
	private Long observedFirstVisualChangeTs;
	private Integer interactive;
	private Integer observedNavigationStart;
	private Long observedFirstContentfulPaintTs;
	private Long observedLastVisualChangeTs;
	private Integer observedLoad;
	private Integer observedLargestContentfulPaint;
	private Long observedDomContentLoadedTs;
	private Integer observedSpeedIndex;
	private Integer estimatedInputLatency;
	private Integer totalBlockingTime;
	private Integer observedFirstPaint;
	private Integer observedLastVisualChange;
	private Boolean lcpInvalidated;
		
	public MetricsDetail(
			Integer first_contentful_paint,
			Long observed_first_paint_ts,
			Integer speed_index,
			Long observed_speed_index_ts,
			Integer observed_first_contentful_paint,
			Long observed_navigation_start_ts,
			Long observed_largest_contentful_paint_ts,
			Integer observed_first_visual_change,
			Long observed_load_ts,
			Integer first_meaningful_paint,
			Integer observed_trace_end,
			Integer observed_first_meaningful_paint,
			Integer first_cpu_idle,
			Long observed_trace_end_ts,
			Long observed_first_meaningful_paint_ts,
			Integer observed_dom_content_loaded,
			Long observed_first_visual_change_ts,
			Integer interactive,
			Integer observed_navigation_start,
			Long observed_first_contentful_paint_ts,
			Long observed_last_visual_change_ts,
			Integer observed_load,
			Integer observed_largest_contentful_paint,
			Long observed_dom_content_loaded_ts,
			Integer observed_speed_index,
			Integer estimated_input_latency,
			Integer total_blocking_time,
			Integer observed_first_paint,
			Integer observed_last_visual_change,
			Boolean lcp_invalidated
	) {
		setFirstContentfulPaint(first_contentful_paint);
		setObservedFirstContentfulPaint(observed_first_contentful_paint);
		setEstimatedInputLatency(estimated_input_latency);
		setFirstCpuIdle(first_cpu_idle);
		setFirstMeaningfulPaint(first_meaningful_paint);
		setInteractive(interactive);
		setLcpInvalidated(lcp_invalidated);
		setObservedFirstMeaningfulPaint(observed_first_meaningful_paint);
		setObservedFirstVisualChangeTs(observed_first_visual_change_ts);
		setObservedDomContentLoaded(observed_dom_content_loaded);
		setObservedDomContentLoadedTs(observed_dom_content_loaded_ts);
		setObservedFirstContentfulPaint(observed_first_contentful_paint);
		setObservedFirstContentfulPaintTs(observed_first_contentful_paint_ts);
		setObservedFirstPaint(observed_first_paint);
		setObservedFirstPaintTs(observed_first_paint_ts);
		setObservedFirstVisualChange(observed_first_visual_change);
		setObservedLargestContentfulPaint(observed_largest_contentful_paint);
		setObservedLargestContentfulPaintTs(observed_largest_contentful_paint_ts);
		setObservedLastVisualChange(observed_last_visual_change);
		setObservedLastVisualChangeTs(observed_last_visual_change_ts);
		setObservedLoad(observed_load);
		setObservedLoadTs(observed_load_ts);
		setObservedNavigationStart(observed_navigation_start);
		setObservedNavigationStartTs(observed_navigation_start_ts);
		setObservedSpeedIndex(observed_speed_index);
		setObservedSpeedIndexTs(observed_speed_index_ts);
		setObservedTraceEnd(observed_trace_end);
		setObservedTraceEndTs(observed_trace_end_ts);
		setSpeedIndex(speed_index);
		setTotalBlockingTime(total_blocking_time);
	}
}
