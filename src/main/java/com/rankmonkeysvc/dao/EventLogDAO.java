package com.rankmonkeysvc.dao;

import com.rankmonkeysvc.constants.EventType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "event_log")
@Accessors(chain = true)
public class EventLogDAO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "user_id")
	private String userId;
	
	@Column(name = "reference_id")
    private String referenceId;
	
	@Column(name = "event_type")
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column(name = "data")
	private String data;
	
	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
}