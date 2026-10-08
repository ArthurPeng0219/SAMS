package com.arthur.sams.repository;

import com.arthur.sams.entity.TimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Long> {

    List<TimelineEvent> findAllByOrderBySortOrderAsc();
}
