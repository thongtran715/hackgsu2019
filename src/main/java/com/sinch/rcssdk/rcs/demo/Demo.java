package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;

public class Demo {


    public static void main(String[] args) {

        ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);

        chatBot.sendTextMessage("+14047691562", "Hello from Sinch");


    }
}
