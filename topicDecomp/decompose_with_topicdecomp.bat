@echo off

REM Get arguments
set APP=%1
set APP_PATH=%2

REM Create directories for input and output
mkdir "inputs\%APP%\source_code"

REM Copy source code to input directory
xcopy "%APP_PATH%\*" "inputs\%APP%\source_code" /E /I /Q

REM Create directories for raw results
mkdir "outputs\%APP%\raw_results"

REM Run the Docker container
docker run -v "%cd%\inputs\:/data/" -v "%cd%\outputs\%APP%\raw_results\:/outputs/" -it --rm dcalsel/topicdecomp:microrefact %APP%

REM Create directories for parsed results
mkdir "outputs\%APP%\parsed_microRefact"

REM Run the Python script
python parse_app.py %APP%

REM Exit the script
exit /b
