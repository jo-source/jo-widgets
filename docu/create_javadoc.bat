if exist api_doc/ (
	rmdir /s /q api_doc/
)
javadoc -d api_doc/ -sourcepath ..\modules\core\org.jowidgets.util\src\main\java;..\modules\core\org.jowidgets.i18n\src\main\java;..\modules\core\org.jowidgets.validation\src\main\java;..\modules\core\org.jowidgets.validation.tools\src\main\java;..\modules\core\org.jowidgets.classloading.api\src\main\java;..\modules\core\org.jowidgets.unit\src\main\java;..\modules\core\org.jowidgets.common\src\main\java;..\modules\core\org.jowidgets.api\src\main\java;..\modules\core\org.jowidgets.tools\src\main\java -subpackages org.jowidgets.util -exclude java.net:java.lang -subpackages org.jowidgets.i18n -exclude java.net:java.lang -subpackages org.jowidgets.validation -exclude java.net:java.lang -subpackages org.jowidgets.validation.tools -subpackages org.jowidgets.classloading.api -exclude java.net:java.lang  -subpackages org.jowidgets.unit -exclude java.net:java.lang -exclude java.net:java.lang -subpackages org.jowidgets.common -exclude java.net:java.lang -subpackages org.jowidgets.api -exclude java.net:java.lang -subpackages org.jowidgets.tools -exclude java.net:java.lang


