JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath java-cup-11b.jar:
#CUP=$(JAVA) $(CLASSPATH) java_cup.Main <
CUP=java -jar java-cup-11b.jar

#java -jar java-cup-11b.jar tiny.cup

all: Main.class

Main.class: absyn/*.java parser.java sym.java Lexer.java Main.java

%.class: %.java
	$(JAVAC) $(CLASSPATH)  $^

Lexer.java: tiny.flex
	$(JFLEX) tiny.flex

parser.java: tiny.cup
	$(CUP) -dump -expect 3 tiny.cup

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
