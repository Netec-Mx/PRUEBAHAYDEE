# Práctica 3.4. Escribiendo YAML de ResourceQuota

## Objetivo

- Escribir y aplicar un archivo YAML que defina una ResourceQuota en un namespace, limitando el consumo de recursos como CPU y memoria, para mejorar la gestión de recursos en el clúster.


## Duración aproximada

- 30 minutos.


## Instrucciones

1. **Crear un Namespace:**

    - Definir y crear un nuevo namespace en el clúster que servirá para aplicar la cuota de recursos. El nombre del namespace debe ser claro y relacionado con su propósito, por ejemplo, `limite-recursos`.

<br/>

2. **Definir la ResourceQuota:**

    - Escribir un archivo YAML para la configuración de la `ResourceQuota`.

    - La configuración debe incluir límites tanto para el uso de **CPU** como de **Memory**.

    - Establecer las restricciones de recursos de forma que, dentro de este namespace, los pods no puedan exceder los límites establecidos.

<br/>

3. **Parámetros a configurar:**


    - Incluir en la ResourceQuota los siguientes parámetros:

        - **CPU**: Define el límite de CPU en millicores (por ejemplo, 1000m para un núcleo completo).

        - **Memory**: Define el límite de memoria en MiB o GiB (por ejemplo, 512Mi).

    - Establecer tanto el límite máximo de recursos como el mínimo si deseas garantizar recursos mínimos para los pods en este namespace.

<br/>

4. **Aplicar la configuración**:

    - Utilizar el comando kubectl apply -f <nombre_del_archivo>.yaml para aplicar el archivo YAML en el clúster.

    - Verificar que la ResourceQuota se haya creado correctamente en el namespace especificado.

<br/>

5. **Validación de la ResourceQuota**:

    - Confirmar que la cuota de recursos se haya aplicado correctamente en el namespace usando `kubectl describe resourcequota <nombre_de_la_resourcequota> -n <namespace>`.

    - Revisar que los límites se apliquen cuando intentes crear recursos que excedan las restricciones establecidas.

Al finalizar, revisar los resultados y anotar tus observaciones sobre cómo se comportan los pods cuando alcanzan los límites definidos en la **ResourceQuota**.

<br/>

## Resultado esperado

- Cuando ejecutas el comando para crear el **test-pod** con recursos que exceden los límites definidos en la **ResourceQuota**, deberías ver una salida de error similar a la siguiente:


```text
Error from server (Forbidden): pods "test-pod" is forbidden: exceeded quota: limite-de-recursos, requested: requests.cpu=600m, requests.memory=300Mi, limits.cpu=1500m, limits.memory=1Gi, used: requests.cpu=0, requests.memory=0, limits.cpu=0, limits.memory=0, limited: requests.cpu=500m, requests.memory=256Mi, limits.cpu=1000m, limits.memory=512Mi
```

- **Observaciones**:

    - **Error from server (Forbidden)**: Indica que la creación del pod ha sido rechazada debido a una restricción de permisos basada en la ResourceQuota.

    - **exceeded quota: limite-de-recursos**: Señala que el recurso ResourceQuota llamado limite-de-recursos en el namespace limite-recursos está imponiendo el límite.

    - **requested**: Muestra los recursos solicitados por el pod (cpu=600m, memory=300Mi para requests y cpu=1500m, memory=1Gi para limits).

    - **used**: Indica los recursos ya en uso en este namespace. Si no hay otros pods, estos valores deberían ser 0.

    - **limited**: Especifica los valores máximos permitidos por la ResourceQuota para requests y limits.

Esta salida confirmaría que los límites en la **ResourceQuota** están funcionando como se espera, evitando que el Pod se cree con configuraciones que exceden el consumo permitido de CPU y memoria en el namespace limite-recursos.
