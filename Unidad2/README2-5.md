# Práctica 2.5. Ejercicios Básicos K8s

## Objetivo
- Aplicar comandos y configuraciones básicas en Kubernetes para gestionar y monitorear recursos como Pods, Namespaces y Deployments.


## Duración aproximada
- 30 minutos.

## YAMLs

- podsP3.yaml

```yaml
```

## Instrucciones

- Realizar los siguientes ejercicios:

# Cuestionario de Kubernetes - Unidad 2

1. **Listar la cantidad de Pods que se encuentran dentro del namespace `kube-system`.**

2. **Listar todos los Pods que se encuentren dentro del clúster.**

3. **Crear un Pod de manera imperativa que use la imagen `ubuntu:20`.**

4. **Usando la CLI de Kubernetes, generar el YAML que te permita crear un Pod que use la imagen `alpine`.**

5. **Usando YAML, crear un Pod que use la imagen `node:18.17-slim`.**

6. **Crear un namespace con el formato `<nombre_compañía_abreviado>-dev`.**

7. **Desplegar un ReplicaSet de nombre `dev-ntc` en el nuevo namespace que use la imagen `alpine` y tenga 3 réplicas.**

8. **Eliminar un Pod del ReplicaSet `dev-ntc` y explicar qué sucede.**

9. **Crear un Deployment de nombre `backend` de 4 réplicas que use la imagen `python:3.11` y que exponga el puerto `8888`.**

10. **Realizar los cambios necesarios para que todos los Pods del Deployment `backend` estén en estado "Running".**


## Resultado esperado
