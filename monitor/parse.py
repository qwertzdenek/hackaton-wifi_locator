import time
import logging

import inotify.adapters

def add_event(line, events):
    splitted = line.strip().split(",")
    if len(splitted) < 2: # empty line??
        return

    bssid = splitted[0]
    power = int(splitted[8])
    time_unix = time.mktime(time.strptime(splitted[2].strip(), "%Y-%m-%d %H:%M:%S"))
    
    # some not interesting line...
    if not bssid.contains(":"):
        return
    
    target = events[bssid]
    # create new bssid entry
    if not bssid in events:
        events[bssid] = list()
    
    events[bssid].append({"power": splitted[8], "time": time_unix})

def read_file(dump_file):
    events = dict()
    
    i = inotify.adapters.Inotify()
    i.add_watch(dump_file)

    f = open(dump_file, 'r')
    
    counter = 0
    
    try:
        for event in i.event_gen():
            if event is not None:
                (header, type_names, watch_path, filename) = event
                
                if counter == 100:
                    break
                
                if type_names[0] == 'IN_CLOSE_WRITE':
                    for line in f:
                        counter = counter + 1
                        add_event(line, events)

    finally:
        i.remove_watch(dump_file)
        f.close()
    
    return events
