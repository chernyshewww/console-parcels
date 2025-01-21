package com.hofftech.deliverysystem.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BillingExceptionTest {

    @Test
    void testBillingExceptionMessageAndCause() {
        String message = "Billing error occurred";
        Throwable cause = new RuntimeException("Underlying cause");

        BillingException exception = new BillingException(message, cause);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testBillingExceptionWithoutCause() {
        String message = "Billing error occurred";

        BillingException exception = new BillingException(message, null);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }
}
