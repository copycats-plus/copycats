# Modeling for Copycats

Copycat models work differently from ordinary block models because they change their textures on the fly. Special
considerations need to be made when you want your model to function as a copycat:

- No custom textures, only the unmodified `copycat_base` texture provided by Create can be used.
- No uv rotation or custom uv sizes. You have to use the uv size given by Auto UV, which means it has to match the
  actual quad size.
- No relying on different connective states for connected textures.
    - Custom connected texture logic is handled in code, and it affects every face on the entire model. There cannot be
      parts in a model with different CT states, and they cannot appear connected if there are no adjacent blocks to
      connect to.
- UVs on different faces of the same element must preserve their relative location to each other.
    - Basically, this means that the element must appear to be cut from the same corner of a block on all faces, so you
      cannot have an element which appears to come from the top of a block on one side, and the bottom of a block on the
      opposite side.