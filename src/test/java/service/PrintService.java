package service;

import fw.er.Event;
import fw.er.Service;

public class PrintService extends Service {
    @Override
    public void handleEvent(Event event) {
        System.out.println("Handle: " + event);
    }

    @Override
    public void postCreate(Event event) {
        System.out.println("postCreate: " + event);
    }

    @Override
    public void beforeDestroy() {
    }
}
