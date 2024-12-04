#!/bin/bash

PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
export PROJECT_VERSION

docker build -t sydwellcbuda7/student_portal:$PROJECT_VERSION .
