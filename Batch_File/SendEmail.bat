cd "C:\Backup\NG_Projects\qa-platform-app"
TIMEOUT /T 20

set classpath=C:\Backup\NG_Projects\qa-platform-app\bin;C:\Backup\NG_Projects\qa-platform-app\lib\*;

java org.testng.TestNG "C:\Backup\NG_Projects\qa-platform-app\XML\SendEmail.xml"

exit

