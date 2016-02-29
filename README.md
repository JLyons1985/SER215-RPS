Rock, Paper, Scissors game for SER 215
========

1. Overview:
----
Rock, Paper Scissors is a simple game created by Joshua Lyons & Nergal Givarkes. The game was developed as a group project for SER 215 at ASU.

Below are the steps to get the game up and running. o host your own networking server for the multiplayer portion see the networking section below:

2. Prerequisites:
----
To run the game you need java installed. To compile from source you need JDK 1.7

3. Run from JAR:
----
* Run the .jar located in /dist/SER215-RPS.jar
* Enter a username in the lower right when the client boots up.
* You can now start a single player game or a networked game by using the buttons in the lower left.

4. Compile from Source:
----
* Load up your IDE of choice.
* Make sure you are compiling with jdk 1.7
* Add the two libs located in /libs/ to your library references for the project.
* Compile and run the RPSClientApplication

6. Hosting a Server:
----
* Find your public IP address
* In RPSClientApplication.java change the variables "masterServerIp" and "gameServerIp" to the IP address found above
* Make sure you open up ports 9000-9100 and point them to the computer you are hosting the servers on
* Build the program and and make sure the jar created and any additional files created and placed in the /dist/ folder
* Open command line or terminal and enter the dist folder
* To start the master server run the following "java -cp SER215-RPS.jar com.ser215.rps.RPSMasterServer"
* You will see the server start printing information
* Next run the SER215-RPS.jar file normally.

7. Acknowledgements
----
The game and source code were built by [Joshua Lyons](https://github.com/JLyons1985), [Nergal Givarkes](https://github.com/nergal1986), and uploaded to GitHub February 2016


