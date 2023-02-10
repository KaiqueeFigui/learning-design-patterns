package com.kaique.observer.pubsub.listener;

import com.kaique.observer.FileStep;
import com.kaique.observer.model.Process;
import com.kaique.observer.repository.ProcessRepository;

import java.time.LocalDateTime;

public class UpdateProcessEventListener implements EventListener {

    private final ProcessRepository processRepository;

    public UpdateProcessEventListener(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public void update(FileStep fileStep, Process process) {
        process.setStatus(fileStep.name());
        process.setUpdatedAt(LocalDateTime.now());
        processRepository.save(process);
    }
}
