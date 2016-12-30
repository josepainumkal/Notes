------------works with celery: shows in active workers when created using dockerpy ---------------------------------------------------------------------------------


import docker
client = docker.APIClient(base_url='unix://var/run/docker.sock')
print client.version()
def create_worker_container(container_name):
    container_envs = docker.utils.parse_env_file('/var/www/taskmanager/container_env.txt')
    links=[('postgres-modeldb', 'modeldb'),('postgres-userdb', 'userdb'),('redis-workerdb', 'workerdb')]
    binds={"/vwstorage": "/vwstorage"}
    volumes = ['/vwstorage']

    volume_bindings = {
        '/vwstorage': {
            'bind': '/vwstorage',
            'mode': 'rw',
        },
    }

    host_config = client.create_host_config(
        binds=volume_bindings,
        links=links
        # port_bindings = port_bindings
    )

    networking_config = client.create_networking_config({
        'my-net': client.create_endpoint_config(
            ipv4_address='10.0.9.0/24',
            # aliases=['foo', 'bar'],
            links=links
        )
    })

    container = client.create_container(
        # image='virtualwatershed/vwadaptor',
        image='josepainumkal/vwadaptor:jose_toolUI',
        environment=container_envs,
        stdin_open=True,
        tty=True,
        # command='celery -A worker.modelworker worker -Q rentedQueue --loglevel=info --autoreload --logfile=/celery.log -n '+ container_name,
        command='celery -A worker.modelworker worker -Ofair --loglevel=info --autoreload --logfile=/celery.log -n '+ container_name,
        name=container_name,
        volumes=volumes,
        host_config=host_config,
        networking_config = networking_config
        # host_config=create_host_config(port_bindings={2424:2425})
        # host_config=docker.utils.create_host_config(binds=binds)
    ) 
 
    response = client.start(container=container.get('Id'))
create_worker_container('worker-sumi')

------------To connect to remote docker host-------------------------------------------------------------------------------------------------------------------

docker run -d -p 80:2375 --net=my-net --env="constraint:node==mhs-demo0" -v /var/run/docker.sock:/var/run/docker.sock jarkt/docker-remote-api
client = docker.APIClient(base_url='http://<ip of above container>:2375', tls=False)

---------rsync without entering password------------------------------------------------------------------------------------------------------------------------------------------

sshpass -p "tcuser" rsync -a -e "ssh" --rsync-path="sudo rsync" /vwstorage/ docker@192.168.99.101:/vwstorage/












