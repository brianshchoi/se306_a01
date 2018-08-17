package view.listeners;

/**
 * Observable in the observer pattern -
 * the Algorithm to be observed and register listeners with.
 */
public interface AlgorithmObservable {
    enum EventType {
        BEST_SCHEDULE_UPDATED,
        ALGORTHIM_FINISHED,
        NUM_BRANCHES_CHANGED
    }

    /**
     * Add listener
     * @param listener
     */
    void addAlgorithmListener(AlgorithmListener listener);

    /**
     * Remove Listener
     * @param listener
     */
    void removeAlgorithmListener(AlgorithmListener listener);

    /**
     * Fire an event
     * @param eventType
     */
    void fire(EventType eventType);
}
