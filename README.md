ALE Rainbow SDK for Android
===========================
---

Welcome to the Alcatel-Lucent Enterprise **Rainbow Software Development Kit for Android**!


## Preamble
---
The Alcatel-Lucent Enterprise (ALE) Rainbow Software Development Kit (SDK) is an Android library based on Java for connecting your Android native application to Rainbow.

Its powerfull APIs enable you to create the best Android applications that connect to Alcatel-Lucent Enterprise [Rainbow](https://www.openrainbow.com/).

This documentation will help you to use it.


## Rainbow developer account
---

You need a Rainbow **developer** account in order to use the Rainbow SDK for Android

Please contact the Rainbow [support](mailto:support@openrainbow.com) team if you need one.


### Prerequisites
---

#### Android
---

The Rainbow SDK for Android supports older versions of Android up to Android Jellybean.

The minimal version supported are:

| Pre-requisites | Version supported | API Level |
|:-------------- |---------------- | ----------- |
| Android | >= 4.1 | 16 |


#### Android Studio
---

A stable version of Android Studio is needed to develop with the Rainbow SDK for Android.

The following OS are supported:

| Operating System | Version supported |
| ---------------- | ----------------- |
| Windows | Starting Windows 7 |
| MacOS | Starting OS X 10.11 |





## Set up the project
---------

### Step 1 : Set minimal version of Android SDK

RainbowSDK is using the Android SDK **version 16** so you must set it to the minimal in your app.  
With gradle:

```java
android {
    [...]
    defaultConfig {
        [...]
        minSdkVersion 16
    }
}
```

### Step 2 : Add the gradle dependency to the Rainbow SDK for Android
In your **app\build.gradle**, add these lines:

```java
    allprojects {
        repositories {
            [...]
            maven { url "https://jitpack.io" }
        }
    }

    [...]

    dependencies {
        [...]
        compile 'com.github.Rainbow-CPaaS:Rainbow-Android-SDK:1.29.0'
    }
        
```
And **Sync Now**.



# Configure your Android project
---------


#### Step 1 : Configure permissions
Add the following permissions in your **AndroidManifest.xml**

```java

<uses-permission android:name="android.permission.READ_PROFILE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

```

### Step 2: Add an Application class

**NB:**  If you already have a class which extends Application, you can skip this step. 

Create a java class in your project which extends Application (for example MyApp.java).  
Go to **AndroidManifest.xml** and add the reference:

```java
    <application
        android:name=".MyApp"
        [...]
    </application>
```

### Step 3 : Initialize the Rainbow SDK in your project

In the **onCreate()** of your application, you have to set a notificationBuilder with different parameters and call the initialize method.There are two possible cases for that .

##### 1) connecting to the official Rainbow environment

The initializing method needs two parameters: the **application ID** and the **secret key** of your application.

These information allow to identify the application you are developping. For more information, see [Injecting key and secret](https://api.openrainbow.org/sdk/web/tutorial-10-Injecting%20key%20and%20secret.html) which explains what is the purpose, how to create an application and how to get the application ID and the secret key. 



```java
 @Override
 public void onCreate() {
     super.onCreate();
    RainbowSdk.instance().setNotificationBuilder(getApplicationContext(),
                                                YourActivity.class,
                                                the_icon_app_id,         // You can set it to 0 if you have no app icon
                                                getString(R.string.app_name),
                                               "Connect to the app",
                                                Color.RED);
    
    RainbowSdk.instance().initialize("applicationId", "secretKey"); 
}

```

##### 2) You want to connect on the Developer Sandboxed Platform

However, when you are developping, you may want to connect on the Developer Sandboxed Platform. In that case, you don't need to provide an application ID and the secret key. Just call the initializing method with no parameter as following:


```java
@Override
    public void onCreate() {
        super.onCreate();
        RainbowSdk.instance().setNotificationBuilder(getApplicationContext(),
                                                    YourActivity.class,
                                                    the_icon_app_id,      // You can set it to 0 if you have no app icon
                                                    getString(R.string.app_name),
                                                   "Connect to the app",
                                                    Color.RED);
        RainbowSdk.instance().initialize();   
    }
   

```

That's all! Your Android application is ready for connecting to Rainbow!



## Connecting to Rainbow
---------

In order to connect to Rainbow you  need to have a valid Rainbow account , simply click [here](https://www.openrainbow.com/) if you don't have one .

After getting a valid account you have to start the rainbow service first then signin , if you want to connect to the official Rainbow environment use the following `signin` method

```java

RainbowSdk.instance().connection().start(new StartResponseListener() {
       @Override
        public void onStartSucceeded() {
            RainbowSdk.instance().connection().signin("@email", "password", new SigninResponseListener() {
                @Override
                public void onSigninSucceeded() {
                    // You are now connected
                    // Do something on the thread UI
                }
                @Override
                public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                    // Do something on the thread UI
                }
            });
        }
        @Override
        public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String err) {
            // Do something
        } 
});

```

**But** if you have another host you want to connect on (example: "sandbox.openrainbow.com") use this `signin` method

```java
RainbowSdk.instance().connection().signin("@email", "password", "host", new SigninResponseListener() {
    @Override
        public void onSigninSucceeded() {
            // You are now connected
            // Do something on the thread UI
        }
        @Override
        public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
            // Do something on the thread UI
        }
 ```

That's all! Your application should be connected to Rainbow, congratulation!


## Contacts
---

###  Retrieve the list of contacts

 Rainbow Android  SDK allow you to retrieve the list of contacts from the server. the returned list represents your Roster onec (**Contact who already have their rainbow account and you accept them as friends** ).
 You can access to them by using the following API:


```java
  RainbowSdk.instance().contacts().getRainbowContacts()
```

 **But**  you have to listen to the contacts list changes. See more in [ArrayItemList](https://github.com/Rainbow-CPaaS/Rainbow-Android-SDK/blob/master/docs/tutorials/ArrayItemList.md) , You can do that by creating an IItemListChangeListener in the class which is listening and then register as follows .
 
 ```java
 public class ContactsFragment extends Fragment {
      
    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Do something on the thread UI ,For example :here Im updating my Contcat Adpter 
                    mContactAD.notifyDataSetChanged();
                }
            });

            ArrayItemList arrayItemList = instance().contacts().getRainbowContacts();
        }

    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instance().contacts().getRainbowContacts().registerChangeListener(m_changeListener);
    }

    @Override
    public void onDestroyView() {
        instance().contacts().getRainbowContacts().unregisterChangeListener(m_changeListener);
        super.onDestroyView();
    }

}
 ```


###  Retrieve a contact information
---

Accessing individually an existing contact can be done using the API `getContactFromJabberId()` or `getContactFromCorporateId()`

```java

    ...
    // Retrieve the roster contact  full information.
   RainbowSdk.instance().contacts().getContactFromCorporateId(Contact corporateId);

```

Regarding the method `getUserDataFromId()`, if the contact is not found in the list of contacts, a request is sent to the server to retrieve it (limited set of information depending privacy rules).

###  Searching for a contact by name .
---

If you want to search for a Rainbow contacts **who already have their  Rainbow account** inorder to add them to your network , use this API 
```java
RainbowSdk.instance().contacts().searchByName(String name, final IContactSearchListener listener);
```
 then You have to create **IContactSearchListener**  in the class which is listening and then register as follows .
 ```java
   public class MyFragment extends Fragment  {
        private IContactSearchListener m_searchListener = new IContactSearchListener() {

        @Override
        public void searchStarted() {
        }

        @Override
        public void searchFinished(List<DirectoryContact> list) {
            //here you can get list with the serach Contcat by name result

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 // here you can update the UI thread
                }
            });
        }

        @Override
        public void searchError() {
        }
    };
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         RainbowSdk.instance().contacts().searchByName('Name', m_searchListener);
        }
    }
    
  ```
  
###  Add a contact to a Roster list .
---

In order to invite a contact to your Rainbow network you  can use this API .

```java
 RainbowSdk.instance().contacts().addRainbowContactToRoster(final String contactCorporateId, final String contactEmail, final IRainbowSentInvitationListener listener);
``` 

you need to register to **IRainbowSentInvitationListener** to manage the callbacks after you send the invitaion .For example, prevent adding  contact who is already in your roster list ,if the invitation has already been sent in the last 3600 seconds, or invitation success ,...etc . 

```java
 private IRainbowSentInvitationListener addContactListener = new IRainbowSentInvitationListener() {
        @Override
        public void onInvitationSentSuccess(String s) {
            Log.d(TAG, "onInvitationSentSuccess: " + s);
        }

        @Override
        public void onInvitationSentError(RainbowServiceException e) {
            Log.d(TAG, "onInvitationSentError: " + e.getDetailsMessage());
         
            });

        }

        @Override
        public void onInvitationError() {
            Log.d(TAG, "onInvitationError: ");

        }
    };
```

###  Remove a contact From contacts list
---

To remove a contact from your network , it is the  same mecanism: use this API 

```java
 RainbowSdk.instance().contacts().removeContactFromRoster(String contactJid, String contactEmail, IRainbowContactManagementListener listener);
```

And don't fogret to create  **IRainbowContactManagementListener** as follows :

```java
 private IRainbowContactManagementListener mRemoveContactListener = new IRainbowContactManagementListener() {
        @Override
        public void OnContactRemoveSuccess(String s) {
            Log.d(TAG, "OnContactRemoveSuccess: " + s);

        }

        @Override
        public void onContactRemovedError(Exception e) {
            Log.d(TAG, "onContactRemovedError: " + e.toString());

        }
    };
```

**Note**: be careful about the  contact id used , in **removeContactFromRoster** use Contact **jabber Id** which you can get it using `getContactId()` Rainbow method . However , in **AddRainbowContactToRoster** use contact **Corporate Id** using   `getCorporateId()` method


## Presence
---

### Change presence manually

The Rainbow SDK for Android allows to change the presence of the connected user with list of potential choices  .But first lets get the connected user by calling the following api:

```java

RainbowSDK.instance().myProfile().getConnectedUser();

```
To set Presence manually call this API

```java 

RainbowSDK.instance().myProfile().setPresenceTo(RainbowPresence presence);

```
The following Rainbow Presence  values  are accepted:


| Presence constant | value | Meaning |
|------------------ | ----- | ------- |
| **`ONLINE`** | online | The connected user is seen as **available** |
| **`DND`** | dnd | The connected user is seen as **do not disturb** |
| **`AWAY`** | away | The connected user is seen as **away** |
| **`OFFLINE`** | invisible | The connected user is connected but **seen as offline** |


###  Managing contacts updates.
---

You need to detect when the data of a contact are modified, or when a property of an object in the list are changed .So you have to listen to contact updates by creating a **ContactListener**  as follows .

```java
private Contact.ContactListener m_contactListener= new Contact.ContactListener(){
        @Override
        public void contactUpdated(Contact contact) {
         //here you can detect the updated contact , Do something on the thread UI
        
        }

        @Override
        public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {
          // here you can Listen to contact presence change
        }

        @Override
        public void onActionInProgress(boolean b) {
        // Do something on the thread UI

        }
    };
```

And then register to that listener .

```java
    public class MyFragmentWhichIsListeningToContactUpdates implement Contact.ContactListener {
        private IRainbowContact my_contact;
    
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             my_contact.registerChangeListener(this);
             return view;
         }
         @Override
         public void onDestroyView() {
              my_contact.unregisterChangeListener(this);
             super.onDestroyView();
         }
    }
    
```

###  Listen to contact presence change
---

 Contact presence is  one of the contact property which you can listen to it when its change as mentioned in **Managing contacts updates** . When the presence of a contact changes, the following event is fired:
 
```java
...
public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {
        // here you can Listen to contact presence change
    }
 
```

In the following table , you can find a several values for user presence :

| Presence | Status | Meaning |
|----------------|--------------|---------|
| **`ONLINE`** | online| The contact is connected to Rainbow through a desktop application and is available |
| **`ONLINE`** | mobile_online | The contact is connected to Rainbow through a mobile application and is available |
| **`AWAY`** | away| The contact is connected to Rainbow but hasn't have any activity for several minutes |
| **`DND`** |DoNotDisturb| The contact is connected to Rainbow and doesn't want to be disturbed at this time |
| **`DND_PRESENTATION`** | presentation | The contact is connected to Rainbow and uses an application in full screen |
| **`BUSY_AUDIO`** | audio | The contact is connected to Rainbow and currently engaged in an audio call (WebRTC) |
| **`BUSY_VIDEO`** | video | The contact is connected to Rainbow and currently engaged in a video call (WebRTC) |
| **`DND_PRESENTATION`** | sharing |The contact is connected to Rainbow and currently engaged in a screen sharing presentation(WebRTC) |
| **`OFFLINE`** | | The contact is not connected to Rainbow |
| **`UNSUBSCRIBED`** | | The presence of the Rainbow user is not known (not shared with the connected user) |

Notice: Values other than the ones listed will not be taken into account.



## Instant Messaging 
---------
### Retrieve conversations

To retrieve all active conversations (peer to peer conversations and bubles), you can call the method getAllConversations() but you have to **listen to the conversations list changes**.
See more in **[ArrayItemList.md](https://github.com/Rainbow-CPaaS/Rainbow-Android-SDK/blob/master/docs/tutorials/ArrayItemList.md)**.  
You can do it by creating an IItemListChangeListener in the class which is listening and then register.

```java
   public class MyFragmentWhichIsListeningConversations extends Fragment {
        private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
            @Override
            public void dataChanged() {
                // Do something on the thread UI
                ArrayItemList<IRainbowConversation> conversations = RainbowSdk.instance().conversations().getAllConversations();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            RainbowSdk.instance().conversations().getAllConversations().registerChangeListener(m_changeListener);
            return view;
        }

        @Override
        public void onDestroyView() {
            RainbowSdk.instance().conversations().getAllConversations().unregisterChangeListener(m_changeListener);
            super.onDestroyView();
        }
    }
    
```

To differenciate the two types of conversation, you can use the **isRoomType** method of IRainbowConversation.

```java
    for(IRainbowConversation conversation : conversations.getCopyOfDatalist()) {
        if (conversation.isRoomType()) {
            // The conversation is a bubble / room
        }
        else {
            // The conversation is a peer to peer conversation
        }
    }
    
  ``` 
  
  ### Send a message to a conversation 
  
  To send a message to a conversation, you just have to call the API  `sendMessageToConversation()` with the **IRainbowConversation**.     The GUI is updated by the **IItemListChangeListener** above.
  
  ```java
    RainbowSdk.instance().im().sendMessageToConversation(m_conversation, "your message text);
  ```
  
  ###  Listening to incoming messages
  
 Since you store the conversation you got from `getAllConversation()` method in ArrayItemList you will be able to listen to any       conversation update which include listening for incoming messages .
 So when a new message is received, the  **IItemListChangeListener**  detect that and update the conversation automatically. 
When receiving a  message, the SDK for Android automatically send a receipt of type received to your recipient.
folowing is a table with of recipent types 

| Receipt  constant | value | Meaning |
|------------------ | ----- | ------- |
| **`SENT`** | 0 |server not received message yet(E.g .  you have no connection)|
| **`SENT_SERVER_RECEIVED`** | 1 | client not connected|
| **`SENT_CLIENT_RECEIVED`** | 2 | client connected, just received it|
| **`SENT_CLIENT_READ`** | 3 | client read the message |
| **`RECEIVED`** | 4 | When receiving a message, SDK for Android sends it automatically  |
| **`READ`** | 5 |sent when client call `markMessagesFromConversationAsRead()`   |



  ### Marking a message as read
  
To manually mark a message as read , you need to send a receipt of type read when the message received has been read.
this is can done by  calling  the API `markMessagesFromConversationAsRead()` with the IRainbowConversation as method parameter

 ```java
ainbowSdk.instance().im().markMessagesFromConversationAsRead(m_conversation);

 ```
 

### List of events
---
Here is the list of list of the events that you can subscribe on  supported  by the Rainbow-Android-SDK

| Name | Description |
|------|------------|
| **`SigninResponseListener`** | Fired when you try to connect to Rainbow and do signin |
| **`IContactListener`** | Fired  when contact has been updated |
| **`IRainbowInvitationManagementListener`** | Fired when invitation is being accepted or declined |
| **`IRainbowSentInvitationListener`** | Fired when invitation is being sent |
| **`IContactSearchListener`** | Fired when you search for contact |
| **`IRainbowContactManagementListener`** | Fired when you a contact is being  removed  from  roster list |
| **`IRainbowImListener`** | Fired when a new message is received for a given conversation. |
| **`IRainbowConversationsListener`** | fired when the list of conversations has changed. |
| **`IRainbowGetConversationListener`** | Fired when the conversation is being called|



## Features provided
---

Here is the list of features supported by the Rainbow-Android-SDK


### Instant Messaging

 -  Retrieving or creating a conversation from a contact.
 -  Sending a new message .
 -  Listening for a incoming message.
 -  Marking a message as received or read .

### Contacts

- Get the list of contacts
- Get contact individually
- Managing contacts updates
- Displaying contact full information
- Searching for a contact by name
- Adding the contact to the user network
- Removing the contact from the user network

### Presence

- Get the presence of contacts
- Set the user connected presence

### Conversation

-  Retrieving a conversation from a contact .
- Send a message to a conversation . 
- Marking a message as read .
