
SOURCE_DIR=/path/to/the/monolith/source/code/
OUTPUT_DIR=/path/to/where/to/save/results/and/refactored/code/
DECOMP_DIR=/path/to/your/microservice/candidates/
DECOMP_FILENAME=file.json


docker run -it --rm -v "$SOURCE_DIR":/source \
                    -v "$DECOMP_DIR":/json_dir \
                    -v "$OUTPUT_DIR":/outputs  \
                    dcalsel/microrefact:latest -c /json_dir/"$DECOMP_FILENAME" -pp /source -op/outputs