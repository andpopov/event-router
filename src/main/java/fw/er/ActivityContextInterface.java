package fw.er;

import java.util.ArrayList;
import java.util.List;

public class ActivityContextInterface {
    final private List<Service> services =  new ArrayList<>();
    public void attach(Service service) {
        services.add(service);
    }

    public void detach(Service service) {
        services.remove(service);
    }

    public List<Service> getServices() {
        return services;
    }
}
