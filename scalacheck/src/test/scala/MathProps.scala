import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object MathProps extends Properties("Math"):
  property("max") = forAll { (x: Int, y: Int) =>
    val z = java.lang.Math.max(x, y)
    (z == x || z == y) && z >= x && z >= y
  }
