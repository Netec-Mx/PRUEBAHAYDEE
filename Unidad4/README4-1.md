# Práctica 4.1. Configuración de ConfigMaps & variables de entorno

## Objetivo 
- Al finalizar esta práctica, serás capaz de crear y gestionar ConfigMaps a partir de archivos y variables de entorno en Kubernetes.


## Duración aproximada
- 15 minutos.

## Instrucciones

### Paso 1. Crear un ConfigMaps desde un archivo de configuración

#### 1. Crear un archivo de configuración

- Crear un archivo de configuración con las variables de entorno que quieras almacenar en el ConfigMap. Usa el siguiente contenido de ejemplo y guárdalo como **app-config.properties**:

```plaintext
DATABASE_URL=jdbc:mysql://localhost:3306/mydb
DATABASE_USER=root
DATABASE_PASSWORD=abcd2357
APP_ENV=development
```

#### 2. Crear el ConfigMap desde el archivo

- Usar el siguiente comando para crear el ConfigMap en Kubernetes a partir del archivo **app-config.properties**:

```bash
kubectl create configmap app-config --from-file=app-config.properties
```

#### 3. Verificar el ConfigMap

- Confirmar que el ConfigMap fue creado correctamente con el siguiente comando:

```bash
kubectl get configmap app-config -o yaml
```

<br/>

### Paso 2. Crear un ConfigMap desde variables de entorno directamente en el comando

- Puedes crear un ConfigMap a partir de valores de **clave-valor** sin necesidad de un archivo intermedio. Usar el siguiente comando para crear otro ConfigMap con variables definidas en línea:

```bash
kubectl create configmap inline-config --from-literal=APP_MODE=debug --from-literal=LOG_LEVEL=info
```

<br/>

### Paso 3. Configurar un Pod que utilice los ConfigMaps

1. **Escribir un archivo YAML para el Pod**

- Este archivo YAML describe un Pod que usa las variables de los ConfigMaps como variables de entorno. Guardar como **pod-configmap.yaml**:


```yaml
apiVersion: v1
kind: Pod
metadata:
  name: app-pod
spec:
  containers:
    - name: app-container
      image: nginx
      envFrom:
        - configMapRef:   # Vinculación del ConfigMap
            name: app-config
        - configMapRef:   # Vinculación del ConfigMap
            name: inline-config
      env:
        - name: CUSTOM_ENV  # Una variable más en el YAML
          value: "custom_value"
  restartPolicy: Never

```


2. **Aplicar el Pod en el clúster**

- Usar el siguiente comando para aplicar el archivo YAML y crear el Pod:


```bash
kubectl apply -f pod-configmap.yaml
```

3. **Verificar el Pod**

- Verificar que el Pod esté creado y en ejecución:

```bash
kubectl get pod app-pod
```

4. **Verificar las variables de entorno dentro del contenedor**

- Puedes ingresar al contenedor y verificar que los variables de entorno están configuradas:


```bash
kubectl exec -it app-pod -- env
```

- La salida de este comando debería mostrar las variables de entorno configuradas desde los ConfigMaps y la variable **CUSTOM_ENV**.


<br/>

### Paso 4. Actualizar el ConfigMap

Si necesitas cambiar alguna variable en el **ConfigMap**, puedes editarlo directamente o eliminarlo y recrearlo. A continuación, se muestra cómo editarlo en línea:


1. **Editar el ConfigMap**

- Usar el siguiente comando para editar el ConfigMap *inline-config* y agregar o cambiar las variables según sea necesario.


```bash
kubectl edit configmap inline-config
```

2. **Verificar que el Pod refleje los cambios**

```bash
kubectl delete pod app-pod
kubectl apply -f pod-configmap.yaml
```


<br/>

### Paso 5. Limpieza (Opcional)

- Para eliminar los recursos creados, utilizar los siguientes comandos:


```bash
kubectl delete pod app-pod
kubectl delete configmap app-config
kubectl delete configmap inline-config
```


## Resultado Esperado

<br/>

- Captura de pantalla que muestra el archivo con algunas propiedades, como crear un ConfigMap a partir de un archivo de propiedades y verificar la creación del ConfigMap.

![kubectl](../images/u4_1_1.png)


<br/>

- Captura de pantalla que muestra los ConfigMaps creados, la creación del Pod, verificación del estado del Pod, y verificación si el container en el Pod tiene las variables configuradas.

![kubectl](../images/u4_1_2.png)


<br/>

- Captura de pantalla que muestra el YAML para la creación del Pod que utiliza **configMapRef** y además agrega una nueva propiedad.

![kubectl](../images/u4_1_4.png)


<br/>

- Captura de pantalla que muestra la busqueda específica de las propiedades agregadas al ConfigMap.

![kubectl](../images/u4_1_5.png)




