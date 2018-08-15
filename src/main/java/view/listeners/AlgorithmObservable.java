package view.listeners;

public interface AlgorithmObservable {
    enum EventType {
        BEST_SCHEDULE_UPDATED,
        ALGORTHIM_FINISHED
    }

    void addAlgorithmListener(AlgorithmListener listener);
    void removeAlgorithmListener(AlgorithmListener listener);
    void fire(EventType eventType);
}
