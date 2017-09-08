package fw.er;

public class Monitoring {
    public static enum AlarmType {
        SERVICE_INSTANCE_CREATION_FAILURE,
        SERVICE_POST_CREATE_ERROR,
        SERVICE_INSTANCE_CREATED,
        SERVICE_EVENT_HANDLING_FAILURE
    }

    public static void alarm(AlarmType alarmType, String message) {
        System.err.println("Monitoring:[alarmType=" + alarmType + "]" + message);
    }

    public static void alarm(AlarmType alarmType, String message, Throwable error) {
        System.err.println("Monitoring:[alarmType=" + alarmType + "]" + message + (error != null ? ", error=" + error.getMessage() : ""));
        if(error != null) {
            error.printStackTrace();
        }
    }

}
