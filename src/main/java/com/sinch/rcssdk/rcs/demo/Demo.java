package com.sinch.rcssdk.rcs.demo;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
import com.sinch.rcssdk.rcs.message.component.action.DialPhoneNumber;
import com.sinch.rcssdk.rcs.message.component.action.OpenUrl;
import com.sinch.rcssdk.rcs.message.component.action.RequestLocationPush;
import com.sinch.rcssdk.rcs.message.component.action.ShowLocation;
import com.sinch.rcssdk.rcs.message.component.postback.PostBack;
import com.sinch.rcssdk.rcs.message.component.richcard.FileInfo;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardContent;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardMedia;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedAction;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedReply;
import com.sinch.rcssdk.rcs.message.component.suggestions.Suggestion;
import com.sinch.rcssdk.rcs.message.enums.HeightType;
import com.sinch.rcssdk.rcs.message.enums.OrientationType;
import com.sinch.rcssdk.rcs.message.enums.ThumbnailAlignmentType;
import com.sinch.rcssdk.rcs.message.enums.WidthType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;
import com.sinch.rcssdk.rcs.message.messagetype.TextMessage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {


    public static void main(String[] args) {

            ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
            chatBot.setSupplier(AgentMessage.Supplier.GOOGLE);

            String video = "https://s3.amazonaws.com/sketchers-chatbot/Video/Mark+Nason+Dress+Knit+Commercial.mp4";
            String pig = "https://s3.amazonaws.com/sketchers-chatbot/Video/Picture1.png";


            RichCardContent richCardContent = new RichCardContent();
            FileInfo videoFile  = new FileInfo(FileInfo.Mime_type.VIDEO_MP4, "video.mp4" , video);
            FileInfo videoImage = new FileInfo(FileInfo.Mime_type.IMAGE_PNG, "pig.png",pig);
            richCardContent.setMedia(new RichCardMedia(videoFile, videoImage, HeightType.SHORT));
            richCardContent.setTitle("Sinch hello");
            richCardContent.setDescription("Hello From Sinch");

            // List of suggestions reply
            List<Suggestion> suggestions = new ArrayList<>();

            // Suggested Reply
            suggestions.add(new SuggestedReply("Hello 1", new PostBack("asdasdadasda1221313"))) ;
            suggestions.add(new SuggestedReply("Hello 4", new PostBack("asdasdadasda1221313"))) ;

            richCardContent.setSuggestions(suggestions);

            List<RichCardContent> richCardContents = new ArrayList<>();
            richCardContents.add(richCardContent);
            richCardContents.add(richCardContent);
            richCardContents.add(richCardContent);
            richCardContents.add(richCardContent);
            richCardContents.add(richCardContent);

            chatBot.setRichCardContents(richCardContents);
            chatBot.setWidthType(WidthType.MEDIUM);
            try {
                chatBot.sendCarousel("+14042329644");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


    }
}
