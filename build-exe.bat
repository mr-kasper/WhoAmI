@echo off
echo Building Attendance App...
cd /d "c:\Users\Rachid\Desktop\ppp"
call mvn clean package -DskipTests
echo.
if exist "target\AttendanceApp.exe" (
    echo SUCCESS! EXE file created at: target\AttendanceApp.exe
) else (
    echo Build completed. Check target folder for output.
)
echo.
pause
