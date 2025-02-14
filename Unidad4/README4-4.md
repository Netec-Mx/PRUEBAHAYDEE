# Práctica 4.4. Implementación de Probes & Policies en Pods

## Objetivo
- Configurar Liveness y Readiness Probes, y Restart Policies en Pods.


## Duración aproximada

- 20 minutos.

## Instrucciones

### Paso 1. Crear el archivo YAML del Pod

- Crear un archivo YAML llamado **pod-probes-policies.yaml**, donde definas un Pod con las siguientes configuraciones:

    - **Liveness Probe**: Configura una sonda de liveness para verificar que la aplicación sigue funcionando. Esto ayudará a Kubernetes a reiniciar el contenedor si el **probe** falla.

    - **Readiness Probe**: Configurar el **readiness** para verificar que el contenedor está listo para recibir tráfico.

    - **Restart Policy**: Configurar una política de reinicio. Se ha movido al nivel de spec del Pod, fuera de la sección de containers. Esto asegura que Kubernetes aplique la política de reinicio al Pod completo y no a cada contenedor individual.

<br/>

### Paso 2. Escribir el YAML del POd con Probes y políticas de reinicio

- Este archivo YAML define un Pod que contiene un contenedor ejecutando una aplicación web simple en el puerto **8080**. 

- Configurar los probes de liveness y readiness, y establecer la política de reinicio.

```yaml
aapiVersion: v1
kind: Pod
metadata:
  name: pod-probes-policies
  labels:
    app: probe-example
spec:
  restartPolicy: Always         # Política de reinicio en caso de fallo del contenedor.
  containers:
  - name: probe-container
    image: nginx:latest
    ports:
    - containerPort: 8080
    livenessProbe:
      httpGet:
        path: /
        port: 8080
      initialDelaySeconds: 10      # Esperar antes de iniciar el primer probe de liveness.
      periodSeconds: 5             # Intervalo de tiempo entre cada probe.
      failureThreshold: 3          # Número de fallos consecutivos para marcar el contenedor. como no saludable.
    readinessProbe:
      httpGet:
        path: /
        port: 8080
      initialDelaySeconds: 5       # Esperar antes de iniciar el primer probe de readiness.
      periodSeconds: 3             # Intervalo de tiempo entre cada probe.
      failureThreshold: 2          # Número de fallos consecutivos para marcar el contenedor. como no listo.

```

**Notas:** 

1. **Liveness Probe**:

    - **httpGet**: Realiza una solicitud HTTP al endpoint / en el puerto 8080. Si el contenedor no responde en este endpoint, se considerará que está inactivo.

    - **initialDelaySeconds**: Especifica el tiempo en segundos que Kubernetes esperará antes de enviar el primer probe de liveness después de que el contenedor se inicie.

    - **periodSeconds**: Define el tiempo en segundos entre cada prueba.

    - **failureThreshold**: Después de 3 fallos consecutivos en el liveness probe, Kubernetes reiniciará el contenedor.


2. **Readiness Probe**:

    - Configura un probe de readiness que verifica si el contenedor está listo para recibir tráfico. Esto es importante en los despliegues, ya que previene que el tráfico sea dirigido a contenedores que aún no están listos.

    - **httpGet**, **initialDelaySeconds**, **periodSeconds**, y **failureThreshold** funcionan igual que en el liveness probe, pero específicamente para el readiness.

    - 
3. **Restart Policy**:

    - La política de reinicio Always asegura que el contenedor se reinicie automáticamente cada vez que falle.

<br/>

### Paso 3. Aplicar el archivo YAML

- Para crear el Pod en Kubernetes, utilizar el siguiente comando en la terminal:

    ```bash
    kubectl apply -f pod-probes-policies.yaml
    ```


<br/>

### Paso 4. Verificar la configuración del Pod

- Puedes verificar el estado de los probes con el siguiente comando, el cual muestra detalles sobre el estado de los probes en el Pod:

    ```bash
    kubectl describe pod pod-probes-policies

    ```

- **Nota**:

    - Este comando te permitirá ver si los probes están funcionando correctamente y si la política de reinicio está aplicándose en caso de fallo.

<br/>


### Paso 5. Monitorear el Pod

- Para observar cómo los probes afectan al Pod en tiempo real, usar el siguiente comando:

    ```bash
    kubectl get pod pod-probes-policies -w


    ```

- **Notas**:

    - Esta instrucción mantendrá un seguimiento en tiempo real de los eventos del Pod. Verás si el contenedor se reinicia o cambia de estado según el estado de los probes y la política de reinicio.

    - Para observar el comportamiento de los probes en el Pod, es una buena práctica generar un escenario donde los probes fallen y así ver cómo Kubernetes responde.

<br/>

### Paso 6. Estresar el contenedor para provocar fallos en los Probes

Para que el liveness y/o el readiness probe fallen, puedes simular que el contenedor se cuelga o deja de responder temporalmente. Algunas ideas de cómo hacerlo:

#### Opción A. Simulación mediante la modificación de la configuración del contenedor (con kubectl exec)

1. **Abrir una sesión interactiva en el contenedor**

```bash
kubectl exec -it pod-probes-policies -- /bin/sh
```

<br/>

2. **Interrumpir el servicio que responde en el puerto 8080**

    - Una vez dentro, puedes detener el proceso del servidor web de nginx en el contenedor (si es un entorno donde puedes hacerlo). Por ejemplo:


    ```bash
    pkill nginx
    ```
 

    - **Nota**:
    
        - Esto hará que el contenedor deje de responder en el puerto 8080, lo que provocará que el liveness probe falle, y Kubernetes intentará reiniciar el contenedor.

<br/>

3. **Cerrar la sesión interactiva**:

    - Al terminar, simplemente cerrar la sesión con `exit`.

<br/>


#### Opción B. Simulación de un alto consumo de recursos en el contenedor.

1. **Puedes simular una alta carga de CPU o memoria dentro del contenedor:**

```bash
kubectl exec -it pod-probes-policies -- /bin/sh -c "while true; do :; done"
```

- **Nota**:

    - Este ciclo de **while true** consume CPU y puede provocar que el contenedor se vuelva menos responsivo, causando que los **probes de liveness** y **readiness** detecten problemas en el servicio.

2. **Verificar el Comportamiento del Pod**

    - Volver a observar la salida de `kubectl get pod pod-probes-policies -w` para ver cómo responde Kubernetes. 

    - Si los probes detectan que el contenedor ha fallado, verás que Kubernetes reiniciará el contenedor según la política de reinicio.

<br/>

### Paso 7: Finalizar el Stress Test

Al terminar de observar el comportamiento de los probes y el reinicio, puedes detener el stress test cerrando la sesión interactiva o eliminando el Pod:

```bash
kubectl delete pod pod-probes-policies
```
 
<br/><br/>

## Resultado esperado

<br/>

- Captura de pantalla que muestra el antes y después de la creación del Pod **pod-probes-policies**.

![docker -run hello-world](../images/u4_4_1.png)


<br/>

- Captura de pantalla que muestra parter del despliegue detallado del Pod **pod-probes-policies**.

![docker -run hello-world](../images/u4_4_2.png)

<br/>

- Captura de pantalla que muestra los estados del Pod **pod-probes-policies**.

![docker -run hello-world](../images/u4_4_3.png)


- **Nota**:

    - **Running**: El contenedor intenta ajustarse.

    - **ErrImagePull**: Error al intentar descargar la imagen del contenedor.

    - **CrashLoopBackOff**: Kubernetes detecta fallos continuos y espera antes de intentar reiniciar el contenedor nuevamente.




