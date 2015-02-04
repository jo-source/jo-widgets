if exist pandoc_out/ (
	rmdir /s /q pandoc_out
)
mkdir pandoc_out
pandoc -s -S -t docbook Dokumentation.md -V lang=german --toc --number-sections --epub-chapter-level=1  -o pandoc_out/Dokumentation.db
pandoc Dokumentation.md -V lang=german --toc --number-sections --epub-chapter-level=1 --chapters -o pandoc_out/Dokumentation.pdf
pandoc Dokumentation.md -V lang=german -c style.css --toc --number-sections --epub-chapter-level=1 --chapters -o pandoc_out/Dokumentation.html


