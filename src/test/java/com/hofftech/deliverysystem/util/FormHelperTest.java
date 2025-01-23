package com.hofftech.deliverysystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FormHelperTest {

    private FormHelper formHelper;

    @BeforeEach
    void setUp() {
        formHelper = new FormHelper();
    }

    @Test
    @DisplayName("Должен корректно преобразовать форму в строку")
    void testGetFormAsString() {
        char[][] form = {
                {'x', 'x'},
                {'x', 'x'}
        };

        String formString = formHelper.getFormAsString(form);

        assertThat(formString).isEqualTo("xx\nxx\n");
    }

    @Test
    @DisplayName("Должен возвращать пустую строку для пустой формы")
    void testGetFormAsString_EmptyForm() {
        char[][] form = new char[0][0];

        String formString = formHelper.getFormAsString(form);

        assertThat(formString).isEmpty();
    }
}
