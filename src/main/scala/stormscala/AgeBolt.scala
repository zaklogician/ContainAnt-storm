package stormscala

import org.apache.storm.topology.BasicOutputCollector
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseBasicBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Tuple
import org.apache.storm.tuple.Values

class AgeBolt extends BaseBasicBolt {

  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    val id = input.getLongByField("id")
    val ghash = (1664525*id + 1013904223) >> 4
    val age: Long = (ghash % 30) + 18
    collector.emit( new Values(
      new java.lang.Long(id), new java.lang.Long(age)
    ))
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare( new Fields("id", "age") )
  }

}
