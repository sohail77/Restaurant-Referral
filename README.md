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
	        - [Connecting to the firebase](#connecting-to-the-firebase)
            - [Code Sharing](#code-sharing)
            - [Call Function](#call-function)
            - [Location Driven](#location-driven)
    	- [Expected Functionality](#expected-functionality)
    	    - [QR Scanner](#qR-scanner)
    	    - [Customer Tracking](#customer-tracking)
            - [History of coupons for the customers](#history-of-coupons-for-the-customers)
            - [List restaurant data](#list-restaurant-data)
    	- [Bonus Functionality](#bonus-functionality)
    	    - [Upload photos](#upload-photos)
            - [Login and Registration](#login-and-registration)
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


## Requirements

Minimum Requirements:

Android 6.0.
Internet connection.

//for restaurant application.
Camera Access.

Permission Required for extra functionality.

permissions to make a phone call.
permissions to access GPS.
permissions to access device Camera.
permissions to Write to external storage.
permissions to read from external storage.

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
- Location driven  - Using this feature the customer can navigate to the restaurant. (This is done using the Maps API)

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

A diagram and description of the applications primary functions and decomposition.
For example, if you were using the Model-View-Controller pattern, you should identify
what components belong to what part of the pattern and what their purpose is.

## High-level Organization

### Customer Application SiteMap

<img src="/Images/SiteMap.png" alt="drawing" width="700"/>

### Restaurants Application SiteMap

<img src="/Images/SiteMap2.png" alt="drawing" width="700"/>


## Clickstreams

### Restaurant Detail Screen
To navigate to the Restaurant Detail Screen.

<img src="/Images/ClickStream1.png" alt="drawing" width="600"/>

### Coupon Sharing
To navigate to the Coupon Sharing

<img src="/Images/ClickStream2.png" alt="drawing" width="600"/>

### View History
To navigate to the view History

<img src="/Images/ClickStream3.png" alt="drawing" width="600"/>

### Logout Screen
To navigate to the Logout Screen

<img src="/Images/ClickStream4.png" alt="drawing" width="600"/>

### Restaurants Dashboard
To navigate to the Restaurants Dashboard

<img src="/Images/ClickStream5.png" alt="drawing" width="600"/>

A brief description of the common use cases.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Layout
<img src="/Images/SignUpCustomer.png" alt="drawing" width="300"/>
<img src="/Images/LoginScreenCustomer.png" alt="drawing" width="300"/>
<img src="/Images/CustomerHomeScreen.png" alt="drawing" width="300"/>
<img src="/Images/CustomerRestaurantClick.png" alt="drawing" width="300"/>
<img src="/Images/MyCoupons1.png" alt="drawing" width="300"/>
<img src="/Images/MyCoupons2.png" alt="drawing" width="300"/>

Restaurants

<img src="/Images/RegistrationDashboard.png" alt="drawing" width="300"/>
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

### Restaurans Dashboard

This is the page from where the Restaurants could validate the user coupons and Upload menu and other restaurant related images.

<img src="/Images/RestaurantsDashboard.png" alt="drawing" width="200"/>

<img src="/Images/RestaurantsAppCoupon.png" alt="drawing" width="200"/>

Screenshots of all the primary views (screens) and a brief discussion of the
interactions the user performs on the screens.

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