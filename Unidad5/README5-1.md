# Práctica 5.1. Introducción a los volumes de K8s

## Objetivo 
- Configurar diferentes tipos de volúmenes y reclamos de almacenamiento en Kubernetes, integrando recursos de almacenamiento en los pods y garantizando la persistencia de datos.

## Duración aproximada
- 35 minutos.

## Instrucciones

### Paso 1. Configurar un volumen hostPath

1. Un volumen **hostPath** monta un directorio del nodo en el que se está ejecutando el Pod directamente en el contenedor. Este tipo de volumen es útil para tareas como almacenar logs localmente.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: hostpath-pod
spec:
  containers:
    - name: nginx
      image: nginx
      volumeMounts:
        - mountPath: /usr/share/nginx/html
          name: host-volume
  volumes:
    - name: host-volume
      hostPath:
        path: /data/nginx
        type: DirectoryOrCreate

```

- **Observaciones**:

    - Se crea un pod que ejecuta un contenedor `nginx`.

    - El volumen host-volume monta el directorio `/data/nginx` del nodo anfitrión en el contenedor en `/usr/share/nginx/html`.

    - Si `/data/nginx` no existe en el nodo, se creará automáticamente.


2. Comando para aplicar:

```bash
kubectl apply -f hostpath-pod.yaml
```

<br/>

### Paso 2. Configurar un **PersistentVolume** (PV) y un **PersistenVolumeClaim** (PVC)

- Los **PersistentVolumes** son unidades de almacenamiento disponibles para los clústeres de Kubernetes. 

- Un **PersistentVolumeClaim** es una solicitud de almacenamiento para usar en un pod.

```yaml
apiVersion: v1
kind: PersistentVolume  # PV
metadata:
  name: pv-volume
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteOnce
  hostPath:
    path: "/mnt/data"

```

**Observaciones**:

- Se crea un PersistentVolume llamado pv-volume con 1Gi de almacenamiento.

- Este volumen permite acceso de lectura/escritura a un solo nodo (ReadWriteOnce).

- El volumen se asocia al directorio /mnt/data del nodo.


#### Archivo YAML para PersistentVolumeClaim.

```yaml
apiVersion: v1
kind: PersistentVolumeClaim   # PVC
metadata:
  name: pvc-volume
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

**Observaciones**:

- Se crea un PersistentVolumeClaim llamado pvc-volume que solicita 1Gi de almacenamiento.

- Este reclamo será enlazado al PersistentVolume compatible, en este caso, pv-volume.

<br/>

**Comando para aplicar ambos recursos**.

```bash
kubectl apply -f persistent-volume.yaml
kubectl apply -f persistent-volume-claim.yaml

# Para verificar su existencia.
kubectl get pv
kubectl get pvc

# Verificación más detallada.
kubectl describe pvc pvc-volume

```

**Observaciones**:

- Al verificar el estado de PVs y PVCs, deben de mostrar un estado de **Bound**.

- Si el **PersistentVolumeClaim** muestra un estado **Pending**, esto indica que Kubernetes no pudo enlazar el PVC con un PV compatible.

<br/>

### Paso 3. Configura un Pod que use el PersistentVolumeClaim

Una vez creado el _reclamo_, puedes montar el volumen persistente en un Pod.

Archivo YAML de configuración del Pod.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: pvc-pod
spec:
  containers:
    - name: busybox
      image: busybox
      command: [ "sleep", "3600" ]
      volumeMounts:
        - mountPath: "/mnt/data"
          name: pvc-volume
  volumes:
    - name: pvc-volume
      persistentVolumeClaim:
        claimName: pvc-volume
```

**Observaciones:**

- Este Pod ejecuta un contenedor busybox y monta el PersistentVolumeClaim **pvc-volume** en **/mnt/data**.

- El Pod estará en ejecución durante una hora (sleep 3600), permitiéndote verificar la persistencia.

<br/>

Comando para aplicar:

```bash
kubectl apply -f pvc-pod.yaml
```

<br/>

### Paso 4. Verificar la persistencia de datos.

Una vez que el pod esté en ejecución, puedes verificar que los datos almacenados en el volumen sean persistentes.


```bash
# Comando para acceder al Pod.
kubectl exec -it pvc-pod -- /bin/sh

# Comandos dentro del contendor.
echo "Hola desde el volumen persistente!" > /mnt/data/hola.txt
cat /mnt/data/hola.txt

# Eliminamos el Pod pvc-pod.
kubectl delete pod pvc-pod

# Creamos nuevamente el Pod y accedemos de nuevo al contenedor.
kubectl apply -f pvc-pod.yaml

# Acceder nuevamente al Pod.
kubectl exec -it pvc-pod -- /bin/sh

# Verifica la existencia y contenido del archivo `hola.txt`.
ls -l /mnt/data
cat /mnt/data/hola.txt
```

<br/>

### Paso 5. Limpieza

- Si detectas un problema de configuración, puede ser útil eliminar el PV y PVC y recrearlos:

```bash
kubectl delete pvc pvc-volume
kubectl delete pv pv-volume
kubectl apply -f persistent-volume.yaml
kubectl apply -f persistent-volume-claim.yaml
```

<br/>

## Resultado esperado


- Captura de pantalla que muestra el estado del directorio /mnt/data sobre el nodo worker.

![kubectl](../images/u5_1_1.png)

<br/>


- Captura de pantalla que muestra  el YAML hostpath-pod.yaml, la aplicación de este YAML, y los PVs creados.

![kubectl](../images/u5_1_2.png)

<br/>


- Captura de pantalla que muestra los YAMLs del PersistentVolume & PesistentVolumeClaim.

![kubectl](../images/u5_1_3.png)

<br/>

- Captura de pantalla que muestra la aplicación de pv y pvc, así como la verificación de creación.

![kubectl](../images/u5_1_4.png)

<br/>


- Captura de pantalla que muestra el antes y después de crear al Pod **pvc-pod**.

![kubectl](../images/u5_1_5.png)

<br/>


- Captura de pantalla que muestra la conexión al contenedor del Pod **pvc-pod**, y la salida de algunos comandos Unix, en donde se crea un archivo llamado **hola.txt**.

![kubectl](../images/u5_1_6.png)

<br/>


- Captura de pantalla que muestra el borrado y la creación del Pod **pvc-pod** y como la información del archivo **hola.txt** persiste.

![kubectl](../images/u5_1_7.png)

<br/>







