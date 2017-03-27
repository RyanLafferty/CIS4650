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
	java $(CLASSPATH) Main gcd.tiny -s

run2:
	java $(CLASSPATH) Main gcd.tiny -c 

play:
	java $(CLASSPATH) Main chris.tiny

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
	rm -f *.abs
	rm -f *.sym
	rm -f *.tm
	rm -f *.ast

test: clean all run2

testr:
	java $(CLASSPATH) Main rTest.cm -a

test1:
	java $(CLASSPATH) Main 1.cm -s

test2:
	java $(CLASSPATH) Main 2.cm -s

test3:
	java $(CLASSPATH) Main 3.cm -s

test4:
	java $(CLASSPATH) Main 4.cm -s

test5:
	java $(CLASSPATH) Main 5.cm -s

test6:
	java $(CLASSPATH) Main gcd.cm -s	

test11:
	java $(CLASSPATH) Main 11.cm -s	
