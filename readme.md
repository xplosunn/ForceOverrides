# ForceOverrides

[Scalafix](https://github.com/scalacenter/scalafix) rule to force explicit overriding.

Given the following example:

```scala
trait Printer {
  def print(s: String): Unit
}

object StdOut extends Printer {
  def print(sx: String): Unit = System.out.print(sx)
}
```

This rule re-writes the code to:

```scala
trait Printer {
  def print(s: String): Unit
}

object StdOut extends Printer {
  override def print(sx: String): Unit = System.out.print(sx)
}
```

## Usage

`ThisBuild / scalafixDependencies += "com.github.xplosunn" %% "ForceOverrides" % "0.0.1"`
