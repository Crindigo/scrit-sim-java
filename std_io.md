Version 1.2.0 adds support for communicating over standard i/o, using the --iocontrol option when starting the jar.

The input works like the following:

Send `design` to start inputting a design in the simulator's save format.
Send `.` to finish inputting the design.
Send `sim X` to simulate for X steps. It outputs lines for each fuel and coolant type as `consume TYPE AMT` and `generate TYPE AMT`.
Send `q` to quit.

Save format explanation:
Line 1: Always `scd 1` unless it needs a new version.
Line 2: Two numbers, diameter and depth.
Lines 3+: The X position, Y position, and component ID. Empty positions are omitted.

Component IDs:
fuel_rod_leu235_dioxide
fuel_rod_haleu235_dioxide
fuel_rod_heu235_dioxide
fuel_rod_mox_susy
fuel_rod_bismuth
coolant_boiling
coolant_pressurized
coolant_pressurized_heavy
moderator_graphite
moderator_beryllium
control_rod
control_rod_moderated

Sample session:
```
$ java -jar scrit-simulator-1.2.0.jar --iocontrol
design
scd 1
5 7
0 1 coolant_boiling
0 3 coolant_boiling
1 0 coolant_boiling
1 1 fuel_rod_leu235_dioxide
1 2 coolant_boiling
1 3 fuel_rod_leu235_dioxide
1 4 coolant_boiling
2 1 control_rod
2 2 control_rod
2 3 coolant_boiling
3 0 coolant_boiling
3 1 fuel_rod_leu235_dioxide
3 2 coolant_boiling
3 3 fuel_rod_leu235_dioxide
3 4 coolant_boiling
4 1 coolant_boiling
4 3 coolant_boiling
.
sim 21600
consume leu235Dioxide 6.313726761014383
generate hp_wet_steam 3127.2947222222224
sim 604800
consume leu235Dioxide 180.21198519896714
generate hp_wet_steam 3127.4069444444444
q
```
