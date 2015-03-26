if exist pandoc_out/ (
	rmdir /s /q pandoc_out
)
mkdir pandoc_out
copy style.css pandoc_out\style.css
xcopy images pandoc_out\images\ 

javac PandocPreProcessor.java
java PandocPreProcessor pandoc_in/ pandoc_in_pre/

timeout /t 1 /nobreak > nul

libs\pandoc\pandoc -s -S -t docbook pandoc_in_pre/docbook_in.md -V lang=german --toc --number-sections --epub-chapter-level=1  -o pandoc_out/Dokumentation.db
libs\pandoc\pandoc pandoc_in_pre/pdf_in.md -V lang=german --toc --number-sections --epub-chapter-level=1 --chapters  -H latex.sty  -o pandoc_out/Dokumentation.pdf
libs\pandoc\pandoc pandoc_in_pre/docbook_in.md -V lang=german -c style.css --toc --number-sections --epub-chapter-level=1 --chapters -o pandoc_out/Dokumentation.html


