package com.saer.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class ViewUtilsKtTest {

    @Test
    fun test() {
        val array = arrayOfNulls<Char?>(5)
        assertThat(lastNotNullIndexFromArray(array)).isEqualTo(0)
        array[3] = Char(3)
        assertThat(lastNotNullIndexFromArray(array)).isEqualTo(3)
        array[4] = Char(3)
        assertThat(lastNotNullIndexFromArray(array)).isEqualTo(4)
    }
}