BUILD COMMANDS (needs Symbian Java SDK)

javac -classpath \Series_60_MIDP_SDK_2_1\lib\*.zip -d tmpclasses src\*.java 
preverify -classpath C:\Series_60_MIDP_SDK_2_1\lib\kmidp20.zip;tmpclasses -d classes  tmpclasses 
emulator -classpath C:\Series_60_MIDP_SDK_2_1\lib\kmidp20.zip;classes MJPGViewer 
cd classes
jar -cmf MANIFEST.MF MJPGViewer.jar *.class
cd..
move classes\MJPGViewer.jar bin
