@echo off

set HERE=%~dp0
set ANT_HOME=%HERE%\tools\apache-ant-1.7.0
set ANT_OPTS="-Xmx1024M"

set ANT_CMD_LINE_ARGS=%1
if "%1"=="" goto doneStart
shift
:setupArgs
if "%1"=="" goto doneStart
set ANT_CMD_LINE_ARGS=%ANT_CMD_LINE_ARGS% %1
shift
goto setupArgs
:doneStart

if "%ANT_CMD_LINE_ARGS"=="" set ANT_CMD_LINE_ARGS=test

%ANT_HOME%\bin\ant %BUILD_OPTS% -f %HERE%build.xml %ANT_CMD_LINE_ARGS%

