package com.kaique.observer.service;

import com.kaique.observer.FileStep;
import com.kaique.observer.model.Person;
import com.kaique.observer.model.Process;
import com.kaique.observer.pubsub.EventManager;
import com.kaique.observer.pubsub.listener.UpdateProcessEventListener;
import com.kaique.observer.pubsub.listener.LogEventListener;
import com.kaique.observer.repository.PersonRepository;
import com.kaique.observer.repository.ProcessRepository;
import com.kaique.observer.util.FileUtil;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
public class FileService {

    public static final String CSV_EXTENSION = ".csv";
    private final EventManager eventManager;

    private final PersonRepository personRepository;
    private final ProcessRepository processRepository;

    public FileService(PersonRepository personRepository, ProcessRepository processRepository) {
        this.personRepository = personRepository;
        this.processRepository = processRepository;
        this.eventManager = new EventManager(FileStep.values());
        configureSubscriptions();
    }

    public boolean process(MultipartFile multipartFile) throws IOException, CsvException {
        this.eventManager.notify(FileStep.RECEIVED, new Process(multipartFile.getOriginalFilename()));
        var process = processRepository.findByFilename(multipartFile.getOriginalFilename()).orElseThrow();
        if (!isValidFilename(multipartFile.getOriginalFilename())) {
            this.eventManager.notify(FileStep.FAIL, process);
            return false;
        }

        if (multipartFile.getSize() == 0) {
            this.eventManager.notify(FileStep.FAIL, process);
            return false;
        }

        var fileContent = FileUtil.readCsv(new String(multipartFile.getBytes(), StandardCharsets.UTF_8), multipartFile.getOriginalFilename());
        this.eventManager.notify(FileStep.OPENED, process);

        if (isFileContentEmpty(fileContent)) {
            this.eventManager.notify(FileStep.FAIL, process);
            return false;
        }

        this.eventManager.notify(FileStep.PROCESSING, process);
        for (String[] line : fileContent) {
            var columns = line[0].split(";", -1);
            if (columns.length != 2) {
                this.eventManager.notify(FileStep.FAIL, process);
                return false;
            }

            var name = columns[0];
            var birthDate = LocalDate.parse(columns[1]);
            var person = new Person(name, birthDate);
            personRepository.save(person);
        }
        this.eventManager.notify(FileStep.SUCCESS, process);
        return true;
    }

    private static boolean isFileContentEmpty(List<String[]> fileContent) {
        return fileContent == null || fileContent.isEmpty();
    }


    private void configureSubscriptions() {
        for (FileStep value : FileStep.values()) {
            this.eventManager.subscribe(value, new LogEventListener());
            this.eventManager.subscribe(value, new UpdateProcessEventListener(processRepository));
        }
    }

    private boolean isValidFilename(String filename) {
        return filename != null && StringUtils.endsWithIgnoreCase(filename, CSV_EXTENSION);
    }
}
