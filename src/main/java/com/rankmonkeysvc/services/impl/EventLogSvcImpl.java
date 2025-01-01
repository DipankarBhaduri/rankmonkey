package com.rankmonkeysvc.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rankmonkeysvc.dao.EventLogDAO;
import com.rankmonkeysvc.dto.event.EventLog;
import com.rankmonkeysvc.repositories.EventLogRepo;
import com.rankmonkeysvc.services.EventLogSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.rankmonkeysvc.messages.ErrorLogs.LOG_WITH_PAYLOAD;

@Slf4j
@Service
public class EventLogSvcImpl implements EventLogSvc {

  private final EventLogRepo eventLogRepo;

  private final ObjectMapper objectMapper;

  @Autowired
  public EventLogSvcImpl(EventLogRepo eventLogRepo, ObjectMapper objectMapper) {
    this.eventLogRepo = eventLogRepo;
    this.objectMapper = objectMapper;
  }

  @Override
  public void record(EventLog eventLog, String userID) {
    EventLogDAO eventLogDAO = new EventLogDAO();
	
    try {
		eventLogDAO
			  .setUserId(userID)
			  .setReferenceId(eventLog.getReferenceId())
			  .setEventType(eventLog.getEventType())
			  .setData(objectMapper.writeValueAsString(eventLog.getData()));
    } catch (JsonProcessingException e) {
      log.error(LOG_WITH_PAYLOAD, userID, eventLog, e);
    }
	eventLogRepo.save(eventLogDAO);
  }
}
