JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath java-cup-11b.jar:
#CUP=$(JAVA) $(CLASSPATH) java_cup.Main <
#CUP=cup 
CUP=java -jar java-cup-11b.jar

all: Main.class

Main.class: absyn/*.java parser.java sym.java Lexer.java Main.java

%.class: %.java
	$(JAVAC) $(CLASSPATH)  $^

Lexer.java: tiny.flex
	$(JFLEX) tiny.flex

parser.java: tiny.cup
	$(CUP) -dump -expect 3 tiny.cup

cup:
	$(CUP) < tiny.cup > out.txt

run:
	java $(CLASSPATH) Main gcd.tiny

play:
	java $(CLASSPATH) Main chris.tiny

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~

test: clean all run

test1:
	java $(CLASSPATH) Main 1.cm

test2:
	java $(CLASSPATH) Main 2.cm

test3:
	java $(CLASSPATH) Main 3.cm
