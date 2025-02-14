# Práctica 5.4. Escribiendo y configurando YAML para Ingress Controller

## Objetivo
- Crear archivos YAML para el Ingress Controller en Kubernetes, configurando reglas de tráfico y rutas específicas para acceder a servicios dentro del clúster.


## Duración aproximada

- 30 minutos.

## Prerrequisitos

- Haber realizado la **Práctica 5.3 Configuración de Kubernetes Ingress**.
<br/>

## Instrucciones

## Paso 1. Eliminar rutas actuales

1. Dado que actualmente tienes configurados Hosts separados para cada aplicación (app1.example.com & app2.example.com).

2. Eliminar todas las rutas de Ingress asociadas a los servicios app1-service y app2-service.

<br/>

## Paso 2. Actualizar la configuración de Ingress

1. Crear un nuevo YAML del Ingress para incluir los nuevos paths pada host: `app1.example.com` y `app2.example.com`. Llamar al archivo `multi-hosts-ingress.yaml`, _en esta ocasión tu tendrás que crear el contenido del YAML_.

    - Redirige `http://app1.example.com/app1` al servicio app1-servicio y `http://app2.example.com/app2` al servicio app2-servicio  

2. Aplica el YAML que creaste en el punto anterior.

<br/>

## Paso 3. Probar las nuevas rutas

1. Utilizar los comandos `curl` para verificar las rutas actualizadas.

    - Verificar la ruta /app1 en app1.example.com

    ```bash
    curl -H "Host: example.com" http://<ip-master-node>:<puerto>/app1
    ```

    - Verificar la ruta /app2 en app2.example.com

    ```bash
    curl -H "Host: example.com" http://<ip-master-node>:<puerto>/app2
    ```

<br/>
<br/>

## Resultado esperado

- Capturas de pantallas que muestran la configuración previa.

1. Configuración previa a los cambios de Ingress.

![kubectl](../images/u5_4_1.png)

<br/>


2. Detalles específicos de un Ingress particular.


![kubectl](../images/u5_4_3.png)

<br/>

3. Detalles específicos de un servicio particular.


![kubectl](../images/u5_4_4.png)

<br/>


- Captura de pantalla que muestra como se encuentran los paths en el consumo con la configuración previa a aplicar el nuevo YAML.

![kubectl](../images/u5_4_5.png)

<br/>

- Captura de pantalla que muestra la aplicación del YAML solicitado en la práctica.

![kubectl](../images/u5_4_6.png)

<br/>

- Captura de pantalla que muestra el consumo exitoso de las rutas solicitadas, y como las anteriores rutas (las de la práctica previa ya no funcionan).

![kubectl](../images/u5_4_8.png)

