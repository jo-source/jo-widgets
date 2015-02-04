pandoc -s -S -t docbook Dokumentation.md -V lang=german --toc --number-sections --epub-chapter-level=1  -o Dokumentation.db
pandoc Dokumentation.md -V lang=german --toc --number-sections --epub-chapter-level=1 --chapters -o Dokumentation.pdf
pandoc Dokumentation.md -V lang=german -c style.css --toc --number-sections --epub-chapter-level=1 --chapters -o Dokumentation.html


