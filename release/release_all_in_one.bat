IF EXIST workspace (
	echo folder workspace does already exist, please delete or rename it
	EXIT /B 1
)

call 1_prepare_release_locally
IF %ERRORLEVEL% NEQ 0 (
	echo something went wrong, aborting!
	EXIT /B 1
)

call 2_commit_and_next_snapshot
IF %ERRORLEVEL% NEQ 0 (
	echo something went wrong, aborting!
	EXIT /B 1
)

call 3_deploy
