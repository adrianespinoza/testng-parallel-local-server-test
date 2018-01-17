package com.itinvolve.itsm.framework.validators;

import com.itinvolve.itsm.framework.database.SObjectUtils;

public class ReferenceField {
    public String referenceField;
    public SObejctEntity parentSObject;

    public StandardField withStandardField(String fieldApiName) {
        StandardField sField = new StandardField();

        fieldApiName = SObjectUtils.addPrefix(fieldApiName);

        sField.apiName = this.referenceField + "." + fieldApiName;
        sField.parentSObject = this.parentSObject;
        return sField;
    }

    public ReferenceField withReferenceField(String fieldApiName) {
        fieldApiName = SObjectUtils.convertToReferenceField(fieldApiName);
        fieldApiName = SObjectUtils.addPrefix(fieldApiName);

        this.referenceField = this.referenceField + "." + fieldApiName;

        return this;
    }
}
