package com.fpedFIND.Data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fpedFIND.Entity.Status;
import com.fpedFIND.Repository.StatusRepository;

@Component
public class StatusDataInitializer implements CommandLineRunner {

    private final StatusRepository statusRepository;

    public StatusDataInitializer(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Check if the statuses exist, and insert them if not
        if (statusRepository.findByStatusName("New") == null) {
            Status newStatus = new Status(null, "New");
            statusRepository.save(newStatus);
        }

        if (statusRepository.findByStatusName("Pending") == null) {
            Status pendingStatus = new Status(null, "Pending");
            statusRepository.save(pendingStatus);
        }

        if (statusRepository.findByStatusName("Completed") == null) {
            Status completedStatus = new Status(null, "Completed");
            statusRepository.save(completedStatus);
            
        } if (statusRepository.findByStatusName("Ongoing") == null) {
            Status completedStatus = new Status(null, "Ongoing");
            statusRepository.save(completedStatus);
            
        } if (statusRepository.findByStatusName("Archived") == null) {
            Status completedStatus = new Status(null, "Archived");
            statusRepository.save(completedStatus);
        }
        
    }
}
