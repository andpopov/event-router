package fw.er;

abstract public class Service {
    abstract public void handleEvent(Event event);

    abstract public void postCreate(Event event);

    abstract public void beforeDestroy();
}
