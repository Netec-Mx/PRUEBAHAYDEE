# Práctica 5.2. Implementación y gestión de Namespaces en Kubernetes

## Objetivo
- Implementar Namespaces en Kubernetes para organizar y gestionar recursos dentro del clúster de forma eficaz.



## Duración aproximada
- 20 minutos.


## Instrucciones

### 1. Verificar Namespaces existentes

El siguiente comando mostrará los Namespaces por defecto:

```bash
kubectl get namespaces
```

1. **default**: Este es el Namespace por defecto donde se crean los recursos si no se especifica explícitamente otro Namespace. Es el espacio de nombres "general" para recursos que no necesitan un Namespace especial.


2. **kube-system**: Este Namespace contiene los recursos y Pods que utiliza el sistema de Kubernetes, como los controladores y servicios esenciales del clúster. Es importante evitar hacer cambios en este Namespace, ya que contiene los componentes críticos de Kubernetes.

3. **kube-public:** Este Namespace es de lectura pública para todos los usuarios del clúster, incluso los no autenticados. Por convención, se utiliza para recursos que deben ser accesibles globalmente en el clúster.

4. **kube-node-lease**: Este Namespace contiene objetos Lease, que son utilizados por el sistema de Kubernetes para monitorear la disponibilidad y estado de los nodos. Esto ayuda a mejorar la eficiencia del control de salud de los nodos dentro del clúster.

<br/>

### 2. Crear un nuevo Namespace

Crear un nuevo Namespace llamado **development**.

```bash
kubectl create namespace development

# Verificar el namespace creado correctamente.
kubectl get namespaces

# Verificar el namespace con el atajo.
kubectl get ns
```

<br/>

### 3. Crear un Deployment en el Namespace

Especificar el Namespace en el que quieres crear recursos. Aquí, crearemos un Deployment en el Namespace development.

- Primero, crear un archivo YAML llamado `nginx-development.yaml`.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  namespace: development
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:latest
        ports:
        - containerPort: 80

```

- Aplicar este archivo para desplegar el Deployment en el Namespace **development**.

```bash
kubectl apply -f nginx-deployment.yaml

# Verifica. 
kubectl get deployments -n development
```

<br/>

### 4. Crear un servicio en el Namespace

- Crear un Service para exponer el Deployment en el Namepace **development**.

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
  namespace: development  # Espacio de nombre creado previamente.
spec:
  selector:
    app: nginx
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: ClusterIP

```

- Aplicar este archivo para crear el servicio en el espacio de nombres especificado.

```bash
kubectl apply -f nginx-service.yaml

# Verificar.
kubectl get services -n development
```


- Lista todos los objetos dentro del Namespace.

```bash
kubectl get all -n development

kubectl get configmaps,secrets -n development

```

<br/>

## Resultado Esperado

- Captura de pantalla que muestra los Namespaces creados hasta este momento, los predeterminados (default, kube-node-lease. kube-public y kube-system) y algunos otros creados de prácticas pasadas. Ademas se muestra todos los objetos/recursos creado en un Namespace específico.

![kubectl](../images/u5_2_1.png)

<br/>

- Captura de pantalla que muestra los Namespaces actuals, la creación de un Namespace llamado **development** de forma _imperativa_ y su YAML asociado.

![kubectl](../images/u5_2_2.png)

<br/>

- Captura de pantalla que muestra el contenido de un YAML para crear un Deployment, la creación del deployment y la verificación de la creación correcta del deployment.

![kubectl](../images/u5_2_3.png)

<br/>

- Captura de pantalla que muestra el YAML asociado a un servicio, la creación del servicio y la verificación de la creación correcta del servicio.

![kubectl](../images/u5_2_4.png)

<br/>

- Captura de pantalla que muestra los Namespaces hasta este momento creados y los recursos en el espacio de nombres específico (2 Pods, 1 servicio, 1 deployment y 2 replicaset).

![kubectl](../images/u5_2_5.png)

<br/>
