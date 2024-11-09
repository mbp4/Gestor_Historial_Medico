Link al repositorio: https://github.com/mbp4/Gestor_Historial_Medico.git

Participantes: Miriam Blanco Ponce, Sira González-Madroño, Adrián Thierry Puyo Olías y Sonia Tejero Recio

## Proyecto

En el proyecto se nos pedia realizar una aplicación qque manejará de manera segura los historiales médicos de usuarios y que estos puedan ser accedidos por sus correspodientes médicos, la aplicación se compone de: 

  -> Pantalla de Login

  -> Pantalla con los datos del paciente

  -> Pantalla para el médico

  -> Pantalla para editar los detalles del paciente

  -> Pantalla con los datos del paciente para el médico

## Uso del programa

Lo primero que se le solicitará al usuario será iniciar sesión, donde tendrá la opción de registrarse o de iniciar sesión con un usuario, los usuarios ya almacenados en la base de datos serían: 

  -> Alice

  -> Charlie

  -> Bob

Donde los dos primeros son pacientes y el ultimo es médico y las contraseñas para todos será "1234"

En caso de querer registrar un nuevo usuario, ya sea medico o paciente, se le ofrecerá una pantalla dentro de la misma del loggeo, como un pop up, donde podrá registrar su usuario, contraseña y elegir su "modalidad".

En el proyecto se nos solicitaba guardar los datos de manera segura, por lo tanto utilizaremos firebase, una base de datos en la cual podemoste hacer funciones que cifren los datos, como podrían ser las contraseñas mediante un hash.

Lo siguiente será que aparezcan los datos de los pacientes, esto podrá ser de dos maneras: 

  -> En el caso del médico este podrá navegar haciendo uso de un spinner y elegir el paciente del que quiera saber los detalles. 

  -> En el caso del paciente tendrá toda su información con la posibilidad de cambiarla manualmente para añadir alguna información adicional. En caso de que se modifique la información el usuario deberá salir de la aplicacion y volver a entrar para ver que los cambios se han guardado de manera correcta. 


## Programa 

Dentro del programa nos encontramos cada una de las actividades con sus correspondientes layout, pero a parte nos encontramos con dos dataClass: 

  -> User: data class que contiene los datos que tendrá un usuario (usuario, contraseña y rol). 

  -> Patient: data class que contiene los datos de un usuario con rol de paciente (id, nombre, apellido, edad, alergias, enfermedades y operaciones).
