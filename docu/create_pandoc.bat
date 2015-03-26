if exist pandoc_out/ (
	rmdir /s /q pandoc_out
)
mkdir pandoc_out
copy style.css pandoc_out\style.css
xcopy images pandoc_out\images\ 

Setlocal EnableDelayedExpansion
set inputPath=
for /f %%f in ('dir /b pandoc_in\*.md') do (
	set inputPath=!inputPath! pandoc_in\%%f
	echo %%f
)

libs\pandoc\pandoc -s -S -t docbook %inputPath% -V lang=german --toc --number-sections --epub-chapter-level=1  -o pandoc_out/Dokumentation.db
libs\pandoc\pandoc %inputPath% -V lang=german --toc --number-sections --epub-chapter-level=1 --chapters -o pandoc_out/Dokumentation.pdf
libs\pandoc\pandoc %inputPath% -V lang=german -c style.css --toc --number-sections --epub-chapter-level=1 --chapters -o pandoc_out/Dokumentation.html


