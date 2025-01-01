package com.rankmonkeysvc.dto.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageResponse {
	private String message;
}