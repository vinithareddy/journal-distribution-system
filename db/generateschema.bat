"%PROGRAMFILES%\MySQL\MySQL Server 5.5\bin\mysql.exe" -u root -proot < truncate_transaction_data.sql
"C:\Program Files\MySQL\MySQL Server 5.5\bin\mysqldump.exe" --tables --triggers --routines -u root -proot jds > %~dp0\jds_schema_data.sql

REM "C:\Program Files\MySQL\MySQL Server 5.5\bin\mysqldump.exe" --no-data --tables -u root -proot jds > jds_schema.sql
REM "C:\Program Files\MySQL\MySQL Server 5.5\bin\mysqldump.exe" --no-data -u root -proot jds inward subscriber subscription subscriptiondetails > jds_transaction_data.sql
REM "C:\Program Files\MySQL\MySQL Server 5.5\bin\mysqldump.exe" -u root -proot jds agents cities countries currency districts inward_purpose inward_return_reasons journals payment_mode states subscriber_type users user_role> jds_master_data.sql