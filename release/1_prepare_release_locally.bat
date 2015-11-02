REM local build only, no push or deployment

SET SOURCE_URL=https://github.com/jo-source/jo-widgets.git

SET RELEASE_VERSION=0.53.0
SET TAG_NAME=0.53.0
SET MAINTENANCE_BRANCH_NAME=MAINTENANCE_0.53

SET "WORK_PATH=workspace"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"
SET "LOG_FOLDER=logs"
SET "LOG_FILE=1_prepare.log"
set "LOG_PATH=..\..\%LOG_FOLDER%\%LOG_FILE%"

mkdir %LOG_FOLDER%
echo preparing release %RELEASE_VERSION% > %LOG_FOLDER%\%LOG_FILE% 

IF EXIST %WORK_PATH% (
	echo folder %WORK_PATH% does already exist, please delete or rename it >> %LOG_FOLDER%\%LOG_FILE% 
	EXIT /B 1
)

mkdir %WORK_PATH%
cd %WORK_PATH%

REM creating branch 
mkdir %TAG_NAME%
cd %TAG_NAME%
git clone %SOURCE_URL% .
git checkout -b %MAINTENANCE_BRANCH_NAME%

echo "setting release version and creating a tag" > %LOG_PATH% 2>&1
cd >> %LOG_PATH% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH%
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%RELEASE_VERSION% -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH%
call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%RELEASE_VERSION% -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH%
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_PATH% 2>&1

cd tycho/parent
cd >> %LOG_PATH% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%RELEASE_VERSION% -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH%
call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%RELEASE_VERSION% -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH%
call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH%
call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_PATH% 2>&1

cd ../..
cd >> %LOG_PATH%
cd ../..
