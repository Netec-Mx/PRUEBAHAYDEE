# Práctica 4.5 App Spring Boot

## Objetivo
- Desplegar una aplicación Spring Boot en K8s.


## Duración aproximada
- 40 minutos.

## Instrucciones

### Paso 1. Configuración del espacio de nombres

Crear un **Namespace** para organizar y aislar los recursos asociados a esta práctica.

1. Crear un nuevo directorio de trabajo en el nodo maestro, por ejemplo, **ws2**

2. Crear un archivo YAML llamado `namespace.yaml`.

```yaml 
apiVersion: v1
kind: Namespace
metadata:
  name: springboot-app-ns  
```

**Nota:** El nombre del namespace es solo una sugerencia; puedes elegir y utilizar el nombre que prefieras.

3. Aplicar el YAML para crear el Namespace.

```bash
kubectl apply -f namespace.yaml
```

4. Verificar la creación del Namespace.

```bash
# Lista los namespaces
kubectl get namespaces

# Información básica del namespace
kubectl get namespace <namespace>

# Información detallada del namespace
kubectl describe namespace <namespace>
```

<br/>

### Paso 2. Crear el ConfigMap

Definir un **ConfigMap** para almacenar las variables de configuración de la aplicación.

1. Crear un archivo YAML llamado **configmap.yaml**.

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: springboot-app-config
  namespace: springboot-app-ns
data:
  SPRING_DATASOURCE_URL: "jdbc:h2:mem:testdb"
  SPRING_DATASOURCE_USERNAME: "sa"
  SPRING_DATASOURCE_PASSWORD: ""
  DK_PARTICIPANTE: "nombre"
```

2. Aplicar el archivo YAML para crear el **ConfigMap**.

```bash
kubectl apply -f configmap.yaml
```

3. Verificar la creación del **ConfigMap**.

```bash
kubectl describe configmap springboot-app-config -n springboot-app-ns

# Detalles del ConfigMap.

kubectl get configmap springboot-app-config -n springboot-app-ns -o yaml

```

<br/>

### Paso 3. Definir el Deployment con dos réplicas

El deployment especificará que deseas tener dos réplicas de la aplicación, _distribuidas en dos Pods_.

1. Crear un archivo YAML llamado `deployment.yaml`.

```yaml

apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-app-deployment
  namespace: springboot-app-ns
spec:
  replicas: 2
  selector:
    matchLabels:
      app: springboot-app
  template:
    metadata:
      labels:
        app: springboot-app
    spec:
      containers:
        - name: springboot-app
          image: <dockerhub-username>/<springboot-app>:<versión>  # Reemplaza los datos correspondientes a tu imagen en Docker Hub
          ports:
            - containerPort: <puerto-imagen-Docker> # Reemplaza el puerto expuesto en la aplicación de tu imagen en Docker Hub
          envFrom:
            - configMapRef:
                name: springboot-app-config

```


- **Nota**:  Reemplaza `<dockerhub-username>/<springboot-app>:<versión>` con la imagen en tu cuenta en Docker Hub. Por ejemmplo la imagen creada en la práctica 1.6.


2. Aplicar el archivo YAML para crear el **Deployment**.

```bash
kubectl apply -f deployment.yaml
```

3. Verificar los detalles completos del Deployment en Kubernetes.

```bash
# Muestra un resumen del estado del Deployment espacificado en el Namespace indicado.
kubectl get deployment <deploment-name> -n <namespace>

# Proporciona detalles completos del Deployment
kubectl describe deployment <deployment-name> -n <namespace>

# Devuelve la definición completa del Deployment en formato YAML
kubectl get deployment <deployment-name> -n <namespace> -o yaml
```


**Nota**: Sustituye `<deployment-name>` por el nombre de tu Deployment y `<namespace>` por el nombre del namespace donde se encuentran los componentes de Kubernetes.

<br/>

### Paso 4. Exponer el Deployment con un Service

Permitir el acceso a la aplicación desde dentro del clúster, crear un Service de tipo **ClusterIP**.

1. Crear un archivo YAML llamado `service.yaml`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: springboot-app-service
  namespace: springboot-app-ns
spec:
  selector:
    app: springboot-app
  ports:
    - protocol: TCP
      port: <puerto>  # Puerto del Service accesible para otros componentes del clúster.
      targetPort: <puerto-imagen-Docker> # Puerto en el contenedor donde corre la aplicación
  type: ClusterIP
```

2. Aplicar el archivo YAML para crear el Service.

```bash
kubectl apply -f service.yaml
```

3. Verificar el servicio creado.

```bash
# Forma abreviada
kubectl get svc -n <namespace>

# Forma completa
kubectl get services -n <namespace>
```

<br/>

### Paso 5. Verificar el despliegue

1. Asegúrate de que los Pods estén en ejecución:

```bash
kubectl get pods -n <namespace>
```

2. Verificar que el servicio esté creado correctamente:

```bash
kubectl get svc -n <namespace>
```

3. Validar los todos objetos creados en un espacio de nombres específico, puedes utilizar el siguiente comando de Kubernetes.

```bash
kubectl get all -n <namespace>
```

**Nota**: Reemplaza <namespace> por el nombre de tu namespace, por ejemplo, `springboot-app-ns`.

<br/>

### Paso 6. Consumir el servicio

Para consumir el servicio utilizando `curl` o `wget` desde dentro del clúster de Kubernetes, puedes emplear la dirección IP del clúster (CLUSTER-IP) y el puerto expuesto (PORT) del servicio. En el caso de la documentación de esta práctica, el servicio `springboot-app-service` tiene asignada la dirección IP **10.111.232.187** y expone el puerto **8095**.

Realiza pruebas de consumo desde ambos nodos, tanto el nodo maestro (Master Node) como el nodo trabajador (Worker Node).

**Notas:**

1. ¿Lograste consumir el servicio? ¿Pudiste hacerlo desde ambos nodos (Master Node y Worker Node)? Explica las razones detrás del éxito o la dificultad al realizar el consumo desde cada nodo.

2. A continuación te damos algunas formas de consumir el servicio de tipo CLUSTER-IP.
   

<br/>

#### Opción A. Ejecutar el curl/wget dentro del clúster.

- Si ejecutas curl desde otro Pod en el mismo clúster, puedes hacer lo siguiente:


    ```bash
    curl http://<ip-servicio>:<puerto-servicio>/<path-app>

    # En la documentación de la práctica quedaron los valores siguientes
    curl http://10.111.232.187:8095/api/clients
    
    ```

<br/>

#### Opción B. Usar el nombre del servicio en lugar de la IP.

- En Kubernetes, los servicios pueden ser alcanzados por su nombre.

   ```bash

    curl http://<nombre-del-servicio><puerto-del-servicio>/<app-path>

    # En la documentación de la práctica quedaron los valores siguientes:
    curl http://springboot-app-service:8095/api/clients
   ```

<br/>

#### Opción C. port-forward para consumir desde la máquina local.

- Si deseas acceder al servicio desde la máquina local, utilizar el comando **port-forward** para mapear el puerto del servicio en la máquina:

```bash
kubectl port-forward svc/<nombre-del-servicio> <puerto-imagen-Docker>:<puerto-servicio> -n <namespace>

# En la documentación de la práctica quedaron los valores siguientes:
kubectl port-forward svc/springboot-app-service <puerto-especificado>:8095 -n springboot-app-ns
```

- Luego, puedes consumir el servicio con `curl` o con `wget` en la máquina local:

```bash

wget http://localhost:<puerto-servicio>/<api-path>

# En la documentación de la práctica quedaron los valores siguientes:
wget http://localhost:<puerto-especificado>/api/clients
```

**Notas:**
1. Estas opciones te permitirán consumir el servicio utilizando `curl` o `wget`, adaptándose al entorno de trabajo en el que estés operando.  
2. La respuesta del servicio dependerá de la API que hayas incluido en tu imagen Docker. Si utilizaste la API sugerida en la práctica, es posible que la respuesta no contenga datos aún.
3. El comando `kubectl port-forward` se utiliza para redirigir el tráfico desde un puerto local en tu máquina al puerto de un recurso (Pod o Service) en el clúster de Kubernetes.  

    ```bash
    kubectl port-forward [resource-type/resource-name] [local-port]:[remote-port] -n <namespace>
    ```
    
    a. **Puerto local (`local-port`)**:  
       - Es el puerto en tu máquina desde donde se redirigirán las solicitudes. 
       - Por ejemplo, si especificas `8085`, puedes acceder al recurso en el clúster mediante `http://localhost:8085`.
    
    b. **Puerto remoto (`remote-port`)**:  
       - Es el puerto en el recurso (Pod o Service) en el clúster al que se redirigirá el tráfico desde tu máquina.  
       - Este puerto debe coincidir con el puerto en el que el recurso (por ejemplo, un Pod) está escuchando solicitudes.

  
<br/>

### Paso 7. Limpieza

- Para limpiar todos los recursos creados durante esta práctica en Kubernetes, la forma más sencilla es eliminar el espacio de nombres donde se definieron los objetos. Esto asegurará que todos los recursos asociados dentro de ese espacio de nombres sean eliminados automáticamente, evitando la necesidad de gestionarlos individualmente.

```bash
# Eliminación del namespace.
kubectl delete namespace <namesapce>

# Otra alternativa
kubectl delete -f namespace.yaml

# Nombre sugerido en la práctica.
kubectl delete namespace springboot-app-ns

# Verificación.
kubectl get namespaces
```

<br/><br/>

## Resultado esperado

- Captura de pantalla que incluye lo siguiente: 

1. El contenido del archivo YAML utilizado para crear el namespace.  
2. La aplicación del archivo YAML mediante el comando `kubectl apply`.  
3. La verificación de que el namespace se ha creado correctamente utilizando `kubectl get namespaces`.  
4. La comprobación de que el namespace recién creado no contiene objetos aún, mediante `kubectl get all -n <nombre-del-namespace>`.  

![kubectl](../images/u4_5_1.png)

<br/><br/>


- Captura de pantalla que incluye lo siguiente:

1. Muestra el archivo que define el ConfigMap, incluyendo sus claves y valores.  
2. Evidencia el uso del comando `kubectl apply -f <archivo-yaml>` para crear el ConfigMap en Kubernetes.  
3. Usa el comando `kubectl describe configmap <nombre-del-configmap> -n <namespace>` para visualizar los detalles del ConfigMap recién creado, como sus datos y metadatos asociados.  

![kubectl](../images/u4_5_2.png)

<br/><br/>

- La captura de pantalla siguiente:

1. Muestra el archivo YAML que define el Deployment. Debe incluir las especificaciones básicas, como el nombre del Deployment, las réplicas, la imagen del contenedor, los puertos, etc.

2. Demuestra el uso del comando `kubectl apply -f <archivo-yaml>` para desplegar el Deployment en Kubernetes.

3. Muestra el resultado del comando `kubectl get deployments -n <namespace>` donde se observa que:
     - La columna **READY** muestra `2/2` indicando que las dos réplicas están listas.
     - La columna **UP-TO-DATE** indica `2` confirmando que las dos réplicas están actualizadas.
     - La columna **AVAILABLE** indica `2`, validando que las dos réplicas están disponibles y funcionando correctamente.

![kubectl](../images/u4_5_3.png)

<br/><br/>

- Captura de pantalla que muestra los detalles del Deployment creado en el punto anterior, obtenidos mediante el comando: 

```bash
kubectl describe deployment spring-app-deployment -n springboot-app-ns
```

- La captura incluye información detallada de los eventos, las especificaciones del Deployment, y el estado actual de los Pods asociados.

![kubectl](../images/u4_5_4.png)

<br/><br/>

- Captura de pantalla que incluye lo siguiente:

1. **Contenido del archivo YAML**: Muestra la definición del Service, incluyendo el tipo de servicio (ClusterIP, NodePort, LoadBalancer), el selector que apunta a los Pods, y los puertos expuestos.

2. **Aplicación del archivo YAML**: Evidencia el uso del comando `kubectl apply -f <archivo-yaml>` para crear el Service en el clúster.

3. **Verificación del Service**:
   - Utiliza el comando `kubectl get svc <nombre-del-servicio> -n <namespace>` para confirmar su creación.
   - La salida incluye el **nombre del servicio**, la **ClusterIP**, y el **puerto expuesto**, demostrando que está correctamente configurado y disponible para su uso.
   
   
![kubectl](../images/u4_5_5.png)

<br/><br/>

- La captura de pantalla que incluye lo siguiente:

1. **Comando para crear nuevamente el servicio**: Muestra el uso del comando `kubectl apply -f <archivo-yaml>` para reiterar la creación del servicio, reforzando que el archivo YAML está configurado correctamente.

2. **Salida del comando `kubectl get svc`**:
   - Incluye el nombre del servicio, tipo, IP del clúster, puerto expuesto y edad del servicio para verificar su existencia y disponibilidad.

3. **Detalles del servicio con `kubectl describe`**:
   - Evidencia la información completa del servicio, como:
     - **Nombre del servicio**: Confirma que corresponde al creado.
     - **ClusterIP y puertos**: Verifica los valores configurados.
     - **Endpoints**: Muestra los Pods asociados al servicio.
     - **Eventos**: Asegúrate de que no haya errores o advertencias relacionadas.

![kubectl](../images/u4_5_6.png)

<br/><br/>

- Captura de pantalla que incluye lo siguiente:

1. **Contenido del JSON**: Muestra las propiedades del objeto que será insertado, especificando los campos y valores que se enviarán al servicio.

2. **Verificación de Pods**: Utiliza el comando `kubectl get pods -n <namespace>` para confirmar la cantidad y los nombres de los Pods que están en ejecución y asociados al servicio.

3. **Detalles del Servicio**: Usa `kubectl describe svc <nombre-del-servicio> -n <namespace>` para mostrar los detalles de conexión, como la dirección IP, el puerto expuesto y los endpoints asociados.

4. **Consumo del servicio**: Muestra el resultado de ejecutar el comando `curl` para consumir el servicio y validar que la operación (como la inserción del objeto) se realizó correctamente.

![kubectl](../images/u4_5_7.png)

<br/><br/>

- La captura de pantalla siguiente incluye lo siguiente:

1. **Contenido del JSON del segundo objeto**: 
   - Muestra la estructura y propiedades del segundo objeto que será insertado en el servicio, asegurando que incluye todos los campos requeridos.

2. **Ejecución del comando para insertar el segundo objeto**: 
   - Evidencia el uso de un comando como `curl -X POST` o similar, que envía el objeto al servicio utilizando su endpoint correspondiente.

3. **Verificación de los objetos insertados**:
   - Muestra el uso de un comando como `curl -X GET <url-del-servicio>` para consultar todos los objetos insertados, verificando que tanto el primero como el segundo objeto se encuentren en la respuesta del servicio. 
   - Opcionalmente, se utiliza un formato limpio (e.g., `jq`) para mejorar la legibilidad de la salida.
   
![kubectl](../images/u4_5_8.png)

<br/><br/>

- La siguiente captura de pantalla evidencia las inconsistencias que surgen al trabajar con réplicas de servicios que gestionan una base de datos en memoria. Las peticiones realizadas al servicio muestran cómo las respuestas varían entre las réplicas debido al balanceo de carga. 

- **Consumo del endpoint `/api/clients`**: Se observa que las respuestas cambian en contenido dependiendo de la réplica que atiende cada solicitud. Por ejemplo:
  - En algunas respuestas, los datos devueltos incluyen un cliente con `id=1` y `name=Nombre1`.
  - En otras respuestas, el cliente con `id=2` aparece sin incluir al cliente anterior, indicando que las réplicas no comparten un estado consistente.

- **Demostración del balanceo de carga**: A pesar de que todas las peticiones son enviadas al mismo endpoint (`http://10.111.232.187:8095/api/clients`), el balanceador de carga distribuye las solicitudes entre las distintas réplicas del servicio, cada una con su propia base de datos en memoria.

- **Conclusión**: La falta de persistencia centralizada en una base de datos compartida provoca que cada réplica tenga un conjunto de datos distinto, lo que genera inconsistencias en las respuestas al cliente. Este comportamiento destaca la importancia de diseñar estrategias adecuadas de persistencia en arquitecturas con múltiples réplicas.

![kubectl](../images/u4_5_9.png)

<br/><br/>

- Captura de pantalla que muestra el consumo del servicio desde un Pod temporal, utilizando el comando **wget** y **springboot-app-service** en lugar de la IP.

![kubectl](../images/u4_5_10.png)

<br/><br/>


- Captura de pantalla que ilustra cómo consumir un servicio en Kubernetes utilizando la funcionalidad de `port-forward`, lo que permite acceder al servicio a través de `http://localhost`.

1. **Configuración de `port-forward`**:  
   - Se utiliza el comando:
     ```bash
     kubectl port-forward svc/springboot-app-service 8095:8095 -n springboot-app-ns
     ```
     Esto redirige las solicitudes locales en el puerto `8095` al puerto del servicio `springboot-app-service` en el clúster, haciendo que el servicio sea accesible desde `http://localhost:8095`.

2. **Consumo del servicio**:  
   - Se realizan varias solicitudes a los endpoints del servicio usando `curl`, como:
     ```bash
     curl http://localhost:8095/api/clients/1
     ```
     y 
     ```bash
     curl http://localhost:8095/api/clients/2
     ```
     Las respuestas devueltas muestran los datos correspondientes de la base de datos.

3. **Observaciones**:  
   - El comando `port-forward` facilita probar servicios dentro de Kubernetes desde el entorno local sin necesidad de exponerlos externamente.
   - Las solicitudes realizadas a `http://localhost` interactúan directamente con el servicio a través del puerto redirigido.

**Conclusión**: Este enfoque es útil para pruebas y depuración, ya que permite interactuar con servicios del clúster como si estuvieran ejecutándose localmente.

 

![kubectl](../images/u4_5_11.png)

<br/><br/>

- Captura de pantalla que muestra un listado completo de todos los archivos YAML y JSON utilizados en esta práctica.


![kubectl](../images/u4_5_12.png)

<br/><br/>

- Captura de pantalla que muestre el proceso completo de limpieza de los recursos creados en el namespace utilizado durante la práctica.

![kubectl](../images/u4_5_13.png)

<br/><br/>
