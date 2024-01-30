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
        Object[] object = new Object[]{
                event.getName(),
                event.getTriggerTime(),
                new Timestamp(System.currentTimeMillis())
        };

        return jdbcTemplate.update(sql, object);
    }

    // TODO update
    @PutMapping("/jdbc/events/{id}")
    public String update(@PathVariable Long id, @RequestBody Event updatedEvent) {
        String sql = "UPDATE events SET name = ?, trigger_time = ? WHERE id = ?";
        Object[] object = new Object[]{
                updatedEvent.getName(),
                updatedEvent.getTriggerTime(),
                id
        };

        int rowsAffected = jdbcTemplate.update(sql, object);

        if (rowsAffected > 0) {
            return "Event with ID " + id + " updated successfully.";
        } else {
            return "Event with ID " + id + " not found or could not be updated.";
        }
    }


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
    @GetMapping("/jdbc/events")
    public List<Event> selectAll() {
        String sql = "SELECT * FROM events";
        List<Event> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Event.class));
        return list;
    }


    // TODO delete
    @DeleteMapping("/jdbc/events/{id}")
    public String delete(@PathVariable Long id) {
        String sql = "DELETE FROM events WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        //透過被影響的行數 rowsAffected 來判斷是否刪除成功

        if (rowsAffected > 0) {
            return "Event with ID " + id + " deleted successfully.";
        } else {
            return "Event with ID " + id + " not found or could not be deleted.";
        }
    }
}
