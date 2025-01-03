package com.rankmonkeysvc.constants;

public enum EventType {
  USER_CREATION("USER_CREATION"),
  USER_LOGIN("USER_LOGIN"),
  SET_PASSWORD_REQUEST_VIA_EMAIL("SET_PASSWORD_REQUEST_VIA_EMAIL"),
  SET_PASSWORD_EMAIL_SENT_FAILURE("SET_PASSWORD_EMAIL_SENT_FAILURE"),
  USER_LOGOUT("USER_LOGOUT"),
  USER_STATUS("USER_STATUS"),
  USER_UNBLOCKED("USER_UNBLOCKED"),
  BANK_NOT_FOUND("BANK_NOT_FOUND"),
  FORM_UPDATE("FORM_UPDATE"),
  OTP_INITIATE("OTP_INITIATE"),
  OTP_VERIFY("OTP_VERIFY"),
  SESSION_STATUS("SESSION_STATUS"),
  S3_RESUME_UPLOAD("S3_RESUME_UPLOAD"),
  ADMIN_ACTION("ADMIN_ACTION"),
  KYC_LINK("KYC_LINK"),
  USER_EMAIL_ADDITION("USER_EMAIL_ADDITION"),
  SQS_PUSH("SQS_PUSH"), USER_DELETE("USER_DELETE");

  private final String eventType;

  EventType(String eventType) {
    this.eventType = eventType;
  }

  private String getEventType() {
    return eventType;
  }
}