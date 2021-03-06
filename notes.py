# get length of the queue ( which is also a redis item):
----------------get queue length------------------------------
from redis import Redis
redis = Redis(host='workerdb', port=6379, db=0)
print redis.llen('rentedQueue')
print redis.llen('celery')

----------------------read and write frrom redis-----------------------------------------
p = redis.pipeline()
p.set('MaxRentedModelsAllowed', 3200)
p.set('CurrentRentedModels', 200)
p.execute()
MaxRentedModelsAllowed = redis.get('MaxRentedModelsAllowed')
CurrentRentedModels = redis.get('CurrentRentedModels')
redis.delete('CurrentRentedModels')

------------------------create a new queue---------------------------------------
# if you want different queue dynamically,
process_link.apply_async(args=[link1],
                         queue=queue1)
process_link.apply_async(args=[link2],
                         queue=queue2)
#Also you have to insert following in configuration file
CELERY_CREATE_MISSING_QUEUES = True
#But one thing you have to care about is when starting worker, you have to pass -Q 'queue name' as argument, for consuming from that queue 'queuename' 
celery -A proj worker -l info -Q queue1,queue2

------------------------------------------------------------------------
the changes are made in vwadaptor repositroy which is saved at desktop
repository:
  vwadaptor (celery worker config changes, queue config changes, api model run start shanges,serializer.py for tak_id, etc)
  taskmanager
  
-----------------changes made in api for new queue--------------------------------------------------------

@blueprint.route("/<int:id>/start",methods=['PUT'])
@jwt_required()
@modelrun_authorization_required
def start(id):
    modelrun = ModelRun.query.get(id)
    if modelrun:
      if modelrun.progress_state==PROGRESS_STATES['NOT_STARTED']:
        schemas = load_schemas()
        schema = schemas[modelrun.model_name]
        needed_resources = set(schema['resources']['inputs'].keys())
        available_resources = set([r.resource_type for r in modelrun.resources])
        if needed_resources==available_resources:
          modelrun.progress_state = PROGRESS_STATES['QUEUED']
          # modelrun = modelrun.update()
          
          from redis import Redis
          redis = Redis(host='workerdb', port=6379, db=0)
          # # # default_queue_length = queue_length()
          default_queue_length = int(redis.llen('celery'))
          maxRentedModelsAllowed = int(redis.get('MaxRentedModelsAllowed'))
          currentRentedModels = int(redis.get('CurrentRentedModels'))

          if default_queue_length>0 and (currentRentedModels < maxRentedModelsAllowed):
            task_id = celery.send_task('vwadaptor.run', args=[], kwargs={'modelrun_id':modelrun.id}, queue='rentedQueue')
            currentRentedModels = currentRentedModels+1
            p = redis.pipeline()
            p.set('CurrentRentedModels', currentRentedModels)
            p.execute()
          else:
            task_id = celery.send_task('vwadaptor.run', args=[], kwargs={'modelrun_id':modelrun.id})


          # task_id = celery.send_task('vwadaptor.run', args=[], kwargs={'modelrun_id':modelrun.id})
          modelrun.task_id = str(task_id)
          modelrun = modelrun.update()
          return jsonify({'message':'ModelRun submitted in queue','modelrun':modelrun_serializer(modelrun)}), 200
        else:
          error = {'message':"ModelRun {0} Doesn't have the necessary resources attached".format(modelrun),'missing':list(needed_resources-available_resources)}
          return jsonify(error), 400
      else:
        error = {'message':PROGRESS_STATES_MSG[modelrun.progress_state].format(modelrun_id=modelrun.id)}
        return jsonify(error), 400
    else:
      err = {"message":"ModelRun {0} Not Found".format(id)}
      return jsonify(err), 404
-----------------------------------------------------------------------------------------------------------------
backup folder : desktop/seams_rentedQueue_backup
 if you change anything inside /api/<id>/start, error may come "JSON coldnot be decode error"
after making any change, 
remove '/vwstorage' and  refresh model dashboard page..
if error, then go to home page and reach model page..
if now also error, remove '/vwstorage' and remove all containers(first 5 is enough) and try...
-----------------------------------------------------------------------------------------------------------------
  
Feb 16 Thursaday:
for matplotlib to genereate graphs in docker container (taskmanager), edit the following file:

The permanent way to solve this kind of problem is to edit .matplotlibrc file. Find it via

>>> import matplotlib
>>> matplotlib.matplotlib_fname()
# This is the file location in Ubuntu
'/etc/matplotlibrc'

Then modify the backend in that file to backend : Agg. That is it.

http://stackoverflow.com/questions/2801882/generating-a-png-with-matplotlib-when-display-is-undefined
==================================================================+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  
  
