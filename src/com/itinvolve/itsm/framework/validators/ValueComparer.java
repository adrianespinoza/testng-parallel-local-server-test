package com.itinvolve.itsm.framework.validators;

import com.itinvolve.itsm.framework.database.SObjectUtils;

public class ValueComparer {
    public String valueTo;
    public SObejctEntity inSObject(String sObejctApiName) {
        SObejctEntity sfo = new SObejctEntity();

        sObejctApiName = SObjectUtils.addPrefix(sObejctApiName);

        sfo.apiName = sObejctApiName;
        sfo.valueComp = this;
        return sfo;
    }
}
