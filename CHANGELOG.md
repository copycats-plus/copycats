# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
