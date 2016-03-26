# Inapp
An activity class with in-app setup , purchase and consume methods in android.

Before starting in-app coding ; some steps have to be followed:

 * download "Google Play Billing Library" from Android SDK Manager
 * create a new directory and name it as "aidl"
 * create a package inside it "com.android.vending.billing"
 * copy and paste IInAppBillingService.aidl  (from <sdk path>/extras/google/play_billing) to it.
 * add in-app billing permission to manifest file - <uses-permission android:name="com.android.vending.BILLING" />
 * add other util classes from  <sdk>/extras/google/play_billing/samples/TrivialDrive/src/com/example/android/trivialdrivesample/util to inapp project 