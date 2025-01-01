package com.rankmonkeysvc.dto.support;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SupportRequest {
    private String issueTitle;
    private String issueDescription;
    private String priority;
    private String comments;
}