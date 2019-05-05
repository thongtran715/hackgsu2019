# java-SinchRCS-Library

# Motivation 

This Java Sinch RCS library is a tool where you can directly interact with Sinch RCS API. It is built on top of core RCS API and enables developers the ability to understand core features of rich messaging that normal SMS is not capable of. We hope with this library , you can find the easy ways to use our service. 

---

# Link
Please visit our RCS Page for further information [Sinch-RCS](https://www.sinch.com/docs/rcs/index.html)

---

# Usage
In order to use this library, you will first need to set up your RCS bot environment. You will need to obtain Agent ID and Token ID from Sinch, Inc, so please visit [Sinch](https://www.sinch.com/) and we can help you set up the bot. We will also ask for your [callback URL](https://www.sinch.com/docs/rcs/http-rest.html#callback-request) 
<br />
Otherwise, feel free to use our sample bot which is already integrating in the library. 

---
## Configuration 
You will need to tell our library which bot you are using including the Agent ID and Token ID. <br />
Let's create a configuration class which is called YourBotConfiguration and extends it from ChatAgentConfiguration
```$xslt
public class YourBotConfiguration extends AgentConfiguration {

    public YourBotConfiguration(RCSConfigureType type) {
        super(type);
        this.TOKEN = "YOUR TOKEN";
        this.AGENT_ID = "YOUR ID";
    }
}
```
<br/>
Next step is to create an instance from ChatBot class. <br/>

```
      ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
      // Optional - Setting up your destinated supplier. 
      chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
```

That is it for the configuration. Now let's jump to how we can use this ChatBot instance to send RCS features message. 
# Core Features 
- [ Text Message ](#textMessage)
- [ Image Message ](#imageMessage)
- [ Video Message ](#videoMessage)
- [ Standalone Rich Card Message](#standaloneMessage)
- [ Carousels Message](#carouselsMessage)
- [ Suggestion Chip Sets](#suggestionChipSets)
- [ Agent Events](#agentEvents)
---

<a name="textMessage"></a>
## Text Message

Sending text message is simple. You will just need to have a phone number and the text you want to send 

```$xslt
  chatBot.sendTextMessage("+14047691562", "Hello from Sinch");
```


<a name="imageMessage"></a>
## Image Message

Sending image message requires an URI to host the file and it has mime type of png or jpeg

```$xslt
        try {
            chatBot.sendImage( "+14047691562", "https://s3.amazonaws.com/sketchers-chatbot/Revision_1/Kid/Kids+Boys'+Sport/Boys'+Kinectors+Thermovolt.jpg");
        }catch (FileSizeExceedLimitException e){
            // File size limit the recommendation
            System.out.println(e.getMessage());
        }catch (IOException e){
            // Unable to reach the url host
            System.out.println(e.getMessage());
        }
```

<a name="videoMessage"></a>
## Video Message

<a name="standaloneMessage"></a>
## Standalone Message

<a name="carouselsMessage"></a>
## Carousel Message

<a name="suggestionChipSets"></a>
## Suggestion Chip Sets
Suggestion chip sets enable the ability for end-user interact with the chat bot, and you can have up to 11 units chip attach to your message. Message must not be null.
<br/>
Suggestion chip sets contain display text and post-back data <br/>
- Display Text: Text that is shown in the suggested reply and sent back to the agent when the user taps it. Maximum 25 characters.
- Post-back data: Optional data that will be sent back to the agent when the user taps the reply. Usually, the post-back data will be sent to the callback url of the Agent.
                
### Suggested Reply 
Suggested Reply is used in case where the bot is trying to ask for the custom response from the customer. 
<br/>
Suggested Reply has the type of reply, display text, and post-back data. <br/>
In this library, we use Pair<String, String> with the key is display text and value is post_back data.

```$xslt
            ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
            chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
            
            // Making a list of suggested reply 
            List<Pair<String, String>> suggestedReply = new ArrayList<>();
            
            // Declaring a key as display text, value as post-back data
            suggestedReply.add(new Pair<>("Hi Sich", "jgkaioejn21kl222131223kakdk223123213"));
            
            try{
                chatBot.setSuggestedReply(suggestedReply);
                chatBot.sendTextMessage("+14047691562", "Hello from Sinch");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
```
IO Exception will be thrown if the number of chip sets exceed 11 units or the length of display text exceed 25 characters. 

### Suggested Actions

Suggested Actions has the type of action, display text , post back and also it enables one of the following feature in the native device:

- [DialPhoneNumber](#dialPhoneNumber)
- [ShowLocation](#showLocation)
- [RequestLocationPush](#requestLocationPush)
- [OpenURL](#openURL)
- [CreateCalendarEvent](#createCalendarEvent)

<a name="dialPhoneNumber"></a>
#### Dial Phone Number 
```$xslt
            // List of actions
            List<SuggestedAction>  actions = new ArrayList<>();
            
            // Set display text as well as post-back data. 
            // Depend on use cases, but null post-back would make a better sense when we are trying to call
            SuggestedAction callUsAction = new SuggestedAction("Call Us Now", null);
            
            // Set the Dial Phone Number
            DialPhoneNumber dialPhoneNumber = new DialPhoneNumber("+14047691562");
            callUsAction.setAction(dialPhoneNumber);
            
            // Add the dial phone action to the list 
            actions.add(callUsAction);
            
            try {
                chatBot.setSuggestedActions(actions);
                chatBot.sendTextMessage("+14047691562", "Please call Sinch");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
```
<a name="showLocation"></a>
#### Show Location
    
Show location instance contains the following attributes <br />
- Latitude : It ranges between [-90, 90]
- Longitude : It ranges between [-180, 180]
- Label : Optional label to be shown on the map at the given lat/long. Max length is 1000 characters

```$xslt
            // List of actions
            List<SuggestedAction>  actions = new ArrayList<>();

            // Set display text as well as post-back data.
            // Depend on use cases, but null post-back would make a better sense when we are trying to visit the location
            SuggestedAction showLocationAction = new SuggestedAction("Visit Sinch", null);

            // Set up the URL instance
            ShowLocation showLocation = new ShowLocation(45.4f, 88.9f, "Sinch's HeadQuarter");
            showLocationAction.setAction(showLocation);

            // Add the action to the list.
            actions.add(showLocationAction);

            try{
                chatBot.setSuggestedActions(actions);
                chatBot.sendTextMessage("+14047691562", "Do you want to see what Sinch look like?");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
```

<a name="requestLocationPush"></a>
#### Request Location Push

With Request location push, we are asking users to share their location. Once, they share the location, you can see their latitude and longitude in the callback url in which Sinch has forwarded it.

```$xslt
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
```
<a name="openURL"></a>
#### Open URL

```$xslt
            // List of actions
            List<SuggestedAction>  actions = new ArrayList<>();

            // Set display text as well as post-back data.
            // Depend on use cases, but null post-back would make a better sense when we are trying to visit the URL 
            SuggestedAction openUrlAction = new SuggestedAction("Visit Sinch", null);
            
            // Set up the URL instance
            OpenUrl openUrl = new OpenUrl("https://www.sinch.com/docs/rcs/http-rest.html#openurl");
            openUrlAction.setAction(openUrl);
            
            // Add the action to the list. 
            actions.add(openUrlAction);

            try{
                chatBot.setSuggestedActions(actions);
                chatBot.sendTextMessage("+14047691562", "Do you want to see what Sinch look like?");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
```

<a name="createCalendarEvent"></a>
#### Create Calendar Event 

---

<a name="agentEvents"></a>
## Agent Events 

Agent Event has the following feature 

- [Agent Composing Event](#agentComposing)
- [Agent Read Event](#agentRead)

<a name="agentComposing"></a>
### Agent Composing Event

<a name="agentRead"></a>
### Agent Read Event  

---

# Contributors 
