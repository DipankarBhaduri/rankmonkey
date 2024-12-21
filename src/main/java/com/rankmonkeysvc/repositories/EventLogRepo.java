package com.rankmonkeysvc.repositories;

import com.rankmonkeysvc.dao.EventLogDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepo extends JpaRepository<EventLogDAO, Long> {

}