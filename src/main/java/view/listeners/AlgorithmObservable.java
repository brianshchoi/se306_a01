package view.listeners;

public interface AlgorithmObservable {
    enum EventType {
        BEST_SCHEDULE_UPDATED;
    }

    void addAlgorithmListener(AlgorithmListener listener);
    void removeAlgorithmListener(AlgorithmListener listener);
    void fire(EventType eventType);
}
