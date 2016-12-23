# get length of the queue ( which is also a redis item):

from redis import Redis
redis = Redis(host='workerdb', port=6379, db=0)
print redis.llen('rentedQueue')

# if you want different queue dynamically,

process_link.apply_async(args=[link1],
                         queue=queue1)
process_link.apply_async(args=[link2],
                         queue=queue2)

#Also you have to insert following in configuration file

CELERY_CREATE_MISSING_QUEUES = True

#But one thing you have to care about is when starting worker, you have to pass -Q 'queue name' as argument, for consuming from that queue 'queuename' 

celery -A proj worker -l info -Q queue1,queue2





