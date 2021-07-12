# Profile
Face is an android application developed in Kotlin. This application uses Deepface framework available in python to Analyze and Verify images of a person. 
This application also uses FastAPI framework available in python to write server side code. 
This application creates Base64 string of image and sends it to server over the network.
Application uses Retrofit Library which is avaiable in Kotlin to create a restful API. 
Using the Retrofit the application sends the image to the server. It sends the image (Base64 encoded) through HTTP POST request. 
FastAPI receives the request, analyse or verify the image and then sends the response data bask to Retrofit in JSON format. 
Once the FastAPI receives the request, it creates the image from Base64 string and names it as the IP Address of client in order to prevent image name conflict.
Once the result is computed from the images, the images are automatically deleted from the server to minimize the usage of disk space.
