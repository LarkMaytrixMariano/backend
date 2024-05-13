package com.fpedFIND.Data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fpedFIND.Entity.FileSection;

import com.fpedFIND.Repository.FileSectionRepository;

@Component
public class SectionDataInitializer implements CommandLineRunner {

    private final FileSectionRepository sectionRepository;

    public SectionDataInitializer(FileSectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Check if the sections exist, and insert them if not
        insertSectionIfNotExist("Office of the Chief");
        insertSectionIfNotExist("MIS");
        insertSectionIfNotExist("PPDS");
        insertSectionIfNotExist("FES");
        insertSectionIfNotExist("FCS");
        insertSectionIfNotExist("PMES");
    }

    private void insertSectionIfNotExist(String sectionName) {
        if (sectionRepository.findBySectionName(sectionName) == null) {
        	FileSection section = new FileSection();
            section.setSectionName(sectionName);
            sectionRepository.save(section);
        }
    }
}
