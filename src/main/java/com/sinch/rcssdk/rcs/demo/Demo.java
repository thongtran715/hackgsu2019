package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
import com.sinch.rcssdk.rcs.message.component.richcard.FileInfo;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;

import java.io.IOException;

public class Demo {

    public static void main(String[] args) {

        ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
        // Send Text Message
        // chatBot.sendTextMessage("+14047691562", "Hello from Sinch");

        // Send image messge
        try {
            chatBot.sendImage( "+14047691562", "https://s3.amazonaws.com/sketchers-chatbot/Revision_1/Kid/Kids+Boys'+Sport/Boys'+Kinectors+Thermovolt.jpg");
        }catch (FileSizeExceedLimitException e){
            // File size limit the recommendation
            System.out.println(e.getMessage());
        }catch (IOException e){
            // Unable to reach the url host
            System.out.println(e.getMessage());
        }

    }
}
