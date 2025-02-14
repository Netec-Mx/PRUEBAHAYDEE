# Práctica 3.5. Configuración de Autoscaling con Horizontal Pod Autoscaler (HPA)

## Objetivo
- Escribir y aplicar un archivo YAML para configurar el escalado automático de Pods mediante Horizontal Pod Autoscaler (HPA).


## Duración aproximada
- 30 minutos.

## Instrucciones

### 1. Crear el Deployment Base

- Antes de configurar el HPA, necesitas un Deployment en el clúster de Kubernetes que se pueda escalar. Este Deployment ejecutará una aplicación que consumirá recursos y permitirá observar el autoscaling en acción.

```yaml

# deployment3.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: sample-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: sample-app
  template:
    metadata:
      labels:
        app: sample-app
    spec:
      containers:
      - name: sample-container
        image: nginx  # Usa una imagen de ejemplo, o la que prefieras.
        resources:
          requests:
            cpu: "100m"
            memory: "128Mi"
          limits:
            cpu: "200m"
            memory: "256Mi"
        ports:
        - containerPort: 80

    - Aplicar el archivo del Deployment:

```bash
 
kubectl apply -f deployment3.yaml
```

<br/>

### 2. Configurar el Horizontal Pod Autoscaler (HPA)

- El siguiente archivo YAML configura el Horizontal Pod Autoscaler para el Deployment sample-deployment. 
    
- Este HPA escalará el número de Pods en función del uso de CPU, manteniendo un promedio de uso de CPU de 50%.

```yaml

# hpa.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: sample-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: sample-deployment
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 50  # Baja este valor en tu práctica.
```


- Aplicar el archivo del HPA:

```bash

kubectl apply -f hpa.yaml
```

<br/>


### 3. Verificar el estado del HPA

- Después de configurar el HPA, puedes verificar su estado y comportamiento usando el siguiente comando:

```bash

kubectl get hpa
```


- **Nota**: Este comando mostrará los valores actuales de utilización de recursos y el número de réplicas, lo que te permitirá observar cómo el HPA ajusta la cantidad de Pods en función del uso de CPU.

<br/>

### 4. Crear un servicio

- Para exponer tus Pods del Deployment, puedes crear un servicio de tipo ClusterIP (para acceso interno en el clúster) o NodePort (para acceso externo desde la IP del nodo). 

- Por ejemplo de un archivo YAML para un servicio ClusterIP:

```YAML
# service.yaml
apiVersion: v1
kind: Service
metadata:
  name: sample-service
spec:
  selector:
    app: sample-app  # Asegúrate de que este selector coincide con las etiquetas del Deployment
  ports:
    - protocol: TCP
      port: 80        # Puerto del servicio dentro del clúster.
      targetPort: 80  # Puerto en el que el contenedor está escuchando.

```

- Aplicar el archivo para crear el servicio.

```bash

kubectl apply -f service.yaml
```

- Obtener la IP interna del servicio ejecutando el siguiente comando:

```bash

kubectl get service sample-service

```

- La **CLUSTER-IP** es la IP que usarás en el siguiente punto.

<br/>

### 5. Generar carga de prueba

- Para observar cómo el HPA escala los Pods, puedes generar una carga de prueba en el Deployment. Una forma de hacerlo es ejecutar un comando **kubectl exec** dentro de un Pod para enviar solicitudes al servicio o simular carga en la CPU.

- Por ejemplo:

```bash

kubectl run -i --tty load-generator --rm --image=busybox -- /bin/sh -c "while true; do wget -q -O- http://<service-ip>:80; done"

``` 

- **Nota**: Reemplaza `<service-ip>` con la IP del servicio o el nombre del Pod.

<br/>

### 6. Monitorear el Autoscaling


- Mientras se ejecuta la carga de prueba, usar el siguiente comando para monitorear cómo el HPA escala los Pods:

```bash
kubectl get hpa -w
``` 

- El flag **-w** permite ver los cambios en tiempo real.

<br/>

### 7. Limpiar los recursos

    - Una vez completada la práctica, eliminar los recursos creados para mantener limpio el clúster:


```bash
kubectl delete -f hpa.yaml
kubectl delete -f deployment.yaml
kubectl get pods | grep load-generator | awk '{print $1}' | xargs kubectl delete pod
```

<br/>
<br/>

## Resultado esperado

- Captura de pantalla que muestra el YAML para deployment.

![kubectl](../images/u3_5_1.png)

<br/>

- Captura de pantalla que muestra el YAML para **HorizontalPodAutoscaler**.

![kubectl](../images/u3_5_2.png)

<br/>


- Captura de pantalla que muestra el YAML para **Service**.

![kubectl](../images/u3_5_11.png)

<br/>

- Captura de pantalla que muestra el servicio creado.

![kubectl](../images/u3_5_12.png)

<br/>

- Captura de pantalla que muestra muestra el estado de los objetos en el clúster.

![kubectl](../images/u3_5_7.png)

<br/>


- Captura de pantalla que muestra como aplicar y verificar **Metrics**. 

![kubectl](../images/u3_5_8.png)

<br/>

- Captura de pantalla que muestra los porcentajes de CPU del deployment, observa como no se puede apreciar el escalamiento, debido a que la carga generada no rebasa el 50% de la métrica.

![kubectl](../images/u3_5_9.png)

<br/>


- Captura de pantalla que muestra los Pods *load-generator*.

![kubectl](../images/u3_5_10.png)

<br/>
