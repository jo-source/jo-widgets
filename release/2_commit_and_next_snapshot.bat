REM to be called after part1
REM does push but not deploy
REM release_part1.bat and release_part2.bat combined do the same as release.bat
 
SET USER_NAME="herr.grossmann@gmx.de"
 
SET SOURCE_URL=https://github.com/jo-source/jo-widgets.git

SET RELEASE_VERSION=0.55.0
SET TAG_NAME=0.55.0
SET MAINTENANCE_VERSION=0.55.1-SNAPSHOT
SET MAINTENANCE_BRANCH_NAME=MAINTENANCE_0.55
SET NEXT_DEVELOPMENT_VERSION=0.56.0-SNAPSHOT

SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"

SET "LOG_PATH=logs"
SET "LOG_FILE=..\..\logs\2_commit_and_snapshot.log"

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

git config --local credential.helper wincred
git config --local user.name %USER_NAME%

git commit --all -m "new version %RELEASE_VERSION%"
git push origin %MAINTENANCE_BRANCH_NAME%
git tag -a -m "release %RELEASE_VERSION%" %TAG_NAME%
git push origin %TAG_NAME%
cd ..

REM maintenance branch
mkdir %MAINTENANCE_BRANCH_NAME%
cd %MAINTENANCE_BRANCH_NAME%
cd >> %LOG_FILE% 2>&1

git clone %SOURCE_URL% .
git checkout %MAINTENANCE_BRANCH_NAME%
REM setting release version
call :setversion %MAINTENANCE_VERSION%

git config --local credential.helper wincred
git config --local user.name %USER_NAME%

git commit --all -m "maintenance version %MAINTENANCE_VERSION%"
git push 
cd ..

mkdir development
cd development
cd >> %LOG_FILE% 2>&1
git clone %SOURCE_URL% .
git checkout master

REM setting new development version
call :setversion %NEXT_DEVELOPMENT_VERSION% 

git config --local credential.helper wincred
git config --local user.name %USER_NAME%

git commit --all -m "next development version %NEXT_DEVELOPMENT_VERSION%"
git push

cd ../../
goto end

:setversion
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1
call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%1 -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH% >> %LOG_FILE% 2>&1
cd tycho/parent
call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%1 -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1
call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1
call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH% >> ../../%LOG_FILE% 2>&1
cd ../..
goto :eof

:end


