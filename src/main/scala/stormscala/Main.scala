package stormscala 

import java.util.concurrent.TimeUnit

import org.apache.storm.Config
import org.apache.storm.LocalCluster
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.topology.base.BaseWindowedBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.bolt.JoinBolt

object Main {

  def main(args: Array[String]): Unit = {
    // SkewedRollingTopWordsTopology.main(args)
    CS6JoinTopology.main(args)
  }
}


