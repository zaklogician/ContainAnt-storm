package stormscala

// boilerplate for SMAC verification

object SkewedRollingTopWordsSMAC {

  var wordSpoutCPULoad: Int = 0
  var wordSpoutMemoryLoadOnHeap: Int = 0
  var wordSpoutMemoryLoadOffHeap: Int = 0
  var wordSpoutParallelismHint: Int = 1

  var counterBoltCPULoad: Int = 0
  var counterBoltMemoryLoadOnHeap: Int = 0
  var counterBoltMemoryLoadOffHeap: Int = 0
  var counterBoltParallelismHint: Int = 1

  var aggBoltCPULoad: Int = 0
  var aggBoltMemoryLoadOnHeap: Int = 0
  var aggBoltMemoryLoadOffHeap: Int = 0
  var aggBoltParallelismHint: Int = 1

  var intermediateRankerBoltCPULoad: Int = 0
  var intermediateRankerBoltMemoryLoadOnHeap: Int = 0
  var intermediateRankerBoltMemoryLoadOffHeap: Int = 0
  var intermediateRankerBoltParallelismHint: Int = 1

  var totalRankerBoltCPULoad: Int = 0
  var totalRankerBoltMemoryLoadOnHeap: Int = 0
  var totalRankerBoltMemoryLoadOffHeap: Int = 0
  var totalRankerBoltParallelismHint: Int = 1

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

    case "-wordSpoutCPULoad" :: value :: rest => { wordSpoutCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-wordSpoutMemoryLoadOnHeap" :: value :: rest => { wordSpoutMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-wordSpoutMemoryLoadOffHeap" :: value :: rest => { wordSpoutMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-wordSpoutParallelismHint" :: value :: rest => { wordSpoutParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }

    case "-counterBoltCPULoad" :: value :: rest => { counterBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-counterBoltMemoryLoadOnHeap" :: value :: rest => { counterBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-counterBoltMemoryLoadOffHeap" :: value :: rest => { counterBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-counterBoltParallelismHint" :: value :: rest => { counterBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }

    case "-aggBoltCPULoad" :: value :: rest => { aggBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-aggBoltMemoryLoadOnHeap" :: value :: rest => { aggBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-aggBoltMemoryLoadOffHeap" :: value :: rest => { aggBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-aggBoltParallelismHint" :: value :: rest => { aggBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }

    case "-intermediateRankerBoltCPULoad" :: value :: rest => { intermediateRankerBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-intermediateRankerBoltMemoryLoadOnHeap" :: value :: rest => { intermediateRankerBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-intermediateRankerBoltMemoryLoadOffHeap" :: value :: rest => { intermediateRankerBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-intermediateRankerBoltParallelismHint" :: value :: rest => { intermediateRankerBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }

    case "-totalRankerBoltCPULoad" :: value :: rest => { totalRankerBoltCPULoad = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-totalRankerBoltMemoryLoadOnHeap" :: value :: rest => { totalRankerBoltMemoryLoadOnHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-totalRankerBoltMemoryLoadOffHeap" :: value :: rest => { totalRankerBoltMemoryLoadOffHeap = Math.round( parseDouble(value) ); parseArgs(rest) }
    case "-totalRankerBoltParallelismHint" :: value :: rest => { totalRankerBoltParallelismHint = Math.round( parseDouble(value) ); parseArgs(rest) }

    case "-maxTaskParallelism" :: value :: rest => { maxTaskParallelism = Math.round( parseDouble(value) ); parseArgs(rest) }
    case unknown :: rest => parseArgs(rest)
    case Nil => { Unit }
  }
  
  def main(args: Array[String]): Unit = {
    parseArgs(args(0).split(" ").toList)
    val config = SkewedRollingTopWordsConfig(
      FBolt( wordSpoutCPULoad, wordSpoutMemoryLoadOnHeap,
             wordSpoutMemoryLoadOffHeap, wordSpoutParallelismHint),
      FBolt( counterBoltCPULoad, counterBoltMemoryLoadOnHeap,
             counterBoltMemoryLoadOffHeap, counterBoltParallelismHint),
      FBolt( aggBoltCPULoad, aggBoltMemoryLoadOnHeap,
             aggBoltMemoryLoadOffHeap, aggBoltParallelismHint),
      FBolt( intermediateRankerBoltCPULoad, intermediateRankerBoltMemoryLoadOnHeap,
             intermediateRankerBoltMemoryLoadOffHeap, intermediateRankerBoltParallelismHint),
      FBolt( totalRankerBoltCPULoad, totalRankerBoltMemoryLoadOnHeap,
             totalRankerBoltMemoryLoadOffHeap, totalRankerBoltParallelismHint),
      maxTaskParallelism
    )
    val quality: Double = SkewedRollingTopWordsTopology.runConfig(config)
    System.err.println("Fitness: " + quality)
    println("Result of algorithm run: SUCCESS, 0, 0, " + (1/quality) + ", 0")
  }
  
}
