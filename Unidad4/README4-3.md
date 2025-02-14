# Práctica 4.3. Configuración de Job e InitContainers

## Objetivo

- Configurar Jobs e InitContainers en Kubernetes para ejecutar tareas específicas o configuraciones previas.


## Duración aproximada

- 25 minutos.

## Instrucciones

### Paso 1. Crear un Namespace

1. Crear un **Namespace** para organizar los recursos de esta práctica.

```bash
kubectl create namespace practica-job-init
```

<br/>

### Paso 2. Configuración de InitContainer

Un InitContainer es un contenedor especial que se ejecuta antes de que el contenedor principal comience. Este tipo de contenedor es útil para tareas de configuración, como preparar archivos o verificar dependencias.

1. Crear un archivo YAML para el Deployment que incluirá el InitContainer.


```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
  namespace: practica-job-init
spec:
  replicas: 1
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      initContainers:   # InitContainer
        - name: init-myservice
          image: busybox
          command: ['sh', '-c', 'echo Initializing... && sleep 5']
      containers:       # Container 
        - name: myapp
          image: nginx
          ports:
            - containerPort: 80


```

2. Aplicar el archivo YAML.

```bash
kubectl apply -f init-container-deployment.yaml
```

- **Nota**: Este InitContainer simplemente ejecutará un comando de shell que imprime un mensaje y espera cinco segundos, simulando una tarea de inicialización. Las tareas de inicialización pudieran ser más complejas.


<br/> 

### Paso 3. Configuración de Job

Un Job en Kubernetes se utiliza para ejecutar una tarea específica que debe completarse una vez. Un Job es ideal para tareas de mantenimiento o de procesamiento de datos.

1. Crear un archivo YAML para el Job.

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: my-job
  namespace: practica-job-init
spec:
  template:
    spec:
      containers:
        - name: job-container
          image: busybox
          command: ['sh', '-c', 'echo "Job completed successfully" && sleep 10']
      restartPolicy: Never   # No hay reinicios 
  backoffLimit: 4
```

2. Aplicar el archivo.

```bash
kubectl apply -f job.yaml
```

**Nota**: Este **Job** ejecutará un comando que imprime un mensaje y espera 10 segundos antes de completarse. La **restartPolicy** se configura en Never para evitar reinicios si el Job se completa sin errores.

<br/>

### Paso 4. Verificación de los recursos creados

1. Verificar el estado del **Deployment** y del **InitContainer**:

```bash
kubectl get pods -n practica-job-init  # Localiza el nombre del Pod
kubectl describe pod <nombre-del-pod> -n practica-job-init
```

2. Verificar el estado del **Job**:

```bash
kubectl get jobs -n practica-job-init
kubectl describe job my-job -n practica-job-init
```

3. Para ver los logs de los contenedores:

```bash
kubectl get pods -n practica-job-init  # Localiza el nombre del Pod
kubectl logs <nombre-del-pod> -c init-myservice -n practica-job-init  # InitContainer
kubectl logs job/my-job -n practica-job-init  # Job
```

<br/> 

### Paso 5. Limpieza 

Una vez completada la práctica, eliminar los recursos.

```bash
kubectl delete namespace practica-job-init
```

<br/><br/>

## Resultado esperado

<br/>

- Captura de pantalla que muestra el contenido del init-container-deployment.yaml, como aplica el YAML, como este marca error al no haber creado primero el espacio de nombres, y después como se aplica exitosamente.

![kubectl](../images/u4_3_1.png)


<br/>

- Captura de pantalla que muestra el Pod creado en el espacio de nombres practica-job-init. Además lista los detalles del Pod en la sección **Init Containers**.

    - El nombre del InitContainer `init-myservice`.
    
    - La imagen usada `busybox`.

    - El estado `Completed` si se ejecutó exitosamente.

    - El mensaje que muestra el comando de echo en el InitContainer `Initializing...`.

![kubectl](../images/u4_3_2.png)


<br/>

- Captura de pantalla que muestra el Pod en el espacio de nombre practica-job-init y la bitácora del Pod con el InitContainer.

![kubectl](../images/u4_3_3.png)


<br/>

- Captura de pantalla que muestra el contenido del YAML para la creación del Job, además de com ose aplica el YAML, el Pod y el Job creado en este punto.

![kubectl](../images/u4_3_4.png)

<br/>

- Captura de pantalla que muestra parte del detalle del Pod creado para el InitContainer.

![kubectl](../images/u4_3_5.png)


<br/>

- Captura de pantalla que muestra parte del detalle del Pod creado para el Job.

![kubectl](../images/u4_3_6.png)


<br/>

- Captura de pantalla que muestra los Pods creados en el espacio de nombre practica-job-init, así como las bitacátoras del Job y de InitContainer.

![kubectl](../images/u4_3_7.png)

