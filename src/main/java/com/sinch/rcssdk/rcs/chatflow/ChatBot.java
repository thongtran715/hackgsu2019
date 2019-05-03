package com.sinch.rcssdk.rcs.chatflow;

import com.sinch.rcssdk.rcs.exceptions.CarouselsSizeException;
import com.sinch.rcssdk.rcs.exceptions.MissingRichCardContentsException;
import com.sinch.rcssdk.rcs.exceptions.MissingWidthTypeException;
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

    private TextMessage textMessage;

    private CarouselRichCardMessage carouselRichCardMessage;

    private StandaloneRichCardMessage standaloneRichCardMessage;

    private FileMessage fileMessage;

    private AgentMessage agentMessage;

    private AgentConfiguration agentConfiguration;

    private List<Suggestion> suggestions;

    private AgentMessage.Supplier supplier;

    private List<RichCardContent> richCardContents;

    private WidthType widthType;

    public ChatBot(AgentConfiguration agentConfiguration) {
        textMessage = new TextMessage();
        carouselRichCardMessage = new CarouselRichCardMessage();
        standaloneRichCardMessage = new StandaloneRichCardMessage();
        agentMessage = new AgentMessage();
        this.agentConfiguration = agentConfiguration;
        fileMessage = new FileMessage();
        suggestions = new ArrayList<>();
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
        this.suggestions = sug;
        return sug;
    }

    /**
     *
     * Default Width Type will be MEDIUM
     * @param phoneNumber
     * @param richCardContents
     * @return
     */
    public List<RichCardContent> sendCarousel(String phoneNumber, List<RichCardContent> richCardContents) throws CarouselsSizeException {
        int size = richCardContents.size();
        if (size < 2 || size > 10){
            throw new CarouselsSizeException(size);
        }
        carouselRichCardMessage.setContents(richCardContents);
        carouselRichCardMessage.setWidth(WidthType.MEDIUM);
        setAgentMessage(phoneNumber, carouselRichCardMessage, this.suggestions, this.supplier);
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
        setAgentMessage(phoneNumber, carouselRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContents;
    }

    /**
     *
     * @param phoneNumber
     * @return
     * @throws MissingRichCardContentsException
     * @throws MissingWidthTypeException
     */
    public List<RichCardContent> sendCarousel(String phoneNumber) throws MissingRichCardContentsException, MissingWidthTypeException {
        if (this.richCardContents == null){
            throw new MissingRichCardContentsException();
        }
        if (this.widthType == null){
            throw new MissingWidthTypeException();
        }
        this.carouselRichCardMessage.setContents(this.richCardContents);
        this.carouselRichCardMessage.setWidth(this.widthType);
        this.setAgentMessage(phoneNumber, carouselRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return this.richCardContents;
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
        setAgentMessage(phoneNumber, standaloneRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContent;
    }

    /**
     *
     * @param phoneNumber
     * @param text
     * @return
     */
    public  TextMessage sendTextMessage(String phoneNumber, String text){
        this.textMessage.setText(text);
        setAgentMessage(phoneNumber, this.textMessage, this.suggestions, this.supplier);
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
        setAgentMessage(phoneNumber, this.fileMessage, this.suggestions, this.supplier);
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


    /**
     * GETTER
     */
    public TextMessage getTextMessage() {
        return textMessage;
    }

    public CarouselRichCardMessage getCarouselRichCardMessage() {
        return carouselRichCardMessage;
    }

    public StandaloneRichCardMessage getStandaloneRichCardMessage() {
        return standaloneRichCardMessage;
    }

    public FileMessage getFileMessage() {
        return fileMessage;
    }

    public AgentMessage getAgentMessage() {
        return agentMessage;
    }

    public AgentConfiguration getAgentConfiguration() {
        return agentConfiguration;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public AgentMessage.Supplier getSupplier() {
        return supplier;
    }

    public List<RichCardContent> getRichCardContents() {
        return richCardContents;
    }

    public WidthType getWidthType() {
        return widthType;
    }

    /**
     * SETTER
     */
    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public void setCarouselRichCardMessage(CarouselRichCardMessage carouselRichCardMessage) {
        this.carouselRichCardMessage = carouselRichCardMessage;
    }

    public void setStandaloneRichCardMessage(StandaloneRichCardMessage standaloneRichCardMessage) {
        this.standaloneRichCardMessage = standaloneRichCardMessage;
    }

    public void setFileMessage(FileMessage fileMessage) {
        this.fileMessage = fileMessage;
    }

    public void setAgentMessage(AgentMessage agentMessage) {
        this.agentMessage = agentMessage;
    }

    public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
        this.agentConfiguration = agentConfiguration;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public void setRichCardContents(List<RichCardContent> richCardContents) {
        this.richCardContents = richCardContents;
    }

    public void setWidthType(WidthType widthType) {
        this.widthType = widthType;
    }

    public void setSupplier(AgentMessage.Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * PRIVATE METHODS
     */
    private boolean isRichCardContentsValid(List<RichCardContent> richCardContents) {
        int size = richCardContents.size();
        return size < 2 || size > 10;
    }


}
