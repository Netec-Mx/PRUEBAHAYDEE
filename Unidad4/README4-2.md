# Práctica 4.2. Creación de Secrets en K8s

## Objetivo
- Crear Secrets en Kubernetes utilizando YAML.


## Duración aproximada
- 15 minutos.

## Instrucciones

### 1. Definir el Secren en YAML

-  Crear un archivo YAML llamado **my-secret.yaml** en el cual se definirá el Secret. A continuación, se muestra un ejemplo de cómo definir un Secret con datos de **usuario** y **contraseña** codificados en base64.

```yaml

apiVersion: v1
kind: Secret
metadata:
  name: my-secret
  namespace: default
type: Opaque
data:
  username: dXNlcm5hbWU= # "username" en base64
  password: cGFzc3dvcmQ= # "password" en base64

```

<br/>

### 2. Codificar los datos confidenciales en base64

- Utilizar el siguiente comando para convertir los valores de username y password en base64. En este ejemplo, cambia "username" y "password" por tus valores reales.

```bash
echo -n 'username' | base64
echo -n 'password' | base64
```

- Copiar los valores codificados y pegarlos en el archivo **my-secret.yaml** en lugar de `dXNlcm5hbWU=` y `cGFzc3dvcmQ=`.

<br/>

### 3. Aplicar el Secret al clúster

- Usar el comando `kubectl apply` para crear el Secret en el clúster de Kubernetes:

```bash
kubectl apply -f my-secret.yaml
```

- Verificar que el Secret se haya creado correctamente ejecutando:

```bash
kubectl get secrets my-secret -o yaml
```

<br/>

### 4. Acceder a los datos del Secret en un Pod.

- Para utilizar el Secret en un Pod, debes hacer referencia al Secret en el archivo YAML del Pod. 



```yaml
apiVersion: v1
kind: Pod
metadata:
  name: secret-test-pod
spec:
  containers:
    - name: my-container
      image: nginx
      env:
        - name: SECRET_USERNAME
          valueFrom:
            secretKeyRef:
              name: my-secret
              key: username
        - name: SECRET_PASSWORD
          valueFrom:
            secretKeyRef:
              name: my-secret
              key: password
 
```

- Guardar este archivo como **secret-test-pod.yaml** y aplicarlo con el siguiente comando:

```bash
kubectl apply -f secret-test-pod.yaml
```

<br/>


### 5. Verificar que el Secret sea accesible en el Pod.

- Conectarse al Pod y verificar que las variables de entorno estén configuradas correctamente:

```bash
kubectl exec -it secret-test-pod -- env | grep SECRET_
```

<br/>

### 6. Limpieza (Opcional)

- Una vez finalizada la práctica, eliminar los recursos creados:


```bash
kubectl delete -f my-secret.yaml
kubectl delete -f secret-test-pod.yaml
```

## Resultado esperado

<br/>

- Captura de pantalla que muestra el contenido del YAML, **my-secret.yaml**, y coo username es igual a username y password es igual a password. Los valores se generaron en Base64.

![kubectl](../images/u4_2_1.png)


<br/>

- Captura de pantalla que muestra  el antes y después de crear un **Secrect**, los valores son mostrados en Base64.

![kubectl](../images/u4_2_2.png)

<br/>

- Captura de pantalla que muestra el YAML con la creación de un POD que usa las propiedades puestas en el Secret. Además como se crea el Pod.

![kubectl](../images/u4_2_3.png)

<br/>

- Captura de pantalla que muestra la verificación de las propiedades del **Secret** en el Pod. Además muestra la limpieza de los componentes creados en la práctica.

![kubectl](../images/u4_2_4.png)
