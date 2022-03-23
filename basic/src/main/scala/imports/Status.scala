package imports

enum Status:
  case ONE, TWO
  import Status.*
  def f: String = lala

object Status:
  val s = ONE
  val lala = "Lala"


