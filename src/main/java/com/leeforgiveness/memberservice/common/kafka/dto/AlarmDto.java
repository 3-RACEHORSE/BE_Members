package com.leeforgiveness.memberservice.common.kafka.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AlarmDto {

	private List<String> receiverUuids;
	private String message;
	private String eventType;
	private String uuid;

	@Builder
	public AlarmDto(List<String> receiverUuids, String message, String eventType, String uuid) {
		this.receiverUuids = receiverUuids;
		this.message = message;
		this.eventType = eventType;
		this.uuid = uuid;
	}
}
