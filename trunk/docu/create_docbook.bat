if exist html_out/ (
	rmdir /s /q html_out
)
ping 192.0.2.2 -n 1 -w 2000 > nul
mkdir html_out
copy style.css html_out\style.css
xcopy images html_out\images\ 
javac DocBookPostProcessor.java
java DocBookPostProcessor pandoc_out/Dokumentation.db pandoc_out/Dokumentation_processed.db
java -cp "saxon/saxon.jar;saxon/saxon65.jar;saxon/xslthl-2.1.3.jar;saxon/xercesImpl.jar;saxon/xml-apis.jar" -Dxslthl.config="file:///c:/docbook-xsl-1.78.1/highlighting/xslthl-config.xml" -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl com.icl.saxon.StyleSheet -o html_out\index.html pandoc_out/Dokumentation_processed.db docbook.xsl 


