# Description
The MicroRefact tool contains 2 parts:

- javaParser: a JavaParser based static analysis tool to analyze the AST of the source code of the Java monolith and extract the class metadata. (name, methods, dependencies, annotationsm, etc).
- the Python app is the core of the approach and contains all of the refactoring logic.

The approach takes as input the source code of the application and a decomposition proposition. The monolithic application must be implemented with Java Spring and conforms to the JPA specification. In your initial tests, I would suggest to use one of the applications they include in their evaluation (in the Results table.). 

In their paper, they relied on a Topic Modeling based approach to decompose the monolithic application and suggest the microservice suggestion (https://dl.acm.org/doi/10.1145/3412841.3442016). While the tool is open-source, it is not straightforward to use and requires some setup. I have used it extensively in previous projects so if you wish to use it (in case you want to test refactoring a new application), let me know and I can help you or provide a script to automate the approach.

The input of the decomposition proposition is a JSON file. You can find examples in the [Results/micro_candidate](./Results/micro_candidate) folder.

# Requirements
- A Unix like system (Linux, MacOS, WSL, etc). The tool will break otherwise as they do not handle cross-system pathing and similar problem. 
- Java 
- Maven
- Python 3.9 or higher

# Setup

Compile and package the javaParser tool

```sh
cd app/javaParser
mvn package
```

# Refactor an application

1. Generate or Select the microservice candidates (decomposition) file:
2. Create a folder to save the results in
3. call the main application script:

```sh
python app/main.py -c /path/to/your/microservice/candidates/file.json \
       -pp /path/to/the/monolith/source/code/ \
       -op /path/to/where/to/save/results/and/refactored/code
```
4. Inspect your refactored code in "/path/to/where/to/save/results/and/refactored/code/refactored/your_app_name"


# Generate decompositions with the Topic Modeling model

## Requirements
- Python
- Docker

## Steps

```sh
cd topicDecomp
sh decompose_with_topicdecomp.sh your_app_name /path/to/your/source/code 
```
if using Windows:
```commandline
cd topicDecomp
decompose_with_topicdecomp.bat your_app_name /path/to/your/source/code
```

You'll find the decomposition file in the `outputs/your_app_name/parsed_microRefact` folder.


# Refactor the code without building the application

## Requirements
- Docker

## Steps

Use the following example to refactor the code without building the application (useful for testing the refactoring logic):

```sh
SOURCE_DIR=/path/to/the/monolith/source/code/
OUTPUT_DIR=/path/to/where/to/save/results/and/refactored/code/
DECOMP_DIR=/path/to/your/microservice/candidates/
DECOMP_FILENAME=file.json


docker run -it --rm -v "$SOURCE_DIR":/source \
                    -v "$DECOMP_DIR":/json_dir \
                    -v "$OUTPUT_DIR":/outputs  \
                    dcalsel/microrefact:latest -c /json_dir/"$DECOMP_FILENAME" -pp /source -op/outputs
```

Remember to replace the variables with the correct paths and names.