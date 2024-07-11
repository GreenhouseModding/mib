## Content
- Added `/mib play` command. Which allows users to play Mib instruments at a specific note. 
  Full usage of the command is:
  - `/mib play <soundSet> <note> <octave> <volume> <length> <targets> pos/entity <origin>`
- Removed `fade_start` field from data. This will now be handled automatically.
  - Copper Goat Horns and Flutes can now be played to any length.

## Miscellaneous
- Allowed notes to be case insensitive in data and commands.
- NeoForge module built off NeoForge 21.0.83-beta.
- Renamed all references to "key" to "note" in code, as that is the correct name.