(ns v8.test-core
  (:require [v8.core :as v8])
  (:use [midje.sweet]))

(fact "run-script works"
  (v8/run-script "123") => "123")

(fact "run-script works"
  (v8/run-script "readFile('test/v8/test.txt');") => "123\n")

(fact "simple expressions"
  (v8/run-script "3/0") => "Infinity")

(fact "complex code works"
  (v8/run-script "(function(){ return 5; })();") => "5")

(fact "Unicode won't ☔ on my parade"
  (v8/run-script "\"šećiđon☔\"") => "šećiđon☔")

(fact "syntax error doesnt die"
  (v8/run-script "sjd2-23=dfv;2-") => (throws Exception))

(fact "multuple scripts in parallel work"
  (pmap v8/run-script (repeat 20 "(function(){ return 5; })();")) => (repeat 20 "5"))

(fact "running two scripts on same context"
  (let [cx (v8/create-context)]
    (v8/run-script-in-context cx "x = 17; y = {a: 6};")
    (v8/run-script-in-context cx "x;") => "17"
    (v8/run-script-in-context cx "y.a;") => "6"))
