package view.listeners;

public interface AlgorithmObservable {
    enum EventType {
        BEST_SCHEDULE_UPDATED,
        ALGORTHIM_FINISHED,
        NUM_BRANCHES_CHANGED
    }

    void addAlgorithmListener(AlgorithmListener listener);
    void removeAlgorithmListener(AlgorithmListener listener);
    void fire(EventType eventType);
}
