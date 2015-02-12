if exist api_doc/ (
	rmdir /s /q api_doc/
)
javadoc -d api_doc/ -windowtitle "Jowidgets API Specification" -doctitle "Jowidgets API Specification" -sourcepath ..\modules\core\org.jowidgets.util\src\main\java;..\modules\core\org.jowidgets.i18n\src\main\java;..\modules\core\org.jowidgets.validation\src\main\java;..\modules\core\org.jowidgets.validation.tools\src\main\java;..\modules\core\org.jowidgets.classloading.api\src\main\java;..\modules\core\org.jowidgets.unit\src\main\java;..\modules\core\org.jowidgets.common\src\main\java;..\modules\core\org.jowidgets.api\src\main\java;..\modules\core\org.jowidgets.tools\src\main\java -subpackages org.jowidgets.util:org.jowidgets.i18n:org.jowidgets.validation:org.jowidgets.validation.tools:org.jowidgets.classloading.api:org.jowidgets.unit:org.jowidgets.common:org.jowidgets.api:org.jowidgets.tools -exclude java.net:java.lang


