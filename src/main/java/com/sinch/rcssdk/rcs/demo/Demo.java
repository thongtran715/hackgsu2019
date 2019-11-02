package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;

import java.util.LinkedList;
import java.util.List;
public class Demo {
    public static List<String> findAllList(){
        LinkedList<String> result = new LinkedList<>();
        return result;
    }

    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
        chatBot.sendTextMessage("14047691562", "Hello from Thong to Jon");
    }
}
