package com.saer.telegramnew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.di.lazyViewModel
import com.saer.telegramnew.databinding.FragmentListOfChatsBinding
import com.saer.telegramnew.di.ChatsComponentViewModel

class ListOfChatsFragment : BaseFragment() {

    private val viewModel: ListOfChatsViewModel by lazyViewModel {
        ViewModelProvider(this).get<ChatsComponentViewModel>()
            .chatsComponent.listOfChatsViewModel().create()
    }

    private val binding: FragmentListOfChatsBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }

}