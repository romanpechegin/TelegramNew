package com.saer.telegramnew.ui

import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.telegramnew.databinding.FragmentListOfChatsBinding

class ListOfChatsFragment : BaseFragment() {

    private val binding: FragmentListOfChatsBinding by viewBinding(CreateMethod.INFLATE)


}