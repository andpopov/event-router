package service;

import fw.er.EventID;

import java.io.Serializable;

public class PrintSimpleEventID extends EventID {
    @Override
    public Serializable getID() {
        return "PrintSimpleEventID";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if( !(this instanceof PrintSimpleEventID) ) return false;
        return true;
    }
}
