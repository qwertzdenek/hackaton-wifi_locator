package com.rychlici.wifilocator.backend;

import com.rychlici.wifilocator.common.Location;
import com.rychlici.wifilocator.common.Scan;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocationComp {
    private int[] channelFreq = {(int) 2412e6, (int) 2417e6, (int) 2422e6, (int) 2422e6, (int) 2427e6,
            (int) 2432e6, (int) 2437e6, (int) 2442e6, (int) 2447e6, (int) 2452e6, (int) 2457e6,
            (int) 2462e6, (int) 2467e6, (int) 2472e6, (int) 2484e6, };

    private HashMap<String, Location> apLoc = new HashMap<>();

    public LocationComp() {
        apLoc.put("84:24:8D:C6:C2:30", new Location(0.0, 0.0, 0.0));
        apLoc.put("84:24:8D:C6:EB:A0", new Location(2.75, 7.2, 0.0));
        apLoc.put("84:24:8D:C6:C4:E0", new Location(5.5, 0.0, 0.0));
        apLoc.put("84:24:8D:C3:81:A0", new Location(8.25, 7.2, 0.0));
        apLoc.put("84:24:8D:C6:BE:D0", new Location(11.0, 0.0, 0.0));
    }

    public double getDistance(Scan s) {
        return Math.pow(10.0, (s.getSignalLoss() - 20.0*Math.log(channelFreq[s.getChannel()]) + 147.55) / 20.0);
    }

    public HashMap<String, Double> getDistances(Collection<Scan> scans) {
        HashMap<String, Double> distances = new HashMap<>();

        for (Scan s : scans) {
            Double dist = distances.get(s.getBssid());
            if (dist == null) {
                distances.put(s.getBssid(), getDistance(s));
            } else {
                distances.put(s.getBssid(), dist + (getDistance(s) - dist) / (distances.size() + 1));
            }
        }

        return distances;
    }

    public Location getLocation(Collection<Scan> scans) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        HashMap<String, Double> distances = this.getDistances(scans);

        // skip unknown APs and make sum
        double sum = 0;
        for (Map.Entry<String, Double> ap : distances.entrySet()) {
            if (!apLoc.containsKey(ap.getKey())) {
                distances.remove(ap.getKey());
            }

            sum += ap.getValue();
        }

        // normalize
        for (Map.Entry<String, Double> ap : distances.entrySet()) {
            Double val = ap.getValue();
            distances.put(ap.getKey(), val / sum);
        }

        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            Location currentAPLoc = apLoc.get(entry.getKey());

            x += currentAPLoc.getX() * entry.getValue();
            y += currentAPLoc.getY() * entry.getValue();
            z += currentAPLoc.getZ() * entry.getValue();
        }

        return new Location(x, y, z);
    }
}
