package com.supportportal.constant;


public class Authority {
    public static final String[] USER_AUTHORITIES = { AccessRole.USER.name() + ":"+ Module.ENQUIRY.name()+":"+ Action.READ.name()};
    public static final String[] ADMIN_AUTHORITIES = { AccessRole.ADMIN.name() + ":"+ Module.USER.name()+":"+ Action.UPDATE.name()};
}
