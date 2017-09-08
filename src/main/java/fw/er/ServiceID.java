package fw.er;

import java.util.List;

abstract public class ServiceID {
    abstract public List<EventID> getInitialEventIDs();
    abstract public Class<? extends Service> getServiceClass();
}
