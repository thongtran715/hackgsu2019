package com.sinch.rcssdk.rcs.chatflow;

import com.sinch.rcssdk.rcs.exceptions.CarouselsSizeException;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
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
import com.sinch.rcssdk.rcs.util.Util;
import javafx.util.Pair;

import java.io.IOException;
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
     * @param agentMessage AgentMessage object
     */

    // Make http request to Rcs API Gateway
    private void sendPayLoad(AgentMessage agentMessage) {
        agentMessage.setMessage_id(UUID.randomUUID().toString());
        agentConfiguration.post(agentMessage.toString());
        System.out.println(agentMessage.toString());
    }

    /**
     * @param phone MSIDN number
     * @param message Message object
     * @param suggestions List of suggestions that can go with particular message. Including Suggested Reply and Actions
     */
    // Setting the Agent Message Object
    private void setAgentMessage(String phone, Message message, List<Suggestion> suggestions) {
        agentMessage.setTo(phone);
        agentMessage.setMessage(message);
        agentMessage.setSuggestions(suggestions);
    }

    /**
     * @param phone MSIDN Number
     * @param message Message Object
     * @param suggestions List of suggestions that can go with particular message. Including Suggested Reply and Actions
     * @param supplier OPTIONAL - User defines which RCS Platform to go to
     */
    private void setAgentMessage(String phone, Message message, List<Suggestion> suggestions, AgentMessage.Supplier supplier) {
        this.setAgentMessage(phone, message, suggestions);
        this.agentMessage.setSupplier(supplier);
    }

    /**
     * @param suggestions List of suggestions that can go with particular message.  Suggested Reply in this case
     * @param actions  List of suggestions that can go with particular message. Suggested Actions
     * @return
     */
    private List<Suggestion> setSuggestions(List<Pair<String, String>> suggestions, List<SuggestedAction> actions) {
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
     * Default Width Type will be MEDIUM
     *
     * @param phoneNumber MSIDN Number
     * @param richCardContents List of rich cards that can go with the carousel model
     * @return
     */
    public List<RichCardContent> sendCarousel(String phoneNumber, List<RichCardContent> richCardContents) throws CarouselsSizeException {
        int size = richCardContents.size();
        if (size < 2 || size > 10) {
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
     *
     * @param phoneNumber MSIDN Number
     * @param richCardContents List of rich cards that can go with the carousel model
     * @param widthType With Type for each of the Rich Card. Including SMALL or MEDIUM
     * @return
     */
    public List<RichCardContent> sendCarousel(String phoneNumber, List<RichCardContent> richCardContents, WidthType widthType) {
        carouselRichCardMessage.setContents(richCardContents);
        carouselRichCardMessage.setWidth(widthType);
        setAgentMessage(phoneNumber, carouselRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContents;
    }

    /**
     * @param phoneNumber
     * @return
     * @throws MissingRichCardContentsException It will throw exception if this can't find the objects of rich card
     * @throws MissingWidthTypeException It will throw exception if this can't find the Width Type identity
     */
    public List<RichCardContent> sendCarousel(String phoneNumber) throws MissingRichCardContentsException, MissingWidthTypeException , Exception{
        if (this.richCardContents == null) {
            throw new MissingRichCardContentsException();
        }

        if (this.widthType == null) {
            throw new MissingWidthTypeException();
        }

        if (!isRichCardsValid(richCardContents)){
            throw new Exception("At least one of the rich card in cards is invalid") ;
        }

        this.carouselRichCardMessage.setContents(this.richCardContents);
        this.carouselRichCardMessage.setWidth(this.widthType);
        this.setAgentMessage(phoneNumber, carouselRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return this.richCardContents;
    }

    /**
     * @param phoneNumber MSIDN Number
     * @param richCardContent Rich Card Content Object
     * @param orientationType The Orientation Type of the Rich Card including Horizontal and Vertical
     * @return
     */
    public RichCardContent sendRichCard(String phoneNumber, RichCardContent richCardContent, OrientationType orientationType, ThumbnailAlignmentType thumbnailAlignmentType) throws Exception{
        if (!isValidRichCardContent(richCardContent)){
            throw new Exception("Rich Card invalid");
        }
        standaloneRichCardMessage.setContent(richCardContent);
        standaloneRichCardMessage.setOrientation(orientationType);
        standaloneRichCardMessage.setThumbnail_alignment(thumbnailAlignmentType);
        setAgentMessage(phoneNumber, standaloneRichCardMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return richCardContent;
    }


    /**
     * @param phoneNumber MSIDN Number
     * @param text Plain Text Message
     * @return
     */
    public TextMessage sendTextMessage(String phoneNumber, String text) {
        this.textMessage.setText(text);
        setAgentMessage(phoneNumber, this.textMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return this.textMessage;
    }

    /**
     * @param imageUrl Image URL that ends with PNG, JPEG, ...
     * @param phoneNumber MSIDN Number
     * @return
     */
    public FileMessage sendImage(String phoneNumber, String imageUrl) throws IOException, FileSizeExceedLimitException {

        // Check if the size is valid.
        long size = Util.getFileSize(imageUrl);
        if (!isFileSizeValidHelper(size, FileInfo.Mime_type.IMAGE_JPEG)) {
            throw new FileSizeExceedLimitException(supplier, size);
        }

        FileInfo fileInfo = new FileInfo(FileInfo.Mime_type.IMAGE_PNG, size, "picture.png", imageUrl);
        this.fileMessage.setThumbnail(fileInfo);
        this.fileMessage.setFile(fileInfo);
        setAgentMessage(phoneNumber, this.fileMessage, this.suggestions, this.supplier);
        sendPayLoad(agentMessage);
        return this.fileMessage;
    }

    /**
     * @param phoneNumber MISDN Number
     * @param urlVideo Video source URL that ends with mp4
     * @param videoThumbnail Image URL that ends with PNG, JPEG, ...
     * @return
     */
    public FileMessage sendVideo(String phoneNumber, String urlVideo, String videoThumbnail) throws IOException, FileSizeExceedLimitException {
        // Check if the size is valid.
        long thumbnailSize = Util.getFileSize(videoThumbnail);
        if (!isFileSizeValidHelper(thumbnailSize, FileInfo.Mime_type.IMAGE_JPEG)) {
            throw new FileSizeExceedLimitException(supplier, thumbnailSize);
        }
        long videoSize = Util.getFileSize(urlVideo);
        if (!isFileSizeValidHelper(videoSize, FileInfo.Mime_type.VIDEO_MP4)) {
            throw new FileSizeExceedLimitException(supplier, videoSize);
        }
        FileInfo fileInfo = new FileInfo(FileInfo.Mime_type.VIDEO_MP4, videoSize, "picture.mp4", urlVideo);
        FileInfo thumbNail = new FileInfo(FileInfo.Mime_type.IMAGE_PNG, thumbnailSize, "picture.png", videoThumbnail);
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

    /**
     * SETTER
     */
    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public CarouselRichCardMessage getCarouselRichCardMessage() {
        return carouselRichCardMessage;
    }

    public void setCarouselRichCardMessage(CarouselRichCardMessage carouselRichCardMessage) {
        this.carouselRichCardMessage = carouselRichCardMessage;
    }

    public StandaloneRichCardMessage getStandaloneRichCardMessage() {
        return standaloneRichCardMessage;
    }

    public void setStandaloneRichCardMessage(StandaloneRichCardMessage standaloneRichCardMessage) {
        this.standaloneRichCardMessage = standaloneRichCardMessage;
    }

    public FileMessage getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(FileMessage fileMessage) {
        this.fileMessage = fileMessage;
    }

    public AgentMessage getAgentMessage() {
        return agentMessage;
    }

    public void setAgentMessage(AgentMessage agentMessage) {
        this.agentMessage = agentMessage;
    }

    public AgentConfiguration getAgentConfiguration() {
        return agentConfiguration;
    }

    public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
        this.agentConfiguration = agentConfiguration;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public AgentMessage.Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(AgentMessage.Supplier supplier) {
        this.supplier = supplier;
    }

    public List<RichCardContent> getRichCardContents() {
        return richCardContents;
    }

    public void setRichCardContents(List<RichCardContent> richCardContents)  {

        this.richCardContents = richCardContents;
    }

    public WidthType getWidthType() {
        return widthType;
    }

    public void setWidthType(WidthType widthType) {
        this.widthType = widthType;
    }

    /**
     * @param richCardContents
     * @return
     */
    private boolean isRichCardsValid(List<RichCardContent> richCardContents) {
        int size = richCardContents.size();
        if( size < 2 || size > 10) {
            return false;
        }
        for (RichCardContent richCardContent : richCardContents) {
            try {
                if(!isValidRichCardContent(richCardContent)){
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                return false;
            }
        }
        return true;
    }

    public boolean isRichCardTitleAndDescriptionValid(RichCardContent richCardContent) {
        String title = richCardContent.getTitle();
        String description = richCardContent.getDescription();
        return title.length() < 200 && description.length() < 2000;
    }

    /**
     * @param size Long type determines how big the size is
     * @param m_type Type of File size ( Video or Image)
     * @return
     */
    public boolean isFileSizeValidHelper(long size, FileInfo.Mime_type m_type) {
        switch (supplier) {
            case MAAP_SAMSUNG:
                if (size > 8e+8) {
                    return false;
                }
            case GOOGLE:
                FileInfo.Mime_type mime_type = m_type;
                if (mime_type == FileInfo.Mime_type.IMAGE_JPEG || mime_type == FileInfo.Mime_type.IMAGE_PNG) {
                    if (size > 1.6e+7) {
                        return false;
                    }
                } else if (mime_type == FileInfo.Mime_type.VIDEO_MP4) {
                    if (size > 8e+7) {
                        return false;
                    }
                }
            default:
                return !(size > 1.6e+7);
        }
    }

    public boolean isValidRichCardContent(RichCardContent richCardContent) throws IOException{
        long size_1 = Util.getFileSize(richCardContent.getMedia().getFile().getFile_uri());
        long size_2 = Util.getFileSize(richCardContent.getMedia().getThumbnail().getFile_uri());
        if (!isFileSizeValidHelper(size_1,richCardContent.getMedia().getFile().getMime() ) || !isRichCardTitleAndDescriptionValid(richCardContent) || !isFileSizeValidHelper(size_2, richCardContent.getMedia().getThumbnail().getMime())){
            return false;
        }
        return true;
    }
}
