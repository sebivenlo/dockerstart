# dockerstart
getting on the same level with docker.

* [EricSoldierer](https://github.com/EricSoldierer) and [paulvv007](https://github.com/paulvv007)  added the hello world docker composer app, consisting of a persistence part and a web part.
* [jantrienes](https://github.com/jantrienes) and [holgerkemper](https://github.com/holgerkemper) added instructions and configuration of how to deploy the application with Maven to Wildfly.
* [MarvinRuesenberg](https://github.com/MarvinRuesenberg) added instructions on how you  connect Netbeans with your Docker machine.
* [HermLecluse](https://github.com/HermLecluse) added instructions on how you setup Docker in windows.

[homberghp](https://github.com/homberghp): Please build war yourselves. It does not belong in a repository anyway, and you will have a go with maven.

# Start Docker Machine
This section describes how you can start your docker machine on different operating systems.

## Linux

Somehow, things under Linux are a bit harder, mostly because linux does not require a virtual machine layer, which makes docker more lightweight.
Anyhow, on my machine, the docker0 ip address in 172.17.0.1, one can resolve this in the `hosts` file, see below.

## Windows
Windows makes things complicated, First make sure you're running a windows 10 Pro edition, otherwise you will need to setup an VM for an Unix environment. Then install Docker from Docker.com.

Clone the repository and open powershell inside the ~\dockerstart\helloworld.
run the following command:
```cmd
Docker-compose build
docker-compose up -d
```
install maven if needed : https://www.mkyong.com/maven/how-to-install-maven-in-windows/ (tutorial)

navigate to ~/dockerstart\helloworld\helloworld and run
```cmd
mvn wildfly:deploy -P wildfly-remote
```
this will deploy the application to the wilfdly. (make sure JDK refference in environment path is correct)

log in using Usernname: admin
             Password: Admin#70365
```cmd
docker run -d -p 8080:8080 --name HelloWorld Helloworld_app-web
```
browse to : //localhost:8011/helloworld/

Congratulations you're up and running
## macOS
Start your docker machine and establish a connection to it:
``` bash
docker-machine start
eval $(docker-machine env)
```

# IP address of Docker Machine
The IP address as well as how you establish a connection to the docker-machine depends on how you installed and/or configured docker on your machine. On some machines the docker machine can be accessed under `http://localhost:port`. On mac OSX the default IP address is 192.168.99.100.

To mitigate this issue and to achieve consistency across sebivenlo projects, a convention has to be created. This is being done by creating an alias for the IP address of the docker machine. This can be configured by adding an entry in the `hosts` configuration.

For Unix (including OSX) this is done by append this to the `/etc/hosts` file:

```
192.168.99.100 localdocker
```

for Linux the line could read (and does on ubuntu)

```
172.17.0.1 localdocker
```

But since on ubuntu a bridging network is configured, you could use `localhost` too as the address to visit your application.

On Windows this file is located here: `C:\Windows\System32\Drivers\etc\hosts`

Afterwards, the docker machine can be accessed under `http://docker:port`.

Note that 192.168.0.0/16 and 172.16.0.0/12 are both so called private networks and should not be used on the real internet. It is okay to use them inside a host or on a private net (typically applying NAT).

# Docker Compose to Link Containers
This `helloworld` application consists of two containers and one image.
Th immage, `wfpg` is a wildfly with an installed postgresql jdb driver. Purpose: If it is in the image, you can avoid deploying the jdbc driver.

Of the containers one represents the database, and is calls `app-db`. The  other the application server name `app-web`. To start up both of them, execute the command below in the folder where the `docker-compose.yml` file is located.

```bash
docker-compose up -d
```

Now, you should be able to connect to the Wildfy server via `http://docker:8011` in your browser.

Note that the `app-web` container does NOT yet have the application, a Java project. That is covered in the next section.


# Wildfly and Maven
When you develop an application that is supposed to run on a server, you will somehow have to deploy it. While manual deployment is possible, it is very time consuming. Luckily, Maven can assist us with this process.

There is a [Maven plugin](https://docs.jboss.org/wildfly/plugins/maven/latest/) to deploy applications onto a Wildfly container. For other application containers, such as glassfish or payara, a similar Maven plugins exist.

Following is a brief description of how to setup this plugin, and how to deploy the application to Wildfly.

## Plugin Configuration
Since the configuration parameter like the ip address, the username and/or the password usually differ from user to user, these parameters should be configured per user. This can be done in the global maven configuration. On Unix (Linux, OSX), this file is located under `~/.m2/settings.xml`. On Windows it can be found at `%USER_PROFILE%\.m2\settings.xml`.

Add the following configuration to this file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <profiles>
    <profile>
      <id>wildfly-remote</id>
      <properties>      
        <wildfly-hostname>localhost</wildfly-hostname>
        <wildfly-port>9990</wildfly-port>
        <wildfly-username>admin</wildfly-username>
        <wildfly-password>Admin#70365</wildfly-password>
      </properties>
    </profile>
  </profiles>

</settings>
```

You can leave the host in the deployment profile set to localhost, because in typically map the wildfly admin port to a localhost port in the bridge network setup.


## Deploy Application
After that, you can deploy the helloworld web app to your Wildfly server. Execute the following command in your maven project.

``` bash
mvn clean install wildfly:deploy -P wildfly-remote
```

Verify the result in your browser: `http://docker:8011/helloworld/`. The result should be something like this:

![helloworld-application](helloworld-application.png)

# Docker and NetBeans IDE

With Netbeans 8.2, Docker can be found in the "Services"-Tab in the Netbeans navigation-panel. This service allows us to execute docker commands from the netbeans environment. In order to do that, we have some configuration to do.

## Linux
Netbeans requires a docker daemon to communicate with, that is listening on the tcp-port 2375 on localhost.
The command for this is:

`dockerd -H tcp://127.0.0.1`

If you encounter problems doing this, such as:

`FATA[0000] Error starting daemon: pid file found, ensure docker is not running or delete /var/run/docker.pid`

you will have to stop the current docker service.

to do this, type:

`service docker stop`

and try again.

We will see how we can avoid that later on though.

## Windows and MacOS

When starting the docker-machine on Windows or MacOS, a virtual machine is used. This VM is providing a network address for communication by default.

Since this is the case, there is no need opening additional ports.
What is to be looked out for is the IP that the API is listening at.

e.G.

`192.168.99.100`
`192.168.99.101`

When opening the `add Docker` promt in the docker service, there is already a predefined IP + Port entered. Check if the IP is the same that your machine is running on and you will be able to connect to the daemon easily.

# Accessing Docker From Terminal AND Netbeans

## Linux

After the demo by MarvinRuesenberg on how to connect the netbeans docker client, homberghp investigated a little further. The following was seemed to get it working on an Ubuntu installation:

In the file

`/etc/default/docker`

add or change the line

```
DOCKER_OPTS="-H fd:// -H tcp://127.0.0.1:2375 --dns <your-local-dns> --dns 8.8.8.8 --dns 8.8.4.4"`
```
after changing the test `<your-local-dns>` to the actual dns for you local connections.

Now you can access the docker daemon using the default file descriptor protocol (fd://) as well as via a network connection via localhost (tcp://127.0.0.1), to be used by Java and netbeans.

To keep this working, even after a Linux restart, doe the following:

Create a new directory under `/etc/systemd/system/docker.service.d` with

`mkdir -p etc/systemd/system/docker.service.d`

and add a file named `overlay.conf` with the contents

```
[Service]
# workaround to include default options
EnvironmentFile=/etc/default/docker   
ExecStart=
ExecStart=/usr/bin/dockerd $DOCKER_OPTS -s overlay2
```

The of the file is not relevant, as long as it ens in `.conf`. I chose overlay, because the configuration file also selects the overlay2 driver for the docker file systems.


This file, because of its location and extension, will be automatically loaded by the _systemd_ system and
will setup the docker service with tcp connections and the (faster) overlay2 persistense layer driver. Currently (2017-09-06) overlay2 is the advised driver for docker on ubuntu and tested on xenial.

Notice that changing the persistence driver from the default (aufs) will make any previous images no longer accessible.
You may want to keep those, by changing the text `-s overlay2`  to `-s aufs` in the snippet above.


You can connect and command docker from both services, one being the netbeans service, the other one being the commandline interface. By default this is not possible since docker is communicating via an Unix Socket to which Java and hence also NetBeans-IDE cannot connect.

By doing the above, we are opening an actual network tcp-socket that netbeans is able to address.

You can make this a litle more intuitive by adding the line
```
172.17.0.1 	localdocker.local localdocker
```
 to your `/etc/hosts` file, which allows you to addres the docker daemon with a name.

## Windows and MacOS

This does not have to be done on MacOS and Windows since they are already providing network access via their Virtual Machine.

# Docker Cheat Sheets
* Basic (used in [Jenkins Workshop](https://github.com/sebivenlo/jenkins)): https://sebivenlo.github.io/jenkins/docker-cheat-sheet/docker-cheat-sheet.html
* Very comprehensive: https://github.com/wsargent/docker-cheat-sheet

Enjoy.
