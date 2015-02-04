if exist html_out/ (
	rmdir /s /q html_out
)
mkdir html_out
copy style.css html_out\style.css
xcopy images html_out\images\ 
xsltproc --stringparam chunker.output.indent yes --stringparam l10n.gentext.language de --stringparam chunk.section.depth 4 --stringparam chunk.first.sections 1 --stringparam  section.autolabel 1 --stringparam  section.label.includes.component.label 1 --stringparam base.dir html_out\ --stringparam html.stylesheet style.css C:\docbook-xsl-1.78.1\html\chunk.xsl pandoc_out/Dokumentation.db 


