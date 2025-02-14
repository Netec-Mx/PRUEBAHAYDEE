## **Instalación de Kubernetes**

### versión 1.1

### Entorno del curso básico:

 - 3 máquinas virtuales Ubuntu 20.04
 - 4 CPUs, 8 GB RAM, 50 GB Disco, valores recomendados para el nod maestro.
 - 2 CPUs, 4 GB RAM, 50 GB Disco, valores recomendados para los nodos trabajadores.
 - Kubernete versión 1.30
 - Interfaces: **containerd** & **Weave/Calico**


### Componentes a instalar:

 - Master Node / Control Plane
	 - kube-api-server
	 - etcd
	 - Controller Manager
	 - kube-scheduler

 - Worker Node
	 - kubelet
	 - kube-proxy
	 - containerd


## Control Plane:

1. Instalar paquetería básica.
   
```bash
sudo apt-get update  
sudo apt install apt-transport-https curl -y
```

<br/>

2. Preconfigurar la red. 

```bash
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

# Carga los módulos para almacenamiento de contenedores y enrutamiento del tráfico de redes
sudo modprobe overlay
sudo modprobe br_netfilter


# sysctl params 
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# Aplicando los parámetros
sudo sysctl --system

# Verificación de módulos
lsmod | grep br_netfilter
lsmod | grep overlay

# Verifica los valores actuales 
sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward
```

<br/>

 3. Swap

```bash
# Apagar el swap
sudo swapoff -a
```

- Para persistir la configuración de **swapoff -a** se debe editar el archivo **/etc/fstab**, comentar la linea asignada a swap y luego reiniciar la máquina. 

<br/>

 4. Instalar containerd (Las líneas de instrucciones son largas, tener cuidado al copiar toda la línea)

```bash

# Agregando Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc


# Agrega the repository to Apt sources:
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

<br/>

5. Habilitar containerd como Container Runtime, sustituir el contenido del archivo **/etc/containerd/config.toml**, por lo siguiente:

```
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
    SystemdCgroup = true

```

<br/>

6. Reiniciar containerd.

```bash
sudo systemctl restart containerd
```

<br/>

7. Actualizar e instalar paquetería necesaria

```bash

sudo apt-get update
 
sudo apt-get install -y apt-transport-https ca-certificates curl gpg
```

<br/>

8. Descargar y agregar repositorios de kubernetes, tener cuidado al copiar la línea de instrucción con `echo` es muy larga.

```bash

curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

# This overwrites any existing configuration in /etc/apt/sources.list.d/kubernetes.list

echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

```

<br/>

9. Instalar y  kubelet, kubeadm y kubectl. 

```bash

sudo apt-get update

sudo apt-get install -y kubelet kubeadm kubectl

sudo apt-mark hold kubelet kubeadm kubectl
```

- **Nota**: Importante evitar que una actualización automática rompa dependencias. Se debe mantener una versión específica de un paquete por razones de compatibilidad. La instrucción apt-mark asegurará que las herramientas permanezcan en la versión actual.
  
<br/>

10. Inicializar control plane.

    - **Nota:** Cambiar la IP por la reportada en el comando `ip add`

```bash

# Obtener la IP de la máquina que seá el Nodo Maestro o Control Plane.
ip add

# Crear el clúster de Kubernetes
sudo kubeadm init --pod-network-cidr=10.244.0.0/16 --apiserver-advertise-address=<ip de control plane>
```

<br/>

11. Habilitar cluster para cualquier usuario.

```bash

mkdir -p $HOME/.kube

sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

sudo chown $(id -u):$(id -g) $HOME/.kube/config

```

<br/>

12. Instalar CNI como network add-on de Kubernetes, podría ser Weave o Calico, en cualquier caso deberá de verificar primero cuál es el más acertado de acuerdo a sus necesidades. 

**Opción 1**. Weave 2.29

```bash
kubectl apply -f https://reweave.azurewebsites.net/k8s/v1.30/net.yaml
```

**Opción 2**. Calico 3.25.1

```bash
  kubectl apply -f https://docs.projectcalico.org/v3.25/manifests/calico.yaml
```
<br/>


## Worker Node:

1. Instalar paquetería básica.

```bash
sudo apt-get update 
sudo apt install apt-transport-https curl -y
```

<br/>

2. Preconfigurar networking.

```bash
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

sudo modprobe overlay

sudo modprobe br_netfilter

# sysctl params 
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# Apply sysctl params 
sudo sysctl --system

lsmod | grep br_netfilter

lsmod | grep overlay

sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward
```

<br/>

3. Swap

```bash
sudo swapoff -a
```

- Para persistir la configuración de **swapoff -a** se debe editar el archivo **/etc/fstab**, comentar la linea asignada a swap y luego reiniciar la máquina. 

<br/>

4. Instalar containerd.

``` bash

# Add Docker's official GPG key: 
sudo apt-get update 
sudo apt-get install ca-certificates curl 
sudo install -m 0755 -d /etc/apt/keyrings 
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc 
sudo chmod a+r /etc/apt/keyrings/docker.asc 

# Add the repository to Apt sources (Line too long): 
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Update 
sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

```

<br/>

5. Habilitar containerd como Container Runtime, sustituye el contenido del archivo **/etc/containerd/config.toml** por lo siguinte:

```
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
    SystemdCgroup = true

```

<br/>

6. Reiniciar containerd.

```bash
sudo systemctl restart containerd
```

<br/>

7. Actualizar e instalar paquetería necesaria.

```bash
sudo apt-get update

# apt-transport-https may be a dummy package; if so, you can skip that package
sudo apt-get install -y apt-transport-https ca-certificates curl gpg

```

<br/>

8. Descargar y agregar repositorios de kubernetes.
```bash
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

# This overwrites any existing configuration in /etc/apt/sources.list.d/kubernetes.list (line too long)
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list
  
```

<br/>

9. Instalar kubelet, kubeadm y kubectl.

```bash
sudo apt-get update

sudo apt-get install -y kubelet kubeadm kubectl

sudo apt-mark hold kubelet kubeadm kubectl
```

- **Nota**: Importante evitar que una actualización automática rompa dependencias. Se debe mantener una versión específica de un paquete por razones de compatibilidad. La instrucción apt-mark asegurará que las herramientas permanezcan en la versión actual.

  
10. Unir Worker node a Control Plane, esta instrucción puede ser obtenida en el momento que se crea el clúster de Kubernetes.

```bash
sudo kubeadm join <ip-controlplane>:6443 --token <token> --discovery-token-ca-cert-hash sha256:ec2ee63f8853ad42a9ec0363508d1da7b3983cdef2361efd43e20d7d85953f26
```

En caso de no haber guardado la instrucción, la instrucción siguiente, genera un token de autenticación y muestra el comando completo necesario para unir un nodo trabajador (worker node) o un nodo maestro al clúster de Kubernetes.

```bash
# Master Node / Control Pane
kubeadm token create --print-join-command
```

- **Notas**
  1. Asegúrate de que el puerto 6443 esté accesible desde el nodo trabajador hacia el nodo maestro.
  2. Si el token expira o necesitas uno nuevo, puedes regenerarlo fácilmente con  `kubeadm token create`.

<br/>

---

[Referencias](Referencias.md)
