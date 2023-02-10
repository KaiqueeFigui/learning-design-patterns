package com.kaique.observer.pubsub.listener;

import com.kaique.observer.FileStep;
import com.kaique.observer.model.Process;

public interface EventListener {

    void update(FileStep fileStep, Process process);
}
