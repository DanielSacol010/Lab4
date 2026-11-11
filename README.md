# Lab4

Aplicación de consola desarrollada en Java para gestionar una copa y sus encuentros deportivos.

## Requisitos

- JDK 11 o una versión posterior.
- `javac` y `java` disponibles en el `PATH`.

Puedes comprobar la instalación con:

```bash
java -version
javac -version
```

## Compilación

Desde la raíz del proyecto, ejecuta:

```bash
javac -d bin src/*.java
```

El parámetro `-d bin` guarda los archivos compilados en el directorio `bin`.

## Ejecución

Después de compilar, inicia la aplicación con:

```bash
java -cp bin Main
```

La aplicación se ejecuta en la consola y recibe las opciones mediante la entrada estándar.

## Estructura del proyecto

```text
src/    Código fuente Java
bin/    Clases compiladas
docs/   Documentación de la entrega
```

## Limpieza de archivos compilados

Para eliminar las clases generadas:

```bash
find bin -name "*.class" -delete
```
