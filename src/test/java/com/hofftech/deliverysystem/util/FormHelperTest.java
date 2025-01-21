package com.hofftech.deliverysystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FormHelperTest {

    private FormHelper formHelper;

    @BeforeEach
    void setUp() {
        formHelper = new FormHelper();
    }

    @Test
    void testGetFormAsString() {
        char[][] form = {
                {'x', 'x'},
                {'x', 'x'}
        };

        String formString = formHelper.getFormAsString(form);

        assertThat(formString).isEqualTo("xx\nxx\n");
    }

    @Test
    void testGetFormAsString_EmptyForm() {
        char[][] form = new char[0][0];

        String formString = formHelper.getFormAsString(form);

        assertThat(formString).isEmpty();
    }
}
