
# Usage: make clean, make compile, make run, make 

# Variables
SRC_DIR = src
MAIN_CLASS = Main

# Default target - clean, compile, and run
all: clean compile run

# Clean all .class files
clean:
	@echo Cleaning compiled files...
	@cd $(SRC_DIR) && del /S /Q *.class 2>nul || echo No class files to clean

# Compile all Java files
compile:
	@echo Compiling Java files...
	@cd $(SRC_DIR) && javac $(MAIN_CLASS).java

# Run the program
run:
	@echo Running the game...
	@cd $(SRC_DIR) && java $(MAIN_CLASS)

# Individual targets
.PHONY: all clean compile run