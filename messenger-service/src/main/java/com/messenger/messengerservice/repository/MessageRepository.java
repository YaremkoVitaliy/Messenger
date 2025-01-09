package com.messenger.messengerservice.repository;

import com.messenger.messengerservice.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query(value = """
            select m.* 
            from (select *
                  from messages
                  order by sending_date_time desc 
                  limit 50
                 ) m
            order by sending_date_time asc""", nativeQuery = true)
    List<Message> findLast50Messages();
}
