package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;
import com.sinch.rcssdk.rcs.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo {


    public static void main(String[] args) {

        ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);


        // Making a list of suggested reply
        List<Pair<String, String>> suggestedReply = new ArrayList<>();

        // Declaring a key as display text, value as post-back data
        suggestedReply.add(new Pair<>("Hi Sich", "jgkaioejn21kl222131223kakdk223123213"));

        try {
            chatBot.setSuggestedReply(suggestedReply);
            chatBot.sendTextMessage("+14047691562", "Hello from Sinch");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
