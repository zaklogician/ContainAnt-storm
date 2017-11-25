package stormscala

/** In this experiment, the goal is maximizing throughput 
 *  of an Apache Storm join topology running on a local cluster.
 */

import com.containant._
import com.containant.heuristics._

object CS6SkewedRollingTopWordsTopology {
  //////////////////////////////////////////////////////////////////////
  // Configuration
  val _seed: Int = 0xDEADBEEF
  val _runs: Int = 2
  
  object Hmma extends AntHeuristic {
    override val _evaporationRate: Double = 0.4
    override val _iterations: Int = 20
    override val _antNumber: Int = 0
    override val _minimumFraction: Double = 0.10
    override val _recursionDepth: Int = 5
    override val RNG: java.util.Random = new java.util.Random(_seed)
    override def toString: String = "mma"
  }
  
  object Hgre extends GrEvoHeuristic {
    override val _population: Int = 10
    override val _length: Int = 16
    override val _maxChoice: Int = 4
    override val _tournamentSize = 4
    override val _generations = 2
    override val _recursionDepth = 6
    override val RNG: java.util.Random = new java.util.Random(_seed)
    override def toString: String = "gre"
  }

  object Hran extends RandomHeuristic {
    override val _iterations = 20
    override val _recursionDepth = 5
    override val RNG: java.util.Random = new java.util.Random(_seed)
    override def toString: String = "ran"
  }
  
  object Hmct extends MCTSHeuristic {
    override val _iterations = 20
    override val RNG: java.util.Random = new java.util.Random(_seed)
    override def toString: String = "mct"
  }

  //////////////////////////////////////////////////////////////////////
  // Experiment Details


  def main(args: Array[String]): Unit = {
    import com.containant.casestudies.Framework
    println("\n-----------------------------")
    println("Case Study 6: Apache Storm - Skewed Rolling Topology")
    println("Runs: " + _runs)
    
    val comparison =
      Framework.experimentN[SkewedRollingTopWordConfig](Seq(Hmct, Hgre, Hmma),_runs, SkewedRollingTopWordModule, SkewedRollingTopWordTopology.runConfig)
    
    val reference =
      Framework.experiment[SkewedRollingTopWordConfig](Hran, Hran, _runs, SkewedRollingTopWordModule, SkewedRollingTopWordTopology.runConfig)
    
    println("heuristic,min,mean,max,var")
    for(i <- 0 until comparison.heuristics.size) {
      println(comparison.summaries(i))
    }
    println(reference.summaries(0))
    println("pvalues: " + comparison.pvalues)
    println(if (6*comparison.pvalues.max < 0.05) "significant" else "not significant")
    println()
  }
  
}
