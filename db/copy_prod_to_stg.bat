"%PROGRAMFILES%\MySQL\MySQL Server 5.5\bin\mysqldump.exe" -h localhost -u root -proot evitaran | "%PROGRAMFILES%\MySQL\MySQL Server 5.5\bin\mysql.exe" -h localhost -u root -proot jds > prod_to_stage.log 2>&1
pause