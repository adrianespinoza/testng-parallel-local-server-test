/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.sfusers;

import com.itinvolve.itsm.framework.database.DatabaseSchema;
import com.sforce.soap.partner.sobject.SObject;

/**
 * User class. Provide properties to interact with the salesforce user.
 * @author Adrian Espinoza
 *
 */
public class User implements Comparable<User>{
    /** Store the user name */
    public String name;

    /** Store the user password */
    public String password;

    /** Store the user token */
    public String token;

    /** Store the user profile by default is sysadmin */
    public Profile profile = Profile.SYSADMIN;

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(User o) {
        int result = this.name.compareTo(o.name);
        if (result == 0) {
            result = this.profile.name().compareTo(o.profile.name());
        }
        return result;
    }

    /**
     * This method return the user record.
     * @return A record.
     */
    public SObject getRecord() {
        SObject user = DatabaseSchema.getUser(name);
        return user;
    }
}
