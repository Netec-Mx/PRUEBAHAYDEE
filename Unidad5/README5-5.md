# Práctica 5.5. Implementación de DaemonSets para despliegues especializados

## Objetivo
- Implementar DaemonSets en Kubernetes, asegurando el despliegue de pods especializados en cada nodo o en nodos seleccionados.

## Duración aproximada
- 30 minutos.

## Instrucciones

### Paso 1. Verificar el estado de tus nodos en el clúster

Asegúrate de tener al menos dos nodos en tu clúster. Puedes verificarlo con el siguiente comando:

```bash
kubectl get nodes
```

<br/>

### Paso 2: Crear el archivo YAML para el DaemonSet

En este paso, crearás un archivo YAML para el **DaemonSet**, el cual desplegará un Pod especializado en cada nodo de tu clúster. 

Usamos un contenedor Nginx para el ejemplo, pero podrías personalizarlo según tus necesidades.


1. Crear un archivo YAML llamado `daemonset-example.yaml`.

```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: specialized-daemonset
  labels:
    app: specialized
spec:
  selector:
    matchLabels:
      app: specialized
  template:
    metadata:
      labels:
        app: specialized
    spec:
      containers:
      - name: specialized-container
        image: nginx:latest
        ports:
        - containerPort: 80
      tolerations:
      - key: "node-role.kubernetes.io/master"
        effect: "NoSchedule"

```

**Observaciones**:

1. `kind: DaemonSet`: Indica que se está creando un **DaemonSet**.

2. `template:` Define la plantilla de pods que el DaemonSet desplegará.

3. `tolerations:` Permite el despliegue en nodos con restricciones especiales, como el nodo maestro en este caso.

<br/>

### Paso 3: Aplicar el DaemonSet en el clúster

Ejecutar el siguiente comando para aplicar el archivo **daemonset-example.yaml** en el clúster:

```bash
kubectl apply -f daemonset-example.yaml
```

<br/>

### Paso 4: Verificar el estado del DaemonSet

Comprobar que el DaemonSet esté ejecutándose en todos los nodos esperados.

```bash
kubectl get daemonset specialized-daemonset

```

<br/>

### Paso 5: Verifica los Pods desplegados en cada nodo

Para asegurarte de que el **DaemonSet** está desplegando un Pod en cada nodo, ejecutar:

```bash
kubectl get pods -o wide -l app=specialized
```

Aquí podrás ver que el **DaemonSet** ha desplegado un Pod en cada nodo del clúster.

<br/>


### Paso 6: Limitar el DaemonSet a nodos específicos (opcional)

Si deseas que el DaemonSet solo se ejecute en nodos específicos (por ejemplo, solo en los nodos de trabajo), añadir una etiqueta a los nodos y configurarlo en el DaemonSet.

1. Etiquetar el nodo en el que deseas ejecutar el DaemonSet.

```bash
kubectl label nodes worker-node1 deploy=specialized
```

2. Actualizar o crear un nuevo archivo YAML **daemonset-example2.yaml** para incluir el selector de nodos:

```yaml

apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: specialized-daemonset
  labels:
    app: specialized
spec:
  selector:
    matchLabels:
      app: specialized
  template:
    metadata:
      labels:
        app: specialized
    spec:
      nodeSelector:
        deploy: specialized
      containers:
      - name: specialized-container
        image: nginx:latest
        ports:
        - containerPort: 80

```
<br/>

3. Eliminar y aplicar nuevamente el archivo YAML para actualizar el **DaemonSet**.

```bash
kubectl delete -f daemonset-example.yaml

# Si actualizaste el primer YAML, entonces cambia el nombre del archivo por `deamonset-exmple.yaml`.
kubectl apply -f daemonset-example2.yaml
```

<br/>

### Paso 7: Verifica que el DaemonSet esté solo en nodos seleccionados

Confirmar que el DaemonSet se está ejecutando solo en los nodos con la etiqueta **deploy=specialized**.

```bash
kubectl get pods -o wide -l app=specialized
```

**Nota**: Solo deberías ver Pods ejecutándose en los nodos etiquetados.

<br/>

### Conclusión

- Al completar estos pasos, habrás implementado un DaemonSet en Kubernetes para asegurar que tus pods especializados se desplieguen en cada nodo o solo en nodos específicos de tu clúster, según las necesidades de tu despliegue.

<br/><br/>

## Resultado esperado

- Captura de pantalla que muestra el estado actual del clúster, en este caso se encuentran tres Worker Node y un Node Master todos **Ready**.

![kubectl](../images/u5_5_1.png)

<br/>

- Captura de pantalla que muestra el contenido de `daemonset-example.yaml`.

![kubectl](../images/u5_5_2.png)

<br/>

- Captura de pantalla que muestra el antes y despúes de la creación del **DaemonSet**.

![kubectl](../images/u5_5_3.png)

<br/>

**Observaciones**:

1. **DESIRED: 3**, indicando que se espera que haya 3 réplicas del Pod en el clúster (uno por cada nodo).

2. **CURRENT: 3**, indicando que 3 réplicas del pod han sido creadas en total.

3. **READY: 1**, indicando que solo 1 de las 3 réplicas del pod está lista para manejar tráfico.

4. **UP-TO-DATE: 3**, indicando que las 3 réplicas están actualizadas con la última especificación del DaemonSet.

5. **AVAILABLE: 1**, indicando que solo una réplica del DaemonSet está disponible para manejar tráfico en este momento.

6. **NODE SELECTOR: <none>**, indicando que no hay un nodeSelector configurado para restringir los nodos en los que se debe ejecutar este DaemonSet.


<br/>

- Captura de pantalla que muestra como  las 3 réplicas del Pod han sido creadas  y las tres estan disponibles, 78 segundos después de su creación. Además muestras como los Pods asociados a este DaemonSet están listos y el estado de los nodos del clúster.

![kubectl](../images/u5_5_4.png)

<br/>

- Captura de pantalla que muestra los pods con la columna **LABELS**, y en la salida del segundo comando, se filtran las etiquetas **specialized**. Hasta este momento se ha implementado un DaemonSet en Kubernetes para asegurar que todos tus Pods especializados se desplieguen en cada nodo.

![kubectl](../images/u5_5_5.png)

<br/>

- Captura de pantalla que muestra el nuevo **DaemonSet** a desplegar, definido en el YAML `daemonset-example2.yaml`.


![kubectl](../images/u5_5_6.png)

<br/>

- Captura de pantalla que muestra como solo en uno de los nodos (worker2) se esta ejecutando el **DaemonSet**.  

![kubectl](../images/u5_5_7.png)

<br/>
