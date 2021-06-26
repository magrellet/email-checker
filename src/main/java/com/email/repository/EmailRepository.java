package com.email.repository;

import com.email.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * The interface Email repository.
 */
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    /**
     * Find by date and from and subject optional.
     *
     * @param date    the date
     * @param from    the from
     * @param subject the subject
     * @return the optional
     */
    Optional<Email> findByDateAndFromAndSubject(Date date, String from, String subject);
}
