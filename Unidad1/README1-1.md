
# Práctica 1.1. Instalación Docker


## Objetivo 
- Instalar y configurar Docker en un entorno Linux utilizando máquinas virtuales.

## Duración aproximada
- 35 minutos.

## Requisitos del Sistema

- Docker se puede instalar en varias distribuciones de Linux, incluyendo:
    - Ubuntu (versión 18.04 o posterior es recomendada)
    - Debian (Buster o posterior)
    - CentOS (7 o posterior)
    - Fedora (34 o posterior)
    - RHEL (7 o posterior)

- Docker solo es compatible con arquitecturas de 64 bits.

- Es necesario que el sistema tenga un kernel de Linux versión 3.10 o posterior. Puedes verificar la versión del kernel ejecutando:

    ```bash
    uname -r
    ``` 

- Habilitar el módulo de overlay y overlay2, ya que Docker utiliza estos sistemas de archivos como el controlador de almacenamiento por defecto.

- Permisos de Usuario.
    - Docker necesita permisos de administrador para su instalación y configuración. Usualmente, los comandos se ejecutan con **sudo**.

<br/>

## Instrucciones

1. **Consulta con el instructor**

Pregunta a el instructor cómo acceder al entorno de prácticas del curso y asegúrate de entender todas las instrucciones necesarias para su acceso este día y los días siguientes de clase.

    - ¿Cuántas máquinas virtuales tienes?
    - ¿Cuáles son los sistemas operativos que tienes en cada máquina?

<br/>


2. ***Actualizar e instalar los paquetes requeridos:***

```bash
sudo apt update
sudo apt upgrade
sudo apt install apt-transport-https ca-certificates curl software-properties-common
```
<br/>


3. ***Agregar la clave de Docker y el repositorio***

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

Luego, agregar el repositorio.
```bash
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

<br/>


4. ***Actualizar de nuevo e instalar Docker***
```bash
sudo apt update
apt-cache policy docker-ce
sudo apt install docker-ce
```

<br/>


5. ***Verificar de Docker*** 

```bash
sudo systemctl status docker
```


<br/>


6. ***Configurar adicionalmente***

```bash
docker --version
id -nG
sudo usermod -aG docker <usuario>  # Reemplaza <usuario> con el nombre de tu usuario
cat /etc/group
```

***Nota:*** Para que los cambios en el grupo se apliquen, es recomendable cerrar y volver a abrir la sesión del usuario o ejecutar `newgrp docker`

<br/><br/>

## Resultado esperado

- docker --version

![docker --version](../images/u1_1_1.png)

***Nota:*** La versión de Docker podría cambiar.

<br/>


- sudo systemctl status docker


![sudo systemctl status docker](../images/u1_1_2.png)


<br/>


- docker info

<img src="../images/u1_1_3.png" alt="Descripción de la imagen" style="width:75%;"/>

 
<br/>


- docker run hello-world


![docker -run hello-world](../images/u1_1_4.png)
 


