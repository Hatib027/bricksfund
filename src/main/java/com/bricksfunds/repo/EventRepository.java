package com.bricksfunds.repo;

import com.bricksfunds.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Integer>{

}
