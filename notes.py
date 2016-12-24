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
  vwadaptor (celery worker config changes, queue config changes, api model run start shanges,etc)
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
