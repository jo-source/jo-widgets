if exist docu (
	rmdir /s /q docu
)
mkdir docu
copy style.css docu\style.css
copy pandoc_out\Dokumentation.pdf docu\documentation.pdf
xcopy images docu\images\ 
javac DocBookPostProcessor.java
java DocBookPostProcessor pandoc_out/Dokumentation.db pandoc_out/Dokumentation_processed.db
java -cp "saxon/saxon.jar;saxon/saxon65.jar;saxon/xslthl-2.1.3.jar;saxon/xercesImpl.jar;saxon/xml-apis.jar" -Dxslthl.config="file:///c:/docbook-xsl-1.78.1/highlighting/xslthl-config.xml" -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl com.icl.saxon.StyleSheet -o docu\index.html pandoc_out/Dokumentation_processed.db docbook.xsl 


