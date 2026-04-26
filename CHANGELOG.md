# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 3.0.5 - 2026-02-15

### Added

- Support for Create Fabric 6.0.7+ (1.20.1)

### Fixed

- Null issue with config showing on startups

## 3.0.4 - 2025-11-23

### Fixed

- Hotfix for contraption assembly for functional copycats

## 3.0.3 - 2025-11-23

### Fixed

- Incompatibility with Create 6.0.7 and 6.0.8

## 3.0.2 - 2025-06-14

### Fixed

- X-ray exploit with copycat ghost blocks
- Crash due to an API update in Neoforge causing null pointer exceptions during CT connectivity checks
- Error in schematics due to attempts to update light in schematic levels
- Crash due to changes in placement assist rendering in Create 6.0.6

## 3.0.1 - 2025-05-31

### Added

- Support for Create 6.0.x in Minecraft 1.21.1

### Fixed

- Placement assist of copycat ladders affecting vanilla ladders
- Copycat slopes having incorrect hitboxes
- Copycats not emitting light when a light source is used as material (1.21 exclusive fix)
- Z-fighting on copycat sliding and folding doors 

## 3.0.0 - 2025-04-25

### Added

- Support for Create 6.0.x
- EMI Support (You need to rejoin your world/server for it to update hidden features currently. This is due to EMI not having an option to update the shown items on the go)

### Removed

- Support for Create 0.5.x
- Support for 1.18.2 and 1.19.2 versions of the mod

## 2.2.2 - 2025-03-09

#### NOTICE:
With this version, 1.19.2 and 1.18.2 have become EOL. This means that unless there is a major bug they will no longer receive updates, and we will be focusing on 1.20.1 and 1.21.1 versions of Create only.

### Changed

- In this update we changed how we manage the 
block shapes to optimize the lag when assembling/disassembling contraptions. During our testing we managed to optimize
it by an avg ~56%. There will still be a bit of lag, but it should be nowhere near as bad, and we will continue to optimize it more in future versions(maintained versions).

## 2.2.1 - 2025-03-03

### Fixed

- Fixed copycat sliding and folding doors losing their upper half textures when assembled on contraptions

## 2.2.0 - 2025-02-04

### Added

- **Universal CT System**
  - All copycats now connect textures with each other as long as they have a matching block face
- Copycat byte panels
  - A quarter of a panel. 4 of which forms a full panel
- Copycat corner slices
  - Resizable slices to go with slices and vertical slices
- Copycat sliding/folding doors
  - Animated doors with separate upper and lower textures. CT can be toggled for each half
- Copycat panes
  - Connects to iron bars, glass panes and Copycat flat panes
- Copycat flat panes
  - Connects to panes and can be placed horizontally
- Vertical/Stacked half layers
  - Rotated versions of Copycat half layers

### Fixed

- Fixed feature toggle not disabling crafting recipes correctly
- Fixed disabled items still being searchable in inventory
- Fixed copycat doors not being placed correctly by schematics
- Fixed copycat doors disappearing when disassembled from a contraption
- Fixed a crash caused by incompatibility with newer versions of Additional Placements
- Fixed a performance issue caused by uncached ResourceLocations
- Reduced the resolution of slope hitboxes to reduce lag on contraptions
- Added a filter to hide non-critical errors related to compatibility mixins
- Added a missing description for copycat walls
- Fixed compatibility with Diagonal Fences/Windows/Walls
- Fixed a crash due to malformed NBT data in copycat bytes
- Fixed copycat slabs losing their textures when rotated
- Fixed copycat vertical stairs not updating their shapes correctly
- Fixed copycat panels (from base Create) not connecting textures to regular blocks
- Fixed a potential crash with Continuity
- Fixed a crash when tooltip translations are malformed

## 2.1.4 - 2024-09-15

### Fixed

- Fixed an incompatibility with Immersive Engineering due to block states being initialized before the block registry

## 2.1.3 - 2024-09-15

### Changed

- Multistate copycats can now be mined with both pickaxes and axes

### Fixed

- Fixed light sources in copycats again to eliminate thread deadlocks and restore compatibility with Starlight
- Fixed schematicannon sometimes requiring air to build multistate copycats
- Fixed copycat slabs not connecting to full blocks
- Added compatibility with More Culling
- Fixed occlusion for brackets
- Fixed the inner face of copycat doors when one side of the door is transparent but the other isn't
- Fixed connected textures on the top face of copycat stairs
- Added a workaround for schematicannons not getting the same stack of resources from two separate inventories
- Fixed a crash in copycat migration when block entities from other mods are not properly registered
- Fixed copycat slabs being transformed incorrectly in schematics

## 2.1.2 - 2024-08-03

### Fixed

- Fixed stack overflow issue with the lighting patch when starlight is loaded
- Fixed block transforms on a few blocks

## 2.1.1 - 2024-08-02

### Changed

- All copycats no longer get destroyed by flowing water

### Fixed

- Copycats retaining their textures when broken and placed back down

## 2.1.0 - 2024-08-01

### Changed

- Opted into Neruina's auto report system
- The migration config option has been reset so that migration is on by default from this version onwards
- The item models for Copycat Door and Copycat Iron Door are now 3D
- In addition to Copycat Shafts, Cogwheels and Large Cogwheels, Copycat Trapdoors, Doors, Fences, Fence Gates, Ladders, Pressure Plates and Walls now accept their respective non-copycat counterparts as material
- All copycats now support mirroring and 180-degree rotations. Those with vertical variants also support 90-degree rotations

### Fixed

- **Copycats not emitting light properly when a light source is used as material**
- The 2.0 update sale is over. You now need to pay the full material price when building copycats with a schematicannon
- Incorrect face culling when sodium is installed
- Incompatibility with Diagonal Walls by disabling DW on Copycat Walls
- Stack overflow crash when getting the destroy progress of a multi-state copycat
- Crash when the multi-state copycat block entity isn't removed when the block no longer exists
- Stack overflow crash when computing block friction/enchantment power for multi-state copycats
- Incompatibility with Dynamic Crosshair due to state for placement being null
- Crash due to case conversion with non-default locale
- Incompatibility with Embeddium on Fabric due to missing Indium classes
- Incorrect accessibility on the public `StateType` API
- Face hiding not working in specific configurations
- Multi-state copycats consuming too many items when they are filled by holding the material in the offhand
- Incorrect footsteps when walking on multi-state copycats (Forge only)
- Incorrect particles when running on multi-state copycats
- Bracket-able copycats not dropping the bracket item when broken
- Bracket-able copycats not requiring a bracket item when placed with a schematicannon
- Horizontal connected textures not working for Create's pillar blocks
- Copycats not craftable in Fabric 1.18 due to incorrect item tags

## 2.0.5 - 2024-07-21

### Fixed

- Copycat Slabs not having connected textures when using Fusion CT
- Crash/issues with train disassembly when certain complex blocks are put inside copycats on Forge
- Crash when simple copycats are put near enchanting tables on Fabric
- Bookshelves in copycats not contributing to enchanting power on Fabric
- Incorrect block mirroring for Copycat Slabs, Vertical Stairs and various other copycats
- Incorrect block shape for upside-down Copycat Half Layers
- Incompatibility with Continuity (not available until next Continuity release)

## 2.0.4 - 2024-07-20

### Fixed

- Incorrect face occlusion for Copycat Ladder
- Incorrect link for Forge's update checker
- Crash when Sinytra is installed due to late config initialization

## 2.0.3 - 2024-07-20

### Changed

- Allow Shafts and Cogwheels to be put inside Copycat Shafts and Cogwheels for their original look
- Changed the icon of the functional tab to a Copycat Cogwheel

### Fixed

- Copycat Fluid Pipes turning into normal pipes when being wrenched
- Pillar blocks having incorrect connected textures
- Incorrect occlusion of the inner faces of Copycat Half Layer
- Schematics from old versions of Copycats+ losing their NBT data
- Incompatibility with Create: Extended Cogwheels
- Train disassembly not working with multi-state copycats
- Crash when multi-state copycats are moved by mechanical pistons
- Crash when copycats are fired from a schematicannon

## 2.0.2 - 2024-07-18

### Fixed

- Re-release of the 2.0.1 hotfix version for 1.18.2 and 1.19.2

## 2.0.1 - 2024-07-18

### Fixed

- Fixed class loading issue on servers.
- Fixed compatibility with Create: The Factory Must Grow

## 2.0.0 - 2024-07-18

### Added

- Copycat Door
  - Allows different materials for top and bottom halves
- Copycat Iron Trapdoor/Door
  - Can only be opened with redstone
- Copycat Fluid Pipe
  - Can be wrenched to add a window
  - Accepts transparent materials to display the fluid inside
- Copycat Cogwheel/Large Cogwheel
  - Allows different materials for the shaft and the gear
- A new accessibility option to colorize multi-state copycats
- Conversion recipes between horizontal and vertical copycats
- A shortcut key (left alt by default) to fill all parts of a multi-state copycat with the held item

### Changed

- **Major internal rewrite**
    - For developers, please refer to [the wiki](https://github.com/copycats-plus/copycats/wiki/v2.0-Refactor) for a summary of changes. Most code in the `foundation` package is also documented.
- Optimized rendering on both platforms
- Separated creative tabs for decorative and functional copycats
- Re-ordered items in the creative tab for easy access
- Reverted the shape of Copycat Ladder to match vanilla ladders
- Reworked tooltips to be more concise and removed duplicate strings
- Reworked multi-state copycats to no longer consume items when the same material is already present
- Improved block outlines for Copycat Vertical Slope and Copycat Slope Layers

### Fixed

- Inconsistent connected textures on various copycats on Fabric and in 1.18.2
- Inconsistent behavior of CT toggle across versions
- Multi-state copycats not rotating properly until a block update is received
- Face culling not working for all copycats
- Transparent face hiding not working for multi-state copycats
- Transparent face hiding not working between copycats of different types
- Potentially buggy quad lighting on Forge
- Buggy connected textures between two Copycat Slabs with different orientations
- Placement helpers not rendering for various copycats
- Incorrect/missing destroy particles in various copycats
- Crash due to null pointer exception when migrating old copycats to multi-state copycats
- Incompatibility with Athena's connected textures
- Incompatibility with Indium on Fabric
- Copycat Trapdoor not climbable when above a ladder
- Various block interactions due to Copycat Slopes not having a full face on the back
- Crash due to biomes not being accessible in a superflat world on Fabric
- Buggy quad lighting on Copycat Slopes on Fabric

## 1.3.8 - 2024-06-28

### Fixed

- Re-release of the 1.3.7 hotfix version

## 1.3.7 - 2024-06-28

### Fixed

- Fixed crash issue on servers related to BlockColors

## 1.3.6 - 2024-06-27

### Fixed

- Fixed a crash when copycats from other mods interact with multi-state copycats

## 1.3.5 - 2024-06-27

### Fixed

- Fixed overflow issue with explosions on forge

### Added

- Added Vertical Slopes (better hitbox to come)
- Added Slope Layers (better hitbox to come)
- Added Copycat Shaft

## 1.3.4 - 2024-06-19

### Fixed

- Multistate copycats having much lower friction than normal

## 1.3.3 - 2024-06-18

### Fixed

- Fixed players getting stuck on multistate copycats or if a multistate copycat was underneath a small hitbox like stairs
- Fix crash issue when mods like continuity are installed

### Changed

- Made the block conversion of copycats that are now multistate copycats enabled by default

## 1.3.2 - 2024-06-17

### Added

- **Vertical stairs**
- The Copy Cat (again)
- Migration for multistate copycats that are assembled on contraptions
- Numerous translations in multiple languages thanks to our translators on Crowdin

### Changed

- Copycat boards now display each face in full, without a 1-pixel padding

### Fixed

- Incorrect sounds when multistate blocks are wrenched
- Block picking with mouse middle button not working for multistates

## 1.3.1 - 2024-06-14

### Fixed

- Fixed tag issue breaking normal block breaking with pickaxes and axes

## 1.3.0 - 2024-06-14
### Changed

- Bytes, boards, slabs and half layers are now MultiStates

### Added

- An easier way for mods adding compat to easily get a set of all our blocks and tell if they are multistates easily
- Connected textures on the multistate blocks
- Optional conversion system for blocks that have changed (newly placed blocks will only ever be multistates)
- Made blocks that are now multistates retain their look when converting to a multistate (until wrenched or broken)

## 1.2.7 - 2024-04-08

### Added

- Copycat Ladder - A ladder that can be made to look like any block and can be placed without needing a block behind it
- Copycat Ghost Block - A full size block that has no collision meaning it can be walked/fallen through

## 1.2.6 - 2024-03-30

### Changed

- Made the mod support version 0.5.1.d+ because im tired of people asking XD


## 1.2.5 - 2024-03-27

### Fixed
- Fixed crashing issue with copycats on 1.18.2 Forge
- Slab dupe issue with placement helper and double slabs


## 1.2.4 - 2024-03-16

### Fixed
- Fixed culling issue when used with rubidium mod

## 1.2.3 - 2024-03-15

### Fixed
- Fixed potential crash with the fusion mod on some of the copycats

## 1.2.2 - 2024-03-15

### Fixed
- Fixed potential crash issue with fabric servers caused by one of the mixins
- Fixed recipes using the wrong tag for zinc meaning recipes were broken (fabric)

## 1.2.1 - 2024-03-13

### Added
- Fixed crash issues on fabric caused by mixins


## 1.2.0 - 2024-03-13

### Added
- Compatibility for Create 0.5.1-f (Fabric)

### Changed
 - Project is now a multiloader project allowing us to update easier and quicker aswell as add new blocks to both platforms.

## 1.0.4 - 2024-02-10

### Added

- Copycat Half Layer
- Item tags for future compatibility with Create: Connected

### Fixed

- Incompatibility with Additional Placements

## 1.0.3 - 2024-02-09

### Added

- Copycat Wooden/Stone Buttons
- Copycat Wooden/Stone Pressure Plates
- Copycat Light/Heavy Weighted Pressure Plates

## 1.0.2 - 2024-02-09

### Added

- Fabric port
- Sneak-R-click to toggle connected textures on copycats
- Half Panels and Slices from Create: Copies and Cats
- Enhanced models for Copycat Stairs
- Translations (thanks Crowdin translators!)
- The Copy Cat

### Changed

- Mod logo

### Fixed

- Some of the connected texture glitches on Copycat Board and Copycat Stairs
- Crash when placing copycats from base Create

## 1.0.1 - 2024-01-30

### Added

- Layers from Create: More Copycats (Only block that wasn't really here already in some form)

## 1.0.0 - 2024-01-29

### Added

- All copycats from Create: Connected
