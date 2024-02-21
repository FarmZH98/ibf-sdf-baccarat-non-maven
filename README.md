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

