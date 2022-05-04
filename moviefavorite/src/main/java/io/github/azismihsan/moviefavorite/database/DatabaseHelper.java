/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import com.android.internal.telephony.SmsConstants;
import com.android.internal.util.HexDump;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

/**
 * SMS user data header, as specified in TS 23.040 9.2.3.24.
 */
public class SmsHeader {

    // TODO(cleanup): this data structure is generally referred to as
    // the 'user data header' or UDH, and so the class name should
    // change to reflect this...

    /** SMS user data header information element identifiers.
     * (see TS 23.040 9.2.3.24)
     */
    public static final int ELT_ID_CONCATENATED_8_BIT_REFERENCE       = 0x00;
    public static final int ELT_ID_SPECIAL_SMS_MESSAGE_INDICATION     = 0x01;
    public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_8_BIT  = 0x04;
    public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_16_BIT = 0x05;
    public static final int ELT_ID_SMSC_CONTROL_PARAMS                = 0x06;
    public static final int ELT_ID_UDH_SOURCE_INDICATION              = 0x07;
    public static final int ELT_ID_CONCATENATED_16_BIT_REFERENCE      = 0x08;
    public static final int ELT_ID_WIRELESS_CTRL_MSG_PROTOCOL         = 0x09;
    public static final int ELT_ID_TEXT_FORMATTING                    = 0x0A;
    public static final int ELT_ID_PREDEFINED_SOUND                   = 0x0B;
    public static final int ELT_ID_USER_DEFINED_SOUND                 = 0x0C;
    public static final int ELT_ID_PREDEFINED_ANIMATION               = 0x0D;
    public static final int ELT_ID_LARGE_ANIMATION                    = 0x0E;
    public static final int ELT_ID_SMALL_ANIMATION                    = 0x0F;
    public static final int ELT_ID_LARGE_PICTURE                      = 0x10;
    public static final int ELT_ID_SMALL_PICTURE                      = 0x11;
    public static final int ELT_ID_VARIABLE_PICTURE                   = 0x12;
    public static final int ELT_ID_USER_PROMPT_INDICATOR              = 0x13;
    public static final int ELT_ID_EXTENDED_OBJECT                    = 0x14;
    public static final int ELT_ID_REUSED_EXTENDED_OBJECT             = 0x15;
    public static final int ELT_ID_COMPRESSION_CONTROL                = 0x16;
    public static final int ELT_ID_OBJECT_DISTR_INDICATOR             = 0x17;
    public sta