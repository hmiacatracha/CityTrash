
# Objetivo

Hoy en día la mayoría de las ciudades cuentan con un planeamiento urbanístico en el que cuenta con contenedores según el tipo de residuo, estos a la vez traen integrado sensores que permite conocer y analizar los datos transferidos para poder facilitar la gestión de recogida de residuos. CityTrash está pensado ese tipo de ciudades. Por lo tanto, este es un proyecto clásico de ingeniería en el que se encargará de gestionar diferentes tipos de recursos para planificar
las rutas de recogida de basura en la ciudad. CityTrash contará con varios tipos de trabajadores como usuarios del sistema y estos pueden ser administradores, conductores y recolectores. Los administradores serán los encargados de gestionar los diferentes tipos de recursos (trabajadores, camiones, contenedores, los diferentes tipos de modelos de camiones y contenedores, sensores de los contenedores, rutas estáticas y generar las rutas diarias), y además podrán
acceder al historial general de rutas, donde aparecen todas las rutas diarias generadas del sistema. Todos los trabajadores tendrán su propio historial de rutas, este historial mostrará toda las rutas diarias en los que ese trabajador haya sido asignado a esa ruta diaria como conductor o por ser uno de los recolectores de basura asignados. Todos los usuarios del sistema (anónimos y trabajadores) podrán ver un mapa de los contenedores registrados en la ciudad, y las estadísticas de reciclado por tipo de basura. El sistema realizará una serie de validaciones periódicamente y generará alertas en caso de ser necesario. También se podrá realizar búsquedas avanzadas en los listados de trabajadores, camiones, modelos de camiones y contenedores, rutas estáticas y el historial general de rutas. Esta aplicación se ha desarrollado implementando
el patrón de diseño Modelo Vista Controlador por medio de Spring MVC para las capas de negocio, servicio y presentación, y Thymeleaf en la vista web. Para el almacenamiento de la información se utilizó el gestor de almacenamiento MySql, utilizando hibernate como herramienta de mapeo objeto-relacional (ORM) que facilita la relación entre los objetos y la base de datos. 

## ENTORNO DE DESARROLLO
La aplicación se desarrollará en un entorno de desarrollo integrado (IDE). Base de datos relacional. Las tecnologías empleadas serán Java SE y Java EE, Hibernate, Spring, Junit y Mysql.

## INSTALACIÓN DE CITYTRASH
# 1.Instalación y Configuración
Antes de empezar con la instalación de la aplicación, es necesario tener instalado Maven,
Apache Tomcat, Jetty, JDK y MySql. Ademas hay que configurar las variables de entorno.
## 2. Variables de entorno. 
Para configurar las variables de entorno tenemos que modificar el siguiente fichero en el
sistemas operativo Linux.

    $ gedit $HOME /.bashrc
    
Se debe de añadir las siguientes lı́neas en el fichero .bashrc

    ## J2SE
    export JAVA HOME=/opt/jdk1.8.005
    #Maven
    MAVEN HOME=/opt/apache−maven−3.2.2
    PATH=$MAVEN HOME/bin:$PATH
    export MAVEN OPTS=”−Xms512m −Xmx1024m”

    ## MySQL
    MYSQL HOME=/opt/mysql−5.6.19−linux−glibc2.5−i686
    PATH=$MYSQL HOME/bin:$PATH
    #Tomcat 
    export CATALINA OPTS=$MAVEN OPTS

## Configuración MySQL
### 1. Arrancar MySQL
    $ mysqld
### 2. Creción de la base de datos (Producción y Test)
    $ mysqladmin −u root create citytrashpojo
    $ mysqladmin −u root create citytrashpojotest
### 3. Creación del usuario pojo
    $ mysql −u root
    GRANT ALL PRIVILEGES ON citytrashpojo.∗ to citytrash pojo@localhost
    GRANT ALL PRIVILEGES ON citytrashpojotest.∗ to citytrash pojo@localhost
    exit
### 4. Comprobamos el acceso
    $ mysql −u citytrashpojo −−password=c17yTr4asH citytrashpojo
    exit
    $ mysql −u citytrashpojo −−password=c17yTr4asH citytrashpojotest
    exit

## Jetty
### 1. Iniciamos la base de datos myslq desde la terminal.
    $ mysqld
### 2. Vamos a la raı́z del proyecto y ejecutamos los SCRIPT SQL para la creación de tablas.
    $ mvn sql:execute
### 3. Crear el artefacto del proyecto e instarlo en el repositorio central.
    $ mvn install
### 4. Ejecutar una aplicación en el servidor Web Jetty sin necesidad de empaquetarla en un fichero WAR.
    $ mvn jetty:run
### 5. Ir al navegador para poder ejecutar nuestra aplicación. 
   http://localhost:8080/citytrash/

## 5. Ejecutar en un servidor de aplicaciones TOMCAT
Para arrancar el servicio con Apache Tomcat haremos los pasos del 1-4 del apartado anterior
(apartado 4), a partir de allı́ haremos lo siguiente:

### 1. Configurar un DataSource global llamado jdbc/pojo-examples-ds , este se encuentra en /software/apache-tomcat-8.0.9/conf/server.xml. Esto es para comunicar la aplicación web con la base de datos.

    <Resource name= "jdbc/pojo-citytrashpojo-ds"
    auth="Container"
    type= "javax.sql.DataSource"
    driverClassName= "com.mysql.jdbc.Driver"
    url= "jdbc:mysql://localhost/citytrashpojo"
    username= "citytrashpojo"
    password= "c17yTr4asH"
    maxActive= "4"
    maxIdle= "2"
    maxWait= "10000"
    removeAbandoned= "true"
    removeAbandonedTimeout= "60"
    2logAbandoned= "true"
    validationQuery= "SELECT COUNT(*) FROM PingTable/>


### 2. Añadir en el fichero /software/apache-tomcat-8.0.9/conf/context.xml
    $ gedit ̃/software/apache−tomcat−8.0.9/conf/context.xml
las siguientes lı́neas 

    <ResourceLink name="jdbc/pojo-citytrashpojo-ds"
    global="jdbc/pojo-citytrashpojo-ds"
    type="javax.sql.DataSource/>

### 3. Generemos .war haciendo uso de maven en la raı́z del proyecto.
     $ mvn package
Luego copiamos el fichero .war generado al directorio
      $ cp dirorigen package  ̃/software/apache−tomcat−8.0.9/webapps
      
### 4. Ahora vamos a añadir el driver mysql que se obtiene de HOME/.m2/repository/mysql/mysql-connector-java/5.1.21/mysql-connector-java-5.1.21.jar a la carpeta /software/apache-tomcat-8.0.9/lib

### 5. Antes de iniciar o finalizar TOMCAT es necesario que nos desplacemos hasta la carpeta bin.
      $ cd  ̃/software/apache−tomcat−8.0.9/bin
### 6. Iniciar TOMCAT 
      $ ./startup.sh
### 7.- Comprobar en el navegador 
  http://localhost:8080/citytrash
### 8. Finalizar TOMCAT
      $ ./shutdown.sh

