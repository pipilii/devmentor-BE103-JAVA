package com.devmentor.restfulapidemo;

import com.devmentor.restfulapidemo.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class JdbcEventController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // update: INSET, UPDATE, DELETE
    // query: SELECT

    @PostMapping("/jdbc/events")
    public int create(@RequestBody Event event) {
        String sql = "INSERT INTO events (name, trigger_time, created_at) VALUES (?, ?, ?)";
        Object[] object = new Object[] {
                event.getName(),
                event.getTriggerTime(),
                new Timestamp(System.currentTimeMillis())
        };

        return jdbcTemplate.update(sql, object);
    }

    // TODO update

    @GetMapping("/jdbc/events/{id}")
    public Event select(@PathVariable Long id) {
        String sql = "SELECT * FROM events WHERE id=?";
        Object[] objects = new Object[]{id};

        List<Event> list = jdbcTemplate.query(sql, objects, BeanPropertyRowMapper.newInstance(Event.class));
        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    // TODO select all

    // TODO delete
}
