package fw.er;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class EventRouterManager {
    private static final Logger log = Logger.getLogger(EventRouterManager.class);

    final private static EventRouterManager INSTANCE = new EventRouterManager();
    public static EventRouterManager getInstance() {
        return INSTANCE;
    }

    private EventRouter[] routers;
    private final Map<Object, Integer> activities = new HashMap<Object, Integer>();;
    private final Map<Object, ActivityContextInterface> activityContextInterfaces = new HashMap<>();

    private EventRouterManager() {}

    public void init(int num) {
        routers = new EventRouter[num];
    }

    public void start() {
        for (int i = 0; i < routers.length; i++) {
            EventRouter router = new EventRouter();
            routers[i] = router;
            router.start();
        }
    }

    public void stop() {
        for (EventRouter router : routers) {
            router.stop();
        }
    }

    public void fireInitialEvent(Object activity, Event event) throws IllegalStateException, IllegalAccessException, InstantiationException {
        synchronized (activities) {
            System.out.println();
            int routerIndex = Math.abs(new Random().nextInt()) % routers.length;
            activities.put(activity, routerIndex);
            ActivityContextInterface activityContextInterface = new ActivityContextInterface();
            activityContextInterfaces.put(activity, activityContextInterface);
            event.setActivityContextInterface(activityContextInterface);
            routers[routerIndex].addInitialEvent(event);
        }
    }

    public void fireEvent(Object activity, Event event) throws Exception {
        synchronized (activities) {
            if (activities.containsKey(activity)) {
                int routerIndex = activities.get(activity);
                event.setActivityContextInterface(activityContextInterfaces.get(activity));
                routers[routerIndex].addEvent(event);
            } else {
                throw new Exception("No found activity");
            }
        }
    }

    public void fireFinalEvent(Object activity, Event event) throws Exception {
        synchronized (activities) {
            if (activities.containsKey(activity)) {
                int routerIndex = activities.get(activity);
                routers[routerIndex].addFinalEvent(event);
            } else {
                throw new Exception("No found activity");
            }
        }
    }

    final private Map<ServiceID, Integer> serviceIDs = new ConcurrentHashMap<>();
    final private Map<Service, Integer> services = new ConcurrentHashMap<>();

    public void deployService(ServiceID serviceID) {
        serviceIDs.putIfAbsent(serviceID, 1);
    }

    public void startServices(Event event) {
        for (ServiceID serviceID : serviceIDs.keySet()) {
            if(serviceID.getInitialEventIDs().contains(event.getEventID())) {
                Class<? extends Service> serviceClass = serviceID.getServiceClass();
                if(serviceClass != null) {
                    Service service = null;
                    try {
                        service = serviceClass.newInstance();
                    } catch (Exception e) {
                        Monitoring.alarm(Monitoring.AlarmType.SERVICE_INSTANCE_CREATION_FAILURE, "Cannot instantinate service", e);
                        continue;
                    }
                    try {
                        event.getActivityContextInterface().attach(service);
                        service.postCreate(event);
                    } catch (Throwable e) {
                        event.getActivityContextInterface().detach(service);
                        Monitoring.alarm(Monitoring.AlarmType.SERVICE_POST_CREATE_ERROR, "Service post create error", e);
                        continue;
                    }
                    services.put(service, 1);
                    Monitoring.alarm(Monitoring.AlarmType.SERVICE_INSTANCE_CREATED, "Service " + service + " created successfully");
                }
            }
        }
    }

}
