SET SOURCE_URL=https://github.com/jo-source/jo-widgets.git

SET RELEASE_VERSION=0.50.0
SET TAG_NAME=0.50.0
SET MAINTENANCE_BRANCH_NAME=MAINTENANCE_0.50

SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"
SET "LOG_PATH=logs\"
SET "LOG_FILE=../../logs/release.log"

REM clearing workspace, if any
for /f "delims=" %%i in ('dir /b "%WORK_PATH%*.*"') do rd /s /q "%WORK_PATH%%%i" 2>nul
del "%WORK_PATH%*.*" /f /q

mkdir %LOG_PATH%

mkdir %WORK_PATH%
cd %WORK_PATH%

REM creating branch 
mkdir %TAG_NAME%
cd %TAG_NAME%
git clone %SOURCE_URL% .
git checkout -b %MAINTENANCE_BRANCH_NAME%

echo "setting release version and creating a tag" > %LOG_FILE% 2>&1
cd >> %LOG_FILE% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE%
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%RELEASE_VERSION% -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE%
call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%RELEASE_VERSION% -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1

echo call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE%
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1

cd tycho/parent
cd >> %LOG_FILE% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%RELEASE_VERSION% -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE%
call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%RELEASE_VERSION% -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE%
call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1

echo call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE%
call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1

cd ../..
cd >> %LOG_FILE%

cd ..
cd ../
