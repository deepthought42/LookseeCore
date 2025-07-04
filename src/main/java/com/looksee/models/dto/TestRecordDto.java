package com.looksee.models.dto;

import com.looksee.models.TestRecord;
import com.looksee.models.enums.TestStatus;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRecordDto {
	private String key;
	private Date ranAt;
	private String browser;
	private TestStatus status;
	private long runTimeLength;
	private String testKey;
	private String resultKey;
	
	//Empty constructor for spring
	public TestRecordDto(TestRecord record, String test_key){
		setKey(record.getKey());
		setRanAt(record.getRanAt());
		setBrowser(record.getBrowser());
		setStatus(record.getStatus());
		setRunTimeLength(record.getRunTime());
		setResultKey(record.getResult().getKey());
		setTestKey(test_key);
	}
}
