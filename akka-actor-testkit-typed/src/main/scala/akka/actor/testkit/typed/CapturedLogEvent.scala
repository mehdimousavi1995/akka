/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.actor.testkit.typed

import java.util.Optional

import akka.actor.typed.LogMarker
import akka.annotation.InternalApi
import akka.event.Logging.LogLevel
import akka.util.OptionVal

import scala.collection.JavaConverters._
import scala.compat.java8.OptionConverters._

/**
 * Representation of a Log Event issued by a [[akka.actor.typed.Behavior]]
 */
final case class CapturedLogEvent(
  logLevel: LogLevel,
  message:  String,
  cause:    Option[Throwable],
  marker:   Option[LogMarker],
  mdc:      Map[String, Any]) {

  /**
   * Constructor for Java API
   */
  def this(logLevel: LogLevel, message: String, errorCause: Optional[Throwable], marker: Optional[LogMarker], mdc: java.util.Map[String, Any]) {
    this(logLevel, message, errorCause.asScala, marker.asScala, mdc.asScala.toMap)
  }

  def getMdc: java.util.Map[String, Any] = mdc.asJava

  def getErrorCause: Optional[Throwable] = cause.asJava

  def getLogMarker: Optional[LogMarker] = marker.asJava
}

object CapturedLogEvent {

  /**
   * Helper method to convert [[OptionVal]] to [[Option]]
   */
  private def toOption[A](optionVal: OptionVal[A]): Option[A] = optionVal match {
    case OptionVal.Some(x) ⇒ Some(x)
    case _                 ⇒ None
  }

  def apply(
    logLevel: LogLevel,
    message:  String,
    cause:    Option[Throwable] = None,
    marker:   Option[LogMarker] = None,
    mdc:      Map[String, Any]  = Map.empty): CapturedLogEvent = {
    new CapturedLogEvent(logLevel, message, cause, marker, mdc)
  }

  /**
   * Auxiliary constructor that receives Akka's internal [[OptionVal]] as parameters and converts them to Scala's [[Option]].
   * INTERNAL API
   */
  @InternalApi
  private[akka] def apply(
    logLevel:   LogLevel,
    message:    String,
    errorCause: OptionVal[Throwable],
    logMarker:  OptionVal[LogMarker],
    mdc:        Map[String, Any]): CapturedLogEvent = {
    new CapturedLogEvent(logLevel, message, toOption(errorCause), toOption(logMarker), mdc)
  }
}
