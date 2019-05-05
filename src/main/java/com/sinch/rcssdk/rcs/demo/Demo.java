package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
import com.sinch.rcssdk.rcs.message.component.action.DialPhoneNumber;
import com.sinch.rcssdk.rcs.message.component.action.OpenUrl;
import com.sinch.rcssdk.rcs.message.component.action.RequestLocationPush;
import com.sinch.rcssdk.rcs.message.component.action.ShowLocation;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedAction;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedReply;
import com.sinch.rcssdk.rcs.message.component.suggestions.Suggestion;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {


    public static void main(String[] args) {

            ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
            chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);

            // List of actions
            List<SuggestedAction>  actions = new ArrayList<>();

            // Set display text as well as post-back data.
            SuggestedAction requestLocationPushAction = new SuggestedAction("Show my location", null);

            RequestLocationPush requestLocationPush = new RequestLocationPush();
            requestLocationPushAction.setAction(requestLocationPush);

            actions.add(requestLocationPushAction);

            try{
                chatBot.setSuggestedActions(actions);
                chatBot.sendTextMessage("+14047691562", "Do you want to share your location?");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


    }
}
