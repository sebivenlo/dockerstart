# dockerstart
getting on the same level with docker.

* EricSoldierer added the hello world docker composer app, consisting of a persistence part and a web part.

homberghp: Please build war yourselves. It does not belong in a repository anyway, and you will have a go with maven.

## Docker and NetBeans IDE

After the demo by *MarvinRuesenberg* on how to connect the netbeans docker client to my ubuntu docker installation, I investigated a little futher. Here is what works for me.

In the file `/lib/systemd/system/docker.service` add the line

`EnvironmentFile=-/etc/default/docker`

before 

`ExecStart=/usr/bin/dockerd -H fd://`

and change that last line to

`ExecStart=/usr/bin/dockerd $DOCKER_OPTS -H fd://`

(add $DOCKER_OPTS).

Then in the file

`/etc/default/docker`

add or change the line

`DOCKER_OPTS="-H tcp://127.0.0.1:2375"`


lastly, in your `~/.bashrc`, add

`export DOCKER_HOST=tcp://127.0.0.1:2375`

Enjoy.