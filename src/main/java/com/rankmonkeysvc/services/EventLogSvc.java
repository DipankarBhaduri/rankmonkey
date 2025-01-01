package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.event.EventLog;

public interface EventLogSvc {
  void record(EventLog eventLog, String userID);
}
