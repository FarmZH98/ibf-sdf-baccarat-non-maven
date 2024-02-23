# Find all Java files and write their paths to sources.txt
Get-ChildItem -Path . -Filter *.java -Recurse | ForEach-Object { $_.FullName } | Out-File sources.txt

# Set up the classpath with the JUnit library and the source directory
$env:CLASSPATH = "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;classes"

# Compile the Java source files listed in sources.txt and output the compiled classes to the classes directory
javac @(Get-Content sources.txt) -d bin