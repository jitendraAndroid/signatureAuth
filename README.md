# signatureAuth
Signature Authentication /Thumb Authentication

An application that helps for identifying the two signatures or thumb imprints are matching.

Usage : This application can be used in places where signature or thumb prints on paper are used for authentication, although for thumb imprints we have a biometric mechanism for identification, this application can be used for authenticating old thumb prints on hardcopies.

Logic : Basically the application is build on the idea of image matching. There is an open source library called OpenCV which is used for image comparison, face detection and other process related to images. We used the same library with Brisk description extractor for matching key points and deciding whether to authenticate the image as matching or mismatch.

Upcoming changes :  Will be upgrading the application for getting user input for matching percentage required and also will be trying some other description extractor, for more fine results.

