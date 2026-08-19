# Home-Zone-Checker
In Classs Activity
Group number : 20 
Task 2
IM/2023/135 has done project setup  and UI part
IM/2023/065 has done Distance /zone logic
IM/2023/105 has done feature/permission
IM/2023/127 has done location provider
IM/2023/003 has done last testing part

Test cases are :
1. The app requests location permission.
2. The app retrieves the current emulator/device location using Fused Location Provider.
3. The app has a hardcoded campus latitude and longitude.
4. The app uses Location.distanceTo() to calculate distance.
5. The app uses a 200-meter radius.
6. It displays "Inside Zone" when distance <= 200m.
7. It displays "Outside Zone" when distance > 200m.
8. It displays the calculated distance in meters.