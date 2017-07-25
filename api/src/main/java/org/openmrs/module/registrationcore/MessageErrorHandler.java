package org.openmrs.module.registrationcore;

public interface MessageErrorHandler {

    void handle(String messageBody, String failureReason, String destination, String type, Boolean failure);
}
