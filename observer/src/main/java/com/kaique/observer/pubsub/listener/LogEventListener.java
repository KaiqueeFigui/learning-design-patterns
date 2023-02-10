package com.kaique.observer.pubsub.listener;

import com.kaique.observer.FileStep;
import com.kaique.observer.model.Process;

public class LogEventListener implements EventListener {

    @Override
    public void update(FileStep fileStep, Process process) {
        System.out.println("Process in step: " + fileStep.name());
    }
}
