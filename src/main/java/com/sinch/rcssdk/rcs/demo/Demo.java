package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;

import java.io.IOException;

public class Demo {


    public static void main(String[] args) {

        ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);

        String video = "https://s3.amazonaws.com/sketchers-chatbot/Video/Mark+Nason+Dress+Knit+Commercial.mp4";
        String pig = "https://s3.amazonaws.com/sketchers-chatbot/Video/Picture1.png";
        try {
            chatBot.sendVideo("+14047691562", video, pig );
        }catch (FileSizeExceedLimitException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }


    }
}
