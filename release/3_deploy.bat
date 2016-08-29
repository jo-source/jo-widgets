SET TAG_NAME=0.64.0
SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"

SET "LOG_PATH=logs"
SET "LOG_FILE=..\..\logs\3_deploy.log"

IF NOT EXIST %WORK_PATH% (
	echo folder %WORK_PATH% does not exist, please run step 1 first >> %LOG_FOLDER%\%LOG_FILE% 
	EXIT /B 1
)
cd %WORK_PATH%
IF NOT EXIST %TAG_NAME% (
	echo folder %TAG_NAME% does not exist, please run step 1 first >> ..\%LOG_FOLDER%\%LOG_FILE% 
	EXIT /B 1
)
cd %TAG_NAME%

call mvn -s %MVN_SETTINGS_PATH% -gs %MVN_SETTINGS_PATH% clean deploy -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1
cd tycho/parent
call mvn -s ../../%MVN_SETTINGS_PATH% -gs ../../%MVN_SETTINGS_PATH% clean deploy -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ..\..\%LOG_FILE% 2>&1

cd ../..
cd ../../
