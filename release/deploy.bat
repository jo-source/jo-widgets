SET TAG_NAME=0.50.0
SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"

cd %WORK_PATH%
cd %TAG_NAME%

call mvn -s %MVN_SETTINGS_PATH% clean deploy -Dmaven.repo.local=%MVN_REPO_PATH%
cd tycho/parent
call mvn -s ../../%MVN_SETTINGS_PATH% clean deploy -Dmaven.repo.local=../../%MVN_REPO_PATH%
cd ../..

cd ../../



