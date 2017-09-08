package service;

import fw.er.ActivityContextInterface;
import fw.er.Event;
import fw.er.EventID;

public class PrintInitialEvent extends Event {
    public PrintInitialEvent() {
        setInitialEvent(true);
    }

    @Override
    public EventID getEventID() {
        return new PrintInitialEventID();
    }
}
