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

play:
	java $(CLASSPATH) Main chris.tiny

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
	rm -f *.abs
	rm -f *.sym
test: clean all run

testr:
	java $(CLASSPATH) Main rTest.cm -a

test1:
	java $(CLASSPATH) Main 1.cm -s

test2:
	java $(CLASSPATH) Main 2.cm -a

test3:
	java $(CLASSPATH) Main 3.cm -a

test4:
	java $(CLASSPATH) Main 4.cm -a

test5:
	java $(CLASSPATH) Main 5.cm -a

test6:
	java $(CLASSPATH) Main gcd.cm -a	
