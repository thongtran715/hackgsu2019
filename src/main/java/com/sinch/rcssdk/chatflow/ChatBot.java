package com.sinch.rcssdk.chatflow;

import com.sinch.rcssdk.rcs.message.component.postback.PostBack;
import com.sinch.rcssdk.rcs.message.component.richcard.FileInfo;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardContent;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedAction;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedReply;
import com.sinch.rcssdk.rcs.message.component.suggestions.Suggestion;
import com.sinch.rcssdk.rcs.message.enums.OrientationType;
import com.sinch.rcssdk.rcs.message.enums.ThumbnailAlignmentType;
import com.sinch.rcssdk.rcs.message.enums.WidthType;
import com.sinch.rcssdk.rcs.message.messagetype.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatBot {
    // Sending blank message
    private TextMessage textMessage;
    // Sending Carousel message
    private CarouselRichCardMessage carouselRichCardMessage;
    // Sending Standalone message
    private StandaloneRichCardMessage standaloneRichCardMessage;

    private FileMessage fileMessage;

    private AgentMessage agentMessage;

    private AgentConfiguration agentConfiguration;

    private List<Suggestion> sug;

    private AgentMessage.Supplier supplier;

    public ChatBot(AgentConfiguration agentConfiguration) {
        textMessage = new TextMessage();
        carouselRichCardMessage = new CarouselRichCardMessage();
        standaloneRichCardMessage = new StandaloneRichCardMessage();
        agentMessage = new AgentMessage();
        this.agentConfiguration = agentConfiguration;
        fileMessage = new FileMessage();
        sug = new ArrayList<>();
    }

    /**
     *
     * @param agentMessage
     */

    // Make http request to Rcs API Gateway
    private void sendPayLoad(AgentMessage agentMessage){
        agentMessage.setMessage_id(UUID.randomUUID().toString());
        agentConfiguration.post(agentMessage.toString());
    }

    /**
     *
     * @param phone
     * @param message
     * @param suggestions
     */
    // Setting the Agent Message Object
    private void setAgentMessage(String phone, Message message, List<Suggestion> suggestions) {
        agentMessage.setTo(phone);
        agentMessage.setMessage(message);
        agentMessage.setSuggestions(suggestions);
    }

    /**
     *
     * @param phone
     * @param message
     * @param suggestions
     * @param supplier
     */
    private void setAgentMessage(String phone, Message message, List<Suggestion> suggestions, AgentMessage.Supplier supplier) {
        this.setAgentMessage(phone,message, suggestions);
        this.agentMessage.setSupplier(supplier);
    }

    /**
     *
     * @param suggestions
     * @param actions
     * @return
     */
    private List<Suggestion>  setSuggestions(List<Pair<String, String>> suggestions, List<SuggestedAction> actions){
        List<Suggestion> sug = new ArrayList<>();
        if (suggestions != null) {
            for (int i = 0; i < suggestions.size(); ++i) {
                Pair<String, String> pair = suggestions.get(i);
                sug.add(new SuggestedReply(pair.getKey(), new PostBack(pair.getValue())));
            }
        }
        if (actions != null) {
            for (int i = 0; i < actions.size(); ++i) {
                sug.add(actions.get(i));
            }
        }
        this.sug = sug;
        return sug;
    }

    /**
     *
     * Default Width Type will be MEDIUM
     * @param phoneNumber
     * @param richCardContents
     * @return
     */
    public List<RichCardContent> sendCarousel(String phoneNumber, List<RichCardContent> richCardContents){
        carouselRichCardMessage.setContents(richCardContents);
        carouselRichCardMessage.setWidth(WidthType.MEDIUM);
        setAgentMessage(phoneNumber, carouselRichCardMessage, this.sug, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContents;
    }

    /**
     * Custom Width Type settings
     * @param phoneNumber
     * @param richCardContents
     * @param widthType
     * @return
     */
    public List<RichCardContent> sendCarousel(String phoneNumber, List<RichCardContent> richCardContents, WidthType widthType){
        carouselRichCardMessage.setContents(richCardContents);
        carouselRichCardMessage.setWidth(widthType);
        setAgentMessage(phoneNumber, carouselRichCardMessage, this.sug, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContents;
    }


    /**
     *
     * @param phoneNumber
     * @param richCardContent
     * @param orientationType
     * @return
     */
    public RichCardContent sendRichCard(String phoneNumber, RichCardContent richCardContent, OrientationType orientationType, ThumbnailAlignmentType thumbnailAlignmentType){
        standaloneRichCardMessage.setContent(richCardContent);
        standaloneRichCardMessage.setOrientation(orientationType);
        standaloneRichCardMessage.setThumbnail_alignment(thumbnailAlignmentType);
        setAgentMessage(phoneNumber, standaloneRichCardMessage, this.sug, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContent;
    }


    /**
     *
     * @param supplier
     */
    public void setSupplier(AgentMessage.Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     *
     * @param phoneNumber
     * @param text
     * @return
     */
    public  TextMessage sendTextMessage(String phoneNumber, String text){
        this.textMessage.setText(text);
        setAgentMessage(phoneNumber, this.textMessage, this.sug, this.supplier);
        sendPayLoad(agentMessage);
        return this.textMessage;
    }

    /**
     *
     * @param imageUrl
     * @param phoneNumber
     * @return
     */
    public FileMessage sendImage(String imageUrl, String phoneNumber){
        FileInfo fileInfo = new FileInfo("image/png", 12345, "picture.png", imageUrl) ;
        this.fileMessage.setThumbnail(fileInfo);
        this.fileMessage.setFile(fileInfo);
        setAgentMessage(phoneNumber, this.fileMessage, this.sug, this.supplier);
        sendPayLoad(agentMessage);
        return this.fileMessage;
    }

    /**
     *
     * @param phoneNumber
     * @param urlVideo
     * @param videoThumbnail
     * @return
     */
    public FileMessage sendVideo(String phoneNumber, String urlVideo, String videoThumbnail ) {
        FileInfo fileInfo = new FileInfo("video/mp4", 1234, "picture.mp4", urlVideo);
        FileInfo thumbNail = new FileInfo("image/png", 1234, "picture.png", videoThumbnail);
        this.fileMessage.setThumbnail(thumbNail);
        this.fileMessage.setFile(fileInfo);
        setAgentMessage(phoneNumber, this.fileMessage, null);
        sendPayLoad(agentMessage);
        return this.fileMessage;
    }



}
