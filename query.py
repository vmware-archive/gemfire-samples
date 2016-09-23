import os
import os.path
import subprocess
import sys

LOCATOR='localhost[10000]'

if __name__ == '__main__':
   for required in ['JAVA_HOME', 'GEMFIRE']:
      if required not in os.environ:
         sys.exit('please set the {0} environment variable'.format(required))

   JAVA=os.path.join(os.environ['JAVA_HOME'],'bin','java')
   HERE=os.path.dirname(os.path.abspath(sys.argv[0]))
   TARGET=os.path.join(HERE,'queryevents','target')
   MAIN_JAR=os.path.join(TARGET,'queryevents-1.0-SNAPSHOT.jar')
   DEPENDENCIES=os.path.join(TARGET,'dependency','*')
   CLASSPATH=os.pathsep.join([MAIN_JAR,DEPENDENCIES])
   
   MAIN_CLASS='io.pivotal.pde.sample.QueryEvents'
   
   CMD = [JAVA,'-cp', CLASSPATH, MAIN_CLASS]
   if len(sys.argv) > 1:
      CMD = CMD + sys.argv[1:]
      
   #print ' '.join(CMD) + '\n'
   subprocess.call(CMD)
