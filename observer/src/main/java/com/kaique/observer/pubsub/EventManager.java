package com.kaique.observer.pubsub;

import com.kaique.observer.FileStep;
import com.kaique.observer.model.Process;
import com.kaique.observer.pubsub.listener.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<FileStep, List<EventListener>> listeners = new HashMap<>();

    public EventManager(FileStep... fileSteps) {
        for (FileStep fileStep : fileSteps) {
            this.listeners.put(fileStep, new ArrayList<>());
        }
    }

    public void subscribe(FileStep fileStep, EventListener eventListener) {
        this.listeners.get(fileStep).add(eventListener);
    }

    public void unsubscribe(FileStep fileStep, EventListener eventListener) {
        this.listeners.get(fileStep).remove(eventListener);
    }

    public void notify(FileStep fileStep, Process process) {
        this.listeners.get(fileStep).forEach(listener -> listener.update(fileStep, process));
    }
}
