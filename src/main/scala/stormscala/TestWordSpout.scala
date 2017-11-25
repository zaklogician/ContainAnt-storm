package stormscala

import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Values

import scala.util.Random

class TestWordSpout extends BaseRichSpout {
  var _collector: SpoutOutputCollector = _
  var _rand: Random = _
  
  val words: Seq[String] = Seq("nathan", "mike", "jackson", "golda", "bertels")
  
  override def nextTuple: Unit = {
    Thread.sleep(75)
    val word = words(_rand.nextInt(words.size))
    _collector.emit( new Values( word ) )
  }

  override def open(conf: java.util.Map[_, _], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    _collector = collector
    _rand = new Random(0xDEADBEEF)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("word"))
  }
}

// End ///////////////////////////////////////////////////////////////
