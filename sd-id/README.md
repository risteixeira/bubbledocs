# Projecto de Sistemas Distribuídos #

## Primeira entrega ##

Grupo de SD 42

Pedro Fialho 75713 pedro_fialho1994@hotmail.com

Dharu Queshil 75669 dharu.q@gmail.com

Bruno Almeida 69827 almeidaa.10@hotmail.com


Repositório:
[tecnico-softeng-distsys-2015/A_26_41_42-project](https://github.com/tecnico-softeng-distsys-2015/A_26_41_42-project/)


-------------------------------------------------------------------------------

## Serviço SD-ID

### Instruções de instalação 
*(Como colocar o projecto a funcionar numa mÃ¡quina do laboratÃ³rio)*

[0] Iniciar sistema operativo

Sistema operativo é indiferente


[1] Iniciar servidores de apoio

JUDDI:
> startup.sh (linux) ou
startup.bat (windows)

[2] Criar pasta temporária

> cd ~
> mkdir A_26_41_42

[3] Obter versão entregue

> git clone -b SD-ID_R_1 https://github.com/tecnico-softeng-distsys-2015/A_26_41_42-project/ A_26_41_42


[4] Construir e executar **servidor**

> cd A_26_41_42/sd-id
> mvn clean package 
> mvn exec:java


[5] Construir **cliente**

> cd ../sd-id-cli
> mvn clean package
> mvn exec:java



-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes** ...

> cd ../sd-id-cli
> mvn test




-------------------------------------------------------------------------------
**FIM**
