# TagFinderBot

This is a tutorial to create a telegram bot which can suggest tags/labels in an image using [AWS Rekognition](https://aws.amazon.com/rekognition/). We will be using [TelegramBots Java API](https://github.com/rubenlagus/TelegramBots).


Steps to create the bot -

1. Create new bot and get token from https://t.me/BotFather by clicking on `/newbot` command. Set Description, About, Botpic if you want to. 

2. Create a java maven project

3. Add TelegramBots API dependency.

       <dependency>
           <groupId>org.telegram</groupId>
           <artifactId>telegrambots</artifactId>
           <version>4.1</version>
       </dependency>
       
  and AWS Java SDK Rekognition dependency.
  
       <dependency>
           <groupId>com.amazonaws</groupId>
           <artifactId>aws-java-sdk-rekognition</artifactId>
           <version>1.11.368</version>
       </dependency>
    
_Note: Make sure to use updated dependencies._
       
4. Create a new bot by extending `org.telegram.telegrambots.bots.TelegramLongPollingBot`. Set Bot username in `getBotUsername()` and Bot token in `getBotToken()`. We will recieve updates in `onUpdateReceived()` method.


5. Now create logic to get the picture sent by the user and get `ByteBuffer` from it which can be fed to AWS Rekognition to find tags. Check [TagFinderBot.java](https://github.com/devender-yadav/TagFinderBot/blob/master/src/main/java/com/dev/telegram/tagfinder/TagFinderBot.java#L24) for details.

6. Get ACCESS_KEY and SECRET_KEY from AWS to access AWS Rekognition. Check [AWSRekognition.java](https://github.com/devender-yadav/TagFinderBot/blob/master/src/main/java/com/dev/telegram/tagfinder/AmazonRekognitionUtil.java) for code to get tags/labels from the `ByteBuffer`.


7. Now finally register your bot. Check [TagFinderApp.java](https://github.com/devender-yadav/TagFinderBot/blob/master/src/main/java/com/dev/telegram/tagfinder/TagFinderApp.java) for details.


8. All done. Build the project `mvn clean install`. `tagfinder-0.1-jar-with-dependencies.jar` will be created in target folder.

9. Start your bot 🤖 using

       java -jar tagfinder-0.1-jar-with-dependencies.jar



