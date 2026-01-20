package com.example.helpinghands.ai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.helpinghands.databinding.FragmentChatbotBinding
import com.example.helpinghands.ai.viewmodel.ChatBotViewModel

class ChatBotFragment : Fragment() {

    private lateinit var binding: FragmentChatbotBinding
    private val viewModel: ChatBotViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatbotBinding.inflate(inflater, container, false)

        binding.sendButton.setOnClickListener {
            val msg = binding.userInput.text.toString()
            viewModel.sendMessage(msg)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.botReply.collect { reply ->
                binding.botResponse.text = reply
            }
        }


        return binding.root
    }
}
