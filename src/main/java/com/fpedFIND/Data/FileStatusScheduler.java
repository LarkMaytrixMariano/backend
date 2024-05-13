package com.fpedFIND.Data;


import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fpedFIND.Entity.File;
import com.fpedFIND.Entity.Log;
import com.fpedFIND.Repository.LogRepository;
import com.fpedFIND.Service.UserService;

@Component
public class FileStatusScheduler {

    @Autowired
    private UserService fileService;
    
	//@Autowired
	//private FileRepository repo;
	
	@Autowired
	private LogRepository logRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(FileStatusScheduler.class);


    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // initial delay set to 0 milliseconds, runs every 24 hours
    public void updateFileStatus() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        List<File> newFiles = fileService.findNewFilesUploadedBefore(twoDaysAgo);
        logger.info("Scheduler New File running. {} New file/s found to update.", newFiles.size());

        for (File file : newFiles) {
            file.setStatusName("Ongoing");
            fileService.updateFile(file);
            
            Log log = new Log();
            log.setMessage("System Action: File with ID: " +file.getFileId() +" updated to status 'Ongoing'.");
            log.setTimestamp(LocalDateTime.now());
            logRepository.save(log);
        }
    }
    
//    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // initial delay set to 0 milliseconds, runs every 24 hours
//    public void updateFileStatusforCompleted() {
//        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
//        List<File> filesToUpdate = repo.findFilesByStatusChangedBeforeAndStatusName(fiveDaysAgo, "Completed");
//
//        logger.info("Scheduler Archived running. {} files found to update.", filesToUpdate.size());
//        
//        
// 
//
//        for (File file : filesToUpdate) {
//            file.setStatusName("Archived");
//            repo.save(file);
//            logger.info("File with ID {} updated to status 'Archived'.", file.getFileId());
//            
//            Log log = new Log();
//            log.setMessage("System Action: File with ID: " +file.getFileId() +" updated to status 'Archived'.");
//            log.setTimestamp(LocalDateTime.now());
//            logRepository.save(log);
//        }
//        
//        
//    }
    
    
    // SCHEDULER FOR UPLOADED DATE TIME AND CURRENT DATE TODAY.
//    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // initial delay set to 0 milliseconds, runs every 24 hours
//    public void updateFileStatusForUploadDateTime() {
//        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
//        List<File> filesToUpdate = repo.findFilesByUploadDateTimeBeforeAndStatusName(twoDaysAgo, "Completed");
//
//        logger.info("UploadDateTimeScheduler running. {} files found to update.", filesToUpdate.size());
//
//        for (File file : filesToUpdate) {
//            file.setStatusName("Archived");
//            fileService.updateFile(file);
//            logger.info("File with ID {} updated to status 'Archived'.", file.getFileId());
//        }
//    }
    
    
    
    
//    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // initial delay set to 0 milliseconds, runs every 24 hours
//    public void updateFileStatusForDeadline() {
//        LocalDate oneDayAfter = LocalDate.now().minusDays(1);
//
//        // Find files with "Ongoing" status and deadline one day after today or later
//        List<File> filesToUpdate = repo.findFilesByStatusNameAndDeadline("Ongoing", oneDayAfter);
//
//        logger.info("DeadlineScheduler running. {} files found to update to Pending status.", filesToUpdate.size());
//
//
//        for (File file : filesToUpdate) {
//            file.setStatusName("Pending");
//            fileService.updateFile(file);
//            logger.info("System: File with ID {} updated to status 'Pending' because its deadline is met after 1 day or more.", file.getFileId());
//        
//            Log log = new Log();
//            log.setMessage("System Action: File with ID: " +file.getFileId() +" updated to status 'Pending'.");
//            log.setTimestamp(LocalDateTime.now());
//            logRepository.save(log);
//        
//        }
//    }
// 
    
    
    
    
    
    
}