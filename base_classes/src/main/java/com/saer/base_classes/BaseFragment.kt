package com.saer.base_classes

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId)