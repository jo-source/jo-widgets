SET SOURCE_URL=https://github.com/jo-source/jo-widgets.git

SET RELEASE_VERSION=0.49.1
SET TAG_NAME=0.49.1
SET MAINTENANCE_VERSION=0.49.2-SNAPSHOT
SET MAINTENANCE_BRANCH_NAME=MAINTENANCE_0.49

SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../maven/settings.xml"
SET "MVN_REPO_PATH=../../.m2"

REM clearing workspace, if any
for /f "delims=" %%i in ('dir /b "%WORK_PATH%*.*"') do rd /s /q "%WORK_PATH%%%i" 2>nul
del "%WORK_PATH%*.*" /f /q

mkdir %WORK_PATH%
cd %WORK_PATH%

REM creating branch 
mkdir %TAG_NAME%
cd %TAG_NAME%
git clone %SOURCE_URL% .
git checkout %MAINTENANCE_BRANCH_NAME%

REM setting release version and creating a tag
call :setversion %RELEASE_VERSION%
git commit --all -m "new version %RELEASE_VERSION%"
git push origin %MAINTENANCE_BRANCH_NAME%
git tag -a -m "release %RELEASE_VERSION%" %TAG_NAME%
git push origin %TAG_NAME%
cd ..

REM maintenance branch
mkdir %MAINTENANCE_BRANCH_NAME%
cd %MAINTENANCE_BRANCH_NAME%
git clone %SOURCE_URL% .
git checkout %MAINTENANCE_BRANCH_NAME%
REM setting release version
call :setversion %MAINTENANCE_VERSION%
git commit --all -m "maintenance version %MAINTENANCE_VERSION%"
git push 
cd ..

cd ../../
goto end

:setversion
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH%
call mvn -gs %MVN_SETTINGS_PATH% versions:set -DnewVersion=%1 -DgenerateBackupPoms=false -Dmaven.repo.local=%MVN_REPO_PATH%
call mvn -gs %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=%MVN_REPO_PATH%
cd tycho/parent
call mvn -gs ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%1 -Dtycho.mode=maven -Dmaven.repo.local=../../%MVN_REPO_PATH%
call mvn -gs ../../%MVN_SETTINGS_PATH% versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../%MVN_REPO_PATH%
call mvn -gs ../../%MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../%MVN_REPO_PATH%
cd ../..
goto :eof

:end
