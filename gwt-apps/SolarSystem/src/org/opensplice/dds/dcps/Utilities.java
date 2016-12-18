/*
 *                         OpenSplice DDS
 *
 *   This software and documentation are Copyright 2006 to 2009 PrismTech 
 *   Limited and its licensees. All rights reserved. See file:
 *
 *                     $OSPL_HOME/LICENSE 
 *
 *   for full copyright notice and license terms. 
 *
 */

package org.opensplice.dds.dcps;

/**
 * 
 * 
 * @date Jun 9, 2005
 */
public class Utilities {
    public static final int EXCEPTION_TYPE_BAD_PARAM = 0;
    public static final int EXCEPTION_TYPE_NO_MEMORY = 1;
    public static final int EXCEPTION_TYPE_MARSHAL = 2;
    public static final int EXCEPTION_TYPE_BAD_OPERATION = 3;

    private Utilities() {
    }

    public static RuntimeException createException(int errorCode,
            String errorMessage) {
        RuntimeException exc = null;

        switch (errorCode) {
        case EXCEPTION_TYPE_BAD_PARAM:
            System.out.println("bad param exception thrown");
            break;
        case EXCEPTION_TYPE_NO_MEMORY:
            System.out.println("no memory exception thrown");
            break;
        case EXCEPTION_TYPE_MARSHAL:
            System.out.println("marshal exception thrown");
            break;
        case EXCEPTION_TYPE_BAD_OPERATION:
            System.out.println("bad operation exception thrown");
            break;
        default:
            assert false : "Unknown error code.";
        }
        return exc;
    }
}
