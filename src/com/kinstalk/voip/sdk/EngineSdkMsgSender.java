/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public enum EngineSdkMsgSender {
  ES_MSG_SENDER_ANY,
  ES_MSG_SENDER_UI_DIALOG,
  ES_MSG_SENDER_UI_CALL;

  public final int swigValue() {
    return swigValue;
  }

  public static EngineSdkMsgSender swigToEnum(int swigValue) {
    EngineSdkMsgSender[] swigValues = EngineSdkMsgSender.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (EngineSdkMsgSender swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + EngineSdkMsgSender.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private EngineSdkMsgSender() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private EngineSdkMsgSender(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private EngineSdkMsgSender(EngineSdkMsgSender swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

