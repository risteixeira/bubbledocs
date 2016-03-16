# Bubble Docs SD-STORE


-------------------------------------------------------------------------------

## Serviço SD-STORE 

### Instruções de instalação 


[0] Iniciar sistema operativo 

Linux


[1] Iniciar servidores de apoio

JUDDI:
startup.sh

[2] Criar pasta temporária

> git clone cria a pasta do projecto automaticamente. cd A_26_41_42

[3] Obter versão entregue

> git clone -b SD-STORE_R_1 https://github.com/tecnico-softeng-distsys-2015/A_26_41_42-project.git


[4] Construir e executar **servidor**

> cd A_26_41_42/sd-store mvn package mvn exec:java


[5] Construir **cliente**


> cd A_26_41_42/sd-store-cli mvn package mvn exec:java


-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes**


> cd A_26_41_42/sd-store-cli mvn test

[2] Executar ...

cd A_26_41_42/sd-store-cli




-------------------------------------------------------------------------------
**FIM**