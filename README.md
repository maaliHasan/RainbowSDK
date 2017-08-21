ALE Rainbow SDK for Android
===========================
---

Welcome to the Alcatel-Lucent Enterprise **Rainbow Software Development Kit for Android**!

The Alcatel-Lucent Enterprise (ALE) Rainbow Software Development Kit (SDK) is an npm package for connecting your Android application to Rainbow.


## Preamble
---
Its powerfull APIs enable you to create the best Android applications that connect to Alcatel-Lucent Enterprise [Rainbow](https://www.openrainbow.com/).

This documentation will help you to use it.


## Rainbow developper account
---

Your need a Rainbow **developer** account in order to use the Rainbow SDK for Node.js.

Please contact the Rainbow [support](mailto:support@openrainbow.com) team if you need one.



## Install
---

1. Download the framework SDK zip file or just clone the repository  from [Rainbow-CPaaS githup repo](https://github.com/Rainbow-CPaaS/Rainbow-Android-SDK)
2. Unzip and see next steps for configrautions  and setup the enviroment.




# Configuration
---------
## Set up the project
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


#### Step 1 : Add the following permissions in your **AndroidManifest.xml**
```java
	<uses-permission android:name="android.permission.READ_PROFILE" />
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
```
### Step 2: Add an Application class

**NB: If you already have a class which extends Application, you can skip this step.**  
Create a java class in your project which extends Application (for example MyApp.java).  
Go to **AndroidManifest.xml** and add the reference:
```java
    <application
        android:name=".MyApp"
        [...]
    </application>
```

### Step 3 : Initialize the Rainbow SDK in your project

In the **onCreate()** of your application, you have to set a notificationBuilder with different parameters and call the initialize method.



    @Override
    public void onCreate() {
        super.onCreate();

		RainbowSdk.instance().setNotificationBuilder(getApplicationContext(),
													YourActivity.class,
													the_icon_app_id, // You can set it to 0 if you have no app icon
													getString(R.string.app_name),
													"Connect to the app",
													Color.RED);
		RainbowSdk.instance().initialize(); // Will change in the future
    }




## Usage
Connect to Rainbow
==

---------
You need to have a valid Rainbow account to do that.
To connect to Rainbow, you have to:

* start the rainbow service
* and then use the sign in method when the service is ready.

**NB: The start service method is temporary and will probably be hidden later.**
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
You can also call the *signin* method with the host you want to connect on (example: "sandbox.openrainbow.com"): 
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
	});
```
**Note**: Do not forget to start the service before.  
**Note**: If you don't fill the host, the last will be used. If it is the first time (you don't have a last), the default value is the production server ("openrainbow.com").
That's all! Your application should be connected to Rainbow, congratulation!

## Events
---

### Listen to events

Once you have connected To Rainbow and start  the rainbow service  you will begin receiving events from the SDK.
Here is an example for listening when the SDK is ready to be used (once the connection is successfull to Rainbow):

```java
...
  @Override
  public void onSigninSucceeded() {
      // You are now connected
      // Do something on the thread UI
        Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
        }


```


### List of events

Here is a list of the events that you can subscribe on:


| Name | Description |
|------|------------|
| **SigninResponseListener** | Fired when you try to connect to Rainbow and do signin |
| **IContactListener** | Fired   when contact has been updated |
| **IRainbowConversationsListener** | FThis event is fired when the list of conversations has changed. |
| **IRainbowGetConversationListener** | Fired when the conversation is being called|
| **IRainbowImListener** | Fired when a new message is received for a given conversation. |
| **IRainbowInvitationManagementListener** | Fired when invitation beibg accept or decline |
| **IRainbowSentInvitationListener** | Fired when invitation is being sent |
| **IContactSearchListener** | Fired when you search for contact |
| **IRainbowContactManagementListener** | Fired when you a contact is being  removed  from  roster list |

## Contacts
---
You can take a look to [contact files ](https://github.com/Rainbow-CPaaS/Rainbow-Android-SDK/tree/master/src/main/java/com/ale/infra/contact) provided in SDK src files  to find all what you need to know about Rainbow Contact object . For example : contact name , Ids , email address ,  phone numbers and more

### Retrieve the list of contacts

 Rainbow Android  SDK allow you to retrieve the list of contacts from the server. the returned list represent youy Roster onec (**Contact who already have their rainbow account and you accept them as friends** ).  You can access to them by using the following API:

```java
  RainbowSdk.instance().contacts().getRainbowContacts()
```
 **But**  you have to listen to the contacts list changes. See more in [ArrayItemList](https://github.com/Rainbow-CPaaS/Rainbow-Android-SDK/blob/master/docs/tutorials/ArrayItemList.md) You can do it by creating an IItemListChangeListener in the class which is listening and then register as follows .
 ```java
   
public class ContactsFragment extends Fragment  {
    public static final String TAG = ContactsFragment.class.getSimpleName();
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
Note: This is the fixed list of contacts of the connected user.


### Retrieve a contact information

Accessing individually an existing contact can be done using the API `getContactFromJabberId()`, `getContactFromCorporateId()`

```java

    ...
    // Retrieve the roster contact  full information.
   RainbowSdk.instance().contacts().getContactFromCorporateId(Contact corporateId);

```

Regarding the method `getUserDataFromId()`, if the contact is not found in the list of contacts, a request is sent to the server to retrieve it (limited set of information depending privacy rules).

### Searching for a contact by name .
If you want to search for a Rainbow contacts **who already have their  Rainbow account** inorder to add them to your network , use this API 
```java
RainbowSdk.instance().contacts().searchByName(String name, final IContactSearchListener listener);
```
 then You have to create **IContactSearchListener**  in the class which is listening and then register as follows .
 ```java
   public class MyFragment extends Fragment  {
    public static final String TAG = MyFragment.class.getSimpleName();
        private IContactSearchListener m_searchListener = new IContactSearchListener() {

        @Override
        public void searchStarted() {
        Log.d(TAG, "searchStarted: ");
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
        Log.d(TAG, "searchError: ");

        }
    };
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         RainbowSdk.instance().contacts().searchByName('Name', m_searchListener);
        }
    }
  ```
### Add a contact to a Roster list .
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

### Remove a contact From contacts list
To remove a contact from your network , it is the  same mecanism: use this api 
```java
 RainbowSdk.instance().contacts().removeContactFromRoster(String contactJid, String contactEmail, IRainbowContactManagementListener listener);
```
and don't fogret to craete  **IRainbowContactManagementListener** as follows :
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
**Note**: be carful about the  contact id used , in **removeContactFromRoster** use Contact **jabber Id** which you can get it using `getContactId()` Rainbow method . However , in **AddRainbowContactToRoster** use contact **Corporate Id** using   `getCorporateId()` method

###  Managing contacts updates.
You need to detect when the data of a contact are modified,or when a property of an object in the list are changed .So you have to listen to contact updates by creating a **ContactListener**  as follows .
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
And then register to that listner .
```java
	public class MyFragmentWhichIsListeningToContactUpdates implement Contact.ContactListener {
    	private IRainbowContact my_contact;
    
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle                  savedInstanceState) {
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
 contact presence is  one of the contact property which you can listen to it when its change as mentioned in **Managing contacts updates** , When the presence of a contact changes, the following event is fired:
```java
...
 public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {
          // here you can Listen to contact presence change
        }
```

The presence and status of a Rainbow user can take several values as described in the following table:

| Presence | Status | Meaning |
|----------------|--------------|---------|
| **ONLINE** | **online**| The contact is connected to Rainbow through a desktop application and is available |
| **ONLINE** | **mobile_online** | The contact is connected to Rainbow through a mobile application and is available |
| **AWAY** | **away**| The contact is connected to Rainbow but hasn't have any activity for several minutes |
| **DND** |**DoNotDisturb** | The contact is connected to Rainbow and doesn't want to be disturbed at this time |
| **DND_PRESENTATION** | **presentation** | The contact is connected to Rainbow and uses an application in full screen (presentation mode) |
| **BUSY_AUDIO** | **audio** | The contact is connected to Rainbow and currently engaged in an audio call (WebRTC) |
| **BUSY_VIDEO** | **video** | The contact is connected to Rainbow and currently engaged in a video call (WebRTC) |
| **DND_PRESENTATION** | **sharing** | The contact is connected to Rainbow and currently engaged in a screen sharing presentation (WebRTC) |
| **OFFLINE** | | The contact is not connected to Rainbow |
| **UNSUBSCRIBED** | | The presence of the Rainbow user is not known (not shared with the connected user) |

Notice: With this SDK version, if the contact uses several devices at the same time, only the latest presence information is taken into account.


## Presence

### Change presence manually

The Rainbow SDK for Android allows to change the presence of the connected user with list of potential choices  .But first lets get the connected user by calling the following api:

```java
RainbowSDK.instance().myProfile().getConnectedUser();

```
To set Presence manually call this API
``` JAVA 
RainbowSDK.instance().myProfile().setPresenceTo(RainbowPresence presence);
```
The following Rainbow Presence  values  are accepted:
| Presence constant | value | Meaning |
|------------------ | ----- | ------- |
| **ONLINE** | "online" | The connected user is seen as **available** |
| **DND** | "dnd" | The connected user is seen as **do not disturb** |
| **AWAY** | "away" | The connected user is seen as **away** |
| **OFFLINE** | "invisible" | The connected user is connected but **seen as offline** |

Notice: Values other than the ones listed will not be taken into account.





## Features provided
---

Here is the list of features supported by the Rainbow-Android-SDK


### Instant Messaging

 - 

 - 



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


