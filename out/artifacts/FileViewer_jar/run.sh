#!/bin/bash
echo RUNNING FileViewer.jar...

# Sets the initial heap size to 16gigabytes and the max to 20gigabytes
# java -Xms16G -Xmx20G -jar FileViewer.jar
java -jar FileViewer.jar
