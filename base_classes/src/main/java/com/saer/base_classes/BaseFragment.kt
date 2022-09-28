package com.saer.base_classes

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.saer.core.Resources
import javax.inject.Inject

abstract class BaseFragment : Fragment {

    constructor(): super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    @Inject
    lateinit var resources: Resources
}