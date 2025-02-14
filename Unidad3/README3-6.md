# Práctica 3.6. YAML

## Objetivo
- Crear y gestionar múltiples recursos en Kubernetes, incluyendo Deployment, ResourceQuota, límites y solicitudes de recursos, HPA, VPA y varios tipos de Services.


## Duración aproximada

- 40 minutos.

## Instrucciones

### Paso 1. YAML

- Crear un archivo YAML llamado **multi-resource.yaml** y copiar el siguiente contenido en él. Este archivo definirá múltiples recursos en Kubernetes, incluyendo un Deployments, ResourceQuotas, HPA, y varios tipos de Services.

```YAML
#  
apiVersion: v1
kind: Namespace
metadata:
  name: my-namespace
---
#  
apiVersion: v1
kind: ResourceQuota
metadata:
  name: my-resource-quota
  namespace: my-namespace
spec:
  hard:
    requests.cpu: "500m"
    requests.memory: "512Mi"
    limits.cpu: "1000m"
    limits.memory: "1024Mi"
---
#  
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
  namespace: my-namespace
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-container
        image: nginx
        resources:
          requests:
            cpu: "200m"
            memory: "256Mi"
          limits:
            cpu: "500m"
            memory: "512Mi"
---
#  
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-hpa
  namespace: my-namespace
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-deployment
  minReplicas: 2
  maxReplicas: 5
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 50
---

# 
apiVersion: v1
kind: Service
metadata:
  name: my-clusterip-service
  namespace: my-namespace
spec:
  selector:
    app: my-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: ClusterIP
---
#  
apiVersion: v1
kind: Service
metadata:
  name: my-nodeport-service
  namespace: my-namespace
spec:
  selector:
    app: my-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
      nodePort: 30007
  type: NodePort
---
#  
apiVersion: v1
kind: Service
metadata:
  name: my-loadbalancer-service
  namespace: my-namespace
spec:
  selector:
    app: my-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: LoadBalancer
---
#  
apiVersion: v1
kind: Service
metadata:
  name: my-externalname-service
  namespace: my-namespace
spec:
  type: ExternalName
  externalName: example.com

``` 

<br/>

### Paso 2. Kubectl apply

- Aplicar el archivo YAML en tu clúeste de Kubernetes.

```yaml
kubectl apply -f multi-resource.yaml
``` 

<br/>

### Paso 3. Cuestionario

1. ¿Cuántos Pods están en ejecución para el Deployment my-deployment?

2. ¿Cuáles son los límites y solicitudes de recursos (CPU y memoria) establecidos para los Pods del Deployment my-deployment?

3. ¿Cuáles son los límites de CPU y memoria establecidos por la ResourceQuota en el namespace?

4. ¿Cuánto CPU y memoria está utilizando actualmente el Deployment en comparación con los límites de la ResourceQuota?

5. ¿Cuál es el rango mínimo y máximo de réplicas configurado para el HPA en my-deployment?

6. ¿Cuál es el objetivo de utilización de CPU establecido para el HPA?

7. ¿Qué recomendaciones de recursos (CPU y memoria) sugiere el VPA para los Pods de my-deployment? 

8. ¿Cuál es la IP asignada al Service my-clusterip-service y desde qué otros Pods del clúster se puede acceder a este servicio?

9. ¿Cuál es el puerto externo asignado por Kubernetes para el Service my-nodeport-service?

10. ¿Tiene el Service my-loadbalancer-service una IP pública asignada? ¿Cuál es esta IP?

11. ¿Qué tipo de balanceo de carga proporciona este Service?

12. ¿Hacia qué dominio apunta el Service my-externalname-service y cómo funciona este tipo de servicio?

<br/><br/>
## Resultado Esperado


- Captura de pantalla que muestra el posible estado inicial, esto es antes de la práctica, y los elementos que se aplicarán a la práctica.  

![kubectl](../images/u3_6_1.png)

<br/>

- Captura de pantalla que muestra algunas caracteristicas iniciales del YAML, como la cantidad de lineas, las primeras 10 y las últimas 10 líneas del archivo YAML.

![kubectl](../images/u3_6_2.png)

<br/>


- Captura de pantalla que muestra como eliminar todos los componentes creados del YAML, los componenetes que se aplicarán y la salida al alpicar el YAML al clúster de Kubernetes.

![kubectl](../images/u3_6_3.png)

<br/>

- Captura de pantalla que muestra los espacios de nombres y todos los componentes de Kubernetes asociados a un espacio de nombre específico.

![kubectl](../images/u3_6_4.png)

<br/>




