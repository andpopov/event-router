package fw.er;

abstract public class Event {
    private ActivityContextInterface activityContextInterface;
    private boolean initialEvent = false;

    public void setActivityContextInterface(ActivityContextInterface activityContextInterface) {
        this.activityContextInterface = activityContextInterface;
    }

    public ActivityContextInterface getActivityContextInterface() {
        return activityContextInterface;
    }

    public void setInitialEvent(boolean initialEvent) {
        this.initialEvent = initialEvent;
    }

    public boolean isInitialEvent() {
        return initialEvent;
    }

    abstract public EventID getEventID();
}
