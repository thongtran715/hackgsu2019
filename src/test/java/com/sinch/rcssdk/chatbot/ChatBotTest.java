package com.sinch.rcssdk.chatbot;
import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.exceptions.FileSizeExceedLimitException;
import com.sinch.rcssdk.rcs.exceptions.MissingRichCardContentsException;
import com.sinch.rcssdk.rcs.exceptions.MissingWidthTypeException;
import com.sinch.rcssdk.rcs.message.component.richcard.FileInfo;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardContent;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardMedia;
import com.sinch.rcssdk.rcs.message.enums.MessageType;
import com.sinch.rcssdk.rcs.message.enums.WidthType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;
import com.sinch.rcssdk.rcs.message.messagetype.FileMessage;
import com.sinch.rcssdk.rcs.message.messagetype.TextMessage;
import com.sinch.rcssdk.rcs.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
public class ChatBotTest {
    private ChatBot chatBot;

    @Before
    public void setUp() throws Exception {
        chatBot = new ChatBot(new AgentConfig(RCSConfigureType.api));
        List<RichCardContent> richCardContents = new ArrayList<>();
        RichCardContent richCardContent = new RichCardContent();
        RichCardMedia richCardMedia = new RichCardMedia();
        chatBot.setRichCardContents(richCardContents);
    }
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCarouselExceptionMissingWidth()   {
        try {
            chatBot.sendCarousel("+12332112");
            fail();
        }
        catch (MissingWidthTypeException e){
            assertEquals(e.getMessage(), "Missing Width Type Exception");
            System.out.println("Throw error 1");
        }catch (MissingRichCardContentsException e){
            assertEquals(e.getMessage(), "Missing Rich Card Contents");
        }catch (Exception e){
            assertEquals(e.getMessage(), "At least one of the rich card in cards is invalid");
            fail();
        }
    }

    @Test
    public void testCarouselExceptionRichCardsInValid(){
        try {
            chatBot.setWidthType(WidthType.MEDIUM);
            chatBot.sendCarousel("+12332112");
            fail();
        }
        catch (MissingWidthTypeException e){
            assertEquals(e.getMessage(), "Missing Width Type Exception");
        }catch (MissingRichCardContentsException e){
            assertEquals(e.getMessage(), "Missing Rich Card Contents");
        }catch (Exception e){
            assertEquals(e.getMessage(), "At least one of the rich card in cards is invalid");
        }
    }

    @Test
    public void testFileSizeLimit() {

        chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
        boolean isValid = chatBot.isFileSizeValidHelper(800000000, FileInfo.Mime_type.VIDEO_MP4);
        assertFalse(isValid);
        isValid = chatBot.isFileSizeValidHelper(1000, FileInfo.Mime_type.VIDEO_MP4);
        assertTrue(isValid);
        chatBot.setSupplier(AgentMessage.Supplier.GOOGLE);
        isValid = chatBot.isFileSizeValidHelper(1332321, FileInfo.Mime_type.IMAGE_JPEG);
        assertTrue(isValid);
        isValid = chatBot.isFileSizeValidHelper(1232133123, FileInfo.Mime_type.IMAGE_JPEG);
        assertFalse(isValid);
        isValid = chatBot.isFileSizeValidHelper(123213312, FileInfo.Mime_type.VIDEO_MP4);
        assertFalse(isValid);
        isValid = chatBot.isFileSizeValidHelper(1321, FileInfo.Mime_type.IMAGE_JPEG);
        assertTrue(isValid);
    }

    @Test
    public void testSendTextMessage() {
        TextMessage textMessage = chatBot.sendTextMessage("+14047691562", "Hello from Sinch");
        assertEquals(textMessage.getText(), "Hello from Sinch");
        assertEquals(textMessage.getType(), MessageType.text);
    }

    @Test
    public void testSendImageMessage() {
        try {
            FileMessage fileMessage = chatBot.sendImage( "+14047691562", "https://s3.amazonaws.com/sketchers-chatbot/Revision_1/Kid/Kids+Boys'+Sport/Boys'+Kinectors+Thermovolt.jpg");
            assertEquals(fileMessage.getThumbnail().getFile_uri(), "https://s3.amazonaws.com/sketchers-chatbot/Revision_1/Kid/Kids+Boys'+Sport/Boys'+Kinectors+Thermovolt.jpg" );
        }catch (FileSizeExceedLimitException e){
            // File size limit the recommendation
            System.out.println(e.getMessage());
        }catch (IOException e){
            // Unable to reach the url host
            System.out.println(e.getMessage());
        }
    }


    @Test
    @Deprecated
    public void testSuggestionsChipSet(){
        try {
            String str = "12321321321332132132132132132132132321321321312312321312313131313";
            List<Pair<String, String>> sug = new ArrayList<>();
            sug.add(new Pair<>(str, "something"));
            chatBot.setSuggestedReply(sug) ;
        }catch (IOException e){
            assertEquals(e.getMessage(), "Title exceed maximum length. It has to be 25 characters");
        }
    }


}
