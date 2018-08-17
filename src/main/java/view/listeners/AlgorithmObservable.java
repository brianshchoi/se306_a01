package view.listeners;

public interface AlgorithmObservable {

    enum EventType {
        BEST_SCHEDULE_UPDATED,
        ALGORTHIM_FINISHED,
        NUM_BRANCHES_CHANGED
    }

    /**
     * add listener
     * @param listener
     */
    void addAlgorithmListener(AlgorithmListener listener);

    /**
     * Remove Listener
     * @param listener
     */
    void removeAlgorithmListener(AlgorithmListener listener);

    /**
     * Notify the event
     * @param eventType
     */
    void fire(EventType eventType);
}
