package com.itinvolve.itsm.framework.validators;

import com.itinvolve.itsm.framework.database.SObjectUtils;

public class SObejctEntity {
    public String apiName;
    public ValueComparer valueComp;

    public StandardField withStandardField(String fieldApiName) {
        StandardField sField = new StandardField();

        fieldApiName = SObjectUtils.addPrefix(fieldApiName);

        sField.apiName = fieldApiName;
        sField.parentSObject = this;
        return sField;
    }

    public ReferenceField withReferenceField(String fieldApiName) {
        ReferenceField rField = new ReferenceField();

        fieldApiName = SObjectUtils.convertToReferenceField(fieldApiName);
        fieldApiName = SObjectUtils.addPrefix(fieldApiName);

        rField.referenceField = fieldApiName;
        rField.parentSObject = this;
        return rField;
    }
}
