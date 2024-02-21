compile:
```
javac --source-path src -d bin src/sg/edu/nus/iss/baccarat/server/*
javac --source-path src -d bin src/sg/edu/nus/iss/baccarat/client/*
```

run:
```
java -cp bin sg.edu.nus.iss.baccarat.server.ServerApp 12345 4
java -cp bin sg.edu.nus.iss.baccarat.client.ClientApp localhost:12345
```

compile jar file in bin folder:
```
jar -c -v -f Baccarat.jar .
```

Note:
- Unable to run the jar files using command below. 
```
java -cp bin/Baccarat.jar sg.edu.nus.iss.baccarat.server.ServerApp 12345 2
```

Error: LinkageError occurred while loading main class sg.edu.nus.iss.baccarat.server.ServerApp
        java.lang.UnsupportedClassVersionError: Preview features are not enabled for sg/edu/nus/iss/baccarat/server/ServerApp (class file version 65.65535). Try running with '--enable-preview'


To compile and run test class at root level:
```
javac -cp lib/junit-4.13.2.jar;src -d bin src/sg/edu/nus/iss/baccarat/AppTest.java
java -cp bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore sg.edu.nus.iss.baccarat.AppTest
```

Note:
1. The rules for 3rd card drawing is referred to the MBS rulebook instead of the following instructions... 
2. The 6-Card Rule was not followed due to developer being unfamiliar with the rule.
3. Compile and run test packages/class using mvn package. The manual way can be used if maven is not in used.


User stories/bug left:
1. Some sanity check. Like must login before placing bet. Must bet before being able to play etc, and deal appropirately.
2. recorded issue of the code will start to write into 2nd row of csv file directly and skip 1st row.
3. recorded issue of vs code detect lambda expression error but can compile in line 106 in BaccaratEngine.java - .filter(ch -> ch == ',') 
4. Due to userDB and betAmount being static, even though we uses threads, we cannot support different users accessing the server. To run more, need to spawn runnable from this class instead of from serverapp. Also, we should put the betamount into userdb
5. There is an issue of 2 clients being able to connect to the server at the same time due to no restrictions imposed. This would cause complications to the server because the code does not support it.
