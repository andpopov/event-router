package service;

import fw.er.Activity;
import fw.er.EventRouterManager;
import fw.er.Service;

import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        EventRouterManager manager = EventRouterManager.getInstance();
        manager.init(3);
        manager.start();

        manager.deployService(new PrintServiceID());

        Object activity = new Object();
        manager.fireInitialEvent(activity, new PrintInitialEvent());
        manager.fireEvent(activity, new PrintSimpleEvent());

//        Thread.sleep(1000);
//        manager.stop();

    }
}
