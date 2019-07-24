# Table of contents

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Referred](#referred)
	- [Group members](#group-members)
	- [Description](#description)
	- [Users](#users)
	- [Features](#features)
		- [Features for Restaurant Application](#features-for-restaurant-application)
		- [Features for Customer Application](#features-for-customer-application)
	- [Libraries](#libraries)
	- [Requirements](#requirements)
	- [Installation Notes](#installation-notes)
	- [Final Project Status](#final-project-status)
	    - [Minimum Functionality](#minimum-functionality)
    	- [Expected Functionality](#expected-functionality)
    	- [Bonus Functionality](#bonus-functionality)
    - [Code Examples](#code-examples)
    - [Functional Decomposition](#functional-decomposition)
    - [High-level Organization](#high-level-organization)
	- [Clickstreams](#clickstreams)
		- [Restaurant Detail Screen](#restaurant-detail-screen)
		- [Coupon Sharing](#coupon-sharing)
		- [View History](#view-history)
		- [Logout Screen](#logout-screen)
		- [Restaurants Dashboard](#restaurants-dashboard)
	- [Layout](#layout)
	- [Implementation](#implementation)
	- [Future Work](#future-work)
	- [Sources](#sources)

<!-- /TOC -->

# Referred

The idea behind developing the mobile application is to provide users discounts on the bills when they visit the restaurant, based on the referral system. The more you promote a restaurant the more discount you get. The promotion includes sharing the code with your friends and relatives and when they visit the restaurant you and the person you referred to, both get some discount. In this way, the restaurant gets more customers and the user gets their discounts.

## Group members

| Name                   | Banner ID  | Email               |
| ---------------------- | ---------- | ------------------- |
| Shah Nihir             | B00824490  | nh244735@dal.ca     |
| Mohammed Sohail Ahmed  | B0082154   | sh488044@dal.ca     |

## Description

Due to the increasing trends of people eating in the restaurants and the competition between the restaurants are increasing, we decided to make an application that helps both the restaurants and the customers. This application will help restaurants to gain customers and customers to gain discounts on the dinning. We have developed two applications, one for the restaurants and other for the customers.

The users would be able to see the list of registered restaurants of a specific region. The users would be having the option to refer the restaurant to the other user using the emailId of the other user. When the user refers, user and referred user both get the code in the form of QR Code. When the user shows the QR Code to the restaurant and if valid, discounts will be applied to the user's bill amount.

Hence this application will benefit both the restaurant and user, as it would decrease the advertisement cost for the restaurant and also decrease the cost of dining out for the customer . There will be different signup and register process for both the customers and the restaurant. Also, the restaurant would be able to figure out how many customers came in via the app.


### Users

The target user base of the application is the restaurants and the users who visit the restaurants. There is no age barrier for using this application. The application is easily scalable to use in every region.

### Features


#### Features for Restaurant Application

- Able to validate user QR Code - Through this feature, restaurants would be able to validate if the user coupon is valid or not. Restaurant application would have an option to scan the QR Code. Application has inbuilt QR scanner which would open on a button click and it would validate the QR Code.
- View the customers that came via the application - This feature helps the restaurant to track, how many customers came to the restaurant via the application.
- Uploading images of the menu and items - This feature will allow the restaurants to upload the images of the menu and restaurant using the camera inbuilt with the application

#### Features for Customer application

- Sharing the code to get the discount - Allowing the users to share the code just by entering the email address. Once successfully shared, user and the user referred both will get the unique QR code for the restaurants which could be redeemed at the restaurants
- Calling the Restaurants - This feature will allow the customer to place a call to the restaurants. There is a button provided which place call to the restaurant.
- Navigation to the Restaurant - This feature will navigate the user to the restaurant, through the maps.

## Libraries

**ZXing:** ZXing is a Java library that is used for barcode image processing. It has support for 2D Barcodes, 1D industrial and  1D product(https://github.com/zxing/zxing)

**Glide:** Glide is an efficient and fast image loading library for Android. It also provides support for GIF and handles image caching.(https://github.com/bumptech/glide)

**android-material-design-icon-generator-plugin:** Material Icon generator is a plugin which helps in using all the material design icons straight from the Android Studio. We decided to use this plugin because it saves a good amount of time while development.


## Requirements

**Minimum Requirements**
  - Android 6.0.
  - Internet connection.
  - Camera Access (for restaurant application).

**Permission Required for extra functionality.**
  - permissions to make a phone call.
  - permissions to access GPS.
  - permissions to access device Camera.
  - permissions to Write to external storage.
  - permissions to read from external storage.

## Installation Notes

You need to install 2 apk files for this project.
One is restaurants application apk and second is customer application apk.


## Final Project Status

We have finished all our tasks mentioned in our project contract and project proposal. We have also polished and tested our application.
Implementation of all the minimum, expected and bonus functionality have been done.
If the project would have been continued, our next step would have been payment integration.
A user wallet would be created where user can add money in the wallet and once coupon is verified payment will be automatically deleted from the wallet.


### Minimum Functionality

- Connecting to the firebase - connecting the application to the backend firebase database and display some data from the database on the phone.
- Code Sharing - QR codes will be generated based on user ID, which the user can share directly from the application, just by entering the email address.
- Call function  - User will be able to make a call to the restaurant using the application.
- Location driven  - Using this feature the customer can start the navigation to the restaurant.

### Expected Functionality

- QR Scanner (Completed) – The restaurant will be able to scan the QR Code and validate it directly from the application.
- Customer Tracking (Completed) - This feature will allow the restaurant to keep track of the number of customers arrived.
- History of coupons for the customers (Completed) – This feature will display all the previous coupon codes.
- List restaurant data (Completed) – This feature will display the list of restaurants to the user.

### Bonus Functionality
- Upload photos (Completed) – This feature will allow the restaurants to upload the photos using the camera.
- Login and Registration (Completed) – This will allow the customer to login and register in the customer application and restaurants to login in the restaurant application.


## Code Examples

**Problem 1: When we were trying to make operations on our data from outside the onComplete we were finding it to be null.** [3]

```java
db.collection("Restaurants")
.get()
.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
	@Override
	public void onComplete(@NonNull Task<QuerySnapshot> task) {
		if (task.isSuccessful()) {
			for (QueryDocumentSnapshot document : task.getResult()) {
	//do nothing
	String data= task.getResult()
			}
		} else {
			Log.e("Not hello", "Error getting documents.", task.getException());
		}
	}
});
```
From our research we  found out that firebase functions are fully asynchronous. This means that the call always returns immediately, without blocking the code to wait for a result. The results come some time later, whenever they’re ready.So to solve this issue we had to make use of that data in the onComplete method

```java
db.collection("Restaurants")
.get()
.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
	@Override
	public void onComplete(@NonNull Task<QuerySnapshot> task) {
		if (task.isSuccessful()) {
			for (QueryDocumentSnapshot document : task.getResult()) {
	//do nothing
	String data= task.getResult()
	//do something with “data”
			}
		} else {
			Log.e("Not hello", "Error getting documents.", task.getException());
		}
	}
});

```

**Problem 2: While loading this by using the below code, we were getting a memoryOutOfBound Exception as the size of the image was too large.** [2]
```java
image.setDrawable(R.drawable.cover_img);
```

As a solution we used the Glide library that does bitmap decoding for large images and also image caching for faster loading

```java
Glide.with(context).load(R.drawable.cover_img).into(image);
```

## Functional Decomposition

For the implementation of our project we decided to use the Model-View-Controller architectural pattern. The folder structure of it can be seen below.

<img src="/Images/structure.png" alt="drawing" width="300"/>


**Model-View-Controller(MVC)**

MVC divides the application into three parts this helps in code re-usability and parallel development. The three components are:

    **Model**

    The model component represents the data-related logic of the application. This component can contain information about the data that is transferred between the View and the Controller or it can also contain some business logic.

    **Models in our project**

    In our project we have placed all the model classes in a subdirectory named "Models" so that they are easily understandable. Our model classes represents the data that we are fetching from the website. For example, all the information that we require from the restaurant is represented in the "RestaurantModel" class. Another example will be all the information that a coupon will contain is represented using a "CouponModel" class.   

    **View**

    This component contains all the UI component with which the user can interact.

    **Views in our project**

    In our project we have different XML layout files for each ui component.
    Some examples of these UI files are: Restaurant list item on the home-screen, layout of components (buttons, text, images etc) on different screens, etc. All the views are placed in the "layout" subdirectory which is under the "res" folder (This is the default location for all the views in android).

    **Controller**

    This component is responsible for processing of data. Controller takes the user inputs from the UI and processes the business logic and finally shows the result to the user.

    **Controllers in our project**

    In our project we have placed all the controller files inside the parent directory. We have used Activity classes as our controller to process the data and show the output using the View and the Model classes.

    Our controller classes are: MainActivity, RestaurantDetailActivity, LoginActivity, SignUpActivity, CouponsActivity, CouponHistoryActivity.

  **Adapters and Fragments**

  Apart from our MVC architecture we have segregated other logic into Adapters and Fragments. Adapters are responsible for attaching data to our custom Recycler Views. Fragments are self-contained portions of UI that reside inside an Activity. They also have their own lifecycle. We have segregated Fragments into a separate folder, as it improves the understandability and readability of the project.


## High-level Organization

### Customer Application SiteMap

<img src="/Images/SiteMap.png" alt="drawing" width="700"/>

### Restaurants Application SiteMap

<img src="/Images/SiteMap2.png" alt="drawing" width="700"/>


## Clickstreams

### Restaurant Detail Screen
To navigate to the "Restaurant Details Screen" user need to login into the application. After login user will see a list of restaurants, on clicking it the user will be navigated to the particular restaurant details.

<img src="/Images/ClickStream1.png" alt="drawing" width="600"/>

### Coupon Sharing
To navigate to the "Coupons Sharing"  the user will be provided with a share button on the "Restaurant Detail Screen". On clicking of it, a dialog will open in which user can enter mail id of the referred user.

<img src="/Images/ClickStream2.png" alt="drawing" width="600"/>

### View History
To view the coupons user had or the coupons user used, the user needs to login into the application. Once logged in, on the home screen user will see coupon button on the top right. On clicking it, the user can view the coupons he/she had. On the top right there is a "History"  button on clicking it will display user used coupons.

<img src="/Images/ClickStream3.png" alt="drawing" width="600"/>

### Logout Screen
To logout from the application, the user can find the logout button on the home screen on the top right. On clicking it the user will be log out of the application

<img src="/Images/ClickStream4.png" alt="drawing" width="600"/>

### Restaurants Dashboard
This is for the restaurant application. To scan the barcode or upload the images restaurants need to login into the application. There will be two buttons provided "Scan" to scan the QR code of the customer and "Upload Image" to upload images related to the restaurant.

<img src="/Images/ClickStream5.png" alt="drawing" width="600"/>

A brief description of the common use cases.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Layout
This is the registration screen for the customers/users. User will enter their email id, name, and password.

<img src="/Images/SignUpCustomer.png" alt="drawing" width="300"/>

This is the login screen for the customers/users. User will enter their email id and password to login into the application.

<img src="/Images/LoginScreenCustomer.png" alt="drawing" width="300"/>

This is the home screen for the customers/users. This will have restaurants list along with the option to navigate to "MyCoupons" and to "Logout" from the application.

<img src="/Images/CustomerHomeScreen.png" alt="drawing" width="300"/>

This is the page for particular restaurant detail. This page will display details about the restaurants, their menus, option to place call to the restaurant and navigate to the restaurant.

<img src="/Images/CustomerRestaurantClick.png" alt="drawing" width="300"/>

This is the "MyCoupons" section if the user would not have any coupon then "You don't have any coupons" text will be displayed

<img src="/Images/MyCoupons1.png" alt="drawing" width="300"/>

This is the "MyCoupons" section, where the user coupons will be displayed. More than one coupons are displayed which can be viewed by scrolling on the screen.

<img src="/Images/MyCoupons2.png" alt="drawing" width="300"/>

**Restaurants Application**

This is the Dashboard for the restaurant's application. This page will provide restaurants option to scan and upload images.

<img src="/Images/RegistrationDashboard.png" alt="drawing" width="300"/>

This is the login screen for the Restaurant application.

<img src="/Images/RestaurantsLogin.png" alt="drawing" width="300"/>


## Implementation

### Customers Sign Up

This is the page from where users can login into the application

<img src="/Images/CustomersSignUp.png" alt="drawing" width="200"/>

### Customers Login

This is the page from customers can login into the application

<img src="/Images/CustomersLogin.png" alt="drawing" width="200"/>

### Customer Dashboard

This is the Dashboard for the Customer.

<img src="/Images/CustomerDashboard.png" alt="drawing" width="200"/>

### Restaurant Details

This is the page where all the Restaurants Details are displayed. Apart from that there is option to
navigate to the Restaurant Details Screen

<img src="/Images/RestaurantDetails.png" alt="drawing" width="200"/>

### Call and Navigation Feature

This page allows the user to make call to the restaurants.
This page also allows the user to Navigate to the restaurants.

<img src="/Images/CallNavigationFeature.png" alt="drawing" width="200"/>

### Coupons Sharing

This page allows the user to share the coupons to the other user.

<img src="/Images/Coupons_share.jpg" alt="drawing" width="200"/>

### MyCoupons

This is the page from where user can view the coupons, the user has.

<img src="/Images/MyCoupons.jpg" alt="drawing" width="200"/>

### History

This is the page where user can view the used coupons.

<img src="/Images/History.jpg" alt="drawing" width="200"/>

### Restaurants Dashboard

This is the Login screen for the Restaurant Management Application

<img src="/Images/ResLogin.png" alt="drawing" width="200"/>

This is the page from where the Restaurants could validate the user coupons and Upload menu and other restaurant related images.

<img src="/Images/RestaurantsDashboard.png" alt="drawing" width="200"/>

This screen shows all the coupons that have previously been used by the customers.

<img src="/Images/RestaurantsAppCoupon.png" alt="drawing" width="200"/>


## Future Work

A discussion of how the implementation can be extended or improved if you had more
time and inclination to do so.


## Sources

[1]"zxing/zxing", GitHub, 2019. [Online]. Available: https://github.com/zxing/zxing. [Accessed: 22- Jul- 2019].

[2]"Glide v4 : Fast and efficient image loading for Android", Bumptech.github.io, 2019. [Online]. Available: https://bumptech.github.io/glide/. [Accessed: 23- Jul- 2019].

[3]"Why are the Firebase APIs asynchronous?", Medium, 2019. [Online]. Available: https://medium.com/google-developers/why-are-firebase-apis-asynchronous-callbacks-promises-tasks-e037a6654a93. [Accessed: 23- Jul- 2019].

[4]"Dani | HD photo by Pablo Merchán Montes (@pablomerchanm) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/Orz90t6o0e4. [Accessed: 23- Jul- 2019].

[5]"Korean Food Bibimbap with Kimchi | HD photo by Jakub Kapusnak (@foodiesfeed) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/4f4YZfDMLeU. [Accessed: 23- Jul- 2019].

[6]"We found this coffee place in Gaia, r... | HD photo by Petr Sevcovic (@sevcovic23) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/qE1jxYXiwOA. [Accessed: 23- Jul- 2019].

[7]"Avocado and Egg Toast | HD photo by Joseph Gonzalez (@miracletwentyone) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/fdlZBWIP0aM. [Accessed: 23- Jul- 2019].

[8]"Build your own hot dog | HD photo by Andersen Jensen (@andersenjensen) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/Hk2eu3OODdg. [Accessed: 23- Jul- 2019].v4

[9]"Blackboard, bar stool, furniture and chair | HD photo by Nathan Dumlao (@nate_dumlao) on Unsplash", Unsplash.com, 2019. [Online]. Available: https://unsplash.com/photos/ulPd2UCwZYk. [Accessed: 23- Jul- 2019].

[10]A. Frisk, "Why Canadians are choosing restaurants over the kitchen", Global News, 2019. [Online]. Available: https://globalnews.ca/news/3465075/canadians-eating-out-over-cooking-at-home/. [Accessed: 24- Jul- 2019].  

[11]N. Maunsell, "Canadians will spend more in restaurants in 2018: Canada's Food Price Report", Dalhousie News, 2019. [Online]. Available: https://www.dal.ca/news/2017/12/13/canadians-will-spend-more-in-restaurants-in-2018--canada-s-food-.html. [Accessed: 24- Jul- 2019].

[12]"konifar/android-material-design-icon-generator-plugin", GitHub, 2019. [Online]. Available: https://github.com/konifar/android-material-design-icon-generator-plugin. [Accessed: 24- Jul- 2019].
