package service;

import fw.er.Event;
import fw.er.EventID;

public class PrintSimpleEvent extends Event {
    @Override
    public EventID getEventID() {
        return new PrintSimpleEventID();
    }
}
