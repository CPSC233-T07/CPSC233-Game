#Running Project

#In master branch, in project folder, in package src, run GameApp.java

#To run, 
#import project file
#In order to run, FXGL 11.8-uber.jar
#Which can be dwonloaded with this link 
https://github-production-release-asset-2e65be.s3.amazonaws.com/32761091/ddbf2100-612b-11ea-8ad0-7f7aeac3c5c3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20200408%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20200408T172405Z&X-Amz-Expires=300&X-Amz-Signature=a405c8227eefafec1c9a075bdad007997e07fd66342a5cba79a073af430923eb&X-Amz-SignedHeaders=host&actor_id=60771184&response-content-disposition=attachment%3B%20filename%3Dfxgl-11.8-uber.jar&response-content-type=application%2Foctet-stream

#As well as javafx-sdk-11.0.2
#which can be downloaded with this link

#Windows
https://download2.gluonhq.com/openjfx/11.0.2/openjfx-11.0.2_windows-x64_bin-sdk.zip

#Linux
https://download2.gluonhq.com/openjfx/11.0.2/openjfx-11.0.2_linux-x64_bin-sdk.zip

#Create 2 User Libraries in eclipse
#Window -> Preferences -> Java -> Build Path -> User Libraries
#Hit New... and name one JavaFx and one FXGL
#For each library hit Add External JARS...
#For JavaFx select all the .jar files in openjfx-11.0.2\lib
#For FXGL select the .jar file downloaded with the link above

#Next step is, right click on the project folder, hit Build Path-> Add Libraries... -> User Library and select the two recently created Libraries

#Final step, right click on the GameApp.java, mouse over Run as... and hit Run Configuration...
#Hit Arguments and under VM arguments insert
#--module-path {The directory to the JavaFX lib folder} --add-modules=javafx.controls,javafx.graphics,javafx.base
#Run GameApp.java 


#Controls

#W to move forward

#A to move left

#S to move backward

#D to move right

#Esc to end

