package com.fpedFIND.Data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitialization {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitialization(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        String dropConstraintSQL = "ALTER TABLE fped_comments DROP FOREIGN KEY FKni7chfwy4ie3rlnedl5x30v9m";

        try {
            jdbcTemplate.execute(dropConstraintSQL);
          //  System.out.println("Existing foreign key constraint dropped successfully.");
        } catch (Exception e) {
            System.err.println("Error dropping foreign key constraint: " + e.getMessage());
        }

        // Now, try to add the foreign key constraint again
        String addConstraintSQL = "ALTER TABLE fped_comments " +
                "ADD CONSTRAINT FKni7chfwy4ie3rlnedl5x30v9m " +
                "FOREIGN KEY (file_id) " +
                "REFERENCES fped_files(file_id) " +
                "ON DELETE CASCADE";

        try {
            jdbcTemplate.execute(addConstraintSQL);
         //   System.out.println("Foreign key constraint added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding foreign key constraint: " + e.getMessage());
        }
    }
}
