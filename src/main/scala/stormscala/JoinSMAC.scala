package stormscala

// boilerplate for SMAC verification

object JoinSMAC {
  
  var spoutCPULoad: Int = 0
  var spoutMemoryLoadOnHeap: Int = 0
  var spoutMemoryLoadOffHeap: Int = 0
  var spoutParallelismHint: Int = 1
  var genderBoltCPULoad: Int = 0
  var genderBoltMemoryLoadOnHeap: Int = 0
  var genderBoltMemoryLoadOffHeap: Int = 0
  var genderBoltParallelismHint: Int = 1
  var ageBoltCPULoad: Int = 0
  var ageBoltMemoryLoadOnHeap: Int = 0
  var ageBoltMemoryLoadOffHeap: Int = 0
  var ageBoltParallelismHint: Int = 1
  var maxTaskParallelism: Int = 1

  def parseDouble(xs: String): Float = {
    var result = 0.0
    try {
      val tmp = xs.filter(_ != '\'')
      result = xs.filter(_ != '\'').toDouble
    } catch { case (_: Throwable) => { } }
    result.toFloat
  }

  def parseArgs(list: List[String]): Unit = list match {
    case "-spoutCPULoad" :: value :: rest => { spoutCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-spoutMemoryLoadOnHeap" :: value :: rest => { spoutMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-spoutMemoryLoadOffHeap" :: value :: rest => { spoutMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-spoutParallelismHint" :: value :: rest => { spoutParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-genderBoltCPULoad" :: value :: rest => { genderBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-genderBoltMemoryLoadOnHeap" :: value :: rest => { genderBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-genderBoltMemoryLoadOffHeap" :: value :: rest => { genderBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-genderBoltParallelismHint" :: value :: rest => { genderBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-ageBoltCPULoad" :: value :: rest => { ageBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-ageBoltMemoryLoadOnHeap" :: value :: rest => { ageBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-ageBoltMemoryLoadOffHeap" :: value :: rest => { ageBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-ageBoltParallelismHint" :: value :: rest => { ageBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-maxTaskParallelism" :: value :: rest => { maxTaskParallelism = Math.round( parseDouble(value) ); parseArgs(rest) }
    case unknown :: rest => parseArgs(rest)
    case Nil => { Unit }
  }
  
  def main(args: Array[String]): Unit = {
    parseArgs(args(0).split(" ").toList)
    val config = JoinConfig(
      FBolt( spoutCPULoad,spoutMemoryLoadOnHeap,
             spoutMemoryLoadOffHeap,spoutParallelismHint),
      FBolt( genderBoltCPULoad, genderBoltMemoryLoadOnHeap,
             genderBoltMemoryLoadOffHeap, genderBoltParallelismHint),
      FBolt( ageBoltCPULoad, ageBoltMemoryLoadOnHeap,
             ageBoltMemoryLoadOffHeap, ageBoltParallelismHint),
      maxTaskParallelism
    )
    val quality: Double = JoinTopology.runConfig(config)
    System.err.println("Fitness: " + quality)
    println("Result of algorithm run: SUCCESS, 0, 0, " + (1/quality) + ", 0")
  }
  
}
