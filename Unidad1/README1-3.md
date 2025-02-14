# Práctica 1.3. Clonar, importar, analizar un Microservicio en Java

## Objetivo

- Clonar un repositorio de microservicio en Java, importarlo en el entorno de desarrollo y analizar su estructura y componentes principales.

## Duración aproximada

- 45 minutos.


<br/>

## Instrucciones

1. **Clonar el repositorio**

    - Acceder al repositorio inicial de las prácticas donde se encuentra el código.
    - Clonar el repositorio en la máquina local, si aún no lo has hecho, ejecutar el siguiente comando en la terminal:

    ```bash
    
    git clone <URL_DEL_REPOSITORIO>
    ```

    - Navegar al directorio de la unidad donde se encuentra el código del microservicio:

    ```bash
    
    cd <nombre_del_repositorio>/unidad1/code/ms-clients
    ```


<br/>


2. **Importar el proyecto en el entorno de desarrollo**

    - Abrir el entorno de desarrollo integrado (IDE), como STS o VSC.
    - Importar el proyecto:
        - En STS: Seleccionar File > Import..., navegar a la carpeta ms-clients y seleccionar Open as Project.

        - En VSC: Seleccionar Archivo > Abrir Carpeta..., navegar a la carpeta ms-clients y seleccionar Finish.

    - Esperar a que el IDE cargue al **100%** todas las dependencias de Maven y compilar el proyecto.



<br/>


3. **Analizar la estructura del microservicio**

    - Revisar los paquetes principales y los archivos en el proyecto. Identificar los siguientes componentes clave:

        - Modelo (entity): Revisa la clase que representa los datos principales (por ejemplo, Client).
        - Repositorio (repository): Examina la interfaz del repositorio que extiende JpaRepository u otra clase de repositorio.
        - Servicio (service): Observa la capa de lógica de negocio y los métodos implementados para gestionar la entidad.
        - Controlador (controller): Identifica los endpoints de la API que permiten interactuar con el microservicio.

    - Familiarízate con la estructura de carpetas, especialmente las carpetas src/main/java y src/main/resources.


<br/>



4. **Ejecutar y verificar la configuración**

    - Si es posible, ejecutar el microservicio desde el IDE para asegurarse de que la configuración inicial es correcta. Puedes hacerlo desde la clase principal del microservicio, generalmente una clase anotada con @SpringBootApplication.
    
    - Acceder a la consola para revisar cualquier mensaje de error o éxito que confirme la correcta inicialización del servicio.


<br/>


5. **Conclusión y análisis**

    - Reflexionar sobre los componentes y su función en la arquitectura del microservicio.
    - Anotar cualquier pregunta o área que desees profundizar en relación con el microservicio o la estructura general de una aplicación en Spring Boot. 
    - Preguntar a el instructor cualquier duda de Java.


<br/>


6. **Consumir el microservicio**

    - Usando Postman, Insomia o curl consume los endpoints de tu microservicio.

| **Operación**                | **Método HTTP** | **URL**                                      | **Descripción**                                                |
|------------------------------|-----------------|----------------------------------------------|----------------------------------------------------------------|
| Obtener todos los clientes   | `GET`           | `http://localhost:9095/api/clients`          | Devuelve una lista de todos los clientes.                       |
| Obtener un cliente por ID    | `GET`           | `http://localhost:9095/api/clients/{id}`     | Devuelve un cliente específico, identificado por su `id`.       |
| Crear un nuevo cliente       | `POST`          | `http://localhost:9095/api/clients`          | Crea un nuevo cliente con los datos proporcionados en el cuerpo de la solicitud. |
| Actualizar un cliente        | `PUT`           | `http://localhost:9095/api/clients/{id}`     | Actualiza los datos de un cliente existente, identificado por su `id`. |
| Eliminar un cliente          | `DELETE`        | `http://localhost:9095/api/clients/{id}`     | Elimina un cliente específico, identificado por su `id`.        |

 

<br/>
<br/>


## Resultado esperado

- Al analizar el microservicio, deberás encontrar carpetas similares a las siguientes:


![docker](../images/u1_3_2.png)



<br/>

- **Contestar lo siguiente**

    a. ¿Identificaste y comprendiste el propósito de cada uno de los paquetes principales, como entity, repository, service y controller, en la estructura del proyecto?

    b. ¿Pudiste explicar el flujo de datos en el microservicio, desde la capa de controlador hasta el repositorio, y cómo interactúan entre sí?

    c. ¿Entendiste la función de cada endpoint en el controlador y cómo se mapean a las operaciones CRUD en el servicio?


<br/>



- Al ejecutar el proyecto deberás de ver una imagen similar a la siguiente:


![docker](../images/u1_3_1.png)


<br/>

 
- **Contestar lo siguiente**

    a. ¿Identificaste la entidad principal, sus atributos y cómo está mapeada a la base de datos mediante JPA?

    b. ¿Cuál es el puerto asignado a este microservicio?


<br/>


- Consumo del servicio **ms-clients**.


![docker](../images/u1_3_3.png)
