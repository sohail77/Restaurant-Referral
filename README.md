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


Features for Restaurant Application

- Able to validate user QR Code
    Through this feature, restaurants would be able to validate if the user coupon is valid or not. Restaurant application would have an option to scan the QR Code. Application has inbuilt QR scanner which would open on a button click and it would validate the QR Code.
- View the customers that came via the application
    This feature helps the restaurant to track, how many customers came to the restaurant via the application.
- Uploading images of the menu and items
    This feature will allow the restaurants to upload the images of the menu and restaurant using the camera inbuilt with the application

Features for customer application

- Sharing the code to get the discount.
    Allowing the users to share the code just by entering the email address. Once successfully shared, user and the user referred both will get the unique QR code for the restaurants which could be redeemed at the restaurants
- Calling the Restaurants
    This feature will allow the customer to place a call to the restaurants. There is a button provided which place call to the restaurant.
- Navigation to the Restaurant
    This feature will navigate the user to the restaurant, through the maps.

## Libraries

Provide a list of **ALL** the libraries you used for your project. This should include
sources of any clip art, icons, etc. Example:

**ZXing:** ZXing is a Java library that is used for barcode image processing. It has support for 2D Barcodes, 1D industrial and  1D product(https://github.com/zxing/zxing)

## Requirements

List hardware or system requirements (e.g., Android 5.0, GPS access) needed to run your software.


## Installation Notes

You need to install 2 apk files for this project.
One is restaurants application apk and second is customer application apk.


## Final Project Status

We have finished all our tasks mentioned in our project contract and project proposal. We have also polished and tested our application. Implementation of all the minimum, expected and bonus functionality have been done.

If the project would have been continued, our next step would have been payment integration. A user wallet would be created where user can add money in the wallet and once coupon is verified payment will be automatically deleted from the wallet. 


### Minimum Functionality

- Connecting to the firebase (Completed) - connecting the application to the backend firebase database and display some data from the database on the phone.
- Code Sharing (Completed) - QR codes will be generated based on user ID, which the user can share directly from the application, just by entering the email address.
- Call function (Completed) - User will be able to make a call to the restaurant using the application.
- Location driven (Completed) - Using this feature the customer can navigate to the restaurant. (This is done using the Maps API)

### Expected Functionality

- QR Scanner (Completed) – The restaurant will be able to scan the QR Code and validate it directly from the application.
- Customer Tracking (Completed) - This feature will allow the restaurant to keep track of the number of customers arrived.
- History of coupons for the customers (Completed) – This feature will display all the previous coupon codes.
- List restaurant data (Completed) – This feature will display the list of restaurants to the user.

### Bonus Functionality
- Upload photos (Completed) – This feature will allow the restaurants to upload the photos using the camera.
- Login and Registration (Completed) – This will allow the customer to login and register in the customer application and restaurants to login in the restaurant application.


## Code Examples

You will encounter roadblocks and problems while developing your project.
Share 2-3 'problems' that your team encountered.
Write a few sentences that describe your solution and provide a code snippet/block
that shows your solution. Example:

**Problem 1: We needed to detect shake events**

A short description.
```
// The method we implemented that solved our problem
public void onSensorChanged(SensorEvent event) {
    now = event.timestamp;
    x = event.values[0];
    y = event.values[1];
    z = event.values[2];

    if (now - lastUpdate > 10) {
        force = Math.abs(x + y + z - lastX - lastY - lastZ);
        if (force > threshold) {
            listener.onShake(force);
        }
        lastX = x;
        lastY = y;
        lastZ = z;
        lastUpdate = now;
    }
}

// Source: StackOverflow [3]
```

## Functional Decomposition

A diagram and description of the applications primary functions and decomposition.
For example, if you were using the Model-View-Controller pattern, you should identify
what components belong to what part of the pattern and what their purpose is.

## High-level Organization

The hierarchy or site map of the application.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Clickstreams

A brief description of the common use cases.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Layout

Wire-frames of all the primary views and a brief description describing what each is for.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Prototypes

If you did low-fidelity or high-fidelity prototypes, document the process here,
including the results of your user testing. (Otherwise, delete this section.)

## Implementation

Screenshots of all the primary views (screens) and a brief discussion of the
interactions the user performs on the screens.

## Future Work

A discussion of how the implementation can be extended or improved if you had more
time and inclination to do so.


## Sources

Use IEEE citation style. Some examples:

[1] J. Moule, _Killer UX Design: Create User Experiences to Wow Your Visitors_. Sitepoint, 2012.

[2] _Ngspice_. (2011). [Online]. Available: http://ngspice.sourceforge.net

[3] "Detect shaking of device in left or right direction in android?", StackOverflow.
    https://stackoverflow.com/a/6225656 (accessed July 12, 2019).

What to include in your project sources:
- Stock images
- Design guides
- Programming tutorials
- Research material
- Android libraries
- Everything listed on the Dalhousie [*Plagiarism and Cheating*](https://www.dal.ca/dept/university_secretariat/academic-integrity/plagiarism-cheating.html)
- Remember AC/DC *Always Cite / Don't Cheat* (see Lecture 0 for more info)