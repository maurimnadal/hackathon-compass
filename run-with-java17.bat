@echo off
echo Configurando ambiente para Java 17...

REM Salvar o JAVA_HOME atual
set ORIGINAL_JAVA_HOME=%JAVA_HOME%

REM Verificar se JAVA_HOME_17 está definido
if not defined JAVA_HOME_17 (
    echo ERRO: A variável JAVA_HOME_17 não está definida.
    echo Por favor, instale o JDK 17 e defina JAVA_HOME_17 apontando para ele.
    echo Exemplo: set JAVA_HOME_17=C:\Program Files\Java\jdk-17
    exit /b 1
)

REM Configurar temporariamente para Java 17
set JAVA_HOME=%JAVA_HOME_17%
set PATH=%JAVA_HOME%\bin;%PATH%

echo Usando Java:
java -version

echo.
echo Executando o projeto com Maven...
cd backend
mvn spring-boot:run

REM Restaurar o JAVA_HOME original
set JAVA_HOME=%ORIGINAL_JAVA_HOME%