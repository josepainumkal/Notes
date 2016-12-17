import requests
import json
import os 
from requests_toolbelt import MultipartEncoder


token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6MSwiaWF0IjoxNDgxNjY3Mzc3LCJuYmYiOjE0ODE2NjczNzcsImV4cCI6MTQ4NDI1OTM3N30.UTUjBQu9s0RHK2xxDFGYRjwtOoRRb4boXUpxGsz6m1Q'
payload = {'description': 'test_description', 'model_name': 'prms', 'title': 'test01'}
headers={'Content-Type':'application/json', 'Authorization': 'JWT %s' % token}

r = requests.post(url="http://192.168.99.100:5000/api/modelruns", json=payload, headers=headers)
resp_dict = json.loads(r.content)
modelrun_id = resp_dict['id']

print r.status_code, r.reason
print resp_dict['id']

# File Upload
payload = {'resource_type':'input'}
upload_headers={'Authorization': 'JWT %s' % token}
file_upload_url = 'http://192.168.99.100:5000/api/modelruns/'+str(modelrun_id)+'/upload'
control_file_name = '1-month_input/LC.control'
controlfile = {'file': open(control_file_name, 'rb')}
data_file_name = '1-month_input/data.nc'
datafile = {'file': open(data_file_name, 'rb')}
param_file_name = '1-month_input/parameter.nc'
paramfile = {'file': open(param_file_name, 'rb')}

r = requests.post(url=file_upload_url, data={'resource_type':'control'}, headers=upload_headers, files=controlfile)
print r.status_code, r.reason  
print r.content
r = requests.post(url=file_upload_url, data={'resource_type':'data'}, headers=upload_headers, files=datafile)
print r.status_code, r.reason  
print r.content
r = requests.post(url=file_upload_url, data={'resource_type':'param'}, headers=upload_headers, files=paramfile)
print r.status_code, r.reason  
print r.content

# run model
modelrun_url = 'http://192.168.99.100:5000/api/modelruns/'+str(modelrun_id)+'/start'
modelrun_headers={'Authorization': 'JWT %s' % token}
r = requests.put(url=modelrun_url, headers=modelrun_headers)
print r.status_code, r.reason  
print r.content

##delete model 
# modelrun_id = 148
# modelrun_headers={'Content-Type':'application/json','Authorization': 'JWT %s' % token}
# deletemodel_url= 'http://192.168.99.100:5000/api/modelruns/'+str(modelrun_id)
# r = requests.delete(url=deletemodel_url, headers=modelrun_headers)
# print r.status_code, r.reason  
# print r.content
