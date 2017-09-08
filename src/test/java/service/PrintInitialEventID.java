package service;

import fw.er.EventID;

import java.io.Serializable;

public class PrintInitialEventID extends EventID {
    @Override
    public Serializable getID() {
        return "PrintInitialEventID";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if( !(this instanceof PrintInitialEventID) ) return false;
        return true;
    }
}
