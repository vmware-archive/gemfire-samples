import os
import os.path
import subprocess
import sys

LOCATOR='localhost[10000]'

if __name__ == '__main__':
   for required in ['JAVA_HOME', 'GEMFIRE']:
      if required not in os.environ:
         sys.exit('please set the {0} environment variable'.format(required))
         
   GFSH = os.path.join(os.environ['GEMFIRE'],'bin','gfsh')
   commands = sys.argv[1:]
   subprocess.check_call([GFSH, '-e', 'connect --locator="{0}"'.format(LOCATOR), '-e', ' '.join(commands)])   