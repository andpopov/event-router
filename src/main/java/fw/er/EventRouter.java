package fw.er;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventRouter implements Runnable {
    private static final Logger log = Logger.getLogger(EventRouterManager.class);

    private Lock lock = new ReentrantLock();
    private Condition queueNotEmpty = lock.newCondition();
    private Queue queue;
    private volatile Thread thread;
    public static ThreadGroup threadGroup = new ThreadGroup("EventRouterGroup");
    private final String name;
    private static int num = 0;

    public EventRouter() {
        queue = new Queue();
        synchronized (EventRouter.class) {
            name = "EventRouter" + num;
            num++;
        }
    }

    public EventRouter(String name) {
        queue = new Queue();
        this.name = name;
        synchronized (EventRouter.class) {
            num++;
        }
    }

    public boolean addEvent(Event e) {
        try {
            lock.tryLock();
            queue.add(e);
            queueNotEmpty.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }


    public void addInitialEvent(Event event) {
        EventRouterManager.getInstance().startServices(event);
    }

    public void start() {
        thread = new Thread(threadGroup, this, name);
        thread.start();
        log.info("started ER[" + name + "]");
    }

    public void stop() {
        if(thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public void run() {
        while(true) {
            try {
                nextEvent();
            } catch (InterruptedException e) {
                break;
            }
        }
        log.info("stopped ER[" + name + "]");
    }

    private Event nextEvent() throws InterruptedException {
        Event event = null;
        try {
            lock.tryLock();
            while(queue.isEmpty()) {
                queueNotEmpty.await();
            }
            event = queue.take();
            for (Service service : event.getActivityContextInterface().getServices()) {
                try {
                    service.handleEvent(event);
                } catch (Exception e) {
                    Monitoring.alarm(Monitoring.AlarmType.SERVICE_EVENT_HANDLING_FAILURE, "Event handling error in  service", e);
                }
            }
        } finally {
            lock.unlock();
        }
        return event;
    }

    public void addFinalEvent(Event event) {

    }
}

class Queue {
    private Node head = null, tail = null;

    public boolean add(Event e) {
        Node<Event> node = new Node<Event>();
        node.event = e;
        node.next = head;
        tail = node;
        if(head != null) {
            head = node;
        }
        return true;
    }

    public Event take() {
        if(tail != null) {
            Event event = tail.event;
            tail = tail.next;
            return event;
        }
        return null;
    }

    public boolean isEmpty() {
        return tail == null;
    }
}

class Node<E extends Event> {
    E event;
    Node<E> next;
}