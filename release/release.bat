SET SOURCE_URL=https://github.com/jo-source/jo-widgets.git
SET RELEASE_VERSION=0.49.0
SET NEXT_DEVELOPMENT_VERSION=0.50.0-SNAPSHOT

SET "WORK_PATH=workspace\"
SET "MVN_SETTINGS_PATH=../../../maven/settings.xml"

for /f "delims=" %%i in ('dir /b "%WORK_PATH%*.*"') do rd /s /q "%WORK_PATH%%%i" 2>nul
del "%WORK_PATH%*.*" /f /q

call cd workspace

call mkdir release

call cd release

call git clone %SOURCE_URL%

call cd jo-widgets

call mvn -s %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../.m2

call mvn -s %MVN_SETTINGS_PATH% versions:set -DnewVersion=%RELEASE_VERSION% -DgenerateBackupPoms=false -Dmaven.repo.local=../../.m2

call mvn -s %MVN_SETTINGS_PATH% clean install -Dmaven.repo.local=../../.m2

cd tycho/parent

call mvn -s ../../%MVN_SETTINGS_PATH%  versions:update-parent -DallowSnapshots=true -DgenerateBackupPoms=false -Dmaven.repo.local=../../../../.m2

call mvn -s ../../%MVN_SETTINGS_PATH% org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=%RELEASE_VERSION% -Dtycho.mode=maven -Dmaven.repo.local=../../../../.m2

call mvn -s ../../%MVN_SETTINGS_PATH%  clean install -Dmaven.repo.local=../../../../.m2

rem call cd..

rem call cd release