import httplib
import faker
import json
import os
import random

REST_API_HOST='localhost'
REST_API_PORT=10180
REST_API_URL='/gemfire-api/v1/event'
EVENT_TYPE = 'io.pivotal.pde.sample.Event'
count = 10000

headers = dict()
headers['Content-Type'] = 'application/json'

if __name__ == '__main__':
   fake = faker.Factory.create()
   
   conn = httplib.HTTPConnection(REST_API_HOST,REST_API_PORT)
   try:      
      #conn.request('DELETE',REST_API_URL, None, headers )
      #resp = conn.getresponse()
      #if resp.status != 200:
      #   raise Exception('An error occurred while clearing the event region - REST API returned {0} {1}'.format(resp.status, resp.reason))
      
      #resp.read()
      
      #print 'cleared event region'
      
      for i in range(count):
         json_event = dict()
         dt = fake.date_time_between(start_date="-1y", end_date="now")
         json_event['@type'] = EVENT_TYPE
         json_event['date'] = '{0:4d}-{1:02d}-{2:02d}'.format(dt.year, dt.month, dt.day)
         json_event['type'] = random.choice(['A','B','C','D','E'])
         json_event['state'] = fake.state_abbr()
         json_event['count'] = random.randint(0,99)
         
         jsonStr = json.dumps(json_event, indent=3)
         print 'PUTTING' + os.linesep + jsonStr 
         conn.request('POST',REST_API_URL + '?key={0:06d}'.format(i), jsonStr, headers)
         resp = conn.getresponse()
         if resp.status != 201:
            raise Exception('An error occurred while putting event - REST API returned {0} {1}'.format(resp.status, resp.reason))
         
         resp.read()
         
   finally:
      conn.close()
   
