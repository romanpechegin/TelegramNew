package com.saer.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PhoneNumberUtilsKtTest {

    @Test
    fun `if phone contains only digit and equals phoneLength phoneNumberOrNull return phone else return null`() {
        assertThat(phoneNumberOrNull("", 11)).isNull()
        assertThat(phoneNumberOrNull("afdsafsd", 11)).isNull()
        assertThat(phoneNumberOrNull("afdsafsdaaa", 11)).isNull()
        assertThat(phoneNumberOrNull("79892634770", 11)).isEqualTo("79892634770")
        assertThat(phoneNumberOrNull("+7 (989) 263-47-70", 11)).isEqualTo("79892634770")
        assertThat(phoneNumberOrNull("+7 (989) 263-47-7", 11)).isNull()
        assertThat(phoneNumberOrNull("+7 (989) 263-47-7", 10)).isEqualTo("7989263477")
    }
}