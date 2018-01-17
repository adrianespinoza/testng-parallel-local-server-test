/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class in a SObject wrapper.
 * @author Adrian Espinoza
 *
 */
public class SObjectWrapper {
    /** Store the SObject api name */
    public String fullName;

    /** Store the the field description */
    public Map<String, FieldWrapper> fieldDescribe;

    /** Default constructor */
    public SObjectWrapper() {
        init();
    }
     /**
      * This method return a list of field api names.
      * @return A Set of field api names.
      */
    public Set<String> getFieldApiNames() {
        return fieldDescribe.keySet();
    }

    /**
     * This method recover a specific field description.
     * @param field The field to get description.
     * @return A field wrapper.
     */
    public FieldWrapper getDescribeField(String field) {
        return fieldDescribe.get(field);
    }

    /**
     * This method initialize the SObject wrapper with standard fields.
     */
    private void init() {
        FieldWrapper id = new FieldWrapper();
        id.fieldDescription = new HashMap<String, Object>();
        id.fieldDescription.put("fullName", "Id");
        id.fieldDescription.put("type", "Text");

        FieldWrapper name = new FieldWrapper();
        name.fieldDescription = new HashMap<String, Object>();
        name.fieldDescription.put("fullName", "Name");

        FieldWrapper createdBy = new FieldWrapper();
        createdBy.fieldDescription = new HashMap<String, Object>();
        createdBy.fieldDescription.put("fullName", "CreatedBy");
        createdBy.fieldDescription.put("type", "Lookup");

        FieldWrapper lastModifiedBy = new FieldWrapper();
        lastModifiedBy.fieldDescription = new HashMap<String, Object>();
        lastModifiedBy.fieldDescription.put("fullName", "LastModifiedBy");
        lastModifiedBy.fieldDescription.put("type", "Lookup");

        FieldWrapper owner = new FieldWrapper();
        owner.fieldDescription = new HashMap<String, Object>();
        owner.fieldDescription.put("fullName", "Owner");
        owner.fieldDescription.put("type", "Lookup");

        fieldDescribe = new HashMap<String, FieldWrapper>();

        fieldDescribe.put((String) id.fieldDescription.get("fullName"), id);
        fieldDescribe.put((String) name.fieldDescription.get("fullName"), name);
        fieldDescribe.put((String) createdBy.fieldDescription.get("fullName"), createdBy);
        fieldDescribe.put((String) lastModifiedBy.fieldDescription.get("fullName"), lastModifiedBy);
        fieldDescribe.put((String) owner.fieldDescription.get("fullName"), owner);
    }
}
