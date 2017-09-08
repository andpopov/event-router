package service;

import fw.er.EventID;
import fw.er.ServiceID;

import java.util.Arrays;
import java.util.List;

public class PrintServiceID extends ServiceID {
    @Override
    public List<EventID> getInitialEventIDs() {
        return Arrays.asList(new PrintInitialEventID());
    }

    @Override
    public Class<PrintService> getServiceClass() {
        return PrintService.class;
    }
}
