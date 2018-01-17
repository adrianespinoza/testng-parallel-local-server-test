/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.sfusers;

/**
 * Profile enum. Provide the available profiles that the framework handle.
 * @author Adrian Espinoza
 *
 */
public enum Profile {
    SYSADMIN,
    FULL_ACCESS_USER,
    CAB_USER,
    STANDARD_USER,
    READ_ONLY_USER,
    VENDOR_USER,
    PORTAL_USER,
    PORTAL_TENANT_USER,
    DEFAULT;

    public Profile getProfile(String sProfile) {
        Profile[] profiles = Profile.values();
        Profile profile = null;
        for (int i = 0; i < profiles.length; i++) {
            if (profiles[i].name().equals(sProfile)) {
                profile = profiles[i];
            }
        }
        return profile;
    }
}
